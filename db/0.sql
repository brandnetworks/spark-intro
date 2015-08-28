-- Make the schema
CREATE SCHEMA IF NOT EXISTS test;

-- Make the user, give it full access
CREATE USER test PASSWORD 'development';
GRANT ALL ON SCHEMA test TO test;
GRANT ALL ON ALL TABLES IN SCHEMA test TO test;
