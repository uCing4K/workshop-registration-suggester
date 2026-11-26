package model;

import exception.CapacityFullException;
import java.time.LocalDate;

/**
 * Class Workshop memodelkan data workshop, kapasitas, dan jumlah peserta terdaftar.
 * Menerapkan konsep Encapsulation yang ketat pada atribut capacity dan registered.
 */
public class Workshop {
    private String id;
    private String title;
    private String topic;
    private int capacity;
    private int registered;
    private LocalDate date;
    private String prerequisiteSkill;

    public Workshop(String id, String title, String topic, int capacity, LocalDate date, String prerequisiteSkill) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Kapasitas workshop harus lebih besar dari 0");
        }
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.capacity = capacity;
        this.registered = 0;
        this.date = date;
        this.prerequisiteSkill = prerequisiteSkill;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRegistered() {
        return registered;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPrerequisiteSkill() {
        return prerequisiteSkill;
    }

    public void setPrerequisiteSkill(String prerequisiteSkill) {
        this.prerequisiteSkill = prerequisiteSkill;
    }

    public boolean isFull() {
        return registered >= capacity;
    }

    public int getAvailableSeats() {
        return capacity - registered;
    }

    /**
     * Mencegah perubahan langsung terhadap jumlah peserta.
     * Hanya dapat ditambah melalui increaseRegistered() dengan validasi batas kapasitas.
     */
    public synchronized void increaseRegistered() throws CapacityFullException {
        if (isFull()) {
            throw new CapacityFullException("Kapasitas workshop " + id + " (" + title + ") sudah penuh [" + capacity + "/" + capacity + "].");
        }
        this.registered++;
    }

    public synchronized void decreaseRegistered() {
        if (this.registered > 0) {
            this.registered--;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Topik: %s | Tanggal: %s | Kuota: %d/%d (Sisa: %d) | Prasyarat: %s",
                id, title, topic, date, registered, capacity, getAvailableSeats(),
                (prerequisiteSkill != null ? prerequisiteSkill : "Tidak ada"));
    }
}
