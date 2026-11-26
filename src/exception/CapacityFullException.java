package exception;

/**
 * Exception yang dilempar ketika pendaftaran workshop gagal karena kapasitas ruangan sudah penuh.
 * Menegakkan Business Rule 1: Kapasitas workshop bersifat maksimum.
 */
public class CapacityFullException extends Exception {
    public CapacityFullException(String message) {
        super(message);
    }
}
