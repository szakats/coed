package coed.plugin.views;

/**
 * This is an interface for a view for displaying users.
 * @author Izso
 *
 */
public interface IUserList {
	
	/**
	 * Method receives a string containing the users and displays them in a view
	 * @param users - string of users
	 */
	public void displayUsers(String[] users);
}
