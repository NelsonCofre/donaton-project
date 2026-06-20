CREATE TABLE donations (
    id BIGSERIAL PRIMARY KEY,
    resource_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    origin VARCHAR(255) NOT NULL,
    donation_date DATE NOT NULL,
    warehouse_name VARCHAR(255) NOT NULL,
    CONSTRAINT donations_quantity_positive CHECK (quantity > 0)
);
