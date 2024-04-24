package com.hexated

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.getAndUnpack
import com.lagradost.cloudstream3.utils.getPacked

open class MultiMoviesExtractor : ExtractorApi() {
    override var name = "MultiMovies"
    override var mainUrl = "https://streamwish.com"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val response = app.get(
            url, referer = referer ?: "$mainUrl/", interceptor = WebViewResolver(
                Regex("""master\.m3u8""")
            )
        )
        val sources = mutableListOf<ExtractorLink>()
        if (response.url.contains("m3u8"))
            sources.add(
                ExtractorLink(
                    source = name,
                    name = name,
                    url = response.url,
                    referer = referer ?: "$mainUrl/",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true
                )
            )
        return sources
    }
}

class Fmhd : ExtractorApi() {
    override val name = "Fmhd"
    override var mainUrl = "https://animezia.cloud"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer)
        val script = if (!getPacked(response.text).isNullOrEmpty()) {
            getAndUnpack(response.text)
        } else {
            response.document.selectFirst("script:containsData(sources:)")?.data()
        }
        val m3u8 = Regex("file:\\s*\"(.*?m3u8.*?)\"").find(script ?: return)?.groupValues?.get(1)

        callback.invoke(
            ExtractorLink(
                this.name,
                this.name,
                m3u8 ?: return,
                mainUrl,
                Qualities.Unknown.value,
                INFER_TYPE,
            )
        )
    }
}
