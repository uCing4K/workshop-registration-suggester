package test;

import exception.CapacityFullException;
import exception.DuplicateRegistrationException;
import model.Participant;
import model.Workshop;
import service.RegistrationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Pengujian Unit Mandiri (Standalone Test Runner) untuk memvalidasi minimal 5 skenario pengujian Java.
 * Sesuai dengan spesifikasi LKM Pertemuan 12 & M13.
 */
public class RegistrationTest {

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("   RUNNING AUTOMATED UNIT TESTS: REGISTRATION SERVICE");
        System.out.println("==============================================================");

        int passed = 0;
        int failed = 0;

        // Test 1: Pendaftaran Normal Berhasil
        try {
            testPendaftaranBerhasil();
            System.out.println("[PASS] Test 1: Pendaftaran normal berhasil dan mengurangi sisa kursi");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 1: " + t.getMessage());
            failed++;
        }

        // Test 2: Pendaftaran Gagal karena Kapasitas Penuh (CapacityFullException)
        try {
            testCapacityFullException();
            System.out.println("[PASS] Test 2: CapacityFullException berhasil dicegat saat kuota penuh");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 2: " + t.getMessage());
            failed++;
        }

        // Test 3: Pendaftaran Gagal karena Duplikat (DuplicateRegistrationException)
        try {
            testDuplicateRegistrationException();
            System.out.println("[PASS] Test 3: DuplicateRegistrationException berhasil dicegat saat daftar ulang");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 3: " + t.getMessage());
            failed++;
        }

        // Test 4: Stream Operasi 1 - Filter Workshop Belum Penuh dan Urut Tanggal
        try {
            testStreamFilterAndSort();
            System.out.println("[PASS] Test 4: Stream API berhasil menyaring workshop tersedia dan urut tanggal terdekat");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 4: " + t.getMessage());
            failed++;
        }

        // Test 5: Stream Operasi 2 - Rekapitulasi Jumlah Pendaftar dengan GroupingBy
        try {
            testStreamGroupingBySummary();
            System.out.println("[PASS] Test 5: Stream groupingBy & counting berhasil merekap jumlah pendaftar per workshop");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 5: " + t.getMessage());
            failed++;
        }

        System.out.println("==============================================================");
        System.out.printf("   HASIL AKHIR PENGUJIAN: %d LULUS, %d GAGAL (Total: %d)\n", passed, failed, (passed + failed));
        System.out.println("==============================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testPendaftaranBerhasil() throws Exception {
        RegistrationService service = new RegistrationService();
        Participant p = new Participant("P001", "Aditya", true, "backend");
        Workshop w = new Workshop("W01", "Backend Java", "backend", 5, LocalDate.of(2026, 12, 1), "basic_java");

        service.addParticipant(p);
        service.addWorkshop(w);

        service.register("P001", "W01");

        if (w.getRegistered() != 1 || w.getAvailableSeats() != 4) {
            throw new AssertionError("Kapasitas terdaftar tidak bertambah dengan benar. Aktual: " + w.getRegistered());
        }
        if (service.getAllRegistrations().size() != 1) {
            throw new AssertionError("List registrasi tidak mencatat transaksi baru.");
        }
    }

    private static void testCapacityFullException() throws Exception {
        RegistrationService service = new RegistrationService();
        Participant p1 = new Participant("P001", "Aditya", true, "backend");
        Participant p2 = new Participant("P002", "Chasbiah", true, "backend");
        // Kapasitas hanya 1
        Workshop w = new Workshop("W01", "Mini Workshop", "backend", 1, LocalDate.of(2026, 12, 1), null);

        service.addParticipant(p1);
        service.addParticipant(p2);
        service.addWorkshop(w);

        service.register("P001", "W01");

        try {
            service.register("P002", "W01");
            throw new AssertionError("Seharusnya melempar CapacityFullException karena kuota hanya 1!");
        } catch (CapacityFullException e) {
            // Berhasil tertangkap
        }
    }

    private static void testDuplicateRegistrationException() throws Exception {
        RegistrationService service = new RegistrationService();
        Participant p = new Participant("P001", "Aditya", true, "backend");
        Workshop w = new Workshop("W01", "Backend Java", "backend", 10, LocalDate.of(2026, 12, 1), null);

        service.addParticipant(p);
        service.addWorkshop(w);

        service.register("P001", "W01");

        try {
            service.register("P001", "W01");
            throw new AssertionError("Seharusnya melempar DuplicateRegistrationException saat peserta mendaftar 2 kali!");
        } catch (DuplicateRegistrationException e) {
            // Berhasil tertangkap
        }
    }

    private static void testStreamFilterAndSort() {
        RegistrationService service = new RegistrationService();
        Workshop w1 = new Workshop("W01", "Workshop Desember", "backend", 10, LocalDate.of(2026, 12, 15), null);
        Workshop w2 = new Workshop("W02", "Workshop November", "backend", 10, LocalDate.of(2026, 11, 20), null);
        Workshop w3 = new Workshop("W03", "Workshop Penuh", "backend", 1, LocalDate.of(2026, 10, 1), null);

        service.addWorkshop(w1);
        service.addWorkshop(w2);
        service.addWorkshop(w3);

        // Penuhi W03
        try {
            w3.increaseRegistered();
        } catch (Exception ignored) {}

        List<Workshop> available = service.listAvailableWorkshopsSortedByDate();

        if (available.size() != 2) {
            throw new AssertionError("Jumlah workshop tersedia harus 2, aktual: " + available.size());
        }
        if (!available.get(0).getId().equals("W02")) {
            throw new AssertionError("Urutan pertama harusnya W02 (20 Nov), aktual: " + available.get(0).getId());
        }
        if (!available.get(1).getId().equals("W01")) {
            throw new AssertionError("Urutan kedua harusnya W01 (15 Des), aktual: " + available.get(1).getId());
        }
    }

    private static void testStreamGroupingBySummary() throws Exception {
        RegistrationService service = new RegistrationService();
        Participant p1 = new Participant("P001", "Aditya", true, "backend");
        Participant p2 = new Participant("P002", "Chasbiah", true, "backend");
        Participant p3 = new Participant("P003", "Elshifa", true, "backend");

        Workshop w1 = new Workshop("W01", "Backend Java", "backend", 10, LocalDate.of(2026, 12, 1), null);
        Workshop w2 = new Workshop("W02", "Frontend React", "frontend", 10, LocalDate.of(2026, 12, 5), null);

        service.addParticipant(p1);
        service.addParticipant(p2);
        service.addParticipant(p3);
        service.addWorkshop(w1);
        service.addWorkshop(w2);

        service.register("P001", "W01");
        service.register("P002", "W01");
        service.register("P003", "W02");

        Map<String, Long> summary = service.summaryRegistrationCountPerWorkshop();

        if (summary.get("W01") != 2L) {
            throw new AssertionError("Pendaftar W01 harusnya 2, aktual: " + summary.get("W01"));
        }
        if (summary.get("W02") != 1L) {
            throw new AssertionError("Pendaftar W02 harusnya 1, aktual: " + summary.get("W02"));
        }
    }
}
