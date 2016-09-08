import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

//import java.sql.Date;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import com.mysql.jdbc.Connection;

import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableCell;

public class Account {

	JFrame frame;
	private JTable table;
	Voucher staticvoucher=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Account window = new Account();
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
	public Account(final DBConnect dbconn, int UserNo) {
		initialize(dbconn, UserNo);
	}
	public Account() {
		initialize(null, -1);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final DBConnect dbconn, final int UserNo) {
		//Check if dbconn is null and UserNo is -1; if so, not connect to the database or not login yet.
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Account");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTitle.setBounds(480, 21, 73, 15);
		frame.getContentPane().add(lblTitle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 964, 186);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Account", "SupplierNo.", "CustomerNo.", "ProductNo.", "Amount", "D/C", "Description", "Date", "Source"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, /*Float.class, Integer.class,*/String.class, String.class, Date.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});

		//tableSale.setModel(model);
		table.getColumnModel().getColumn(6).setPreferredWidth(50);
		
		JComboBox box = new JComboBox();  
        box.addItem("Debit");  
        box.addItem("Credit");  
        TableColumn d = table.getColumn("D/C");  
        DefaultCellEditor dce = new DefaultCellEditor(box);   
        d.setCellEditor(dce);
        
        ////////////////////////////////////////////////
        

        //修改表格的默认编辑器：
        table.getColumnModel().getColumn(7).setCellEditor(new MyButtonEditor());

         

//        /*
//        这样后就能基本达到效果了。但是还要注意，对列2来说，还需要启用可编辑功能，才行，不然仍然触发不了事件的。
//
//        代码片段：
//        */
//
//        public boolean isCellEditable(int row, int column)  
//        {  
//            // 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。   
//            if (column == 2)  
//            {  
//                return true;  
//            }  
//            else  
//            {  
//                return false;  
//            }  
//        } 
        
        ////////////////////////////////////////////////
                 
        scrollPane.setViewportView(table);
		
		JButton btnAcctVou = new JButton("Account Voucher");
		btnAcctVou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.setVisible(false);
				if(staticvoucher!=null)
				{
					staticvoucher.frame.dispose();//hope to close originvoucher when we close interface account
					staticvoucher=null;
				}
				
//				System.exit(0);//tryyitry
				AcctVoucher acctVoucher=new AcctVoucher(dbconn, UserNo);
				acctVoucher.frame.setVisible(true);
			}
		});
		btnAcctVou.setBounds(81, 256, 170, 23);
		frame.getContentPane().add(btnAcctVou);
		
		JButton btnOriginalVou = new JButton("Original Voucher");
		btnOriginalVou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(staticvoucher!=null)
				{
					staticvoucher.frame.dispose();//hope to close originvoucher when we close interface account
				}
				
				Voucher voucher=new Voucher(dbconn, UserNo, table);
				staticvoucher=voucher;
				
				
//				System.exit(0);//tryyitry
//				AcctVoucher acctVoucher=new AcctVoucher(dbconn, UserNo);
//				acctVoucher.frame.setVisible(true);
				
				voucher.frame.setLocation(0,350);
				voucher.frame.setVisible(true);
			}
		});
		btnOriginalVou.setBounds(302, 256, 170, 23);
		frame.getContentPane().add(btnOriginalVou);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int rowcount = table.getRowCount();
				int i=0;
				double sumd = 0, sumc = 0;
