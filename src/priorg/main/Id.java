package priorg.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Id<T>/* extends Comparable> implements Comparable<T>*/ {

    private int id;
    private Class<T> obj;

    public Id(int id, Class<T> objClass) {
        this.id = id;
        this.obj = objClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Id) {
            return (id == ((Id) o).id && obj == ((Id) o).obj);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    //    @Override
//    public int compareTo(T o) {
//        Method compareMethod = null;
//        try {
//            compareMethod = obj.getMethod("compareTo", o.getClass().getField("obj").getClass());
//        } catch (NoSuchMethodException | NoSuchFieldException e) {
//            System.err.println(e.getMessage());
//        }
//
//        try {
//            if (compareMethod != null) {
//                return (int) compareMethod.invoke(obj, o.getClass().getField("obj").getClass());
//            }
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
//            System.err.println(e.getMessage());
//        }
//
//        throw new RuntimeException("Error while comparing objects by id");
//    }
}
