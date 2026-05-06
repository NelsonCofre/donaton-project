-- Por si ya existía la tabla con name opcional (instalaciones anteriores).
UPDATE users SET name = trim(name);
UPDATE users SET name = 'Usuario' WHERE name IS NULL OR name = '';

ALTER TABLE users ALTER COLUMN name SET NOT NULL;
