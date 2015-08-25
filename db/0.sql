-- Make the schema
CREATE SCHEMA IF NOT EXISTS spider;

-- Make the user, give it full access
CREATE USER spider_rw PASSWORD 'development';
GRANT ALL ON SCHEMA spider TO spider_rw;
GRANT ALL ON ALL TABLES IN SCHEMA spider TO spider_rw;
