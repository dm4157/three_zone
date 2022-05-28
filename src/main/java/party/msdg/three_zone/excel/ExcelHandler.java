package party.msdg.three_zone.excel;

import com.alibaba.excel.EasyExcel;
import party.msdg.three_zone.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wow! Sweet moon.
 */
public class ExcelHandler {
    
    /**
     * 读取用户花名册
     */
    public List<User> readUsers() {
        // 读
        List<Map<Integer, String>> listMap = EasyExcel.read("src/main/resources/users.xls").sheet().doReadSync();
        
        List<User> users = new ArrayList<>(listMap.size());
        listMap.forEach(map -> {
            User user = new User();
            user.setCode(map.get(0));
            user.setName(map.get(1));
            user.setPhone(map.get(2));
            user.setCompany(map.get(3));
            user.setCenter(map.get(4));
            user.setDepartment(map.get(5));
            user.setOffice(map.get(6));
            user.setAddress(map.get(7));
            user.setState(map.get(8));
            users.add(user);
        });
        
        return users;
    }
    
    public void write(List<User> users) {
//        List<List<String>> data = new ArrayList<>();
//        String tempFilePath = fileRice.taskPath(profile, task.getId());
//        ExcelWriter writer = fileRice.createWriter(tempFilePath, columns);
//        WriteSheet writeSheet = EasyExcel.writerSheet().build();
//        writer.write(pageData, writeSheet);
//        writer.finish();
        EasyExcel.write("/Users/stone/Desktop/员工三区分布.xls", User.class)
            .sheet("三区划分")
            .doWrite(() -> {
                // 分页查询数据
                return users;
            });
    
    }
    
}
