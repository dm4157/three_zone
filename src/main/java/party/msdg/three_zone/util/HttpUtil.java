package party.msdg.three_zone.util;

import okhttp3.*;

import java.io.IOException;

/**
 * Wow! Sweet moon.
 */
public class HttpUtil {
    
    public static String get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String get(String url, String token) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .header("token", token)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static final MediaType JSON= MediaType.get("application/json; charset=utf-8");
    public static String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
//            .header("Content-Type", "application/json")
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public static void main(String[] args) {
        String str = HttpUtil.get("https://shdistrict.eshimin.com/sh-api/api/app/member/sas/V1/query?key=&page=1&limit=10&servCode=3991&districtId=310104&confName=田林街");
        System.out.println(str);
    }
}
