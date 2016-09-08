import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Balance {

	JFrame frame;
	private JTable table;
	private JLabel lblBalance;
	private JTextField textSum;
	static double sumofInventory=0;
	static double sumofExpense=0;
	static double sumofSales=0;
	//static double EBIT=0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Balance window = new Balance();
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
	public Balance(final DBConnect dbconn, int UserNo){
		initialize(dbconn, UserNo);
	}
	public Balance(){
		initialize(null, -1);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final DBConnect dbconn, final int UserNo){
		//Check if dbconn is null and UserNo is -1; if so, not connect to the database or not login yet
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 482);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 75, 414, 220);
		frame.getContentPane().add(scrollPane);
		 
		table = new JTable();
		DefaultTableModel model = new DefaultTableModel(null, new String[] {"Account", "Debit", "Credit"})
		{
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
//		table.setModel(new DefaultTableModel(
//			new Object[][] {
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				{null, null, null, null},
//				
//			},
//			new String[] {
//				"Account", "Debit", "Credit", "Balance"
//			}
//		)); 
		table.setModel(model);
		
		scrollPane.setViewportView(table);
		//Get all information from Table Balance
		try 
		{	
			ResultSet rs = dbconn.Query(dbconn.conn, "Balance");
			while(rs.next())
			{
				String account = rs.getString(1);
				double balance = rs.getDouble(2);
//				DecimalFormat df = new DecimalFormat("######0.00");//take two decimal
//				df.format(balance);
//				System.out.println(balance);
				if(account.contains("库存")) sumofInventory+=Math.abs(balance);
				if(account.contains("仓储费用")) sumofExpense+=Math.abs(balance);
				if(account.contains("运输费用")) sumofExpense+=Math.abs(balance);
				if(account.contains("营业成本")) sumofExpense+=Math.abs(balance);
				if(account.contains("营业收入")) sumofSales+=Math.abs(balance);
				
				String a[];
				a = new String[]{"","",""};
				if (balance>0) a = new String[]{account, Math.abs(balance) + "", ""};
				if (balance<0) a = new String[]{account, "", Math.abs(balance) + ""};
				if (balance!=0) model.addRow(a);
			}
			rs.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		lblBalance = new JLabel("Balance");
		lblBalance.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblBalance.setBounds(182, 18, 74, 15);
		frame.getContentPane().add(lblBalance);
		
		JLabel lblTime = new JLabel("New label");
		lblTime.setBounds(157, 43, 119, 15);
		frame.getContentPane().add(lblTime);
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(date);
		lblTime.setText(time);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Transfer transfer=new Transfer(dbconn, UserNo);
				transfer.frame.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnBack.setBounds(66, 391, 93, 23);
		frame.getContentPane().add(btnBack);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				System.exit(0);
			}
		});
		btnExit.setBounds(264, 391, 93, 23);
		frame.getContentPane().add(btnExit);
		
		textSum = new JTextField();
		textSum.setEditable(false);
		textSum.setBounds(238, 335, 119, 21);
		frame.getContentPane().add(textSum);
		textSum.setColumns(1);
		
		/*
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"EBIT","Sales", "Expense", "Inventory"}));
		comboBox.setBounds(66, 335, 93, 21);
		frame.getContentPane().add(comboBox);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(comboBox.getSelectedItem().toString())
				{
					case "EBIT": textSum.setText((sumofSales-sumofExpense)+""); break;
					case "Sales": textSum.setText(sumofSales+""); break;
					case "Expense": textSum.setText(sumofExpense+""); break;
					case "Inventory": textSum.setText(sumofInventory+""); break;
					default: textSum.setText("Error");
				}
				
			}
		});
		*/
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"EBIT","Sales", "Expense", "Inventory"}));
		comboBox.setBounds(66, 335, 93, 21);
		frame.getContentPane().add(comboBox);
		
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				String cur = (String)comboBox.getSelectedItem();
				DecimalFormat df = new DecimalFormat("######0.00");//take two decimal
				switch(cur.charAt(1))
				{
					
//					df.format(balance);
//					System.out.println(balance);
					case 'B': textSum.setText(df.format(sumofSales-sumofExpense)+""); break; //take two decimals
					case 'a': textSum.setText(df.format(sumofSales)+""); break;
					case 'x': textSum.setText(df.format(sumofExpense)+""); break;
					case 'n': textSum.setText(df.format(sumofInventory)+""); break;
					default: textSum.setText("Error");
				}
				
			}
		});
		
		
		
		
		
	}
}




