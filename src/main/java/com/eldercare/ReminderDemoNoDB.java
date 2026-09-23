package com.eldercare;

import com.eldercare.models.ActivityReminder;
import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Reminder demo that needs NO database. The clock is simulated, so you can
 * see a whole day of reminders in a second.
 */
public class ReminderDemoNoDB {

    public static void main(String[] args) {

        // ----- test data kept in memory -----
        Elderly elder = new Elderly("Ramesh Kumar", 72, "9000000001",
                LocalDate.of(1954, 3, 10));
        elder.setElderlyId(1);

        Caregiver caregiver = new Caregiver("Anitha Nurse", "6000000001");
        caregiver.setCaregiverId(1);

        List<ActivityReminder> reminders = new ArrayList<>();
        reminders.add(makeReminder(1, "medication", LocalTime.of(8, 0), "Take aspirin with water"));
        reminders.add(makeReminder(2, "meal", LocalTime.of(12, 0), "Have lunch"));
        reminders.add(makeReminder(3, "exercise", LocalTime.of(15, 0), "Walk for 20 minutes"));

        // ----- in-memory replacements for the database services -----
        ReminderService reminderService = new ReminderService() {
            @Override
            public List<ActivityReminder> getActiveReminders() {
                return reminders;
            }

            @Override
            public ActivityReminder getReminderById(int id) {
                for (ActivityReminder r : reminders) {
                    if (r.getReminderId() == id) {
                        return r;
                    }
                }
                return null;
            }

            @Override
            public boolean notifyReminder(int id) {
                ActivityReminder r = getReminderById(id);
                if (r == null) {
                    return false;
                }
                r.markNotified();
                return true;
            }

            @Override
            public boolean completeReminder(int id) {
                ActivityReminder r = getReminderById(id);
                if (r == null) {
                    return false;
                }
                r.markCompleted();
                return true;
            }
        };

        ElderlyService elderlyService = new ElderlyService() {
            @Override
            public Elderly getElderlyById(int id) {
                return id == 1 ? elder : null;
            }
        };

        CaregiverService caregiverService = new CaregiverService() {
            @Override
            public List<Caregiver> getAvailableCaregivers() {
                return List.of(caregiver);
            }
        };

        ReminderEngine engine = new ReminderEngine(reminderService,
                elderlyService, caregiverService, new NotificationService());

        // ----- a simulated day -----
        System.out.println("MEDICINE: elderly person ignores it");
        check(engine, 7, 59);    // too early
        check(engine, 8, 0);     // 1st reminder
        check(engine, 8, 5);     // 2nd reminder
        check(engine, 8, 10);    // 3rd reminder + caregiver told
        check(engine, 8, 15);    // nothing more is sent

        System.out.println("\nLUNCH: elderly person confirms");
        check(engine, 12, 0);    // 1st reminder
        System.out.println("Ramesh presses DONE");
        engine.markComplete(2);  // caregiver told
        check(engine, 12, 5);    // nothing - already done

        System.out.println("\nEXERCISE: elderly person snoozes");
        check(engine, 15, 0);    // 1st reminder
        System.out.println("Ramesh presses SNOOZE (10 minutes)");
        engine.snooze(3, 10, LocalTime.of(15, 1));
        check(engine, 15, 5);    // snoozed - nothing sent
        check(engine, 15, 11);   // snooze over - 2nd reminder
    }

    private static ActivityReminder makeReminder(int id, String type,
                                                 LocalTime time, String text) {
        ActivityReminder reminder = new ActivityReminder(1, type, time, "DAILY", text);
        reminder.setReminderId(id);
        return reminder;
    }

    private static void check(ReminderEngine engine, int hour, int minute) {
        System.out.println("-- clock " + LocalTime.of(hour, minute));
        int sent = engine.checkReminders(LocalTime.of(hour, minute));
        if (sent == 0) {
            System.out.println("   (nothing to send)");
        }
    }
}
