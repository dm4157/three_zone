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
public class XuhuiSearcher implements Searcher {
    
    @Override
    public boolean support(String zone) {
        return zone().equals(zone);
    }
    
    @Override
    public String path() {
        return "https://api.xuhui.gov.cn/iocxh/api/app/member/sas/V1/entry?key=&page=1&limit=10&servCode=3991&districtId=310104&confName={0}";
    }
    
    @Override
    public String zone() {
        return Zone.ZONE[1];
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
        
        String result = HttpUtil.get(path, Tokens.XUHUI);
        if (null == result) return null;
        
        // 解析结果
        System.out.println(result);
        return parse(result);
    }
    
    private String parse(String res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readValue(res, JsonNode.class);
    
        if ("SUCCESS".equals(node.get("msg").asText())) {
            int count = node.get("data").get("totalCount").asInt();
            if (count > 0 && count < 30) {
                return node.get("data").get("list").get(0).get("classtypeId").asText();
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        XuhuiSearcher xuhuiSearcher = new XuhuiSearcher();
        String res = xuhuiSearcher.search(new Address("上海市徐汇区漕河泾街道龙漕路龙漕家园"));
        System.out.println(res);
    }
}
