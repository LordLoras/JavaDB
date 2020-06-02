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
import javax.swing.JLabel;
import javax.swing.JTextField;




public class Frame {

	private JFrame frame;
	JTable table;
	JComboBox comboBox = new JComboBox();	
	DBManager manager = new DBManager();
	private JScrollPane scrollPane;
	private JTextField searchTextbox;

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
		//upon initialization draw employee table as default
		table.setModel(manager.DrawEmployeeTable());
		manager.HideID(table);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1212, 672);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnDelete = new JButton("DELETE");
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
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
			}
		});
		btnDelete.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(btnDelete);		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(401, 14, 762, 607);
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
					table.setModel(manager.DrawEmployeeTable());
    				manager.HideID(table);
                }
				
				//triggers on selecting products
				if(e.getStateChange() == ItemEvent.SELECTED && comboBox.getSelectedItem() == "Products") 
				{
    				table.setModel(manager.DrawProductsTable());
    				manager.HideID(table);
				}
				
				//triggers on selecting pets
				if(e.getStateChange() == ItemEvent.SELECTED && comboBox.getSelectedItem() == "Pets") 
				{
    				table.setModel(manager.DrawPetsTable());
    				manager.HideID(table);
				}
				
				
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Employees", "Products", "Pets"}));
		comboBox.setBounds(10, 45, 89, 20);
		frame.getContentPane().add(comboBox);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				table.setModel(manager.DrawEmployeeTable());
				switch(searchTextbox.getText())
				{
				case "Employee Name":
					
				
				}
			}
		});
		searchBtn.setBounds(10, 176, 89, 23);
		frame.getContentPane().add(searchBtn);
		
		JComboBox searchBox = new JComboBox();
		searchBox.setModel(new DefaultComboBoxModel(new String[] {"Employee Name", "Employee Salary", "Employee Phone", "Product Name", "Product Price", "Product Rating"}));
		searchBox.setBounds(10, 114, 168, 20);
		frame.getContentPane().add(searchBox);
		
		JLabel lblNewLabel = new JLabel("Search by");
		lblNewLabel.setBounds(10, 93, 68, 14);
		frame.getContentPane().add(lblNewLabel);
		
		searchTextbox = new JTextField();
		searchTextbox.setBounds(10, 145, 165, 20);
		frame.getContentPane().add(searchTextbox);
		searchTextbox.setColumns(10);
	}
}
