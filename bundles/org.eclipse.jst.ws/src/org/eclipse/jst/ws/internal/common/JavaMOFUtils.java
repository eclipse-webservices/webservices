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

package org.eclipse.jst.ws.internal.common;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.internal.plugin.JavaEMFNature;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jem.java.JavaVisibilityKind;
import org.eclipse.jem.java.Method;

/**
* This class contains some useful utilities for dealing with the JavaMOF
*/

public final class JavaMOFUtils
{
 /**
  * Determine whether this class of the given name is loadable in the given
  * project.
  * @param className The fully qualified name of the class
  * @param project The project
  * @return True if the class is loadable and no exceptions are thrown. 
  */
  public static boolean isClassLoadable(String className, IProject project) throws CoreException
  {
   	return isClassLoadable(getJavaClass(className,project));
  }

  public static boolean isClassLoadable(JavaClass javaClass)
  {
	return javaClass.isExistingType();    
  }

  /**
  * Determine whether the specified class has a public constructor
  * with an empty parameter list.
  * @param className The fully qualified name of the class
  * @param project The project
  * @return True if the class is loadable from the project and the 
  *         class has a public constructor with an empty parameter list  
  */
  public static boolean hasPublicDefaultCtor(String className, IProject project) throws CoreException
  {
    return hasPublicDefaultCtor(getJavaClass(className, project));
  }


 public static boolean hasPublicDefaultCtor(JavaClass javaClass)
  {
   
    List methodList = javaClass.getMethods();
    if (methodList==null)
      return true;

    Iterator iMethods = methodList.iterator();
    boolean userDefinedCtor = false;
    while (iMethods.hasNext())
    {
      Method thisMethod = (Method)iMethods.next();

      if (thisMethod.isConstructor())
      {
        userDefinedCtor = true; 

        //Since the user has defined a ctor, the default ctor is no longer available.
        //We must ensure that the user has provided a public ctor with no parameters.

        //check if public
        if (thisMethod.getJavaVisibility().getValue() == JavaVisibilityKind.PUBLIC)
        {
          //see if parameter list is empty
          List paramList = thisMethod.getParameters();
          if (paramList.isEmpty() )
          {
	        return true;             
          }
        }
      }

    }

    if (userDefinedCtor)
    {
	  return false; //if the user defined a ctor and we still haven't
	  	            //returned true, none of them were public with no parameters.
    }
    else
    { 
       return true;    
    }

  }
  /**
  * Determine whether the specified class implements the specified interface.
  * @param className The fully qualified name of the class
  * @param interfaceName The fully qualified name of the interface
  * @param project The project
  * @return True if the class and interface are loadable from the project and
  *         the class implements the interface.
  */
  public static boolean implementsInterface(String className, String interfaceName, IProject project)
  			throws CoreException
  {
   return implementsInterface(getJavaClass(className, project), getJavaClass(interfaceName, project));
  }

public static boolean implementsInterface(JavaClass javaClass, JavaClass interfaceClass)
  {
   
    if (!javaClass.isExistingType())     return false;

    if (!interfaceClass.isExistingType())  return false;
        
    return javaClass.implementsInterface(interfaceClass);
  }

  /**
  * Determine whether the specified class extends the specified superclass
  * @param className The fully qualified name of the class
  * @param superClassName The fully qualified name of the superclass
  * @param project The project
  * @return True if the class and superClass are loadable from the project and
  *         the class extends the superClass
  */
  public static boolean extendsClass(String className, String superClassName, IProject project)
         throws CoreException
  {
    return extendsClass(getJavaClass(className,project), getJavaClass(superClassName, project));
  }

  public static boolean extendsClass(JavaClass javaClass, JavaClass superClass)
  {
    return superClass.isAssignableFrom(javaClass);
  }

  /**
  * Determine whether the specified class is actually an interface
  * @param className The fully qualified name of the class
  * @param project The project
  * @return True if the class is loadable from the project and is 
  *         actually an interface
  */
  public static boolean isInterface(String className, IProject project) throws CoreException
  {
     return isInterface(getJavaClass(className, project));
  }

 public static boolean isInterface(JavaClass javaClass)
  {
      return javaClass.isInterface();
  }
 public static boolean hasAbstractMethods (String className, IProject project) throws CoreException
 	{
	    return hasAbstractMethods(getJavaClass(className, project));
  	}

  public static boolean hasAbstractMethods (JavaClass javaClass)
 	{
	   List methodList = javaClass.getMethods();
       if (methodList==null)
      	return false;

	   Iterator iMethods = methodList.iterator();
	   while (iMethods.hasNext())
    	{
      		Method thisMethod = (Method)iMethods.next();
			if ( thisMethod.isAbstract()) return true;
	   	}
		return false;
 	}

 public static JavaClass getJavaClass(String className , IProject project) throws CoreException
 	{
 		JavaEMFNature jMOF = (JavaEMFNature)JavaEMFNature.createRuntime(project);
    	return (JavaClass)JavaRefFactory.eINSTANCE.reflectType(className,jMOF.getResourceSet());
 	}

 public static boolean isValidSEIFile(JavaClass beanClass, JavaClass seiClass)
 	{
 		if (!seiClass.isInterface())
 			return false;

		Vector beanMethodsList = new Vector();
 		List beanMethods = beanClass.getMethods();
		Iterator beanMethodsIterator = beanMethods.iterator();
	   	while (beanMethodsIterator.hasNext())
    	{
      		Method thisMethod = (Method)beanMethodsIterator.next();
			beanMethodsList.add(thisMethod.getMethodElementSignature());
	   	}

		List seiMethods = seiClass.getMethods();
 		if (beanMethods == null ) {
 			if ( seiMethods == null) return true;
 			else	return false;
 		}
		if (seiMethods == null) return false;

		Iterator seiMethodsIterator = seiMethods.iterator();
	   	while (seiMethodsIterator.hasNext())
    	{
      		Method thisMethod = (Method)seiMethodsIterator.next();
			if ( !beanMethodsList.contains(thisMethod.getMethodElementSignature())) {
				return false;
			}
	   	}
		return true;
 		
 	}
 
}
