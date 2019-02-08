/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.Field;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
import org.eclipse.wst.common.environment.IEnvironment;



/**
* Objects of this class represent a visitor.
* */
public class JavaMofTypeVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private IEnvironment env_;

  private String clientProject;

  /*
  * Constructor
  **/
  public JavaMofTypeVisitor(IEnvironment env)
  {
  	env_ = env;
  }

   /*
  * Use this to reflect 
  */
  public void setClientProject(String clientProject)
  {
    this.clientProject = clientProject;
  }

   /*
  * Use this to reflect 
  */
  public String getClientProject()
  {
    return clientProject;
  }
  
  /*
  * Get the type belonging to the parameter
  * @param JavaParameter javaParameter that owns the type
  * @param VisitorAction Action to be performed on each method
  **/
  public IStatus run ( Object typeNavigator, VisitorAction vAction)
  {
  	IStatus status = Status.OK_STATUS;
    if (typeNavigator instanceof JavaParameter){

      JavaParameter javaParameter = (JavaParameter)typeNavigator;
      JavaHelpers javaHelpers = javaParameter.getJavaType();
      status = vAction.visit(javaHelpers);
    }
    else if (typeNavigator instanceof JavaHelpers){
    	status = vAction.visit(typeNavigator);	
    }
    else if (typeNavigator instanceof SamplePropertyDescriptor){
      SamplePropertyDescriptor pd = (SamplePropertyDescriptor)typeNavigator;
      JavaMofReflectionCommand javaMofRef = new JavaMofReflectionCommand();
      javaMofRef.setClientProject(clientProject);
      javaMofRef.setProxyBean(((JavaHelpers)pd.getPropertyType()).getQualifiedName());
      javaMofRef.setEnvironment( env_ );
      status = javaMofRef.execute( null, null );
      if (status.getSeverity()==Status.ERROR)
      	return status;
      
      status = vAction.visit(javaMofRef.getJavaClass());
    }
    else if (typeNavigator instanceof Field){
      Field field = (Field)typeNavigator;
      JavaMofReflectionCommand javaMofRef = new JavaMofReflectionCommand();
      javaMofRef.setClientProject(clientProject);
      javaMofRef.setProxyBean(((JavaHelpers)field.getEType()).getQualifiedName());
      javaMofRef.setEnvironment( env_ );
     
      status = javaMofRef.execute( null, null );
      if (status.getSeverity()==Status.ERROR)
      	return status;
      
      status = vAction.visit(javaMofRef.getJavaClass());
    }
    
    return status;
  }
  
        
  
}

