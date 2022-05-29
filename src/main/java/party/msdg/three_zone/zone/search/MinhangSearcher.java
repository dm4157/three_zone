package party.msdg.three_zone.zone.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.util.HttpUtil;
import party.msdg.three_zone.util.JacksonUtil;
import party.msdg.three_zone.zone.Zone;

import java.io.IOException;

/**
 * Wow! Sweet moon.
 */
public class MinhangSearcher implements Searcher{
    @Override
    public boolean support(String zone) {
        return zone().equals(zone);
    }
    
    @Override
    public String path() {
        return "https://gtgsh.shmh.gov.cn/qym/yqjkcx/fkqRiskLevel/list";
    }
    
    @Override
    public String zone() {
        return Zone.ZONE[7];
    }
    
    @Override
    public String search(Address address) {
        String town = address.getParts().get("镇");
        String street = address.getParts().get("街道");
        
        String streetName = null;
        if (town != null) {
            if (town.equals("莘庄")) {
                streetName = "莘庄工业区";
            } else {
                streetName = town + "镇";
            }
        } else if (street != null) {
            streetName = street + "街道";
        }
        if (null == streetName) return null;
        
        Param param = new Param(streetName, address.getParts().get("小区"));
    
        String result = null;
        try {
            String res = HttpUtil.post(path(), JacksonUtil.writeValueAsString(param));
            result = parse(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (null != result) {
            result = result + "#5";
        }
        return result;
    }
    
    private String parse(String res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readValue(res, JsonNode.class);
        if (node.get("total").asInt() > 0) {
            return node.get("rows").get(0).get("riskLevel").asText();
        }
        return null;
    }
    
    @Data
    public static class Param {
        private String streetName;
        private String unitName;
    
        public Param(String streetName, String unitName) {
            this.streetName = streetName;
            this.unitName = unitName;
        }
    }
    
    public static void main(String[] args) {
        MinhangSearcher searcher = new MinhangSearcher();
        Address add = new Address("上海市闵行区吴泾镇莫家石桥路塘泾北苑");
    
       for (int i=0 ; i<10; i++) {
           System.out.println(searcher.search(add));
       }
    }
}
