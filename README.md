# TrelloApiTestAutomation
TrelloApiTestAutomation

Prerequisites: Java 11 version should be installed and JAVA_HOME should be set as local machine environment, Maven 3 version should be downloaded and should be set as maven path in local machine.
Trello key and token should be generated and used as a maven environment variable

In project folder, command at below should be run 

 mvn -Dkey=<key> -Dtoken=<token> clean surefire-report:report verify
 