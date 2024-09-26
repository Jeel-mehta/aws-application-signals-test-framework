/*
 * Copyright Amazon.com, Inc. or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

val javaVersion = if (project.hasProperty("javaVersion")) {
  project.property("javaVersion").toString()
} else {
  "11"
}
val javaVersionRefactored = JavaVersion.toVersion(javaVersion)

plugins {
  java
  application
  id("org.springframework.boot")
  id("io.spring.dependency-management") version "1.1.0"
  id("com.google.cloud.tools.jib")
}

group = "com.amazon.sampleapp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = javaVersionRefactored
java.targetCompatibility = javaVersionRefactored

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-logging")
  implementation ("org.apache.httpcomponents:httpclient:4.5.13")
}

jib {
  from {
    image = "openjdk:$javaVersion-jdk"
  }
  // Replace this value with the ECR Image URI
  to {
    image = "{{ECR_IMAGE_URI}}"
  }
  container {
    mainClass = "com.amazon.sampleapp.RemoteService"
    ports = listOf("8080")
  }
}

application {
  mainClass.set("com.amazon.sampleapp.RemoteService")
}