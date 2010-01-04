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
	
	/**
	 * Method receives a user and adds its to the current list of users
	 * @param user - user that must be added from list
	 */
	public void displayUser(String user);
	
	/**
	 * Method receives a user as parameter and removes him from the list of users
	 * @param user - user that must be removed
	 */
	public void removeUser(String user);
}
