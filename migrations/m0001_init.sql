DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profile;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS imgs;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS logs;

CREATE TABLE IF NOT EXISTS users (
    uid uuid PRIMARY KEY,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    age int CHECK ( age > 18 ),
    gender CHARACTER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profile (
    uid uuid REFERENCES users(uid),
    bio VARCHAR(255),
    status BOOL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS posts (
    pid uuid PRIMARY KEY,
    uid uuid REFERENCES users(uid),
    post VARCHAR(512),
    likes int,
    dislikes int,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS imgs (
    id uuid PRIMARY KEY,
    pid uuid REFERENCES posts(pid),
    img bytea
);

CREATE TABLE IF NOT EXISTS chats (
    cid uuid PRIMARY KEY,
    sender uuid,
    receiver uuid,
    msg VARCHAR(512),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS log (
    id uuid PRIMARY KEY,
    event VARCHAR(512),
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);