package ntukhpi.ddy.semit_diplomnic.enums.status;


public enum status {
    done("зроблено"),
    checking("перевіряється"),
    rejected("відхілено");

    private final String displayName;

    status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static status getByStatus(String nameVR) {
        int index = -1;
        status[] vrValues = values();
        boolean flFound = false;
        while (!flFound && index<vrValues.length-1) {
            index++;
            if (vrValues[index].getDisplayName().equals(nameVR)) {
                flFound = true;
            }
        }
        status vr = null;
        if (!flFound) {
            vr = status.values()[0];
        } else {
            vr = status.values()[index];
        }
        return vr;
    }

    public static status getStatusById(int index) {
        if (index >= status.values().length) {
            return status.values()[0];
        } else {
            return status.values()[index];
        }
    }

    public static String[]  getStatusTypes() {
        status[] vr = values();
        String[] vrNames = new String[vr.length];
        for (int i = 0; i < vr.length; i++) {
            vrNames[i] = vr[i].getDisplayName();
        }
        return vrNames;
    }
}
