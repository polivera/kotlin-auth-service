package com.caveowl.utils

import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash and validate passwords using PBKDF2, Salt and Pepper.
 */
class Password(
    private val password: String
) {
    companion object {
        private const val ALGO: String = "PBKDF2WithHmacSHA1"
        private const val ITERATIONS: Int = 65536
        private const val KEY_LENGTH: Int = 500
        private const val SALT_BYTE_SIZE: Int = 12
        private const val SALT_STRING_SIZE: Int = 16
    }

    private var salt: String = ""
    private var pepper: Int = 0
    private var hashedPassword: String = ""

    /**
     * Get hashed password
     */
    fun hash(): String {
        this.salt = generateSalt()
        this.pepper = generatePepper()
        this.hashedPassword = generateHashedPassword()

        val passwordParts = this.hashedPassword.splitAt(this.pepper)

        return "${passwordParts[0]}${this.salt}${passwordParts[1]}"
    }

    /**
     * Validate plain text password against the hashed password
     */
    fun isValid(fullPassword: String): Boolean {
        var ind = 1
        var isValidPassword = false

        while (ind <= 10 && !isValidPassword) {
            val passwordAndSalt = this.separateSaltFromHashPassword(ind, fullPassword)
            this.salt = passwordAndSalt[1]
            val hashedPassword = this.generateHashedPassword()
            isValidPassword = passwordAndSalt[0] == hashedPassword
            ind++
        }

        return isValidPassword
    }

    /**
     * Separate the salt from the hashed password.
     *
     * The hashed password is composed of the hash password and the salt, but the
     * place is random, so we have to try all possible places
     *
     * @param ind where to start the slice
     * @return an array with hash password without salt on index 0 and salt at index 1
     */
    private fun separateSaltFromHashPassword(ind: Int, fullPassword: String): List<String> {
        val passwordParts = fullPassword.splitAt(ind)
        val hashAndSalt = passwordParts[1].splitAt(SALT_STRING_SIZE)

        return listOf(
            passwordParts[0] + hashAndSalt[1],
            hashAndSalt[0]
        )
    }


    /**
     * Generate random password salt.
     *
     * This will add a random string to the user password.
     *
     * @return String password salt
     */
    private fun generateSalt(): String {
        val saltBytes = ByteArray(SALT_BYTE_SIZE)
        val random = SecureRandom()
        random.nextBytes(saltBytes)
        return this.byteArrayToString(saltBytes)
    }

    /**
     * Generate password pepper.
     *
     * The pepper will be used as a random index to inject the salt into the password.
     *
     * @return Int pepper
     */
    private fun generatePepper(): Int {
        return (1..10).random()
    }

    /**
     * Hash password.
     *
     * This will generate a hash using the password and the previously generated salt
     *
     * @return String hashed password
     */
    private fun generateHashedPassword(): String {
        if (this.salt.isEmpty()) {
            throw IllegalArgumentException("Salt was not generated")
        }
        val spec = PBEKeySpec(this.password.toCharArray(), this.salt.toByteArray(), ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGO)
        return this.byteArrayToString(factory.generateSecret(spec).encoded)
    }

    /**
     * Transform ByteArray into a String
     *
     * @return String
     */
    private fun byteArrayToString(bArray: ByteArray): String {
        return Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(bArray)
    }
}
