package game.objects;

/**
 * Implemented by anything that can take damage (currently just the Player,
 * but designed so NPCs or destructible scenery could implement it later
 * without changing the Hazard code that calls it).
 */
public interface Damageable {

    /** Apply damage / a life loss, with a short reason for player feedback. */
    void takeDamage(int amount, String reason);

    boolean isAlive();
}
