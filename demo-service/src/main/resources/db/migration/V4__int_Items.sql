-- Items Table
CREATE TABLE Items (
                       id uuid PRIMARY key  DEFAULT uuid_generate_v4(),
                       seller_id uuid,
                       item_name VARCHAR(255) NOT NULL,
                       description TEXT,
                       price float8 NOT NULL,
                       created TIMESTAMP DEFAULT NOW(),
                       updated TIMESTAMP DEFAULT NOW(),
    -- Other item-specific fields
                       FOREIGN KEY (seller_id) REFERENCES Users(id)
);

-- Item Characteristics Table
CREATE TABLE ItemCharacteristics (
                        id uuid PRIMARY key  DEFAULT uuid_generate_v4(),
                        item_id uuid,
                        name VARCHAR(255) NOT NULL,
                        value VARCHAR(255) NOT NULL,
                        FOREIGN KEY (item_id) REFERENCES Items(id)
);