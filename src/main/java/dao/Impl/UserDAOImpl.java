package dao.Impl;

import dao.UserDAO;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAOImpl implements UserDAO {

    private Connection connection;
    public final static String DB = "dbuser";
    public final static String DB_TABLE = "user";

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<User> getAllUsers() {
       Collection<User> list = new ArrayList<User>();
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("select * from " + DB + "." + DB_TABLE);
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                String password = result.getString("password");
                Date date = result.getDate("date");
                list.add(new User(id, name, password, date));
            }
            result.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void addUser(User user) {

        try {
            PreparedStatement stm = connection.prepareStatement("insert into " + DB + "." + DB_TABLE + " (name, password, date) values (?, ?, ? )");
            stm.setString(1, user.getName());
            stm.setString(2, user.getPassword());
            stm.setDate(3, user.getBirthday());
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            PreparedStatement stm = connection.prepareStatement("update " + DB + "." + DB_TABLE + " set name=?, password=?, date=? where id=?");
            stm.setString(1, user.getName());
            stm.setString(2, user.getPassword());
            stm.setDate(3, user.getBirthday());
            stm.setLong(4, user.getId());
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserFromID(Long id) {
        try {
            PreparedStatement stm = connection.prepareStatement("delete from " + DB + "." + DB_TABLE + " where id=?");
            stm.setLong(1, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUserExist(String name, String password) {
        String query = "select * from " + DB + "." + DB_TABLE + " where name=? and  password=?";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, name);
            stm.setString(2, password);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                result.close();
                stm.close();
                return true;
            }
            result.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}