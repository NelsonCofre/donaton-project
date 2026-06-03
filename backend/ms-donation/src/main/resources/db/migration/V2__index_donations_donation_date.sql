-- Índice para consultas por rango de fechas o ordenación por fecha de donación.
CREATE INDEX idx_donations_donation_date ON donations (donation_date);
