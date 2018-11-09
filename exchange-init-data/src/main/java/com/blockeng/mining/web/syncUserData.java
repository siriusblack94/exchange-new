package com.blockeng.mining.web;

import com.blockeng.mining.entity.User;
//import com.cn1win.util.string.StringUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class syncUserData {

    public static void main(String[] args) {

        updatePassword();

    }

    private static void updatePassword() {


        List<User> users = new ArrayList<User>();
        Connection conn = getConn();
        String sql = "select id,password,paypassword  from 'user' where flag=0";
        PreparedStatement pstmt;
        try {

            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int i=1;
            while (rs.next()) {


                Long id = (rs.getLong("id"));
                String password = (rs.getString("password"));
                String paypassword = (rs.getString("paypassword"));

                String new_password = password!=null?new BCryptPasswordEncoder().encode(password.toLowerCase()):"";
                String new_payPassword = paypassword!=null?new BCryptPasswordEncoder().encode(paypassword.toLowerCase()):"";


                sql = "update 'user' set password=? ,paypassword=? where id=? and flag=0";
                pstmt = (PreparedStatement)conn.prepareStatement(sql);
                pstmt.setString(1,new_password);
                pstmt.setString(2,new_payPassword);
                pstmt.setLong(3,id);
                pstmt.execute();

                System.out.println("update--"+i++);

            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://rm-j6cy4tt1uz6yv7c95.mysql.rds.aliyuncs.com:3306/ex_trade";
        String username = "ex";
        String password = "pBV7GyIne4Li2U6wNgxXMPK0HZDQRz";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
