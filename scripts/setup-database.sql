-- Create databases
CREATE DATABASE kairos_db;
CREATE DATABASE kairos_dev_db;
CREATE DATABASE kairos_test_db;

-- Create user and grant permissions
CREATE USER kairos_user WITH PASSWORD 'kairos_pass';
GRANT ALL PRIVILEGES ON DATABASE kairos_db TO kairos_user;
GRANT ALL PRIVILEGES ON DATABASE kairos_dev_db TO kairos_user;
GRANT ALL PRIVILEGES ON DATABASE kairos_test_db TO kairos_user;

-- Connect to each database and grant schema privileges
\c kairos_db;
GRANT ALL ON SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kairos_user;

\c kairos_dev_db;
GRANT ALL ON SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kairos_user;

\c kairos_test_db;
GRANT ALL ON SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kairos_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kairos_user;
