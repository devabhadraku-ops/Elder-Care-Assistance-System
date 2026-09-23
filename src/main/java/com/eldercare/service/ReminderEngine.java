package com.eldercare.service;

import com.eldercare.models.ActivityReminder;
import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Sends activity reminders (medicine, meals, exercise...) at the right time.
 *
 * How it works:
 *  - checkReminders(now) looks at every active reminder that is not completed.
 *  - The 1st reminder is sent at the reminder time, the 2nd 5 minutes later,
 *    the 3rd 10 minutes later. The number already sent is stored by
 *    ReminderService.notifyReminder(), so it survives a restart.
 *  - When the 3rd reminder goes out and the elderly person still has not
 *    confirmed, the caregivers are told.
 *  - markComplete(...) confirms a reminder and tells the caregivers.
 *  - snooze(...) delays the next reminder.
 *
 * checkReminders takes the time as a parameter so it can be tested without
 * waiting. startAutoCheck() runs it every minute with the real time.
 *
 * Known limits: every reminder is treated as DAILY, and "completed" is not
 * reset overnight yet (that needs one new database method).
 */
public class ReminderEngine {

    public static final int REPEAT_MINUTES = 5;      // gap between reminders
    public static final int MAX_REMINDERS = 3;       // then tell the caregiver
    public static final int MAX_LATE_MINUTES = 60;   // ignore very old reminders

    private final ReminderService reminderService;
    private final ElderlyService elderlyService;
    private final CaregiverService caregiverService;
    private final NotificationService notificationService;

    // reminder id -> time until which it is snoozed (in memory only)
    private final Map<Integer, LocalTime> snoozedUntil = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler;

    public ReminderEngine() {
        this(new ReminderService(), new ElderlyService(),
                new CaregiverService(), new NotificationService());
    }

    // Constructor that lets tests pass in their own services
    public ReminderEngine(ReminderService reminderService,
                          ElderlyService elderlyService,
                          CaregiverService caregiverService,
                          NotificationService notificationService) {
        this.reminderService = reminderService;
        this.elderlyService = elderlyService;
        this.caregiverService = caregiverService;
        this.notificationService = notificationService;
    }

    /**
     * Sends every reminder that is due at the given time.
     * @return how many reminders were sent
     */
    public int checkReminders(LocalTime now) {
        int sent = 0;
        List<ActivityReminder> reminders;

        try {
            reminders = reminderService.getActiveReminders();
        } catch (Exception e) {
            System.out.println("Could not load reminders: " + e.getMessage());
            return 0;
        }

        for (ActivityReminder reminder : reminders) {
            try {
                if (isDue(reminder, now)) {
                    sendReminder(reminder);
                    sent++;
                }
            } catch (Exception e) {
                System.out.println("Problem with reminder "
                        + reminder.getReminderId() + ": " + e.getMessage());
            }
        }
        return sent;
    }

    /** Elderly person says "done". Tells the caregivers. */
    public boolean markComplete(int reminderId) {
        ActivityReminder reminder = reminderService.getReminderById(reminderId);
        if (reminder == null) {
            return false;
        }
        if (!reminderService.completeReminder(reminderId)) {
            return false;
        }
        snoozedUntil.remove(reminderId);
        notifyCaregivers(reminder.getElderlyId(),
                nameOf(reminder.getElderlyId()) + " completed: "
                        + label(reminder) + " - " + reminder.getDescription());
        return true;
    }

    /** Delay the next reminder by the given number of minutes. */
    public void snooze(int reminderId, int minutes, LocalTime now) {
        snoozedUntil.put(reminderId, now.plusMinutes(minutes));
        System.out.println("Reminder " + reminderId + " snoozed for "
                + minutes + " minutes");
    }

    public void snooze(int reminderId, int minutes) {
        snooze(reminderId, minutes, LocalTime.now());
    }

    /** Checks the reminders every minute in the background. */
    public void startAutoCheck() {
        if (scheduler != null) {
            return;
        }
        scheduler = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "reminder-checker");
            thread.setDaemon(true);   // does not stop the app from closing
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkReminders(LocalTime.now());
            } catch (Exception e) {
                System.out.println("Reminder check failed: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
        System.out.println("Reminder checker started (runs every minute)");
    }

    public void stopAutoCheck() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    // ---------------------------------------------------------------

    boolean isDue(ActivityReminder reminder, LocalTime now) {

        if (reminder.isCompleted() || !reminder.isActive()) {
            return false;
        }
        if (reminder.getNotificationCount() >= MAX_REMINDERS) {
            return false;
        }

        LocalTime snoozeEnd = snoozedUntil.get(reminder.getReminderId());
        if (snoozeEnd != null && now.isBefore(snoozeEnd)) {
            return false;
        }

        long minutesSince = ChronoUnit.MINUTES.between(reminder.getReminderTime(), now);
        if (minutesSince < 0 || minutesSince > MAX_LATE_MINUTES) {
            return false;
        }

        // 1st reminder at +0 min, 2nd at +5, 3rd at +10
        return minutesSince >= (long) reminder.getNotificationCount() * REPEAT_MINUTES;
    }

    private void sendReminder(ActivityReminder reminder) {

        int number = reminder.getNotificationCount() + 1;
        String name = nameOf(reminder.getElderlyId());

        notificationService.sendAppNotification(name,
                "Reminder " + number + " of " + MAX_REMINDERS + " - time for "
                        + label(reminder) + ": " + reminder.getDescription());

        reminderService.notifyReminder(reminder.getReminderId());

        if (number >= MAX_REMINDERS) {
            notifyCaregivers(reminder.getElderlyId(),
                    name + " has not confirmed " + label(reminder) + " ("
                            + reminder.getDescription() + ") after "
                            + MAX_REMINDERS + " reminders");
        }
    }

    private void notifyCaregivers(int elderlyId, String message) {
        try {
            for (Caregiver caregiver : caregiverService.getAvailableCaregivers()) {
                notificationService.sendAppNotification(caregiver.getName(), message);
            }
        } catch (Exception e) {
            System.out.println("Could not notify caregivers: " + e.getMessage());
        }
    }

    private String nameOf(int elderlyId) {
        try {
            Elderly elder = elderlyService.getElderlyById(elderlyId);
            if (elder != null) {
                return elder.getName();
            }
        } catch (Exception e) {
            // fall through
        }
        return "Elderly #" + elderlyId;
    }

    private String label(ActivityReminder reminder) {
        return reminder.getActivityType().toUpperCase();
    }
}
