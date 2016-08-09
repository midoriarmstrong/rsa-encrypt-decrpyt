package com.example;

import java.math.BigInteger;

/**
 * Created by Midori on 2016-08-09.
 */
public class LinearCongruenceSolver {
    public static BigInteger solveCongruence(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger gcd = a.gcd(n);
        System.out.println("GCD: " + gcd.toString());
        BigInteger[] numArray = new BigInteger[200], quotientArray = new BigInteger[200];
        BigInteger num2 = a, num1 = n, x = new BigInteger("1"), y;
        int count = 1;
        numArray[0] = n;
        numArray[1] = a;
        quotientArray[0] = new BigInteger("0");
        quotientArray[1] = quotientArray[0];
        System.out.println("EEA: ");
        do {
            count++;
            quotientArray[count] = num1.divide(num2);
            numArray[count] = num1.subtract(num2.multiply(quotientArray[count]));
            System.out.println(num1.toString() + " = " + quotientArray[count].toString() + "*" + num2.toString() + " + " + numArray[count].toString());
            num1 = num2;
            num2 = numArray[count];
        } while (num2.compareTo(gcd) != 0);

        y = quotientArray[count].multiply(new BigInteger("-1"));

        System.out.println("Reverse substitution: ");
        while (count > 1) {
            BigInteger temp = y;
            y = x.add(y.multiply(quotientArray[count - 1].multiply(new BigInteger("-1"))));
            x = temp;
            System.out.println(gcd.toString() + " = " + y.toString() + "*" + numArray[count - 2].toString() + " + " + x.toString() + "*" + numArray[count - 1].toString());
            count--;
        }

        BigInteger answer = x;
        BigInteger[] division = b.divideAndRemainder(gcd);
        if (division[1].compareTo(new BigInteger("0")) == 0) {
            answer = answer.multiply(division[0]);
            BigInteger m = n.divide(gcd);
            answer = answer.mod(m);
            System.out.println(answer.toString() + " (mod " + m.toString() + ")");
        }
        else {
            answer = null;
            System.out.println("No valid solutions");
        }
        return answer;
    }
}
