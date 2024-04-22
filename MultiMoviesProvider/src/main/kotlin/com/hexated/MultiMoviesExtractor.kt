package com.hexated

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities

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

    suspend fun getExtractorLinks(url: String): List<ExtractorLink>? {
        val referer = "$mainUrl/"
        val streamWishLinks = getUrl(url, referer)
        val vidHideLinks = getUrl(url.replace(mainUrl, "https://vidhide.com"), referer)
        return (streamWishLinks ?: emptyList()) + (vidHideLinks ?: emptyList())
    }
}
