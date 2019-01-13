package com.familytree.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
 
public class ConnectionUtils {

	private static final String CONNECTION_URL="jdbc:mysql://localhost:3306/CO3098_CW3";
	private static final String USERNAME="root";
	private static final String PASSWORD="test";
	public static Connection getConnection() {
		Connection con=null;
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection(  
			CONNECTION_URL,USERNAME,PASSWORD);  
			//here sonoo is database name, root is username and password
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
