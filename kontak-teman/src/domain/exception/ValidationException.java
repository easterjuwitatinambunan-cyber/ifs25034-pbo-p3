package domain.exception;

/**
 * Dilempar ketika validasi aturan bisnis atau input domain gagal.
 * Pesan exception sudah berupa kalimat yang siap ditampilkan ke pengguna.
 */
public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message);
    }
}
