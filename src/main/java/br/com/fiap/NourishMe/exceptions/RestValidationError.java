package br.com.fiap.NourishMe.exceptions;

public record RestValidationError(String field, String message) {
    
}
