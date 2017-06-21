package com.yunho.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBase {
	public static Statement DBconnection() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/DMS";
		String uid = "root";
		String upw = "hy980615";
		Connection connection;
		Statement statement;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, uid, upw);
			statement = connection.createStatement();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			statement = null;
		}
		return statement;
	}
}
