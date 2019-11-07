package fr.romain.assignment3.shared;

public enum ClassRoomId {
    W201(60),
    W202(60),
    W101(20),
    W102(30);

    public int capacity;
    private ClassRoomId(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return this.name() + " (capacity: " + capacity + ")";
    }
}
