CREATE TABLE necessities (
    id BIGSERIAL PRIMARY KEY,
    resource_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    location VARCHAR(255) NOT NULL,
    reported_date DATE NOT NULL,
    reported_by VARCHAR(255) NOT NULL,
    CONSTRAINT necessities_quantity_positive CHECK (quantity > 0)
);
