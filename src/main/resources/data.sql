DELETE FROM sales_orders;
DELETE FROM customers;
DELETE FROM customer_profiles;

INSERT INTO customer_profiles (version, phone_number, billing_address, shipping_address) VALUES
(0, '+1-201-555-0100', 'North Caldwell, NJ', 'Soprano House, North Caldwell, NJ'),
(0, '+1-201-555-0101', 'North Caldwell, NJ', 'Soprano Kitchen Entrance, NJ'),
(0, '+1-973-555-0102', 'Newark, NJ', 'Apartment, Newark, NJ'),
(0, '+1-973-555-0103', 'Belleville, NJ', 'Nuovo Vesuvio Back Room, NJ'),
(0, '+1-973-555-0104', 'Newark, NJ', 'Bada Bing Office, NJ'),
(0, '+1-201-555-0105', 'West Orange, NJ', 'Sacrimoni Residence, NJ'),
(0, '+1-973-555-0106', 'Nutley, NJ', 'Janice Apartment, NJ'),
(0, '+1-973-555-0107', 'Essex County, NJ', 'Satriale''s Pork Store, NJ');

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Tony Soprano', 'tony.soprano@example.com', DATE '2026-01-10',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-201-555-0100'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Carmela Soprano', 'carmela.soprano@example.com', DATE '2026-01-11',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-201-555-0101'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Christopher Moltisanti', 'christopher.moltisanti@example.com', DATE '2026-01-13',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-973-555-0102'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Paulie Gualtieri', 'paulie.gualtieri@example.com', DATE '2026-01-14',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-973-555-0103'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Silvio Dante', 'silvio.dante@example.com', DATE '2026-01-15',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-973-555-0104'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Johnny Sack', 'john.sacrimoni@example.com', DATE '2026-01-16',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-201-555-0105'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Janice Soprano', 'janice.soprano@example.com', DATE '2026-01-18',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-973-555-0106'));

INSERT INTO customers (version, name, email, created_at, profile_id)
VALUES (0, 'Artie Bucco', 'artie.bucco@example.com', DATE '2026-01-20',
    (SELECT id FROM customer_profiles WHERE phone_number = '+1-973-555-0107'));

INSERT INTO sales_orders (version, customer_id, order_date, status, total_amount) VALUES
(0, (SELECT id FROM customers WHERE email = 'tony.soprano@example.com'), DATE '2026-02-01', 'PENDING', 320.50),
(0, (SELECT id FROM customers WHERE email = 'tony.soprano@example.com'), DATE '2026-02-05', 'CONFIRMED', 185.00),
(0, (SELECT id FROM customers WHERE email = 'tony.soprano@example.com'), DATE '2026-02-11', 'DELIVERED', 710.40),
(0, (SELECT id FROM customers WHERE email = 'carmela.soprano@example.com'), DATE '2026-02-03', 'SHIPPED', 410.75),
(0, (SELECT id FROM customers WHERE email = 'carmela.soprano@example.com'), DATE '2026-02-08', 'DELIVERED', 96.30),
(0, (SELECT id FROM customers WHERE email = 'christopher.moltisanti@example.com'), DATE '2026-02-09', 'DELIVERED', 99.99),
(0, (SELECT id FROM customers WHERE email = 'christopher.moltisanti@example.com'), DATE '2026-02-15', 'CANCELLED', 240.00),
(0, (SELECT id FROM customers WHERE email = 'paulie.gualtieri@example.com'), DATE '2026-02-06', 'CONFIRMED', 150.00),
(0, (SELECT id FROM customers WHERE email = 'silvio.dante@example.com'), DATE '2026-02-10', 'SHIPPED', 500.00),
(0, (SELECT id FROM customers WHERE email = 'john.sacrimoni@example.com'), DATE '2026-02-12', 'PENDING', 875.25),
(0, (SELECT id FROM customers WHERE email = 'janice.soprano@example.com'), DATE '2026-02-13', 'DELIVERED', 129.49),
(0, (SELECT id FROM customers WHERE email = 'artie.bucco@example.com'), DATE '2026-02-14', 'CONFIRMED', 349.95),
(0, (SELECT id FROM customers WHERE email = 'artie.bucco@example.com'), DATE '2026-02-16', 'PENDING', 52.20);
