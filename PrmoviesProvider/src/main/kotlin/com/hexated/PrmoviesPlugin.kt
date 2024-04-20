package com.hexated

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class PrmoviesPlugin: Plugin() {
    override fun load(context: Context) {
        registerMainAPI(PrmoviesProvider())
    }
}
