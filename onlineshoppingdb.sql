CREATE DATABASE onlineshopping;
USE onlineshopping;
CREATE TABLE products (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE,
    stock INT
);
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    user_email VARCHAR(100),
    final_amount DOUBLE,
    address VARCHAR(200),
    order_date DATE,
    status VARCHAR(50)
);
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_name VARCHAR(100),
    quantity INT
);
INSERT INTO products VALUES
(1,'Laptop',65000,5),
(2,'iPhone',80000,8),
(3,'Headphones',25000,10);
INSERT INTO orders VALUES
(1,'abc@gmail.com',72000,'Hyderabad',
'2026-02-26','PLACED');
INSERT INTO order_items(order_id,product_name,quantity)
VALUES
(1,'Laptop',1),
(1,'Headphones',2);
SELECT * FROM products;
SELECT * FROM orders;

UPDATE products
SET stock = 20
WHERE id = 1;
UPDATE products SET stock = 15 WHERE id = 1;
UPDATE products SET stock = 30 WHERE id = 2;
UPDATE products SET stock = 50 WHERE id = 3;
UPDATE orders SET status='SHIPPED' WHERE order_id=1;
UPDATE orders SET status='DELIVERED' WHERE order_id=2;
UPDATE orders SET status='CANCELLED' WHERE order_id=3;
SELECT * FROM orders;
SELECT * FROM customers;
SELECT * FROM products;
SHOW DATABASES;
ALTER TABLE orders
MODIFY order_id INT AUTO_INCREMENT;
INSERT INTO products VALUES
(4,'keyboard',65000,51),
(5,'mouse',800,18),
(6,'pendrive',5000,1000);
SHOW TABLES;
SELECT * FROM order_items;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
ALTER TABLE orders AUTO_INCREMENT = 1;
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20)
);
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_status VARCHAR(50),
    payment_date DATE,
    amount DOUBLE,
    
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
ALTER TABLE orders
ADD customer_id INT;
SELECT * FROM customers;
SELECT * FROM payments;
ALTER TABLE products MODIFY id INT AUTO_INCREMENT;
ALTER TABLE orders MODIFY order_id INT AUTO_INCREMENT;

TRUNCATE TABLE products;
INSERT INTO products (name, price, stock) VALUES
('Dell Inspiron i5 Laptop', 55000, 10),
('HP Pavilion Ryzen 5 Laptop', 60000, 8),
('Lenovo ThinkPad i7', 72000, 6),
('MacBook Air M1', 95000, 5),
('MacBook Pro M2', 125000, 4),
('Asus VivoBook i3', 42000, 12),
('Acer Aspire 7', 50000, 9),
('MSI Gaming Laptop', 85000, 7),
('iPhone 13', 70000, 15),
('iPhone 14', 80000, 12),
('iPhone 15', 90000, 10),
('Samsung Galaxy S21', 65000, 14),
('Samsung Galaxy S22', 75000, 11),
('Samsung Galaxy S23', 85000, 9),
('OnePlus 11', 58000, 18),
('OnePlus Nord CE3', 28000, 20),
('Redmi Note 12', 18000, 25),
('Realme Narzo 60', 16000, 22),
('Vivo V27', 30000, 17),
('Oppo F23', 25000, 19),
('Boat Rockerz Headphones', 2000, 50),
('Sony WH-1000XM4', 25000, 15),
('JBL Tune 760NC', 8000, 20),
('Noise Bluetooth Headphones', 1500, 40),
('Apple AirPods Pro', 24000, 12),
('Samsung Galaxy Buds', 9000, 18),
('OnePlus Buds Z2', 5000, 25),
('Boat Airdopes 141', 1500, 60),
('Logitech Wireless Mouse', 800, 70),
('HP Wired Mouse', 400, 90),
('Dell Keyboard', 1000, 60),
('Mechanical Gaming Keyboard', 3500, 30),
('Redgear Gaming Mouse', 1200, 45),
('Corsair RGB Keyboard', 7000, 20),
('USB 64GB Pendrive', 600, 80),
('SanDisk 128GB Pendrive', 900, 75),
('External HDD 1TB', 4000, 25),
('Seagate 2TB HDD', 6500, 18),
('Samsung 1TB SSD', 8000, 22),
('WD 512GB SSD', 4500, 28),
('Mi 32 inch LED TV', 20000, 10),
('Samsung 43 inch Smart TV', 35000, 8),
('LG 50 inch 4K TV', 55000, 6),
('Sony Bravia 55 inch', 75000, 5),
('Fire TV Stick', 4000, 30),
('Chromecast', 3500, 25),
('Boat Bluetooth Speaker', 2500, 40),
('JBL Flip Speaker', 9000, 15),
('Sony Home Theatre', 20000, 10),
('Philips Soundbar', 12000, 12),
('Canon DSLR Camera', 45000, 7),
('Nikon DSLR Camera', 50000, 6),
('Sony Mirrorless Camera', 65000, 5),
('GoPro Hero 11', 40000, 8),
('Tripod Stand', 1500, 30),
('Camera Bag', 1200, 35),
('Mi Power Bank 10000mAh', 1200, 50),
('Ambrane Power Bank 20000mAh', 1800, 45),
('Boat Charging Cable', 300, 80),
('Type-C Fast Charger', 800, 60),
('Wireless Charger', 1500, 40),
('Apple 20W Charger', 2000, 25),
('Laptop Cooling Pad', 1500, 35),
('Laptop Bag', 1200, 50),
('Gaming Chair', 8000, 15),
('Office Chair', 5000, 20),
('Study Table', 7000, 10),
('Smart Watch Noise', 3000, 40),
('Fire-Boltt Smart Watch', 2500, 45),
('Apple Watch Series 8', 45000, 8),
('Samsung Galaxy Watch', 20000, 12),
('Mi Fitness Band', 2000, 60),
('Router TP-Link', 1500, 35),
('WiFi Extender', 2000, 30),
('Ethernet Cable', 300, 70),
('Printer HP DeskJet', 5000, 20),
('Canon Inkjet Printer', 6000, 18),
('Epson EcoTank Printer', 15000, 10),
('Scanner', 4000, 15),
('Projector', 30000, 8),
('VR Headset', 25000, 6),
('Gaming Console PS5', 55000, 5),
('Xbox Series X', 52000, 6),
('Nintendo Switch', 30000, 10),
('Game Controller', 3500, 25),
('Graphics Card RTX 3060', 35000, 7),
('Graphics Card RTX 4060', 45000, 6),
('RAM 16GB DDR4', 4000, 30),
('RAM 32GB DDR5', 9000, 20),
('Motherboard ASUS', 12000, 12),
('Processor Intel i5', 18000, 15),
('Processor AMD Ryzen 7', 25000, 10),
('UPS Backup', 5000, 18),
('Inverter Battery', 15000, 10),
('LED Desk Lamp', 800, 40),
('Extension Board', 600, 50),
('Smart Plug', 1200, 35);
SELECT * FROM products;
TRUNCATE TABLE orders;
ALTER TABLE orders
ADD payment_type VARCHAR(20);
ALTER TABLE order_items
ADD CONSTRAINT fk_order
FOREIGN KEY (order_id) REFERENCES orders(order_id)
ON DELETE CASCADE;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE products;

