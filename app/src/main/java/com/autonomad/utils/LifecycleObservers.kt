package com.autonomad.utils

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry

class LifecycleObservers(private val lifecycleRegistry: LifecycleRegistry) : LifecycleEventListener,
    Lifecycle by lifecycleRegistry {

    private val observers = hashMapOf<String, Boolean>()

    fun newObserver(key: String) = MultipleLifecycleObserver(key, this)

    override fun onResume(key: String) {
        if (!observers.containsValue(true)) lifecycleRegistry.onNext(Lifecycle.State.Started)
        observers[key] = true
    }

    override fun onPause(key: String) {
        observers[key] = false
        if (!observers.containsValue(true))
            lifecycleRegistry.onNext(Lifecycle.State.Stopped.WithReason(ShutdownReason(1000, "Paused")))
    }

    override fun onDestroy(key: String) {
        observers.remove(key)
        if (observers.isEmpty()) lifecycleRegistry.onComplete()
    }

    class MultipleLifecycleObserver(private val key: String, private val listener: LifecycleEventListener) :
        LifecycleObserver {

        @OnLifecycleEvent(Event.ON_PAUSE)
        fun onPause() {
            listener.onPause(key)
        }

        @OnLifecycleEvent(Event.ON_RESUME)
        fun onResume() {
            listener.onResume(key)
        }

        @OnLifecycleEvent(Event.ON_DESTROY)
        fun onDestroy() {
            listener.onDestroy(key)
        }
    }
}

interface LifecycleEventListener {
    fun onResume(key: String)
    fun onPause(key: String)
    fun onDestroy(key: String)
}
