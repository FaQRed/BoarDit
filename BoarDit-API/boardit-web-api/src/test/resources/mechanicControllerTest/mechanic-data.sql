DELETE
FROM board_game_mechanic;
DELETE
FROM mechanic;

ALTER TABLE mechanic
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO mechanic (name)
VALUES ('Mechanic 1'),
       ('Mechanic 2');