//				String voucherNo = (String) table.getValueAt(i, 0);       //account?
				String account = (String) table.getValueAt(i, 0); 
				do
				{
//					voucherNo = (String) table.getValueAt(i, 0);
					account = (String) table.getValueAt(i, 0);            //use account to go through all the rows in Account Interface
					if (account.equals("")) break;
					DecimalFormat decimalFormat=new DecimalFormat(".00");
					double amount = 0;
					String samount = (String) table.getValueAt(i, 4);
					boolean decimal = true;
					amount = Double.parseDouble(samount);
//					for (int i1=0;i1<samount.length();i1++)
//					{
//						if ((samount.charAt(i1)!='.')&&(decimal))
//						{
//							amount *= 10;
//							amount += samount.charAt(i1) - '0';
//						}
//						if (samount.charAt(i1)=='.') 
//						{
//							decimal = !decimal;
//						}
//						int t = 100;
//						if ((samount.charAt(i1)!='.')&&(!decimal))
//						{
//							amount += (double)(samount.charAt(i1) - '0')/t;
//							t *= 10;
//						}
//					}
					System.out.println("The amount is: "+amount);
					int dc = (table.getValueAt(i, 5).equals("Debit"))?0:1;
					sumc += dc==0?0:amount;
					sumd += dc==0?amount:0;
					
					i++;
				} while ((i<=rowcount)&&(!account.equals("")));
				if (sumd - sumc!=0)
					JOptionPane.showMessageDialog(null, "Unbalanced amount!","", JOptionPane.ERROR_MESSAGE);
					//System.out.print("Fuck U!");//FUUUUUUUUUUUUUUUUUUUUUCK UUUUUUUUU NOT BALANCE! but no need to clear
					
				else
				{
				
					String voucherno = "0000000000";
					try 
					{	
						String sql = "SELECT voucherNo from voucher WHERE voucherNo in (Select max(voucherNo) from voucher)";
						ResultSet rs = dbconn.Run(dbconn.conn, sql);
						//if (rs.next()) voucherno = "0";
						while(rs.next())
						{
							voucherno = rs.getString(1);
							//System.out.print(voucherno + " ");
						}
						rs.close();
					} 
					catch (SQLException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int no = 0;
					int ii;
					for (ii=0;ii<voucherno.length();ii++)
					{
						no *= 10;
						no += voucherno.charAt(ii) - '0';
					}
					//System.out.print(no); to get the value of String voucherno, then +1
					
					rowcount = table.getRowCount();
					i=0;  //use account again
					sumd = sumc = 0;
					account = (String) table.getValueAt(i, 0);
					String supplierno = (String) table.getValueAt(i, 1);  //now i=0
					String customerno = (String) table.getValueAt(i, 2);
					String productno = (String) table.getValueAt(i, 3);
					String source = (String) table.getValueAt(i, 8);
					int oddeven=0; //1 indicates odd
					do
					{
						if ((sumd - sumc==0)&&(oddeven==0)) no++; //max+1
						voucherno = no+"";
						while (voucherno.length()<10)
						{
							voucherno = "0" + voucherno;
						}
						account = (String) table.getValueAt(i, 0);     //whether we have got to the end
						if (account.equals("")) break;
//						String account = (String) table.getValueAt(i, 0);
//						String supplierno = (String) table.getValueAt(i, 1);
//						String customerno = (String) table.getValueAt(i, 2);
//						String productno = (String) table.getValueAt(i, 3);
						DecimalFormat decimalFormat=new DecimalFormat(".00");
						double amount = 0;
						String samount = (String) table.getValueAt(i, 4);
						boolean decimal = true;
						amount = Double.parseDouble(samount);
//						for (int i1=0;i1<samount.length();i1++)
//						{
//							if ((samount.charAt(i1)!='.')&&(decimal))
//							{
//								amount *= 10;
//								amount += samount.charAt(i1) - '0';
//							}
//							if (samount.charAt(i1)=='.') 
//							{
//								decimal = !decimal;
//							}
//							int t = 100;
//							if ((samount.charAt(i1)!='.')&&(!decimal))
//							{
//								amount += (double)(samount.charAt(i1) - '0')/t;
//								t *= 10;
//							}
//						}
						System.out.println("amount 2 is: "+amount);
						int dc = (table.getValueAt(i, 5).equals("Debit"))?0:1;
//						System.out.println("The amount is: "+amount);																										
						sumc += dc==0?0:amount; //debit?
						sumd += dc==0?amount:0;
						if(amount==0)
						{
							oddeven = (oddeven==0)?1:0;        //There will no be 0 -100 +100; only can be 0 0
						}
						String description = (String) table.getValueAt(i, 6);
						Date utildate=(Date) table.getValueAt(i, 7);
						java.sql.Date date=new java.sql.Date(utildate.getTime());
						System.out.println(date);
//						SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
//						String dateString=f.format(date);
//						System.out.println(dateString);
//						java.util.Date utildate = (Date) table.getValueAt(i, 7);
//						java.sql.Date date=new java.sql.Date(utildate.getTime());
//						
//						
//						System.out.println(date);
//						String source = (String) table.getValueAt(i, 8);			
						String sql_account = "INSERT INTO account values(\""+voucherno+"\",\""+account+"\",\""+amount+"\","+dc+",\""+date+"\",0)";
						String sql_voucher = "INSERT INTO voucher values(\""+voucherno+"\",\""+supplierno+"\",\""+customerno+"\",\""+productno +"\",\""+description+"\",\""+date+"\",\""+source+"\",\""+UserNo+"\")";
						
						double signedamount = (dc==0)?amount:-amount; //dc=0, debit
						String sql_balance = "INSERT INTO balance(account,balance) VALUES(\""+account+"\","+signedamount+") ON DUPLICATE KEY UPDATE balance=balance+"+signedamount;
						String sql_account_recorded="UPDATE account SET recorded=1 WHERE voucherNo=\""+voucherno+"\" and account=\""+account+"\"";
						dbconn.Update(dbconn.conn, sql_account);
					    if(date.getTime()<=new Date().getTime()) 
					    {
					    	dbconn.Update(dbconn.conn, sql_balance);
					    	dbconn.Update(dbconn.conn, sql_account_recorded);
					    }
						
						System.out.print("INSERT INTO account values("+voucherno+","+account+","+amount+","+dc+","+date+");" + "\n");
						if ((sumd - sumc==0)&&(oddeven==0)) 
						{
							dbconn.Update(dbconn.conn, sql_voucher);
							System.out.print("INSERT INTO voucher values("+voucherno+","+supplierno+","+customerno+","+productno +","+description+","+date+","+source+","+UserNo+");" + "\n");
							sumd = sumc = 0;
						}
						i++;
					} while ((i<=rowcount)&&(!account.equals("")));
					JOptionPane.showMessageDialog(null, "Record successfully!","", JOptionPane.INFORMATION_MESSAGE);
 					//System.out.print("Fuck U MOTHERFXCKER!");//FUUUUUUUUUUUUUUUUUUUUUCK UUUUUUUUU RECORD SUCCESS!
					//drawNewTable();
					
					table.setModel(new DefaultTableModel(
							new Object[][] {
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
								{"", null, null, null, null, null, null, null, null, null},
							},
							new String[] {
								"Account", "SupplierNo.", "CustomerNo.", "ProductNo.", "Amount", "D/C", "Description", "Date", "Source"
							}
						) {
							Class[] columnTypes = new Class[] {
								String.class, String.class, String.class, String.class, String.class, /*Float.class, Integer.class,*/String.class, String.class, Date.class, String.class
							};
							public Class getColumnClass(int columnIndex) {
								return columnTypes[columnIndex];
							}
						});

						//tableSale.setModel(model);
						table.getColumnModel().getColumn(6).setPreferredWidth(50);
						JComboBox box = new JComboBox();  
				        box.addItem("Debit");  
				        box.addItem("Credit");  
				        TableColumn d = table.getColumn("D/C");  
				        DefaultCellEditor dce = new DefaultCellEditor(box);   
				        d.setCellEditor(dce);
				        
				        table.getColumnModel().getColumn(7).setCellEditor(new MyButtonEditor());
					
					//"INSERT INTO account values("+voucherNo+","+account+","+amount+","+debitCredit+","+date+");"
					//"INSERT INTO voucher values("+voucherNo+","+supplierNo+","+customerNo+","+productNo +","+description+","+date+","+source+","+userNo+");"
				}
			}
		});
		btnRecord.setBounds(552, 256, 80, 23);
		frame.getContentPane().add(btnRecord);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				((DefaultTableModel) table.getModel()).setRowCount(0);
				
				table.setModel(new DefaultTableModel(
					new Object[][] {
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
						{"", null, null, null, null, null, null, null, null, null},
					},
					new String[] {
						"Account", "SupplierNo.", "CustomerNo.", "ProductNo.", "Amount", "D/C", "Description", "Date", "Source"
					}
				) {
					Class[] columnTypes = new Class[] {
						String.class, String.class, String.class, String.class, String.class, /*Float.class, Integer.class,*/String.class, String.class, Date.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});

				//tableSale.setModel(model);
				table.getColumnModel().getColumn(6).setPreferredWidth(50);
				JComboBox box = new JComboBox();  
		        box.addItem("Debit");  
		        box.addItem("Credit");  
		        TableColumn d = table.getColumn("D/C");  
		        DefaultCellEditor dce = new DefaultCellEditor(box);   
		        d.setCellEditor(dce);
		        
		        table.getColumnModel().getColumn(7).setCellEditor(new MyButtonEditor());
			/*	
			    textVoucher.setText("");
				textAccount.setText("");
				textAmount.setText("");
				textSupplier.setText("");
				textCustomer.setText("");
				textItem.setText("");
				textDescription.setText("");
				textAcctDate.setText("");
			*/
			}
		});
		btnClear.setBounds(656, 256, 80, 23);
		frame.getContentPane().add(btnClear);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				System.exit(0);
			}
		});
		btnExit.setBounds(863, 256, 80, 23);
		frame.getContentPane().add(btnExit);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.setVisible(false);
				if(staticvoucher!=null)
				{
					staticvoucher.frame.dispose();//hope to close originvoucher when we close interface account
					staticvoucher=null;
				}
				
