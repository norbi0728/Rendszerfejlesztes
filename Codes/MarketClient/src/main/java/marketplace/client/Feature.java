package marketplace.client;

public enum Feature {
    YEAR("�vj�rat"),
    COLOR("Sz�n");

    private Feature(String s) {
        description = s;
    }

    private String description;
    @Override
    public String toString() {
        return description;
    }
}
