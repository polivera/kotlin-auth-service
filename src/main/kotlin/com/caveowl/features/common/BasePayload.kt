package com.caveowl.features.common

abstract class BasePayload {
    var errors = mutableMapOf<String, String>()
        private set

    var isValid: Boolean = false
        private set
        get() = this.errors.isEmpty()

    abstract fun validate()
}