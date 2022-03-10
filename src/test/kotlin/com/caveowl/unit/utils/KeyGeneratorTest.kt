package com.caveowl.unit.utils

import com.caveowl.utils.KeyGenerator
import kotlin.test.*


internal class KeyGeneratorTest {

    @Test
    fun `Generate valid rsa key pair`() {
//        val generator = KeyGenerator

        assertNotNull(KeyGenerator.privateKey)
        assertNotNull(KeyGenerator.publicKey)
    }

//    @Test
//    fun `Get base64 public and private keys`() {
//        val generator = KeyGenerator().apply { generateKeyPair() }
//        val pubKey = generator.getPublicKeyBase64()
//        val prvKey = generator.getPrivateKeyBase64()
//
//        assertNotNull(pubKey)
//        assertNotNull(prvKey)
//        assertContains(pubKey, "-----BEGIN RSA PUBLIC KEY-----")
//        assertContains(pubKey, "-----END RSA PUBLIC KEY-----")
//        assertContains(prvKey, "-----BEGIN RSA PRIVATE KEY-----")
//        assertContains(prvKey, "-----END RSA PRIVATE KEY-----")
//    }
//
//    @Test
//    fun `Get Json Web Key`() {
//        val generator = KeyGenerator().apply { generateKeyPair() }
//        val jwk = generator.getJWK()
//
//        assertNotNull(jwk.n)
//        assertNotNull(jwk.e)
//        assertNotNull(jwk.kid)
//        assertEquals("RS256", jwk.alg)
//        assertEquals("sig", jwk.use)
//        assertEquals("RSA", jwk.kty)
//    }
}
