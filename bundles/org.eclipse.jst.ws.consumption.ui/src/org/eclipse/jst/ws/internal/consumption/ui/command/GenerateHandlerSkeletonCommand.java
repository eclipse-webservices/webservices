/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060404   134913 sengpl@ca.ibm.com - Seng Phung-Lu      
 * 20060517   142027 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerDescriptionHolder;
import org.eclipse.jst.ws.internal.consumption.ui.common.HandlerServiceRefHolder;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.HandlerTableItem;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.eclipse.EnvironmentUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;



public class GenerateHandlerSkeletonCommand extends AbstractDataModelOperation
{
  private IPath outputLocation_;
  private boolean genSkeleton_;
  private String handlerNameForEdit_ = null;
  
  private HandlerServiceRefHolder[] handlerServiceRefHolder_;
  private HandlerDescriptionHolder[]handlerDescriptionHolder_;
  
  public GenerateHandlerSkeletonCommand()
  {
  }
  

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
  
    IStatus     returnStatus = Status.OK_STATUS;
  	
  	if (!genSkeleton_)	return returnStatus; 
  	
      // form the map of classname to locations
      if (handlerDescriptionHolder_!=null){
        for (int i=0;i<handlerDescriptionHolder_.length;i++){
          Hashtable handlerTable = new Hashtable();
          IPath outPath = handlerDescriptionHolder_[i].getSourceOutputPath();
          List handlers = handlerDescriptionHolder_[i].getHandlerList();
          for (int j=0;j<handlers.size();j++){
            HandlerTableItem hti = (HandlerTableItem)handlers.get(j);
            String className = hti.getHandlerClassName();
            if (className!=null && outPath!=null)
              handlerTable.put(className, outPath);
          }
          returnStatus = genHandlersClasses(handlerTable, monitor);
        }
        
      }
      else {
        for (int i=0;i<handlerServiceRefHolder_.length;i++){
          Hashtable handlerTable = new Hashtable();
          IPath outPath = handlerServiceRefHolder_[i].getSourceOutputPath();
          List handlers = handlerServiceRefHolder_[i].getHandlerList();
          for (int j=0;j<handlers.size();j++){
            HandlerTableItem hti = (HandlerTableItem)handlers.get(j);
            String className = hti.getHandlerClassName();
            if (className!=null && outPath!=null)
              handlerTable.put(className, outPath);
          }
          returnStatus = genHandlersClasses(handlerTable, monitor);
        }
      }
    
 
    return returnStatus;

  }
  
  private IStatus genHandlersClasses(Hashtable handlersForGen, IProgressMonitor monitor){
    IEnvironment env          = getEnvironment();
    MultiStatus status       = null;
    IStatus     returnStatus = Status.OK_STATUS;
    boolean error = false;
    
    IStatus writeStatus;    
    Enumeration keys = handlersForGen.keys();
    while (keys.hasMoreElements()) {
        String className = (String)keys.nextElement();
        writeStatus = writeFile(env, className, (IPath)handlersForGen.get(className), monitor );
        // handle status return
        if (writeStatus.getSeverity() == Status.ERROR) {  // write status is OK or ERROR
            error = true;
            if (status == null) {
                status = StatusUtils.multiStatus( ConsumptionUIMessages.MSG_ERROR_GENERATE_HANDLER_SKELETON, new IStatus[0] );
            }
            status.add(writeStatus);
        } 
    }
    
    if (error) 
    {
       env.getStatusHandler().reportError(status);
       returnStatus = status;
    }
    
    return returnStatus;
    
  }
  
  
  private IStatus writeFile (IEnvironment env, String className, IPath outputLocation, IProgressMonitor monitor ) 
  {
  	IStatus status = Status.OK_STATUS;
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
        outputLocation_ = outputLocation;
  	}
  	
  	OutputStream outputStream = FileResourceUtils.newFileOutputStream( EnvironmentUtils.getResourceContext(env), filePath, monitor, env.getStatusHandler());
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
  		status = Status.OK_STATUS;	
  	} 
    catch (IOException e) 
    {
  		status = StatusUtils.errorStatus( NLS.bind(ConsumptionUIMessages.MSG_ERROR_WRITE_FILE, new String[]{ className }), e );
  		if (bw != null) {
  			try {
  				bw.close();
  			} catch (IOException e1) {
  			}
  		}
  	}
  	return status;
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
  
  /**
   * An array of HandlerDescriptionHolders
   * @return
   */
  public void setHandlerServiceRefHolder(HandlerServiceRefHolder[] handlerHolders){
    this.handlerServiceRefHolder_ = handlerHolders;
  }
  
  /**
   * An array of HandlerDescriptionHolders
   * @return
   */
  public void setHandlerDescriptionHolders(HandlerDescriptionHolder[] handlerHolders){
    this.handlerDescriptionHolder_ = handlerHolders;
  }
  
 
}
