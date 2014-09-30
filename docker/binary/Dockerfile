FROM dockerfile/java:oracle-java8
# using this until a stable OpenJDK 8 JRE is available on Docker Hub, possibly Zulu

MAINTAINER Emerson Farrugia <emerson@openmhealth.org>

RUN mkdir /root/omh-dsu-ri
ADD omh-dsu-ri.jar /root/omh-dsu-ri/dsu-ri.jar
EXPOSE 8083

CMD /usr/bin/java -jar /root/omh-dsu-ri/dsu-ri.jar
