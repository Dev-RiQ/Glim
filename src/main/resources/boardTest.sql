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
INSERT INTO users (
    username, password, name, nickname, content, img, phone,
    created_date, followers, followings, role, tags, rate,
    read_board_id, read_alarm_id, plat_form
) VALUES
      (
          'user001', 'encrypted_pw_001', '홍길동', '@gildong',
          '안녕하세요! 여행을 좋아해요.', 'https://example.com/images/user1.jpg', '010-1234-5678',
          NOW(), 100, 50, 'ROLE_USER', '여행,사진', 1, 0, 0, 'LOCAL'
      ),
      (
          'user002', 'encrypted_pw_002', '김영희', '@younghee',
          '맛집 탐방 중입니다.', 'https://example.com/images/user2.jpg', '010-2345-6789',
          NOW(), 250, 120, 'ROLE_USER', '맛집,카페', 2, 0, 0, 'NAVER'
      ),
      (
          'user003', 'encrypted_pw_003', '이철수', '@chulsoo',
          '일상 공유 계정입니다.', 'https://example.com/images/user3.jpg', '010-3456-7890',
          NOW(), 400, 300, 'ROLE_ADMIN', '일상,운동', 3, 0, 0, 'GOOGLE'
      );






-- 삽입 확인
SELECT *
FROM boards;