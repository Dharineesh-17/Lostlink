package com.example.lostlink.exception;

/**
 * ============================================================
 * ResourceNotFoundException — Thrown When a Resource is Not Found
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * When a client requests a resource by ID (e.g., GET /api/items/999)
 * and that ID doesn't exist in the database, we throw this exception
 * instead of returning null. The GlobalExceptionHandler catches it
 * and returns a proper 404 Not Found response.
 * 
 * Extends RuntimeException (unchecked) because "resource not found"
 * is a programmer error / client error — it should NOT force the
 * developer to add try-catch blocks everywhere.
 * ============================================================
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName() { return fieldName; }
    public Object getFieldValue() { return fieldValue; }
}