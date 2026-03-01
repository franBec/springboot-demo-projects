-- Lite test seed for the Sakila schema.
-- Contains minimal rows for all tables, preserving FK dependency order.
-- FILM data is limited to films 1-10; all other tables are trimmed accordingly.

-- 2 countries (used by cities 300 and 576, which back the 4 store/staff addresses)
INSERT INTO "PUBLIC"."COUNTRY" VALUES
(8,  'Australia', TIMESTAMP '2006-02-15 04:44:00'),
(20, 'Canada',    TIMESTAMP '2006-02-15 04:44:00');

-- 2 cities (referenced by ADDRESS rows below)
INSERT INTO "PUBLIC"."CITY" VALUES
(300, 'Lethbridge', 20, TIMESTAMP '2006-02-15 04:45:25'),
(576, 'Woodridge',   8, TIMESTAMP '2006-02-15 04:45:25');

-- 4 addresses: 1-2 = store addresses, 3-4 = staff home addresses
INSERT INTO "PUBLIC"."ADDRESS" VALUES
(1, '47 MySakila Drive',    NULL, 'Alberta', 300, '',      '',            TIMESTAMP '2006-02-15 04:45:30'),
(2, '28 MySQL Boulevard',   NULL, 'QLD',     576, '',      '',            TIMESTAMP '2006-02-15 04:45:30'),
(3, '23 Workhaven Lane',    NULL, 'Alberta', 300, '',      '14033335568', TIMESTAMP '2006-02-15 04:45:30'),
(4, '1411 Lillydale Drive', NULL, 'QLD',     576, '',      '6172235589',  TIMESTAMP '2006-02-15 04:45:30');

-- 6 languages (all original rows; FILM has FK to LANGUAGE)
INSERT INTO "PUBLIC"."LANGUAGE" VALUES
(1, 'English',  TIMESTAMP '2006-02-15 05:02:19'),
(2, 'Italian',  TIMESTAMP '2006-02-15 05:02:19'),
(3, 'Japanese', TIMESTAMP '2006-02-15 05:02:19'),
(4, 'Mandarin', TIMESTAMP '2006-02-15 05:02:19'),
(5, 'French',   TIMESTAMP '2006-02-15 05:02:19'),
(6, 'German',   TIMESTAMP '2006-02-15 05:02:19');

-- 16 categories (all original rows; FILM_CATEGORY has FK to CATEGORY)
INSERT INTO "PUBLIC"."CATEGORY" VALUES
(1,  'Action',      TIMESTAMP '2006-02-15 04:46:27'),
(2,  'Animation',   TIMESTAMP '2006-02-15 04:46:27'),
(3,  'Children',    TIMESTAMP '2006-02-15 04:46:27'),
(4,  'Classics',    TIMESTAMP '2006-02-15 04:46:27'),
(5,  'Comedy',      TIMESTAMP '2006-02-15 04:46:27'),
(6,  'Documentary', TIMESTAMP '2006-02-15 04:46:27'),
(7,  'Drama',       TIMESTAMP '2006-02-15 04:46:27'),
(8,  'Family',      TIMESTAMP '2006-02-15 04:46:27'),
(9,  'Foreign',     TIMESTAMP '2006-02-15 04:46:27'),
(10, 'Games',       TIMESTAMP '2006-02-15 04:46:27'),
(11, 'Horror',      TIMESTAMP '2006-02-15 04:46:27'),
(12, 'Music',       TIMESTAMP '2006-02-15 04:46:27'),
(13, 'New',         TIMESTAMP '2006-02-15 04:46:27'),
(14, 'Sci-Fi',      TIMESTAMP '2006-02-15 04:46:27'),
(15, 'Sports',      TIMESTAMP '2006-02-15 04:46:27'),
(16, 'Travel',      TIMESTAMP '2006-02-15 04:46:27');

