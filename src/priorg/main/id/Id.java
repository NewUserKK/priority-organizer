package priorg.main.id;

public abstract class Id {

    private int id;

    public Id(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (getClass().equals(o.getClass())) {
            return (id == ((Id) o).id);
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
