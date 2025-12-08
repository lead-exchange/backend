DELETE
FROM matches;

ALTER TABLE matches
    ADD COLUMN common_status varchar(50) NOT NULL;
