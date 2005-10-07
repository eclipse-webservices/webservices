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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jem.java.impl.FieldImpl;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.command.common.JavaMofReflectionCommand;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
import org.eclipse.wst.common.environment.Environment;


/**
* Objects of this class represent a visitor.
* */
public class JavaMofAttributeVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  private String clientProject;
  private boolean returnParameter;
  private Environment env;
  
  
  public void setEnvironment(Environment env)
  {
  	this.env = env;
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

  public void setReturnParameter(boolean returnParameter)
  {
    this.returnParameter = returnParameter;
  }
 
  
  /*
  * Get the attribute belonging to this complex type 
  * @param JavaParameter javaParameter that owns the type
  * @param VisitorAction Action to be performed on each method
  **/
  public IStatus run ( Object javaclass, VisitorAction vAction)
  {
  	IStatus status = Status.OK_STATUS;
    JavaClass javaClass = (JavaClass)javaclass;     

    Hashtable spdMap = new Hashtable();  
    for (Iterator m=javaClass.getPublicMethods().iterator(); m.hasNext(); ) {
      Method method=(Method)m.next();
      if (((method.getMethodElementSignature().startsWith("get") 
      	|| method.getMethodElementSignature().startsWith("is")) 
        && method.listParametersWithoutReturn().length == 0 )
        || (method.getMethodElementSignature().startsWith("set") 
        && method.listParametersWithoutReturn().length > 0)) {
        int sub = 3;
        if(method.getMethodElementSignature().startsWith("is"))
          sub = 2;	
        String propertyName = method.getMethodElementSignature().substring(sub);    
        String lower = propertyName.substring(0,1).toLowerCase();
        String remainder = propertyName.substring(1); 
        propertyName = lower + remainder;
        int index = propertyName.lastIndexOf("(");
        String temp = propertyName;
        propertyName = temp.substring(0,index); 
        
        //find the propertydescriptor
        SamplePropertyDescriptor spd = (SamplePropertyDescriptor)spdMap.get(propertyName); 
        if(spd == null){
          spd = new SamplePropertyDescriptor(propertyName);
          spdMap.put(propertyName,spd); 
        }
        if(method.getMethodElementSignature().startsWith("set"))
          spd.setWriteMethod(method);
        else 
          spd.setReadMethod(method);

        FieldImpl field = (FieldImpl)javaClass.getFieldNamed(propertyName);
        JavaHelpers propertyType = null; 
        if(field != null){
          propertyType = field.getJavaType();
          spd.setfStatic(field.isStatic());
        }
        else{
          if(method.getMethodElementSignature().startsWith("get") || method.getMethodElementSignature().startsWith("is"))
          	propertyType = method.getReturnType();
          else{
             JavaParameter params[] = method.listParametersWithoutReturn(); 
             propertyType = params[0].getJavaType();
          }  
        }
          	
        spd.setPropertyType(propertyType); 
        
      }
    }
    Enumeration spdEnum = spdMap.elements();
    while(spdEnum.hasMoreElements()){
      SamplePropertyDescriptor spd = (SamplePropertyDescriptor)spdEnum.nextElement();
      if(spdCheck(spd))
        status = vAction.visit(spd);   
    } 
   
    return status;

  }
  
  private boolean spdCheck(SamplePropertyDescriptor spd)
  {  
    //check for indexed properties
  	Method writeMethod = spd.getWriteMethod();
  	if(writeMethod != null)
  	  if(writeMethod.listParametersWithoutReturn().length > 1) return false;
  	
  	if(spd.getPropertyType() == null) return false;
    if(spd.getPropertyType().isPrimitive()) return true;
    if(returnParameter && TypeFactory.isRecognizedReturnType(spd.getPropertyType())) return true;
    if(!returnParameter && TypeFactory.isUnSupportedType(spd.getPropertyType())) return true;
    if(!checkPolarity(spd)) return false;   
       
    // so its a bean make sure it has a default constructor       
    JavaMofReflectionCommand javaMofRef = new JavaMofReflectionCommand();
    javaMofRef.setClientProject(getProject());
    javaMofRef.setProxyBean(((JavaHelpers)spd.getPropertyType()).getQualifiedName());
    javaMofRef.setEnvironment( env );
    javaMofRef.execute(null, null);
    if(javaMofRef.getJavaClass() instanceof JavaClass){
       if(TypeFactory.recognizedBean(javaMofRef.getJavaClass().getJavaName())) return true;
       return defaultCheck((JavaClass)javaMofRef.getJavaClass());
               
    }
    return true;
  
  }
  
  private boolean checkPolarity(SamplePropertyDescriptor spd){
    
  	Method readMethod = spd.getReadMethod();
  	Method writeMethod = spd.getWriteMethod();
  	if((readMethod == null && writeMethod != null) || (readMethod != null && writeMethod == null))
  	  return true;	
  	if(readMethod == null && writeMethod == null)
  	  return false;	
  	  	
  	JavaParameter javaParameter[] = writeMethod.listParametersWithoutReturn();
    for(int i = 0;i< javaParameter.length;i++){
      JavaParameter jp = javaParameter[i]; 	
      jp.getJavaType().getJavaName();
      readMethod.getReturnType().getJavaName();
      if(jp.getJavaType().getJavaName().equals(readMethod.getReturnType().getJavaName()))
        return true; 
    }
  	return false;
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
                }
          }
       }
    
       return defaultConst;
  }
          
}
