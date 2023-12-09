package graphs;

public enum HamiltonianGraphType {
    HAMILTONIAN("Hamiltonian Graph"),
    SEMIHAMILTONIAN("Semihamiltonian Graph"),
    NON_HAMILTONIAN("Non-Hamiltonian Graph");

    private final String description;

    HamiltonianGraphType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
