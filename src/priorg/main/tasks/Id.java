package priorg.main.tasks;

public class Id {

    private int id;

    public Id(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Id) {
//            return (id == ((Id) obj).id && object == ((Id) obj).object);
            return (id == ((Id) obj).id);
        }

        return false;
    }
}
