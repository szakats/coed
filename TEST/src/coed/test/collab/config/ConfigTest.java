package coed.test.collab.config;

import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.config.Config;

public class ConfigTest {
	public static void main (String args[]) {
		try {
			Config c = new Config("D:\\University\\III YEAR\\1st Semester\\SE\\CoED\\coed\\CoedCollaboration\\src\\coed\\collab\\client\\myfile.txt");
			System.out.println(c.getString("host"));
			c.setInt("money",111);
			c.setDouble("faef",89.0);
			c.writeConfigFile();
		} catch(InvalidConfigFileException e) {
			e.printStackTrace();
		}
	}
}
