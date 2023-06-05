import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
    java
}

group = "me.xdgamer90000.botwows"
version = "1"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://m2.chew.pro/releases" )
    maven ( "https://jitpack.io/" )
    maven { url = uri("https://m2.chew.pro/snapshots") }
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.2")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    //implementation("pw.chew:jda-chewtils:1.23.0")
    implementation("pw.chew:jda-chewtils-command:2.0-SNAPSHOT")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("org.json:json:20211205")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.github.Vincentvibe3:ef-player:v.1.2.12")
    implementation("pw.chew:jda-chewtils:2.0-SNAPSHOT")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")



}

tasks.test {
    useJUnit()
}

tasks.withType<ShadowJar>{
    archiveFileName.set("botwows-${project.version}.jar")
}

application {
    mainClass.set("me.xdgamer90000.botwows.App")
}

project.gradle.startParameter.excludedTaskNames.add("jar")