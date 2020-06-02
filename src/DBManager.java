import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class DBManager {
	
	private Connection conn;  
    private Statement st;
    private ResultSet rset;
	private PreparedStatement pst;
    public DBManager () 
    {  
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            e.printStackTrace();
            return;
        }
        
        // Read connection URL from prop file
        Properties prop = new Properties();
    	try 
    	{
    	    prop.load(new FileInputStream("config.properties"));
    	    System.out.println(prop.getProperty("host"));
    	} catch (IOException e) 
    	{
    	    e.printStackTrace();
    	}
                    
        try 
        {                   
            // Connect to the database.
            conn = DriverManager.getConnection(prop.getProperty("host") + 
        	prop.getProperty("database") + "?useSSL=false", 
        	prop.getProperty("username"), 
        	prop.getProperty("password"));           
        	st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);     
        	
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void UpdateEmployee(String EmployeeName, int EmployeeSalary, int EmployeePhone, int id) 
    {
     try 
    {
    	//prepare the sql query
		pst = conn.prepareStatement("UPDATE employee_table SET employee_name = ?, employee_salary = ?, employee_phone = ? WHERE id = ?");
		pst.setString(1, EmployeeName);
		pst.setInt(2, EmployeeSalary);
		pst.setInt(3, EmployeePhone);
		pst.setInt(4, id);
		pst.executeUpdate();
		pst.close();
	} 
     catch (SQLException e) 
     {
		e.printStackTrace();
	}
    }
    
    
    
    //update the products
    public void UpdateProducts(String ProductName, int ProductPrice, int ProductRating, int id) 
    {
     try 
    {
    	//prepare the sql query
		pst = conn.prepareStatement("UPDATE products_table SET product_name = ?, product_price = ?, product_rating = ? WHERE id = ?");
		pst.setString(1, ProductName);
		pst.setInt(2, ProductPrice);
		pst.setInt(3, ProductRating);
		pst.setInt(4, id);
		pst.executeUpdate();
		pst.close();
	} 
     catch (SQLException e) 
     {
		e.printStackTrace();
	}
    }
    
    
    //execute normal query against the database
    public ResultSet ExecuteQuery(String sql)
    {
    	try 
    	{
    		
			rset = st.executeQuery(sql);
			
		}
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
    	return rset;
    }
    
    //execute update query
    public ResultSet ExecuteUpdate(String sql)
    {
    	try 
    	{
    		
			st.executeUpdate(sql);
			
		}
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
    	return rset;
    }

}
