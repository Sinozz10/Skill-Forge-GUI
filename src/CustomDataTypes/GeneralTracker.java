package CustomDataTypes;

public class GeneralTracker {
    private final String ID;
    private boolean state;

    public GeneralTracker(String ID) {
        this.ID = ID;
        this.state = false;
    }

    public GeneralTracker(String ID, boolean state) {
        this.ID = ID;
        this.state = state;
    }

    public String getID() {
        return ID;
    }

    public boolean isTrue() {
        return state;
    }

    public void toggle() {
        state = !state;
    }
}
