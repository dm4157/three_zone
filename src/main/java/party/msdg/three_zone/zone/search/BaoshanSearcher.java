package party.msdg.three_zone.zone.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.util.HttpUtil;
import party.msdg.three_zone.zone.Zone;

import java.io.IOException;

/**
 * Wow! Sweet moon.
 */
public class BaoshanSearcher implements Searcher {
    @Override
    public boolean support(String zone) {
        return zone.equals(zone());
    }
    
    @Override
    public String path() {
        return "https://shbsq.yunban.cn/shbs-sqhf/cgi-bin/user-api/three/list?accessToken={0}&address={1}";
    }
    
    @Override
    public String zone() {
        return Zone.ZONE[8];
    }
    
    @Override
    public String search(Address address) {
        String path = path().replace("{0}", Tokens.BAOSHAN);
    
        String res = null;
        String believe = null;
        try {
            String area = address.getParts().get(Address.SMALL);
            if (null != area) {
                believe = "5";
                res = tryOnce(path, area);
            }
            if (null == res) {
                String road = address.getParts().get("路");
                if (null != road) {
                    believe = "4";
                    res = tryOnce(path, road + "路");
                }
            }
            if (null == res) {
                String street = address.getParts().get("街");
                if (null == street) {
                    street = address.getParts().get("街道");
                }
            
                if (null != street) {
                    believe = "3";
                    res = tryOnce(path, street + "街");
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
    
    private String tryOnce(String path, String key) throws IOException {
        path = path.replace("{1}", key);
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
    
        if (200 == node.get("status").asInt()) {
            if (node.get("result").size() > 0) {
                if (node.get("result").get(0).get("list").size() > 0) {
                    return node.get("result").get(0).get("list").get(0).get("type").asText();
                }
               
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        BaoshanSearcher searcher = new BaoshanSearcher();
        String res = searcher.search(new Address("上海市宝山区大场镇四季宜景苑"));
        System.out.println(res);
    }
}
