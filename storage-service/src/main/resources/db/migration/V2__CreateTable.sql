CREATE SEQUENCE storage_seq
    MAXVALUE 2147483647;

CREATE TABLE storages (
    id INT4 NOT NULL DEFAULT nextval('storage_seq'),
    storage_type TEXT NOT NULL UNIQUE,
    bucket_name TEXT NOT NULL,
    path TEXT NOT NULL,
    PRIMARY KEY (id)
);
