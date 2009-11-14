package coed.base.data;

import java.io.File;
import java.io.Serializable;

/**
 * TODO: javadoc
 * @author Neobi
 *
 */
public class CoedFile implements Serializable { 
    /**
	 * Relative path of the file in its project
	 */
    private String path;
    
    /**
     * Project of the file (its name)
     */
    private CoedProject project;
    
    //CONSTRUCTORS----------------------------------------
    
    public CoedFile(CoedProject project, String path){
    	this.project=project;
    	this.path=path;
    }
    
    //INSTANCE METHODS-------------------------------------
   
    public CoedProject getProject() { 
    	return this.project;
    }
    
    public String getPath() {
    	return this.path;
    }
}