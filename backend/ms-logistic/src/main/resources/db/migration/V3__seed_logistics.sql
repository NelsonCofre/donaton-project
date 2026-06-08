INSERT INTO collection_centers (id, name, location) VALUES
    (1, 'Centro Acopio Valparaíso Norte', 'Valparaíso, sector El Membrillo'),
    (2, 'Centro Acopio Viña del Mar', 'Viña del Mar, calle 14 Norte'),
    (3, 'Centro Acopio Quilpué', 'Quilpué, sector El Belloto'),
    (4, 'Centro Acopio Villa Alemana', 'Villa Alemana, sector Miraflores');

INSERT INTO inventories (center_id, resource, quantity) VALUES
    (1, 'Frazadas', 450),
    (1, 'Colchonetas', 200),
    (1, 'Kits de higiene personal', 180),
    (2, 'Agua potable (litros)', 8000),
    (2, 'Leche en polvo', 500),
    (2, 'Insumos médicos (vendajes y antiséptico)', 250),
    (3, 'Alimentos no perecederos', 1200),
    (3, 'Ropa de invierno (adulto)', 700),
    (3, 'Pañales talla M', 350),
    (4, 'Mochilas escolares con útiles', 220);

INSERT INTO shipments (shipment_date, status, center_id) VALUES
    ('2026-06-06', 'PLANNED', 1),
    ('2026-06-05', 'IN_TRANSIT', 2),
    ('2026-06-04', 'DELIVERED', 3),
    ('2026-06-03', 'DELIVERED', 1),
    ('2026-06-02', 'CANCELLED', 4);

SELECT setval(pg_get_serial_sequence('collection_centers', 'id'), (SELECT COALESCE(MAX(id), 1) FROM collection_centers));
SELECT setval(pg_get_serial_sequence('inventories', 'id'), (SELECT COALESCE(MAX(id), 1) FROM inventories));
SELECT setval(pg_get_serial_sequence('shipments', 'id'), (SELECT COALESCE(MAX(id), 1) FROM shipments));
