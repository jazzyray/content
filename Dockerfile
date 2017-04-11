FROM centos
RUN \
  yum update -y && \
  yum install -y java-1.8.0-openjdk && \
  yum install -y wget && \
  mkdir -p /data/annotated-content-publisher-api \
  mkdir -p /data/annotated-content-publisher-api/work \
  mkdir -p /data/annotated-content-publisher-api/target

WORKDIR data/annotated-content-publisher-api

COPY start.sh /data/annotated-content-publisher-api
COPY target/annotated-content-publisher-0.0.1-SNAPSHOT.jar /data/annotated-content-publisher-api/target
COPY annotated-content-publisher-configuration.yml /data/annotated-content-publisher-api

EXPOSE 9102
EXPOSE 9104

ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk
CMD /data/annotated-content-publisher-api/start.sh
