package coed.test.collab.server;

import coed.collab.server.db.DBConnector;

public class DbTest {
	
	public DbTest(){
		doTest();
	}
	
	public void doTest(){
		try{
		DBConnector.getInstance().connect("D:\\University\\III YEAR\\1st Semester\\SE\\CoED\\coed\\config.ini");
		DBConnector.getInstance().alter("Insert into Users values (\"neobi\", \"password\")");
		}catch (Exception e) {}
		
		
	}
	
	public static void main(String[] args){
		DbTest t = new DbTest();
		
	}

}
