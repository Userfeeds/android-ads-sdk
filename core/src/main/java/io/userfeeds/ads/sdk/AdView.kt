package io.userfeeds.ads.sdk

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import io.reactivex.Single.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.security.SecureRandom
import kotlin.LazyThreadSafetyMode.*

class AdView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.userfeeds_banner_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.e("AdView", "onAttachedToWindow")
        just(Ads(
                items = listOf(
                        Ad("Yafi - Internet Chess", BigDecimal("0.50"), "http://yafi.pl"),
                        Ad("CoinMarkerCap", BigDecimal("0.30"), "http://coinmarketcap.com"),
                        Ad("CoinBase", BigDecimal("0.20"), "https://www.coinbase.com/join")
                ),
                widgetUrl = "http://userfeeds.io/",
                contextImage = "https://beta.userfeeds.io/api/contexts/static/img/ethereum.png"
        ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAds, this::onError)
    }

    private fun onAds(ads: Ads) {
        val viewPager = findViewById(R.id.userfeeds_ads_pager) as ViewPager
        viewPager.adapter = AdsPagerAdapter(ads)
        viewPager.currentItem = ads.items.randomIndex(random)
    }

    private fun onError(error: Throwable) {
        Log.e("AdView", "error", error)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e("AdView", "onDetachedFromWindow")
    }

    companion object {

        private val random by lazy(NONE) { SecureRandom() }
    }
}