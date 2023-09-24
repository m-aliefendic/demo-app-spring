
CREATE TABLE TOKEN (
                       id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                       user_id UUID NOT NULL,
                       created_on TIMESTAMP NOT NULL,
                       expires_on TIMESTAMP NOT NULL
);