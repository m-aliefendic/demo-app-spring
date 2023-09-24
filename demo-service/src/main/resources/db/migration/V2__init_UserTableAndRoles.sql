CREATE TABLE ROLES (
                       id BIGINT PRIMARY KEY,
                       role_name VARCHAR(255) NOT NULL,
                       description VARCHAR(255)
);

CREATE TABLE USERS (
                       id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       address VARCHAR(255),
                       language VARCHAR(255),
                       salt VARCHAR(255),
                       phone_number VARCHAR(20),
                       date_of_birth TIMESTAMP,
                       registration_date TIMESTAMP DEFAULT NOW(),
                       updated TIMESTAMP DEFAULT NOW()
);

-- User Roles Table
CREATE TABLE USER_ROLES (
                           user_id UUID,
                           role_id BIGINT,
                           FOREIGN KEY (user_id) REFERENCES Users(id),
                           FOREIGN KEY (role_id) REFERENCES Roles(id)
);