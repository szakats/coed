package coed.test.plugin.views;

import coed.plugin.views.UserListView;

public class UserListTest {

	public UserListTest(){
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserListView v=new UserListView();
		
		String[] x= {"a","d","s"};
		v.displayUsers(x);
		System.out.println("Multiple user add: " + v.getUsers());
		String y="b";
		v.displayUser(y);
		System.out.println("Single user add(b) : " + v.getUsers());
		y="q";
		v.displayUser(y);
		System.out.println("Single user add(q) : " + v.getUsers());
		y="b";
		v.removeUser(y);
		System.out.println("Single user remove(b) : " +v.getUsers());
		y="q";
		v.removeUser(y);
		System.out.println("Single user remove(q) : " +v.getUsers());

	}

}
