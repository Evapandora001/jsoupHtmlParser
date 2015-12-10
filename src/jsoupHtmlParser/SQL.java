package jsoupHtmlParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 获取feature过程中SQL语句，数据的select 以及最后feature的update
 */
public class SQL {
	/**
	 * @return  SQL连接
	 */
	public static Connection getConn(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection(
					"jdbc:mysql://192.168.122.51:3306/weibo?useUnicode=true&characterEncoding=utf8","root","");
			return conn;
		} catch (Exception e) {
			System.err.println("数据库连接失败");
			e.printStackTrace();
		}
		return null;
	}
}
