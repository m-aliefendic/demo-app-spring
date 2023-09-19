CREATE TABLE Roles (
                       id SERIAL PRIMARY KEY,
                       role_name VARCHAR(255) NOT NULL,
                       description VARCHAR(255)
);

CREATE TABLE Users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       address VARCHAR(255),
                       phone_number VARCHAR(20),
                       date_of_birth DATE,
                       registration_date TIMESTAMP DEFAULT NOW(),
                       updated TIMESTAMP DEFAULT NOW()

);

-- User Roles Table
CREATE TABLE UserRoles (
                           user_id INT,
                           role_id INT,
                           FOREIGN KEY (user_id) REFERENCES Users(id),
                           FOREIGN KEY (role_id) REFERENCES Roles(id)
);