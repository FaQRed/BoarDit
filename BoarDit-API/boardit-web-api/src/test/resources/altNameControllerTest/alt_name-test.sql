delete from alternate_name;
delete from board_game;


ALTER TABLE alternate_name ALTER COLUMN id RESTART WITH 1;
ALTER TABLE board_game ALTER COLUMN id RESTART WITH 1;

INSERT INTO board_game (id,  max_players, min_players, playing_time, year_published, bgg_id, description, image_link, name)
VALUES (1,  4, 2, 45, 2020, 123456, 'Sample board game description', 'https://example.com/image.jpg', 'Sample Board Game');

-- Now insert into alternate_name without specifying the id for auto-increment
INSERT INTO alternate_name ( board_game_id, name)
VALUES ( 1, 'Test Alternate Name 1'), (1, 'Test Alternate Name 2');

