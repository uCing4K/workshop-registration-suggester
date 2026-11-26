package model;

import java.time.LocalDateTime;

/**
 * Class Registration merepresentasikan relasi pendaftaran satu peserta ke satu workshop.
 */
public class Registration {
    private String participantId;
    private String workshopId;
    private LocalDateTime registrationTime;

    public Registration(String participantId, String workshopId) {
        this.participantId = participantId;
        this.workshopId = workshopId;
        this.registrationTime = LocalDateTime.now();
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    @Override
    public String toString() {
        return String.format("Pendaftaran -> Peserta: %s | Workshop: %s | Waktu: %s",
                participantId, workshopId, registrationTime);
    }
}
