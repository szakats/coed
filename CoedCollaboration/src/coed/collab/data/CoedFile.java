package coed.collab.data;

import java.io.File;

/**
 * TODO: javadoc
 * @author Izso
 *
 */
public abstract class CoedFile { //TODO: create real class instead of abstract
   
   public CoedFile(String path) {};
   public CoedFile(String project, String path, String version, String[] allVersions){};
   
   public abstract String getProject();
   public abstract String getPath();
   public abstract String getVersion();
   public abstract String[] getAllVersions();
   public abstract File getFile();
}