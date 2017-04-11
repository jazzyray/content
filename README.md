Content API
=

Content API for managing content objects


# Quick test

```
docker-compose up -d
```

## For swagger documentation
```
http://localhost:8089/swagger
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