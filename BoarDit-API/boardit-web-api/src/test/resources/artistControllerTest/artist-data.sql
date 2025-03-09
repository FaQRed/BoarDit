
DELETE FROM board_game_artist;
DELETE FROM artist;

ALTER TABLE artist ALTER COLUMN id RESTART WITH 1;

-- Добавление тестовых данных
INSERT INTO artist (bgg_id, name) VALUES
                                          (101, 'Test Artist 1'),
                                          (102, 'Test Artist 2');