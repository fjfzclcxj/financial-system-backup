import java.sql.*;

public class DBConnect 
{
	public Connection conn;
	
	public DBConnect()
	{
		// TODO Auto-generated method stub   
//		String driver = "com.mysql.jdbc.Driver";// 驱动程序名        
//	    String url = "jdbc:mysql://127.0.0.1:3307/test";// URL指向要访问的数据库名scutcs                 
//		String user = "root";// MySQL配置时的用户名     
//		String password = "T28Z6b";// MySQL配置时的密码   
		//在线数据库
		String driver = "com.mysql.jdbc.Driver";// 驱动程序名        
	    String url = "jdbc:mysql://bodong31.mysql.rds.aliyuncs.com/accountingdb";// URL指向要访问的数据库名accountingdb                 
		String user = "qsh31";// MySQL配置时的用户名     
		String password = "228zhenNB";// MySQL配置时的密码           
		try 
	    {             // 加载驱动程序            
	    	Class.forName(driver);
	    	conn = DriverManager.getConnection(url, user, password); // 连续数据库            
    		if(!conn.isClosed())  System.out.println("Succeeded connecting to the Database!");
    		/* Statement statement = conn.createStatement();// statement用来执行SQL语句  
	    	String sql = "select * from student"; // 要执行的SQL语句   
	    	ResultSet rs = statement.executeQuery(sql); // 结果集  
	    	while(rs.next())
	    	{                 
	    	}
	    	rs.close();            
	    	*/
	    	//conn.close();
	    } 
	    catch(ClassNotFoundException e) 
	    {
	    	System.out.println("Sorry,can`t find the Driver!");             
	    	e.printStackTrace();
	    } 
	    catch(SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch(Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static ResultSet Query(Connection conn, String query_sql)
	{
		ResultSet rs = null;
		try
		{
			String sql = "select * from " + query_sql; // 要执行的SQL语句   
			String tableName = query_sql;// .substring(0, query_sql.indexOf('('));
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(sql); // 结果集
	    	/*
	    	{
	    		int userno = rs.getInt(1);
	    		String password = rs.getString(2);
	    		String username = rs.getString(3);
	    		System.out.print(userno + " " + password + " " + username + "\n");
	    	}
	    	System.out.print(tableName + " Data query succeed!");
	    	*/
	    	
		}
	    catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
		return rs;
	}
	
	public static ResultSet Run(Connection conn, String full_sql)
	{
		ResultSet rs = null;
		try
		{
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(full_sql); // 结果集
		}
		catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
		return rs;
	}
	
	public static int Update(Connection conn, String full_sql)
	{
		int rs = 0;
		try
		{
			Statement statement = conn.createStatement();
			rs = statement.executeUpdate(full_sql); // 结果集
		}
		catch (SQLException e) 
	    {
	    	e.printStackTrace();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
		return rs;
	}
	
	
	protected void finalize() throws SQLException
	{
		System.out.print("Database disconnect succeess!");
		this.conn.close();
	}

}
