package CustomDataTypes;

import com.google.gson.annotations.Expose;

public class GeneralTracker {
    @Expose
    private final String ID;
    @Expose
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

    public void setState(boolean state) {
        this.state = state;
    }
}
