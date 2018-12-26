package priorg.main.id;

/** Class representing ID of an object. */
public abstract class Id {

    private int id;

    public Id(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (getClass().equals(obj.getClass())) {
            return (id == ((Id) obj).id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return ((getClass().getName().hashCode() + id) * 87198767) % 28934797;
    }

    @Override
    public String toString() {
        return getClass().getName() + "_" + id;
    }
}
