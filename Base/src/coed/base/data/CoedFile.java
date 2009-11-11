package coed.base.data;

import java.io.File;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedFile { //TODO: create real class instead of abstract
   
   public CoedFile(String path) {}
   public CoedFile(String project, String path, String version, String[] allVersions){}
   
   public String getProject() { return null;}
   public String getPath() {return null;}
   public String getVersion() {return null;}
   public String[] getAllVersions() {return null;}
   public File getFile() {return null;}
}