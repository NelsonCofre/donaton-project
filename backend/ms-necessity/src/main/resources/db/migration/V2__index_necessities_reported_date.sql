-- Índice para consultas por rango de fechas o ordenación por fecha de reporte.
CREATE INDEX idx_necessities_reported_date ON necessities (reported_date);
