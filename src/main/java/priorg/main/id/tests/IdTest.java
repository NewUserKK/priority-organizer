package priorg.main.id.tests;

import priorg.main.id.CategoryId;
import priorg.main.id.TaskId;

public class IdTest {

    public static void main(String[] args) {
        testEqualsHashcode();
        testToString();
    }

    private static void testEqualsHashcode() {
        System.out.println(new CategoryId(1).equals(new TaskId(0)));
        System.out.println(new CategoryId(1).equals(new TaskId(1)));
        System.out.println(new CategoryId(1).equals(new CategoryId(0)));
        System.out.println(new CategoryId(1).equals(new CategoryId(1)));

        System.out.println(new CategoryId(1).hashCode());
        System.out.println(new CategoryId(0).hashCode());
        System.out.println(new TaskId(0).hashCode());
        System.out.println(new TaskId(1).hashCode());
    }

    private static void testToString() {
        System.out.println(new CategoryId(1));
        System.out.println(new TaskId(1));
    }

}
