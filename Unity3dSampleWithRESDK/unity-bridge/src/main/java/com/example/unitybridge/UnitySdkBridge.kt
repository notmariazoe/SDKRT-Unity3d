package com.example.unitybridge

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.existing.sdk.ExistingSdk
import com.existing.sdk.BannerAd

class UnitySdkBridge(private val context: Context) {
    /** Container for rendering content from the SDK. */
    private lateinit var bannerAd: BannerAd

    fun initializeSync(callback: InitializationCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            val existingSdk = ExistingSdk(context)
            val result = existingSdk.initialize()
            callback.onInitializationComplete(result)
        }
    }

    fun createFileSync(size: Int, callback: FileCreationCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            val existingSdk = ExistingSdk(context)
            val result = existingSdk.createFile(size)
            callback.onFileCreationComplete(result)
        }
    }

    fun loadBannerAdSync(baseActivity: AppCompatActivity, appPackageName: String, callback: BannerAdLoadingCallback) {
        CoroutineScope(Dispatchers.Main).launch {

            // 1. Create an AttributeSet (we'll create an empty one)
            val attrs: AttributeSet? = null // Or use a custom AttributeSet if needed

            // 2. Create the BannerAd with the AttributeSet
            val bannerAd = BannerAd(context, attrs)

            // 3. Set Layout Params (VERY IMPORTANT)
            bannerAd.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  // Or specific width
                ViewGroup.LayoutParams.WRAP_CONTENT // Or specific height
            )

            val linearLayout = createLinearLayout(context)
            linearLayout.addView(bannerAd)

            val unityDecorView = baseActivity.window.decorView as ViewGroup
            unityDecorView.addView(linearLayout)

            bannerAd.loadAd(
                baseActivity,
                appPackageName,
                shouldStartActivityPredicate(),
                false,
                "NONE")

            callback.onLoadAdComplete("?YAY?")
        }
    }

    private fun createLinearLayout(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)

        // Set layout orientation (e.g., vertical or horizontal)
        linearLayout.orientation = LinearLayout.VERTICAL // Or LinearLayout.HORIZONTAL

        // Set layout width and height
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, // Or a specific width
            400  // Or a specific height
        )

        // Set top margin (100 pixels)
        val topMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, // Use DIPs for density independence
            100f, // 100 dp
            context.resources.displayMetrics
        ).toInt()

        val params = linearLayout.layoutParams as LinearLayout.LayoutParams
        params.topMargin = topMarginPx
        linearLayout.layoutParams = params


        //  Optional: Add some styling (example)
        linearLayout.setBackgroundColor(Color.LTGRAY)
        linearLayout.setPadding(16, 16, 16, 16) // Example padding

        return linearLayout
    }

    private fun shouldStartActivityPredicate() : () -> Boolean {
        return { true }
    }
}