-- 53 actors (only those appearing in films 1-10)
INSERT INTO "PUBLIC"."ACTOR" VALUES
(1,   'PENELOPE',  'GUINESS',    TIMESTAMP '2006-02-15 04:34:33'),
(2,   'NICK',      'WAHLBERG',   TIMESTAMP '2006-02-15 04:34:33'),
(10,  'CHRISTIAN', 'GABLE',      TIMESTAMP '2006-02-15 04:34:33'),
(19,  'BOB',       'FAWCETT',    TIMESTAMP '2006-02-15 04:34:33'),
(20,  'LUCILLE',   'TRACY',      TIMESTAMP '2006-02-15 04:34:33'),
(21,  'KIRSTEN',   'PALTROW',    TIMESTAMP '2006-02-15 04:34:33'),
(22,  'ELVIS',     'MARX',       TIMESTAMP '2006-02-15 04:34:33'),
(23,  'SANDRA',    'KILMER',     TIMESTAMP '2006-02-15 04:34:33'),
(24,  'CAMERON',   'STREEP',     TIMESTAMP '2006-02-15 04:34:33'),
(26,  'RIP',       'CRAWFORD',   TIMESTAMP '2006-02-15 04:34:33'),
(29,  'ALEC',      'WAYNE',      TIMESTAMP '2006-02-15 04:34:33'),
(30,  'SANDRA',    'PECK',       TIMESTAMP '2006-02-15 04:34:33'),
(35,  'JUDY',      'DEAN',       TIMESTAMP '2006-02-15 04:34:33'),
(37,  'VAL',       'BOLGER',     TIMESTAMP '2006-02-15 04:34:33'),
(40,  'JOHNNY',    'CAGE',       TIMESTAMP '2006-02-15 04:34:33'),
(41,  'JODIE',     'DEGENERES',  TIMESTAMP '2006-02-15 04:34:33'),
(51,  'GARY',      'PHOENIX',    TIMESTAMP '2006-02-15 04:34:33'),
(53,  'MENA',      'TEMPLE',     TIMESTAMP '2006-02-15 04:34:33'),
(55,  'FAY',       'KILMER',     TIMESTAMP '2006-02-15 04:34:33'),
(59,  'DUSTIN',    'TAUTOU',     TIMESTAMP '2006-02-15 04:34:33'),
(62,  'JAYNE',     'NEESON',     TIMESTAMP '2006-02-15 04:34:33'),
(64,  'RAY',       'JOHANSSON',  TIMESTAMP '2006-02-15 04:34:33'),
(68,  'RIP',       'WINSLET',    TIMESTAMP '2006-02-15 04:34:33'),
(81,  'SCARLETT',  'DAMON',      TIMESTAMP '2006-02-15 04:34:33'),
(85,  'MINNIE',    'ZELLWEGER',  TIMESTAMP '2006-02-15 04:34:33'),
(88,  'KENNETH',   'PESCI',      TIMESTAMP '2006-02-15 04:34:33'),
(90,  'SEAN',      'GUINESS',    TIMESTAMP '2006-02-15 04:34:33'),
(96,  'GENE',      'WILLIS',     TIMESTAMP '2006-02-15 04:34:33'),
(99,  'JIM',       'MOSTEL',     TIMESTAMP '2006-02-15 04:34:33'),
(103, 'MATTHEW',   'LEIGH',      TIMESTAMP '2006-02-15 04:34:33'),
(108, 'WARREN',    'NOLTE',      TIMESTAMP '2006-02-15 04:34:33'),
(110, 'SUSAN',     'DAVIS',      TIMESTAMP '2006-02-15 04:34:33'),
(117, 'RENEE',     'TRACY',      TIMESTAMP '2006-02-15 04:34:33'),
(123, 'JULIANNE',  'DENCH',      TIMESTAMP '2006-02-15 04:34:33'),
(130, 'GRETA',     'KEITEL',     TIMESTAMP '2006-02-15 04:34:33'),
(133, 'RICHARD',   'PENN',       TIMESTAMP '2006-02-15 04:34:33'),
(137, 'MORGAN',    'WILLIAMS',   TIMESTAMP '2006-02-15 04:34:33'),
(138, 'LUCILLE',   'DEE',        TIMESTAMP '2006-02-15 04:34:33'),
(142, 'JADA',      'RYDER',      TIMESTAMP '2006-02-15 04:34:33'),
(147, 'FAY',       'WINSLET',    TIMESTAMP '2006-02-15 04:34:33'),
(157, 'GRETA',     'MALDEN',     TIMESTAMP '2006-02-15 04:34:33'),
(160, 'CHRIS',     'DEPP',       TIMESTAMP '2006-02-15 04:34:33'),
(162, 'OPRAH',     'KILMER',     TIMESTAMP '2006-02-15 04:34:33'),
(169, 'KENNETH',   'HOFFMAN',    TIMESTAMP '2006-02-15 04:34:33'),
(170, 'MENA',      'HOPPER',     TIMESTAMP '2006-02-15 04:34:33'),
(175, 'WILLIAM',   'HACKMAN',    TIMESTAMP '2006-02-15 04:34:33'),
(181, 'MATTHEW',   'CARREY',     TIMESTAMP '2006-02-15 04:34:33'),
(185, 'MICHAEL',   'BOLGER',     TIMESTAMP '2006-02-15 04:34:33'),
(188, 'ROCK',      'DUKAKIS',    TIMESTAMP '2006-02-15 04:34:33'),
(194, 'MERYL',     'ALLEN',      TIMESTAMP '2006-02-15 04:34:33'),
(197, 'REESE',     'WEST',       TIMESTAMP '2006-02-15 04:34:33'),
(198, 'MARY',      'KEITEL',     TIMESTAMP '2006-02-15 04:34:33'),
(200, 'THORA',     'TEMPLE',     TIMESTAMP '2006-02-15 04:34:33');

