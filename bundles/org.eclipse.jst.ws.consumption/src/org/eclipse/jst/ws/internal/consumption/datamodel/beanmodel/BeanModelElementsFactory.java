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

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;

import org.eclipse.jem.java.Field;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaParameter;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.sampleapp.common.SamplePropertyDescriptor;
import org.eclipse.jst.ws.internal.datamodel.Element;
import org.eclipse.jst.ws.internal.datamodel.Model;

/**
 * TypeFactory
 * Creation date: (4/10/2001 12:41:48 PM)
 * @author: Gilbert Andrews
 */
public class BeanModelElementsFactory 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  public static final String RETURN_PARAM_NAME="returnp";

  private BeanModelElementsFactory(){}

  public static Element getBeanModelElement(Object object)
  {
    return BeanModelElementsFactory.getBeanModelElement(object,null,null);
  }

  public static Element getBeanModelElement(Object object,Element parentElement)
  {
    return BeanModelElementsFactory.getBeanModelElement(object,parentElement,null);
  }
  
 /*
  * Take in an object and return an element depending on what 
  * type we are dealing with from the javamof
  **/
  public static Element getBeanModelElement(Object object,Element parentElement, Model model)
  {
    Element returnElement = null;

    if (object instanceof JavaClass){
      JavaClass javaClass = (JavaClass)object;
      String javaName = javaClass.getJavaName();
      if (!javaClass.isArray()){ 
	      if(parentElement == null && model ==null) returnElement = new BeanElement(javaName);
	      else if(parentElement == null && model !=null) returnElement = new BeanElement(javaName,model,false);
	      else{
	        if(parentElement instanceof ParameterElement){ 
	           ParameterElement tempElement = (ParameterElement)parentElement;
	           returnElement = new BeanElement(tempElement,javaName);
	        }
	        else if(parentElement instanceof AttributeElement){
	           AttributeElement tempElement = (AttributeElement)parentElement;
	           returnElement = new BeanElement(tempElement,javaName);
	        }
            else if(parentElement instanceof FieldElement){
               FieldElement tempElement = (FieldElement)parentElement;
	           returnElement = new BeanElement(tempElement,javaName);
            }
            else if(parentElement instanceof MethodElement){
               returnElement = new ParameterElement((MethodElement)parentElement,RETURN_PARAM_NAME,MethodElement.REL_RETURN_PARAMETERS,true);
            }	         
	      }
      } 
      else{
          if(parentElement == null && model ==null) returnElement = new ArrayElement(javaName);
	      else if(parentElement == null && model !=null) returnElement = new ArrayElement(javaName,model,false);
	      else{
	        if(parentElement instanceof ParameterElement){ 
	           ParameterElement tempElement = (ParameterElement)parentElement;
	           returnElement = new ArrayElement(tempElement,javaName);
	        }
	        else if(parentElement instanceof AttributeElement){
	           AttributeElement tempElement = (AttributeElement)parentElement;
	           returnElement = new ArrayElement(tempElement,javaName);
	        }
            else if(parentElement instanceof FieldElement){
               FieldElement tempElement = (FieldElement)parentElement;
	           returnElement = new ArrayElement(tempElement,javaName);
	        }
            else if(parentElement instanceof MethodElement){
               returnElement = new ParameterElement((MethodElement)parentElement,RETURN_PARAM_NAME,MethodElement.REL_RETURN_PARAMETERS,true);
            }

	      }
       }

    }
    else if(object instanceof SamplePropertyDescriptor){
      SamplePropertyDescriptor pd = (SamplePropertyDescriptor)object;
      returnElement = new AttributeElement((BeanElement)parentElement,pd.getName());  
    }
    else if(object instanceof Field){
      Field field = (Field)object;
      returnElement = new FieldElement((BeanElement)parentElement,field.getName());
    }
    else if (object instanceof Method){
      Method method = (Method)object;
      returnElement = new MethodElement((BeanElement)parentElement,method.getName(), method.getMethodElementSignature());
    }
    //non beaninfo
    else if (object instanceof String){
      String name = (String)object;  
      returnElement = new AttributeElement((BeanElement)parentElement,name);
    }
    else if (object instanceof JavaParameter){
      JavaParameter javaParameter = (JavaParameter)object;

      
      if (javaParameter.isReturn()) 
         returnElement = new ParameterElement((MethodElement)parentElement,RETURN_PARAM_NAME,MethodElement.REL_RETURN_PARAMETERS,true); 
      else{ 
         //a java parameter gives us the qualified name we dont want this
         String name = javaParameter.getQualifiedName();
         String shortName;
         int index = name.lastIndexOf(".");
         if (index != -1){
           index++;
           shortName = name.substring(index);
         }
      else shortName = name;

         returnElement = new ParameterElement((MethodElement)parentElement,shortName,MethodElement.REL_PARAMETERS,false); 

      }
     
    }
    else if (object instanceof JavaHelpers){
      JavaHelpers javaHelper = (JavaHelpers)object;
      String javaName; 
      if (javaHelper.isArray()) javaName = TypeFactory.ARRAY_NAME;
      else javaName = javaHelper.getJavaName();
      if (parentElement instanceof ParameterElement){
          returnElement = new SimpleElement((ParameterElement)parentElement,javaName,javaHelper.isPrimitive());
      }
      else if(parentElement instanceof AttributeElement){
          returnElement = new SimpleElement((AttributeElement)parentElement,javaName,javaHelper.isPrimitive());
      }
      else if(parentElement instanceof FieldElement){
          returnElement = new SimpleElement((FieldElement)parentElement,javaName,javaHelper.isPrimitive());
      }
      else if(parentElement instanceof MethodElement){
      	returnElement = new ParameterElement((MethodElement)parentElement,RETURN_PARAM_NAME,MethodElement.REL_RETURN_PARAMETERS,true);
      }
    }
    return returnElement;
  }
  
  
  


}

