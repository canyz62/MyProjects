FROM openjdk:11

ENV GRADLE_USER_HOME=/workspace/.gradle

COPY ./ /workspace
WORKDIR /workspace

RUN ls -la && \
    chmod +x ./gradlew && \
    ./gradlew assemble && \
    ls -la && \
    mkdir /dependencies && \
    cp -r build /dependencies && \
    cp -r .gradle /dependencies && \
    cd / && rm -r workspace
