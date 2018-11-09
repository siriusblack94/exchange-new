package com.blockeng.mining;

import com.blockeng.mining.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Map<Long, List<User>> userMap = new HashMap();
        List<User> list = new ArrayList<>();
        list.add(new User().setId(2L).setDirectInviteid(1L));
        list.add(new User().setId(3L).setDirectInviteid(1L));
        list.add(new User().setId(4L).setDirectInviteid(1L));
        userMap.put(1L, list);
        list = new ArrayList<>();
        list.add(new User().setId(5L).setDirectInviteid(2L));
        list.add(new User().setId(6L).setDirectInviteid(2L));
        list.add(new User().setId(7L).setDirectInviteid(2L));
        userMap.put(2L, list);
        list = new ArrayList<>();
        list.add(new User().setId(8L).setDirectInviteid(3L));
        list.add(new User().setId(9L).setDirectInviteid(3L));
        list.add(new User().setId(10L).setDirectInviteid(3L));
        userMap.put(3L, list);
        list = new ArrayList<>();
        list.add(new User().setId(11L).setDirectInviteid(4L));
        list.add(new User().setId(12L).setDirectInviteid(4L));
        list.add(new User().setId(13L).setDirectInviteid(4L));
        userMap.put(4L, list);

        list = new ArrayList<>();
        list.add(new User().setId(15L).setDirectInviteid(5L));
        list.add(new User().setId(16L).setDirectInviteid(5L));
        list.add(new User().setId(17L).setDirectInviteid(5L));
        userMap.put(5L, list);

        TestDi testDi = new TestDi();
        List<User> userList = userMap.get(1L);
        List<User> newList = new ArrayList<>();
        List<User> child = testDi.getChild(newList, userList, userMap);
        System.out.println(child);

    }


}
