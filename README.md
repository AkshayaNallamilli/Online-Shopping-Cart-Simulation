# 🛒 Online Shopping Cart Simulation

## 📖 Overview

Online Shopping Cart Simulation is a Java Swing-based desktop application that replicates the functionality of a real-world e-commerce platform. The system enables users to browse products, manage shopping carts, place orders, complete payments, and track order history. It also provides an administrative dashboard for inventory and order management.

The application uses **Java Swing** for the graphical user interface and **MySQL** for data storage and management.



## ✨ Features

### 👤 User Features
- User Login
- Browse & Search Products
- Add Items to Cart
- Checkout & Payments
- Order History

### 🔐 Admin Features
- Admin Login
- Product Management
- Inventory Management
- Order Monitoring
- Order Status Updates

### 💳 Payment Options
- Online Payment
- Cash on Delivery (COD)

### 🗄️ Database Features
- Customer Management
- Cart Management
- Order Management
- Payment Records
- Stock Tracking



## 🛠️ Technologies Used

| Technology | Purpose                    |
| ---------- | -------------------------- |
| Java       | Application Development    |
| Java Swing | GUI Development            |
| JDBC       | Database Connectivity      |
| MySQL      | Database Management        |
| OOP        | Software Design Principles |



## 🗄️ Database

The project uses a MySQL database named:

```sql
onlineshopping
```

### Database Tables

* products
* customers
* cart
* orders
* order_items
* payments
* admin



## 📂 Project Structure

```text
Online-Shopping-Cart-Simulation
│
├── OnlineShoppingGUI.java
├── onlineshoppingdb.sql
├── README.md
│
├── User Module
├── Admin Module
├── Product Management
├── Shopping Cart
├── Checkout System
├── Payment System
├── Order Management
└── Inventory Management
```



## 🚀 How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/AkshayaNallamilli/Online-Shopping-Cart-Simulation.git
```

### 2. Create Database

Import the provided SQL file:

```sql
SOURCE onlineshoppingdb.sql;
```

Or import it through MySQL Workbench.

### 3. Configure Database Connection

Update the MySQL credentials in the Java source code if necessary:

```java
jdbc:mysql://localhost:3307/onlineshopping
```

### 4. Run the Application

Compile and run:

```text
OnlineShoppingGUI.java
```



## 📋 Application Workflow

### User Side

1. Login using Email and Mobile Number
2. Browse Available Products
3. Search Products
4. Add Products to Cart
5. Proceed to Checkout
6. Enter Delivery Address
7. Select Payment Method
8. Place Order
9. View Order History

### Admin Side

1. Login as Administrator
2. View All Orders
3. Update Order Status
4. Manage Products
5. Update Inventory
6. Monitor Customer Orders



## 📸 Screenshots

### Home Screen

<img width="1281" height="711" alt="image" src="https://github.com/user-attachments/assets/a5294b16-71bf-45d2-966d-bfbf214a3992" />


### User Dashboard
<img width="977" height="737" alt="image" src="https://github.com/user-attachments/assets/857e0eed-c7ad-4f2d-bef9-5636c3263ec9" />



### Admin Dashboard

<img width="967" height="732" alt="image" src="https://github.com/user-attachments/assets/d6433c56-bb5c-4c01-8d0f-5db1dcab0dec" />




## 🔮 Future Enhancements

* User Registration System
* Product Categories
* Product Reviews and Ratings
* Wishlist Functionality
* Email Notifications
* Sales Analytics Dashboard
* PDF Invoice Generation
* Multi-Admin Support



## 👨‍💻 Author

**Akshaya Nallamilli**

B.Tech Student | Aspiring Software Developer



## 📄 License

This project is developed for educational and learning purposes only.


⭐ If you find this project useful, consider giving the repository a star.
