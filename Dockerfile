FROM centos
RUN \
  yum update -y && \
  yum install -y java-1.8.0-openjdk && \
  yum install -y wget && \
  mkdir -p /data/annotated-content-publisher-api && \
  mkdir -p /data/annotated-content-publisher-api/work && \
  mkdir -p /data/annotated-content-publisher-api/target && \
  mkdir -p /data/annotated-content-publisher-api/activemq

ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk

WORKDIR /data/annotated-content-publisher-api

COPY start-docker.sh /data/annotated-content-publisher-api
COPY target/annotated-content-publisher-0.0.1-SNAPSHOT.jar /data/annotated-content-publisher-api/target
COPY annotated-content-publisher-configuration.yml /data/annotated-content-publisher-api

WORKDIR /data/annotated-content-publisher-api/activemq

RUN \
  cd /data/annotated-content-publisher-api/activemq && \
  wget http://mirror.ox.ac.uk/sites/rsync.apache.org//activemq/5.14.4/apache-activemq-5.14.4-bin.tar.gz && \
  tar zxvf apache-activemq-5.14.4-bin.tar.gz

WORKDIR /data/annotated-content-publisher-api

EXPOSE 9102
EXPOSE 9104
EXPOSE 8161
EXPOSE 61616

CMD /data/annotated-content-publisher-api/start-docker.sh
