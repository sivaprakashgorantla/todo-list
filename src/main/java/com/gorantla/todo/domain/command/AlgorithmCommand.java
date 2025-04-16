package com.gorantla.todo.domain.command;

/**
 * Command interface for the Command pattern implementation for algorithms
 */
public interface AlgorithmCommand {
    /**
     * Execute the algorithm
     * @return Result of the algorithm execution
     */
    String execute();
}
