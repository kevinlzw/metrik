import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktlint: Configuration by configurations.creating

plugins {
    idea
    application
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("io.gitlab.arturbosch.detekt").version("1.17.1")
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("kapt") version "1.4.21"
    jacoco
}

dependencies {
    ktlint("com.pinterest:ktlint:0.39.0")
    implementation(kotlin("stdlib-jdk8"))
}


group = "metrik-backend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter {
        content {
            includeGroup("org.jetbrains.kotlinx")
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.0")
    }
}

// Configure Api test framework
idea {
    module {
        sourceDirs = sourceDirs - file("src/apiTest/kotlin")
        testSourceDirs = testSourceDirs + file("src/apiTest/kotlin")
    }
}

sourceSets {
    create("apiTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
        java.srcDir("src/apiTest/kotlin")
        resources.srcDir("src/apiTest/kotlin")
    }
}

val apiTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

configurations["apiTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["apiTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
// End of Configure Api test framework

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.mapstruct:mapstruct:1.4.1.Final")

    configurations.compile {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    kapt("org.mapstruct:mapstruct-processor:1.4.1.Final")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.1.0")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    testImplementation("io.rest-assured:rest-assured:4.4.0")
    testImplementation("io.rest-assured:json-path:4.4.0")
    testImplementation("io.rest-assured:xml-path:4.4.0")
    testImplementation("io.rest-assured:spring-mock-mvc:4.4.0")
    testImplementation("org.codehaus.groovy:groovy:3.0.8")
    testImplementation("org.codehaus.groovy:groovy-json:3.0.8")
    testImplementation("org.codehaus.groovy:groovy-xml:3.0.8")

    configurations.testCompile {
        exclude("ch.qos.logback", "logback-classic")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    outputs.upToDateWhen { false }
}

val apiTest = task<Test>("apiTest") {
    description = "Run API tests."
    group = "verification"
    testClassesDirs = sourceSets["apiTest"].output.classesDirs
    classpath = sourceSets["apiTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(apiTest) }

detekt {
    toolVersion = "1.17.1"
    input = files(
        "src/main/kotlin",
        "src/test/kotlin",
        "src/apiTest/kotlin"
    )
    config = files("gradle/detekt/detekt.yml")
    buildUponDefaultConfig = true

    reports {
        xml.enabled = false
        txt.enabled = false
        html.enabled = true
        html.destination = file("${buildDir}/reports/detekt/detekt.html")
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("fourkeymetrics/Application.kt")
    finalizedBy(tasks.check)
}

apply(from = "gradle/git-hooks/install-git-hooks.gradle")
apply(from = "gradle/jacoco/jacoco.gradle")


// ktlint
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintFormat = task<JavaExec>("ktlintFormat") {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    group = "formatting"
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.jacocoTestReport {
    reports {
        csv.isEnabled = true
    }
}