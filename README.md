# Delivery Checkout RestAPI | Applaudo Studios ![Status badge](https://img.shields.io/badge/status-completed-green)

✨ Create Checkout and Orders with Keycloack as Auth Server.

## Documentation
- [You can review the documentation - Postman Collection](https://documenter.getpostman.com/view/21748987/2s8Z73zBeZ).
- [OpenAPI Spec](http://localhost:9090/api/v1/swagger-ui/).

## ⚙️ Database Configuration
You can configure to use SQLite In-Memory. Change MySQL Driver to H2 Database:
`jdbc:h2://mem:db;`

If you want MySQL Database, you can create database in your local machine called `db_applaudo` and configure credentials
in application.yml OR change to `git checkout dev-docker` branch.

## 🦀 Technologies
![SpringBoot badge](https://img.shields.io/badge/springboot-java-brightgreen)
![Keycloack badge](https://img.shields.io/badge/keycloak-jboss-yellow)
![MySQL badge](https://img.shields.io/badge/mysql-db-red)

## 🧾 License
The MIT License (MIT)