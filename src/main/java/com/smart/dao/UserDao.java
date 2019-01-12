package com.smart.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.smart.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository//1.通过spring注解定义一个dao
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private final static String MATCH_COUNT_SQL=" select count(*) from "+" t_user where user_name=? and password=?";
    private final static String UPDATE_LOGIN_INFO_SQL=" UPDATE t_user SET "+" last_visit=?,last_ip=?,credit=? where user_id=?";
    private final static String QUERY_BY_USERNAME=" SELECT user_id ,user_name,credits "+" FROM t_user WHERE user_name=?";
    public User findUserByUserName(final String userName){
        final User user=new User();
        jdbcTemplate.query(QUERY_BY_USERNAME, new Object[]{userName},
                new RowCallbackHandler() {
                    public void processRow(ResultSet resultSet) throws SQLException {
                        user.setUserId(resultSet.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(resultSet.getInt("credits"));
                    }
                }
        );
        return user;
    }
    public void updateLoginInfo(User user){
        jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL,new Object[]{user.getLastVisit(),user.getLastIp(),user.getCredits(),user.getUserId()});
    }
    @Autowired//2.自动注入JdbcTemplate的Bean
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    public int getMartchCount(String userName,String password){
        String sqlStr=" select count(*) from t_user "+" where user_name =? and password=?";
        return jdbcTemplate.queryForObject(sqlStr,new Object[]{userName,password},Integer.class);
    }
}
