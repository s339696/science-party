package models.enums;

/**
 * Defines all available Perks.
 */
public enum PerkType {
    NO_PERK("Keine Fähigkeit"),
    GIVE_ANSWER("Frage automatisch richtig beantworten"),;

    private final String description;

    private PerkType(final String description) {
        this.description = description;
    }

    public static PerkType fromInt(int x) {
        switch(x) {
            case 0:
                return PerkType.NO_PERK;
            case 1:
                return PerkType.GIVE_ANSWER;
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }
}
