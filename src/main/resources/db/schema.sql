CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders(
    id SERIAL PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    order_date timestamp without time zone NOT NULL,
    shipping_method VARCHAR(50) NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    product_ids BIGINT [] NOT NULL
);
