Content API
=

Content API for managing content objects

# Integration Tests

## Installing ActiveMQ

### OSX
```
brew install apache-activemq
```

```
ActiveMQ is installed in /usr/local/Cellar/apache-activemq/x.x.x/
```

## Starting ActiveMQ

```
http://activemq.apache.org/getting-started.html#GettingStarted-StartingActiveMQStartingActiveMQ
```

```
$ /usr/local/Cellar/activemq/x.x.x/bin/activemq start
```

## Testing its running

```
http://127.0.0.1:8161/admin/
admin/admin
```

### Running Integration Test

'''
mvn clean verify -P integration-test
'''


# Quick REST test

```
docker-compose up -d
```

## For swagger documentation
```
http://localhost:8089/swagger
```

# Quick JMS test

Use ActiveMQ admin
http://127.0.0.1:8161/admin/

Goto the queues page
send a message to the annotated-content queue such as

```
POST /annotatedcontent/tsk550fnfym8?asych=false HTTP/1.1
Host: localhost:9102
Content-Type: application/vnd.ontotext.ces.document+json; charset=utf-8
X-Request-ID: 4b6c6796-0fca-11e7-93ae-92361f002671

{ "id": "tsk550fnfym8", "feature-set": [], "document-parts": { "feature-set": [{ "name": { "type": "XS_STRING", "name": "Types" }, "value": { "type": "TNS_COLLECTION", "lang": null, "value": "blogPost;" } }, { "name": { "type": "XS_STRING", "name": "IPTC" }, "value": { "type": "TNS_COLLECTION", "lang": null, "value": "" } }, { "name": { "type": "XS_STRING", "name": "PublishDate" }, "value": { "type": "XS_STRING", "lang": null, "value": "2014-06-26T13:00:55Z" } }, { "name": { "type": "XS_STRING", "name": "Genres" }, "value": { "type": "TNS_COLLECTION", "lang": null, "value": "Comment;" } }, { "name": { "type": "XS_STRING", "name": "Id" }, "value": { "type": "XS_STRING", "lang": null, "value": "tsk550fnfym8" } }, { "name": { "type": "XS_STRING", "name": "numberOfTokens" }, "value": { "type": "XS_INTEGER", "lang": null, "value": "32" } }, { "name": { "type": "XS_STRING", "name": "Sections" }, "value": { "type": "TNS_COLLECTION", "lang": null, "value": "World;" } }, { "name": { "type": "XS_STRING", "name": "Title" }, "value": { "type": "XS_STRING", "lang": null, "value": "Morning prayers" } }], "document-part": [{ "feature-set": [], "id": "0", "part": "ALL", "content": { "text": "\nJitendra Prakash/Reuters\n\n\nA Sadhu Hindu holy man offers milk at the waters of the holy river Ganga as part of his morning prayers in the northern Indian city of Allahabad\n", "node": [] } }] }, "annotation-sets": [] }
```


# Docker

## Build

```
docker build .
```
  
## Tag
### Get the image id

```
docker images
```

## Push to quay

### Login

```
docker login -e="." -u="ontotext+ontotext" -p="XXXX" quay.io
```

### tag
```
docker tag ${IMAGE} annotated-content-publisher

docker tag ${IMAGE} quay.io/ontotext/annotated-content-publisher

```

### push to quay
```
docker push quay.io/ontotext/annotated-content-publisher

```

## Run Interactive
```
docker run --name annotated-content-publisher -it annotated-content-publisher /bin/bash
```   

## Run Daemon
```
docker run --name annotated-content-publisher -d annotated-content-publisher
```

## Shell to docker container



### Get container ids
```
docker ps -a
```

```
docker exec -i -t ${CONTAINER_ID} /bin/bash
```



## Invoke

## Run via docker-compose

### Environment

Create a .env file with the correct environment settings

```
SOME_THING=XXX

```

### Interactive
```
docker-compose up
```

### Daemon
```
docker-compose up -d
```