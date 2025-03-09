DELETE
FROM board_game_artist;
DELETE
FROM board_game_category;
DELETE
FROM board_game_mechanic;
DELETE
FROM artist;
DELETE
FROM category;
DELETE
FROM mechanic;
DELETE
FROM alternate_name;
DELETE
FROM expansion;
DELETE
FROM board_game;



ALTER TABLE board_game
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE category
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mechanic
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE artist
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE alternate_name
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE expansion
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO board_game (name, year_published, min_players, max_players, playing_time)
VALUES ('Test Board Game 1', 2020, 2, 4, 60),
       ('Test Board Game 2', 2021, 3, 6, 90);

INSERT INTO category (name)
VALUES ('Category 1'),
       ('Category 2'),
       ('Category 3');

INSERT INTO mechanic (name)
VALUES ('Mechanic 1'),
       ('Mechanic 2'),
       ('Mechanic 3');

INSERT INTO artist (name)
VALUES ('Artist 1'),
       ('Artist 2');

INSERT INTO board_game_category (board_games_id, categories_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO board_game_mechanic (board_games_id, mechanics_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO board_game_artist (board_games_id, artists_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO alternate_name (board_game_id, name)
VALUES (1, 'Alternate Name 1'),
       (1, 'Alternate Name 2');

INSERT INTO expansion (board_game_id, name, description, year_published, min_players, max_players, playing_time)
VALUES (1, 'Expansion 1', 'Description 1', 2020, 2, 4, 60),
       (2, 'Expansion 2', 'Description 2', 2021, 3, 6, 90);
