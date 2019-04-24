package ru.debian17.findme.app.ext

import android.content.Context
import android.content.res.AssetManager
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.debian17.findme.R
import ru.debian17.findme.data.model.category.Category
import java.io.*
import java.nio.file.Files
import java.nio.file.Path

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

fun View.hideKeyboard(): Boolean {
    return try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
        false
    }
}

fun <T> Single<T>.subscribeOnIO(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnUI(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnUI(): Completable = observeOn(AndroidSchedulers.mainThread())

fun Context.getCategories(): List<Category> {
    val inputStream = assets.open(getString(R.string.categories_file_name))
    val bf = BufferedReader(InputStreamReader(inputStream))
    val categories = ArrayList<Category>()
    var line = bf.readLine()
    while (line != null) {
        val id = line.toInt()
        val categoryName = bf.readLine()!!
        val isPoint = bf.readLine()!!.toBoolean()
        val isLong = bf.readLine()!!.toBoolean()
        val category = Category(id, categoryName, isPoint, isLong)
        categories.add(category)
        line = bf.readLine()
    }
    return categories
}