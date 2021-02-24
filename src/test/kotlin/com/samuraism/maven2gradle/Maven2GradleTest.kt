package com.samuraism.maven2gradle

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.CONCURRENT)

class Maven2GradleTest {

    @Test
    fun maven2gradle() {
        assertEquals(
            """implementation 'com.samuraism:webdriver-installer:1.4'""",
            Maven2Gradle().maven2gradle(
                """        <dependency>
            <groupId>com.samuraism</groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
"""
            )
        )
    }

    @Test
    fun maven2gradleWithDependenciesElement() {
        assertEquals(
            """dependencies {
    implementation 'com.samuraism:webdriver-installer:1.4'
}""",
            Maven2Gradle().maven2gradle(
                """<dependencies>
                            <dependency>
            <groupId>com.samuraism</groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
        </dependencies>
"""
            )
        )
    }

    @Test
    fun maven2gradleWithProjectElement() {
        assertEquals(
            """dependencies {
    implementation 'com.samuraism:webdriver-installer:1.4'
}""",
            Maven2Gradle().maven2gradle(
                """<project>
                <dependencies>
                            <dependency>
            <groupId>com.samuraism</groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
        </dependencies>
        </project>
"""
            )
        )
    }

    @Test
    fun maven2gradleKts() {
        assertEquals(
            """implementation("com.samuraism:webdriver-installer:1.4")""",
            Maven2Gradle(true).maven2gradle(
                """        <dependency>
            <groupId>com.samuraism</groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
"""
            )
        )
    }

    @Test
    fun withSpace() {
        assertEquals(
            """implementation 'com.samuraism:webdriver-installer:1.4'""",
            Maven2Gradle().maven2gradle(
                """<dependency>
            <groupId>  com.samuraism  </groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
"""
            )
        )
    }

    @Test
    fun withSpaceKts() {
        assertEquals(
            """implementation("com.samuraism:webdriver-installer:1.4")""",
            Maven2Gradle(true).maven2gradle(
                """<dependency>
            <groupId>  com.samuraism  </groupId>
            <artifactId>webdriver-installer</artifactId>
            <version>1.4</version>
        </dependency>
"""
            )
        )
    }

    @Test
    fun exclude() {
        assertEquals(
            """implementation('ch.qos.logback:logback-classic:1.2.3') {
            |    exclude group: org.slf4j, module: "slf4j-api"
            |}""".trimMargin(),
            Maven2Gradle().maven2gradle(
                """<dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
"""
            )
        )
    }

    @Test
    fun excludeKts() {
        assertEquals(
            """implementation("ch.qos.logback:logback-classic:1.2.3") {
            |    exclude(group = "org.slf4j", module = "slf4j-api")
            |}""".trimMargin(),
            Maven2Gradle(true).maven2gradle(
                """<dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
"""
            )
        )
    }

    @Test
    fun multipleExclusion() {
        assertEquals(
            """implementation('org.seleniumhq.selenium:selenium-chrome-driver:1.2.3') {
            |    exclude group: com.google.guava, module: "guava"
            |    exclude group: com.squareup.okio, module: "okio"
            |}""".trimMargin(),
            Maven2Gradle().maven2gradle(
                """        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-chrome-driver</artifactId>
            <version>1.2.3</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.guava</groupId>
                    <artifactId>guava</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.squareup.okio</groupId>
                    <artifactId>okio</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

"""
            )
        )
    }

    @Test
    fun multipleExclusionKts() {
        assertEquals(
            """implementation("org.seleniumhq.selenium:selenium-chrome-driver:1.2.3") {
            |    exclude(group = "com.google.guava", module = "guava")
            |    exclude(group = "com.squareup.okio", module = "okio")
            |}""".trimMargin(),
            Maven2Gradle(true).maven2gradle(
                """        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-chrome-driver</artifactId>
            <version>1.2.3</version>
            <exclusions>
                <exclusion>
                    <groupId>com.google.guava</groupId>
                    <artifactId>guava</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>com.squareup.okio</groupId>
                    <artifactId>okio</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

"""
            )
        )
    }

}