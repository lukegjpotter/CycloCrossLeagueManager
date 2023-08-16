# CycloCross League Manager

A suite of tools to help run a CycloCross League. The key features are that it parses the Results and updates the
Standings and also Provides Griding based on the Standings.

### Deploy Buttons

AWS, Render, Railways, Heroku

### Detailed explanation

This is in essence an ETL Tool.

The Results processor will extract results from a RaceResults webpage, Google Sheet, or a CSV, to determine the results
of the race.

The Google Sheet with the Standings will be Updated with the results.

This tool can also provide Griding for Races. It takes in a Start List, and Grids the Riders based off the UCI Points
(sourced on CycloCross24), and the Standings Google Sheet.

### Build, Run and Test

Enable Google Sheets API Usage ...how?

Set the Properties ...which properties?

Build the software with

    ./gradlew build bootRun

Use Curl to provide a Results Source.

    curl ...
