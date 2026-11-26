package exception;

/**
 * Exception yang dilempar ketika seorang peserta mencoba mendaftar kembali pada workshop yang sama.
 * Menegakkan Business Rule 2: Satu peserta tidak boleh mendaftar dua kali pada workshop yang sama.
 */
public class DuplicateRegistrationException extends Exception {
    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
