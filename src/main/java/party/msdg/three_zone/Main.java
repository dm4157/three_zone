package party.msdg.three_zone;

import party.msdg.three_zone.excel.ExcelHandler;
import party.msdg.three_zone.model.Address;
import party.msdg.three_zone.model.User;
import party.msdg.three_zone.zone.search.JinganSearcher;
import party.msdg.three_zone.zone.search.SearchHandler;
import party.msdg.three_zone.zone.search.Searcher;

import java.util.List;

/**
 * Wow! Sweet moon.
 */
public class Main {
    
    public static String[] ZONE = {"黄浦","徐汇","长宁","静安","普陀","虹口","杨浦","闵行","宝山","嘉定","浦东新","金山","松江","南汇","奉贤","青浦","崇明"};
    
    public static void main(String[] args) {
        SearchHandler searchHandler = new SearchHandler();
        ExcelHandler handler = new ExcelHandler();
        List<User> users = handler.readUsers();

        users.forEach(user -> {
            if (!user.getState().equals("是")) return;
            
            
            
            Address address = new Address(user.getAddress());
            String zone = address.getParts().get("区");
            if (null == zone || "".equals(zone)) return;
            
            // 尝试搜索
            Searcher searcher = searchHandler.find(zone);
            if (null != searcher) {
                String region = searcher.search(address);
                user.setRegion(region);
                System.out.println(user.getAddress() + "::" + region);
            } else {
            
            }
            
            // 尝试匹配
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
