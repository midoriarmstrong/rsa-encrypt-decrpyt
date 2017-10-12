package com.example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

/**
 * RSA encrypts and decrypts messages using 3-digit ASCII codes
 * Also supports RSA key generation
 */

public class RSAAlgorithm {
    public Boolean keysGenerated = false; // Whether keys have been generated

    private BigInteger rsaN; // Modulus n (used in both public and private keys)
    private BigInteger rsaD; // Private exponent d
    private BigInteger rsaE; // Public exponent e

    /**
     * Takes a string, converts it to ASCII, then encrypts it
     *
     * @param msg Plaintext message
     *
     * @return Encrypted message
     */
    public String encrypt(String msg) {
        if (!keysGenerated || rsaN.equals(BigInteger.ZERO) || rsaE.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Both rsaN and rsaE must be initialized.");
        }

        String cipherText = "";
        for (int i = 0; i < msg.length(); i++) { // Convert text message to ASCII nums
            int asciiVal = (int)msg.charAt(i);

            if (asciiVal < 100) cipherText += '0'; // Append a 0 if ascii val is 2 characters

            cipherText += asciiVal;
        }

        BigInteger cipherNum = new BigInteger(cipherText);

        return cipherNum.modPow(rsaE, rsaN).toString(); // Find C = M^e (mod n) to encrypt
    }

    /**
     * Decrypts a given string into plaintext, assuming it was 3-digit ASCII-encoded
     *
     * @param msg Ciphertext message
     *
     * @return Decrypted message
     */
    public String decrypt(String msg) {
        if (!keysGenerated || rsaD.equals(BigInteger.ZERO) || rsaN.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Both rsaD and rsaN must be initialized.");
        }

        BigInteger plainNum = new BigInteger(msg);
        plainNum = plainNum.modPow(rsaD, rsaN); // Find N = C^d (mod n) to decrypt
        String asciiText = plainNum.toString();
        int len = asciiText.length();

        // As messages are encrypted with 3-digit ASCII, if text length is not divisible by 3, it is invalid (unless there is a leading 0)
        if (len % 3 != 0 && (len + 1) % 3 != 0) {
            throw new IllegalArgumentException("Ciphertext is invalid");

        } else if ((len + 1) % 3 == 0) { // Append zero if necessary
            asciiText = '0' + asciiText;
            len++;
        }

        String plainText = "";
        for (int i = 0; i < len; i += 3) { // Convert from ASCII to plain text
            plainText += (char)Integer.parseInt(asciiText.substring(i, i + 3));
        }

        return plainText;
    }

    /**
     * Generates random RSA public and private keys
     */
    public void generateKeys() {
        Random rnd = new Random(); // Init new random seed

        BigInteger pValue = BigInteger.probablePrime(1024, rnd), qValue; // Two large different prime numbers p and q

        do {
            qValue = BigInteger.probablePrime(1024, rnd);
        } while (pValue.equals(qValue)); // Calculate new q values until p and q are unique

         BigInteger nValue = pValue.multiply(qValue), // Calculate n = pq
                    phi = (pValue.subtract(BigInteger.ONE)).multiply(qValue.subtract(BigInteger.ONE)), // Calculate phi = (p - 1)(q - 1)
                    eValue = BigInteger.probablePrime(16, rnd), // Generate another smaller probable prime number for exponent - will be coprime with phi
                    dValue = LinearCongruence.solveCongruence(eValue, BigInteger.ONE, phi); // Solve for d in ed = 1 (mod phi)

        // Store the generated key values
        setKeys(nValue, dValue, eValue);
    }

    /**
     * Manually sets keys based on given values
     *
     * @param nValue Value of rsaN
     * @param dValue Value of rsaD
     * @param eValue Value of rsaE
     */
    public void setKeys(BigInteger nValue, BigInteger dValue, BigInteger eValue) {
        rsaN = nValue;
        rsaD = dValue;
        rsaE = eValue;
        keysGenerated = true;
    }

    /**
     * Gets n, d, and e values which make up the public and private keys
     *
     * @return HashMap with keys of n, d, and e and their respective values
     */
    public HashMap<String, BigInteger> getKeys() {
        if (!keysGenerated) {
            throw new IllegalArgumentException("Cannot access keys that have not been instantiated.");
        }

        HashMap<String, BigInteger> keys = new HashMap<>();
        keys.put("n", rsaN);
        keys.put("d", rsaD);
        keys.put("e", rsaE);

        return keys;
    }

    /**
     * Resets public and private keys
     */
    public void resetKeys() {
        rsaN = null;
        rsaD = null;
        rsaE = null;
        keysGenerated = false;
    }
}
