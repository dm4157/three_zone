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
        searchers.add(new JiadingSearcher());
        searchers.add(new XuhuiSearcher());
        searchers.add(new BaoshanSearcher());
        searchers.add(new MinhangSearcher());
    }
    
    public List<Searcher> find(String zone) {
        List<Searcher> match = new ArrayList<>();
        for (Searcher one : searchers) {
            if (one.support(zone)) {
                match.add(one);
            }
        }
        
        return match;
    }
}
