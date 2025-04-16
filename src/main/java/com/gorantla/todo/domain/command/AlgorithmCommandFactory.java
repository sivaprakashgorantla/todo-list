package com.gorantla.todo.domain.command;

import com.gorantla.todo.domain.entity.Task;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;

@Component
public class AlgorithmCommandFactory {
    
    private final Map<Task.AlgorithmType, AlgorithmCommand> commands;
    
    public AlgorithmCommandFactory(
            FibonacciCommand fibonacciCommand,
            SortCommand sortCommand,
            EncryptionCommand encryptionCommand,
            CompressionCommand compressionCommand) {
        
        commands = new HashMap<>();
        commands.put(Task.AlgorithmType.FIBONACCI, fibonacciCommand);
        commands.put(Task.AlgorithmType.SORT, sortCommand);
        commands.put(Task.AlgorithmType.ENCRYPTION, encryptionCommand);
        commands.put(Task.AlgorithmType.COMPRESSION, compressionCommand);
    }
    
    public AlgorithmCommand getCommand(Task.AlgorithmType algorithmType) {
        return commands.get(algorithmType);
    }
}
