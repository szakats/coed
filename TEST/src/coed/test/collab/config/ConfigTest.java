package coed.test.collab.config;

import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.config.Config;

public class ConfigTest {
	public static void main (String args[]) {
		try {
			Config c = new Config("D:\\University\\III YEAR\\1st Semester\\SE\\CoED\\coed\\CoedCollaboration\\src\\coed\\collab\\client\\config\\myfile.txt");
			System.out.println(c.getString("host"));
		} catch(InvalidConfigFileException e) {
			e.printStackTrace();
		}
	}
}
