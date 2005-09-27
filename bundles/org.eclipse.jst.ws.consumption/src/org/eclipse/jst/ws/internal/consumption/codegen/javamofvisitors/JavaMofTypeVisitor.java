/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors;

import org.eclipse.jem.java.Field;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.impl.FieldImpl;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;



/**
* Objects of this class represent a visitor.
* */
public class JavaMofTypeVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private Environment env_;

  private String clientProject;

  /*
  * Constructor
  **/
  public JavaMofTypeVisitor(Environment env)
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
  public Status run ( Object typeNavigator, VisitorAction vAction)
  {
  	Status status = new SimpleStatus("");
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
      status = EnvironmentUtils.convertIStatusToStatus(javaMofRef.execute( null, null ));
      if (status.getSeverity()==Status.ERROR)
      	return status;
      
      status = vAction.visit(javaMofRef.getJavaClass());
    }
    else if (typeNavigator instanceof Field){
      FieldImpl field = (FieldImpl)typeNavigator;
      JavaMofReflectionCommand javaMofRef = new JavaMofReflectionCommand();
      javaMofRef.setClientProject(clientProject);
      javaMofRef.setProxyBean(field.getJavaType().getQualifiedName());
      javaMofRef.setEnvironment( env_ );
     
      status = EnvironmentUtils.convertIStatusToStatus(javaMofRef.execute( null, null ));
      if (status.getSeverity()==Status.ERROR)
      	return status;
      
      status = vAction.visit(javaMofRef.getJavaClass());
    }
    
    return status;
  }
  
        
  
}

