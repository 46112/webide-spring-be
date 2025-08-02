# Use Ubuntu as base image
FROM ubuntu:22.04

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive

# Install system dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    curl \
    wget \
    git \
    vim \
    nano \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME dynamically
RUN JAVA_HOME=$(find /usr/lib/jvm -name "java-17-openjdk-*" -type d | head -1) && \
    echo "export JAVA_HOME=$JAVA_HOME" >> /etc/environment && \
    echo "export PATH=\$PATH:\$JAVA_HOME/bin" >> /etc/environment
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Install Gradle
RUN wget -O gradle.zip https://services.gradle.org/distributions/gradle-8.5-bin.zip \
    && unzip gradle.zip -d /opt \
    && rm gradle.zip \
    && ln -s /opt/gradle-8.5/bin/gradle /usr/local/bin/gradle

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies
RUN JAVA_HOME=$(find /usr/lib/jvm -name "java-17-openjdk-*" -type d | head -1) ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Expose port
EXPOSE 8080

# Default command to run the application with continuous build
CMD ["sh", "-c", "JAVA_HOME=$(find /usr/lib/jvm -name 'java-17-openjdk-*' -type d | head -1) ./gradlew bootRun --no-daemon --continuous"] 