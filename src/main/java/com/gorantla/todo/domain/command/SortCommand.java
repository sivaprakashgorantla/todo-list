package com.gorantla.todo.domain.command;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Random;

@Component
public class SortCommand implements AlgorithmCommand {
    
    private static final int DEFAULT_ARRAY_SIZE = 10;
    private static final int MAX_VALUE = 100;
    
    @Override
    public String execute() {
        return execute(DEFAULT_ARRAY_SIZE);
    }
    
    public String execute(int size) {
        if (size <= 0) {
            return "Invalid input: array size must be positive";
        }
        
        // Generate random array
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(MAX_VALUE);
        }
        
        // Create a copy for sorting
        int[] originalArray = Arrays.copyOf(array, array.length);
        
        // Sort the array
        Arrays.sort(array);
        
        StringBuilder result = new StringBuilder();
        result.append("Sort Algorithm\n");
        result.append("Original array: ").append(Arrays.toString(originalArray)).append("\n");
        result.append("Sorted array: ").append(Arrays.toString(array));
        
        return result.toString();
    }
}
