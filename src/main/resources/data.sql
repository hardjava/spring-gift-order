ALTER TABLE wish ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE wish ADD COLUMN updated_at TIMESTAMP;

INSERT INTO product (name, price, image_url)
VALUES ('시어버터 에센셜 패키지', 30000, 'https://example.com/strawberry.jpg');

INSERT INTO product_option (option_name, quantity, product_id)
VALUES ('01. [Best] 시어버터 핸드 & 시어 스틱 립 밤', 567, 1);

INSERT INTO users (email, password, role)
VALUES ('example@example.com', 'f47c4b720cd560809b27592e4933f897170e7cfe999a3f80d04c7b2887aa8843', 'admin');

INSERT INTO refresh_token (user_id, refresh_token)
VALUES (1, 'some-refresh-token-value');