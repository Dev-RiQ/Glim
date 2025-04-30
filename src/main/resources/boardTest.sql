use glimdb;

INSERT INTO Stories (user_id, fileName, likes, views, createdAt)
VALUES
    (1, 'videos/74552d77-d834-4d84-a33a-c124cf2c0819_video%20%285%29', 10, 150, '2024-04-01 10:30:00'),
    (2, 'videos/363da01c-a655-4336-8a77-6846c40e14fd_video%20%282%29', 25, 320, '2024-04-02 14:45:00');

INSERT INTO story_likes (story_id, user_id, created_at)
VALUES
    (1, 1, '2024-04-01 11:00:00'),
    (2, 2, '2024-04-02 15:10:00');

INSERT INTO story_tags (story_id, tag)
VALUES
    (1, '여행'),
    (2, '맛집');

INSERT INTO story_views (story_id, user_id)
VALUES
    (1, 1),
    (2, 2);


-- 삽입 확인
SELECT *
FROM boards;