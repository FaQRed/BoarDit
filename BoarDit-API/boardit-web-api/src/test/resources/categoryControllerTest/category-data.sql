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
FROM board_game;

ALTER TABLE category ALTER COLUMN id RESTART WITH 1;

INSERT INTO category (name)
VALUES ('Category 1'),
       ('Category 2'),
       ('Category 3');