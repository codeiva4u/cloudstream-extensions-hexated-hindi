
package com.hexated

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class MovierulzhdPlugin: Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list directly.
        registerMainAPI(Movierulzhd())
        registerMainAPI(Hdmovie2())
        registerMainAPI(Animesaga())
        registerExtractorAPI(Fmhd())
        registerExtractorAPI(Akamaicdn())
        registerExtractorAPI(AnimesagaStream())
        registerExtractorAPI(Jezt294anecte())

    
    }
}
