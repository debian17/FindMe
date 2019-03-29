package ru.debian17.findme.app.ext

import android.view.View
import android.view.animation.AlphaAnimation
import androidx.annotation.UiThread
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@UiThread
fun View.show(duration: Long = 300L) {
    if (visibility != View.VISIBLE) {
        clearAnimation()
        startAnimation(AlphaAnimation(0f, 1f).apply { setDuration(duration) })
        visibility = View.VISIBLE
    }
}

@UiThread
fun View.hide(duration: Long = 300L) {
    if (visibility != View.GONE) {
        clearAnimation()
        startAnimation(AlphaAnimation(1f, 0f).apply { setDuration(duration) })
        visibility = View.GONE
    }
}

fun View.longSnackBar(message: String?) {
    val m = message ?: return
    Snackbar.make(this, m, Snackbar.LENGTH_LONG).show()
}

fun <T> Single<T>.subscribeOnIO(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnUI(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnUI(): Completable = observeOn(AndroidSchedulers.mainThread())