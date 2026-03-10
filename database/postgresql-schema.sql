-- PostgreSQL Database Schema for Avani Turmerics
-- Run this script on Railway/Render PostgreSQL database

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS cart_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS customer_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS order_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 1;

-- Products table
CREATE TABLE IF NOT EXISTS products (
    product_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('product_seq'),
    product_name VARCHAR(255) NOT NULL,
    price_per_kg DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default product (Avani Turmeric Powder)
INSERT INTO products (product_id, product_name, price_per_kg) 
VALUES (1, 'Avani Turmeric Powder', 300.00)
ON CONFLICT (product_id) DO NOTHING;

-- Cart table
CREATE TABLE IF NOT EXISTS cart (
    cart_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('cart_seq'),
    product_id INTEGER NOT NULL,
    quantity_kg DECIMAL(10, 2) NOT NULL,
    price_per_kg DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Customer details table
CREATE TABLE IF NOT EXISTS customer_details (
    customer_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('customer_seq'),
    full_name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pin_code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    order_id INTEGER PRIMARY KEY DEFAULT NEXTVAL('order_seq'),
    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity_kg DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (customer_id) REFERENCES customer_details(customer_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_cart_session ON cart(session_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date);
