import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val arrowVersion = "0.10.4"
val kotlinTest = "3.3.2"
val detektVersion = "1.4.0"

plugins {
	id("org.springframework.boot") version "2.3.0.M1"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	id("io.gitlab.arturbosch.detekt") version "1.4.0"

	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

detekt {
	toolVersion = detektVersion
	input = files("src/main/kotlin", "src/test/kotlin")
	config = files("detekt.yml")
	failFast = false
}

group = "com.vferras"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	jcenter()
	maven { url = uri("https://repo.spring.io/milestone") }

	jcenter {
		content {
			includeGroup("org.jetbrains.kotlinx")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.arrow-kt:arrow-core:$arrowVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.kotlintest:kotlintest-runner-junit5:$kotlinTest")

}

tasks.withType<Test> {
	useJUnitPlatform()

	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
