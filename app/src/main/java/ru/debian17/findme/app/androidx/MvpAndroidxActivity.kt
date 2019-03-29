package ru.debian17.findme.app.androidx

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpDelegate

@SuppressLint("Registered")
@SuppressWarnings("unused")
open class MvpAndroidxActivity : AppCompatActivity() {

    private var mMvpDelegate: MvpDelegate<out MvpAndroidxActivity>? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    protected override fun onStart() {
        super.onStart()

        getMvpDelegate().onAttach()
    }

    protected override fun onResume() {
        super.onResume()

        getMvpDelegate().onAttach()
    }

    protected override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    protected override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    protected override fun onDestroy() {
        super.onDestroy()

        getMvpDelegate().onDestroyView()

        if (isFinishing) {
            getMvpDelegate().onDestroy()
        }
    }

    private fun getMvpDelegate(): MvpDelegate<*> {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate<MvpAndroidxActivity>(this)
        }
        return mMvpDelegate!!
    }

}