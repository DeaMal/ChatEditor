package chat.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataBase {
    private static DataBase instance;
    private DataSource data;

    private DataBase(){}

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public void init(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        data = new HikariDataSource(config);
    }

    public Connection connect() {
        try {
            return data.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillData(String string) {
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            InputStream inputStream = DataBase.class.getClassLoader().getResourceAsStream(string);
            assert inputStream != null;
            Scanner is = new Scanner(inputStream).useDelimiter(";");
            while (is.hasNext()) {
                statement.executeUpdate(is.next());
            }
            is.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return data;
    }
}
