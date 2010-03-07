/**
 * 
 */
package coed.collab.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import coed.base.data.exceptions.InvalidConfigFileException;
import coed.collab.server.db.DBConnectionException;
import coed.collab.server.db.DBConnector;

/**
 * @author neobi008
 *
 */
public class UserManager {
	
	private String configPath;

	public UserManager(String configPath){
		this.configPath = configPath;
	}
	
	public boolean validateUser(String user, String password){
		try {
			DBConnector.getInstance().connect(configPath);
			ResultSet res = DBConnector.getInstance().query("Select * from users where name='"+user+"'");
			if (res.next() && res.getString(2).equals(password)) return true;
		} catch (InvalidConfigFileException e) {
			e.printStackTrace();
		} catch (DBConnectionException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
