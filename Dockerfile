FROM anapsix/alpine-java

USER root

RUN apk upgrade --update && \
    apk add --no-cache libstdc++ &&\
    apk add --update curl bash && \
    rm -rf /var/cache/apk/*

ENV JAVA_OPTS -Xms256m -Xmx1024m

EXPOSE 2127

COPY build/libs/CallLogManager-all-1.0-SNAPSHOT.jar /CallLogDriver.jar

WORKDIR /

ENTRYPOINT ["java","-jar","CallLogDriver.jar"]