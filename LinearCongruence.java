package com.example;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Solves linear congruences
 */
public class LinearCongruence {

    /**
     * Solves linear congruence in the form ax = b (mod n)
     *
     * @param a First integer
     * @param b Second integer
     * @param n The positive modulus
     *
     * @return The smallest solution x, where 1 < x < n, or -1 if unsolvable
     */
    public static BigInteger solveCongruence(BigInteger a, BigInteger b, BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) == -1) {
            throw new IllegalArgumentException("The modulus must be positive.");
        }

        BigInteger num1 = a, // Holds lesser number between a and n
                   num2 = n, // Holds greater number, will hold GCD after EEA
                   y = BigInteger.ZERO,
                   x = BigInteger.ONE;

        if (n.compareTo(a) == -1) { // if n < a
            num1 = n;
            num2 = a;
        }

        ArrayList<BigInteger[]> divRems = new ArrayList();
        // Initialize first two remainder values which will not be set in the EEA loop
        divRems.add(new BigInteger[] { BigInteger.ZERO, num2 } );
        divRems.add(new BigInteger[] { BigInteger.ZERO, num1 } );

        // Apply Extended Euclidean Algorithm to solve ax + ny = gcd(a, n)
        // At each step, calculate r_i-2 = q_i*r_i-1 + r_i and store q_i and r_i
        while (num1.compareTo(BigInteger.ZERO) != 0) { // while num2 != num1
            BigInteger[] divRem = num2.divideAndRemainder(num1);
            divRems.add(divRem);
            num2 = num1;
            num1 = divRem[1];
        }

        // If b is not divisible by num2 = gcd(a, n), then congruence is unsolvable, as ax + ny = b will not have a solution
        if (b.mod(num2).compareTo(BigInteger.ZERO) != 0) { // b % gcd != 0
            return BigInteger.ONE.negate();
        }

        // Reverse substitution of quotient values
        // We need only substitute back to the first iteration of the EEA to reach r_i-1 and r_i-2
        for (int i = divRems.size() - 1; i > 2; i--) {
            BigInteger temp = x;
            x = y.subtract(x.multiply(divRems.get(i - 1)[0]));
            y = temp;
        }

        return x.mod(n.divide(num2)); // Return smallest solution by modding result by n/gcd
    }
}