-- 10 films (films 1-10, all with LANGUAGE_ID=1 English, ORIGINAL_LANGUAGE_ID=NULL)
INSERT INTO "PUBLIC"."FILM" VALUES
(1,  'ACADEMY DINOSAUR',  'A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies',                                          DATE '2006-01-01', 1, NULL, 6, 0.99, 86,  20.99, 'PG',    'Deleted Scenes,Behind the Scenes',             TIMESTAMP '2006-02-15 05:03:42'),
(2,  'ACE GOLDFINGER',    'A Astounding Epistle of a Database Administrator And a Explorer who must Find a Car in Ancient China',                                      DATE '2006-01-01', 1, NULL, 3, 4.99, 48,  12.99, 'G',     'Trailers,Deleted Scenes',                      TIMESTAMP '2006-02-15 05:03:42'),
(3,  'ADAPTATION HOLES',  'A Astounding Reflection of a Lumberjack And a Car who must Sink a Lumberjack in A Baloon Factory',                                         DATE '2006-01-01', 1, NULL, 7, 2.99, 50,  18.99, 'NC-17', 'Trailers,Deleted Scenes',                      TIMESTAMP '2006-02-15 05:03:42'),
(4,  'AFFAIR PREJUDICE',  'A Fanciful Documentary of a Frisbee And a Lumberjack who must Chase a Monkey in A Shark Tank',                                             DATE '2006-01-01', 1, NULL, 5, 2.99, 117, 26.99, 'G',     'Commentaries,Behind the Scenes',               TIMESTAMP '2006-02-15 05:03:42'),
(5,  'AFRICAN EGG',       'A Fast-Paced Documentary of a Pastry Chef And a Dentist who must Pursue a Forensic Psychologist in The Gulf of Mexico',                   DATE '2006-01-01', 1, NULL, 6, 2.99, 130, 22.99, 'G',     'Deleted Scenes',                               TIMESTAMP '2006-02-15 05:03:42'),
(6,  'AGENT TRUMAN',      'A Intrepid Panorama of a Robot And a Boy who must Escape a Sumo Wrestler in Ancient China',                                                DATE '2006-01-01', 1, NULL, 3, 2.99, 169, 17.99, 'PG',    'Deleted Scenes',                               TIMESTAMP '2006-02-15 05:03:42'),
(7,  'AIRPLANE SIERRA',   'A Touching Saga of a Hunter And a Butler who must Discover a Butler in A Jet Boat',                                                        DATE '2006-01-01', 1, NULL, 6, 4.99, 62,  28.99, 'PG-13', 'Trailers,Deleted Scenes',                      TIMESTAMP '2006-02-15 05:03:42'),
(8,  'AIRPORT POLLOCK',   'A Epic Tale of a Moose And a Girl who must Confront a Monkey in Ancient India',                                                            DATE '2006-01-01', 1, NULL, 6, 4.99, 54,  15.99, 'R',     'Trailers',                                     TIMESTAMP '2006-02-15 05:03:42'),
(9,  'ALABAMA DEVIL',     'A Thoughtful Panorama of a Database Administrator And a Mad Scientist who must Outgun a Mad Scientist in A Jet Boat',                      DATE '2006-01-01', 1, NULL, 3, 2.99, 114, 21.99, 'PG-13', 'Trailers,Deleted Scenes',                      TIMESTAMP '2006-02-15 05:03:42'),
(10, 'ALADDIN CALENDAR',  'A Action-Packed Tale of a Man And a Lumberjack who must Reach a Feminist in Ancient China',                                                NULL, 1, NULL, 6, 4.99, 63,  24.99, 'NC-17', 'Trailers,Deleted Scenes',                      TIMESTAMP '2006-02-15 05:03:42');

-- STORE and STAFF have a circular FK; disable referential integrity for this block
SET REFERENTIAL_INTEGRITY FALSE;

INSERT INTO "PUBLIC"."STORE" VALUES
(1, 1, 1, TIMESTAMP '2006-02-15 04:57:12'),
(2, 2, 2, TIMESTAMP '2006-02-15 04:57:12');

