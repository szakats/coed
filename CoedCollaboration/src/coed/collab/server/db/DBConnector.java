/**
 * 
 */
package coed.collab.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import coed.base.data.exceptions.InvalidConfigFileException;
import coed.collab.client.config.Config;



/**
 * Singleton class that creates a connection to a mysql database
 * @author Neobi
 *
 */

public final class DBConnector{
	
	Connection conn = null;
	public static final DBConnector connector = new DBConnector(); //for thread safe singleton
    
    private DBConnector(){
    }
    
    public void connect(String configPath) throws InvalidConfigFileException, DBConnectionException{
    	Config config = new Config(configPath);
	    String url = "jdbc:mysql://localhost:3306/";
	    String dbName = config.getString("db.databaseName");
	    String userName = config.getString("db.userName"); 
	    String password = config.getString("db.password");
	    try{
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	      conn = DriverManager.getConnection(url+dbName,userName,password);
	    }catch(Exception ex)
	    {
	    	throw new DBConnectionException();
	    }  
    }
    
    /**
     * Processes a query and returns the result as a ResultSet.
     * Throws SQLException when query is unsuccessful
     * @param q the string containing the query
     * @return the result set
     */
    
    public ResultSet query(String q) throws SQLException{
    	Statement stat = conn.createStatement();
    	return  stat.executeQuery(q);
    }
    
    /**
     * Alters a table's contents and returns the number of rows affected
     * Throws SQLException when query is unsuccessful
     * @param q - the string representing the query
     * @return the number of rows affected
     */
    
    public int alter(String q)throws SQLException{
    	Statement stat = conn.createStatement();
    	return stat.executeUpdate(q);
    }
}
