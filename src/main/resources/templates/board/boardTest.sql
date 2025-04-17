use glimdb;

INSERT INTO boards (board_id, user_id, location, content, views, likes, comments, shares, tag_user_ids, created_at, updated_at, bgm_id, board_type, view_likes, view_shares, commentable) VALUES
(1, 1, 'Seoul', 'Exploring Seoul''s vibrant streets!', 150, 20, 5, 3, '2,3', '2025-04-16 10:00:00', '2025-04-16 10:00:00', 101, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
(2, 2, 'Busan', 'Chilling at Haeundae Beach 🌊', 200, 35, 10, 7, '1', '2025-04-15 14:30:00', '2025-04-15 14:30:00', 102, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
(3, 3, 'Jeju', 'Hiking in Jeju''s volcanic trails!', 300, 50, 15, 2, '4,5', '2025-04-14 09:00:00', '2025-04-14 09:00:00', 103, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
(4, 4, 'Incheon', 'Night views from Incheon Bridge ✨', 100, 10, 3, 1, '1,2', '2025-04-13 18:00:00', '2025-04-13 18:00:00', 104, 'SHORTS', 'TRUE', 'TRUE', 'FALSE'),
(5, 5, 'Daegu', 'Trying spicy food in Daegu!', 250, 40, 8, 4, '3', '2025-04-12 12:00:00', '2025-04-12 12:00:00', 105, 'BASIC', 'FALSE', 'FALSE', 'TRUE'),
(6, 1, 'Gyeongju', 'Visiting ancient temples in Gyeongju', 180, 25, 6, 2, '2', '2025-04-11 15:00:00', '2025-04-11 15:00:00', 106, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
(7, 2, 'Suwon', 'Walking around Hwaseong Fortress', 120, 15, 4, 0, '3,4', '2025-04-10 11:00:00', '2025-04-10 11:00:00', 107, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
(8, 3, 'Gangneung', 'Coffee by the sea in Gangneung', 224230, 304324324, 7432324, 542351153, '1,5', '2025-04-09 08:00:00', '2025-04-09 08:00:00', 108, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
(9, 4, 'Daejeon', 'Science museum tour in Daejeon', 90, 8, 2, 1, '2', '2025-04-08 16:00:00', '2025-04-08 16:00:00', 109, 'SHORTS', 'TRUE', 'TRUE', 'FALSE'),
(10, 5, 'Ulsan', 'Industrial vibes in Ulsan', 160, 22, 5, 3, '1,3', '2025-04-07 13:00:00', '2025-04-07 13:00:00', 110, 'BASIC', 'FALSE', 'FALSE', 'TRUE');

INSERT INTO board_comments (comment_id, board_id, user_id, content, likes, created_at, update_at, reply_comment_id) VALUES
                                                                                                                  (1, 1, 1, 'This is a great post!', 6365335, '2025-04-17 10:00:00', '2025-04-17 10:00:00', 0),
                                                                                                                  (2, 1, 2, 'Thanks for sharing!', 365353, '2025-04-17 10:15:00', '2025-04-17 10:15:00', 0),
                                                                                                                  (3, 2, 3, 'I have a question about this.', 0, '2025-04-17 11:00:00', '2025-04-17 11:00:00', 0),
                                                                                                                  (4, 2, 1, 'Can you clarify this point?', 3575437, '2025-04-17 11:30:00', '2025-04-17 11:30:00', 3),
                                                                                                                  (5, 3, 4, 'Awesome content!', 754644, '2025-04-17 12:00:00', '2025-04-17 12:00:00', 0),
                                                                                                                  (6, 3, 5, 'I totally agree with you.', 4466, '2025-04-17 12:30:00', '2025-04-17 12:30:00', 5),
                                                                                                                  (7, 4, 2, 'This needs more attention.', 146, '2025-04-17 13:00:00', '2025-04-17 13:00:00', 0),
                                                                                                                  (8, 4, 3, 'Great job on this!', 6865443, '2025-04-17 13:15:00', '2025-04-17 13:15:00', 0),
                                                                                                                  (9, 5, 1, 'Interesting perspective.', 376547, '2025-04-17 14:00:00', '2025-04-17 14:00:00', 0),
                                                                                                                  (10, 5, 4, 'Looking forward to more!', 2465765, '2025-04-17 14:30:00', '2025-04-17 14:30:00', 0);

-- 삽입 확인
SELECT * FROM boards;