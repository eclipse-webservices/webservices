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
package org.eclipse.wst.command.internal.env.core.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.wst.command.internal.provisional.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.data.Transformer;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;


public class DataFlowManager 
{
  private DataMappingRegistryImpl registry_;
  private Hashtable               classTable_;
  private int                     order_;
  private IEnvironment				environment_;
  
  public DataFlowManager( DataMappingRegistryImpl registry, IEnvironment environment )
  {
    registry_   = registry;
    classTable_ = new Hashtable();
    order_      = 0;
    environment_ = environment;
  }
  
  public DataMappingRegistry getMappingRegistry()
  {
    return registry_; 
  }
  
  public void process( Object object )
  {
    // Add this object to the classTable_ if required.
    String     objectType = object.getClass().getName();
    ClassEntry classEntry = (ClassEntry)classTable_.get( objectType );
	
    environment_.getLog().log(ILog.INFO, "data", 5004, this, "process", "Processing: " + objectType );
    
    if( classEntry == null )
    {
      classEntry = new ClassEntry();
      classTable_.put( objectType, classEntry );
    }
    
    classEntry.lastObject_ = object;
    classEntry.order_      = order_++;
    
    // Now process the setters for this object
    Vector ruleEntries  = registry_.getRuleEntries( objectType );
    
    if( ruleEntries != null )
    {
      if( classEntry.setterList_ == null )
      {
        classEntry.setterList_ = getSetterList( object );  
      }    
      
      // For each setter in this object try to find a rule.
      for( int setterIndex = 0; setterIndex < classEntry.setterList_.size(); setterIndex++ )
      {
        ObjectMethod currentObjectMethod = new ObjectMethod();
        Method       setterMethod        = (Method)classEntry.setterList_.elementAt( setterIndex );
        RuleEntry    currentRuleEntry    = null;

        currentObjectMethod.order = -1;
        
        // Find rules that match this setter.  Note: there can be more than one rule
        // that matches this setter.  In this case we use the most recent, which is 
        // defined by the order field.
        for( int index = 0; index < ruleEntries.size(); index++ )
        {
          RuleEntry ruleEntry = (RuleEntry)ruleEntries.elementAt( index );       
          
          if( setterMethod.getName().equals( "set" + ruleEntry.targetProperty_ ) )
          {
            // We found a setter for this rule.  Now find the getter method.
            // Note: getGetterMethod always returns a value, but if there is no
            //       getters available it will set the order to -1.
            ObjectMethod getter = getGetterMethod( ruleEntry.sourceType_, ruleEntry.sourceProperty_ );
            
            if( getter.order == -1 )
            {
            	environment_.getLog().log(ILog.INFO , "data", 5005, this, "process", "  >>No getter found for property: " + setterMethod.getName());
            }
            
            if( currentObjectMethod.order < getter.order ) 
            {
              // We found a more recent getter.
              currentObjectMethod = getter;
              currentRuleEntry = ruleEntry;
            }
          }
        } 
        
        if( currentObjectMethod.order != -1 )
        {
          invokeMethod( currentObjectMethod.object, 
                        currentObjectMethod.method, 
                        object, 
                        setterMethod, 
                        currentRuleEntry.transformer_ );
        }
        else
        {
        	environment_.getLog().log(ILog.INFO, "data", 5006, this, "process", "  >>No rule found for setter: " + setterMethod.getName() );
        }
      }
    }   
  }
  
  /**
   * Find all the setters for this object and return a vector of them.
   * 
   * @param object
   * @return
   */
  private Vector getSetterList( Object object )
  {
    Vector result = new Vector();
    
    Method[] methods = object.getClass().getMethods();
    
    for( int index = 0; index < methods.length; index++ )
    {
      Method  method     = methods[index];
      boolean isPublic   = Modifier.isPublic( method.getModifiers() );
      Class   returnType = method.getReturnType();
    
      if( isPublic && 
          returnType == Void.TYPE && 
          method.getParameterTypes().length == 1 &&
          method.getName().startsWith( "set" ))
      {
        method.setAccessible( true );
        result.add( method );
      }
    }
    
    return result;
  }
  
  private ObjectMethod getGetterMethod( String sourceType, String sourceProperty )
  {
    ClassEntry   classEntry = (ClassEntry)classTable_.get( sourceType );
    ObjectMethod getterFound = new ObjectMethod();
    
    // Indicate that there is no getter yet.
    getterFound.order = -1;
    
    if( classEntry != null )
    {
      if( classEntry.getterList_ == null )
      {
        // Build the getter list.
        classEntry.getterList_ = getGetterList( classEntry.lastObject_ );
      }
      
      for( int index = 0; index < classEntry.getterList_.size(); index++ )
      {
        Method getter = (Method)classEntry.getterList_.elementAt( index );
        
        if( getter.getName().equals( "get" + sourceProperty ))
        {
          getterFound.order  = classEntry.order_;
          getterFound.method = getter;
          getterFound.object = classEntry.lastObject_;
          break;
        }
      }
    }
    
    return getterFound;
  }
  
  private Vector getGetterList( Object object )
  {
    Vector result = new Vector();
    
    Method[] methods = object.getClass().getMethods();
    
    for( int index = 0; index < methods.length; index++ )
    {
      Method  method     = methods[index];
      boolean isPublic   = Modifier.isPublic( method.getModifiers() );
      Class   returnType = method.getReturnType();
    
      if( isPublic && 
          returnType != Void.TYPE && 
          method.getParameterTypes().length == 0 &&
          method.getName().startsWith( "get" ))
      {
        method.setAccessible( true );
        result.add( method );
      }
    }
    
    return result;
  }
    
  private void invokeMethod( Object      sourceObject, 
                             Method      sourceMethod , 
                             Object      clientObject, 
                             Method      clientMethod,
                             Transformer transformer)
  {  	
  	Object data = null;
  	  	  	
  	try
  	{
  	  data = sourceMethod.invoke( sourceObject, new Object[0] );
  	}
  	catch( InvocationTargetException exc )
  	{
  	  exc.printStackTrace();
      // pgm Need to externalize this string.
      throw new IllegalArgumentException( "Provider \"" + sourceObject.getClass().getName() +
                                          "\" threw an exception." );
  	}
  	catch( IllegalAccessException exc )
  	{
  	  exc.printStackTrace();
      // pgm Need to externalize this string.
      throw new IllegalArgumentException( "Provider \"" + sourceObject.getClass().getName() +
                                        "\" threw an exception." );
  	}
  	
  	environment_.getLog().log(ILog.INFO, "data", 5007, this, "invokeMethod ","  Setting prop: " + clientMethod.getName() + " data=" + data + " from: " + sourceObject.getClass().getName() );
  	
  	
  	if( transformer != null )
  	{
  	  data = transformer.transform( data );  	    
  	}
  	
  	try
  	{  	  
  	  clientMethod.invoke( clientObject, new Object[]{ data } );
  	}
  	catch( InvocationTargetException exc )
  	{
  	  exc.printStackTrace();
      // pgm Need to externalize this string.
      throw new IllegalArgumentException( "Client \"" + clientObject.getClass().getName() +
                                          "\" threw an exception." );
  	}
  	catch( IllegalAccessException exc )
  	{
  	  exc.printStackTrace();
      // pgm Need to externalize this string.
      throw new IllegalArgumentException( "Client \"" + clientObject.getClass().getName() +
                                          "\" threw an exception." );
  	}
  }
  
  private class ObjectMethod
  {
    public Object object;
    public Method method;
    public int    order;
  }
}
