-- ============================================================================
-- Flyway Migration V3: Grant DML Privileges to Application User
-- ============================================================================
--
-- This migration enforces the principle of least privilege by granting
-- the sakila_app user (created by postgres/init-users.sh) with DML-only
-- privileges (SELECT, INSERT, UPDATE, DELETE) on all tables and sequences.
--
-- The sakila (admin) user retains full DDL privileges for future migrations.
--

-- Grant schema usage
GRANT USAGE ON SCHEMA public TO sakila_app;

-- Grant DML privileges on all existing tables
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO sakila_app;

-- Grant sequence privileges for auto-increment IDs (SERIAL columns)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO sakila_app;

-- Set default privileges for future tables created by sakila (admin) user
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO sakila_app;

-- Set default privileges for future sequences
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO sakila_app;
