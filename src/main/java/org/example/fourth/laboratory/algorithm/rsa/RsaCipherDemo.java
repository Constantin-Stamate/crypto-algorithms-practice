package org.example.fourth.laboratory.algorithm.rsa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RsaCipherDemo {

    public static void main(String[] args) {
        String message = readMessageFromFile()
                .toUpperCase().replace(" ", "");

        Pair primeNumbers = generatePrimeNumbers();

        long prime1 = primeNumbers.first();
        long prime2 = primeNumbers.second();
        System.out.println("\nRandomly chosen prime numbers: " + prime1 + " " + prime2);

        long modulus = prime1 * prime2;
        System.out.println("The product of the two primes (n) is: " + modulus);

        long totient = (prime1 - 1) * (prime2 - 1);
        System.out.println("Euler's totient (phi) is: " + totient);

        int publicExponent = (int) chooseE(totient);
        long privateExponent = extendedEuclid(publicExponent, totient);

        Pair privateKey = new Pair(privateExponent, modulus);
        Pair publicKey = new Pair(publicExponent, modulus);

        System.out.println("\nPrivate key (d, n): (" + privateExponent + ", " + modulus + ")");
        System.out.println("Public key (e, n): (" + publicExponent + ", " + modulus + ")");

        ArrayList<Long> encryptedMessage = new ArrayList<>();

        System.out.println("\nEncrypted message: ");
        for (int i = 0; i < message.length(); i++) {
            long characterValue = message.charAt(i);
            long cipherValue = encrypt(characterValue, modulus, publicExponent);
            encryptedMessage.add(cipherValue);
            System.out.print(cipherValue + " ");
        }

        StringBuilder decryptedMessage = new StringBuilder();
        long startTime = System.nanoTime();

        for (long cipherValue : encryptedMessage) {
            char decryptedChar = (char) encrypt(cipherValue, modulus, privateExponent);
            decryptedMessage.append(decryptedChar);
        }

        long endTime = System.nanoTime();
        double executionTimeMillis = ((endTime - startTime) * 1.0) / 1000000;
        System.out.println("\nExecution time: " + executionTimeMillis + " milliseconds");

        writeMessageToFile(decryptedMessage.toString());
    }

    private static String readMessageFromFile() {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("src/main/resources/rsa/input.txt")));
        } catch (IOException e) {
            System.out.println("Error reading file: " + "src/main/resources/rsa/input.txt");
            return "";
        }
    }

    private static void writeMessageToFile(String message) {
        try (FileWriter writer = new FileWriter("src/main/resources/rsa/output.txt")) {
            writer.write(message);
            System.out.println("Decrypted message written to: " + "src/main/resources/rsa/output.txt");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + "src/main/resources/rsa/output.txt");
        }
    }

    private static Pair generatePrimeNumbers() {
        long lowerBound = 100;
        long upperBound = 1000;
        ArrayList<Long> primes = new ArrayList<>();
        Random random = new Random();

        for (long number = lowerBound; number <= upperBound; number++) {
            if (isPrime(number)) {
                primes.add(number);
            }
        }

        int index1 = random.nextInt(primes.size());
        int index2 = random.nextInt(primes.size());
        while (index2 == index1) {
            index2 = random.nextInt(primes.size());
        }

        return new Pair(primes.get(index1), primes.get(index2));
    }

    private static boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }

        return true;
    }

    private static long chooseE(long totient) {
        ArrayList<Long> possibleExponents = new ArrayList<>();
        Random random = new Random();

        for (long i = 2; i < totient; i++) {
            if (gcd(i, totient) == 1) {
                possibleExponents.add(i);
            }
        }

        return possibleExponents.get(random.nextInt(possibleExponents.size()));
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    private static long extendedEuclid(long e, long totient) {
        long privateExponent;
        ArrayList<Long> vec1 = new ArrayList<>();
        ArrayList<Long> vec2 = new ArrayList<>();

        vec1.add(-1L);
        vec2.add(-1L);
        vec1.add(0L);
        vec2.add(1L);
        vec1.add(totient);
        vec2.add(e);
        vec1.add(-1L);
        vec2.add(totient / e);

        long remainder = totient;
        long quotient;
        long tempValue;

        while (remainder != 1) {
            tempValue = vec1.get(1) - vec2.get(3) * vec2.get(1);
            remainder = vec1.get(2) % vec2.get(2);
            quotient = vec2.get(2) / remainder;

            vec1 = new ArrayList<>(vec2);
            vec2.clear();
            vec2.add(-1L);
            vec2.add(tempValue);
            vec2.add(remainder);
            vec2.add(quotient);
        }

        privateExponent = vec2.get(1) + totient;
        return privateExponent;
    }

    public static long encrypt(long base, long modulus, long exponent) {
        long result = 1;
        base = base % modulus;

        while (exponent > 0) {
            if ((exponent % 2) == 1) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }

        return result;
    }
}