use glimdb;
INSERT INTO users (
     username, password, name, nickname, sex, birth, content, img, phone,
     created_date, followers, followings, role, tags, rate, read_board_id, plat_Form
 ) VALUES (
              'testuser1', '1234', '홍길동', '@gil_dong', 'SEX_MALE', '1995-05-20',
              '자기소개입니다.', 'https://example.com/profile.jpg', '01012345678',
              NOW(), 10, 5, 'ROLE_USER', 'java, spring', 85, 0, 'LOCAL'
          );
