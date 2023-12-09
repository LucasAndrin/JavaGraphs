package graphs;

public class Edge {
    public int origin, destiny, weight;
    public Edge(int origin, int destiny, int weight) {
        this.origin = origin;
        this.destiny = destiny;
        this.weight = weight;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Edge{");
        sb.append("origin=").append(origin);
        sb.append(", destiny=").append(destiny);
        sb.append(", weight=").append(weight);
        sb.append('}');
        return sb.toString();
    }
}
