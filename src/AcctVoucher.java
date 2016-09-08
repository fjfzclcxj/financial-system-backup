import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import com.mysql.jdbc.Connection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

import java.util.Vector;

public class AcctVoucher {

	JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AcctVoucher window = new AcctVoucher();
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
	public AcctVoucher(final DBConnect dbconn, int UserNo) {
		initialize(dbconn, UserNo);
	}
	public AcctVoucher() {
		initialize(null, -1);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final DBConnect dbconn, final int UserNo) {
		//Check if dbconn is null and UserNo is -1; if so, not connect to the database or not login yet
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblAcctVou = new JLabel("Accounting Voucher");
		lblAcctVou.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblAcctVou.setBounds(514, 10, 196, 22);
		frame.getContentPane().add(lblAcctVou);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 60, 1164, 285);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		/*
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null},
				{"", null, null, null, null, null, null, null, null, null, null},
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
				"Accounting VoucherNo.", "Account", "Amount", "D/C", "Date", "Description", "VoucherNo.", "SupplierNo.", "CustomerNo.", "ItemNo.", "Accountant"
			}
		) {
		*/
		DefaultTableModel model = new DefaultTableModel(null, new String[] {	"Account-VoucherNo.", "Account", "Amount", "D/C", "Date", "Description", "VoucherNo.", "SupplierNo.", "CustomerNo.", "ItemNo.", "Accountant"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
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
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		//table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		//table.getColumnModel().getColumn(5).setPreferredWidth(150);
		//table.getColumnModel().getColumn(6).setPreferredWidth(45);
		//table.getColumnModel().getColumn(7).setPreferredWidth(50);
		//table.getColumnModel().getColumn(8).setPreferredWidth(45);
		//table.getColumnModel().getColumn(9).setPreferredWidth(45);
		scrollPane.setViewportView(table);
		
		try 
		{	
			ResultSet rs = dbconn.Query(dbconn.conn, "acctvoucher");
			while(rs.next())
			{
				String voucherno = rs.getString(1);
				String account = rs.getString(2);
				String supplierno =  rs.getString(3);
				String customerno =  rs.getString(4);
				String productno =  rs.getString(5);
				DecimalFormat decimalFormat=new DecimalFormat(".00");
				double amount =  rs.getDouble(6);
				String samount = decimalFormat.format(amount);
				String dc = (rs.getInt(7)==0)?"Debit":"Credit";
				String description =  rs.getString(8);
				Date date =  rs.getDate(9);
				String source =  rs.getString(10);
				String userNo =  rs.getString(11);
				//if (!dc.equals("Debit")) account = "      " + account;
				
				Vector a =  new Vector();//Use Class Vector to contain information
				a.add(voucherno);
				a.add(account);
				a.add(samount);
				a.add(dc);
				a.add(date);
				a.add(description);
				a.add(source);
				a.add(supplierno);
				a.add(customerno);
				a.add(productno);
				a.add(userNo);
				//"select account.voucherNo, account.account, voucher.supplierNo, voucher.customerNo, voucher.productNo, account.amount, account.debitCredit, voucher.description, account.date, voucher.source, voucher.userNo from account join voucher on account.voucherNo=voucher.voucherNo;"
				model.addRow(a);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Account account=new Account(dbconn, UserNo);
				account.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnBack.setBounds(917, 367, 93, 23);
		frame.getContentPane().add(btnBack);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				System.exit(0);
			}
		});
		btnExit.setBounds(1048, 367, 93, 23);
		frame.getContentPane().add(btnExit);
	}
}
