CREATE TABLE songs (
    resource_id INT4 NOT NULL,
    name TEXT,
    artist TEXT,
    album TEXT,
    length TEXT,
    year INT4,
    PRIMARY KEY (resource_id)
);
