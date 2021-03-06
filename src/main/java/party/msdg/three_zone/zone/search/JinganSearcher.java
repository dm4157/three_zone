package party.msdg.three_zone.zone.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.math3.analysis.function.Add;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.util.HttpUtil;
import party.msdg.three_zone.util.JacksonUtil;
import party.msdg.three_zone.zone.Zone;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 全局（虽然是静安）
 * Wow! Sweet moon.
 */
public class JinganSearcher implements Searcher{
    private final int index = 3;
    
    @Override
    public boolean support(String zone) {
        return true;
//        return zone.equals(Zone.ZONE[index]) || zone.equals(Zone.ZONE[0]) || zone.equals(Zone.ZONE[5]);
    }
    
    @Override
    public String path() {
        return "https://shdistrict.eshimin.com/sh-api/api/app/member/sas/V1/query?key=&page=1&limit=10&servCode=3991&districtId={0}&confName={1}";
    }
    
    @Override
    public String zone() {
        return Zone.ZONE[index];
    }
    
    @Override
    public String search(Address address) {
        String path = path().replace("{0}", Zone.CODE.get(address.getParts().get("区")));
        
        String res = null;
        String believe = null;
        try {
//            return tryOnce(path, address.getParts().get(Address.SMALL));
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
        
        if ("SUCCESS".equals(node.get("msg").asText())) {
            int count = node.get("data").get("totalCount").asInt();
            if (count > 0 && count < 30) {
                return node.get("data").get("list").get(0).get("classtypeId").asText();
            }
        }
        return null;
    }
}
