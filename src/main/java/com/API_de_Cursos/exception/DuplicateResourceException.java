package com.API_de_Cursos.exception;

public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;

    public DuplicateResourceException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
    }

    public DuplicateResourceException(String resourceName, String field, String value) {
        super(String.format("%s já cadastrado com %s: %s", resourceName, field, value));
        this.resourceName = resourceName;
        this.fieldName = field;
    }

    public DuplicateResourceException(String resourceName, String field) {
        super(String.format("%s já cadastrado com este %s", resourceName, field));
        this.resourceName = resourceName;
        this.fieldName = field;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
