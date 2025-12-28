-- Enable TimescaleDB extension
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- Create the telemetry table
CREATE TABLE IF NOT EXISTS telemetry (
    id BIGSERIAL PRIMARY KEY,
    sensor_id INTEGER NOT NULL,
    sensor_name VARCHAR(100) NOT NULL,
    sensor_type VARCHAR(50) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    unit VARCHAR(20),
    status VARCHAR(20),
    timestamp TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_telemetry_sensor_id ON telemetry(sensor_id);
CREATE INDEX IF NOT EXISTS idx_telemetry_timestamp ON telemetry(timestamp);
CREATE INDEX IF NOT EXISTS idx_telemetry_sensor_timestamp ON telemetry(sensor_id, timestamp);
CREATE INDEX IF NOT EXISTS idx_telemetry_sensor_type ON telemetry(sensor_type);

-- Convert the table to a hypertable (TimescaleDB feature)
-- This enables time-series optimizations
-- Note: This will fail if hypertable already exists, which is fine
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM _timescaledb_catalog.hypertable 
        WHERE hypertable_name = 'telemetry'
    ) THEN
        PERFORM create_hypertable('telemetry', 'timestamp', if_not_exists => TRUE);
    END IF;
END $$;

