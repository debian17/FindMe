package ru.debian17.findme.app.mvp

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.debian17.findme.data.network.error.ErrorParser
import ru.debian17.findme.data.network.error.ErrorResponse

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {

    private val compositeDisposable = CompositeDisposable()

    private val errorParser = ErrorParser()

    protected var errorBody: ErrorResponse? = null

    protected fun unsubscribeOnDestroy(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    protected fun getError(throwable: Throwable): ErrorResponse {
        return errorParser.getErrorResponse(throwable)
    }

}