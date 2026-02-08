plugins {
    id("java")
    id("war")
}

group = "ua.opnu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    providedCompile("javax.servlet:javax.servlet-api:4.0.1")

    implementation("org.thymeleaf:thymeleaf:3.0.15.RELEASE")

    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.thymeleaf.extras:thymeleaf-extras-java8time:3.0.4.RELEASE")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}