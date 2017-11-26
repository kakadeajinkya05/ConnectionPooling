package com.turvo.connectionPool.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.turvo.connectionPool.ConnectionPoolImpl;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class ConnectionPoolTest {
	
	public static void main(String args[]) throws SQLException {
		int poolsize = 5;
		String query = "select * from recipes";
		
		final ConnectionPoolImpl pool = new ConnectionPoolImpl(poolsize, false);
		
		Callable<List<String>> task = new Callable<List<String>>() {
			@Override
			public List<String> call() throws Exception {
				return pool.executionPoint(query);
			}
		};

		ExecutorService exec = Executors.newFixedThreadPool(30);
		List<Future<List<String>>> results = new ArrayList<>();

		for (int i = 0; i < 30; i++) {
			results.add(exec.submit(task));
		}
		exec.shutdown();
		List<String> connections = null;
		try {
			for (Future<List<String>> result : results) {
				connections = result.get();
				System.out.println(connections.toString());
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
	}
}
