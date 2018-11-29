package com.jwt.DataBaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

public final class Config {
	@Context	
	private ServletContext context;
	
	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		if (this.context==null){
			this.context = context;
			loadConfig();
		}
	}

	String host;
	String database;
    String username;
    String password;
    private static Config instance;
    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }
    
    public void loadConfig() {
    	String fullPath = context.getRealPath("/WEB-INF/environement.ini");
    	Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(fullPath)));	
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		this.host = props.getProperty("POSTGRES_HOST"); 
		this.database = props.getProperty("POSTGRES_DB");
		this.username = props.getProperty("POSTGRES_USERNAME");
		this.password = props.getProperty("POSTGRES_PASSWORD");
    }
}
