import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


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
    	    //System.out.println(prop.getProperty("host"));
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
    
   //hide the ID column from the user
    public void HideID(JTable table)
    {    	
		table.getColumnModel().getColumn(0).setWidth(0);
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);     	
    }
    
    //Draw the employee table to the JTable
    public DefaultTableModel DrawEmployeeTable()
    {
    	//set table headers for employees
    	DefaultTableModel model = new DefaultTableModel(new String[]{"id","Employee Name", "Employee Salary", "Employee Phone Number"}, 0);
		ResultSet rs = ExecuteQuery("SELECT * FROM employee_table");
		try {
			//fetch all results from employee_table and add them to the table
			while(rs.next())
			{
				int id = rs.getInt("id");
			    String a = rs.getString("employee_name");
			    int b = rs.getInt("employee_salary");
			    int c = rs.getInt("employee_phone");
			    model.addRow(new Object[]{id, a, b, c});   
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return model;	
    }
    
    
    //Draw Products Table
    public DefaultTableModel DrawProductsTable()
    {    	
    	//set table headers for employees
    	DefaultTableModel model = new DefaultTableModel(new String[]{"id","Product Name", "Product Price", "Product Rating"}, 0);
		ResultSet rs = ExecuteQuery("SELECT * FROM products_table");
		try {
			//fetch all results from employee_table and add them to the table
			while(rs.next())
			{
				int id = rs.getInt("id");
			    String a = rs.getString("product_name");
			    int b = rs.getInt("product_price");
			    int c = rs.getInt("product_rating");
			    model.addRow(new Object[]{id, a, b, c});
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return model;	
    }
    
    //Draw Pets Table
    
    public DefaultTableModel DrawPetsTable()
    {    	
    	//set table headers for employees
    	DefaultTableModel model = new DefaultTableModel(new String[]{"id","Pet Name", "Pet Species", "Pet Breed", "Pet Age"}, 0);
		ResultSet rs = ExecuteQuery("SELECT * FROM pets_table");
		try {
			//fetch all results from employee_table and add them to the table
			while(rs.next())
			{
				int id = rs.getInt("id");
			    String a = rs.getString("pet_name");
			    String b = rs.getString("pet_species");
			    String c = rs.getString("pet_breed");
			    int d = rs.getInt("pet_age");
			    model.addRow(new Object[]{id, a, b, c, d}); 
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return model;	
    }
    
    //do search
    public DefaultTableModel SearchField(String table_name, String column_name, String searchFor)
    {
    	String sql = "SELECT * FROM " + table_name + " WHERE " + column_name + " LIKE " + "\"" + searchFor + "%\"";    	
    	ResultSet rs = ExecuteQuery(sql);
    	//if its employee table return according JTable model
    	try
    	{
    	if(table_name == "employee_table")
    	{
    		DefaultTableModel model = new DefaultTableModel(new String[]{"id","Employee Name", "Employee Salary", "Employee Phone Number"}, 0);
    		
    			//fetch all results from employee_table and add them to the table
    			while(rs.next())
    			{
    				int id = rs.getInt("id");
    			    String a = rs.getString("employee_name");
    			    String b = rs.getString("employee_salary");
    			    String c = rs.getString("employee_phone");
    			    model.addRow(new Object[]{id, a, b, c}); 
    			}
    		return model;	
    	}
    	
    	//if its products table return according JTable model.
    	if(table_name == "products_table")
    	{
    		DefaultTableModel model = new DefaultTableModel(new String[]{"id","Product Name", "Product Price", "Product Rating"}, 0);
    		
    			//fetch all results from employee_table and add them to the table
    			while(rs.next())
    			{
    				int id = rs.getInt("id");
    			    String a = rs.getString("product_name");
    			    int b = rs.getInt("product_price");
    			    int c = rs.getInt("product_rating");
    			    model.addRow(new Object[]{id, a, b, c}); 
    			}
    		return model;	
    	}
    	
    	//
    	if(table_name == "pets_table")
    	{
    		DefaultTableModel model = new DefaultTableModel(new String[]{"id","Pet Name", "Pet Species", "Pet Breed", "Pet Age"}, 0);
    		
    			//fetch all results from employee_table and add them to the table
    			while(rs.next())
    			{
    				int id = rs.getInt("id");
    			    String a = rs.getString("pet_name");
    			    String b = rs.getString("pet_species");
    			    String c = rs.getString("pet_breed");
    			    int d = rs.getInt("pet_age");
    			    model.addRow(new Object[]{id, a, b, c, d}); 
    			}
    		return model;	
    	}
    	
    	}
    	 catch (SQLException ex) {
 			ex.printStackTrace();}
    	
    	//
    	
    	return new DefaultTableModel();
    }
    
}
