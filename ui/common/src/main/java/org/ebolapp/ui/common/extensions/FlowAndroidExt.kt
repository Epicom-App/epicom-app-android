package org.ebolapp.ui.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.cancellation.CancellationException

fun <T> Fragment.observe(flow: Flow<T>, block: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest {
            block(it)
        }
    }
}

fun <T> AppCompatActivity.observe(flow: Flow<T>, block: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collectLatest {
            block(it)
        }
    }
}

fun <A, B: Any, R> Flow<A>.withLatestFrom(other: Flow<B>, transform: suspend (A, B) -> R): Flow<R> = flow {
    coroutineScope {
        val latestB = AtomicReference<B?>()
        val outerScope = this
        launch {
            try {
                other.collect { latestB.set(it) }
            } catch(e: CancellationException) {
                outerScope.cancel(e)
            }
        }
        collect { a: A ->
            latestB.get()?.let { b -> emit(transform(a, b)) }
        }
    }
}