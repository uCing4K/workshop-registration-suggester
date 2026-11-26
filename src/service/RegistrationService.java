package service;

import exception.CapacityFullException;
import exception.DuplicateRegistrationException;
import model.Participant;
import model.Registration;
import model.Workshop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation untuk operasi bisnis dan pengolahan data pendaftaran workshop.
 * Mengimplementasikan interface Registrable dan memanfaatkan Collections/Generics serta Stream API.
 */
public class RegistrationService implements Registrable {
    private final Map<String, Participant> participants;
    private final Map<String, Workshop> workshops;
    private final List<Registration> registrations;

    public RegistrationService() {
        this.participants = new HashMap<>();
        this.workshops = new HashMap<>();
        this.registrations = new ArrayList<>();
    }

    // --- Manajemen Master Data ---

    public void addParticipant(Participant participant) {
        if (participant == null || participant.getId() == null) {
            throw new IllegalArgumentException("Data peserta tidak boleh bernilai null");
        }
        if (participants.containsKey(participant.getId())) {
            throw new IllegalArgumentException("Peserta dengan ID " + participant.getId() + " sudah terdaftar di sistem.");
        }
        participants.put(participant.getId(), participant);
    }

    public void addWorkshop(Workshop workshop) {
        if (workshop == null || workshop.getId() == null) {
            throw new IllegalArgumentException("Data workshop tidak boleh bernilai null");
        }
        if (workshops.containsKey(workshop.getId())) {
            throw new IllegalArgumentException("Workshop dengan ID " + workshop.getId() + " sudah terdaftar di sistem.");
        }
        workshops.put(workshop.getId(), workshop);
    }

    public Participant getParticipant(String id) {
        return participants.get(id);
    }

    public Workshop getWorkshop(String id) {
        return workshops.get(id);
    }

    public Collection<Participant> getAllParticipants() {
        return Collections.unmodifiableCollection(participants.values());
    }

    public Collection<Workshop> getAllWorkshops() {
        return Collections.unmodifiableCollection(workshops.values());
    }

    public List<Registration> getAllRegistrations() {
        return Collections.unmodifiableList(registrations);
    }

    // --- Operasi Transaksi Pendaftaran (Registrable) ---

    @Override
    public void register(String participantId, String workshopId)
            throws CapacityFullException, DuplicateRegistrationException {

        // Validasi keberadaan data
        Participant participant = participants.get(participantId);
        if (participant == null) {
            throw new NoSuchElementException("Peserta dengan ID '" + participantId + "' tidak ditemukan.");
        }

        Workshop workshop = workshops.get(workshopId);
        if (workshop == null) {
            throw new NoSuchElementException("Workshop dengan ID '" + workshopId + "' tidak ditemukan.");
        }

        // Business Rule 3: Hanya peserta berstatus aktif yang boleh didaftarkan
        if (!participant.isActive()) {
            throw new IllegalStateException("Pendaftaran ditolak: Peserta " + participant.getName() + " berstatus NON-AKTIF.");
        }

        // Business Rule 2: Satu peserta tidak boleh mendaftar dua kali pada workshop yang sama
        boolean alreadyRegistered = registrations.stream()
                .anyMatch(r -> r.getParticipantId().equalsIgnoreCase(participantId)
                        && r.getWorkshopId().equalsIgnoreCase(workshopId));
        if (alreadyRegistered) {
            throw new DuplicateRegistrationException("Peserta " + participantId + " sudah terdaftar di workshop " + workshopId + ".");
        }

        // Business Rule 1: Kapasitas workshop bersifat maksimum
        if (workshop.isFull()) {
            throw new CapacityFullException("Kapasitas workshop " + workshopId + " penuh. Pendaftaran ditolak.");
        }

        // Eksekusi penambahan peserta & pencatatan registrasi
        workshop.increaseRegistered();
        registrations.add(new Registration(participantId, workshopId));
    }

    @Override
    public boolean cancel(String participantId, String workshopId) {
        Optional<Registration> match = registrations.stream()
                .filter(r -> r.getParticipantId().equalsIgnoreCase(participantId)
                        && r.getWorkshopId().equalsIgnoreCase(workshopId))
                .findFirst();

        if (match.isPresent()) {
            registrations.remove(match.get());
            Workshop w = workshops.get(workshopId);
            if (w != null) {
                w.decreaseRegistered();
            }
            return true;
        }
        return false;
    }

