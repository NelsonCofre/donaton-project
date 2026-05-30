CREATE TABLE collection_centers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE inventories (
    id BIGSERIAL PRIMARY KEY,
    center_id BIGINT NOT NULL REFERENCES collection_centers (id),
    resource VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT inventories_quantity_positive CHECK (quantity > 0)
);

CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    shipment_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    center_id BIGINT NOT NULL REFERENCES collection_centers (id)
);
