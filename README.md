# Information Security — Labs

This repository contains **Java programs** for laboratory exercises in the **Information Security
course** at **UTM (Technical University of Moldova)**. The labs focus on the **practical implementation
of encryption and decryption algorithms**, including **Caesar, Vigenère, and RSA**. The programs cover
data handling, applying cryptographic algorithms, testing their security, and visualizing results, aiming
to support **hands-on learning and experimentation in information security**.

## Lab 1 — Caesar Cipher

- Goal: Learn how to implement the **Caesar cipher** for encrypting text.
- Contents (summary):
    - Data handling: reading and storing input text.
    - Encryption: applying the Caesar shift with **single-key and double-key variations**.
    - Decryption: reversing the shift using the **same key(s)** to retrieve the original text.
    - Visualization/Output: displaying encrypted and decrypted text.

## Lab 2 — Caesar Cipher Attacks

- Goal: Implement **encryption and decryption of Caesar cipher**, including decryption using brute force and frequency analysis.
- Contents (summary):
    - Data handling: reading and storing input text.
    - Encryption: applying the Caesar shift with **single-key and double-key variations**.
    - Decryption: trying all possible shifts (brute force) and analyzing letter frequencies.
    - Visualization/Output: displaying encrypted and decrypted text along with frequency analysis results.

## Lab 3 — Vigenère Cipher

- Goal: Implement the **Vigenère cipher** for polyalphabetic substitution encryption.
- Contents (summary):
    - Data handling: reading input text and **encryption key (keyword)**.
    - Encryption: applying the Vigenère algorithm using the **keyword**.
    - Decryption: reversing the encryption using the **same keyword**.
    - Visualization/Output: displaying encrypted and decrypted text.

## Lab 4 — RSA Algorithm

- Goal: Understand and implement the **RSA public-key cryptosystem**.
- Contents (summary):
    - Key generation: generating **public and private keys**.
    - Encryption: encrypting messages using the **public key**.
    - Decryption: decrypting messages using the **private key**.
    - Visualization/Output: displaying keys, encrypted messages, and decrypted messages.

## Installation

1. **Clone the repository**

```bash
  git clone https://github.com/Constantin-Stamate/crypto-algorithms-practice
```

2. **Navigate to the project folder**

```bash
  cd crypto-algorithms-practice
```

3. **Compile the Java sources**

```bash
  javac -d out $(find src/main/java -name "*.java")
```

4. **Run all demo classes**

```bash
   java -cp out:. org.example.first.laboratory.algorithm.caesar.CaesarCipherDemo
   java -cp out:. org.example.first.laboratory.algorithm.caesar.CaesarCipherExtendedDemo
          
   java -cp out:. org.example.second.laboratory.algorithm.caesar.attack.CaesarCipherAttack
   java -cp out:. org.example.second.laboratory.algorithm.caesar.attack.ExtendedCaesarCipherAttack
          
   java -cp out:. org.example.third.laboratory.algorithm.vigenere.VigenereCipherDemo
   java -cp out:. org.example.third.laboratory.algorithm.vigenere.VigenereCipherExtendedDemo
          
   java -cp out:. org.example.fourth.laboratory.algorithm.rsa.RsaCipherDemo
```

## Resources

For guidance and references on cryptography and algorithms, you can check:

- [RSA Algorithm](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) — for understanding the RSA encryption method
- [Caesar Cipher](https://en.wikipedia.org/wiki/Caesar_cipher) — for the classic shift cipher
- [Vigenère Cipher](https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher) — for polyalphabetic substitution

## Technologies

- Programming Language: Java
- Editor/IDE: IntelliJ IDEA

## Author

This project was developed as part of the **Information Security Course** at **UTM (Technical University of Moldova)**, where the implemented cryptographic algorithms were studied and applied.

- GitHub: [Constantin-Stamate](https://github.com/Constantin-Stamate)
- Email: constantinstamate.r@gmail.com