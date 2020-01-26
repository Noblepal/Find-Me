package com.intelligence.findme

import android.app.Application
import android.content.Context
import android.os.Build
import com.singhajit.sherlock.core.Sherlock
import com.singhajit.sherlock.core.investigation.AppInfo
import com.singhajit.sherlock.util.AppInfoUtil

class Debug : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Sherlock.init(this)
        Sherlock.setAppInfoProvider {
            AppInfo.Builder()
                .with("Application Version", AppInfoUtil.getAppVersion(base))
                .with("Android Version", Build.VERSION.RELEASE)
                .build()
        }
    }
}