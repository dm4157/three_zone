package party.msdg.three_zone.zone.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.math3.analysis.function.Add;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.util.JacksonUtil;
import party.msdg.three_zone.zone.Zone;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
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
    
        try {
            return tryOnce(path, address.getParts().get(Address.SMALL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    private String tryOnce(String path, String key) throws IOException {
        path = path.replace("{1}", key);
        System.out.println(path);
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(path)
            .build();
    
        String result = null;
        try (Response response = client.newCall(request).execute()) {
            result =  response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (null == result) return null;
        
        // 解析结果
        System.out.println(result);
        String res = parse(result);
        return res;
    }
    
    private String parse(String res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readValue(res, JsonNode.class);
        
        if ("SUCCESS".equals(node.get("msg").asText())) {
            int count = node.get("data").get("totalCount").asInt();
            if (count > 0 && count < 10) {
                return node.get("data").get("list").get(0).get("classtypeId").asText();
            }
        }
        return null;
    }
}
