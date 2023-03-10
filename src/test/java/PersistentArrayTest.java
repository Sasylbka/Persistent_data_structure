
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import PersistentDataStructure.PersistentArray;
import PersistentDataStructure.PersistentLinkedList;
import org.junit.Test;

public class PersistentArrayTest {

    @Test
    public void add() {
        PersistentArray<Integer> init = new PersistentArray<>(1);
        PersistentArray<Integer> a = init.add(0);
        System.out.println(a.toString());
        PersistentArray<Integer> b = a.add(1);
        PersistentArray<Integer> c = b.add(2);
        PersistentArray<Integer> d = c.add(3);
        PersistentArray<Integer> e = d.add(4);
        assertEquals("(0, _)", a.toString());
        assertEquals("(0, 1)", b.toString());
        assertEquals("((0, 1), (2, _))", c.toString());
        assertEquals("((0, 1), (2, 3))", d.toString());
        assertEquals("(((0, 1), (2, 3)), ((4, _), _))", e.toString());

        PersistentArray<Integer> initInit = new PersistentArray<>(2);
        PersistentArray<Integer> aa = initInit.add(0);
        PersistentArray<Integer> bb = aa.add(1);
        PersistentArray<Integer> cc = bb.add(2);
        PersistentArray<Integer> dd = cc.add(3);
        PersistentArray<Integer> ee = dd.add(4);
        assertEquals("(0, _, _, _)", aa.toString());
        assertEquals("(0, 1, _, _)", bb.toString());
        assertEquals("(0, 1, 2, _)", cc.toString());
        assertEquals("(0, 1, 2, 3)", dd.toString());
        assertEquals("((0, 1, 2, 3), (4, _, _, _), _, _)", ee.toString());
    }

    @Test
    public void set() {
        PersistentArray<Integer> a = new PersistentArray<Integer>(1).add(0).add(1).add(2).add(3);
        PersistentArray<Integer> b = a.set(1, 806);
        assertEquals("((0, 1), (2, 3))", a.toString());
        assertEquals("((0, 806), (2, 3))", b.toString());

        PersistentArray<Integer> aa = new PersistentArray<Integer>(2).add(0).add(1).add(2).add(3).add(4)
                .add(10);
        PersistentArray<Integer> bb = aa.set(5, 806);
        assertEquals("((0, 1, 2, 3), (4, 10, _, _), _, _)", aa.toString());
        assertEquals("((0, 1, 2, 3), (4, 806, _, _), _, _)", bb.toString());
    }

    @Test
    public void pop() {
        PersistentArray<Integer> cur1 = new PersistentArray<Integer>(1).add(0);
        PersistentArray<Integer> prev1 = cur1;
        PersistentArray<Integer> cur2 = new PersistentArray<Integer>(2).add(0);
        PersistentArray<Integer> prev2 = cur2;
        for (int i = 2; i <= 30; i++) {
            cur1 = prev1.add(i);
            cur2 = prev2.add(i);

            assertEquals(prev1.toString(), cur1.pop().toString());
            assertEquals(prev2.toString(), cur2.pop().toString());

            prev1 = cur1;
            prev2 = cur2;
        }
    }

    @Test
    public void get() {
        PersistentArray<Integer> a = new PersistentArray<>(2);
        for (int i = 0; i < 100; i++) {
            a = a.add(i * i);
        }
        long expected = 75 * 75;
        long actual = a.get(75);
        assertEquals(expected, actual);
    }

    @Test
    public void toPersistentLinkedList() {
        PersistentArray<Integer> a = new PersistentArray<>(1);
        PersistentArray<Integer> b = a.add(0).add(1).add(2).add(3).add(4);
        PersistentLinkedList<Integer> ll = b.toPersistentLinkedList();
        assertEquals("(((0, 1), (2, 3)), ((4, _), _))", ll.innerRepresentation());
        assertEquals("[0, 1, 2, 3, 4]", ll.toString());
    }

    @Test
    public void testNestedStructures() {
        PersistentArray<Integer> inside = new PersistentArray<Integer>(1).add(100);
        PersistentArray<PersistentArray<Integer>> outer1 = new PersistentArray<PersistentArray<Integer>>(
                2).add(inside);
        PersistentArray<Integer> inner1 = new PersistentArray<Integer>(2).add(90);
        PersistentArray<PersistentArray<Integer>> outer2 = outer1.add(inner1);
        PersistentArray<Integer> inner2 = inner1.add(80);
        assertEquals("((100, _), _, _, _)", outer1.toString());
        assertEquals("(90, _, _, _)", inner1.toString());
        assertEquals("((100, _), (90, _, _, _), _, _)", outer2.toString());
        assertEquals("(90, 80, _, _)", inner2.toString());
    }

    @Test
    public void size() {
        PersistentArray<Integer> a = new PersistentArray<>(1);
        assertEquals(0, a.size());
        PersistentArray<Integer> b = a.add(3);
        assertEquals(1, b.size());
        PersistentArray<Integer> c = b.add(5);
        assertEquals(2, c.size());
        PersistentArray<Integer> d = c.add(3);
        assertEquals(3, d.size());
        PersistentArray<Integer> e = d.pop();
        assertEquals(2, e.size());
        PersistentArray<Integer> f = e.pop();
        assertEquals(1, f.size());
        PersistentArray<Integer> g = f.pop();
        assertEquals(0, g.size());
    }

    @Test
    public void redoTest(){
        PersistentArray<String> v0 = new PersistentArray<>(2);
        PersistentArray<String> v1 = v0.add("a");
        PersistentArray<String> v2 = v1.add("b");
        PersistentArray<String> v3 = v1.add("c");

        PersistentArray<String> v1Canceled = v2.undo();
        PersistentArray<String> v2Restored = v1Canceled.redo();
        PersistentArray<String> v3Restored = v3.undo().redo();

        assertEquals("(a, _, _, _)", v2.undo().toString());
        assertEquals("(a, _, _, _)", v3.undo().toString());
        assertEquals("(_, _, _, _)", v2.undo().undo().toString());

        assertEquals("(a, b, _, _)", v2Restored.toString());
        assertEquals("(a, c, _, _)", v3Restored.toString());

        v3Restored = v3.undo().redo();
        v2Restored = v2.undo().redo();
        assertEquals("(a, b, _, _)", v2Restored.toString());
        assertEquals("(a, c, _, _)", v3Restored.toString());
    }
    @Test
    public void undoTest() {
        PersistentArray<String> init = new PersistentArray<>(2);
        PersistentArray<String> a = init.add("a");
        PersistentArray<String> b = a.add("b");
        PersistentArray<String> c = b.add("c");
        PersistentArray<String> bb = c.undo();
        PersistentArray<String> aa = c.undo().undo();
        assertEquals("(a, _, _, _)", aa.toString());
        assertEquals("(a, b, _, _)", bb.toString());
        assertEquals("(a, b, c, _)", c.toString());
    }
    @Test
    public void iterator() {
        PersistentArray<Integer> a = new PersistentArray<>(2);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int newValue = i * i * 3;
            result.add(newValue);
            a = a.add(newValue);
        }
        Iterator<Integer> it = a.iterator();
        for (int i = 0; i < 100; i++) {
            assertTrue(it.hasNext());
            assertEquals(result.get(i), it.next());
        }
        assertFalse(it.hasNext());
    }
}
