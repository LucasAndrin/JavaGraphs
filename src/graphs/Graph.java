package graphs;

import java.util.*;

public class Graph {
    private int size;
    private List<List<Edge>> vertices;
    private boolean isTargeted = false;

    private Graph(int size) {
        setSize(size);
    }

    private Graph(int size, boolean isTargeted) {
        this.isTargeted = isTargeted;
        setSize(size);
    }

    private void setSize(int size) {
        this.size = size;
        vertices = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            vertices.add(new ArrayList<>(size - 1));
        }
    }

    public static Graph create(int size) {
        return new Graph(size);
    }

    public static Graph create(int size, boolean isTargeted) {
        return new Graph(size, isTargeted);
    }

    public void addEdge(int origin, int destiny) {
        addEdge(origin, destiny, 1);
    }

    public void addEdge(int origin, int destiny, int weight) {
        getEdges(origin).add(new Edge(origin, destiny, weight));

        if (!isTargeted) {
            getEdges(destiny).add(new Edge(destiny, origin, weight));
        }
    }

    public Edge getEdge(int origin, int destiny) {
        ListIterator<Edge> edgesIterator = getEdges(origin).listIterator();
        while (edgesIterator.hasNext()) {
            if (edgesIterator.next().destiny == destiny) {
                return edgesIterator.previous();
            }
        }

        return null;
    }

    public void removeEdge(int origin, int destiny) {
        removeOneEdge(origin, destiny);

        if (!isTargeted) {
            removeOneEdge(destiny, origin);
        }
    }

    private void removeOneEdge(int origin, int destiny) {
        ListIterator<Edge> originEdges = getEdges(origin).listIterator();
        while (originEdges.hasNext()) {
            if (originEdges.next().destiny == destiny) {
                originEdges.remove();
                break;
            }
        }
    }

    /**
     * a) Um método para remover um vértice.
     * Removes the vertex and all edges associtate with it
     *
     * @param origin int
     */
    public void removeVertex(int origin) {
        List<Edge> ownEdges = getEdges(origin);
        for (Edge ownEdge : ownEdges) {
            List<Edge> outerEdges = getEdges(ownEdge.destiny);
            outerEdges.removeIf(edge -> edge.destiny == origin);
        }

        System.out.println();
        show();

        vertices.remove(origin);
        size--;

        for (List<Edge> edges : vertices) {
            for (Edge edge : edges) {
                if (origin < edge.destiny) {
                    edge.destiny--;
                }
            }
        }
    }

    /**
     * b) Verificar se um grafo é conexo. Retorna true se for conexo.
     * Checks if the graph is connected
     * @return boolean
     */
    public boolean isConnected() {
        for (int origin = 0; origin < size; origin++) {
            for (int destiny = origin + 1; destiny < size; destiny++) {
                if (!verticesAreConnected(origin, destiny, new boolean[size])) {
                    return false;
                }
            }

            for (int destiny = origin - 1; destiny > 0; destiny--) {
                if (!verticesAreConnected(origin, destiny, new boolean[size])) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean verticesAreConnected(int origin, int destiny, boolean[] visiteds) {
        if (origin == destiny) {
            return true;
        }

        visiteds[origin] = true;
        for (Edge edge : getEdges(origin)) {
            if (!visiteds[edge.destiny] && verticesAreConnected(edge.destiny, destiny, visiteds)) {
                return true;
            }
        }
        return false;
    }

    /**
     * c) Verificar se um grafo é completo. Retorna true se for completo.
     * Checks if the graph is complete
     * @return boolean
     */
    public boolean isComplete() {
        for (int origin = 0; origin < size; origin++) {
            if (getEdges(origin).size() != size - 1) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> dijkstra(int origin, int destiny) {
        int[] distances = new int[size];
        int[] previousVertices = new int[size];

        for (int i = 0; i < size; i++) {
            distances[i] = Integer.MAX_VALUE;
            previousVertices[i] = -1;
        }

        distances[origin] = 0;

        PriorityQueue<VertexDistancePair> priorityQueue = new PriorityQueue<>();
        priorityQueue.offer(new VertexDistancePair(origin, 0));

        while (!priorityQueue.isEmpty()) {
            VertexDistancePair pair = priorityQueue.poll();

            if (pair.distance >= distances[pair.vertex]) {
                for (Edge edge : getEdges(pair.vertex)) {
                    int distance = pair.distance + edge.weight;

                    if (distance < distances[edge.destiny]) {
                        distances[edge.destiny] = distance;
                        previousVertices[edge.destiny] = pair.vertex;

                        priorityQueue.offer(new VertexDistancePair(edge.destiny, distance));
                    }
                }
            }
        }
        return buildPath(previousVertices, destiny);
    }

    private List<Integer> buildPath (int[] previousVertices, int destiny) {
        List<Integer> path = new ArrayList<>();
        for (int vertex = destiny; vertex != - 1; vertex = previousVertices[vertex]) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public int getSize() {
        return size;
    }

    private record VertexDistancePair(int vertex, int distance) implements Comparable<VertexDistancePair> {
        @Override
        public int compareTo(VertexDistancePair other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    /**
     * e) Verificar se um grafo é euleriano, semieuleriano ou não euleriano.
     * Get Eulerian Graph Type
     * @return EulerianGraphType
     */
    public EulerianGraphType getEulerianType() {
        if (isConnected()) {
            int oddDegreeCount = countOddDegreeVertices();

            if (oddDegreeCount == 0) { // Circuit
                return EulerianGraphType.EULERIAN;
            } else if (oddDegreeCount == 2) { // Trail
                return EulerianGraphType.SEMIEULERIAN;
            }
        }

        return EulerianGraphType.NON_EULERIAN;
    }

    private int countOddDegreeVertices() {
        int oddDegreeVertices = 0;
        for (int origin = 0; origin < size; origin++) {
            if (getEdges(origin).size() % 2 != 0) {
                oddDegreeVertices++;
            }
        }
        return oddDegreeVertices;
    }

    public boolean isHamiltonian() {
        for (int origin = 0; origin < size; origin++) {
            List<Integer> path = new ArrayList<>();
            if (deepFirstSearchHamiltonianPath(origin, origin, new boolean[size], path, 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean deepFirstSearchHamiltonianPath(int startVertex, int currentVertex, boolean[] visiteds, List<Integer> path, int count) {
        visiteds[currentVertex] = true;
        path.add(currentVertex);

        if (count == size) {
            // Check if the last vertex has an edge to the starting vertex
            for (Edge edge : getEdges(currentVertex)) {
                if (edge.destiny == startVertex) {
                    return true; // Hamiltonian cycle found
                }
            }
        }

        for (Edge edge : getEdges(currentVertex)) {
            int nextVertex = edge.destiny;
            if (!visiteds[nextVertex]) {
                if (deepFirstSearchHamiltonianPath(startVertex, nextVertex, visiteds, path, count + 1)) {
                    return true;
                }
            }
        }

        // Backtrack
        visiteds[currentVertex] = false;
        path.remove(path.size() - 1);

        return false;
    }


    public HamiltonianGraphType getHamiltonianType() {
        if (isHamiltonian()) {
            return HamiltonianGraphType.HAMILTONIAN;
        }

        int verticesWithTwoNeighbors = countVerticesWithTwoNeighbors();
        int verticesWithOneNeighbor = countVerticesWithOneNeighbor();

        if (verticesWithTwoNeighbors == 2 && verticesWithOneNeighbor == size - 2) {
            return HamiltonianGraphType.SEMIHAMILTONIAN;
        }

        return HamiltonianGraphType.NON_HAMILTONIAN;
    }

    private int countVerticesWithTwoNeighbors() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (getEdges(i).size() == 2) {
                count++;
            }
        }
        return count;
    }

    private int countVerticesWithOneNeighbor() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (getEdges(i).size() == 1) {
                count++;
            }
        }
        return count;
    }

    public void show() {
        for (int i = 0; i < size; i++) {
            System.out.print("Vertex: " + i + ':');
            for (Edge edge : getEdges(i)) {
                System.out.print(" { destiny: " + edge.destiny + ", weight: " + edge.weight + " }");
            }
            System.out.println();
        }
    }

    private void depthFirstSearch(int origin, boolean[] visiteds) {
        visiteds[origin] = true;
        for (Edge edge : getEdges(origin)) {
            if (!visiteds[edge.destiny]) {
                depthFirstSearch(edge.destiny, visiteds);
            }
        }
    }

    List<Edge> getEdges(int origin) {
        return vertices.get(origin);
    }
}
