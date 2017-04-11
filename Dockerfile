FROM centos
RUN \
  yum update -y && \
  yum install -y java-1.8.0-openjdk && \
  yum install -y wget && \
  mkdir -p /data/content-api \
  mkdir -p /data/content-api/work \
  mkdir -p /data/content-api/target

WORKDIR data/content-api

COPY start.sh /data/content-api
COPY target/content-0.0.1-SNAPSHOT.jar /data/content-api/target
COPY content-configuration.yml /data/content-api

EXPOSE 9102
EXPOSE 9104

ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk
CMD /data/content-api/start.sh
