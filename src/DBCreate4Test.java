import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBCreate4Test 
{
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub   
		String driver = "com.mysql.jdbc.Driver";// 驱动程序名        
	    String url = "jdbc:mysql://bodong31.mysql.rds.aliyuncs.com/accountingdb";// URL指向要访问的数据库名accountingdb                 
		String user = "qsh31";// MySQL配置时的用户名     
		String password = "228zhenNB";// MySQL配置时的密码           
	    try 
	    {             // 加载驱动程序            
	    	Class.forName(driver);
	    	Connection conn = DriverManager.getConnection(url, user, password); // 连续数据库            
    		if(!conn.isClosed())  System.out.println("Succeeded connecting to the Database!");
    		
    		//具体MySQL操作 - 创建表格 － 测试用
    		String Login = "Login(UserNo int primary key not null, Password varchar(20), Username varchar(50))";
    		String Voucher = "Voucher(VoucherNo varchar(7) primary key not null, SupplierNo varchar(20), CustomerNo varchar(20), ProductNo varchar(20), Description text, Date date, Source varchar(50), UserNo int, foreign key(UserNo) references Login(UserNo))";
    		String Account = "Account(AccountNo varchar(7), Account varchar(20), Amount double, DorC tinyint, Date date, primary key(AccountNo, Account))";
    		String Balance = "Balance(Account varchar(20) primary key not null, balance double)";
    		CreateTable(conn, Login);
    		CreateTable(conn, Voucher);
    		CreateTable(conn, Account);
    		CreateTable(conn, Balance);
    		
    		//具体MySQL操作 － 插入测试数据
    		
//    		String LoginDB = "Login values(111111,'r00t_User','root')";
//    		InsertData(conn, LoginDB);
//    		String LoginDB = "Login values(999999,'r00t_Tester','tester')";
//    		InsertData(conn, LoginDB);
//    		String LoginDB = "Login values(222222,'r00t_Reader','semi_root')";
//    		InsertData(conn, LoginDB);
    		//具体MySQL操作 － 查询测试数据
    		Query(conn, "Login");
	    	conn.close();
	    } 
	    catch (ClassNotFoundException e) 
	    {
	    	System.out.println("Sorry,can`t find the Driver!");             
	    	e.printStackTrace();
	    } 
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	public static void CreateTable(Connection conn, String table_sql)
	{
		try
		{
			String tableName = table_sql.substring(0, table_sql.indexOf('('));
			ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
			if (rs.next()) System.out.println("Table " + tableName + " already exist!");
			else
			{
				Statement statement = conn.createStatement();// statement用来执行SQL语句  
				String sql = "create table " + table_sql; // 要执行的SQL语句   
		    	statement.executeUpdate(sql); // 结果集
		    	/*while(rs.next())
		    	{
		    	}
		    	rs.close();
		    	*/
		    	System.out.println("Table " + tableName + " create succeed!");
			}
		}
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static void InsertData(Connection conn, String data_sql)
	{
		try
		{
			Statement statement = conn.createStatement();// statement用来执行SQL语句  
			String sql = "insert into " + data_sql; // 要执行的SQL语句   
			String tableName = data_sql.substring(0, data_sql.indexOf('('));
	    	statement.executeUpdate(sql); // 结果集
	    	/*
	    	 while(rs.next())
	    	 {
	    	 }
	    	 rs.close();
	    	*/
	    	System.out.print(tableName + " Data insert succeed!");
		}
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static void Query(Connection conn, String query_sql)
	{
		try
		{
			Statement statement = conn.createStatement();// statement用来执行SQL语句  
			String sql = "select * from " + query_sql; // 要执行的SQL语句   
			String tableName = query_sql;// .substring(0, query_sql.indexOf('('));
	    	ResultSet rs = statement.executeQuery(sql); // 结果集
	    	while(rs.next())
	    	{
	    		int userno = rs.getInt(1);
	    		String password = rs.getString(2);
	    		String username = rs.getString(3);
	    		System.out.print(userno + " " + password + " " + username + "\n");
	    	}
	    	rs.close();
	    	System.out.print(tableName + " Data query succeed!");
		}
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static void GetQuery_Order()
	{}
	public static void GetQuery_Inventory()
	{}
	public static void GetQuery_Transpotation()
	{}
	public static void GetQuery_Deposit()
	{}
	public static void GetQuery_Sale()
	{}
	
}
