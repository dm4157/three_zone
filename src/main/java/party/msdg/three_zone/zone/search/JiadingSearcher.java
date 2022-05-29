package party.msdg.three_zone.zone.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.util.HttpUtil;
import party.msdg.three_zone.zone.Zone;

import java.io.IOException;

/**
 * Wow! Sweet moon.
 */
public class JiadingSearcher implements Searcher {
    @Override
    public boolean support(String zone) {
        return zone.equals(zone());
    }
    
    @Override
    public String path() {
        return "http://three.jcmc.sh.cn:8090/threeAreasManagement/THREE_AREA_URL/api-preventionControlArea/threeStatistics/getVillageInfo?villageName={0}";
    }
    
    @Override
    public String zone() {
        return Zone.ZONE[9];
    }
    
    @Override
    public String search(Address address) {
    
        String res = null;
        String believe = null;
        try {
            String area = address.getParts().get(Address.SMALL);
            if (null != area) {
                believe = "5";
                res = tryOnce(path().replace("{0}", area));
            }
            if (null == res) {
                String road = address.getParts().get("路");
                if (null != road) {
                    believe = "4";
                    res = tryOnce(path().replace("{0}", road + "路"));
                }
            }
            if (null == res) {
                String street = address.getParts().get("街");
                if (null == street) {
                    street = address.getParts().get("街道");
                }
                
                if (null != street) {
                    believe = "3";
                    res = tryOnce(path().replace("{0}", street + "街"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        if (null != res) {
            res = res + "#" + believe;
        }
        return res;
    }
    
    private String tryOnce(String path) throws IOException {
        System.out.println(path);
        
        String result = HttpUtil.get(path);
        if (null == result) return null;
        
        // 解析结果
        System.out.println(result);
        return parse(result);
    }
    
    private String parse(String res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readValue(res, JsonNode.class);
        
        if (200 == node.get("code").asInt()) {
            if (node.get("data").size() > 0) {
                return node.get("data").get(0).get("type").asText();
            }
        }
        return null;
    }
}
