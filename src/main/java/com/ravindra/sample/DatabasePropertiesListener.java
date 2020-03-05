package com.ravindra.sample;

import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabasePropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

	private final static String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
	private final static String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";
	private final static String SPRING_REDIS_HOST = "spring.redis.host";
	private final static String SPRING_REDIS_PORT = "spring.redis.port";
	
	

	private ObjectMapper mapper = new ObjectMapper();
	
    // sample codes from AWS
	private String getSecret() {
		
		
		
		String secretName = "prod/key";
	    String region = "ap-south-1";

	    // Create a Secrets Manager client
	    AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
	                                    .withRegion(region)
	                                    .build();
	    
	    // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
	    // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
	    // We rethrow the exception by default.
	    
	    String secret = null, decodedBinarySecret = null;
	    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
	                    .withSecretId(secretName);
	    GetSecretValueResult getSecretValueResult = null;

	    try {
	        getSecretValueResult = client.getSecretValue(getSecretValueRequest);
	    } catch (DecryptionFailureException e) {
	        // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
	        // Deal with the exception here, and/or rethrow at your discretion.
	        throw e;
	    } catch (InternalServiceErrorException e) {
	        // An error occurred on the server side.
	        // Deal with the exception here, and/or rethrow at your discretion.
	        throw e;
	    } catch (InvalidParameterException e) {
	        // You provided an invalid value for a parameter.
	        // Deal with the exception here, and/or rethrow at your discretion.
	        throw e;
	    } catch (InvalidRequestException e) {
	        // You provided a parameter value that is not valid for the current state of the resource.
	        // Deal with the exception here, and/or rethrow at your discretion.
	        throw e;
	    } catch (ResourceNotFoundException e) {
	        // We can't find the resource that you asked for.
	        // Deal with the exception here, and/or rethrow at your discretion.
	        throw e;
	    }

	    // Decrypts secret using the associated KMS CMK.
	    // Depending on whether the secret is a string or binary, one of these fields will be populated.
	    if (getSecretValueResult.getSecretString() != null) {
	        secret = getSecretValueResult.getSecretString();
	    }
	    else {
	        decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
	    }

		
		
	    return secret != null ? secret : decodedBinarySecret;
		
		
      }
	
	
	
	
	private String getString(String json, String path) {
        try {
			JsonNode root = mapper.readTree(json);
			return root.path(path).asText();
		} catch (IOException e) {
			System.out.println("Can't get {} from json {}"+ path+ json+ e);
			return null;
		}
	}

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		
		
		
		// Get username and password from AWS Secret Manager using secret name
		
		
		
		String secretJson = getSecret();
		String dbUser = getString(secretJson, "username");
		String dbPassword = getString(secretJson, "password");
		String redishost = getString(secretJson, "redishost");
		String redisport = getString(secretJson, "redisport");
		

		ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
		Properties props = new Properties();
		props.put(SPRING_DATASOURCE_USERNAME, dbUser);
		props.put(SPRING_DATASOURCE_PASSWORD, dbPassword);
		props.put(SPRING_REDIS_HOST, redishost);
		props.put(SPRING_REDIS_PORT, redisport);
		environment.getPropertySources().addFirst(new PropertiesPropertySource("aws.secret.manager", props));
		
	}

  

	
}