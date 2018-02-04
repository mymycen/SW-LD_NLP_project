# Search exactly match if not find dictionary

1. cd to repo folder  
``cd initBackend``
2. Run all the below commands  
 ```
 mvn clean install	
 ```
3. Run `mvn spring-boot:run` to start Spring boots server
4. Or Load the repository into IDE.
5. Locate Main.java and run the class to start Spring boots server
6. Now you must be able to send and retrieve via port 8080/nlp

http://localhost:8080/nlp?message=what is the capital of Germany?&subject=Germany&predicate=capital&onlyMatch=true
