package model;

/**
 * Abstract class Person mewakili kerangka dasar entitas manusia dalam sistem.
 * Menerapkan konsep Abstraction dan Encapsulation.
 */
public abstract class Person {
    private String id;
    private String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method abstrak yang wajib di-override oleh setiap subclass untuk
     * mengembalikan peran spesifik dari objek Person terkait.
     * Menerapkan konsep Polymorphism.
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Nama: %s", getRole(), id, name);
    }
}