SET FOREIGN_KEY_CHECKS = 1;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;

SET FOREIGN_KEY_CHECKS = 1;
SELECT * FROM products;     
SELECT * FROM orders;       
SELECT * FROM order_items; 

SET FOREIGN_KEY_CHECKS = 0;TRUNCATE TABLE orders;SET FOREIGN_KEY_CHECKS = 1;

TRUNCATE TABLE customers;
TRUNCATE TABLE payments;
SELECT * FROM payments;
ALTER TABLE payments CHANGE payment_status payment_type VARCHAR(50);
DESCRIBE orders;
SELECT * FROM customers;
INSERT INTO products (name, price, stock) VALUES
('Dell Inspiron i5 Laptop', 55000, 10),
('HP Pavilion Ryzen 5 Laptop', 60000, 8),
('Lenovo ThinkPad i7', 72000, 6),
('MacBook Air M1', 95000, 5),
('MacBook Pro M2', 125000, 4),

('Asus VivoBook i3', 42000, 12),
('Acer Aspire 7', 50000, 9),
('MSI Gaming Laptop', 85000, 7),
('iPhone 13', 70000, 15),
('iPhone 14', 80000, 12),

('iPhone 15', 90000, 10),
('Samsung Galaxy S21', 65000, 14),
('Samsung Galaxy S22', 75000, 11),
('Samsung Galaxy S23', 85000, 9),
('OnePlus 11', 58000, 18),

('OnePlus Nord CE3', 28000, 20),
('Redmi Note 12', 18000, 25),
('Realme Narzo 60', 16000, 22),
('Vivo V27', 30000, 17),
('Oppo F23', 25000, 19),

('Boat Rockerz Headphones', 2000, 50),
('Sony WH-1000XM4', 25000, 15),
('JBL Tune 760NC', 8000, 20),
('Noise Bluetooth Headphones', 1500, 40),
('Apple AirPods Pro', 24000, 12),

('Samsung Galaxy Buds', 9000, 18),
('OnePlus Buds Z2', 5000, 25),
('Boat Airdopes 141', 1500, 60),
('Logitech Wireless Mouse', 800, 70),
('HP Wired Mouse', 400, 90),

('Dell Keyboard', 1000, 60),
('Mechanical Gaming Keyboard', 3500, 30),
('Redgear Gaming Mouse', 1200, 45),
('Corsair RGB Keyboard', 7000, 20),
('USB 64GB Pendrive', 600, 80),

('SanDisk 128GB Pendrive', 900, 75),
('External HDD 1TB', 4000, 25),
('Seagate 2TB HDD', 6500, 18),
('Samsung 1TB SSD', 8000, 22),
('WD 512GB SSD', 4500, 28),

('Mi 32 inch LED TV', 20000, 10),
('Samsung 43 inch Smart TV', 35000, 8),
('LG 50 inch 4K TV', 55000, 6),
('Sony Bravia 55 inch', 75000, 5),
('Fire TV Stick', 4000, 30),

('Chromecast', 3500, 25),
('Boat Bluetooth Speaker', 2500, 40),
('JBL Flip Speaker', 9000, 15),
('Sony Home Theatre', 20000, 10),
('Philips Soundbar', 12000, 12);
CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);
SELECT * FROM admin;
INSERT INTO admin (username, password)
VALUES ('admin1', 'admin123');
INSERT INTO admin (username, password)
VALUES ('admin2', 'admin1234');
SELECT * FROM order_items;
CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(100),
    product_id INT,
    quantity INT
);
SELECT * FROM cart;
SELECT * FROM products;
ALTER TABLE order_items
ADD price DOUBLE;
SELECT * FROM order_items;
ALTER TABLE orders
ADD order_time TIME;
SELECT * FROM orders;
ALTER TABLE order_items
ADD product_id INT;
DESC orders;
DESC order_items;
ALTER TABLE orders
ADD delivery_status VARCHAR(30) DEFAULT 'PLACED';

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE cart;
TRUNCATE TABLE order_items;
TRUNCATE TABLE payments;
TRUNCATE TABLE orders;
TRUNCATE TABLE customers;

SET FOREIGN_KEY_CHECKS = 1;