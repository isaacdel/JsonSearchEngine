# JSON Search Engine

This cmd app searches withing a array of JSON objects and returns the matching JSONs for the query.
The app supports large JSON files.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

MongoDB V3.4 (run the "mongod" from the Mongodb /bin folder prior to next step)<br />
Maven<br />
Java 1.8<br />


### Installing

From the root folder of the project (where the pom.xml file is):<br />
    1) Run maven build (to build .jar) by executing in command line:<br />
        mvn clean compile assembly:single<br />
    2) Go to "target" folder in root of project<br />
    3) Run db setup and population by executing in command line:<br />
        java -jar JSONSearch-1.0-jar-with-dependencies.jar -s "true"<br />
    4) Run the search query where:<br />
        java -jar JSONSearch-1.0-jar-with-dependencies.jar -j "JSON_FILE" -f "JSON_FIELD" -v "FIELD_VALUE"<br />
        where:<br />
            JSON_FILE = users/tickets/organizations (required)<br />
            JSON_FIELD = the query field (required)<br />
            FIELD_VALUE = the query field's value, cannot be empty.<br />
                note:<br />
                Not including this parameter will return all JSONs with the given JSON_FIELD.<br />
                In order to search for an empty value pass a space char (" ").<br />


## Running the tests

Run in cmd (root folder where the pom.xml file is) :<br />
    mvn clean install<br />

## Expanding the search to new JSON array files

Add the file to the Resources/JSON folder<br />
Create a Model for it<br />
Add it to File Validation<br />
Add it to "insertRecords" method in Main class<br />
