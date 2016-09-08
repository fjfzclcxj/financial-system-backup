import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUI extends MouseAdapter {

	JDialog jf = new JDialog();

	JPanel jp = new JPanel();

	JComboBox yearBox = new JComboBox();

	JComboBox monthBox = new JComboBox();

	JButton[][] buttons = new JButton[7][7];

	String[] weeks = { "日", "一", "二", "三", "四", "五", "六" };

	Calendar cd = Calendar.getInstance();

	int curX = -1;

	int curY = -1;
	
	int row = 0;
	
	int col = 7;

	//JTextField dateText;
	
	JTable dateTable;

	public DateUI(JTable dateTable, int row, int col) {
		
		this.dateTable = dateTable;
		this.row = row;
		this.col = col;
		jf.setLocationRelativeTo(dateTable);
		jf.getContentPane().add(jp);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jp.setBorder(BorderFactory.createTitledBorder("日期"));
		jp.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel(new GridLayout(1, 0,0,0));
		for (int i = 2000; i < 2100; i++) {
			yearBox.addItem(i + "年");
		}
		yearBox.setSelectedItem("2009年");
		for (int i = 1; i <= 12; i++) {
			monthBox.addItem(i + "月");
		}
		northPanel.add(yearBox);
		northPanel.add(monthBox);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(7, 7));
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (i == 0) {
					buttons[i][j] = new JButton(weeks[j]);
					buttons[i][j].setEnabled(false);
					buttons[i][j].setBackground(Color.green);
				} else {
					buttons[i][j] = new JButton("");
					buttons[i][j].setBackground(Color.white);
					buttons[i][j].addMouseListener(this);
				}
				if (j == 0) {
					buttons[i][j].setForeground(Color.red);
				}
				centerPanel.add(buttons[i][j]);
			}
		}
		setDate();
		yearBox.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					return;
				}
				String yy = e.getItem().toString();
				int yi = Integer.parseInt(yy.substring(0, yy.length() - 1));
				cd.set(Calendar.YEAR, yi);
				setDate();
			}

		});
		monthBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 2) {
					return;
				}
				String yy = e.getItem().toString();
				int yi = Integer.parseInt(yy.substring(0, yy.length() - 1));
				cd.set(Calendar.MONTH, yi - 1);
				setDate();
			}

		});
		jp.add(northPanel, BorderLayout.NORTH);
		jp.add(centerPanel, BorderLayout.CENTER);
		jf.setSize(370,220);
		jf.setVisible(true);
	}

	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 1) {
			buttons[curX][curY].setBackground(Color.white);
			for (int i = 1; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					if (buttons[i][j].getText().equals("")) {
						continue;
					}
					if (buttons[i][j].equals(e.getSource())) {
						curX = i;
						curY = j;
						buttons[curX][curY].setBackground(Color.MAGENTA);
					}
				}
			}
		} 
		else 
		{
			String month = monthBox.getSelectedItem().toString().substring(0, 1);
			System.out.print(month + "\n");
			//while (month.length()<2) month = "0" + month;
			String day = buttons[curX][curY].getText();
			//while (day.length()<2) day = "0" + day;
			System.out.print(day + "\n");
			//String ds=yearBox.getSelectedItem().toString()+ monthBox.getSelectedItem().toString()+ buttons[curX][curY].getText();
			String ds = yearBox.getSelectedItem().toString() + month + "月"+ day +" ";
			ds=ds.replaceAll("年|月", "-");
			try  
			{  
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");  
			    Date date = sdf.parse(ds);  
			    dateTable.setValueAt(date, row, col);
			    System.out.print(date);
			}  
			catch (ParseException e1)  
			{  
			    System.out.println(e1.getMessage());  
			}
			jf.dispose();
		}
	}

	public void setDate() {
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				buttons[i][j].setText("");
				buttons[i][j].setBackground(Color.white);
				buttons[i][j].setEnabled(false);
			}
		}
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH) + 1;
		int day = cd.get(Calendar.DAY_OF_MONTH);
		curX = cd.get(Calendar.WEEK_OF_MONTH);
		curY = cd.get(Calendar.DAY_OF_WEEK) - 1;
		buttons[curX][curY].setBackground(Color.MAGENTA);
		yearBox.setSelectedItem(year + "年");
		monthBox.setSelectedItem(month + "月");
		cd.set(Calendar.DAY_OF_MONTH, 1);
		int week = cd.get(Calendar.DAY_OF_WEEK);
		int maxDay = cd.getActualMaximum(Calendar.DAY_OF_MONTH);
		int k = 0;
		int dm = 1;
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				k++;
				if (k >= week && k < maxDay + week) {
					buttons[i][j].setText(dm++ + "");
					buttons[i][j].setEnabled(true);
				}

			}
		}
		cd.set(Calendar.DAY_OF_MONTH, day);
	}

	public static void main(String[] args) {
		//传入文本框参数，双击时可把日期填入文本框
		new DateUI(new JTable(), 7, 0);
	}

}
