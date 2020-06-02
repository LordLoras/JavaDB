import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class reading {
	
	private Connection conn;  
    private Statement stmt;
    private ResultSet rset;
	
    public reading () 
    {  
    	Properties prop = new Properties();

    	try {
    	    prop.load(new FileInputStream("config.properties"));
    	    System.out.println(prop.getProperty("host"));
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
    }