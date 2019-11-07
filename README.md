# Weight REST service

This is a simple REST service that uses Spring Boot.  I created it to keep track
of my weight.  I weigh myself each day, and have a web page on my local intranet
that invokes this REST service to store the value.  The REST service also keeps
track of monthly and yearly averages.  The data is stored in a Postgresql database.

## Installation

This is a Maven project, so assuming you have Maven installed, you can
just clone the project to your machine and run 

mvn install

## Usage

I run this as a standalone jar.  You need to specify the username and password
for your database as properties on the command line.

## License

Licensed under the MIT license.
