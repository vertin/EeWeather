package com.vertin.eetweather.util

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.*

internal class SingleEmitterListener<T>(private val emitter: SingleEmitter<T>) : OnCompleteListener<T> {
    override fun onComplete(task: Task<T>) =
        if (task.isSuccessful && task.result != null) emitter.onSuccess(task.result)
        else emitter.onError(
            if (task.isSuccessful) NullPointerException("Task result is null")
            else task.exception ?: NullPointerException("Task failed with no exception")
        )
}

internal class CompletableEmitterListener(private val emitter: CompletableEmitter) :
    OnCompleteListener<Void> {
    override fun onComplete(task: Task<Void>) =
        if (task.isSuccessful) emitter.onComplete()
        else emitter.onError(task.exception ?: NullPointerException("Task failed with no exception"))
}

internal class SingleTaskOnSubscribe<Result>(private val task: Task<Result>) :
    SingleOnSubscribe<Result> {
    override fun subscribe(emitter: SingleEmitter<Result>) {
        task.addOnCompleteListener(SingleEmitterListener(emitter))
    }
}


internal class CompletableTaskOnSubscribe(private val task: Task<Void>) : CompletableOnSubscribe {
    override fun subscribe(emitter: CompletableEmitter) {
        task.addOnCompleteListener(CompletableEmitterListener(emitter))
    }
}



fun <T> Task<T>.toSingle(): Single<T> = Single.create(SingleTaskOnSubscribe(this))