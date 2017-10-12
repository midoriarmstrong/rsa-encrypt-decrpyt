package com.example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Gets user input to RSA encrypt/decrypt messages.
 */

public class EncryptDecrypt {

    public static void main(String args[]) {
        String choice; // Holds user's request
        RSAAlgorithm rsa = new RSAAlgorithm(); // Init empty RSAAlgorithm
        Scanner sc = new Scanner(System.in);

        do {
            choosePath(rsa); // Run initial operation

            // Continue program until user requests stop
            System.out.println("Would you like to do another operation? y/n");
            choice = sc.nextLine();
        } while (choice.equals("y"));
    }

    public static void choosePath(RSAAlgorithm rsa) {
        Scanner sc = new Scanner(System.in);

        // If rsa does not have keys, ask to populate
        if (!rsa.keysGenerated) {
            System.out.println("Would you like to generate new keys? y/n");

            if (sc.nextLine().equals("y")) {
                rsa.generateKeys();

            } else {
                char[] keys = new char[]{ 'n', 'd', 'e' };
                BigInteger[] vals = new BigInteger[3];

                for (int i = 0; i < keys.length; i++) {
                    String num;
                    System.out.println("Please input your " + keys[i] + " or 'next' if you do not have this value.");
                    System.out.println("Note that you will not be able to do some operations without the appropriate key.");
                    num = sc.nextLine();

                    if (num.equals("next")) {
                        vals[i] = BigInteger.ZERO;
                        continue;
                    }

                    try {
                        vals[i] = new BigInteger(num);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid value for " + keys[i] + ": value not saved");
                        vals[i] = BigInteger.ZERO;
                    }
                }

                rsa.setKeys(vals[0], vals[1], vals[2]);
            }
        }

        System.out.println("Input d for decryption, e for encryption, p to print keys, or r to reset keys");
        String choice = sc.nextLine();

        switch(choice) {
            case "e":
                System.out.println("Input the message to encrypt");
                String plainText = sc.nextLine();
                System.out.println("Encrypted message:\n" + rsa.encrypt(plainText));
                break;
            case "d":
                System.out.println("Input the message to decrypt");
                String cipherText = sc.nextLine();
                System.out.println("Decrypted message:\n" + rsa.decrypt(cipherText));
                break;
            case "p":
                HashMap<String, BigInteger> keys = rsa.getKeys();
                for(HashMap.Entry<String, BigInteger> entry : keys.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                }
                break;
            case "r":
                rsa.resetKeys();
                break;
            default:
                System.out.println("Unknown option");
                break;
        }
    }
}
