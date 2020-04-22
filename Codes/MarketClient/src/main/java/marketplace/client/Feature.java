package marketplace.client;

public enum Feature {
    YEAR("Évjárat"),
    COLOR("Szín");

    private Feature(String s) {
        description = s;
    }

    private String description;
    @Override
    public String toString() {
        return description;
    }
}
