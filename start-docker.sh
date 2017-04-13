#!/usr/bin/env bash

# start active mq
/data/annotated-content-publisher-api/activemq/apache-activemq-5.14.4/bin/activemq start

# start the annotate-content-publisher service
java -jar target/annotated-content-publisher-0.0.1-SNAPSHOT.jar server annotated-content-publisher-configuration.yml