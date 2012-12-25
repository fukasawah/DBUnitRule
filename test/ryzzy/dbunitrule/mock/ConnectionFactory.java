package ryzzy.dbunitrule.mock;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {
	public static Connection createMySQLConnection(String propertiesPath) {
		try
		{
			Properties prop = new Properties();
			prop.load(new FileInputStream(propertiesPath));
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(
					prop.getProperty("DB_URL"),
					prop.getProperty("DB_USER"),
					prop.getProperty("DB_PASSWORD")
					);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