    // --- Operasi Pengolahan Koleksi dengan Stream API / Lambda ---

    /**
     * Fitur Stream/Lambda 1:
     * Menyaring workshop yang masih memiliki sisa kursi (belum penuh),
     * kemudian mengurutkannya berdasarkan tanggal pelaksanaan terdekat.
     */
    public List<Workshop> listAvailableWorkshopsSortedByDate() {
        return workshops.values().stream()
                .filter(w -> !w.isFull())
                .sorted(Comparator.comparing(Workshop::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Fitur Stream/Lambda 2:
     * Merekapitulasi jumlah pendaftar per workshop menggunakan groupingBy dan counting.
     * Mengembalikan Map berisi key ID Workshop dan value total pendaftar.
     */
    public Map<String, Long> summaryRegistrationCountPerWorkshop() {
        return registrations.stream()
                .collect(Collectors.groupingBy(
                        Registration::getWorkshopId,
                        Collectors.counting()
                ));
    }

    // --- Bridge & Sinkronisasi Java -> Prolog Knowledge Base ---

    /**
     * Stage 5: Mengekspor representasi memori Java ke format file fakta SWI-Prolog (.pl)
     * Aturan konversi:
     * - ID Java di-convert ke huruf kecil (p001, w01)
     * - String skill di-convert ke format atom snake_case (misal: "basic_java")
     */
    public void exportToPrologKnowledgeBase(File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("% =========================================================\n");
            writer.write("% KNOWLEDGE BASE WORKSHOP REGISTRATION & SUGGESTER\n");
            writer.write("% Auto-generated from Java RegistrationService runtime state\n");
            writer.write("% =========================================================\n\n");

            writer.write("% --- Fakta Status Peserta (active/1) ---\n");
            for (Participant p : participants.values()) {
                if (p.isActive()) {
                    writer.write(String.format("active(%s).\n", p.getId().toLowerCase()));
                }
            }

            writer.write("\n% --- Fakta Skill Peserta (completed/2) ---\n");
            for (Participant p : participants.values()) {
                String pAtom = p.getId().toLowerCase();
                for (String skill : p.getCompletedSkills()) {
                    String skillAtom = skill.toLowerCase().replace(' ', '_');
                    writer.write(String.format("completed(%s, %s).\n", pAtom, skillAtom));
                }
            }

            writer.write("\n% --- Fakta Minat Peserta (interest/2) ---\n");
            for (Participant p : participants.values()) {
                if (p.getInterest() != null && !p.getInterest().isEmpty()) {
                    String pAtom = p.getId().toLowerCase();
                    String interestAtom = p.getInterest().toLowerCase().replace(' ', '_');
                    writer.write(String.format("interest(%s, %s).\n", pAtom, interestAtom));
                }
            }

            writer.write("\n% --- Fakta Topik Workshop (topic/2) ---\n");
            for (Workshop w : workshops.values()) {
                String wAtom = w.getId().toLowerCase();
                String topicAtom = w.getTopic().toLowerCase().replace(' ', '_');
                writer.write(String.format("topic(%s, %s).\n", wAtom, topicAtom));
            }

            writer.write("\n% --- Fakta Prasyarat Workshop (requires/2) ---\n");
            for (Workshop w : workshops.values()) {
                if (w.getPrerequisiteSkill() != null && !w.getPrerequisiteSkill().isEmpty()) {
                    String wAtom = w.getId().toLowerCase();
                    String reqAtom = w.getPrerequisiteSkill().toLowerCase().replace(' ', '_');
                    writer.write(String.format("requires(%s, %s).\n", wAtom, reqAtom));
                }
            }

            writer.write("\n% --- Fakta Ketersediaan Kursi (seats_available/1) ---\n");
            for (Workshop w : workshops.values()) {
                if (!w.isFull()) {
                    String wAtom = w.getId().toLowerCase();
                    writer.write(String.format("seats_available(%s).\n", wAtom));
                }
            }
        }
    }
}
