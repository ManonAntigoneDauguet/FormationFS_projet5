INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Drake', 'RAMORAY'),
       ('Phoebe', 'PHALANGE');

INSERT INTO SESSIONS (name, description, date, teacher_id)
VALUES ('Yoga pour débutants', 'Venez apprendre les poses qui alignent les chats crâ... ou du moins à avoir l\'air sexy en le faisant !', '2024-12-10 10:00:00', 1),
       ('Yoga avancé', 'Rejoignez cette séance où l\'alignement des énergies cosmiques fusionne avec la fluidité des mouvements. En vous concentrant sur le flux d\'énergie transdimensionnelle, vous expérimenterez l\'unité du corps et de l\'esprit, tout en vous connectant à des fréquences vibratoires qui dépassent la réalité perçue. Les poses sont interchangeables et dépendront de la résonance de votre aura du jour.', '2024-12-12 14:00:00', 2);
