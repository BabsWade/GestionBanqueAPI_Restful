package sn.gestionbanque.gestioncompte.exception;

public class CompteInexistantException extends RuntimeException {
    public CompteInexistantException(String message) {
        super(message);
    }
}
