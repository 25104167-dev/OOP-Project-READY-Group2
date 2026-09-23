package game.gameplay;

/** Shared Key instances so a door and the challenge that grants its key refer to the same object. */
public final class Keys {

    private Keys() {
    }

    public static final Key ENTRANCE_KEY = new Key("entrance_key", "Orientation Badge");
    public static final Key CLASSROOM_KEY = new Key("classroom_key", "Classroom Safety Key");
    public static final Key ENVIRONMENT_KEY = new Key("environment_key", "Environmental Access Card");
    public static final Key FIRST_AID_KEY = new Key("first_aid_key", "First Aid Clearance Badge");
    public static final Key EVACUATION_KEY = new Key("evacuation_key", "Evacuation Pass");
}
