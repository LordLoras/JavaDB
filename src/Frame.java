import java.awt.Event;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;




public class Frame {

	private JFrame frame;
	private JTable table;
	JComboBox comboBox = new JComboBox();	
	DBManager manager = new DBManager();
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 910, 672);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			//get the id of currently selected item and delete the currently selected row based on the ID and table		
			int id = 0;
			//if the user selected row set id to the rows ID
			if(table.getSelectedRow() != -1)
			{
				id = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
			}
			
			//check if a row has been selected and delete the entry from the database depending on combobox selection on button click
			if(comboBox.getSelectedItem() == "Products" && table.getSelectedRow() != -1)
			{
			System.out.println("selected");
			//manager.ExecuteUpdate("DELETE FROM products_table WHERE id=\"" + id + "\"");			
			}
			if(comboBox.getSelectedItem() == "Employees" && table.getSelectedRow() != -1)
			{
				manager.ExecuteUpdate("DELETE FROM employee_table WHERE id=\"" + id + "\"");			
			}								
			if(comboBox.getSelectedItem() == "Pets" && table.getSelectedRow() != -1)	
			{
				manager.ExecuteUpdate("DELETE FROM pets_table WHERE id=\"" + id + "\"");	
			}
			

		   
		}});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDelete.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(btnDelete);		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(109, 15, 762, 607);
		frame.getContentPane().add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		table.getDefaultEditor(String.class).addCellEditorListener(new CellEditorListener() {
			
			@Override
			public void editingStopped(ChangeEvent e) {
				//select id of the row that has stopped being edited and push the update to the db
			    int id =  Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
				String EmployeeName = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);
				int EmployeeSalary = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
				int EmployeePhone =  Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
				if(comboBox.getSelectedItem() == "Employees")
				{
					manager.UpdateEmployee(EmployeeName, EmployeeSalary, EmployeePhone, id);					
				}
				
				//handle Products
				String ProductName = (String) table.getModel().getValueAt(table.getSelectedRow(), 1);
				int ProductPrice = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
				int ProductRating =  Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
				if(comboBox.getSelectedItem() == "Products")
				{
					manager.UpdateProducts(ProductName, ProductPrice, ProductRating, id);				
				}
				
				
			}
			
			@Override
			public void editingCanceled(ChangeEvent e) {
				//System.out.println("cancel");
			}
		});
		
		
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				//triggers on selecting employees
				if(e.getStateChange() == ItemEvent.SELECTED && comboBox.getSelectedItem() == "Employees") 
				{                  
					//set table headers for employees
                    DefaultTableModel model = new DefaultTableModel(new String[]{"id","Employee Name", "Employee Salary", "Employee Phone Number"}, 0);
    				ResultSet rs = manager.ExecuteQuery("SELECT * FROM employee_table");
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
    				table.setModel(model);
    				//hide the ID column from the user
    				table.getColumnModel().getColumn(0).setWidth(0);
    				table.getColumnModel().getColumn(0).setMinWidth(0);
    				table.getColumnModel().getColumn(0).setMaxWidth(0); 
                    
                }
				
				//triggers on selecting products
				if(e.getStateChange() == ItemEvent.SELECTED && comboBox.getSelectedItem() == "Products") 
				{
					DefaultTableModel model = new DefaultTableModel(new String[]{"id","Product Name", "Product Price", "Product Rating"}, 0);
    				ResultSet rs = manager.ExecuteQuery("SELECT * FROM products_table");
    				try {
    					//fetch all results from products_table and add them to the table
    					while(rs.next())
    					{
    						int id = rs.getInt("id");
    					    String a = rs.getString("product_name");
    					    int b = rs.getInt("product_price");
    					    int c = rs.getInt("product_rating");
    					    model.addRow(new Object[]{id,a, b, c});
    					}
    				}
    				catch (SQLException ex) 
    				{
    					// TODO Auto-generated catch block
    					ex.printStackTrace();
    				}
    				table.setModel(model);
    				//hide the ID column from the user
    				table.getColumnModel().getColumn(0).setWidth(0);
    				table.getColumnModel().getColumn(0).setMinWidth(0);
    				table.getColumnModel().getColumn(0).setMaxWidth(0); 
    				table.setModel(model);
				}
				
				
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Employees", "Products", "Pets"}));
		comboBox.setBounds(10, 45, 89, 20);
		frame.getContentPane().add(comboBox);
	}
}
