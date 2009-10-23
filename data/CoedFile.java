package coed.data;

public abstract class CoedFile {
   
   public CoedFile(String path);
   public CoedFile(String project, String path, String version, String[] allVersions); //TODO
   
   public String getProject();
   public String getPath();
   public String getVersion();
   public String[] getAllVersions();
   public File getFile();
}