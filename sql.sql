-- The creation of the database kaspodb and the user kaspoadmin is taken care of by Docker-compose environment set up ;
-- here we create the schema, the table and set some permissions.
CREATE SCHEMA entrln;
GRANT ALL PRIVILEGES ON DATABASE kaspodb TO kaspoadmin;
GRANT ALL PRIVILEGES ON SCHEMA entrln TO kaspoadmin;
-- create the test table
CREATE TABLE entrln.analytics_activity (
    userid VARCHAR(255),
    clientid VARCHAR(255),
    activity_type VARCHAR(255),
    bucket VARCHAR(255)
);
GRANT ALL PRIVILEGES ON TABLE entrln.analytics_activity TO kaspoadmin;

-- Run a quick test to ensure the table is created correctly.
INSERT INTO entrln.analytics_activity (userid, clientid, activity_type, bucket) VALUES
    ('dummy_user_1', 'client1', 'page_view', 'bucketA'),
    ('dummy_user_2', 'client2', 'click', 'bucketB'),
    ('dummy_user_3', 'client3', 'purchase', 'bucketC');

SELECT * FROM entrln.analytics_activity;
DELETE FROM entrln.analytics_activity;
SELECT * FROM entrln.analytics_activity;