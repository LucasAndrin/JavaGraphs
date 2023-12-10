package graphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = Graph.create(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 0);
        graph.show();
        System.out.println();
    }

    @Test
    void testAddEdge() {
        graph.addEdge(0, 2);
        assertTrue(graph.getEdges(0).stream().anyMatch(edge -> edge.destiny == 2));
        assertTrue(graph.getEdges(2).stream().anyMatch(edge -> edge.destiny == 0));
    }

    @Test
    void testRemoveEdge() {
        graph.removeEdge(0, 1);
        assertNull(graph.getEdge(0, 1));
        assertNull(graph.getEdge(1, 0));
    }

    @Test
    void testRemoveVertex() {
        graph.removeVertex(0);
        assertEquals(4, graph.getSize());
        assertNull(graph.getEdge(0, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.getEdge(4, 0));
        graph.show();
    }

    @Test
    void testIsConnected() {
        assertTrue(graph.isConnected());
    }

    @Test
    void testIsComplete() {
        assertFalse(graph.isComplete());

        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 4);
        graph.show();
        assertTrue(graph.isComplete());
    }

    @Test
    void testDijkstra() {
        List<Integer> path = graph.dijkstra(0, 0);
        int[] expected = {0};
        assertTrue(assertListEqualsArray(expected, path));

        path = graph.dijkstra(0, 1);
        expected = new int[]{0, 1};
        assertTrue(assertListEqualsArray(expected, path));

        graph.addEdge(1, 4, 100);
        path = graph.dijkstra(1, 4);
        expected = new int[]{1, 0, 4};
        assertTrue(assertListEqualsArray(expected, path));
    }

    private boolean assertListEqualsArray(int[] expected, List<Integer> path) {
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i) != expected[i]) {
                return false;
            }
        }
        return true;
    }

    @Test
    void testGetEulerianType() {
        assertEquals(EulerianGraphType.EULERIAN, graph.getEulerianType());

        graph.addEdge(0,2);
        assertEquals(EulerianGraphType.SEMIEULERIAN, graph.getEulerianType());
    }

    @Test
    void testIsHamiltonian() {
        assertTrue(graph.isHamiltonian());
    }

    @Test
    void testGetHamiltonianType() {
        assertEquals(HamiltonianGraphType.HAMILTONIAN, graph.getHamiltonianType());

        graph = Graph.create(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        assertEquals(HamiltonianGraphType.SEMIHAMILTONIAN, graph.getHamiltonianType());


        graph = Graph.create(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        assertEquals(HamiltonianGraphType.NON_HAMILTONIAN, graph.getHamiltonianType());
    }

    @Test
    void exerciceA() {
        graph = Graph.create(5, true);

        graph.addEdge(0, 1);
        graph.addEdge(0, 4, 10);
        graph.addEdge(0, 3, 3);
        graph.addEdge(1, 2, 5);
        graph.addEdge(2, 4);
        graph.addEdge(3, 2, 2);
        graph.addEdge(3, 4, 6);

        for (int i = 0; i < 5; i++) {
            System.out.println(graph.dijkstra(0, i));
        }
    }

    @Test
    void exerciceB() {
        graph = Graph.create(6, true);

        graph.addEdge(0, 1, 15);
        graph.addEdge(0, 2, 9);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 1, 4);
        graph.addEdge(2, 3, 3);
        graph.addEdge(2, 4, 16);
        graph.addEdge(3, 5, 21);
        graph.addEdge(3, 4, 6);
        graph.addEdge(4, 5, 7);

        for (int i = 0; i < 6; i++) {
            List<Integer> paths = graph.dijkstra(0, i);
            paths.replaceAll(integer -> integer + 1);
            System.out.println(paths);
        }
    }

    @Test
    void exerciceC() {
        graph = Graph.create(18);
        // 1
        graph.addEdge(0, 1, 260);
        graph.addEdge(0, 2, 170);
        // 2
        graph.addEdge(1, 2, 135);
        graph.addEdge(1, 4, 50);
        // 3
        graph.addEdge(2, 4, 80);
        // 4
        graph.addEdge(3, 4, 150);
        graph.addEdge(3, 5, 120);
        graph.addEdge(3, 6, 100);
        // 5
        graph.addEdge(4, 5, 70);
        graph.addEdge(4, 8, 130);
        // 6
        graph.addEdge(5, 6, 150);
        graph.addEdge(5, 7, 200);
        // 7
        graph.addEdge(6, 7, 80);
        // 8
        graph.addEdge(7, 9, 160);
        graph.addEdge(7, 10, 100);
        // 9
        graph.addEdge(8, 9, 70);
        // 10
        graph.addEdge(9, 10, 160);
        graph.addEdge(9, 11, 80);
        graph.addEdge(9, 12, 80);
        // 11
        graph.addEdge(10, 11, 80);
        graph.addEdge(10, 14, 150);
        graph.addEdge(10, 17, 200);
        // 12
        graph.addEdge(11, 14, 110);
        graph.addEdge(11, 12, 100);
        // 13
        graph.addEdge(12, 13, 70);
        // 14
        graph.addEdge(13, 14, 120);
        graph.addEdge(13, 15, 50);
        graph.addEdge(13, 16, 80);
        // 15
        graph.addEdge(14, 15, 100);
        graph.addEdge(14, 17, 140);
        // 16
        graph.addEdge(15, 16, 50);

        System.out.println(graph.dijkstra(4, 15));
    }
}