package com.example.socialguard

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService

class ProfanityFilterJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val serviceIntent = Intent(this, ProfanityFilterService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, ProfanityFilterJobIntentService::class.java, JOB_ID, work)
        }
    }
}
