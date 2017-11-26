## Connection Pooling

-- Setup Project
	
	Configure datasource properties in application.properties and create tables in database and pass the query  

-- To Build the project run following command 

	gradlew eclipse dependencies
	
-- To run the test cases run following command
	
	gradlew test	

-- To run the project run 
	**(Make sure you have lombok jar installed in eclipse)	

	 ConnectionPoolTest.class main method 

-- Sql create statement 
	
	CREATE TABLE recipes ( recipe_id INT NOT NULL, recipe_name VARCHAR(30) NOT NULL, PRIMARY KEY (recipe_id));

-- Sql insert statement

	INSERT INTO recipes  (recipe_id, recipe_name) VALUES(1,'Tacos');

-- Sql select statement

	select * from recipes;
	 
	
