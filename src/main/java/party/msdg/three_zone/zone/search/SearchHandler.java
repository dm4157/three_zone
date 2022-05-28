package party.msdg.three_zone.zone.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Wow! Sweet moon.
 */
public class SearchHandler {
    
    private List<Searcher> searchers;
    
    public SearchHandler() {
        searchers = new ArrayList<>();
        searchers.add(new JinganSearcher());
    }
    
    public Searcher find(String zone) {
        Searcher se = null;
        for (Searcher one : searchers) {
            if (one.support(zone)) {
                se = one;
                break;
            }
        }
        
        return se;
    }
}
