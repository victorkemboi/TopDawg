plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("dependencies") {
            id = "com.mes.topdawg.dependencies"
            implementationClass = "com.mes.topdawg.DependenciesPlugin"
        }
    }
}