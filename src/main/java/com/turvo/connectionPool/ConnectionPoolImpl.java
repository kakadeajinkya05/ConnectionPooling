package com.turvo.connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolImpl extends ConnectionPool {

	public ConnectionPoolImpl(int size, Boolean dynamicCreation) throws SQLException {
		super(size, dynamicCreation);
		createPool();
	}

	@Override
	protected List<Connection> getConnectionObjectFromPool() throws SQLException {
		Connection con1, con2;
		List<Connection> connections = new ArrayList<>();
			con1 = DriverManager.getConnection(props.getUrl1(), props.getUsername1(), props.getPassword1());
			con2 = DriverManager.getConnection(props.getUrl2(), props.getUsername2(), props.getPassword2());
			if (!con1.isClosed() && !con2.isClosed()) {
				connections.add(con1);
				connections.add(con2);
			}else{
				throw new SQLException(); 
			}

		return connections;
	}

	@Override
	protected void returnConnectionObjectToPool(List<Connection> connection) throws SQLException {
		if(pool.size()<size){
			pool.add(connection);
		}
	}

	public List<String> executionPoint(String query) throws Exception {
		List<Connection> connections = acquire();
		try {
			return performQueryExecution(connections,query);
		} finally {
			returnConnectionObjectToPool(connections);
		}
	}

	private List<String> performQueryExecution(List<Connection> connections, String query) throws SQLException {
		
		List<String> list = new ArrayList<>();
		for(Connection con : connections ){
			viewTable(con, query, list);	
		}
		return list;
	}

	private void viewTable(Connection con, String query, List<String> list) throws SQLException {

		Statement stmt = null;
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				list.add(rs.getString("recipe_name"));
			}
			if (stmt != null) {
				stmt.close();
			}
	}
}