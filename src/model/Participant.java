package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Participant merepresentasikan peserta workshop.
 * Mengimplementasikan Inheritance dari Person dan Overriding getRole().
 */
public class Participant extends Person {
    private boolean active;
    private Set<String> completedSkills;
    private String interest;

    public Participant(String id, String name, boolean active, String interest) {
        super(id, name);
        this.active = active;
        this.interest = interest;
        this.completedSkills = new HashSet<>();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Set<String> getCompletedSkills() {
        return Collections.unmodifiableSet(completedSkills);
    }

    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty()) {
            this.completedSkills.add(skill.trim());
        }
    }

    @Override
    public String getRole() {
        return "Peserta Workshop";
    }

    @Override
    public String toString() {
        return String.format("%s | Status: %s | Minat: %s | Skills: %s",
                super.toString(),
                (active ? "Aktif" : "Non-Aktif"),
                interest,
                completedSkills);
    }
}
