/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;



public class GenerateHandlerSkeletonCommand extends SimpleCommand
{

  private IProject serviceProject_;
  private MessageUtils msgUtils_;
 
  private String LABEL = "TASK_LABEL_GEN_HANDLER_SKELETON";
  private String DESCRIPTION = "TASK_DESC_GEN_HANDLER_SKELETON";
  
  private IPath outputLocation_;
  private String[] handlerNames_;
  private boolean genSkeleton_;
  private String handlerNameForEdit_ = null;
  
  public GenerateHandlerSkeletonCommand()
  {
    String       pluginId = "org.eclipse.jst.ws.consumption.ui";
  	msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
  	setName (msgUtils_.getMessage(LABEL));
  	setDescription( msgUtils_.getMessage(DESCRIPTION));    
  }
  

  public Status execute (Environment env)
  {
  	
  	if (!genSkeleton_)
  		return new SimpleStatus(""); 
  	
  	int i;
  	boolean error = false;
  	boolean warning = false;
  	
  	SimpleStatus status = null;
  	Status writeStatus;	
  	
  	
    for (i=0; i<handlerNames_.length; i++) {
    	writeStatus = writeFile(env, handlerNames_[i], outputLocation_);
    	// handle status return
    	if (writeStatus.getSeverity() == Status.ERROR) {  // write status is OK or ERROR
    		error = true;
    		if (status == null) {
    			status = new SimpleStatus( "execute", msgUtils_.getMessage("MSG_ERROR_GENERATE_HANDLER_SKELETON"), Status.ERROR);
    		}
    		status.addChild(writeStatus);
    	} 
    }
    if (error) {
       env.getStatusHandler().reportError(status);
    } else {
    	status = new SimpleStatus( "" );
    }
  	return status;
  }
  
  public Status writeFile (Environment env, String className, IPath outputLocation) 
  {
  	Status status = new SimpleStatus("");
  	int index;
  	
  	String simpleClassName = className;
  	String packageName = null;
  	IPath filePath = outputLocation;
  	if (className != null) {
  		index = className.lastIndexOf('.');
  		if (index != -1) {
  			simpleClassName = className.substring(index + 1);
  			packageName = className.substring(0, index);
  			
  		  	String packageSegment = packageName;
  		  	int j = 0;
  		  	j = packageSegment.indexOf('.');
  		  	while (j != -1) {
  		  		filePath = filePath.append(packageSegment.substring(0, j));
  		  		packageSegment = packageSegment.substring(j + 1);
  		  		j = packageSegment.indexOf('.');
  		  	}
  		    filePath = filePath.append(packageSegment);
  		}
  		else {
  		  packageName = "";
  		}
  	}
  	
  	filePath = filePath.append(simpleClassName);
  	
  	filePath = filePath.addFileExtension("java");

  	// check if Handler already exists; do not overwrite existing Handlers
  	IResource workspaceRes = ResourceUtils.findResource(filePath);
  	if (workspaceRes!=null && workspaceRes.exists()) {
  		return status;
  	}
  	
  	if (handlerNameForEdit_ == null){
  		handlerNameForEdit_ = className;
  	}
  	
  	OutputStream outputStream = FileResourceUtils.newFileOutputStream( EnvironmentUtils.getResourceContext(env), filePath, env.getProgressMonitor(), env.getStatusHandler());
  	// create buffered writer for writing file
  	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
  	try {
  		if (packageName.length() != 0) {
  			bw.write("package " +packageName+";");
  			bw.newLine();
  			bw.newLine();
  		}
  		bw.write("import javax.xml.rpc.handler.GenericHandler;");
  		bw.newLine();
  		bw.write("import javax.xml.rpc.handler.MessageContext;");
  		bw.newLine();
  		bw.write("import javax.xml.namespace.QName;");
  		bw.newLine();
  		bw.newLine();
  		
  		bw.write("public class "+simpleClassName+" extends GenericHandler");
  		bw.newLine();
  		bw.write("{");
  		bw.newLine();
  		bw.newLine();
  		
  		bw.write("   public QName[] getHeaders ()");
  		bw.newLine();
  		bw.write("   {");
  		bw.newLine();
  		bw.write("      // Fill in method body");
  		bw.newLine();
  		bw.write("      return null;");
  		bw.newLine();
  		bw.write("   }");
  		bw.newLine();
  		bw.newLine();
  		bw.write("   public boolean handleRequest( MessageContext context )");
  		bw.newLine();
  		bw.write("   {");
  		bw.newLine();
  		bw.write("      // Fill in method body or delete method to use GenericHandler");
  		bw.newLine();
  		bw.write("      return true;");
  		bw.newLine();
  		bw.write("   }");
  		bw.newLine();
  		bw.newLine();
  		bw.write("   public boolean handleResponse( MessageContext context )");
  		bw.newLine();
  		bw.write("   {");
  		bw.newLine();
  		bw.write("      // Fill in method body or delete method to use GenericHandler ");
  		bw.newLine();
  		bw.write("      return true;");
  		bw.newLine();
  		bw.write("   }");
  		bw.newLine();
  		bw.newLine();
  		bw.write("   public boolean handleFault( MessageContext context )");
  		bw.newLine();
  		bw.write("   {");
  		bw.newLine();
  		bw.write("      // Fill in method body or delete method to use GenericHandler");
  		bw.newLine();
  		bw.write("      return true;");
  		bw.newLine();
  		bw.write("   }");
  		bw.newLine();
  		
  		bw.newLine();
  		bw.write("}");
  		bw.close();
  		status = new SimpleStatus( "" );	
  	} catch (IOException e) {
  		status = new SimpleStatus( "writeFile", msgUtils_.getMessage("MSG_ERROR_WRITE_FILE", new String[]{ className }), Status.ERROR, e );
  		if (bw != null) {
  			try {
  				bw.close();
  			} catch (IOException e1) {
  			}
  		}
  	}
  	return status;
  }

  public Status undo(Environment environment)
  {
    return null;
  }

  public Status redo(Environment environment)
  {
    return null;
  }
  
  public void setOutputLocation(IPath outputLocation) 
  {
	this.outputLocation_ = outputLocation;
  }
  
  public void setHandlerNames (String[] handlerNames)
  {
  	this.handlerNames_ = handlerNames;
  }
  
  public void setGenSkeletonEnabled(boolean genSkeleton)
  {
  	this.genSkeleton_ = genSkeleton;
  }
  
  public IProject getProject(){
  	IProject project = null;
  	if (outputLocation_!=null){
  		project = ResourceUtils.getProjectOf(outputLocation_);
  	}
  	return project;
  }
  
  public List getClassNames(){
  	List classes = new ArrayList();
  	if (handlerNameForEdit_!=null){
  		classes.add(handlerNameForEdit_);
  	}
  	return classes;
  }
  
}
