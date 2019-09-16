# Project Title

This is simple meet up registration project.


### Prerequisites
You must have java 8 installed in your machine

###Instructions
Open project with Intelij or Eclipse
Run ApplicationBootstrap.java
Open browser on http://localhost:8060
A sample form will appear infront of you
Fill the required field and hit submit
You are supposed to provide the valid values for the fields i.e. name must not contain numbers, email address must follow the email address convention, phone number must start + followed by a number.

### Installing
If you want to run with maven, please run mvn clean install and then in project home run
java -jar target/meetup-registration-0.0.1-SNAPSHOT.jar



## Running the tests

The test folder contains the integration and unit tests.
Go to pat.registration in test folder and PatRegistrationTests for integration test
Run PatRegistrationServiceTest to run unit test for Service


### Technical Decisions Made
All the endpoints in the project are contained in RegistrationController. I believe the registration endpoints are for one same purpose of registration and should be combined in one controller class.
RegistrationResource is changed to RegistrationService and it's only job will be get the data from the controller and delegate to the repository.
JbCrypt is used as a dependency to encrypt the passwords before storing them in the database.


## Author

Asmat Soomro (soomro.asmat@gmail.com) is the sole author of this project.