package ie.koala.fantascope.app

import android.app.Application
import ie.koala.fantascope.BuildConfig

open class FantascopeApplication : Application() {

    companion object {
        val versionName: String
            get() = BuildConfig.VERSION_NAME

        val versionCode: String
            get() = BuildConfig.VERSION_CODE.toString()

        val versionBuildTimestamp: String
            get() = BuildConfig.BUILD_TIME

        val versionGitHash: String
            get() = BuildConfig.GIT_HASH

    }
}