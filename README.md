# Statement Validation Process #

### What is this repository for? ###

* Quick summary
  Rabobank customer statement need to be validated based on below conditions
  
     * all transaction references should be unique
     * end balance needs to be validated with mutation

* Version 1.0


**1. Clone the repository** 

```bash
git clone https://github.com/vijaitheerkatharisi/statement-processor.git
```

**2. Run the app using maven**

```bash
cd statement-processor
mvn spring-boot:run
```

That's it! The application can be accessed at `http://localhost:9090`.

That's it! The application can be accessed at `http://localhost:9090/swagger-ui.html`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/statement-processor-1.0.0-RELEASE.jar
```

### Who do I talk to? ###

* Vijai Theerkatharisi
* vijai.t@hotmail.com