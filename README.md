*** Requirements

Steps to achieve the springboot interaction with secretsmanager

1. AWS cli (Install & configure with IAM Access key & Secret key)
   # Make sure it contains iam policy to access with Secret manager
   
2. AWS console (setup the keys in secret manager service ex: username : root, password : password )

3. Modify values in the ListenerClass in Springboot code

4. Install mysql and setup the database ex: dbdemo

5. Modify the database name and port in the application.properties

6. Optional (leverage the redis container for session and cache)
