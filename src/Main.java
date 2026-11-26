import exception.CapacityFullException;
import exception.DuplicateRegistrationException;
import model.Participant;
import model.Person;
import model.Workshop;
import service.RegistrationService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Entry point aplikasi Workshop Registration & Suggester.
 * Menyediakan CLI interaktif dengan exception handling yang kokoh (Kualitas 1 & 2).
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RegistrationService service = new RegistrationService();

    public static void main(String[] args) {
        seedInitialData();

        System.out.println("==============================================================");
        System.out.println("   SELAMAT DATANG DI WORKSHOP REGISTRATION & SUGGESTER");
        System.out.println("      Tugas Besar Pemrograman Lanjut - TI-A Kelompok 5");
        System.out.println("==============================================================");

        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Pilih menu (0-7): ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Kualitas 1: Menangani input non-angka dengan peringatan lalu mengulang menu
                System.out.println("\n[PERINGATAN] Input '" + input + "' tidak valid! Masukkan angka pilihan menu (0-7).\n");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> menuTambahPeserta();
                    case 2 -> menuTambahWorkshop();
                    case 3 -> menuDaftarWorkshop();
                    case 4 -> menuLihatWorkshopTersedia();
                    case 5 -> menuRekapPendaftar();
                    case 6 -> menuLihatSemuaData();
                    case 7 -> menuEksporProlog();
                    case 0 -> {
                        running = false;
                        System.out.println("\nTerima kasih telah menggunakan sistem Workshop Registration & Suggester!");
                    }
                    default -> System.out.println("\n[PERINGATAN] Pilihan menu " + choice + " tidak tersedia. Silakan coba lagi.\n");
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR TERDETEKSI] " + e.getMessage() + "\n");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n----------------- MENU UTAMA APLIKASI -----------------");
        System.out.println("1. Tambah Data Peserta Baru (J1)");
        System.out.println("2. Tambah Data Workshop Baru (J1)");
        System.out.println("3. Daftarkan Peserta ke Workshop (J2)");
        System.out.println("4. Tampilkan Workshop Tersedia (Urut Tanggal - Stream J3)");
        System.out.println("5. Rekap Jumlah Pendaftar per Workshop (Stream J4)");
        System.out.println("6. Tampilkan Seluruh Data Master (Polymorphism & Collection)");
        System.out.println("7. Ekspor Data ke Prolog Knowledge Base (Stage 5 Sync)");
        System.out.println("0. Keluar");
        System.out.println("-------------------------------------------------------");
    }

    // --- Seeding Data Awal Sesuai Skenario Dokumen LKM ---
    private static void seedInitialData() {
        // Peserta
        Participant p1 = new Participant("P001", "Aditya Widyatamaka", true, "backend");
        p1.addSkill("basic_java");
        p1.addSkill("oop");

        Participant p2 = new Participant("P002", "Chasbiah Azzahra", true, "backend");
        p2.addSkill("basic_java");

        Participant p3 = new Participant("P003", "Elshifa Viorissa", true, "ai");
        p3.addSkill("basic_prolog");

        Participant p4 = new Participant("P004", "Dzauq Non-Aktif", false, "backend");
        p4.addSkill("basic_java");

        service.addParticipant(p1);
        service.addParticipant(p2);
        service.addParticipant(p3);
        service.addParticipant(p4);

        // Workshop
        Workshop w1 = new Workshop("W01", "Backend Java Fundamentals", "backend", 20, LocalDate.of(2026, 12, 1), "basic_java");
        Workshop w2 = new Workshop("W02", "Advanced OOP & Pattern", "backend", 15, LocalDate.of(2026, 12, 5), "oop");
        Workshop w3 = new Workshop("W03", "Intro to AI with Logic", "ai", 1, LocalDate.of(2026, 12, 10), "basic_prolog");

        service.addWorkshop(w1);
        service.addWorkshop(w2);
        service.addWorkshop(w3);
    }

    // --- Menu J1: Tambah Peserta ---
    private static void menuTambahPeserta() {
        System.out.println("\n>>> [J1] TAMBAH DATA PESERTA");
        System.out.print("Masukkan ID Peserta (misal P005): ");
        String id = scanner.nextLine().trim();

        System.out.print("Masukkan Nama Lengkap: ");
        String name = scanner.nextLine().trim();

        System.out.print("Status Aktif? (y/n, default y): ");
        String actInput = scanner.nextLine().trim().toLowerCase();
        boolean active = !actInput.equals("n");

        System.out.print("Minat Topik (misal: backend / ai): ");
        String interest = scanner.nextLine().trim();

        Participant p = new Participant(id, name, active, interest);

        System.out.print("Masukkan skill yang telah dikuasai (pisahkan dengan koma, contoh: basic_java, oop): ");
        String skillsInput = scanner.nextLine().trim();
        if (!skillsInput.isEmpty()) {
            for (String s : skillsInput.split(",")) {
                p.addSkill(s.trim());
            }
        }

        service.addParticipant(p);
        System.out.println("[SUKSES] Peserta " + name + " (" + id + ") berhasil ditambahkan!");
    }

    // --- Menu J1: Tambah Workshop ---
    private static void menuTambahWorkshop() {
        System.out.println("\n>>> [J1] TAMBAH DATA WORKSHOP");
        System.out.print("Masukkan ID Workshop (misal W04): ");
        String id = scanner.nextLine().trim();

        System.out.print("Masukkan Judul Workshop: ");
        String title = scanner.nextLine().trim();

        System.out.print("Masukkan Topik (misal: backend / ai): ");
        String topic = scanner.nextLine().trim();

        int capacity;
        while (true) {
            System.out.print("Masukkan Kapasitas Peserta: ");
            String capInput = scanner.nextLine().trim();
            try {
                capacity = Integer.parseInt(capInput);
                if (capacity <= 0) {
                    System.out.println("[PERINGATAN] Kapasitas harus bilangan positif!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Input kapasitas harus angka!");
            }
        }

        LocalDate date;
        while (true) {
            System.out.print("Masukkan Tanggal Pelaksanaan (format YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateInput);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("[PERINGATAN] Format tanggal salah! Gunakan format YYYY-MM-DD.");
            }
        }

        System.out.print("Masukkan Prasyarat Skill (opsional, tekan enter jika tidak ada): ");
        String prereq = scanner.nextLine().trim();
        if (prereq.isEmpty()) prereq = null;

        Workshop w = new Workshop(id, title, topic, capacity, date, prereq);
        service.addWorkshop(w);
        System.out.println("[SUKSES] Workshop " + title + " (" + id + ") berhasil ditambahkan!");
    }

    // --- Menu J2: Daftarkan Peserta ---
    private static void menuDaftarWorkshop() {
        System.out.println("\n>>> [J2] PENDAFTARAN PESERTA KE WORKSHOP");
        System.out.print("Masukkan ID Peserta (misal P001): ");
        String pId = scanner.nextLine().trim();

        System.out.print("Masukkan ID Workshop (misal W01): ");
        String wId = scanner.nextLine().trim();

        try {
            service.register(pId, wId);
            Workshop w = service.getWorkshop(wId);
            System.out.println("\n[SUKSES] Pendaftaran BERHASIL!");
            System.out.println("Detail: Peserta " + pId + " terdaftar di " + w.getTitle() + ".");
            System.out.println("Sisa kuota workshop saat ini: " + w.getAvailableSeats() + " kursi.");
        } catch (CapacityFullException e) {
            System.out.println("\n[PENDAFTARAN DITOLAK] " + e.getMessage());
        } catch (DuplicateRegistrationException e) {
            System.out.println("\n[PENDAFTARAN DITOLAK] " + e.getMessage());
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\n[VALIDASI GAGAL] " + e.getMessage());
        }
    }

    // --- Menu J3: List Workshop Tersedia (Stream API) ---
    private static void menuLihatWorkshopTersedia() {
        System.out.println("\n>>> [J3] DAFTAR WORKSHOP TERSEDIA (FILTER SISA KURSI & URUT TANGGAL)");
        List<Workshop> available = service.listAvailableWorkshopsSortedByDate();

        if (available.isEmpty()) {
            System.out.println("Saat ini seluruh workshop telah penuh atau belum ada data workshop.");
        } else {
            System.out.printf("%-6s | %-30s | %-12s | %-12s | %-10s | %-15s\n",
                    "ID", "Judul", "Topik", "Tanggal", "Sisa Kursi", "Prasyarat");
            System.out.println("--------------------------------------------------------------------------------------------------");
            available.forEach(w -> System.out.printf("%-6s | %-30s | %-12s | %-12s | %-10d | %-15s\n",
                    w.getId(),
                    w.getTitle(),
                    w.getTopic(),
                    w.getDate(),
                    w.getAvailableSeats(),
                    (w.getPrerequisiteSkill() != null ? w.getPrerequisiteSkill() : "-")
            ));
        }
    }

    // --- Menu J4: Rekap Pendaftar per Workshop (Stream API groupingBy) ---
    private static void menuRekapPendaftar() {
        System.out.println("\n>>> [J4] REKAPITULASI JUMLAH PENDAFTAR PER WORKSHOP");
        Map<String, Long> summary = service.summaryRegistrationCountPerWorkshop();

        if (summary.isEmpty()) {
            System.out.println("Belum ada pendaftaran yang tercatat dalam sistem.");
        } else {
            System.out.printf("%-12s | %-30s | %-15s\n", "ID Workshop", "Nama Workshop", "Jumlah Pendaftar");
            System.out.println("------------------------------------------------------------------");
            summary.forEach((wId, count) -> {
                Workshop w = service.getWorkshop(wId);
                String title = (w != null) ? w.getTitle() : "Tidak Diketahui";
                System.out.printf("%-12s | %-30s | %-15d orang\n", wId, title, count);
            });
            System.out.println("------------------------------------------------------------------");
            System.out.println("Total transaksi pendaftaran tercatat: " + service.getAllRegistrations().size());
        }
    }

    // --- Menu 6: Tampilkan Semua Data Master & Polymorphism ---
    private static void menuLihatSemuaData() {
        System.out.println("\n>>> DATA MASTER PESERTA (DEMONSTRASI POLYMORPHISM & OOP)");
        List<Person> personList = new ArrayList<>(service.getAllParticipants());
        for (Person p : personList) {
            // Polymorphism: memanggil p.getRole() lewat referensi Person
            System.out.println(p.toString());
        }

        System.out.println("\n>>> DATA MASTER WORKSHOP");
        service.getAllWorkshops().forEach(System.out::println);
    }

    // --- Menu 7: Ekspor ke File Fakta Prolog (.pl) ---
    private static void menuEksporProlog() {
        System.out.println("\n>>> [STAGE 5] EKSPOR DATA KE PROLOG KNOWLEDGE BASE");
        File dir = new File("prolog");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File outFile = new File(dir, "exported_data.pl");
        try {
            service.exportToPrologKnowledgeBase(outFile);
            System.out.println("[SUKSES] Data runtime Java berhasil diekspor ke: " + outFile.getAbsolutePath());
            System.out.println("Fakta Prolog yang diekspor mencakup: active/1, completed/2, interest/2, topic/2, requires/2, seats_available/1.");
        } catch (IOException e) {
            System.out.println("[GAGAL EKSPOR] " + e.getMessage());
        }
    }
}
