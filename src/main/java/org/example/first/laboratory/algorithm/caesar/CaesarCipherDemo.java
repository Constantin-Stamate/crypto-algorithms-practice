package org.example.first.laboratory.algorithm.caesar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CaesarCipherDemo {

    private static final int ALPHABET_SIZE = 26;

    static Path inputPath = Paths.get("src/main/resources/caesar/input.txt");
    static Path outputPath = Paths.get("src/main/resources/caesar/output.txt");
    static Path keyPath = Paths.get("src/main/resources/caesar/keys/numeric_key.txt");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an operation: 1 - Encrypt, 2 - Decrypt");

        int option = scanner.nextInt();

        if (option != 1 && option != 2) {
            System.out.println("Invalid option!");
            return;
        }

        int key;

        try {
            String keyContent = Files.readString(keyPath).trim();
            key = Integer.parseInt(keyContent);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading the key from file!");
            return;
        }

        if (key < 1 || key > 25) {
            System.out.println("The key must be between 1 and 25!");
            return;
        }

        try {
            String text = Files.readString(inputPath).toUpperCase().replaceAll(" ", "").trim();
            String result;

            if (option == 1) {
                result = encrypt(text, key);
            } else {
                result = decrypt(text, key);
            }

            Files.writeString(outputPath, result);
            System.out.println("Operation completed successfully. Check output.txt!");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    private static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ('A' + (c - 'A' + key) % ALPHABET_SIZE));
            } else {
                return "Invalid input!";
            }
        }

        return result.toString();
    }

    private static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ('A' + (c - 'A' - key + ALPHABET_SIZE) % ALPHABET_SIZE));
            } else {
                return "Invalid input!";
            }
        }

        return result.toString();
    }
}