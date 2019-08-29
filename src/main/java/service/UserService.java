package service;

import com.mysql.jdbc.Connection;
import dao.Impl.UserDAOHibernateImpl;
import dao.Impl.UserDAOJDBCImpl;
import dao.UserDAO;
import dao.UserDaoFactory;
import model.User;
import util.DBHelper;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class UserService {

    private static UserService userService;
    private final static String DB = UserDAOJDBCImpl.DB;
    private final static String DB_TABLE = UserDAOJDBCImpl.DB_TABLE;
    private static UserDaoFactory userDaoFactory = UserDaoFactory.getInstance();

    private UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public Collection<User> getAllUser() {
        Collection<User> list;
        list = getUserDAO().getAllUsers();
        return list;
    }

    public boolean addUser(User user) {
        getUserDAO().addUser(user);
        if (getUserDAO().isUserExist(user.getName(), user.getPassword())) {
            return true;
        }
        return false;
    }

    public void updateUser(User user) {
        getUserDAO().updateUser(user);
    }

    public void deleteUserFromID(Long id) {
        getUserDAO().deleteUserFromID(id);
    }

    private static UserDAO getUserDAO() {
        //return new UserDAOJDBCImpl(DBHelper.getConnection());
        return userDaoFactory.getUserDao();
       // return new UserDAOHibernateImpl(DBHelper.getSessionFactory().openSession());
    }

    public void createTable() {
        try {
            Connection connection = DBHelper.getConnection();
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE if not exists " + DB + "." + DB_TABLE + " (id bigint auto_increment" +
                    ", name varchar(256) not null" +
                    ", password VARCHAR(256) not null" +
                    ", date DATE not null, PRIMARY KEY (id)" +
                    ", UNIQUE INDEX `id_UNIQUE` (name ASC) VISIBLE) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}