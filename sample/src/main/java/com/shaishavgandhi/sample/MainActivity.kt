package com.shaishavgandhi.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.NativeAppInstallAd
import com.google.android.gms.ads.formats.NativeContentAd
import com.shaishavgandhi.rxads.RxAdLoader
import com.shaishavgandhi.rxads.extensions.asSingle
import com.shaishavgandhi.rxads.extensions.loadInstallAd
import com.shaishavgandhi.rxads.extensions.loadInstallAds
import com.shaishavgandhi.rxads.extensions.loadNativeContentAd
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        installAd.setOnClickListener {
            loadNativeInstallAd()
        }

        contentAd.setOnClickListener {
            loadNativeContentAdWithExtension()
        }

        interstitialAd.setOnClickListener {
            loadInterstitialAd()
        }

    }

    private fun loadNativeInstallAd() {
        val disposable = RxAdLoader(this, "ca-app-pub-3940256099942544/2247696110")
                .withNativeAdOptions(NativeAdOptions.Builder().setRequestMultipleImages(true).build())
                .loadInstallAd(AdRequest.Builder().build())
                .subscribeWith(object : DisposableSingleObserver<NativeAppInstallAd>() {

                    override fun onSuccess(installAd: NativeAppInstallAd) {
                        headline.text = installAd.headline
                        Glide.with(image).load(installAd.images[0].uri).into(image)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })

        disposables.add(disposable)
    }

    private fun loadNativeInstallAdWithExtension() {
        val disposable = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .loadInstallAd(AdRequest.Builder().build())
                .subscribe({ installAd ->
                        headline.text = installAd.headline
                        Glide.with(image).load(installAd.images[0].uri).into(image)
                    }, { e ->
                    e.printStackTrace()
                })

        disposables.add(disposable)
    }

    private fun loadUnifiedAd() {
        val disposable = RxAdLoader(this, "ca-app-pub-3940256099942544/2247696110")
                .loadUnifiedAd(AdRequest.Builder().build())
                .subscribe({ ad ->
                    headline.text = ad.headline
                    Glide.with(image).load(ad.images[0].uri).into(image)
                }, { error ->
                    error.printStackTrace()
                })

        disposables.add(disposable)
    }

    private fun loadNativeContentAd() {
        val disposable = RxAdLoader(this, "ca-app-pub-3940256099942544/2247696110")
                .loadNativeContentAd(AdRequest.Builder().build())
                .subscribeWith(object : DisposableSingleObserver<NativeContentAd>() {
                    override fun onSuccess(contentAd: NativeContentAd) {
                        headline.text = contentAd.headline
                        Glide.with(image).load(contentAd.images[0].uri).into(image)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

        disposables.add(disposable)
    }

    private fun loadNativeContentAdWithExtension() {
        val disposable = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .loadNativeContentAd(AdRequest.Builder().build())
                .subscribeWith(object : DisposableSingleObserver<NativeContentAd>() {
                    override fun onSuccess(contentAd: NativeContentAd) {
                        headline.text = contentAd.headline
                        Glide.with(image).load(contentAd.images[0].uri).into(image)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

        disposables.add(disposable)
    }

    private fun loadInterstitialAd() {
        val ad = InterstitialAd(this)
        ad.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        ad.asSingle(AdRequest.Builder().build())
                .subscribe({ ad ->
                    ad.show()
                }, { error ->

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}