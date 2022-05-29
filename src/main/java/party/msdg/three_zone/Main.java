package party.msdg.three_zone;

import party.msdg.three_zone.excel.ExcelHandler;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.model.ThreeZone;
import party.msdg.three_zone.model.User;
import party.msdg.three_zone.util.NumberUtil;
import party.msdg.three_zone.util.similarity.CosineSimilarity;
import party.msdg.three_zone.zone.inventory.LocalTreasure;
import party.msdg.three_zone.zone.search.JinganSearcher;
import party.msdg.three_zone.zone.search.SearchHandler;
import party.msdg.three_zone.zone.search.Searcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wow! Sweet moon.
 */
public class Main {
    
    public static String[] ZONE = {"黄浦","徐汇","长宁","静安","普陀","虹口","杨浦","闵行","宝山","嘉定","浦东新","金山","松江","南汇","奉贤","青浦","崇明"};
    
    public static void main(String[] args) {
        String split = "::";
        SearchHandler searchHandler = new SearchHandler();

        // 读取花名册
        ExcelHandler handler = new ExcelHandler();
        List<User> users = handler.readUsers();
        
        /*
         --------------------------
         🤔 为匹配准备
         --------------------------
         */
        List<ThreeZone> threeZones = LocalTreasure.threeZones();
        Map<String, List<ThreeZone>> townMap = new HashMap<>();
        Map<String, List<ThreeZone>> streetMap = new HashMap<>();
        threeZones.forEach(tz -> {
            if (tz.getTown() != null) {
                String key = tz.getZone() + split + tz.getTown();
                townMap.computeIfAbsent(key, k -> new ArrayList<>());
                townMap.get(key).add(tz);
            }
            if (tz.getStreet() != null) {
                String key = tz.getZone() + split + tz.getStreet();
                streetMap.computeIfAbsent(key, k -> new ArrayList<>());
                streetMap.get(key).add(tz);
            }
        });

        users.forEach(user -> {
            /*
             --------------------------
             🤔 前期准备
             --------------------------
             */
            if (!user.getState().equals("是")) return;
            
            Address address = new Address(user.getAddress());
            String zone = address.getParts().get("区");
            if (null == zone || "".equals(zone)) return;
            
            // 全面防范
            if (zone.equals("金山") || zone.equals("奉贤") || zone.equals("崇明")) {
                user.setRegion("防范区");
                user.setBelieve("5");
                return;
            }
            
            /*
             --------------------------
             🐙 尝试搜索
             --------------------------
             */
            List<Searcher> searchers = searchHandler.find(zone);
            if (searchers.size() > 0) {
                for (Searcher se : searchers) {
                    String region = se.search(address);
                    if (null != region) {
                        String[] rs = region.split("#");
                        user.setRegion(rs[0]);
                        user.setBelieve(rs[1]);
                        System.out.println(user.getAddress() + "::" + region);
                        break;
                    }
                }
            }
            
            if (user.getRegion() != null) return;
            
            /*
             --------------------------
             🐟 尝试匹配
             --------------------------
             */
            String town = address.getParts().get("镇");
            String street = address.getParts().get("街道");
            if (null == street) {
                street = address.getParts().get("街");
            }
            
            List<ThreeZone> tzs = townMap.get(zone + "区" + split + town);
            if (null == tzs) {
                tzs = streetMap.get(zone + "区" + split + street);
            }
            if (null == tzs) {
                if (zone.equals("杨浦")) {
                    user.setRegion("防控区");
                    user.setBelieve("2");
                }
            } else {
                String area = address.getParts().get("小区");
                if (null != area) {
                    for (ThreeZone tz : tzs) {
                        if (tz.getArea().contains(area)) {
                            user.setRegion(tz.getType());
                            user.setBelieve("5");
                            break;
                        } else {
                            double score = CosineSimilarity.getSimilarity(area, tz.getArea());
                            System.out.println(zone + "-" + town + "-" + street + "  " + area + split + tz.getArea() + split + score);
                            if (score > 0.1) {
                                if (user.getBelieve() != null) {
                                    double oldS = Double.parseDouble(user.getBelieve());
                                    if (score * 5 > oldS) {
                                        user.setRegion(tz.getType());
                                        user.setBelieve(String.valueOf(NumberUtil.parse(5 * score, 1)));
                                    }
                                } else {
                                    user.setRegion(tz.getType());
                                    user.setBelieve(String.valueOf(NumberUtil.parse(5 * score, 1)));
                                }
                            }
                        }
                    }
                    
                }
            }
        });
      
        handler.write(users);
        
//        JinganSearcher jinganSearcher = new JinganSearcher();
//
//        Address address = new Address("上海市静安区彭浦镇晋城路663弄1号阳城世家苑");
//        System.out.println(address);
//        String res = jinganSearcher.search(address);
//        System.out.println(res);
//
//
        // 地址拆分测试
//        List<User> users = handler.readUsers();
//        users.forEach(user -> System.out.println(user.getAddress() + "::" + new Address(user.getAddress())));
    }
}
