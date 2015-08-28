CREATE TABLE IF NOT EXISTS twitter_statuses (
    id          bigint,
    user_id     bigint,
    text        text,
    latitude    real,
    longitude   real,
    CONSTRAINT twitter_status_pk PRIMARY KEY (id, user_id)
)

