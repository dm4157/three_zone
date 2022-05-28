package party.msdg.three_zone.zone.search;

import party.msdg.three_zone.model.Address;

/**
 * Wow! Sweet moon.
 */
public interface Searcher {
    boolean support(String zone);
    
    String path();
    String zone();

    String search(Address address);
}
