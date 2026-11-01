package com.API_de_Cursos.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Long id;

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.id = null;
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s não encontrado com ID: %d", resourceName, id));
        this.resourceName = resourceName;
        this.id = id;
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("%s não encontrado com %s: %s", resourceName, field, value));
        this.resourceName = resourceName;
        this.id = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Long getId() {
        return id;
    }
}
