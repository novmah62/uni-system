# uni-system

[![Last Commit](https://img.shields.io/github/last-commit/novmah62/uni-system)](https://github.com/novmah62/uni-system/commits/main)
![GitHub stars](https://img.shields.io/github/stars/novmah62/uni-system?style=social)

**uni-system is a multi-service platform comprising a post management system, a centralized user management system, and a Machine Learning system to support content moderation, all strongly connected through Kafka. Notably, the system focuses on building a comprehensive Identity and Access Management (IAM) solution integrated with Keycloak to provide advanced security and user management features. Redis is used as a caching layer to synchronize user data between the Post System and User Central, improving performance and data consistency.**

## Table of Contents

- [Purpose](#purpose)
- [Main Components](#main-components)
    - [Post System](#post-system)
    - [User Central](#user-central)
    - [ML System](#ml-system)
    - [IAM with Keycloak](#iam-with-keycloak)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Installation Steps](#installation-steps)
        - [Using Docker Compose (Recommended)](#using-docker-compose-recommended)
    - [Application Configuration](#application-configuration)
- [Usage](#usage)
    - [Post System](#usage-post-system)
    - [User Central](#usage-user-central)
    - [ML System](#usage-ml-system)
    - [IAM with Keycloak](#usage-iam-with-keycloak)
- [System Architecture](#system-architecture)
- [Contributing](#contributing)

## Purpose

The `uni-system` project is built with the aim of providing a basic yet scalable and highly secure social networking platform. The system separates post and user management functionalities while integrating artificial intelligence to enhance content quality and protect users. A key highlight of the project is the implementation of a robust IAM system based on Keycloak, ensuring secure and efficient identity and access management. **Redis is used as a cache to synchronize user data between the Post System and User Central, improving performance and data consistency.**

## Main Components

### Post System

* **Functionality:** Manages the creation, reading, updating, and deletion (CRUD) of posts. Supports user interaction through commenting features. **Supports login and logout via Keycloak (SSO/SLO).**
* **Technology:** Spring Boot (Java).

### User Central

* **Functionality:** Manages user accounts (registration, login, information updates, **logout**), and provides direct messaging between users. **Supports login and logout via Keycloak (SSO/SLO).**
* **Technology:** Spring Boot (Java).

### ML System

* **Functionality:** Utilizes Machine Learning algorithms to analyze and identify comments and posts that show signs of spam.
* **Communication:** Interacts asynchronously with the Post System via Kafka for processing and reporting spam content.
* **Technology:** Python.

### IAM with Keycloak

* **Functionality:** Implements a comprehensive Identity and Access Management (IAM) system with the following features:
    * **Single Sign-On (SSO):** Allows users to log in once and access multiple applications (Post System, User Central).
    * **Time-based One-time Password (TOTP):** Provides a second layer of security for user accounts.
    * **Single Logout (SLO):** Allows users to log out of all logged-in applications via SSO.
    * **Role-Based Access Control (RBAC):** Manages access based on user roles.
* **Configuration:** Uses `keycloak-realm` to manage IAM configurations.

## Technology Stack

* **Post System:** Spring Boot (Java)
* **User Central:** Spring Boot (Java)
* **ML System:** Python
* **Message Broker:** Kafka
* **Identity and Access Management:** Keycloak
* **Cache:** Redis
* **Database (User Central):** PostgreSQL
* **Database (Keycloak):** MySQL
* **IAM Configuration:** `keycloak-realm`
* **Containerization:** Docker, Docker Compose

## Installation

### Prerequisites

Before proceeding with the installation, ensure the following software is installed on your system:

* Docker ([https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/))
* Docker Compose ([https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/))
* Java Development Kit (JDK)
* Maven (for Post System and User Central)
* Python (suitable version for ML System)
* Pip (Python package installer)

### Installation Steps

#### Using Docker Compose (Recommended)

Ensure you have a `docker-compose.yml` file in the root directory of the project with the service configurations (PostgreSQL, MySQL, Keycloak, Redis, Zookeeper, Kafka) as provided earlier.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/novmah62/uni-system.git](https://github.com/novmah62/uni-system.git)
    cd uni-system
    ```

2.  **Start the services using Docker Compose:**
    ```bash
    docker-compose up -d
    ```
    This command will download and run all the services defined in the `docker-compose.yml` file.

### Application Configuration

After the backend services are set up, you need to configure the Post System and User Central to connect to Keycloak, PostgreSQL/Redis, and Kafka. Configuration details will be in the `application.properties` or `application.yml` files of each Spring Boot application. Ensure you configure the clients in Keycloak for both Post System and User Central to support SSO and SLO.

* **Post System:** Configure the connection to Keycloak for authentication/authorization (including SSO and SLO) and Kafka for sending/receiving spam-related messages.
* **User Central:** Configure the connection to Keycloak for authentication/authorization (including SSO and SLO), PostgreSQL for the user database, and Redis for user data caching.
* **ML System:** Configure the connection to Kafka to receive messages and potentially send back results (depending on the design).

## Usage

### Post System

* Users can **log in** and **log out** of the Post System through the provided interface, using the **SSO** mechanism managed by Keycloak.
* After authentication, users can create, view, update, delete posts, and comment.
* When a new comment or post is created, the system sends a message to Kafka for the ML System to process.

### User Central

* Users can **register**, **log in**, and **log out** of the User Central system through the provided interface, using the **SSO** mechanism managed by Keycloak.
* After authentication, users can update their account information and use the direct messaging feature.
* **Single Logout (SLO):** When a user logs out of one of the systems (Post System or User Central), they will also be logged out of the other system (and Keycloak) if configured correctly.

### ML System

* The ML System listens for messages from Kafka related to new posts and comments.
* After analysis using AI algorithms, the system makes a decision on whether the content is spam.
* The results can be sent back to the Post System via Kafka or updated directly in the database (depending on the design).

### IAM with Keycloak

* Users will be redirected to the Keycloak login page for authentication when accessing the Post System or User Central (if not already logged in).
* Upon successful authentication, Keycloak will provide a token for users to access protected resources.
* TOTP can be configured in Keycloak, and users can enable it in their account settings.
* **Single Logout (SLO):** Allows users to log out of all related applications with a single logout action from any application participating in the SSO session.
* RBAC is managed in Keycloak, allowing the assignment of roles and permissions to users, thereby controlling their actions within the Post System and User Central.

## System Architecture

```mermaid
graph LR
    subgraph Post System
        PS(Spring Boot)
    end
    subgraph User Central
        UC(Spring Boot)
    end
    subgraph ML System
        ML(Python)
    end
    KB(Kafka Broker)
    KC(Keycloak)
    RD(Redis)
    PG(PostgreSQL)
    MY(MySQL)

    PS -- Sends/Receives messages --> KB
    UC -- Authenticates/Authorizes (SSO/SLO) --> KC
    PS -- Authenticates/Authorizes (SSO/SLO) --> KC
    UC -- Sends/Receives messages --> KB
    UC -- Stores data --> PG
    UC -- Caches data --> RD
    PS -- Caches data --> RD
    ML -- Receives messages --> KB
    ML -- Analyzes content --> ML
    ML -- Sends results --> KB
    KC -- Stores configuration --> MY

    style KB fill:#f9f,stroke:#333,stroke-width:2px
    style KC fill:#ccf,stroke:#333,stroke-width:2px
    style RD fill:#aaf,stroke:#333,stroke-width:2px
    style PG fill:#afa,stroke:#333,stroke-width:2px
    style MY fill:#faa,stroke:#333,stroke-width:2px
