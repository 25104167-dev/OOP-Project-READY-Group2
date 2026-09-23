package game.player;

import game.education.InformationCard;
import game.gameplay.Key;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds everything the player has collected: keys (for unlocking rooms) and
 * information cards (for the Knowledge Menu). Encapsulates the backing
 * collections behind read-only views.
 */
public class Inventory {

    private final Set<Key> keys = new LinkedHashSet<>();
    private final List<InformationCard> informationCards = new ArrayList<>();

    public void addKey(Key key) {
        keys.add(key);
    }

    public boolean hasKey(Key key) {
        return keys.contains(key);
    }

    public int getKeyCount() {
        return keys.size();
    }

    public void addInformationCard(InformationCard card) {
        informationCards.add(card);
    }

    public List<InformationCard> getInformationCards() {
        return List.copyOf(informationCards);
    }

    public int getCollectibleCount() {
        return informationCards.size();
    }
}
