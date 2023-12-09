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
    void testIsHamiltonian() {
        assertTrue(graph.isHamiltonian());
    }

    @Test
    void testGetEulerianType() {
        assertEquals(EulerianGraphType.EULERIAN, graph.getEulerianType());
    }

    @Test
    void testGetHamiltonianType() {
        assertEquals(HamiltonianGraphType.HAMILTONIAN, graph.getHamiltonianType());
    }
}