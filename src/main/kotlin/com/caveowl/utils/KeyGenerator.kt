package com.caveowl.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.util.*

object KeyGenerator {

    private const val RESOURCE_PATH = "src/main/resources"
    private const val PRV_KEY_FILE_NAME = "jwtRS256.key"
    private const val PUB_KEY_FILE_NAME = "jwtRS256.pub"
    private const val JSON_PUB_KEY = "jwks.json"

    lateinit var privateKey: PrivateKey
        private set
    lateinit var publicKey: PublicKey
        private set
    private val kid: String = getRandomString(43)

    init {
        File("$RESOURCE_PATH/$PRV_KEY_FILE_NAME").let { it ->
            if (it.createNewFile()) {
                generateKeyPair()
                it.writeText(this.getPrivateKeyBase64())
                File("$RESOURCE_PATH/$PUB_KEY_FILE_NAME").writeText(this.getPublicKeyBase64())
                File("$RESOURCE_PATH/$JSON_PUB_KEY").writeText(Json.encodeToString(this.getJWK()))
            } else {
                TODO("Load data from file")
            }
        }



//        File("src/main/resources/cert-file.pem").writeText(this.getPrivateKeyBase64())
    }

    /**
     * This will generate key pair in memory
     * (not good when using more than 1 instance of this service)
     */
    fun generateKeyPair(algo: String = "rsa", size: Int = 2048) {
        with(
            KeyPairGenerator.getInstance(algo).apply {
                initialize(size)
            }.generateKeyPair()
        ) {
            privateKey = private
            publicKey = public
        }
    }

    fun getPrivateKeyBase64(): String {
        return with(Base64.getEncoder()) {
            "-----BEGIN RSA PRIVATE KEY-----\n" +
                    encodeToString(privateKey.encoded) +
                    "\n-----END RSA PRIVATE KEY-----\n"
        }
    }

    fun getPublicKeyBase64(): String {
        return with(Base64.getEncoder()) {
            "-----BEGIN RSA PUBLIC KEY-----\n" +
                    encodeToString(publicKey.encoded) +
                    "\n-----END RSA PUBLIC KEY-----\n"
        }
    }

    fun getJWK(): JWK {
        val pk = this.publicKey as RSAPublicKey
        val urlEncoder = Base64.getUrlEncoder()
        return JWK(
            kty = pk.algorithm,
            kid = kid,
            e = urlEncoder.encodeToString(pk.publicExponent.toByteArray()),
            n = urlEncoder.encodeToString(pk.modulus.toByteArray()),
            alg = "RS256",
            use = "sig"
        )
    }

    @kotlinx.serialization.Serializable
    data class JWK(
        val kty: String,
        val kid: String,
        val e: String,
        val n: String,
        val alg: String,
        val use: String
    )
}

/*
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJbB5Xb9YKQApj
4DLdpBhBTS9PptLJjMY42+EuOnpwAWMazsqi4pU1uIWLdq5NZF0YymqWZDLp1Ies
2bqkCiahymwaE+I0KlOLAIzrfLkWabB9xYFpcs2TQTHlD0UL9noomO3cPtPc/rcu


MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyWweV2/WCkAKY+Ay3aQY

 */
