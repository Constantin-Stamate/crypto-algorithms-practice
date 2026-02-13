package org.example.third.laboratory.algorithm.vigenere;

import java.io.*;
import java.util.Locale;

public class VigenereCipherDemo {

    private static final int ALPHABET_SIZE = 26;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        char[][] table = new char[ALPHABET_SIZE][ALPHABET_SIZE];
        for (int row = 0; row < ALPHABET_SIZE; row++) {
            for (int col = 0; col < ALPHABET_SIZE; col++) {
                int idx = (row + col) % ALPHABET_SIZE;
                table[row][col] = ALPHABET.charAt(idx);
            }
        }

        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vigenere/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                textBuilder.append(line.toUpperCase(Locale.ENGLISH));
            }
        } catch (IOException e) {
            System.out.println("Error reading input.txt: " + e.getMessage());
            return;
        }

        String cleanText = removePunctuation(textBuilder.toString());

        String keyword;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vigenere/keys/key.txt"))) {
            keyword = br.readLine().toUpperCase(Locale.ENGLISH);
        } catch (IOException e) {
            System.out.println("Error reading key.txt: " + e.getMessage());
            return;
        }

        int[] textIndexes = new int[cleanText.length()];
        int validLength = 0;
        for (int i = 0; i < cleanText.length(); i++) {
            char c = cleanText.charAt(i);
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                textIndexes[validLength++] = index;
            }
        }

        int[] keyIndexes = new int[validLength];
        for (int i = 0; i < validLength; i++) {
            keyIndexes[i] = ALPHABET.indexOf(keyword.charAt(i % keyword.length()));
        }

        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < validLength; i++) {
            encrypted.append(table[textIndexes[i]][keyIndexes[i]]);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/vigenere/output.txt"))) {
            writer.write(encrypted.toString());
            System.out.println("Encrypted text has been saved to output.txt!");
        } catch (IOException e) {
            System.out.println("Error writing to output.txt: " + e.getMessage());
        }
    }

    private static String removePunctuation(String text) {
        return text.replaceAll("\\p{Punct}", "");
    }
}