INSERT INTO "PUBLIC"."STAFF" VALUES
(1, 'Mike', 'Hillyer',  3, NULL, 'Mike.Hillyer@sakilastaff.com',  1, TRUE, 'Mike', 'd435bf0a69ad0a866b9ba50fde5bef0c',          TIMESTAMP '2006-02-15 04:57:16');
INSERT INTO "PUBLIC"."STAFF" VALUES
(2, 'Jon',  'Stephens', 4, NULL, 'Jon.Stephens@sakilastaff.com',  2, TRUE, 'Jon',  '8cb2237d0679ca88db6464eac60da96345513964', TIMESTAMP '2006-02-15 04:57:16');

SET REFERENTIAL_INTEGRITY TRUE;

-- 2 customers (referencing store 1 and store 2, address 5 and 6 — but we only have 1-4;
-- reuse address 3 and 4 to avoid adding more cities)
INSERT INTO "PUBLIC"."CUSTOMER" VALUES
(1, 1, 'MARY',    'SMITH',   'MARY.SMITH@sakilacustomer.org',   3, TRUE, TIMESTAMP '2006-02-14 22:04:36', TIMESTAMP '2006-02-15 04:57:20'),
(2, 1, 'PATRICIA','JOHNSON', 'PATRICIA.JOHNSON@sakilacustomer.org', 4, TRUE, TIMESTAMP '2006-02-14 22:04:36', TIMESTAMP '2006-02-15 04:57:20');

-- 62 film-actor associations (only for films 1-10)
INSERT INTO "PUBLIC"."FILM_ACTOR" VALUES
(1,   1,  TIMESTAMP '2006-02-15 05:05:03'),
(10,  1,  TIMESTAMP '2006-02-15 05:05:03'),
(20,  1,  TIMESTAMP '2006-02-15 05:05:03'),
(30,  1,  TIMESTAMP '2006-02-15 05:05:03'),
(40,  1,  TIMESTAMP '2006-02-15 05:05:03'),
(53,  1,  TIMESTAMP '2006-02-15 05:05:03'),
(108, 1,  TIMESTAMP '2006-02-15 05:05:03'),
(162, 1,  TIMESTAMP '2006-02-15 05:05:03'),
(188, 1,  TIMESTAMP '2006-02-15 05:05:03'),
(198, 1,  TIMESTAMP '2006-02-15 05:05:03'),
(19,  2,  TIMESTAMP '2006-02-15 05:05:03'),
(85,  2,  TIMESTAMP '2006-02-15 05:05:03'),
(90,  2,  TIMESTAMP '2006-02-15 05:05:03'),
(160, 2,  TIMESTAMP '2006-02-15 05:05:03'),
(2,   3,  TIMESTAMP '2006-02-15 05:05:03'),
(19,  3,  TIMESTAMP '2006-02-15 05:05:03'),
(24,  3,  TIMESTAMP '2006-02-15 05:05:03'),
(64,  3,  TIMESTAMP '2006-02-15 05:05:03'),
(123, 3,  TIMESTAMP '2006-02-15 05:05:03'),
(41,  4,  TIMESTAMP '2006-02-15 05:05:03'),
(81,  4,  TIMESTAMP '2006-02-15 05:05:03'),
(88,  4,  TIMESTAMP '2006-02-15 05:05:03'),
(147, 4,  TIMESTAMP '2006-02-15 05:05:03'),
(162, 4,  TIMESTAMP '2006-02-15 05:05:03'),
(51,  5,  TIMESTAMP '2006-02-15 05:05:03'),
(59,  5,  TIMESTAMP '2006-02-15 05:05:03'),
(103, 5,  TIMESTAMP '2006-02-15 05:05:03'),
(181, 5,  TIMESTAMP '2006-02-15 05:05:03'),
(200, 5,  TIMESTAMP '2006-02-15 05:05:03'),
(21,  6,  TIMESTAMP '2006-02-15 05:05:03'),
(23,  6,  TIMESTAMP '2006-02-15 05:05:03'),
(62,  6,  TIMESTAMP '2006-02-15 05:05:03'),
(108, 6,  TIMESTAMP '2006-02-15 05:05:03'),
(137, 6,  TIMESTAMP '2006-02-15 05:05:03'),
(169, 6,  TIMESTAMP '2006-02-15 05:05:03'),
(197, 6,  TIMESTAMP '2006-02-15 05:05:03'),
(99,  7,  TIMESTAMP '2006-02-15 05:05:03'),
(133, 7,  TIMESTAMP '2006-02-15 05:05:03'),
(162, 7,  TIMESTAMP '2006-02-15 05:05:03'),
(170, 7,  TIMESTAMP '2006-02-15 05:05:03'),
(185, 7,  TIMESTAMP '2006-02-15 05:05:03'),
(55,  8,  TIMESTAMP '2006-02-15 05:05:03'),
(96,  8,  TIMESTAMP '2006-02-15 05:05:03'),
(110, 8,  TIMESTAMP '2006-02-15 05:05:03'),
(138, 8,  TIMESTAMP '2006-02-15 05:05:03'),
(10,  9,  TIMESTAMP '2006-02-15 05:05:03'),
(22,  9,  TIMESTAMP '2006-02-15 05:05:03'),
(26,  9,  TIMESTAMP '2006-02-15 05:05:03'),
(53,  9,  TIMESTAMP '2006-02-15 05:05:03'),
(68,  9,  TIMESTAMP '2006-02-15 05:05:03'),
(108, 9,  TIMESTAMP '2006-02-15 05:05:03'),
(130, 9,  TIMESTAMP '2006-02-15 05:05:03'),
(175, 9,  TIMESTAMP '2006-02-15 05:05:03'),
(194, 9,  TIMESTAMP '2006-02-15 05:05:03'),
(29,  10, TIMESTAMP '2006-02-15 05:05:03'),
(35,  10, TIMESTAMP '2006-02-15 05:05:03'),
(37,  10, TIMESTAMP '2006-02-15 05:05:03'),
(64,  10, TIMESTAMP '2006-02-15 05:05:03'),
(117, 10, TIMESTAMP '2006-02-15 05:05:03'),
(142, 10, TIMESTAMP '2006-02-15 05:05:03'),
(157, 10, TIMESTAMP '2006-02-15 05:05:03'),
(188, 10, TIMESTAMP '2006-02-15 05:05:03');

