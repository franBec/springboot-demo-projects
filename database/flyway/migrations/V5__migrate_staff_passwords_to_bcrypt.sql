ALTER TABLE staff ALTER COLUMN password TYPE VARCHAR(60);

UPDATE staff SET password = '$2a$10$BuPx936L/1mmFsSEvFUanOR/dvE4nhAciLvCovbSZQxVbVA9gVDrO' WHERE staff_id = 1;
UPDATE staff SET password = '$2a$10$Gw380J97F3u0AfFfvNkJUOhbrGcaUsL9oaRoyMaoPmr07ovBLodBe' WHERE staff_id = 2;
