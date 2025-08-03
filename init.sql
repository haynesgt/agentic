CREATE EXTENSION IF NOT EXISTS dblink;

CREATE OR REPLACE PROCEDURE create_db_if_not_exists(db_name TEXT)
LANGUAGE plpgsql
AS $$
DECLARE
    exists boolean;
BEGIN
    SELECT EXISTS (
        SELECT FROM pg_database WHERE datname = db_name
    ) INTO exists;

    IF NOT exists THEN
        PERFORM dblink_exec(
            'dbname=postgres',
            format('CREATE DATABASE %I', db_name)
        );
    END IF;
END;
$$;

CALL create_db_if_not_exists('agentic_temporal_dev');
CALL create_db_if_not_exists('agentic_dev');
