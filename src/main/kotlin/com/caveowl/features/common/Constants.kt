package com.caveowl.features.common

object Constants {
    object PATTERNS {
        // TODO - make a better pattern
        const val EMAIL_VALIDATION = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        object PASSWORD {
            const val HAS_NUMBER = ".*\\d.*"
            const val HAS_LOWER_CASE_LETTER = ".*[a-z].*"
            const val HAS_UPPER_CASE_LETTER = ".*[A-Z].*"
            const val HAS_SYMBOL = ".*[*.!@#$%^&(){}\\[\\]:\";'<>,?/~`_+-=|\\\\].*"
        }
    }
}
