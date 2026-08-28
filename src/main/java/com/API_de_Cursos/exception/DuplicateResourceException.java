package com.API_de_Cursos.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String field, String value) {
        super(String.format("%s já cadastrado com %s: %s", resourceName, field, value));
    }

    public DuplicateResourceException(String resourceName, String field) {
        super(String.format("%s já cadastrado com este %s", resourceName, field));
    }
}
