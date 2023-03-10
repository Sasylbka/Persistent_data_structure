
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import PersistentDataStructure.PersistentLinkedList;
import org.junit.Before;
import org.junit.Test;

public class PersistentLinkedListTest {
    private PersistentLinkedList<String> empty;
    private PersistentLinkedList<String> a;
    private PersistentLinkedList<String> ba;
    private PersistentLinkedList<String> bac;
    private PersistentLinkedList<String> dbac;
    private PersistentLinkedList<String> dbeac;
    private PersistentLinkedList<String> deac;
    private PersistentLinkedList<String> deacf;
    private PersistentLinkedList<String> deac2;
    private PersistentLinkedList<String> eac;
    private PersistentLinkedList<String> ac;
    private PersistentLinkedList<String> gac;

    @Before
    public void setUp() throws Exception {
        empty = new PersistentLinkedList<String>(1);
        a = empty.addFirst("a"); // (a,_)
        ba = a.addFirst("b"); // (a,b)
        bac = ba.addLast("c"); // ((a,b),(c,_))
        dbac = bac.addFirst("d"); // ((a,b),(c,d))
        dbeac = dbac.add(2, "e"); // (((a,b),(c,d)), ((e,_),_))
        deac = dbeac.remove(1); // (((a,_),(c,d)), ((e,_),_))
        deacf = deac.addLast("f"); // (((a,f),(c,d)), ((e,_),_))
        deac2 = deacf.removeLast(); // (((a,_),(c,d)), ((e,_),_))
        eac = deac2.removeFirst(); // (((a,_),(c,_)), ((e,_),_))
        ac = eac.removeFirst(); // ((a,_),(c,_))
        gac = ac.addFirst("g"); // ((a,g),(c,_))
    }

    @Test
    public void getFirst() {
        assertEquals("a", a.getFirst());
        assertEquals("b", ba.getFirst());
        assertEquals("b", bac.getFirst());
        assertEquals("d", dbeac.getFirst());
    }

    @Test
    public void getLast() {
        assertEquals("a", a.getLast());
        assertEquals("a", ba.getLast());
        assertEquals("c", bac.getLast());
        assertEquals("c", dbeac.getLast());
    }

    @Test
    public void get() {
        assertEquals("d", dbeac.get(0));
        assertEquals("b", dbeac.get(1));
        assertEquals("e", dbeac.get(2));
        assertEquals("c", dbeac.get(4));
    }

    @Test
    public void add() {
        assertEquals("hmm", dbeac.add(3, "hmm").get(3));
        assertEquals("a", dbeac.get(3));
    }

    @Test
    public void addFirst() {
        assertEquals("hmm", dbeac.addFirst("hmm").get(0));
        assertEquals("d", dbeac.addFirst("hmm").get(1));
        assertEquals("d", dbeac.get(0));
    }

    @Test
    public void addLast() {
        assertEquals("hmm", dbeac.addLast("hmm").getLast());
        assertEquals("c", dbeac.getLast());
    }

    @Test
    public void remove() {
        assertEquals("b", dbeac.get(1));
        assertEquals("e", deac.get(1));
    }

    @Test
    public void removeFirst() {
        assertEquals("e", eac.getFirst());
    }

    @Test
    public void removeLast() {
        assertEquals("d", deac2.getFirst());
        assertEquals("c", deac2.getLast());
    }

    @Test
    public void emptyList() {
        PersistentLinkedList<Integer> v1 = new PersistentLinkedList<>(1);
        PersistentLinkedList<Integer> v2 = v1.addFirst(1);
        PersistentLinkedList<Integer> v3 = v2.removeLast();
        PersistentLinkedList<Integer> v4 = v3.addLast(3);
        PersistentLinkedList<Integer> v5 = v4.removeFirst();
        assertEquals("[]", v5.toString());
        PersistentLinkedList<Integer> v6 = v5.addLast(4);
        assertEquals("[4]", v6.toString());
    }

    @Test
    public void testToString() {
        assertEquals("[a]", a.toString());
        assertEquals("[g, a, c]", gac.toString());
    }

