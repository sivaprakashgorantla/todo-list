package com.gorantla.todo.domain.command;

import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class CompressionCommand implements AlgorithmCommand {
    
    private static final String DEFAULT_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +  
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    
    @Override
    public String execute() {
        return execute(DEFAULT_TEXT);
    }
    
    public String execute(String text) {
        if (text == null || text.isEmpty()) {
            return "Invalid input: text cannot be empty";
        }
        
        try {
            // Compress the text
            byte[] input = text.getBytes(StandardCharsets.UTF_8);
            byte[] compressedData = compress(input);
            String compressedText = Base64.getEncoder().encodeToString(compressedData);
            
            // Decompress the text
            byte[] decompressedData = decompress(compressedData);
            String decompressedText = new String(decompressedData, StandardCharsets.UTF_8);
            
            // Calculate compression ratio
            double compressionRatio = (double) input.length / compressedData.length;
            
            StringBuilder result = new StringBuilder();
            result.append("Compression Algorithm\n");
            result.append("Original size: ").append(input.length).append(" bytes\n");
            result.append("Compressed size: ").append(compressedData.length).append(" bytes\n");
            result.append("Compression ratio: ").append(String.format("%.2f", compressionRatio)).append(":1\n");
            result.append("Original text: ").append(text.length() > 50 ? text.substring(0, 50) + "..." : text).append("\n");
            result.append("Compressed text (Base64): ").append(compressedText.length() > 50 ? compressedText.substring(0, 50) + "..." : compressedText).append("\n");
            result.append("Decompressed matches original: ").append(text.equals(decompressedText));
            
            return result.toString();
            
        } catch (Exception e) {
            return "Compression failed: " + e.getMessage();
        }
    }
    
    private byte[] compress(byte[] data) throws Exception {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }
    
    private byte[] decompress(byte[] data) throws Exception {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }
}
