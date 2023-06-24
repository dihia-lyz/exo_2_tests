create table if not exists annonce
(
    id          varchar primary key,
    titre       varchar,
    prix        INT8,
    description varchar,
    typeA       varchar
);