//				System.exit(0);//tryyitry
				Transfer transfer=new Transfer(dbconn, UserNo);
				transfer.frame.setVisible(true);
				
			}
		});
		btnBack.setBounds(760, 256, 80, 23);
		frame.getContentPane().add(btnBack);
	
		JLabel lblUserNo = new JLabel("");
		lblUserNo.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblUserNo.setBounds(10, 22, 125, 15);
		if(UserNo==-1)
			lblUserNo.setText("UserNo.: not log in");
		else lblUserNo.setText("UserNo.: "+UserNo);
		frame.getContentPane().add(lblUserNo);
	}
	
	////////////////////////////////////////////////////////////
	public class MyButtonEditor extends DefaultCellEditor  
    {  
      
        /** 
         * serialVersionUID 
         */  
        private static final long serialVersionUID = -6546334664166791132L;  
      
        private JPanel panel;  
      
        private JButton button;  
      
        public MyButtonEditor()  
        {  
            // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。   
            super(new JTextField());  
      
            // 设置点击几次激活编辑。   
            this.setClickCountToStart(1);  
      
            this.initButton();  
      
            this.initPanel();  
      
            // 添加按钮。   
            this.panel.add(this.button);  
        }  
      
        private void initButton()  
        {  
            this.button = new JButton();  
      
            // 设置按钮的大小及位置。   
            this.button.setBounds(0, 0, 50, 15);  
      
            // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。   
            this.button.addActionListener(new ActionListener()  
            {  
                public void actionPerformed(ActionEvent e)  
                {  
                    // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
                    MyButtonEditor.this.fireEditingCanceled();
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    System.out.print("Performed Success! Col: " + col + " Row: " + row + "\n");
                    DateUI dateTable = new DateUI(table, row, col);
                    // 这里可以做其它操作。   
                    // 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。   
                }  
            });  
      
        }  
      
        private void initPanel()  
        {  
            this.panel = new JPanel();  
      
            // panel使用绝对定位，这样button就不会充满整个单元格。   
            this.panel.setLayout(null);  
        }  
      
      
        /** 
         * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格） 
         */  
        @Override  
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
        {  
            // 只为按钮赋值即可。也可以作其它操作。   
            this.button.setText(value == null ? "" : String.valueOf(value));  
      
            return this.panel;  
        }  
      
        /** 
         * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。 
         */  
        @Override  
        public Object getCellEditorValue()  
        {  
            return this.button.getText();  
        }  
      
    } 
	

	
	
	////////////////////////////////////////////////////////////
}
