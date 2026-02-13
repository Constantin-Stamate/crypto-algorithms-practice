package org.example.first.laboratory.algorithm.caesar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class CaesarCipherExtendedDemo {

    private static final int ALPHABET_SIZE = 26;

    static Path inputPath = Paths.get("src/main/resources/caesar/input.txt");
    static Path outputPath = Paths.get("src/main/resources/caesar/output.txt");
    static Path numericKeyPath = Paths.get("src/main/resources/caesar/keys/numeric_key.txt");
    static Path alphabetKeyPath = Paths.get("src/main/resources/caesar/keys/alphabet_key.txt");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an operation: 1 - Encrypt, 2 - Decrypt");

        int option = scanner.nextInt();

        int numericKey;
        String alphabetKey;

        try {
            numericKey = Integer.parseInt(Files.readString(numericKeyPath).trim());
            alphabetKey = Files.readString(alphabetKeyPath).trim().toUpperCase();

            if (!alphabetKey.matches("[A-Z]+") || alphabetKey.length() < 7) {
                System.out.println("Alphabet key must contain only Latin letters and have at least 7 characters!");
                return;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error while reading the keys from the files!");
            return;
        }

        if (numericKey < 1 || numericKey > 25) {
            System.out.println("The numeric key must be between 1 and 25!");
            return;
        }

        try {
            String text = Files.readString(inputPath)
                    .toUpperCase()
                    .replaceAll(" ", "")
                    .trim();

            String modifiedAlphabet = buildModifiedAlphabet(alphabetKey);
            String result;

            if (option == 1) {
                result = encrypt(text, numericKey, modifiedAlphabet);
            } else {
                result = decrypt(text, numericKey, modifiedAlphabet);
            }

            Files.writeString(outputPath, result);
            System.out.println("Operation completed successfully. Check output.txt!");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    private static String buildModifiedAlphabet(String alphabetKey) {
        Set<Character> seen = new LinkedHashSet<>();
        StringBuilder result = new StringBuilder();

        for (char c : alphabetKey.toCharArray()) {
            seen.add(c);
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            seen.add(c);
        }

        for (char c : seen) {
            result.append(c);
        }

        return result.toString();
    }

    private static String encrypt(String text, int numericKey, String alphabet) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index == -1) return "Invalid input!";

            char encryptedChar = alphabet.charAt((index + numericKey) % ALPHABET_SIZE);
            result.append(encryptedChar);
        }

        return result.toString();
    }

    private static String decrypt(String text, int numericKey, String alphabet) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = alphabet.indexOf(c);
            if (index == -1) return "Invalid input!";

            char decryptedChar = alphabet.charAt((index - numericKey + ALPHABET_SIZE) % ALPHABET_SIZE);
            result.append(decryptedChar);
        }

        return result.toString();
    }
}