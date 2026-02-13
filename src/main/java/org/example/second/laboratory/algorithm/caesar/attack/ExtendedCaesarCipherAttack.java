package org.example.second.laboratory.algorithm.caesar.attack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class ExtendedCaesarCipherAttack {

    private static final int ALPHABET_SIZE = 26;

    private static final Path INPUT_FILE = Paths.get("src/main/resources/attack/input.txt");
    private static final Path ENCRYPTED_FILE = Paths.get("src/main/resources/attack/output_encrypted.txt");
    private static final Path DECRYPTED_FILE = Paths.get("src/main/resources/attack/output_decrypted.txt");
    private static final Path SHIFT_KEY_FILE = Paths.get("src/main/resources/attack/keys/numeric_key.txt");
    private static final Path ALPHABET_KEY_FILE = Paths.get("src/main/resources/attack/keys/alphabet_key.txt");

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
                Files.writeString(DECRYPTED_FILE, decryptedText);
            }
            System.out.println("Decryption completed. Result saved to output_decrypted.txt!");
        } catch (IOException e) {
            System.out.println("Error writing decrypted file: " + e.getMessage());
        }

        scanner.close();
    }

    private static void encryptText() {
        try {
            String plainText = Files.readString(INPUT_FILE).trim();
            int shiftKey = readShiftKey();
            String alphabetKey = readAlphabetKey();
            if (alphabetKey == null || alphabetKey.isEmpty()) return;

            String alphabet = buildModifiedAlphabet(alphabetKey);
            String encryptedText = encrypt(plainText, shiftKey, alphabet);

            Files.writeString(ENCRYPTED_FILE, encryptedText);
            System.out.println("Text successfully encrypted and saved to output_encrypted.txt!");
        } catch (IOException e) {
            System.out.println("Error reading or writing files: " + e.getMessage());
        }
    }

    private static int readShiftKey() {
        try {
            String keyContent = Files.readString(SHIFT_KEY_FILE).trim();
            return Integer.parseInt(keyContent);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading shift key: " + e.getMessage());
            return -1;
        }
    }

    private static String readAlphabetKey() {
        try {
            String keyContent = Files.readString(ALPHABET_KEY_FILE).trim();

            if (keyContent.isEmpty()) {
                System.out.println("Alphabet key cannot be empty!");
                return null;
            }

            return keyContent;
        } catch (IOException e) {
            System.out.println("Error reading alphabet key: " + e.getMessage());
            return null;
        }
    }

    private static String buildModifiedAlphabet(String key) {
        Set<Character> seen = new LinkedHashSet<>();

        for (char c : key.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) seen.add(c);
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            seen.add(c);
        }

        StringBuilder result = new StringBuilder();
        for (char c : seen) result.append(c);

        return result.toString();
    }

    private static String encrypt(String text, int shiftKey, String alphabet) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char upperC = Character.toUpperCase(c);
                int index = alphabet.indexOf(upperC);
                char shiftedChar = alphabet.charAt((index + shiftKey) % alphabet.length());
                result.append(Character.isLowerCase(c) ? Character.toLowerCase(shiftedChar) : shiftedChar);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String decryptByBruteForce() {
        try {
            String encryptedText = Files.readString(ENCRYPTED_FILE).trim();
            String alphabetKey = readAlphabetKey();
            if (alphabetKey == null) return null;

            String alphabet = buildModifiedAlphabet(alphabetKey);
            String[] commonWords = {"the", "and", "that", "this", "was", "with", "from", "have", "you", "for", "of", "it", "is"};

            for (int shift = 1; shift < ALPHABET_SIZE; shift++) {
                StringBuilder decrypted = new StringBuilder();

                for (char c : encryptedText.toCharArray()) {
                    if (Character.isLetter(c)) {
                        char upperC = Character.toUpperCase(c);
                        int index = alphabet.indexOf(upperC);
                        char shiftedChar = alphabet.charAt((index - shift + ALPHABET_SIZE) % ALPHABET_SIZE);
                        decrypted.append(Character.isLowerCase(c) ? Character.toLowerCase(shiftedChar) : shiftedChar);
                    } else {
                        decrypted.append(c);
                    }
                }

                String result = decrypted.toString().toLowerCase();
                int matchCount = 0;
                for (String word : commonWords) {
                    if (result.contains(word)) matchCount++;
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
            String encryptedText = Files.readString(ENCRYPTED_FILE).trim();
            String alphabetKey = readAlphabetKey();
            if (alphabetKey == null) return null;

            String alphabet = buildModifiedAlphabet(alphabetKey);
            int[] frequencies = new int[ALPHABET_SIZE];

            for (char c : encryptedText.toUpperCase().toCharArray()) {
                int index = alphabet.indexOf(c);
                if (index != -1) frequencies[index]++;
            }

            int maxFreqIndex = 0;
            for (int i = 1; i < ALPHABET_SIZE; i++) {
                if (frequencies[i] > frequencies[maxFreqIndex]) maxFreqIndex = i;
            }

            int assumedEIndex = alphabet.indexOf('E');
            int estimatedKey = (maxFreqIndex - assumedEIndex + ALPHABET_SIZE) % ALPHABET_SIZE;
            System.out.println("Estimated key based on frequency analysis: " + estimatedKey);

            StringBuilder decrypted = new StringBuilder();
            for (char c : encryptedText.toCharArray()) {
                if (Character.isLetter(c)) {
                    char upperC = Character.toUpperCase(c);
                    int index = alphabet.indexOf(upperC);
                    char shiftedChar = alphabet.charAt((index - estimatedKey + ALPHABET_SIZE) % ALPHABET_SIZE);
                    decrypted.append(Character.isLowerCase(c) ? Character.toLowerCase(shiftedChar) : shiftedChar);
                } else {
                    decrypted.append(c);
                }
            }

            return decrypted.toString();
        } catch (IOException e) {
            System.out.println("Error reading encrypted file: " + e.getMessage());
            return null;
        }
    }
}