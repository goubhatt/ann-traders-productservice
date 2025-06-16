CREATE TABLE IF NOT EXISTS anntraders.products (
    id BIGINT PRIMARY KEY DEFAULT nextval('anntraders.products_id_seq'),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    ownerid BIGINT NOT NULL REFERENCES anntraders.users(id),

    createdby VARCHAR(20),
    createtime TIMESTAMP DEFAULT NOW(),
    updatedby VARCHAR(20),
    updatetime TIMESTAMP
);
