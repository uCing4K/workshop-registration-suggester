package service;

import exception.CapacityFullException;
import exception.DuplicateRegistrationException;

/**
 * Interface Registrable mendefinisikan kontrak operasi transaksi pendaftaran.
 * Memisahkan kontrak abstraksi dari detail implementasinya.
 */
public interface Registrable {
    /**
     * Mendaftarkan peserta ke workshop.
     * @param participantId ID peserta
     * @param workshopId ID workshop
     * @throws CapacityFullException jika kuota workshop sudah penuh
     * @throws DuplicateRegistrationException jika peserta sudah pernah terdaftar di workshop ini
     */
    void register(String participantId, String workshopId) throws CapacityFullException, DuplicateRegistrationException;

    /**
     * Membatalkan pendaftaran peserta dari workshop.
     * @param participantId ID peserta
     * @param workshopId ID workshop
     * @return true jika pembatalan berhasil, false jika data pendaftaran tidak ditemukan
     */
    boolean cancel(String participantId, String workshopId);
}
