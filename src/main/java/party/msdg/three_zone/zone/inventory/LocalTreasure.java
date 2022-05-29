package party.msdg.three_zone.zone.inventory;

import com.alibaba.excel.EasyExcel;
import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import party.msdg.three_zone.model.ThreeZone;
import party.msdg.three_zone.model.User;
import party.msdg.three_zone.util.AddUtil;
import party.msdg.three_zone.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Wow! Sweet moon.
 */
public class LocalTreasure {
    
    public static void main(String[] args) throws IOException {
        LocalTreasure.save();
    }

    public static void save() throws IOException {
        EasyExcel.write("src/main/resources/three_zones.xls", ThreeZone.class)
            .sheet("三区名单-本地宝")
            .doWrite(LocalTreasure::threeZones);
    }
    
    public static List<ThreeZone> threeZones() {
        String url = "http://m.sh.bendibao.com/news/fengkong/?qu=%E5%85%A8%E9%83%A8";
        Document doc = Jsoup.parse(HttpUtil.get(url));
    
        List<ThreeZone> threeZones = new ArrayList<>();
        threeZones.addAll(parse(doc.getElementById("fkqu"), "封控区"));
        threeZones.addAll(parse(doc.getElementById("gkqu"), "管控区"));
        threeZones.addAll(parse(doc.getElementById("ffqu"), "防范区"));
        return threeZones;
    }
    
    private static List<ThreeZone> parse(Element el, String type) {
        Elements children = el.getElementsByClass("news_block");
        if (children.size() == 0) return new ArrayList<>();
        
        List<ThreeZone> threeZones = new ArrayList<>();
    
        for (Element oneBlock : children) {
            // 获取头部信息，区和镇/街道
            String title = oneBlock.getElementsByTag("p").get(0).html();
            String[] tits = title.split(" ");
            String zone = tits[0];
            String town = null;
            String street = null;
            if (tits[1].contains("街道")) {
                street = tits[1].substring(0, tits[1].indexOf("街道"));
            } else if (tits[1].contains("镇")) {
                town = tits[1].substring(0, tits[1].indexOf("镇"));
            } else {
                System.out.println(tits[1] + "::" + title);
                town = AddUtil.removeBracket(tits[1]);
                town = AddUtil.removeBracketZH(town);
            }
        
            // 循环获得具体地址
            Elements locations = oneBlock.getElementsByTag("li");
            for (Element element : locations) {
                String location = element.getElementsByClass("name").get(0).html();
                location = AddUtil.removeBracketZH(location);
                threeZones.add(new ThreeZone(zone, town, street, location, type));
            }
        }
        
        return threeZones;
    }
}
