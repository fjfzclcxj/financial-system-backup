import java.awt.EventQueue;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;



import com.mysql.jdbc.Connection;

public class Voucher {

	JFrame frame;
	private JTable tableOrder;
	private JTable tableInventory;
	private JTable tableTrans;
	private JTable tableDeposit;
	private JTable tableSale;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Voucher window = new Voucher();
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
	public Voucher(final DBConnect dbconn, int UserNo, JTable accTable) {
		initialize(dbconn, UserNo, accTable);
	}
	public Voucher() {
		initialize(null, -1, null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final DBConnect dbconn, final int UserNo, final JTable accTable) {
		//Check if dbconn is null and UserNo is -1; if so, not connect to the database or not login yet
		frame = new JFrame();
		frame.setLocation(10,100);
		frame.setBounds(100, 100, 1000, 465);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JTabbedPane Voucher = new JTabbedPane(JTabbedPane.TOP);
		Voucher.setBounds(10, 71, 964, 310);
		frame.getContentPane().add(Voucher);
		
		JScrollPane scrollOrder = new JScrollPane();
		Voucher.addTab("Order", null, scrollOrder, null);
		
		Vector o = new Vector();
		Vector i = new Vector();
		Vector t = new Vector();
		Vector d = new Vector();
		Vector s = new Vector();
		
		try 
		{	
			String sql = "SELECT DISTINCT source from voucher";
			ResultSet rs = dbconn.Run(dbconn.conn, sql);
			while(rs.next())
			{
				String source = rs.getString(1);
				String no = source.substring(1);
				switch(source.charAt(0))
				{
					case 'o': o.add(no); break;
					case 'i': i.add(no); break;
					case 't': t.add(no); break;
					case 'd': d.add(no); break;
					case 's': s.add(no); break;
					default: ;
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tableOrder = new JTable();
		/*tableOrder.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, "", null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"No.", "OrderNo.", "ItemNo.", "SupplierNo.", "Types of Payment", "Quantity", "Price", "Amount", "OrderDate", "ReceiptDate", "Recorded"
			}
		) 
		*/
		DefaultTableModel model_Order = new DefaultTableModel(null, new String[] {"OrderNo.", "ItemNo.", "SupplierNo.", "Types of Payment", "Quantity", "Price", "Amount", "ReceiptDate", "Recorded"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tableOrder.setModel(model_Order);
		tableOrder.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableOrder.getColumnModel().getColumn(4).setPreferredWidth(100);
		scrollOrder.setViewportView(tableOrder);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		scrollOrder.setColumnHeaderView(chckbxNewCheckBox);
		
		//Get all information from Table ???
		try 
		{	
			//Perhaps need to connect to another database, use SQL
//			ResultSet rs = dbconn.Query(dbconn.conn, "test_supplier_order"); //using test data
			ResultSet rs = dbconn.Query(dbconn.conn, "supplier_order");
			while(rs.next())
			{
				String osno = rs.getString(1);
				int i1 = 0;
				for (i1 = 0;i1 < o.size();i1++)
				{
					if (osno.equals(o.elementAt(i1))) break;
				}
				if (i1>=o.size())
				{
					String ino = rs.getString(2);
					int quantity = rs.getInt(3);
//					Timestamp receiptdate = rs.getTimestamp(4);
					Date receiptdate = rs.getDate(4);
					int price = rs.getInt(5);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					String sprice = decimalFormat.format(price);
					String sno = rs.getString(6);
					int amount = price * quantity;
					//DecimalFormat decimalFormat=new DecimalFormat(".00");
					String samount = decimalFormat.format(amount);
					String spayment = rs.getString(8);
					Vector a =  new Vector();//Use Class Vector to contain information
					a.add(osno);
					a.add(ino);
					a.add(sno);
					a.add(spayment);
					a.add(quantity);
					a.add(sprice);
					a.add(samount);
					a.add(receiptdate);
					a.add(false);
					//if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
					//if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
					if (!spayment.contains("供应商库存")) model_Order.addRow(a);
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JScrollPane scrollInventory = new JScrollPane();
		Voucher.addTab("Inventory", null, scrollInventory, null);
		
		tableInventory = new JTable();
		/*tableInventory.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"No.", "InventoryNo.", "Inventory Expense", "Date", "Recorded"
			}
		) 
		*/
		DefaultTableModel model_Inventory = new DefaultTableModel(null, new String[] {"InventoryNo.", "Inventory Expense", "Date", "Recorded"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tableInventory.setModel(model_Inventory);
		scrollInventory.setViewportView(tableInventory);
		
		//Get all information from Table ???
		try 
		{	
			//Perhaps need to connect to another database, use SQL
//			ResultSet rs = dbconn.Query(dbconn.conn, "test_store_cost"); //using test data
			ResultSet rs = dbconn.Query(dbconn.conn, "store_cost");
			while(rs.next())
			{
				String scno = rs.getString(1);
				int i1 = 0;
				for (i1 = 0;i1 < i.size();i1++)
				{
					if (scno.equals(i.elementAt(i1))) break; 
				}
				if (i1>=i.size())
				{
					int cost = rs.getInt(2);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					String scost = decimalFormat.format(cost);
					Date date = rs.getDate(3);
					Vector a =  new Vector();//Use Class Vector to contain information
					a.add(scno);
					a.add(scost);
					a.add(date);
					a.add(false);
					//if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
					//if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
					model_Inventory.addRow(a);
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JScrollPane scrollTrans = new JScrollPane();
		Voucher.addTab("Transportation", null, scrollTrans, null);
		
		tableTrans = new JTable();
		/*
		tableTrans.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"No.", "TransNo.", "Transportation Expense", "Date", "Recorded"
			}
		)
		*/
		DefaultTableModel model_Trans = new DefaultTableModel(null, new String[] {"TransNo.", "Transportation Expense", "Date", "Recorded"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tableTrans.setModel(model_Trans);
		scrollTrans.setViewportView(tableTrans);
		
		//Get all information from Table ???
		try 
		{	
			//Perhaps need to connect to another database, use SQL
//			ResultSet rs = dbconn.Query(dbconn.conn, "test_trans_cost"); //using test data
			ResultSet rs = dbconn.Query(dbconn.conn, "trans_cost");
			while(rs.next())
			{
				String tcno = rs.getString(1);
				int i1 = 0;
				for (i1 = 0;i1 < t.size();i1++)
				{
					if (tcno.equals(t.elementAt(i1))) break; 
				}
				if (i1>=t.size())
				{
					int cost = rs.getInt(2);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					String scost = decimalFormat.format(cost);
					Date date = rs.getDate(3);
					Vector a =  new Vector();//Use Class Vector to contain information
					a.add(tcno);
					a.add(scost);
					a.add(date);
					a.add(false);
					//if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
					//if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
					model_Trans.addRow(a);
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JScrollPane scrollDeposit = new JScrollPane();
		Voucher.addTab("Deposit", null, scrollDeposit, null);
		
		tableDeposit = new JTable();
		/*
		tableDeposit.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"No.", "DepositNo.", "CustomerNo.", "Amount", "Date", "Recorded"
			}
		)
		*/ 
		DefaultTableModel model_Deposit = new DefaultTableModel(null, new String[] {"DepositNo.", "CustomerNo.", "Amount", "Date", "Recorded"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tableDeposit.setModel(model_Deposit);
		scrollDeposit.setViewportView(tableDeposit);
		
		//Get all information from Table ???
		try 
		{	
			//Perhaps need to connect to another database, use SQL
//			ResultSet rs = dbconn.Query(dbconn.conn, "test_deposit"); //using test data
			ResultSet rs = dbconn.Query(dbconn.conn, "deposit");
			//int count = 0;
			while(rs.next())
			{
				/*
				count++;
				String strcount = count + "";
				while (strcount.length()<8)
				{
					strcount = "0" + strcount;
				}
				*/
				String dpno = rs.getString(1);
				int i1 = 0;
				for (i1 = 0;i1 < d.size();i1++)
				{
					if (dpno.equals(d.elementAt(i1))) break; 
				}
				if (i1>=d.size())
				{
					String cno = rs.getString(2);
					Double amount = rs.getDouble(3);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					String samount = decimalFormat.format(amount);
					Date date = rs.getDate(4);
					Vector a =  new Vector();//Use Class Vector to contain information
					//a.add(strcount);
					a.add(dpno);
					a.add(cno);
					a.add(samount);
					a.add(date);
					a.add(false);
					//if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
					//if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
					model_Deposit.addRow(a);
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JScrollPane scrollSale = new JScrollPane();
		Voucher.addTab("Sale", null, scrollSale, null);
		
		tableSale = new JTable();
		/*
		tableSale.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"No.", "SaleNo.", "CustomerNo.", "Sales", "SaleDate", "Types of Payment", "ItemNo.", "Item Cost", "Quantity", "SupplierInventory", "Recorded"
			}
		) 
		*/
		DefaultTableModel model_Sale = new DefaultTableModel(null, new String[] {"SaleNo.", "CustomerNo.", "Quantity", "Cost", "Sales", "SaleDate", "Types of Payment", "ItemNo.", "SupplierNo.", "SupplierInventory", "Recorded"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tableSale.setModel(model_Sale);
		tableSale.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableSale.getColumnModel().getColumn(5).setPreferredWidth(110);
		tableSale.getColumnModel().getColumn(9).setPreferredWidth(100);
		scrollSale.setViewportView(tableSale);
		
		//Get all information from Table ???
		try 
		{	
//			ResultSet rs = dbconn.Query(dbconn.conn, "test_client_order"); //using test data
			ResultSet rs = dbconn.Query(dbconn.conn, "client_order");
			while(rs.next())
			{
				String orno = rs.getString(1);
				int i1 = 0;
				for (i1 = 0;i1 < s.size();i1++)
				{
					if (orno.equals(s.elementAt(i1))) break; 
				}
				if (i1>=s.size())
				{
					String cno = rs.getString(2);
					String ino = rs.getString(3);
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					int quantity = rs.getInt(4);
//					int price = rs.getInt(5);
					double price = rs.getDouble(5);
					String samount = decimalFormat.format(quantity*price);
					Date date = rs.getDate(6);
					int cost = rs.getInt(8);
					String scost = decimalFormat.format(quantity*cost);
					String payment = rs.getString(7);
					String sno = rs.getString(9);
					int si = rs.getInt(12);
					Vector a =  new Vector();//Use Class Vector to contain information
					a.add(orno);
					a.add(cno);
					a.add(quantity);
					a.add(scost);
					a.add(samount);
					a.add(date);
					a.add(payment);
					a.add(ino);
					a.add(sno);
					a.add(si);
					a.add(false);
					//if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
					//if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
					model_Sale.addRow(a);
				}
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lblVoucher = new JLabel("Voucher");
		lblVoucher.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblVoucher.setBounds(474, 21, 97, 15);
		frame.getContentPane().add(lblVoucher);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				System.exit(0);
			}
		});
		btnExit.setBounds(823, 393, 93, 23);
		frame.getContentPane().add(btnExit);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Account account=new Account(dbconn, UserNo);
				//account.frame.setVisible(true);
				
//				DefaultTableModel accmodel = (DefaultTableModel) accTable.getModel();
//				accTable.setModel(new DefaultTableModel(
//						new Object[][] {
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//							{"", null, null, null, null, null, null, null, null, null},
//						},
//						new String[] {
//							"Account", "SupplierNo.", "CustomerNo.", "ProductNo.", "Amount", "D/C", "Description", "Date", "Source"
//						}
//					) {
//						Class[] columnTypes = new Class[] {
//							String.class, String.class, String.class, String.class, String.class, /*Float.class, Integer.class,*/String.class, String.class, Date.class, String.class
//						};
//						public Class getColumnClass(int columnIndex) {
//							return columnTypes[columnIndex];
//						}
//					});
//
//					//tableSale.setModel(model);
//					accTable.getColumnModel().getColumn(6).setPreferredWidth(50); //clear then import
//					JComboBox box = new JComboBox();  
//			        box.addItem("Debit");  
//			        box.addItem("Credit");  
//			        TableColumn d = accTable.getColumn("D/C");  
//			        DefaultCellEditor dce = new DefaultCellEditor(box);   
//			        d.setCellEditor(dce);
//			        accTable.getColumnModel().getColumn(7).setCellEditor(MyButtonEditor());
					
				int curTab = Voucher.getSelectedIndex();
				System.out.print(" " + curTab);
				switch (curTab)
				{
					case 0:
						
						int curOrder = tableOrder.getSelectedRow();
						//1	
						String voucherno = "   ";
						//accTable.setValueAt(voucherno, 0, 0);
						String ino = (String) tableOrder.getValueAt(curOrder, 1);
						String account = "库存-" + ino;
						accTable.setValueAt(account, 0, 0);
						String supplierno = (String) tableOrder.getValueAt(curOrder, 2);
						accTable.setValueAt(supplierno, 0, 1);
						String customerno = null;
						accTable.setValueAt(customerno, 0, 2);
						String productno = ino;
						accTable.setValueAt(productno, 0, 3);
						String samount = (String) tableOrder.getValueAt(curOrder, 6);
						accTable.setValueAt(samount, 0, 4);
						String dc = "Debit";
						accTable.setValueAt(dc, 0, 5);
						String description = "";
						Date date = (Date) tableOrder.getValueAt(curOrder, 7);
						accTable.setValueAt(date, 0, 7);
						String source = "o" + (String) tableOrder.getValueAt(curOrder, 0);
						accTable.setValueAt(source, 0, 8);
						String spayment = (String) tableOrder.getValueAt(curOrder, 3);
						if (spayment.contains("一年付一次"))
						{
							description = "一级供应商，一年后付款，当天记应付账款";
							accTable.setValueAt(description, 0, 6);
							//2
							//accTable.setValueAt(voucherno, 1, 0);
							account = "应付账款-" + supplierno;
							accTable.setValueAt(account, 1, 0);
							accTable.setValueAt(supplierno, 1, 1);
							accTable.setValueAt(customerno, 1, 2);
							accTable.setValueAt(productno, 1, 3);
							accTable.setValueAt(samount, 1, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 1, 5);
							accTable.setValueAt(description, 1, 6);
							accTable.setValueAt(date, 1, 7);
							accTable.setValueAt(source, 1, 8);
							//3
							//accTable.setValueAt(voucherno, 2, 0);
							accTable.setValueAt(account, 2, 0);
							accTable.setValueAt(supplierno, 2, 1);
							accTable.setValueAt(customerno, 2, 2);
							accTable.setValueAt(productno, 2, 3);
							accTable.setValueAt(samount, 2, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 2, 5);
							description = "一级供应商，一年后的付款，对应采购单";
							accTable.setValueAt(description, 2, 6);
							Calendar cal = Calendar.getInstance();
							cal.clear();
							cal.set(Calendar.YEAR, date.getYear()+1900);  
					        cal.roll(Calendar.DAY_OF_YEAR, -1);  
					        Date lastDate = cal.getTime();
							accTable.setValueAt(lastDate, 2, 7);
							accTable.setValueAt(source, 2, 8);
							//4
							//accTable.setValueAt(voucherno, 3, 0);
							account = "银行存款";
							accTable.setValueAt(account, 3, 0);
							accTable.setValueAt(supplierno, 3, 1);
							accTable.setValueAt(customerno, 3, 2);
							accTable.setValueAt(productno, 3, 3);
							accTable.setValueAt(samount, 3, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 3, 5);
							description = "一级供应商，一年后的付款，对应采购单";
							accTable.setValueAt(description, 3, 6);
							accTable.setValueAt(lastDate, 3, 7);
							accTable.setValueAt(source, 3, 8);
						}
						
						tableOrder.setValueAt(true, curOrder, 8);
						break;
					case 1:
						//1
						int curInventory = tableInventory.getSelectedRow();
						voucherno = "   ";
						//accTable.setValueAt(voucherno, 0, 0);
						account = "仓储费用";
						accTable.setValueAt(account, 0, 0);
						supplierno = null;
						accTable.setValueAt(supplierno, 0, 1);
						customerno = null;
						accTable.setValueAt(customerno, 0, 2);
						productno =  null;
						accTable.setValueAt(productno, 0, 3);
						samount = (String) tableInventory.getValueAt(curInventory, 1);
						accTable.setValueAt(samount, 0, 4);
						dc = "Debit";
						accTable.setValueAt(dc, 0, 5);
						description = "当天仓储成本";
						accTable.setValueAt(description, 0, 6);
						date = (Date) tableInventory.getValueAt(curInventory, 2);
						accTable.setValueAt(date, 0, 7);
						source = "i" + (String) tableInventory.getValueAt(curInventory, 0);
						accTable.setValueAt(source, 0, 8);
						//2
						//accTable.setValueAt(voucherno, 1, 0);
						account = "应付租金";
						accTable.setValueAt(account, 1, 0);
						accTable.setValueAt(supplierno, 1, 1);
						accTable.setValueAt(customerno, 1, 2);
						accTable.setValueAt(productno, 1, 3);
						accTable.setValueAt(samount, 1, 4);
						dc = "Credit";
						accTable.setValueAt(dc, 1, 5);
						description = "当天仓储成本";
						accTable.setValueAt(description, 1, 6);
						accTable.setValueAt(date, 1, 7);
						accTable.setValueAt(source, 1, 8);
						//3
						//accTable.setValueAt(voucherno, 2, 0);
						accTable.setValueAt(account, 2, 0);
						accTable.setValueAt(supplierno, 2, 1);
						accTable.setValueAt(customerno, 2, 2);
						accTable.setValueAt(productno, 2, 3);
						accTable.setValueAt(samount, 2, 4);
						dc = "Debit";
						accTable.setValueAt(dc, 2, 5);
						description = "月末结清仓储成本";
						accTable.setValueAt(description, 2, 6);
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.YEAR, date.getYear()+1900);
						cal.set(Calendar.MONTH, date.getMonth()+1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.add(Calendar.DAY_OF_MONTH, -1);
						Date lastDate = cal.getTime();
						accTable.setValueAt(lastDate, 2, 7);
						accTable.setValueAt(source, 2, 8);
						//4
						//accTable.setValueAt(voucherno, 3, 0);
						account = "银行存款";
						accTable.setValueAt(account, 3, 0);
						accTable.setValueAt(supplierno, 3, 1);
						accTable.setValueAt(customerno, 3, 2);
						accTable.setValueAt(productno, 3, 3);
						accTable.setValueAt(samount, 3, 4);
						dc = "Credit";
						accTable.setValueAt(dc, 3, 5);
						description = "月末结清仓储成本";
						accTable.setValueAt(description, 3, 6);
						accTable.setValueAt(lastDate, 3, 7);
						accTable.setValueAt(source, 3, 8);
						
						tableInventory.setValueAt(true, curInventory, 3);
						break;
					case 2:
						//1
						int curTrans = tableTrans.getSelectedRow();
						voucherno = "   ";
						//accTable.setValueAt(voucherno, 0, 0);
						account = "运输费用";
						accTable.setValueAt(account, 0, 0);
						supplierno = null;
						accTable.setValueAt(supplierno, 0, 1);
						customerno = null;
						accTable.setValueAt(customerno, 0, 2);
						productno =  null;
						accTable.setValueAt(productno, 0, 3);
						samount = (String) tableTrans.getValueAt(curTrans, 1);
						accTable.setValueAt(samount, 0, 4);
						dc = "Debit";
						accTable.setValueAt(dc, 0, 5);
						description = "当天运输成本";
						accTable.setValueAt(description, 0, 6);
						date = (Date) tableTrans.getValueAt(curTrans, 2);
						accTable.setValueAt(date, 0, 7);
						source = "t" + (String) tableTrans.getValueAt(curTrans, 0);
						accTable.setValueAt(source, 0, 8);
						//2
						//accTable.setValueAt(voucherno, 1, 0);
						account = "银行存款";
						accTable.setValueAt(account, 1, 0);
						accTable.setValueAt(supplierno, 1, 1);
						accTable.setValueAt(customerno, 1, 2);
						accTable.setValueAt(productno, 1, 3);
						accTable.setValueAt(samount, 1, 4);
						dc = "Credit";
						accTable.setValueAt(dc, 1, 5);
						description = "当天运输成本";
						accTable.setValueAt(description, 1, 6);
						accTable.setValueAt(date, 1, 7);
						accTable.setValueAt(source, 1, 8);
						
						tableTrans.setValueAt(true, curTrans, 3);
						break;
					case 3:
						//1
						int curDeposit = tableDeposit.getSelectedRow();
						voucherno = "   ";
						//accTable.setValueAt(voucherno, 0, 0);
						String cno = (String) tableDeposit.getValueAt(curDeposit, 1);
						account = "银行存款";
						accTable.setValueAt(account, 0, 0);
						supplierno = null;
						accTable.setValueAt(supplierno, 0, 1);
						customerno = cno;
						accTable.setValueAt(customerno, 0, 2);
						productno =  null;
						accTable.setValueAt(productno, 0, 3);
						samount = (String) tableDeposit.getValueAt(curDeposit, 2);
						accTable.setValueAt(samount, 0, 4);
						dc = "Debit";
						accTable.setValueAt(dc, 0, 5);
						description = "客户预存资金";
						accTable.setValueAt(description, 0, 6);
						date = (Date) tableDeposit.getValueAt(curDeposit, 3);
						accTable.setValueAt(date, 0, 7);
						source = "d" + (String) tableDeposit.getValueAt(curDeposit, 0);
						accTable.setValueAt(source, 0, 8);
						//2
						//accTable.setValueAt(voucherno, 1, 0);
						account = "预收账款-" + cno;
						accTable.setValueAt(account, 1, 0);
						accTable.setValueAt(supplierno, 1, 1);
						accTable.setValueAt(customerno, 1, 2);
						accTable.setValueAt(productno, 1, 3);
						accTable.setValueAt(samount, 1, 4);
						dc = "Credit";
						accTable.setValueAt(dc, 1, 5);
						description = "客户预存资金";
						accTable.setValueAt(description, 1, 6);
						accTable.setValueAt(date, 1, 7);
						accTable.setValueAt(source, 1, 8);
						
						tableDeposit.setValueAt(true, curDeposit, 4);
						break;
					case 4:
						//1
						int curSale = tableSale.getSelectedRow();
						int fvmi = (Integer) tableSale.getValueAt(curSale, 9);
						voucherno = "   ";
						samount = (String) tableSale.getValueAt(curSale, 4);
						String scost = (String) tableSale.getValueAt(curSale, 3);
						date = (Date) tableSale.getValueAt(curSale, 5);
						cal = Calendar.getInstance();
						cal.set(Calendar.YEAR, date.getYear()+1900);
						cal.set(Calendar.MONTH, date.getMonth()+1);
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.add(Calendar.DAY_OF_MONTH, -1);
						lastDate = cal.getTime();
						source = "s" + (String) tableSale.getValueAt(curSale, 0);
						customerno = (String) tableSale.getValueAt(curSale, 1);
						productno = (String) tableSale.getValueAt(curSale, 7);
						supplierno = (String) tableSale.getValueAt(curSale, 8); //Cannot access!!!!!!!!!!!!!!!
						if (fvmi==0)
						{
							description = "客户订单，预存资金付款";
							spayment = (String) tableSale.getValueAt(curSale, 6);
							if (spayment.contains("预存资金")) account = "预收账款-" + customerno;
							else 
							{
								account = "银行存款";
								description = "客户订单，" + spayment + "付款";
							}
							//1
							//accTable.setValueAt(voucherno, 0, 0);
							accTable.setValueAt(account, 0, 0);
							accTable.setValueAt(supplierno, 0, 1);
							accTable.setValueAt(customerno, 0, 2);
							accTable.setValueAt(productno, 0, 3);
							accTable.setValueAt(samount, 0, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 0, 5);
							accTable.setValueAt(description, 0, 6);
							accTable.setValueAt(date, 0, 7);
							accTable.setValueAt(source, 0, 8);
							//2
							//accTable.setValueAt(voucherno, 1, 0);
							account = "营业收入-" + productno;
							accTable.setValueAt(account, 1, 0);
							accTable.setValueAt(supplierno, 1, 1);
							accTable.setValueAt(customerno, 1, 2);
							accTable.setValueAt(productno, 1, 3);
							accTable.setValueAt(samount, 1, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 1, 5);
							accTable.setValueAt(description, 1, 6);
							accTable.setValueAt(date, 1, 7);
							accTable.setValueAt(source, 1, 8);
							//3
							//accTable.setValueAt(voucherno, 2, 0);
							account = "营业成本-" + productno;
							accTable.setValueAt(account, 2, 0);
							accTable.setValueAt(supplierno, 2, 1);
							accTable.setValueAt(customerno, 2, 2);
							accTable.setValueAt(productno, 2, 3);
							accTable.setValueAt(scost, 2, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 2, 5);
							description = "减少库存，非供应商库存";
							accTable.setValueAt(description, 2, 6);
							accTable.setValueAt(date, 2, 7);
							accTable.setValueAt(source, 2, 8);
							//4
							//accTable.setValueAt(voucherno, 3, 0);
							account = "库存-" + productno;
							accTable.setValueAt(account, 3, 0);
							accTable.setValueAt(supplierno, 3, 1);
							accTable.setValueAt(customerno, 3, 2);
							accTable.setValueAt(productno, 3, 3);
							accTable.setValueAt(scost, 3, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 3, 5);
							accTable.setValueAt(description, 3, 6);
							accTable.setValueAt(date, 3, 7);
							accTable.setValueAt(source, 3, 8);
						}
						else
						{
							description = "客户订单，预存资金付款";
							spayment = (String) tableSale.getValueAt(curSale, 6);
							if (spayment.contains("预存资金")) account = "预收账款-" + customerno;
							else 
							{
								account = "银行存款";
								description = "客户订单，" + spayment + "付款";
							}
							//1
							//accTable.setValueAt(voucherno, 0, 0);
							accTable.setValueAt(account, 0, 0);
							accTable.setValueAt(supplierno, 0, 1);
							accTable.setValueAt(customerno, 0, 2);
							accTable.setValueAt(productno, 0, 3);
							accTable.setValueAt(samount, 0, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 0, 5);
							accTable.setValueAt(description, 0, 6);
							accTable.setValueAt(date, 0, 7);
							accTable.setValueAt(source, 0, 8);
							//2
							//accTable.setValueAt(voucherno, 1, 0);
							account = "营业收入-" + productno;
							accTable.setValueAt(account, 1, 0);
							accTable.setValueAt(supplierno, 1, 1);
							accTable.setValueAt(customerno, 1, 2);
							accTable.setValueAt(productno, 1, 3);
							accTable.setValueAt(samount, 1, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 1, 5);
							accTable.setValueAt(description, 1, 6);
							accTable.setValueAt(date, 1, 7);
							accTable.setValueAt(source, 1, 8);
							//3
							//accTable.setValueAt(voucherno, 2, 0);
							account = "库存-" + productno;
							accTable.setValueAt(account, 2, 0);
							accTable.setValueAt(supplierno, 2, 1);
							accTable.setValueAt(customerno, 2, 2);
							accTable.setValueAt(productno, 2, 3);
							accTable.setValueAt(scost, 2, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 2, 5);
							description = "供应商库存，转化为库存";
							accTable.setValueAt(description, 2, 6);
							accTable.setValueAt(date, 2, 7);
							accTable.setValueAt(source, 2, 8);
							//4
							//accTable.setValueAt(voucherno, 3, 0);
							account = "应付账款-" + supplierno;
							accTable.setValueAt(account, 3, 0);
							accTable.setValueAt(supplierno, 3, 1);
							accTable.setValueAt(customerno, 3, 2);
							accTable.setValueAt(productno, 3, 3);
							accTable.setValueAt(scost, 3, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 3, 5);
							accTable.setValueAt(description, 3, 6);
							accTable.setValueAt(date, 3, 7);
							accTable.setValueAt(source, 3, 8);
							//5
							//accTable.setValueAt(voucherno, 4, 0);
							accTable.setValueAt(account, 4, 0);
							accTable.setValueAt(supplierno, 4, 1);
							accTable.setValueAt(customerno, 4, 2);
							accTable.setValueAt(productno, 4, 3);
							accTable.setValueAt(scost, 4, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 4, 5);
							description = "月末按卖了多少付款给供应商";
							accTable.setValueAt(description, 4, 6);
							accTable.setValueAt(lastDate, 4, 7);
							accTable.setValueAt(source, 4, 8);
							//6
							//accTable.setValueAt(voucherno, 5, 0);
							account = "银行存款";
							accTable.setValueAt(account, 5, 0);
							accTable.setValueAt(supplierno, 5, 1);
							accTable.setValueAt(customerno, 5, 2);
							accTable.setValueAt(productno, 5, 3);
							accTable.setValueAt(scost, 5, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 5, 5);
							accTable.setValueAt(description, 5, 6);
							accTable.setValueAt(lastDate, 5, 7);
							accTable.setValueAt(source, 5, 8);
							//7
							//accTable.setValueAt(voucherno, 6, 0);
							account = "营业成本-" + productno;
							accTable.setValueAt(account, 6, 0);
							accTable.setValueAt(supplierno, 6, 1);
							accTable.setValueAt(customerno, 6, 2);
							accTable.setValueAt(productno, 6, 3);
							accTable.setValueAt(scost, 6, 4);
							dc = "Debit";
							accTable.setValueAt(dc, 6, 5);
							description = "减少库存，供应商库存";
							accTable.setValueAt(description, 6, 6);
							accTable.setValueAt(date, 6, 7);
							accTable.setValueAt(source, 6, 8);
							//8
							//accTable.setValueAt(voucherno, 7, 0);
							account = "库存-" + productno;
							accTable.setValueAt(account, 7, 0);
							accTable.setValueAt(supplierno, 7, 1);
							accTable.setValueAt(customerno, 7, 2);
							accTable.setValueAt(productno, 7, 3);
							accTable.setValueAt(scost, 7, 4);
							dc = "Credit";
							accTable.setValueAt(dc, 7, 5);
							accTable.setValueAt(description, 7, 6);
							accTable.setValueAt(date, 7, 7);
							accTable.setValueAt(source, 7, 8);
						}
						tableSale.setValueAt(true, curSale, 10);
						break;
					default:;
				}
			}
		});
		btnImport.setBounds(581, 393, 93, 23);
		frame.getContentPane().add(btnImport);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Account account=new Account(dbconn, UserNo);
				//account.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnBack.setBounds(702, 393, 93, 23);
		frame.getContentPane().add(btnBack);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"All", "Recorded", "Unrecorded"}));
		comboBox.setBounds(870, 50, 80, 21);
		frame.getContentPane().add(comboBox);
	}
}
