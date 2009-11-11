package coed.base.data;

import java.io.File;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedFile { 
    /**
	 * Relative path of the file in its project
	 */
    private String path;
    
    /**
     * Project of the file (its name)
     */
    private String project;
    
    /**
     * Current version of the file
     */
    private String version;
    
    /**
     * All existing vwersions of the file
     */
    private String[] allVersions;
    
    //CONSTRUCTORS----------------------------------------
    
    public CoedFile(String project, String path, String version, String[] allVersions){
    	this.project=project;
    	this.path=path;
    	this.version=version;
    	this.allVersions=allVersions;
    }
    
    public CoedFile(String project, String path, String version){
    	this(project,path,version,null);
    }
    
    public CoedFile(String project, String path){
    	this(project,path,null,null);
    }
    
    //INSTANCE METHODS-------------------------------------
   
    public String getProject() { 
    	return this.project;
    }
    
    public String getPath() {
    	return this.path;
    }
    public String getVersion() {
    	return this.version;
    }
    
    public String[] getAllVersions() {
    	return this.allVersions;
    }
    
    public File getFile() {
    	return FileManager.getFileFor(this);
    }
}