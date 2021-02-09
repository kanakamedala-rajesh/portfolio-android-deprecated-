package com.venkatasudha.portfolio.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.venkatasudha.portfolio.R
import com.venkatasudha.portfolio.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var systemUIVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        showBelowCutout()
        window.addSystemUIVisibilityListener {
            systemUIVisible = it
        }

        val imageView = findViewById<ImageView>(R.id.iv_logo)
        val motionLayout = findViewById<MotionLayout>(R.id.motion_layout)

        Timber.d("Started Main Activity")
        DrawableCompat.setTint(imageView.drawable, ContextCompat.getColor(applicationContext, R.color.secondaryColor))

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Timber.d("onTransitionStarted, p1: $p1, p2: $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Timber.d("onTransitionCompleted, p1: $p1")
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Timber.d("onTransitionTrigger, p1: $p1, p2: $p2, p3: $p3")
            }
        })

        imageView.setOnClickListener {
            toggleSystemUI()
        }

    }

    private fun toggleSystemUI() {
        if (systemUIVisible) {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateSystemUIVisibility(hasFocus)
    }

    /**
     * Hides/Shows the system bars and makes the Activity "fullscreen".
     *
     * If this should be the default state it should be called from [Activity.onWindowFocusChanged] if hasFocus is true.
     * It is also recommended to take care of cutout areas. The default behavior is that the app shows in the cutout area in portrait mode if not in fullscreen mode.
     * This can cause "jumping" if the user swipes a system bar to show it.
     * It is recommended to set [WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER], call [showBelowCutout] from [Activity.onCreate]
     *
     * (see [Android Developers article about cutouts](https://developer.android.com/guide/topics/display-cutout#never_render_content_in_the_display_cutout_area)).
     *
     * @param hasFocus - indicates if window has focus ot nor
     *
     * @see addSystemUIVisibilityListener
     */
    private fun updateSystemUIVisibility(hasFocus: Boolean) {
        if (hasFocus) {
            hideSystemUI()
        } else {
            showSystemUI()
        }
    }

    private fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // show app content in fullscreen, i. e. behind the bars when they are shown (alternative to
            // deprecated View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.setDecorFitsSystemWindows(false)
            // finally, show the system bars
            window.insetsController?.show(WindowInsets.Type.systemBars())
        } else {
            // Shows the system bars by removing all the flags
            // except for the ones that make the content appear under the system bars.
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemUIAPI30()
        } else {
            @Suppress("DEPRECATION")
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            // make navbar translucent - do this already in hideSystemUI() so that the bar
            // is translucent if user swipes it up
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    /**
     * @reference https://medium.com/swlh/modifying-system-ui-visibility-in-android-11-e66a4128898b
     * */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUIAPI30() {
        window.insetsController?.let {
            // Default behavior is that if navigation bar is hidden, the system will "steal" touches
            // and show it again upon user's touch. We just want the user to be able to show the
            // navigation bar by swipe, touches are handled by custom code -> change system bar behavior.
            // Alternative to deprecated SYSTEM_UI_FLAG_IMMERSIVE.
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // make navigation bar translucent (alternative to deprecated WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // do this already in hideSystemUI() so that the bar is translucent if user swipes it up
            window.navigationBarColor = resources.getColor(R.color.transparentColor, null)
            // Finally, hide the system bars, alternative to View.SYSTEM_UI_FLAG_HIDE_NAVIGATION and SYSTEM_UI_FLAG_FULLSCREEN.
            it.hide(WindowInsets.Type.systemBars())
        }
    }

    /**
     * makes sure that content is not missed at cutouts, displays information below cutouts region
     * */
    private fun showBelowCutout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
        }
    }

    /**
     * Registers a listener which is informed when UI is shown (true) or hidden (false).
     * Used to handle changes to system UI done manually by user.
     */
    private fun Window.addSystemUIVisibilityListener(visibilityListener: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.setOnApplyWindowInsetsListener { v, insets ->
                val suppliedInsets = v.onApplyWindowInsets(insets)
                // only check for statusBars() and navigationBars(), because captionBar() is not always
                // available and isVisible() could return false, although showSystemUI() had been called:
                visibilityListener(suppliedInsets.isVisible(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()))
                suppliedInsets
            }
        } else {
            @Suppress("DEPRECATION")
            decorView.setOnSystemUiVisibilityChangeListener {
                visibilityListener((it and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0)
            }
        }
    }
}
