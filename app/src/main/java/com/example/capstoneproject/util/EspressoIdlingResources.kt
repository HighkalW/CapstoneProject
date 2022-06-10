package com.example.capstoneproject.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResources {
    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)
    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
    fun increment() {
        countingIdlingResource.increment()
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdlingResources.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResources.decrement() // Set app as idle.
    }
}