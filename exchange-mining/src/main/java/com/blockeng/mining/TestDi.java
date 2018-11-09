package com.blockeng.mining;

import com.blockeng.mining.entity.User;

import java.util.List;
import java.util.Map;

public class TestDi {


    public TestDi() {
    }


    /***
     *
     *
     * @param list
     * @param userMap
     * @return
     */
    public List<User> getChild(List<User> result, List<User> list, Map<Long, List<User>> userMap) {
//        for (int i = 0; i < list.size(); i++) {
//            User user = list.get(i);
//            result.add(user);
//            List<User> list1 = userMap.get(user.getId());
//            if (!CollectionUtils.isEmpty(list1)) {
//                getChild(result,list1, userMap);
//            }
//        }
        return result;
    }
}
