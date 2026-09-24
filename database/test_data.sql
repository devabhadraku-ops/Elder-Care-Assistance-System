USE EldercareAssistanceSystem;

INSERT INTO elderly (username, password, name, age, phone, email) VALUES
('john_smith', 'password', 'John Smith', 75, '555-0001', 'john@email.com'),
('mary_johnson', 'password', 'Mary Johnson', 68, '555-0002', 'mary@email.com'),
('robert_brown', 'password', 'Robert Brown', 82, '555-0003', 'robert@email.com');

INSERT INTO caregivers (username, password, name, phone, email, qualification) VALUES
('alice_williams', 'password', 'Alice Williams', '555-1001', 'alice@email.com', 'RN'),
('david_miller', 'password', 'David Miller', '555-1002', 'david@email.com', 'CNA'),
('emma_davis', 'password', 'Emma Davis', '555-1003', 'emma@email.com', 'Health Aide');

INSERT INTO caregiverassignments (caregiver_id, elderly_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 3);

INSERT INTO activityreminders (elderly_id, activity_type, reminder_time, frequency, description) VALUES
(1, 'Medication', '09:00:00', 'DAILY', 'Take Aspirin'),
(1, 'Meal', '12:00:00', 'DAILY', 'Lunch time'),
(2, 'Medication', '08:00:00', 'DAILY', 'Take Metformin'),
(2, 'Exercise', '10:00:00', 'WEEKDAYS', 'Light walking'),
(3, 'Medication', '07:00:00', 'DAILY', 'Take Heart Medicine');

INSERT INTO alerts (elderly_id, alert_type, severity, description) VALUES
(1, 'SOS', 'CRITICAL', 'Emergency call'),
(1, 'HEALTH', 'HIGH', 'High blood pressure'),
(2, 'MEDICATION', 'MEDIUM', 'Medication overdue');

INSERT INTO healthmetrics (elderly_id, blood_pressure_systolic, blood_pressure_diastolic, heart_rate, temperature, weight) VALUES
(1, 150, 95, 78, 37.2, 75.5),
(2, 130, 85, 72, 36.8, 68.2),
(3, 140, 90, 80, 37.1, 82.3);

INSERT INTO servicerequests (elderly_id, service_type, description, urgency) VALUES
(1, 'Cleaning', 'Deep clean home', 'HIGH'),
(2, 'Cooking', 'Prepare meals', 'MEDIUM'),
(3, 'Transportation', 'Drive to pharmacy', 'LOW');
