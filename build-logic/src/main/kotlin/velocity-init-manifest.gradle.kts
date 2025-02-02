import org.gradle.jvm.tasks.Jar
import java.io.ByteArrayOutputStream

val currentShortRevision = ByteArrayOutputStream().use {
    exec {
        executable = "git"
        args = listOf("rev-parse", "HEAD")
        standardOutput = it
    }
    it.toString().trim().substring(0, 8)
}

tasks.withType<Jar> {
    manifest {
        val buildNumber = System.getenv("BUILD_NUMBER")
        val velocityHumanVersion: String =
                if (project.version.toString().endsWith("-SNAPSHOT")) {
                    if (buildNumber == null) {
                        "${project.version} (git-$currentShortRevision)"
                    } else {
                        "${project.version} (git-$currentShortRevision-b$buildNumber)"
                    }
                } else {
                    "${project.version}"
                }
        attributes["Implementation-Version"] = velocityHumanVersion
    }
}
