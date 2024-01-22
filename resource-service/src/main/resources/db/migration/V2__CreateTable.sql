CREATE SEQUENCE resource_seq
    MAXVALUE 2147483647;

CREATE TABLE resources (
    id INT4 NOT NULL DEFAULT nextval('resource_seq'),
    s3_key TEXT NOT NULL,
    PRIMARY KEY (id)
);