-- 10 film-category associations (one per film, for films 1-10)
INSERT INTO "PUBLIC"."FILM_CATEGORY" VALUES
(1,  6,  TIMESTAMP '2006-02-15 05:07:09'),
(2,  11, TIMESTAMP '2006-02-15 05:07:09'),
(3,  6,  TIMESTAMP '2006-02-15 05:07:09'),
(4,  11, TIMESTAMP '2006-02-15 05:07:09'),
(5,  8,  TIMESTAMP '2006-02-15 05:07:09'),
(6,  9,  TIMESTAMP '2006-02-15 05:07:09'),
(7,  5,  TIMESTAMP '2006-02-15 05:07:09'),
(8,  11, TIMESTAMP '2006-02-15 05:07:09'),
(9,  11, TIMESTAMP '2006-02-15 05:07:09'),
(10, 15, TIMESTAMP '2006-02-15 05:07:09');

-- 10 inventory entries (one copy of each film in store 1)
INSERT INTO "PUBLIC"."INVENTORY" VALUES
(1,  1,  1, TIMESTAMP '2006-02-15 05:09:17'),
(2,  2,  1, TIMESTAMP '2006-02-15 05:09:17'),
(3,  3,  1, TIMESTAMP '2006-02-15 05:09:17'),
(4,  4,  1, TIMESTAMP '2006-02-15 05:09:17'),
(5,  5,  1, TIMESTAMP '2006-02-15 05:09:17'),
(6,  6,  1, TIMESTAMP '2006-02-15 05:09:17'),
(7,  7,  1, TIMESTAMP '2006-02-15 05:09:17'),
(8,  8,  1, TIMESTAMP '2006-02-15 05:09:17'),
(9,  9,  1, TIMESTAMP '2006-02-15 05:09:17'),
(10, 10, 1, TIMESTAMP '2006-02-15 05:09:17');

-- 2 rentals (customer 1 rented inventory items 1 and 2, handled by staff 1)
INSERT INTO "PUBLIC"."RENTAL" VALUES
(1, TIMESTAMP '2005-05-24 22:53:30', 1, 1, TIMESTAMP '2005-05-26 22:04:30', 1, TIMESTAMP '2006-02-15 21:30:53'),
(2, TIMESTAMP '2005-05-24 22:54:33', 2, 2, TIMESTAMP '2005-05-28 19:40:33', 1, TIMESTAMP '2006-02-15 21:30:53');

-- 2 payments (one per rental above)
INSERT INTO "PUBLIC"."PAYMENT" VALUES
(1, 1, 1, 1, 2.99, TIMESTAMP '2005-05-25 11:30:37', TIMESTAMP '2006-02-15 22:12:30'),
(2, 2, 1, 2, 0.99, TIMESTAMP '2005-05-28 10:35:23', TIMESTAMP '2006-02-15 22:12:30');
