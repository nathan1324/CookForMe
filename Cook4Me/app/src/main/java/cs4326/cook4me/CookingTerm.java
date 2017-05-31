package cs4326.cook4me;

/**
 * This class serves to take in data from Firebase in its native format
 */

public class CookingTerm {
    String name;
    String definition;

    public CookingTerm() {
        //empty
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }
}
