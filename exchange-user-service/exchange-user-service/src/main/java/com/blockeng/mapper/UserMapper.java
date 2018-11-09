package com.blockeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blockeng.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByIdCard(@Param("idCard") String idCard);

    int updateAuthName(User user);

    int updatePassword(@Param("id") long id, @Param("password") String password);

    int updatePayPassword(@Param("id") long id, @Param("payPassword") String payPassword);

    int updatePhone(@Param("mobile") String mobile, @Param("countryCode") String countryCode, @Param("id") long id);

    int updatePassWordByMobile(@Param("password") String password, @Param("mobile") String mobile);

    int updatePassWordByEmail(@Param("password") String password, @Param("email") String email);

    int updateUserBase(@Param("id") long userId, @Param("username") String userName, @Param("email") String email, @Param("payPassword") String payPassword);

    List<User> selectListByInviteId(@Param("userId") String userId);

    int updateEmail(@Param("mobile") String newEmail, @Param("id") Long id);
}
