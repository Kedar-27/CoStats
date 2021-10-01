package com.example.costats.util.extensions

import android.util.Log
import com.example.costats.models.Resource
import com.example.costats.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

fun <T> resourceFlow(
    doRetry: Boolean = true,
    block: suspend FlowCollector<Resource<T>>.() -> Unit
): Flow<Resource<T>> {
    return flow(block).asResourceFlow(doRetry)
}

fun <T> Flow<Resource<T>>.asResourceFlow(doRetry: Boolean = true): Flow<Resource<T>> {
    return this
        .onStart { emit(Resource.loading(data = null)) }
        .distinctUntilChanged()
        .retryWhen { cause, attempt ->
            // Exponential backoff of 1 second on each retry
            if (attempt > 1) delay(1000 * attempt)

            // Do not retry for IllegalArgument or 3 attempts are reached
            if (cause is IllegalArgumentException || attempt == 3L) false
            else doRetry
        }
        .catch {
            when (it) {
                is IllegalArgumentException -> {
                    Log.d(
                        "resourceFlow",
                        "TestLog: Couldn't complete request: IllegalArgumentException"
                    )
                    emit(Resource.error(msg = Constants.REQUEST_FAILED_MESSAGE, data = null))
                }
                else -> {
                    it.printStackTrace()
                    emit(Resource.error(msg = Constants.REQUEST_FAILED_MESSAGE, data = null))
                }
            }
        }
        .flowOn(Dispatchers.IO)
}