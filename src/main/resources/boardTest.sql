use glimdb;

INSERT INTO boards (board_id, user_id, location, content, views, likes, comments, shares, tag_user_ids, created_at,
                    updated_at, bgm_id, board_type, view_likes, view_shares, commentable)
VALUES (1, 3, 'Seoul', 'Night market vibes in Myeongdong!', 230, 45, 12, 5, '1,4', '2025-03-01 09:15:00',
        '2025-03-01 09:15:00', 115, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (2, 7, 'Busan', 'Sunset at Gwangan Bridge 🌅', 180, 30, 8, 3, '2,5', '2025-03-02 17:30:00', '2025-03-02 17:30:00',
        123, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (3, 1, 'Jeju', 'Chasing waterfalls in Jeju!', 320, 60, 15, 7, '3', '2025-03-03 11:00:00', '2025-03-03 11:00:00',
        109, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (4, 9, 'Incheon', 'Exploring Chinatown in Incheon', 150, 20, 5, 2, '1,2', '2025-03-04 14:45:00',
        '2025-03-04 14:45:00', 130, 'SHORTS', 'FALSE', 'FALSE', 'TRUE'),
       (5, 5, 'Daegu', 'Street food tour in Daegu 🍗', 270, 50, 10, 4, '4', '2025-03-05 12:30:00', '2025-03-05 12:30:00',
        112, 'BASIC', 'TRUE', 'TRUE', 'FALSE'),
       (6, 2, 'Gyeongju', 'Strolling through Bulguksa Temple', 200, 35, 9, 3, '3,5', '2025-03-06 10:00:00',
        '2025-03-06 10:00:00', 119, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (7, 8, 'Suwon', 'Evening at Suwon Hwaseong', 130, 15, 3, 1, '2', '2025-03-07 18:20:00', '2025-03-07 18:20:00',
        125, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (8, 4, 'Gangneung', 'Morning coffee by the ocean ☕', 290, 55, 14, 6, '1,3', '2025-03-08 08:00:00',
        '2025-03-08 08:00:00', 108, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (9, 6, 'Daejeon', 'Fun day at Expo Park!', 110, 12, 4, 2, '5', '2025-03-09 13:15:00', '2025-03-09 13:15:00', 114,
        'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (10, 10, 'Ulsan', 'Hiking in Ulsan’s mountains', 170, 25, 6, 3, '2,4', '2025-03-10 09:45:00',
        '2025-03-10 09:45:00', 121, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (11, 3, 'Seoul', 'Han River picnic day!', 240, 40, 11, 5, '1', '2025-03-11 12:00:00', '2025-03-11 12:00:00', 127,
        'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (12, 5, 'Busan', 'Exploring Jagalchi Market', 190, 32, 7, 4, '3,4', '2025-03-12 15:30:00', '2025-03-12 15:30:00',
        113, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (13, 7, 'Jeju', 'Sunrise at Seongsan Ilchulbong', 350, 70, 18, 8, '2,5', '2025-03-13 06:30:00',
        '2025-03-13 06:30:00', 118, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (14, 1, 'Incheon', 'Relaxing at Songdo Central Park', 140, 18, 4, 2, '1,3', '2025-03-14 16:00:00',
        '2025-03-14 16:00:00', 124, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (15, 9, 'Daegu', 'Nightlife in downtown Daegu', 260, 48, 12, 5, '4', '2025-03-15 20:00:00',
        '2025-03-15 20:00:00', 110, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (16, 2, 'Gyeongju', 'Cherry blossoms in Gyeongju 🌸', 220, 38, 10, 4, '2,5', '2025-03-16 11:30:00',
        '2025-03-16 11:30:00', 116, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (17, 6, 'Suwon', 'Exploring Suwon’s old town', 120, 14, 3, 1, '3', '2025-03-17 14:00:00', '2025-03-17 14:00:00',
        122, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (18, 4, 'Gangneung', 'Surfing in Gangneung waves!', 280, 52, 13, 6, '1,4', '2025-03-18 09:00:00',
        '2025-03-18 09:00:00', 107, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (19, 8, 'Daejeon', 'Museum hopping in Daejeon', 100, 10, 2, 1, '5', '2025-03-19 13:45:00', '2025-03-19 13:45:00',
        129, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (20, 10, 'Ulsan', 'Sunset at Daewangam Park', 160, 22, 5, 3, '2,3', '2025-03-20 18:30:00', '2025-03-20 18:30:00',
        111, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (21, 3, 'Seoul', 'Shopping in Gangnam!', 250, 42, 10, 5, '1,4', '2025-03-21 15:00:00', '2025-03-21 15:00:00',
        120, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (22, 5, 'Busan', 'Cruising around Busan Port', 200, 35, 8, 4, '2', '2025-03-22 10:30:00', '2025-03-22 10:30:00',
        126, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (23, 7, 'Jeju', 'Exploring Udo Island 🏝️', 330, 65, 16, 7, '3,5', '2025-03-23 09:00:00', '2025-03-23 09:00:00',
        108, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (24, 1, 'Incheon', 'Evening walk at Wolmido', 130, 16, 4, 2, '1,4', '2025-03-24 19:00:00', '2025-03-24 19:00:00',
        117, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (25, 9, 'Daegu', 'Trying Daegu’s famous makchang', 270, 50, 12, 5, '2,3', '2025-03-25 12:15:00',
        '2025-03-25 12:15:00', 123, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (26, 2, 'Gyeongju', 'Night at Anapji Pond', 210, 37, 9, 4, '5', '2025-03-26 20:00:00', '2025-03-26 20:00:00',
        119, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (27, 6, 'Suwon', 'Suwon street food adventure', 140, 18, 5, 2, '3,4', '2025-03-27 13:30:00',
        '2025-03-27 13:30:00', 115, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (28, 4, 'Gangneung', 'Hiking in Seoraksan!', 300, 58, 14, 6, '1,2', '2025-03-28 08:30:00', '2025-03-28 08:30:00',
        110, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (29, 8, 'Daejeon', 'Relaxing at Yuseong Hot Springs', 110, 12, 3, 1, '5', '2025-03-29 16:00:00',
        '2025-03-29 16:00:00', 128, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (30, 10, 'Ulsan', 'Exploring Taehwagang River', 180, 28, 6, 3, '2,4', '2025-03-30 11:00:00',
        '2025-03-30 11:00:00', 112, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (31, 3, 'Seoul', 'Bukchon Hanok Village vibes', 260, 45, 11, 5, '1,3', '2025-03-31 14:00:00',
        '2025-03-31 14:00:00', 121, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (32, 5, 'Busan', 'Haeundae Beach at dusk', 190, 33, 8, 4, '2,5', '2025-04-01 18:00:00', '2025-04-01 18:00:00',
        109, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (33, 7, 'Jeju', 'Manjanggul Cave adventure', 340, 68, 17, 8, '3', '2025-04-02 10:00:00', '2025-04-02 10:00:00',
        116, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (34, 1, 'Incheon', 'Sunset at Eurwangni Beach', 150, 20, 5, 2, '1,4', '2025-04-03 19:30:00',
        '2025-04-03 19:30:00', 124, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (35, 9, 'Daegu', 'E-World amusement park fun!', 280, 52, 13, 6, '2,3', '2025-04-04 15:00:00',
        '2025-04-04 15:00:00', 111, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (36, 2, 'Gyeongju', 'Cycling through Gyeongju fields', 200, 35, 9, 4, '5', '2025-04-05 09:30:00',
        '2025-04-05 09:30:00', 118, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (37, 6, 'Suwon', 'Suwon’s vibrant night market', 130, 16, 4, 2, '3,4', '2025-04-06 20:00:00',
        '2025-04-06 20:00:00', 122, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (38, 4, 'Gangneung', 'Sunrise over Gyeongpo Beach', 290, 55, 14, 6, '1,2', '2025-04-07 06:00:00',
        '2025-04-07 06:00:00', 107, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (39, 8, 'Daejeon', 'Art gallery tour in Daejeon', 100, 10, 2, 1, '5', '2025-04-08 13:00:00',
        '2025-04-08 13:00:00', 129, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (40, 10, 'Ulsan', 'Relaxing at Ilsan Beach', 170, 25, 6, 3, '2,3', '2025-04-09 12:30:00', '2025-04-09 12:30:00',
        113, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (41, 3, 'Seoul', 'Exploring Hongdae’s street art', 270, 48, 12, 5, '1,4', '2025-04-10 16:00:00',
        '2025-04-10 16:00:00', 120, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (42, 5, 'Busan', 'Gamcheon Culture Village vibes', 210, 38, 9, 4, '2,5', '2025-04-11 11:00:00',
        '2025-04-11 11:00:00', 108, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (43, 7, 'Jeju', 'Snorkeling in Jeju’s clear waters', 360, 72, 18, 8, '3', '2025-04-12 09:00:00',
        '2025-04-12 09:00:00', 115, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (44, 1, 'Incheon', 'Day trip to Ganghwa Island', 140, 18, 4, 2, '1,4', '2025-04-13 14:30:00',
        '2025-04-13 14:30:00', 123, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (45, 9, 'Daegu', 'Suseong Lake at night', 260, 50, 12, 5, '2,3', '2025-04-14 19:00:00', '2025-04-14 19:00:00',
        110, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (46, 2, 'Gyeongju', 'Exploring Cheomseongdae', 190, 33, 8, 4, '5', '2025-04-15 10:00:00', '2025-04-15 10:00:00',
        117, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (47, 6, 'Suwon', 'Suwon’s traditional markets', 120, 14, 3, 1, '3,4', '2025-04-16 13:00:00',
        '2025-04-16 13:00:00', 121, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (48, 4, 'Gangneung', 'Camping by the sea in Gangneung', 300, 58, 14, 6, '1,2', '2025-04-16 15:30:00',
        '2025-04-16 15:30:00', 109, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (49, 8, 'Daejeon', 'Hiking in Gyeryongsan', 110, 12, 3, 1, '5', '2025-04-17 08:00:00', '2025-04-17 08:00:00',
        128, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (50, 10, 'Ulsan', 'Exploring Ulsan’s bamboo forest', 180, 28, 6, 3, '2,3', '2025-04-17 11:30:00',
        '2025-04-17 11:30:00', 114, 'BASIC', 'TRUE', 'FALSE', 'TRUE');
INSERT INTO board_comments (comment_id, board_id, user_id, content, likes, created_at, update_at, reply_comment_id)
VALUES (1, 1, 1, 'This is a great post!', 6365335, '2025-04-17 10:00:00', '2025-04-17 10:00:00', 0),
       (2, 1, 2, 'Thanks for sharing!', 365353, '2025-04-17 10:15:00', '2025-04-17 10:15:00', 0),
       (3, 2, 3, 'I have a question about this.', 0, '2025-04-17 11:00:00', '2025-04-17 11:00:00', 0),
       (4, 2, 1, 'Can you clarify this point?', 3575437, '2025-04-17 11:30:00', '2025-04-17 11:30:00', 3),
       (5, 3, 4, 'Awesome content!', 754644, '2025-04-17 12:00:00', '2025-04-17 12:00:00', 0),
       (6, 3, 5, 'I totally agree with you.', 4466, '2025-04-17 12:30:00', '2025-04-17 12:30:00', 5),
       (7, 4, 2, 'This needs more attention.', 146, '2025-04-17 13:00:00', '2025-04-17 13:00:00', 0),
       (8, 4, 3, 'Great job on this!', 6865443, '2025-04-17 13:15:00', '2025-04-17 13:15:00', 0),
       (9, 5, 1, 'Interesting perspective.', 376547, '2025-04-17 14:00:00', '2025-04-17 14:00:00', 0),
       (10, 5, 4, 'Looking forward to more!', 2465765, '2025-04-17 14:30:00', '2025-04-17 14:30:00', 0);


INSERT INTO boards (board_id, user_id, location, content, views, likes, comments, shares, tag_user_ids, created_at,
                    updated_at, bgm_id, board_type, view_likes, view_shares, commentable)
VALUES (51, 1, 'Seoul', 'Morning jog along the Han River!', 220, 40, 10, 4, '2,3', '2025-03-01 07:30:00',
        '2025-03-01 07:30:00', 111, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (52, 1, 'Busan', 'Trying fresh sashimi at Jagalchi!', 190, 35, 8, 3, '4', '2025-03-03 12:00:00',
        '2025-03-03 12:00:00', 118, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (53, 1, 'Jeju', 'Exploring lava tubes in Jeju!', 310, 65, 15, 7, '2,5', '2025-03-05 09:45:00',
        '2025-03-05 09:45:00', 124, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (54, 1, 'Incheon', 'Sunset views at Yeongjong Bridge', 140, 20, 5, 2, '3', '2025-03-07 18:30:00',
        '2025-03-07 18:30:00', 109, 'SHORTS', 'FALSE', 'FALSE', 'TRUE'),
       (55, 1, 'Daegu', 'Exploring Seomun Market!', 260, 50, 12, 5, '1,4', '2025-03-09 11:15:00', '2025-03-09 11:15:00',
        116, 'BASIC', 'TRUE', 'TRUE', 'FALSE'),
       (56, 1, 'Gyeongju', 'Evening at Donggung Palace', 200, 38, 9, 4, '2', '2025-03-11 19:00:00',
        '2025-03-11 19:00:00', 122, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (57, 1, 'Suwon', 'Hiking at Gwanggyosan Mountain', 130, 15, 4, 1, '3,5', '2025-03-13 10:00:00',
        '2025-03-13 10:00:00', 127, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (58, 1, 'Gangneung', 'Chasing waves at Jeongdongjin', 280, 55, 13, 6, '1,2', '2025-03-15 08:00:00',
        '2025-03-15 08:00:00', 113, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (59, 1, 'Daejeon', 'Strolling through Hanbat Arboretum', 110, 12, 3, 1, '4', '2025-03-17 14:30:00',
        '2025-03-17 14:30:00', 120, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (60, 1, 'Ulsan', 'Morning hike at Gajisan', 170, 25, 6, 3, '2,3', '2025-03-19 07:00:00', '2025-03-19 07:00:00',
        108, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (61, 1, 'Seoul', 'Coffee break in Itaewon', 240, 45, 11, 5, '1,5', '2025-03-21 15:00:00', '2025-03-21 15:00:00',
        125, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (62, 1, 'Busan', 'Night lights at Yongdusan Park', 180, 30, 7, 3, '3,4', '2025-03-23 20:00:00',
        '2025-03-23 20:00:00', 110, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (63, 1, 'Jeju', 'Relaxing at Hamdeok Beach', 320, 70, 16, 8, '2', '2025-03-25 13:00:00', '2025-03-25 13:00:00',
        117, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (64, 1, 'Incheon', 'Exploring Freedom Park', 150, 22, 5, 2, '1,3', '2025-03-27 16:45:00', '2025-03-27 16:45:00',
        123, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (65, 1, 'Daegu', 'Evening at Apsan Park', 270, 52, 12, 5, '4,5', '2025-03-29 18:00:00', '2025-03-29 18:00:00',
        112, 'BASIC', 'TRUE', 'FALSE', 'TRUE'),
       (66, 1, 'Gyeongju', 'Exploring Tumuli Park', 210, 40, 10, 4, '2,3', '2025-03-31 11:30:00', '2025-03-31 11:30:00',
        119, 'BASIC', 'FALSE', 'TRUE', 'TRUE'),
       (67, 1, 'Suwon', 'Suwon’s street food scene', 140, 18, 4, 2, '1,4', '2025-04-02 13:00:00', '2025-04-02 13:00:00',
        126, 'SHORTS', 'TRUE', 'FALSE', 'FALSE'),
       (68, 1, 'Gangneung', 'Sunset at Anmok Beach', 290, 60, 14, 6, '3,5', '2025-04-04 19:00:00',
        '2025-04-04 19:00:00', 114, 'BASIC', 'TRUE', 'TRUE', 'TRUE'),
       (69, 1, 'Daejeon', 'Fun at Daejeon Aquarium', 120, 15, 3, 1, '2', '2025-04-06 14:00:00', '2025-04-06 14:00:00',
        121, 'SHORTS', 'FALSE', 'TRUE', 'FALSE'),
       (70, 1, 'Ulsan', 'Exploring Ulsan Grand Park', 180, 28, 7, 3, '1,3', '2025-04-08 10:30:00',
        '2025-04-08 10:30:00', 109, 'BASIC', 'TRUE', 'FALSE', 'TRUE');


INSERT INTO board_comments (comment_id, board_id, user_id, content, likes, created_at, update_at, reply_comment_id) VALUES
(11, 1, 2, 'Wow, Myeongdong looks amazing!', 15, '2025-03-01 10:00:00', '2025-03-01 10:00:00', 0),
(12, 1, 4, 'What food did you try?', 8, '2025-03-01 10:30:00', '2025-03-01 10:30:00', 11),
(13, 2, 3, 'Beautiful sunset!', 20, '2025-03-02 18:00:00', '2025-03-02 18:00:00', 0),
(14, 2, 5, 'Is this at Gwangan Bridge?', 5, '2025-03-02 18:15:00', '2025-03-02 18:15:00', 13),
(15, 3, 1, 'Jeju is on my list!', 25, '2025-03-03 11:30:00', '2025-03-03 11:30:00', 0),
(16, 3, 4, 'Which waterfall is this?', 10, '2025-03-03 12:00:00', '2025-03-03 12:00:00', 15),
(17, 4, 6, 'Chinatown looks colorful!', 12, '2025-03-04 15:00:00', '2025-03-04 15:00:00', 0),
(18, 4, 2, 'Did you try the jajangmyeon?', 7, '2025-03-04 15:20:00', '2025-03-04 15:20:00', 17),
(19, 5, 3, 'Daegu food is the best!', 18, '2025-03-05 13:00:00', '2025-03-05 13:00:00', 0),
(20, 5, 1, 'What’s your favorite dish?', 9, '2025-03-05 13:30:00', '2025-03-05 13:30:00', 19),
(21, 6, 5, 'Bulguksa is so serene!', 14, '2025-03-06 10:30:00', '2025-03-06 10:30:00', 0),
(22, 6, 2, 'Did you visit Seokguram too?', 6, '2025-03-06 11:00:00', '2025-03-06 11:00:00', 21),
(23, 7, 4, 'Hwaseong Fortress is stunning!', 10, '2025-03-07 19:00:00', '2025-03-07 19:00:00', 0),
(24, 7, 1, 'How’s the night view?', 4, '2025-03-07 19:15:00', '2025-03-07 19:15:00', 23),
(25, 8, 3, 'Perfect coffee spot!', 22, '2025-03-08 08:30:00', '2025-03-08 08:30:00', 0),
(26, 8, 5, 'Which café is this?', 11, '2025-03-08 09:00:00', '2025-03-08 09:00:00', 25),
(27, 9, 2, 'Expo Park looks fun!', 8, '2025-03-09 13:45:00', '2025-03-09 13:45:00', 0),
(28, 9, 4, 'Any cool exhibits?', 3, '2025-03-09 14:00:00', '2025-03-09 14:00:00', 27),
(29, 10, 1, 'Love hiking in Ulsan!', 16, '2025-03-10 10:15:00', '2025-03-10 10:15:00', 0),
(30, 10, 3, 'What trail did you take?', 7, '2025-03-10 10:45:00', '2025-03-10 10:45:00', 29),
(31, 11, 5, 'Han River picnics are the best!', 19, '2025-03-11 12:30:00', '2025-03-11 12:30:00', 0),
(32, 11, 2, 'What snacks did you bring?', 10, '2025-03-11 13:00:00', '2025-03-11 13:00:00', 31),
(33, 12, 4, 'Jagalchi Market is so lively!', 13, '2025-03-12 16:00:00', '2025-03-12 16:00:00', 0),
(34, 12, 1, 'Did you try the seafood?', 6, '2025-03-12 16:30:00', '2025-03-12 16:30:00', 33),
(35, 13, 3, 'Seongsan sunrise is breathtaking!', 28, '2025-03-13 07:00:00', '2025-03-13 07:00:00', 0),
(36, 13, 5, 'How early did you wake up?', 12, '2025-03-13 07:30:00', '2025-03-13 07:30:00', 35),
(37, 14, 2, 'Songdo Park is so relaxing!', 9, '2025-03-14 16:30:00', '2025-03-14 16:30:00', 0),
(38, 14, 4, 'Did you rent a bike?', 5, '2025-03-14 17:00:00', '2025-03-14 17:00:00', 37),
(39, 15, 1, 'Daegu nightlife looks fun!', 17, '2025-03-15 20:30:00', '2025-03-15 20:30:00', 0),
(40, 15, 3, 'Which bars did you visit?', 8, '2025-03-15 21:00:00', '2025-03-15 21:00:00', 39),
(41, 16, 5, 'Cherry blossoms are gorgeous!', 21, '2025-03-16 12:00:00', '2025-03-16 12:00:00', 0),
(42, 16, 2, 'Where in Gyeongju is this?', 10, '2025-03-16 12:30:00', '2025-03-16 12:30:00', 41),
(43, 17, 4, 'Suwon’s old town is charming!', 11, '2025-03-17 14:30:00', '2025-03-17 14:30:00', 0),
(44, 17, 1, 'Any hidden gems?', 4, '2025-03-17 15:00:00', '2025-03-17 15:00:00', 43),
(45, 18, 3, 'Surfing in Gangneung sounds epic!', 23, '2025-03-18 09:30:00', '2025-03-18 09:30:00', 0),
(46, 18, 5, 'Which beach is best for surfing?', 12, '2025-03-18 10:00:00', '2025-03-18 10:00:00', 45),
(47, 19, 2, 'Daejeon museums are awesome!', 7, '2025-03-19 14:15:00', '2025-03-19 14:15:00', 0),
(48, 19, 4, 'Which museum was your favorite?', 3, '2025-03-19 14:45:00', '2025-03-19 14:45:00', 47),
(49, 20, 1, 'Daewangam Park at sunset is stunning!', 15, '2025-03-20 19:00:00', '2025-03-20 19:00:00', 0),
(50, 20, 3, 'Did you take any photos?', 8, '2025-03-20 19:30:00', '2025-03-20 19:30:00', 49),
(51, 51, 2, 'Han River jog sounds refreshing!', 14, '2025-03-01 08:00:00', '2025-03-01 08:00:00', 0),
(52, 51, 4, 'What’s your running route?', 6, '2025-03-01 08:30:00', '2025-03-01 08:30:00', 51),
(53, 52, 3, 'Fresh sashimi is the best!', 11, '2025-03-03 12:30:00', '2025-03-03 12:30:00', 0),
(54, 52, 1, 'Which stall did you visit?', 5, '2025-03-03 13:00:00', '2025-03-03 13:00:00', 53),
(55, 53, 5, 'Lava tubes are so cool!', 20, '2025-03-05 10:15:00', '2025-03-05 10:15:00', 0),
(56, 53, 2, 'Was it crowded?', 9, '2025-03-05 10:45:00', '2025-03-05 10:45:00', 55),
(57, 54, 4, 'Yeongjong Bridge looks magical!', 10, '2025-03-07 19:00:00', '2025-03-07 19:00:00', 0),
(58, 54, 1, 'Best spot for sunset?', 4, '2025-03-07 19:30:00', '2025-03-07 19:30:00', 57),
(59, 55, 3, 'Seomun Market is a must-visit!', 16, '2025-03-09 11:45:00', '2025-03-09 11:45:00', 0),
(60, 55, 5, 'What did you buy?', 7, '2025-03-09 12:15:00', '2025-03-09 12:15:00', 59),
(61, 56, 2, 'Donggung Palace at night is beautiful!', 13, '2025-03-11 19:30:00', '2025-03-11 19:30:00', 0),
(62, 56, 4, 'Any night photography tips?', 6, '2025-03-11 20:00:00', '2025-03-11 20:00:00', 61),
(63, 57, 1, 'Gwanggyosan hike looks tough!', 9, '2025-03-13 10:30:00', '2025-03-13 10:30:00', 0),
(64, 57, 3, 'How long was the hike?', 4, '2025-03-13 11:00:00', '2025-03-13 11:00:00', 63),
(65, 58, 5, 'Jeongdongjin waves are awesome!', 18, '2025-03-15 08:30:00', '2025-03-15 08:30:00', 0),
(66, 58, 2, 'Did you try surfing?', 10, '2025-03-15 09:00:00', '2025-03-15 09:00:00', 65),
(67, 59, 4, 'Hanbat Arboretum is so peaceful!', 7, '2025-03-17 15:00:00', '2025-03-17 15:00:00', 0),
(68, 59, 1, 'Any favorite spots there?', 3, '2025-03-17 15:30:00', '2025-03-17 15:30:00', 67),
(69, 60, 3, 'Gajisan hike sounds amazing!', 12, '2025-03-19 07:30:00', '2025-03-19 07:30:00', 0),
(70, 60, 5, 'What’s the view like?', 6, '2025-03-19 08:00:00', '2025-03-19 08:00:00', 69);


INSERT INTO board_comments (comment_id, board_id, user_id, content, likes, created_at, update_at, reply_comment_id) VALUES
(71, 1, 3, 'Myeongdong night market is so vibrant!', 12, '2025-03-01 11:00:00', '2025-03-01 11:00:00', 0),
(72, 1, 5, 'Did you try the street food?', 7, '2025-03-01 11:15:00', '2025-03-01 11:15:00', 71),
(73, 1, 1, 'Love the energy here!', 20, '2025-03-01 12:00:00', '2025-03-01 12:00:00', 0),
(74, 1, 4, 'Which stalls are a must-visit?', 9, '2025-03-01 12:30:00', '2025-03-01 12:30:00', 73),
(75, 1, 6, 'The lights make it so festive!', 15, '2025-03-01 13:00:00', '2025-03-01 13:00:00', 0),
(76, 1, 2, 'Totally agree, it’s magical!', 8, '2025-03-01 13:15:00', '2025-03-01 13:15:00', 75),
(77, 1, 7, 'What’s the best food to try?', 10, '2025-03-01 14:00:00', '2025-03-01 14:00:00', 0),
(78, 1, 3, 'Tteokbokki was amazing!', 6, '2025-03-01 14:30:00', '2025-03-01 14:30:00', 77),
(79, 1, 8, 'Myeongdong is always a good time!', 18, '2025-03-01 15:00:00', '2025-03-01 15:00:00', 0),
(80, 1, 1, 'Did you shop for anything?', 5, '2025-03-01 15:30:00', '2025-03-01 15:30:00', 79),
(81, 1, 9, 'The crowd adds to the vibe!', 14, '2025-03-01 16:00:00', '2025-03-01 16:00:00', 0),
(82, 1, 4, 'It’s so lively at night!', 7, '2025-03-01 16:15:00', '2025-03-01 16:15:00', 81),
(83, 1, 10, 'Any tips for first-timers?', 11, '2025-03-01 17:00:00', '2025-03-01 17:00:00', 0),
(84, 1, 2, 'Bring cash for the stalls!', 6, '2025-03-01 17:30:00', '2025-03-01 17:30:00', 83),
(85, 1, 5, 'This place is a foodie’s dream!', 16, '2025-03-01 18:00:00', '2025-03-01 18:00:00', 0),
(86, 1, 3, 'What’s your favorite snack?', 8, '2025-03-01 18:30:00', '2025-03-01 18:30:00', 85),
(87, 1, 6, 'Myeongdong never disappoints!', 13, '2025-03-01 19:00:00', '2025-03-01 19:00:00', 0),
(88, 1, 1, 'Did you try the hotteok?', 5, '2025-03-01 19:15:00', '2025-03-01 19:15:00', 87),
(89, 1, 7, 'The atmosphere is electric!', 17, '2025-03-01 20:00:00', '2025-03-01 20:00:00', 0),
(90, 1, 4, 'So many lights and sounds!', 9, '2025-03-01 20:30:00', '2025-03-01 20:30:00', 89),
(91, 1, 8, 'Perfect spot for night vibes!', 14, '2025-03-02 09:00:00', '2025-03-02 09:00:00', 0),
(92, 1, 2, 'Did you see any street performers?', 7, '2025-03-02 09:30:00', '2025-03-02 09:30:00', 91),
(93, 1, 9, 'Myeongdong is a must-visit!', 12, '2025-03-02 10:00:00', '2025-03-02 10:00:00', 0),
(94, 1, 3, 'Any favorite stalls?', 6, '2025-03-02 10:30:00', '2025-03-02 10:30:00', 93),
(95, 1, 10, 'The food smells amazing!', 15, '2025-03-02 11:00:00', '2025-03-02 11:00:00', 0),
(96, 1, 1, 'Did you try the dumplings?', 8, '2025-03-02 11:30:00', '2025-03-02 11:30:00', 95),
(97, 1, 5, 'So much to explore here!', 13, '2025-03-02 12:00:00', '2025-03-02 12:00:00', 0),
(98, 1, 4, 'It’s like a food festival!', 7, '2025-03-02 12:30:00', '2025-03-02 12:30:00', 97),
(99, 1, 6, 'Myeongdong is so lively!', 16, '2025-03-02 13:00:00', '2025-03-02 13:00:00', 0),
(100, 1, 2, 'Did you buy any souvenirs?', 9, '2025-03-02 13:30:00', '2025-03-02 13:30:00', 99),
(101, 1, 7, 'The night market is so fun!', 14, '2025-03-02 14:00:00', '2025-03-02 14:00:00', 0),
(102, 1, 3, 'What’s the vibe like?', 6, '2025-03-02 14:30:00', '2025-03-02 14:30:00', 101),
(103, 1, 8, 'Can’t get enough of Myeongdong!', 18, '2025-03-02 15:00:00', '2025-03-02 15:00:00', 0),
(104, 1, 1, 'Did you try the fish cakes?', 10, '2025-03-02 15:30:00', '2025-03-02 15:30:00', 103),
(105, 1, 9, 'The energy is contagious!', 15, '2025-03-02 16:00:00', '2025-03-02 16:00:00', 0),
(106, 1, 4, 'So many food options!', 8, '2025-03-02 16:30:00', '2025-03-02 16:30:00', 105),
(107, 1, 10, 'Myeongdong is a food paradise!', 13, '2025-03-02 17:00:00', '2025-03-02 17:00:00', 0),
(108, 1, 2, 'Did you try the egg bread?', 7, '2025-03-02 17:30:00', '2025-03-02 17:30:00', 107),
(109, 1, 5, 'The stalls are so colorful!', 16, '2025-03-02 18:00:00', '2025-03-02 18:00:00', 0),
(110, 1, 3, 'Which food was your favorite?', 9, '2025-03-02 18:30:00', '2025-03-02 18:30:00', 109),
(111, 1, 6, 'Myeongdong is so crowded but fun!', 14, '2025-03-03 09:00:00', '2025-03-03 09:00:00', 0),
(112, 1, 1, 'How do you handle the crowds?', 6, '2025-03-03 09:30:00', '2025-03-03 09:30:00', 111),
(113, 1, 7, 'The night market is a blast!', 17, '2025-03-03 10:00:00', '2025-03-03 10:00:00', 0),
(114, 1, 4, 'Did you try the skewers?', 8, '2025-03-03 10:30:00', '2025-03-03 10:30:00', 113),
(115, 1, 8, 'Myeongdong is food heaven!', 15, '2025-03-03 11:00:00', '2025-03-03 11:00:00', 0),
(116, 1, 2, 'What’s the best dessert there?', 7, '2025-03-03 11:30:00', '2025-03-03 11:30:00', 115),
(117, 1, 9, 'The vibe is so unique!', 13, '2025-03-03 12:00:00', '2025-03-03 12:00:00', 0),
(118, 1, 3, 'Did you see any cool shops?', 6, '2025-03-03 12:30:00', '2025-03-03 12:30:00', 117),
(119, 1, 10, 'Myeongdong is so much fun!', 16, '2025-03-03 13:00:00', '2025-03-03 13:00:00', 0),
(120, 1, 1, 'Did you try the corn dogs?', 9, '2025-03-03 13:30:00', '2025-03-03 13:30:00', 119),
(121, 1, 5, 'The night market is awesome!', 14, '2025-03-03 14:00:00', '2025-03-03 14:00:00', 0),
(122, 1, 4, 'What’s the best time to visit?', 7, '2025-03-03 14:30:00', '2025-03-03 14:30:00', 121),
(123, 1, 6, 'Myeongdong is a vibe!', 12, '2025-03-03 15:00:00', '2025-03-03 15:00:00', 0),
(124, 1, 2, 'Did you try the bingsu?', 6, '2025-03-03 15:30:00', '2025-03-03 15:30:00', 123),
(125, 1, 7, 'The lights are so pretty!', 15, '2025-03-03 16:00:00', '2025-03-03 16:00:00', 0),
(126, 1, 3, 'Any tips for navigating?', 8, '2025-03-03 16:30:00', '2025-03-03 16:30:00', 125),
(127, 1, 8, 'Myeongdong is unforgettable!', 13, '2025-03-04 09:00:00', '2025-03-04 09:00:00', 0),
(128, 1, 1, 'Did you try the fried chicken?', 7, '2025-03-04 09:30:00', '2025-03-04 09:30:00', 127),
(129, 1, 9, 'The market is so lively!', 16, '2025-03-04 10:00:00', '2025-03-04 10:00:00', 0),
(130, 1, 4, 'What’s the best food stall?', 9, '2025-03-04 10:30:00', '2025-03-04 10:30:00', 129),
(131, 1, 10, 'Myeongdong is a must-see!', 14, '2025-03-04 11:00:00', '2025-03-04 11:00:00', 0),
(132, 1, 2, 'Did you try the waffles?', 6, '2025-03-04 11:30:00', '2025-03-04 11:30:00', 131),
(133, 1, 5, 'The night market is so cool!', 17, '2025-03-04 12:00:00', '2025-03-04 12:00:00', 0),
(134, 1, 3, 'Any favorite vendors?', 8, '2025-03-04 12:30:00', '2025-03-04 12:30:00', 133),
(135, 1, 6, 'Myeongdong is the best!', 15, '2025-03-04 13:00:00', '2025-03-04 13:00:00', 0),
(136, 1, 1, 'Did you try the spicy noodles?', 7, '2025-03-04 13:30:00', '2025-03-04 13:30:00', 135);

INSERT INTO board_comments (board_id, user_id, content, likes, created_at, update_at, reply_comment_id)
VALUES
    (100, 1, '이 댓글에 대한 첫 번째 답글입니다.', 2, '2025-04-18 12:00:00', '2025-04-18 12:00:00', 13),
    (100, 2, '좋은 의견 감사합니다!', 0, '2025-04-18 12:01:00', '2025-04-18 12:01:00', 13),
    (100, 3, '추가로 궁금한 점이 있습니다.', 1, '2025-04-18 12:02:00', '2025-04-18 12:02:00', 13),
    (100, 4, '이해했습니다. 동의합니다.', 3, '2025-04-18 12:03:00', '2025-04-18 12:03:00', 13),
    (100, 5, '더 자세히 설명해 주세요.', 0, '2025-04-18 12:04:00', '2025-04-18 12:04:00', 13);

-- 삽입 확인
SELECT *
FROM boards;