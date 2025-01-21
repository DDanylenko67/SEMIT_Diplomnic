package ntukhpi.ddy.semit_diplomnic.enums.groupType;


public enum groupType {
    bachelor("Бакалавр"),
    master("Магістр");

    private final String displayName;

    groupType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static groupType getByGroupType(String nameVR) {
        int index = -1;
        groupType[] vrValues = values();
        boolean flFound = false;
        while (!flFound && index<vrValues.length-1) {
            index++;
            if (vrValues[index].getDisplayName().equals(nameVR)) {
                flFound = true;
            }
        }
        groupType vr = null;
        if (!flFound) {
            vr = groupType.values()[0];
        } else {
            vr = groupType.values()[index];
        }
        return vr;
    }

    public static groupType getGroupTypeById(int index) {
        if (index >= groupType.values().length) {
            return groupType.values()[0];
        } else {
            return groupType.values()[index];
        }
    }

    public static String[]  getGroupTypes() {
        groupType[] vr = values();
        String[] vrNames = new String[vr.length];
        for (int i = 0; i < vr.length; i++) {
            vrNames[i] = vr[i].getDisplayName();
        }
        return vrNames;
    }
}

