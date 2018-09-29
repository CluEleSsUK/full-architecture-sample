X dropwizard REST endpoint
X write message onto Kafka topic
* replicate to cassandra instance
* consume from kafka topic
* save a view to database
* provide graphql instance to said view



* consume from kafka, write to cassandra with incremental backoff on fail
* consume from kafka, stick into graphql instance 
* small microservice that counts events for a given clientId or something
* allow rebuild of graphql from cassandra?