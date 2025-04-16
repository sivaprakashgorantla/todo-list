package com.gorantla.todo.domain.command;

import org.springframework.stereotype.Component;

@Component
public class FibonacciCommand implements AlgorithmCommand {
    
    private static final int DEFAULT_SEQUENCE_LENGTH = 10;
    
    @Override
    public String execute() {
        return execute(DEFAULT_SEQUENCE_LENGTH);
    }
    
    public String execute(int n) {
        if (n <= 0) {
            return "Invalid input: sequence length must be positive";
        }
        
        StringBuilder result = new StringBuilder();
        long[] sequence = new long[n];
        
        // Initialize the first two numbers
        if (n >= 1) sequence[0] = 0;
        if (n >= 2) sequence[1] = 1;
        
        // Generate the fibonacci sequence
        for (int i = 2; i < n; i++) {
            sequence[i] = sequence[i-1] + sequence[i-2];
        }
        
        // Build the result string
        result.append("Fibonacci Sequence (").append(n).append(" numbers): ");
        for (int i = 0; i < n; i++) {
            result.append(sequence[i]);
            if (i < n - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }
}
