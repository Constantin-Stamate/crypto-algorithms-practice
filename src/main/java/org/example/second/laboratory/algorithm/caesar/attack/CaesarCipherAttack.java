package org.example.second.laboratory.algorithm.caesar.attack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CaesarCipherAttack {

    private static final int ALPHABET_SIZE = 26;

    private static final Path INPUT_PATH = Paths.get("src/main/resources/attack/input.txt");
    private static final Path ENCRYPTED_PATH = Paths.get("src/main/resources/attack/output_encrypted.txt");
    private static final Path DECRYPTED_PATH = Paths.get("src/main/resources/attack/output_decrypted.txt");
    private static final Path KEY_PATH = Paths.get("src/main/resources/attack/keys/numeric_key.txt");

    public static void main(String[] args) {
        encryptText();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose decryption method: 1 - Brute Force, 2 - Frequency Analysis");

        int option = scanner.nextInt();

        String decryptedText;
        if (option == 1) {
            decryptedText = decryptByBruteForce();
        } else if (option == 2) {
            decryptedText = decryptByFrequencyAnalysis();
        } else {
            System.out.println("Invalid option!");
            scanner.close();
            return;
        }

        try {
            if (decryptedText != null) {
                Files.writeString(DECRYPTED_PATH, decryptedText);
            }
            System.out.println("Decryption completed. Result saved in output_decrypted.txt!");
        } catch (IOException e) {
            System.out.println("Error writing decrypted file: " + e.getMessage());
        }

        scanner.close();
    }

    private static void encryptText() {
        try {
            String plainText = Files.readString(INPUT_PATH).trim();

            int key = readKeyFromFile();
            if (key == -1) return;

            String encryptedText = encrypt(plainText, key);

            Files.writeString(ENCRYPTED_PATH, encryptedText);
            System.out.println("Text successfully encrypted and saved to output_encrypted.txt!");
        } catch (IOException e) {
            System.out.println("Error reading/writing files: " + e.getMessage());
        }
    }

    private static int readKeyFromFile() {
        try {
            String keyContent = Files.readString(KEY_PATH).trim();
            int key = Integer.parseInt(keyContent);

            if (key < 5 || key > 15) {
                System.out.println("The key must be between 5 and 15!");
                return -1;
            }

            return key;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading key from file!");
            return -1;
        }
    }

    private static String encrypt(String text, int key) {
        StringBuilder encrypted = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isUpperCase(character)) {
                encrypted.append((char) ('A' + (character - 'A' + key) % ALPHABET_SIZE));
            } else if (Character.isLowerCase(character)) {
                encrypted.append((char) ('a' + (character - 'a' + key) % ALPHABET_SIZE));
            } else {
                encrypted.append(character);
            }
        }

        return encrypted.toString();
    }

    private static String decryptByBruteForce() {
        try {
            String encryptedText = Files.readString(ENCRYPTED_PATH).trim();
            String[] commonWords = {"the", "and", "that", "this", "was", "with", "from", "have", "you", "for", "of", "it", "is"};

            for (int shift = 1; shift < ALPHABET_SIZE; shift++) {
                StringBuilder decrypted = new StringBuilder();
                for (char character : encryptedText.toCharArray()) {
                    if (Character.isUpperCase(character)) {
                        decrypted.append((char) ('A' + (character - 'A' - shift + ALPHABET_SIZE) % ALPHABET_SIZE));
                    } else if (Character.isLowerCase(character)) {
                        decrypted.append((char) ('a' + (character - 'a' - shift + ALPHABET_SIZE) % ALPHABET_SIZE));
                    } else {
                        decrypted.append(character);
                    }
                }

                String result = decrypted.toString().toLowerCase();
                int matchCount = 0;
                for (String word : commonWords) {
                    if (result.contains(word)) {
                        matchCount++;
                    }
                }

                if (matchCount >= 4) {
                    System.out.println("Successfully decrypted using brute force. Estimated key: " + shift);
                    return decrypted.toString();
                }
            }

            return "Brute force decryption failed!";
        } catch (IOException e) {
            System.out.println("Error reading encrypted file: " + e.getMessage());
            return null;
        }
    }

    private static String decryptByFrequencyAnalysis() {
        try {
            String encryptedText = Files.readString(ENCRYPTED_PATH);

            int[] frequencies = new int[ALPHABET_SIZE];
            for (char character : encryptedText.toCharArray()) {
                if (Character.isLowerCase(character)) {
                    frequencies[character - 'a']++;
                } else if (Character.isUpperCase(character)) {
                    frequencies[character - 'A']++;
                }
            }

            int maxFreqIndex = 0;
            for (int i = 1; i < ALPHABET_SIZE; i++) {
                if (frequencies[i] > frequencies[maxFreqIndex]) {
                    maxFreqIndex = i;
                }
            }

            int estimatedKey = (maxFreqIndex - ('e' - 'a') + ALPHABET_SIZE) % ALPHABET_SIZE;
            System.out.println("Estimated key based on frequency analysis: " + estimatedKey);

            StringBuilder decrypted = new StringBuilder();
            for (char character : encryptedText.toCharArray()) {
                if (Character.isLetter(character)) {
                    char base = Character.isUpperCase(character) ? 'A' : 'a';
                    char decryptedChar = (char) ((character - base - estimatedKey + ALPHABET_SIZE) % ALPHABET_SIZE + base);
                    decrypted.append(decryptedChar);
                } else {
                    decrypted.append(character);
                }
            }

            return decrypted.toString();
        } catch (IOException e) {
            System.out.println("Error reading encrypted file: " + e.getMessage());
            return null;
        }
    }
}