import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.drake.statelayout.StateLayout
import com.drake.statelayout.stateCreate
import com.jccppp.dialog.JustDialog
import com.mybase.libb.R
import com.mybase.libb.coroutines.ErrorInfo
import com.mybase.libb.ext.getBActivity
import com.mybase.libb.ui.BaseActivity
import com.mybase.libb.ui.BaseFragment
import kotlinx.coroutines.*



object CHttp {

    private var error: ((Throwable, Boolean) -> ErrorInfo)? = null

    @JvmStatic
    fun init(error: (t: Throwable, showToast: Boolean) -> ErrorInfo) {
        this.error = error
    }

    fun getError(throwable: Throwable, showToast: Boolean): ErrorInfo {
        return error?.invoke(throwable, showToast) ?: object : ErrorInfo(throwable, showToast) {
            override fun otherError(throwable: Throwable): String {
                return ""
            }
        }
    }
}

enum class LoadState {
    LOAD,
    SUCCESS,
    ERROR,
    COMPLETE
}

fun <T> CoroutineScope.myAsync(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return async(SupervisorJob(coroutineContext[Job]), start, block)

}

suspend fun <T> Deferred<T>.tryAwait(
    onCatch: ((Throwable) -> Unit)? = null,
    onSuccess: (T) -> Unit
) {
    try {
        onSuccess.invoke(await())
    } catch (e: Throwable) {
        onCatch?.invoke(e)
    }
}


fun ViewModel.post(
    state: ((LoadState) -> Unit)? = null,
    showToast: Boolean = true,
    onError: ((ErrorInfo) -> Unit)? = null,
    request: (suspend CoroutineScope.() -> Unit),
) = viewModelScope._launch(
    null,
    request,
    state = state,
    stateView = null,
    showLoading = false,
    showToast = showToast,
    onError = onError
)

private fun CoroutineScope._launch(
    any: Any?,
    request: (suspend CoroutineScope.() -> Unit),
    state: ((LoadState) -> Unit)? = null,
    stateView: View?,
    showLoading: Boolean,
    showToast: Boolean,
    onError: ((ErrorInfo) -> Unit)? = null
): Job {

    state?.invoke(LoadState.LOAD)
    if (showLoading) changeLoadState(any, true)

    var mState: StateLayout? = null

    if (stateView != null) {
        if (stateView is StateLayout) {
            mState = stateView
        } else if (stateView.parent is StateLayout) {
            mState = stateView.parent as StateLayout
        } else {
            mState = stateView.stateCreate()
        }
    }

    mState?.showLoading(refresh = false)
    mState?.setRetryIds(R.id.retry_state)
    mState?.onRefresh {
        _launch(any, request, state, stateView, showLoading, showToast, onError)
    }

    return launch {
        runCatching {
            request()
        }.onSuccess {
            mState?.showContent()
            state?.invoke(LoadState.SUCCESS)
            state?.invoke(LoadState.COMPLETE)
            if (showLoading) changeLoadState(any, false)
        }.onFailure {
            if (it !is CancellationException) {
                val errorInfo = CHttp.getError(it, showToast)
                onError?.invoke(errorInfo)
                state?.invoke(LoadState.ERROR)
                state?.invoke(LoadState.COMPLETE)
            }
            if (showLoading) changeLoadState(any, false)
            mState?.showError()
        }

    }
}

fun FragmentActivity.post(
    state: ((LoadState) -> Unit)? = null,
    stateView: View? = null,
    showLoading: Boolean = false,
    showToast: Boolean = true,
    onError: ((ErrorInfo) -> Unit)? = null,
    request: (suspend CoroutineScope.() -> Unit)
) = lifecycleScope._launch(
    this,
    request,
    state = state,
    stateView = stateView,
    showLoading = showLoading,
    showToast = showToast,
    onError = onError
)

fun LifecycleOwner.post(
    state: ((LoadState) -> Unit)? = null,
    stateView: View? = null,
    showLoading: Boolean = false,
    showToast: Boolean = true,
    onError: ((ErrorInfo) -> Unit)? = null,
    request: (suspend CoroutineScope.() -> Unit)
) = lifecycleScope._launch(
    this,
    request,
    state = state,
    stateView = stateView,
    showLoading = showLoading,
    showToast = showToast,
    onError = onError
)

fun Fragment.post(
    state: ((LoadState) -> Unit)? = null,
    stateView: View? = null,
    showLoading: Boolean = false,
    showToast: Boolean = true,
    onError: ((ErrorInfo) -> Unit)? = null,
    request: (suspend CoroutineScope.() -> Unit)
) = viewLifecycleOwner.lifecycleScope._launch(
    this,
    request,
    state = state,
    stateView = stateView,
    showLoading = showLoading,
    showToast = showToast,
    onError = onError
)

fun postGlobal(
    state: ((LoadState) -> Unit)? = null,
    showToast: Boolean = true,
    onError: ((ErrorInfo) -> Unit)? = null,
    request: (suspend CoroutineScope.() -> Unit),
): Job {
    return MainScope()._launch(
        null,
        request,
        state = state,
        stateView = null,
        showLoading = false,
        showToast = showToast,
        onError = onError
    )
}

private fun changeLoadState(any: Any?, show: Boolean) {
    if (any is BaseActivity)
        if (show) any.showProgress() else any.dismissProgress()
    else if (any is BaseFragment)
        if (show) any.getBActivity()?.showProgress() else any.getBActivity()?.dismissProgress()
    else if (any is JustDialog)
        if (show) any.windowContext.getBActivity()
            ?.showProgress() else any.windowContext.getBActivity()?.dismissProgress()

}
