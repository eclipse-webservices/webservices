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

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jem.java.Field;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.JavaVisibilityKind;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.java.impl.FieldImpl;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


/**
* Objects of this class represent a visitor.
* */
public class JavaMofFieldVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  private String clientProject;

  /*
  * Constructor
  **/
  public JavaMofFieldVisitor()
  {
  }

  /*
  * Use this to reflect 
  */
  public void setProject(String clientProject)
  {
    this.clientProject = clientProject;
  }

   /*
  * Use this to reflect 
  */
  public String getProject()
  {
    return clientProject;
  }

  /*
  * Get the attribute belonging to this complex type 
  * @param JavaParameter javaParameter that owns the type
  * @param VisitorAction Action to be performed on each method
  **/
  public Status run ( Object javaclass, VisitorAction vAction)
  {
  	Status status = new SimpleStatus("");
    JavaClass javaClass = (JavaClass)javaclass;     

    boolean holderClass = false;
    EList implemented = javaClass.getImplementsInterfaces();
	for (int i = 0; i < implemented.size(); i++) {
	  JavaClass anInterface = (JavaClass) implemented.get(i);
	  if (anInterface.getQualifiedName().equals("javax.xml.rpc.holders.Holder"))
	    holderClass = true;
	}      

     //beaninfo code
    if(holderClass){     
      EList e  = javaClass.getFields();
      ListIterator list = e.listIterator();
      while(list.hasNext()){
        Field field = (Field)list.next();     
        if(field.getJavaVisibility().getValue() == JavaVisibilityKind.PUBLIC && !field.isFinal() && !field.isStatic()){
          if(fieldCheck((FieldImpl)field))
            status = vAction.visit(field);
        }
      }
    }
    
    return status;
  }       
  private boolean fieldCheck(FieldImpl field)
  {
    // so its a bean make sure it has a default constructor 
    JavaMofReflectionCommand javaMofRef = new JavaMofReflectionCommand();
    javaMofRef.setProxyBean(field.getJavaType().getQualifiedName());
    javaMofRef.setClientProject(getProject());
    javaMofRef.execute(null);
    if(javaMofRef.getJavaClass() instanceof JavaClass){
      if(TypeFactory.recognizedBean(javaMofRef.getJavaClass().getJavaName())) return true;
      return defaultCheck((JavaClass)javaMofRef.getJavaClass());
    }
    return true;
  }
        
 private boolean defaultCheck(JavaClass javaClass){
    
   Iterator m=javaClass.getMethods().iterator();
   //now check for a default constructor
   boolean defaultConst = true;
   while (m.hasNext()) {
    Method method=(Method)m.next();
    if (javaClass.getName().equals(method.getName())){
      //now the inputs
      JavaParameter javaParameter[] = method.listParametersWithoutReturn();
      if (javaParameter.length > 0){
        //then we have no default constructor
        defaultConst = false; 
      }
      else if(javaParameter.length == 0){
        if (method.getJavaVisibility().getValue() == 0)
          return true;
        else if(method.getJavaVisibility().getValue() == 1) 
          defaultConst = false;
      }
    }
  }
    
  return defaultConst;
  }
}
