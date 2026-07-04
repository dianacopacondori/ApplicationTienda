package com.example.applicationtienda.patterns.behavioral;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}
