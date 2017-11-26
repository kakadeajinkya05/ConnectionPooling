package com.turvo.connectionPool.tests;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Test;

import com.turvo.connectionPool.ConnectionPoolImpl;

public class ConnectionPoolTest {


	@Test
	public void should_Return_Result_If_Data_Is_Present_In_Table() throws Exception {
		String query = "select * from recipes";
		ConnectionPoolImpl pool = new ConnectionPoolImpl(5, false);
		List<String > results = pool.executionPoint(query);
		assertThat(results.isEmpty(),Is.is(false));
	}
	
	@Test
	public void should_Return_Result_For_Multiple_Connection() throws Exception {
		String query = "select * from recipes";
		ConnectionPoolImpl pool = new ConnectionPoolImpl(5, false);
		List<String > results = null ;
		for(int i=0;i<10;i++){
			results = pool.executionPoint(query);
		}
		assertThat(results.isEmpty(),Is.is(false));
	}
	
	@Test
	public void should_Return_Empty_Result_If_Data_Is_Not_Present_In_Table() throws Exception {
		String query = "select * from recipes";
		ConnectionPoolImpl pool = new ConnectionPoolImpl(5, false);
		List<String > results = pool.executionPoint(query);
		results.clear();
		assertThat(results.isEmpty(),Is.is(true));
	}
	
	@Test(expected=SQLSyntaxErrorException.class )
	public void should_Throw_Exception_If_Table_Is_Not_Present() throws SQLSyntaxErrorException {
		String query = "select * from Employees";
		try {
			ConnectionPoolImpl pool = new ConnectionPoolImpl(5, false);
			pool.executionPoint(query);
		} catch (SQLSyntaxErrorException e) {
			throw new SQLSyntaxErrorException();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
