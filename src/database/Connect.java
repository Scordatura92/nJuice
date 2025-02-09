package database;

import java.sql.*;

public final class Connect {
	
	private final String USERNAME = "root"; // change with your MySQL username, the default username is 'root'
	private final String PASSWORD = ""; // change with your MySQL password, the default password is empty
	private final String DATABASE = "njuice"; // change with the database name that you use
	private final String HOST = "localhost:3306"; // change with your MySQL host, the default port is 3306
	private final String CONECTION = String.format("jdbc:mysql://%s/%s?enabledTLSProtocols=TLSv1.2", HOST, DATABASE);
	
	private Connection con;
	private Statement st;
	private static Connect connect;
	
	//result set
	//untuk nampung hasil query
	public ResultSet rs;
	public PreparedStatement ps;
	public ResultSetMetaData rsm;
	


    private Connect() {
    	try {  
    		 Class.forName("com.mysql.cj.jdbc.Driver");
             con = DriverManager.getConnection(CONECTION,USERNAME, PASSWORD);  
             st = con.createStatement(); 
             System.out.println("server connected !s");
        } catch(Exception e) {
        	e.printStackTrace();
        	System.out.println("Failed to connect the database, the system is terminated!");
        	System.exit(0);
        }  
    }
    
    
    //SELECT data 
    public ResultSet selectQuery(String query)
    {
    	try {
    		rs = st.executeQuery(query);
			System.out.println(rs);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return rs;
    }
    
	/**
	* This method is used for get instance from Connect class
	* @return Connect This returns instance from Connect class
	*/
    public static synchronized Connect getConnection() {
		/**
		* If the connect is null then:
		*   - Create the instance from Connect class
		*   - Otherwise, just assign the previous instance of this class
		*/
		return connect = (connect == null) ? new Connect() : connect;
    }

    /**
	* This method is used for SELECT SQL statements.
	* @param String This is the query statement
	* @return ResultSet This returns result data from the database
	*/
    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
    	try {
            rs = st.executeQuery(query);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return rs;
    }

	/**
	* This method is used for INSERT, UPDATE, or DELETE SQL statements.
	* @param String This is the query statement
	*/
    public void executeUpdate(String query) {
    	try {
			st.executeUpdate(query);
			System.out.println(st);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
     
}