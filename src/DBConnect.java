import java.sql.*;

public class DBConnect 
{
	public Connection conn;
	
	public DBConnect()
	{
		// TODO Auto-generated method stub   
//		String driver = "com.mysql.jdbc.Driver";// ����������        
//	    String url = "jdbc:mysql://127.0.0.1:3307/test";// URLָ��Ҫ���ʵ����ݿ���scutcs                 
//		String user = "root";// MySQL����ʱ���û���     
//		String password = "T28Z6b";// MySQL����ʱ������   
		//�������ݿ�
		String driver = "com.mysql.jdbc.Driver";// ����������        
	    String url = "jdbc:mysql://bodong31.mysql.rds.aliyuncs.com/accountingdb";// URLָ��Ҫ���ʵ����ݿ���accountingdb                 
		String user = "qsh31";// MySQL����ʱ���û���     
		String password = "228zhenNB";// MySQL����ʱ������           
		try 
	    {             // ������������            
	    	Class.forName(driver);
	    	conn = DriverManager.getConnection(url, user, password); // �������ݿ�            
    		if(!conn.isClosed())  System.out.println("Succeeded connecting to the Database!");
    		/* Statement statement = conn.createStatement();// statement����ִ��SQL���  
	    	String sql = "select * from student"; // Ҫִ�е�SQL���   
	    	ResultSet rs = statement.executeQuery(sql); // �����  
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
			String sql = "select * from " + query_sql; // Ҫִ�е�SQL���   
			String tableName = query_sql;// .substring(0, query_sql.indexOf('('));
			Statement statement = conn.createStatement();
			rs = statement.executeQuery(sql); // �����
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
			rs = statement.executeQuery(full_sql); // �����
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
			rs = statement.executeUpdate(full_sql); // �����
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
