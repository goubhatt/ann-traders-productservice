CREATE TABLE IF NOT EXISTS anntraders.users (
    id BIGINT PRIMARY KEY DEFAULT nextval('anntraders.users_id_seq'),
    cognitosub VARCHAR(50) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    role VARCHAR(20), -- optional, for custom app roles

    createdby VARCHAR(20),
    createtime TIMESTAMP DEFAULT NOW(),
    updatedby VARCHAR(20),
    updatetime TIMESTAMP
);
