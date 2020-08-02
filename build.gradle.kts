plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("org.openjfx.javafxplugin") version "0.0.9"
    kotlin("plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    maven
}

group = rootProject.group
version = rootProject.version

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    compileJava {
        sourceCompatibility = JavaVersion.VERSION_13.toString()
        targetCompatibility = JavaVersion.VERSION_13.toString()
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "13"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "13"
        }
    }
    jar {
        enabled = true
    }
    bootJar {
        enabled = false
    }
}

javafx{
    version="14"
    modules("javafx.graphics","javafx.controls","javafx.media","javafx.web","javafx.fxml")
}
