package com.gorantla.todo.domain.command;

import org.springframework.stereotype.Component;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class EncryptionCommand implements AlgorithmCommand {
    
    private static final String DEFAULT_TEXT = "Hello, World!";
    
    @Override
    public String execute() {
        return execute(DEFAULT_TEXT);
    }
    
    public String execute(String text) {
        if (text == null || text.isEmpty()) {
            return "Invalid input: text cannot be empty";
        }
        
        try {
            // Generate a secret key for AES encryption
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            
            // Create cipher for encryption
            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            // Encrypt the text
            byte[] encryptedBytes = encryptCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
            
            // Create cipher for decryption
            Cipher decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            // Decrypt the text
            byte[] decryptedBytes = decryptCipher.doFinal(Base64.getDecoder().decode(encryptedText));
            String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            StringBuilder result = new StringBuilder();
            result.append("Encryption Algorithm\n");
            result.append("Original text: ").append(text).append("\n");
            result.append("Encrypted text: ").append(encryptedText).append("\n");
            result.append("Decrypted text: ").append(decryptedText);
            
            return result.toString();
            
        } catch (Exception e) {
            return "Encryption failed: " + e.getMessage();
        }
    }
}
