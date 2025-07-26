package stellarburgers.enums;

public enum Ingredient {

    BUN("61c0c5a71d1f82001bdaaa6c"),
    SAUCE("61c0c5a71d1f82001bdaaa75"),
    MAIN("61c0c5a71d1f82001bdaaa76"),
    INVALID("invalid_id_567");

    private final String id;

    Ingredient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
