# CycloCross League Manager

A suite of tools to help run a CycloCross League. The key features are that it parses the Results and updates the
Standings and also Provides Griding based on the Standings.

### Deploy Buttons

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/lukegjpotter/CycloCrossLeagueManager)

Coming soon: AWS, Railways, Heroku

### Detailed explanation

This is, in essence, an ETL Tool.

The Results processor will extract results from a RaceResults webpage, Google Sheet, or a CSV, to determine the results
of the race.

The Google Sheet with the Standings will be Updated with the results.

This tool can also provide Griding for Races. It takes in a Start List, and Grids the Riders based off the UCI Points
(sourced on CycloCross24), and the Standings Google Sheet.

### Active APIs

`/gridding` to handle the gridding for a round.

### APIs in Development

`/raceresults` to handle updating the standings after a round.

### Build and Run

This service relies on a Google `StoredCredential` that has privileges to access Google Sheets. As the CycloCross League
is
expected to run on Google Sheets for Sign-ups, Gridding, and Standings.

As this project uses Spring Boot 3, you need Java 17 to run it.  
This project uses port 8080 by default, so ensure that it's free when you're trying to run it.

#### Build.Gradle Dependencies Version Updates

To ensure that you have the latest versions of the dependencies, run the following command, and update the versions in
the `build.gradle` file:

    ./gradlew dependencyUpdates -Drevision=release -DoutputFormatter=plain --no-parallel

#### Application Properties

Be sure to set the `application.properties` for the Google Sheets for the Current Season, Previous Season, and the Year
of the upcoming World Championships. Please update these before each new season.

Be sure to also update the `application-test.properties` with links to Google Sheets with test data.

Build and Run the software in the following ways:

#### Command Line

You can run this in your Terminal by editing the `setEnvironmentalVariables.sh` or `setEnvironmentalVariables.bat`
files to set the Environmental Variable as needed.  
Be wary not to commit the updated file to a public Git Repo. Consider adding it to the `.gitignore` file.

    source setEnvironmentalVariables.sh
    ./gradlew build bootRun

#### Docker

    docker build --pull -t cyclocross-league-manager:latest .
    
    docker run --name cyclocross_league_manager \
           -p 8080:8080 \
           -d --rm cyclocross-league-manager:latest

#### IDE

You can run this in your IDE by adding the Sprint Profile Environment Variable to all of your Test Run Configurations.

    spring.profiles.active=test

#### Cloud Hosting Service

The configuration files for the Cloud Hosting Providers will contain a preset for any Environment Variable. Be sure to
set or change this value as needed.

### Operate or Test

The running service can be operated or tested in the following ways:

#### Command Line

Use Curl to provide the input to the running service.

Optional: Install JSON to format/pretty print the Response.

    sudo npm i -g json

To run the Gridding endpoint, use the following:

    curl -X POST localhost:8080/gridding \
     -H 'Content-type:application/json' \
     -d '{
             "signups" : "https://docs.google.com/spreadsheets/456",
             "gridding" : "https://docs.google.com/spreadsheets/789",
             "roundNumber" : 1
         }' | json

Then it will return

    {
        "gridding"  : "https://www.docs.google.com/spreadsheets/123",
        "errorMessage" : ""
    }

#### PostMan

There is a PostMan Collection that has the prefilled JSON Bodies. Be sure to set the Environment, as there'll be
localhost and Render supplied Environments.

PostMan
Collection: [CycloCross League Manager](https://www.postman.com/bold-moon-552911/workspace/cyclocrossleaguemanager/collection/3947605-8ef74542-9ee1-4d3e-b0dc-8e2fa3788ce8?action=share&creator=3947605&active-environment=3947605-0c8caa03-52fc-4d0f-9b75-1de68e9cffab).

### Spring Doc Open API

Formerly known as Swagger-UI, you can append the `/swagger-ui/index.html` to your URL.

Links: [localhost](http://localhost:8080/swagger-ui/index.html)
or [Render coming soon](https://cyclocrossleaguemanager.onrender.com/swagger-ui/index.html).