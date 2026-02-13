package org.example.third.laboratory.algorithm.vigenere;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VigenereCipherExtendedDemo {

    static int[] frequency = new int[10000];
    static int[] divCount = new int[10000];

    static final double[] ENGLISH_FREQUENCIES = {
            8.17, 1.49, 2.78, 4.25, 12.70, 2.23, 2.02, 6.09, 6.97, 0.15,
            0.77, 4.03, 2.41, 6.75, 7.51, 1.93, 0.10, 5.99, 6.33, 9.06,
            2.76, 0.98, 2.36, 0.15, 1.97, 0.07
    };

    static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        StringBuilder encryptedText = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/vigenere/output.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                encryptedText.append(line.replace(" ", "").toUpperCase());
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        ArrayList<String> sequenceList = new ArrayList<>();

        if (encryptedText.length() >= 6) {
            for (int k = 3; k < 7; k++) {
                for (int i = 0; i <= encryptedText.length() - k; i++) {
                    String sequence = encryptedText.substring(i, i + k);
                    sequenceList.add(sequence);
                    System.out.println(sequence);
                }
            }
        }

        int[] foundDistances = new int[10000];
        int q = 0, pos = 0;

        for (int i = 0; i < sequenceList.size(); i++) {
            int count = 0;
            for (int j = i; j < sequenceList.size(); j++) {
                if (sequenceList.get(i).equals(sequenceList.get(j))) {
                    count++;
                    pos = j;
                }
            }
            if (count > 1) {
                System.out.println(sequenceList.get(i) + " ----> " + count + " at position: " + pos);
                frequency[pos - i]++;
            }
        }

        System.out.println("Distances between sequences:");
        for (int i = 999; i >= 0; i--) {
            if (frequency[i] != 0) {
                foundDistances[q] = i;
                System.out.print(foundDistances[q] + " ");
                q++;
            }
        }

        int maxDiv = -9999;
        int probableKeyLength = 0;

        for (int i = 0; i < q; i++) {
            for (int k = 2; k < 20; k++) {
                if (foundDistances[i] % k == 0)
                    divCount[k]++;
            }
        }

        for (int i = 4; i < 25; i++) {
            if (divCount[i] >= maxDiv) {
                maxDiv = divCount[i];
                probableKeyLength = i;
            }
        }

        System.out.println("\nProbable key length: " + probableKeyLength);

        char[][] textMatrix = new char[60][60];

        for (int i = 0; i < encryptedText.length(); i++) {
            for (int j = 0; j < probableKeyLength; j++) {
                if (i * probableKeyLength + j < encryptedText.length()) {
                    textMatrix[j][i] = encryptedText.charAt(i * probableKeyLength + j);
                    System.out.print(textMatrix[j][i] + " ");
                }
            }

            System.out.println();
        }

        StringBuilder keyWord = new StringBuilder();
        ArrayList<Character> letterColumn = new ArrayList<>();

        for (int i = 0; i < probableKeyLength; i++) {
            letterColumn.clear();

            for (int j = 0; j < 60 && textMatrix[i][j] != '\0'; j++) {
                letterColumn.add(textMatrix[i][j]);
            }

            keyWord.append(determineLetter(letterColumn));
        }

        System.out.println("Probable key: " + keyWord);

        String decryptedText = decrypt(encryptedText.toString(), keyWord.toString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/vigenere/input.txt"))) {
            writer.write(decryptedText);
            System.out.println("Decrypted text saved in input.txt!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static char determineLetter(ArrayList<Character> column) {
        Map<Character, Integer> frequencies = new HashMap<>();
        char letter = 0;
        double index;
        double max = -1;

        for (char c : column) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }

        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            System.out.println("\n" + entry.getKey() + " ---> " + entry.getValue());
            index = cosineIndex(entry.getKey(), entry.getValue());

            if (index > max) {
                max = index;
                letter = entry.getKey();
            }

            System.out.print(index + "\n");
        }

        return letter;
    }

    private static double cosineIndex(char c, Integer count) {
        int position = ENGLISH_ALPHABET.indexOf(c);
        if (position == -1) return 0.0;
        double index = Math.pow((count - ENGLISH_FREQUENCIES[position]), 2) / ENGLISH_FREQUENCIES[position];
        return Math.round(index * 100.0) / 100.0;
    }

    private static String decrypt(String encryptedText, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int alphabetLength = ENGLISH_ALPHABET.length();
        int j = 0;

        for (int i = 0; i < encryptedText.length(); i++) {
            char c = encryptedText.charAt(i);
            char keyChar = key.charAt(j % key.length());

            int posC = ENGLISH_ALPHABET.indexOf(c);
            int posK = ENGLISH_ALPHABET.indexOf(keyChar);

            if (posC == -1 || posK == -1) {
                decryptedText.append(c);
            } else {
                int posD = (posC - posK + alphabetLength) % alphabetLength;
                decryptedText.append(ENGLISH_ALPHABET.charAt(posD));
            }

            j++;
        }

        return decryptedText.toString();
    }
}