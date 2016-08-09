package com.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Midori on 2016-05-09.
 */

public class RSAAlgorithm {
    public static void main(String args[]) {
        String choice = new String();
        do {
            choosePath();
            System.out.println("Would you like to do another operation? If yes, input anything. If not, input 'no'");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                choice = br.readLine();
            }
            catch (IOException e) {
                System.out.println("Oops! Something went wrong. Please try again");
            }
        } while (choice.compareTo("no") != 0);
    }

    public static void choosePath() {
        System.out.println("Input d for decryption, e for encryption, g to generate new random public and private keys");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String rsaChoice = br.readLine();
            if (rsaChoice.compareTo("e") == 0) {
                System.out.println("Please input your n");
                String n = br.readLine();
                System.out.println("Please input your e");
                String e = br.readLine();
                encrypt(new BigInteger(n), new BigInteger(e));
            }
            else if (rsaChoice.compareTo("d") == 0) {
                System.out.println("Please input your n");
                String n = br.readLine();
                System.out.println("Please input your d");
                String d = br.readLine();
                decrpyt(new BigInteger(n), new BigInteger(d));
            }
            else if (rsaChoice.compareTo("g") == 0) {
                generateKeys();
            }
            else {
                System.out.println("Please input d or e");
            }
        } catch (IOException e) {
            System.out.println("Input d or e please!");
        }
    }

    public static void encrypt(BigInteger n, BigInteger e) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userInput = new String();
        BigInteger ciphertext = new BigInteger("0");
        System.out.println("Please input an integer");
        try {
            userInput = br.readLine();
            ciphertext = new BigInteger(userInput);

        } catch (IOException e1) {
            System.out.println("Please input an integer!");
        }

        ciphertext = ciphertext.modPow(e, n);
        System.out.println("Ciphertext is " + ciphertext.toString());
    }

    public static void decrpyt(BigInteger n, BigInteger d) {
        System.out.println("Please input the ciphertext");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BigInteger ciphertext = new BigInteger("0");
        try {
            String userInput = br.readLine();
            ciphertext = new BigInteger(userInput);

        } catch (IOException e1) {
            System.out.println("Please input an integer!");
        }
        ciphertext = ciphertext.modPow(d, n);
        System.out.println("The decrpyted message is: " + ciphertext.toString());
    }

    public static BigInteger[] generateKeys() {
        BigInteger pValue= new BigInteger(1000, 5, new Random());
        BigInteger qValue = new BigInteger(1000, 5, new Random());
        BigInteger nValue = pValue.multiply(qValue);
        BigInteger eValue= new BigInteger(20, 5, new Random());

        BigInteger phi = (pValue.subtract(new BigInteger("1"))).multiply((qValue.subtract(new BigInteger("1"))));
        LinearCongruenceSolver linCong = new LinearCongruenceSolver();
        BigInteger dValue = linCong.solveCongruence(eValue, new BigInteger("1"), phi);

        System.out.println("Public key: <n: " + nValue.toString() + "\ne: " + eValue.toString() + ">");
        System.out.println("Private key: <n: as above, \nd: " + dValue.toString() + ">");
        BigInteger[] bigInt = new BigInteger[]{nValue, eValue, pValue, qValue, dValue};
        return bigInt;
    }
}
