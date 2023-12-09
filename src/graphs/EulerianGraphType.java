package graphs;

public enum EulerianGraphType {
    EULERIAN("Eulerian Graph"),
    SEMIEULERIAN("Semieulerian Graph"),
    NON_EULERIAN("Non-Eulerian Graph");

    private final String description;

    EulerianGraphType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
