package com.turvo.connectionPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.turvo.connectionPool.properties.DataSourceProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ConnectionPool {
	
    public final BlockingQueue<List<Connection>> pool;
    private final ReentrantLock lock = new ReentrantLock();
    private int createdObjects = 0;
    protected int size;
    protected DataSourceProperties props;
    
    protected abstract List<Connection> getConnectionObjectFromPool() throws SQLException;
    protected abstract void returnConnectionObjectToPool(List<Connection> connection) throws SQLException;
    
    protected ConnectionPool(int size, Boolean dynamicCreation) {
    	try {
    		setup(); 
			Class.forName(props.getDriver1()).newInstance();
			Class.forName(props.getDriver2()).newInstance();
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
        pool = new ArrayBlockingQueue<>(size, true);
        this.size = size;
        if (!dynamicCreation) {
            lock.lock();
        }
    }
 
    protected List<Connection> acquire() throws Exception {
        if (!lock.isLocked()) {
            if (lock.tryLock()) {
                try {
                    ++createdObjects;
                    return getConnectionObjectFromPool();
                } finally {
                    if (createdObjects < size) lock.unlock();
                }
            }
        }
        return pool.take();
    }
 
    protected void createPool() throws SQLException {
        if (lock.isLocked()) {
            for (int i = 0; i < size; ++i) {
                pool.add(getConnectionObjectFromPool());
                createdObjects++;
            }
        }
        System.out.println("Object Created in pool: "+createdObjects);
    }
 
    private void setup(){
		try {
			File file = new File("src/main/resources/application.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			props =  DataSourceProperties.builder()
					.driver1(properties.getProperty("driver1"))
					.url1(properties.getProperty("url1"))
					.username1(properties.getProperty("username1"))
					.password1(properties.getProperty("password1"))
					.driver2(properties.getProperty("driver2"))
					.url2(properties.getProperty("url2"))
					.username2(properties.getProperty("username2"))
					.password2(properties.getProperty("password2")).build();
		} catch (FileNotFoundException e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
	}
    
}
