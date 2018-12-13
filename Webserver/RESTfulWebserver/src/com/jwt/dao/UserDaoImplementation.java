package com.jwt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Address;
import com.jwt.model.BasicUser;
import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.User;

public class UserDaoImplementation implements UserDao {

    DatabaseProvider db;

    public UserDaoImplementation() {
        db = DatabaseProvider.getInstance();
    }

    public User createUser(String email, String password, int user_type) {
        //		TODO: implement me
        User user = new User();
        return user;
    }

    @Override
    public BasicUser getUser(int id) {
        //TODO change to arguments syntax
        ResultSet rsUser = db.querySelectDB("SELECT * FROM basic_user b, user_reg u WHERE b.id_user = u.id_user AND b.id_user = " + id);

        try {
            if (rsUser.next()) {
                return new BasicUser(rsUser.getInt("id_user"), rsUser.getString("first_name"), rsUser.getString("last_name"), rsUser.getString("email"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BasicUser> getAllUser() {
        ResultSet rsUser = db.querySelectDB("SELECT * FROM basic_user b, user_reg u WHERE b.id_user = u.id_user");
        return getListByRS(rsUser);
    }

    @Override
    public List<BasicUser> getFriends(int id) {
        //TODO change to arguments syntax
        ResultSet rsFriends = db.querySelectDB("SELECT b.id_user, b.first_name, b.last_name, b.dob, u.email FROM basic_user b, friendship f, user_reg u WHERE u.id_user = b.id_user AND b.id_user = f.id_user2 AND f.id_user1 = " + id);

        return getListByRS(rsFriends);
    }

    public boolean getFriendById(int userId, int friendId) {
        ResultSet rsFriend = db.querySelectDB("SELECT * FROM friendship WHERE id_user1 = ? AND id_user2 = ?", userId, friendId);

        try {
            if (rsFriend.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public BasicUser getAdditionalInfo(int userId) {
        ResultSet rs = db.querySelectDB("SELECT b.id_user, u.email, b.first_name, b.last_name, b.dob, a.country, a.state, a.city, a.street, a.postcode, a.housenumber, a.longitude, a.latitude, g.gender FROM basic_user b, address a, gender g, user_reg u WHERE b.id_address = a.id_address AND u.id_user = b.id_user AND b.id_gender = g.id_gender AND b.id_user = ?", userId);
        BasicUser user = null;
        try {
            if (rs.next()) {
                user = new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                user.setUserAddress(new Address(rs.getString("country"), rs.getString("state"), rs.getString("city"), rs.getInt("postcode"), rs.getString("street"), rs.getString("housenumber"), rs.getDouble("longitude"), rs.getDouble("latitude")));
                user.setGender(rs.getString("gender"));
                user.setDob(rs.getString("dob"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<BasicUser> searchUser(String input, int userId) {

        //TODO add more advanced search methods to handle input
        ResultSet rs = db.querySelectDB("SELECT u.id_user, b.first_name, b.last_name, u.email FROM user_reg u, basic_user b WHERE b.id_user = u.id_user AND u.id_user != ? AND (email = ? OR first_name = ? OR last_name = ?)", userId, input, input, input);

        List<BasicUser> user = new ArrayList<>();
        try {
            while (rs.next()) {
                if (getFriendById(userId, rs.getInt("id_user"))) {
                    //add user with friendship status "friends"
                    user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), true));
                } else {
                    //add user without friendship status
                    user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public User getUser(String email, String password) {
        User user = null;
        String selectSQL = "SELECT * FROM user_reg WHERE email=\'" + email + "\' AND password=\'" + password + "\'";
        try {
            DatabaseProvider conn = DatabaseProvider.getInstance();
            ResultSet result = conn.querySelectDB(selectSQL);

            while (result.next()) {
                user = new User(result.getInt("id_user"), result.getString("email"), result.getInt("id_user_type"));
            }
            return user;
        } catch (SQLException ex) {
            return null;
        }
    }

    private List<BasicUser> getListByRS(ResultSet rs) {
        List<BasicUser> user = new ArrayList<>();
        try {
            while (rs.next()) {
                user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }
    }
