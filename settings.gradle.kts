pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(
            url = "https://devrepo.kakao.com/nexus/content/groups/public/"
        )
        maven(
            url = "https://jitpack.io"
        )
    }
}
rootProject.name = "CommentDiary"
include(":app")
include(":data")
include(":domain")
include(":presentation")