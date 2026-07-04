package com.example.applicationtienda.patterns.behavioral;

import android.util.Log;
import java.util.Stack;

public class CommandHistory {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    private static final String TAG = "CommandHistory";

    // Ejecutar un comando
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Limpiar redo cuando se ejecuta un nuevo comando
        Log.d(TAG, "Ejecutado: " + command.getDescription());
    }

    // Deshacer la última acción
    public boolean undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            Log.d(TAG, "Deshecho: " + command.getDescription());
            return true;
        }
        Log.d(TAG, "No hay acciones para deshacer");
        return false;
    }

    // Rehacer la última acción deshecha
    public boolean redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            Log.d(TAG, "Rehecho: " + command.getDescription());
            return true;
        }
        Log.d(TAG, "No hay acciones para rehacer");
        return false;
    }

    // Ver historial
    public void printHistory() {
        Log.d(TAG, "=== HISTORIAL DE COMANDOS ===");
        Log.d(TAG, "Pendientes de deshacer: " + undoStack.size());
        Log.d(TAG, "Pendientes de rehacer: " + redoStack.size());
    }
}