    @Test
    public void innerRepresentation() {
        assertEquals("(((a, _), (c, _)), ((e, _), _))", eac.innerRepresentation());
        assertEquals("((a, _), (c, _))", ac.innerRepresentation());
    }

    @Test
    public void size() {
        assertEquals(0, empty.size());
        assertEquals(0, a.removeFirst().size());
        assertEquals(0, a.removeLast().size());
        assertEquals(0, a.remove(0).size());
        assertEquals(5, deacf.size());
        assertEquals(4, deac2.size());
        assertEquals(3, gac.size());
    }
    @Test
    public void redoTest() {
        PersistentLinkedList<String> v0 = new PersistentLinkedList<>(2);
        PersistentLinkedList<String> v1 = v0.addLast("a");
        PersistentLinkedList<String> v2 = v1.addLast("b");
        PersistentLinkedList<String> v3 = v1.addLast("c");

        PersistentLinkedList<String> v2Restored = v2.undo().redo();
        PersistentLinkedList<String> v3Restored = v3.undo().redo();

        assertEquals("[a]", v2.undo().toString());
        assertEquals("[a]", v3.undo().toString());
        assertEquals("[]", v2.undo().undo().toString());

        assertEquals("[a, b]", v2Restored.toString());
        assertEquals("[a, c]", v3Restored.toString());

        v3Restored = v3.undo().redo();
        v2Restored = v2.undo().redo();
        assertEquals("[a, b]", v2Restored.toString());
        assertEquals("[a, c]", v3Restored.toString());

    }
    @Test
    public void undoTest() {
        PersistentLinkedList<String> init = new PersistentLinkedList<>(2);
        PersistentLinkedList<String> a = init.addFirst("y");
        PersistentLinkedList<String> b = a.addLast("z");
        PersistentLinkedList<String> c = b.addFirst("x");
        PersistentLinkedList<String> d = c.removeLast();

        PersistentLinkedList<String> cc = d.undo();
        PersistentLinkedList<String> bb = d.undo().undo();
        PersistentLinkedList<String> aa = d.undo().undo().undo();
        assertEquals("[y]", aa.toString());
        assertEquals("[y, z]", bb.toString());
        assertEquals("[x, y, z]", cc.toString());
        assertEquals("[x, y]", d.toString());
    }
/*  @Test
    public void toPersistentArray() {
        assertEquals("(((a, _), (c, _)), ((e, wow), _))", eac.toPersistentArray().add("wow").toString());
    }*/

    @Test
    public void testNestedStructures() {
        PersistentLinkedList<Integer> inside = new PersistentLinkedList<Integer>(1).addLast(100);
        PersistentLinkedList<PersistentLinkedList<Integer>> outer1 = new PersistentLinkedList<PersistentLinkedList<Integer>>(
                2).addFirst(inside);
        PersistentLinkedList<Integer> inner1 = new PersistentLinkedList<Integer>(2).addFirst(90);
        inner1 = inner1.addFirst(70);
        PersistentLinkedList<PersistentLinkedList<Integer>> outer2 = outer1.addLast(inner1);
        PersistentLinkedList<Integer> inner2 = inner1.addFirst(80);
        assertEquals("[[100]]", outer1.toString());
        assertEquals("[70, 90]", inner1.toString());
        assertEquals("[[100], [70, 90]]", outer2.toString());
        assertEquals("[80, 70, 90]", inner2.toString());
    }

    @Test
    public void iterator() {
        PersistentLinkedList<Integer> a = new PersistentLinkedList<>(2);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int newValue = i * i * 3;
            result.add(newValue);
            a = a.addLast(newValue);
        }

        ListIterator<Integer> it = a.iterator();
        for (int i = 0; i < 100; i++) {
            assertTrue(it.hasNext());
            assertEquals(result.get(i), it.next());
        }
        assertFalse(it.hasNext());
        for (int i = 99; i >= 0; i--) {
            assertTrue(it.hasPrevious());
            assertEquals(result.get(i), it.previous());
        }
        assertFalse(it.hasPrevious());
    }



}
