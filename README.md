# CycloCross League Manager

A suite of tools to help run a CycloCross League. The key features are that it parses the Results and updates the
Standings and also Provides Griding based on the Standings.

### Deploy Buttons

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/lukegjpotter/CycloCrossLeagueManager)

Coming soon: AWS, Railways, Heroku

### Detailed explanation

This is in essence an ETL Tool.

The Results processor will extract results from a RaceResults webpage, Google Sheet, or a CSV, to determine the results
of the race.

The Google Sheet with the Standings will be Updated with the results.

This tool can also provide Griding for Races. It takes in a Start List, and Grids the Riders based off the UCI Points
(sourced on CycloCross24), and the Standings Google Sheet.

### Active APIs

none

### APIs in Development

`/gridding` to handle the gridding for a round.
`/raceresults` to handle updating the standings after a round.

### Build and Run

This services relies on a Google API Key that has privileges to access Google Sheets. As the CycloCross League is
expected to run on Google Sheets for Sign-ups, Gridding and Standings. So the ways to Build and Run detail ways to set
the `GOOGLE_SHEETS_API_KEY` environment variable.

As this project uses Spring Boot 3, you need Java 17 to run it.  
This project uses port 8080 by default, so ensure that it's free when you're trying to run it.

`ToDo:` How to generate the Google Sheets API Key????

Build and Run the software in the following ways:

#### Command Line

You can run this in your Terminal by editing the `setEnvironmentalVariables.sh` or `setEnvironmentalVariables.bat`
files to set the Environmental Variable to your API Key.  
Be wary not to commit the updated file to a public Git Repo. Consider adding it to the `.gitignore` file.

    source setEnvironmentalVariables.sh
    ./gradlew build bootRun

#### Docker

    docker build --pull -t cyclocross-league-manager:latest .
    
    docker run --name cyclocross_league_manager \
           -p 8080:8080 \
           --env=GOOGLE_SHEETS_API_KEY='123456789' \
           -d --rm cyclocross-league-manager:latest

#### IDE

You can run this in your IDE by adding the Environment Variable to your Run Configurations.

    GOOGLE_SHEETS_API_KEY = 123456789

#### Cloud Hosting Service

The configuration files for the Cloud Hosting Providers will contain a preset for the `GOOGLE_SHEETS_API_KEY`
Environment Variable. Be sure to set or change this value as needed.

### Operate or Test

The running service can be operated or tested in the following ways:

#### Command Line

Use Curl to provide the input to the running service.

Optional: Install JSON to format/pretty print the Response.

    sudo npm i -g json

To run the Gridding endpoint, use the following:

    curl -X POST localhost:8080/route \
     -H 'Content-type:application/json' \
     -d '{"...": "..."} | json

Then it will return

    {
        "..."  : "https://www.docs.google.com/spreadsheets/123",
        "errorMessage" : ""
    }

#### PostMan

There is a PostMan Collection that has the prefilled JSON Bodies. Be sure to set the Environment, as there'll be
localhost and Render supplied Environments.

Coming Soon: [PostMan Collection](#).

### Spring Doc Open API

Formerly known as Swagger-UI, you can append the `/swagger-ui/index.html` to your URL.

Links: [localhost](http://localhost:8080/swagger-ui/index.html) or [Render coming soon](#).