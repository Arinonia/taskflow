plugins {
    id("java")
}

group = "fr.arinonia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.jfoenix:jfoenix:8.0.10")
}

tasks.test {
    useJUnitPlatform()
}