package com.example.applicationtienda.infrastructure.di;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {

    // 1. Instancia única (volátil para seguridad en hilos)
    private static volatile ServiceContainer instance;

    // Mapa para guardar las dependencias (Interfaz -> Implementación)
    private final Map<Class<?>, Object> services = new HashMap<>();

    // 2. Constructor privado (Nadie puede instanciarlo con 'new')
    private ServiceContainer() {
    }

    // 3. Método público para obtener la única instancia (Thread-safe)
    public static ServiceContainer getInstance() {
        if (instance == null) {
            synchronized (ServiceContainer.class) {
                if (instance == null) {
                    instance = new ServiceContainer();
                }
            }
        }
        return instance;
    }

    // 4. Método para registrar dependencias (Bind)
    public <T> void registerService(Class<T> interfaceType, T implementation) {
        services.put(interfaceType, implementation);
    }

    // 5. Método para resolver dependencias (Get)
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> interfaceType) {
        T service = (T) services.get(interfaceType);
        if (service == null) {
            throw new IllegalArgumentException("Servicio no registrado: " + interfaceType.getSimpleName());
        }
        return service;
    }

    // Método para limpiar (Útil para tests)
    public void clear() {
        services.clear();
    }
}
