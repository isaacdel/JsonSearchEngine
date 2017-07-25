# Json Search Engine

This cmd app searches withing a array of json objects and returns the matching jsons for the query.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

MongoDB V3.4 (run the "mongod" from the Mongodb /bin folder prior to next step)
Maven
Java 1.8


### Installing

From the root folder of the project (where the pom.xml file is):
    1) Run maven build (to build .jar) by executing in command line:
        mvn clean compile assembly:single
    2) Go to "target" folder in root of project
    3) Run db setup and population by executing in command line:
        java -jar jsonSearch-1.0-jar-with-dependencies.jar -s "true"
    4) Run the search query where:
        java -jar jsonSearch-1.0-jar-with-dependencies.jar -j "JSON_FILE" -f "JSON_FIELD" -v "FIELD_VALUE"
        where:
            JSON_FILE = users/tickets/organizations (required)
            JSON_FIELD = the query field (required)
            FIELD_VALUE = the query field's value, cannot be empty.
                note:
                Not including this parameter will return all jsons with the given JSON_FIELD.
                In order to search for an empty value pass a space char (" ").


## Running the tests

Run in cmd (root folder where the pom.xml file is) :
    mvn clean install

## Expanding the search to new Json array files

Add the file to the Resources/json folder
Create a Model for it
Add it to File Validation
Add it to "insertRecords" method in Main class
