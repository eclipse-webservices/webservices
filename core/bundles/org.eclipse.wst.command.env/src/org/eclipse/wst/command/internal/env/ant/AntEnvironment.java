/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060523   133714 joan@ca.ibm.com - Joan Haggarty
 * 20060726   151614 pmoogk@ca.ibm.com - Peter Moogk
 * 20070314   176886 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ant;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.env.EnvironmentMessages;
import org.eclipse.wst.command.internal.env.core.CommandManager;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.core.data.BeanModifier;
import org.eclipse.wst.command.internal.env.core.data.ClassEntry;
import org.eclipse.wst.command.internal.env.core.data.Transformer;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.eclipse.IEclipseStatusHandler;
import org.eclipse.wst.command.internal.env.plugin.EnvPlugin;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * 
 * Access to status handler, log, resource context and command manager.  
 * Initializes data for commands from Ant property files based on antDataMapping extensions. 
 * 
 * @author joan
 *
 */

public class AntEnvironment extends EclipseEnvironment{
	
	private Hashtable antProperties_;
	private Hashtable operationDataRecord_ = new Hashtable();
	private boolean mappingComplete_;
	private ClassEntry classEntry;	
		
	// extensionPoint names and namespace
	private static String MAPPER_EXT_PT = "antDataMapping";  //$NON-NLS-1$
	private static String SCENARIO_EXT_PT = "antScenario";  //$NON-NLS-1$
	private static String EXT_PT_NAMESPACE = "org.eclipse.wst.command.env"; ////$NON-NLS-1$
	
	// antDataMapping extension point attributes
	private static final String MAPPER_OPERATION_ATTRIBUTE= "operation"; //$NON-NLS-1$
	private static final String MAPPER_KEY_ATTRIBUTE= "key"; //$NON-NLS-1$
	private static final String MAPPER_PROPERTY_ATTRIBUTE= "property"; //$NON-NLS-1$
	private static final String MAPPER_TRANSFORM_ATTRIBUTE= "transform"; //$NON-NLS-1$
	private static final String MAPPER_REQUIRED_ATTRIBUTE= "required"; //$NON-NLS-1$
		
	// antScenario extension point attributes
	private static final String SCENARIO_TYPE_ATTRIBUTE = "scenarioType"; //$NON-NLS-1$
	private static final String SCENARIO_CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	// Ant property IDs
	private static final String VERBOSE_PROPERTY = "Verbose"; //$NON-NLS-1$
	private static final String OVERWRITE_PROPERTY = "OverwriteFilesEnabled"; //$NON-NLS-1$
	private static final String CREATEFOLDER_PROPERTY = "CreateFoldersEnabled"; //$NON-NLS-1$
	private static final String CHECKOUT_PROPERTY = "CheckoutFilesEnabled"; //$NON-NLS-1$
	private static final String SCENARIO_TYPE_PROPERTY = "ScenarioType"; //$NON-NLS-1$
	
    private AntController controller_;
	
	public AntEnvironment(AntController controller, TransientResourceContext context, IEclipseStatusHandler handler, Hashtable properties)
	{
	   super(controller.getOperationManager(), context, handler);
	   antProperties_ = properties;	
	   controller_ = controller;	
	   setContext( context );
	}
	
	private void setContext(TransientResourceContext context) 
	{
	  Boolean overwriteSet    = getBooleanProperty( OVERWRITE_PROPERTY );
	  Boolean createfolderSet = getBooleanProperty( CREATEFOLDER_PROPERTY );
	  Boolean checkoutSet     = getBooleanProperty( CHECKOUT_PROPERTY );
	  
	  if( overwriteSet != null ) context.setOverwriteFilesEnabled( overwriteSet.booleanValue() );
	  if( createfolderSet != null ) context.setCreateFoldersEnabled( createfolderSet.booleanValue() );
	  if( checkoutSet != null ) context.setCheckoutFilesEnabled( checkoutSet.booleanValue() );
	}

	public boolean verbose()
	{
	  Boolean result = getBooleanProperty( VERBOSE_PROPERTY );
	  
	  return result == null ? false : result.booleanValue();
	}
	
	public Boolean getBooleanProperty( String property )
	{
	  String  value  = getProperty( property );	
	  Boolean result = null;
		  
	  if( value != null )
	  {
		value = value.toLowerCase();
		result = new Boolean( value.equals( "true") );
	  }
	  
	  return result;
	}
	
	// returns String since the property table built by Ant is property value pairs where the value is a String
	private String getProperty(String key)
	{
		Object property = antProperties_.get(key);
		if (property != null && (!property.toString().equals("")))
			return property.toString().trim();
		return null;
	}
	
	// call from engine prior to executing the operation 
	public IStatus initOperationData(AbstractDataModelOperation op)
	{
		//check to see if data has already been primed for this operation 
		String qualifiedClassName = op.getClass().getName();
		if (operationDataRecord_.get(qualifiedClassName) == null)
		{
			classEntry = new ClassEntry();
			
			try {
				//extension lookup for the bean - may be more than one property for it
				Enumeration operationData = getMappingExtensions(op);					
				classEntry.setterList_= getSetterList(op);		
				while (operationData.hasMoreElements())
				{				
					PropertyDataHolder mapping = (PropertyDataHolder)operationData.nextElement();
						
					    mappingComplete_ = false;
						String property = mapping.property_;
						String setterMethodName = createSetterName(property);		
												
						int step = 1;
						while (!mappingComplete_)
						{							
						   switch (step) {
							case 1:
								mappingComplete_ = transformAndSet(mapping, setterMethodName);								
								break;								
							case 2:
								mappingComplete_ = callSetter(mapping.operation_, mapping.value_, setterMethodName);
								break;
								
							case 3:
								mappingComplete_ = callPrimitiveSetter(mapping);
								break;
							
							case 4:
								mappingComplete_ = callSetterConstructor(mapping);
								break;
								
							default:
								mappingComplete_ = true;
								break;								
							}
						   step++;
						}					
				  }				
                //add operation to the record - no need to initialize again...
                operationDataRecord_.put(qualifiedClassName, "");
			}			
			catch (Exception e)
			{					                                
				throw new IllegalArgumentException(e.getMessage());
			}

		}
		return Status.OK_STATUS;
	}

	/**
	 * Creates setter name based on the property passed in.  If the 
	 * property has any leading qualifiers they are stripped off.
	 * The property is capitalized and set is prepended.
	 * @param property  The name of the property that requires a setter.
	 * @return
	 */	
	private String createSetterName(String property)
	{		
		while (property.indexOf(".")>=0)
		{
			property=property.substring(property.indexOf(".")+1);
		}		 
		 String firstChar = property.substring(0,1);
		 firstChar = firstChar.toUpperCase();
		 property = firstChar + property.substring(1);  
		 String setterName = "set" + property;		 
		 return setterName;		 
	}	
	
	   /**
	    * Retrieves extensions for the org.eclipse.wst.command.env antDataMapping
	    * extension point.  Extracts those that with class attribute values that match operationName.
	    * All mappings are converted to PropertyDataHolder objects.
	    * Any m:1 Ant key to property mappings are collected into a key-value map within
	    * a single PropertyDataHolder.  
	    * @param operationName The name of the operation that is being initialized.
	    * @return A collection of PropertyDataHolder objects. Returns null if there are no extensions matching the operationName.
	    */
	   private Enumeration getMappingExtensions(AbstractDataModelOperation operation) throws CoreException
	   {	   		   
		   String operationName = operation.getClass().getName();
		   //go to ext registry and get all antMapping extensions
		   IExtensionRegistry reg = Platform.getExtensionRegistry();
		   IExtensionPoint extPt = reg.getExtensionPoint(EXT_PT_NAMESPACE, MAPPER_EXT_PT);
		   Hashtable dataTable = new Hashtable(25);
		   
		   IConfigurationElement[] elements = extPt.getConfigurationElements();
		   
		   for (int i = 0; i < elements.length; i++) {
			
			 IConfigurationElement ce = elements[i];
			 Object obj = ce.getAttribute(MAPPER_OPERATION_ATTRIBUTE);
			 // look for mappings for this operation
			 if (obj.equals(operationName))
			 {				 
				 String key = ce.getAttribute(MAPPER_KEY_ATTRIBUTE);
				 String value = getProperty(key);
				 
				 //check to see if the property for this extension is already in the data table
				 // if so, there is a m:1 mapping
				 if (value != null)  //only do a mapping if there is an Ant property value passed in...
				 {
				
					 String property = ce.getAttribute(MAPPER_PROPERTY_ATTRIBUTE);
					 Object transform = null;
					 try
					 {
						 //check to make sure there is an transform attribute provided
						 //  if so, get the class to do the transformation
						 if (ce.getAttribute(MAPPER_TRANSFORM_ATTRIBUTE)!= null)
							 transform = ce.createExecutableExtension(MAPPER_TRANSFORM_ATTRIBUTE);	 
					 }
					 catch (CoreException cex) {
					   Status errorStatus = new Status(Status.ERROR, "ws_ant", 5092, cex.getMessage(), cex);
					   getStatusHandler().reportError(errorStatus);
					   getLog().log(ILog.ERROR, "ws_ant", 5092, this, "getMappingExtensions", EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_DATA_TRANSFORM, key, transform));
					   throw new CoreException(errorStatus);
					 }
					 
					 if (transform != null && transform instanceof BeanModifier/*dataTable.containsKey(property)*/)
					 {
						 //get the PropertyDataHolder from the table
						 PropertyDataHolder holder = (PropertyDataHolder)dataTable.get(property);
						 if (holder == null)
						 {
							 holder = new PropertyDataHolder();
							 holder.key_ = "";
							 holder.value_ = "";	
							 holder.transform_ = transform;
							 holder.operation_ = operation;
							 holder.property_ = property;
							 holder.map_ = new HashMap();
							 holder.map_.put(key, value);
							 dataTable.put(property, holder);
						 }						 
						 else
						 {						
							holder.map_.put(key, value);
						 }					
					 }
					 else  //plain property mapping not a bean
					 {
						 PropertyDataHolder holder = new PropertyDataHolder();
						 holder.operation_ = operation;
						 holder.key_ = key;
						 holder.property_ = property;
						 holder.transform_ = transform;
						 holder.value_ = value;
						 dataTable.put(property, holder);
					 }			 
				 }
				 else if(ce.getAttribute(MAPPER_REQUIRED_ATTRIBUTE)!=null && ce.getAttribute(MAPPER_REQUIRED_ATTRIBUTE).equals("true"))
				 {				
					 String msg = EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_REQUIRED_PROPERTY, key.toString());
					 Status statusObj = new Status(IStatus.ERROR, 
								EnvPlugin.ID,
								IStatus.ERROR,
								msg, 
								null);
					 getStatusHandler().reportError(statusObj);
	                 getLog().log(ILog.ERROR, "ws_ant", 9999, this, "getMappingExtensions", msg);					 
				 }
				 else if (verbose())
				 {
					 String msg = EnvironmentMessages.bind(EnvironmentMessages.MSG_INFO_ANT__PROPERTY_DEFAULT, key.toString());
					 Status statusObj = new Status(IStatus.INFO, 
								EnvPlugin.ID,
								IStatus.INFO,
								msg, 
								null);
					 getStatusHandler().reportInfo(statusObj);
					 getLog().log(ILog.INFO, "ws_ant", 9999, this, "getMappingExtensions", msg);					 
				 }
		      }    	 
		  }
		   return dataTable.elements();
		}     
	   
	   private boolean transformAndSet(PropertyDataHolder mapping, String setterMethodName)
	   {
			Object transform = mapping.transform_;
			if (transform != null)
			{
				// get transform class & create setter parameters		
				try
				{
					//Object classObject = Class.forName(transform).newInstance();
					Object param = new Object();
					if (transform instanceof Transformer)
					{
						Transformer transformer = (Transformer)transform;						
						// transform the property value
						param = transformer.transform(mapping.value_);						
					}
					else if (mapping.map_ != null && transform instanceof BeanModifier)
					{
						  BeanModifier modifier = (BeanModifier)transform;		
                          Method getter = getGetterMethod(mapping);
						  param = getter.invoke(mapping.operation_, new Object[]{});						  
						  modifier.modify(param, mapping.map_);
					}												
					return callSetter(mapping.operation_, param, setterMethodName);
					
				}
				catch (Exception exc)
				{
					getStatusHandler().reportError(new Status(Status.ERROR, "ws_ant", 5093, exc.getMessage(), exc));
                    getLog().log(ILog.ERROR, "ws_ant", 5093, this, "transformAndSet", EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_DATA_TRANSFORM, mapping.key_, mapping.transform_));
                    throw new IllegalArgumentException(exc.getMessage());
				}				
			}
			return false;
	   }	   
	   
	   private Vector getSetterList(Object op)
	   {
		   Vector result = new Vector();
           Method[] methods = op.getClass().getMethods();		     
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
	   
	   private Method getGetterMethod(PropertyDataHolder mapping)
	   {
	       Method getterFound = null;     

	       if (classEntry.getterList_ == null)
	       {
	    	   classEntry.getterList_ = getGetterList(mapping.operation_);  
	       }
	       
	       for( int index = 0; index < classEntry.getterList_.size(); index++ )
	       {
	         Method getter = (Method)classEntry.getterList_.elementAt( index );
	         
	         if( getter.getName().equals( "get" + mapping.property_ ))
	         {
	            getterFound = getter;
	            break;
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
       
	   private boolean callSetter(AbstractDataModelOperation op, Object param, String setterMethodName) throws CoreException
	   {
		   for (Iterator iterator = classEntry.setterList_.iterator(); iterator.hasNext();) 
		   {
				Method method = (Method) iterator.next();
				if (method.getName().equals(setterMethodName))
				{                                        
			       Class[] paramTypes = method.getParameterTypes();
			       if (paramTypes.length == 1 && param != null)
			       {			  
			        	 try{			      			
			     			 method.invoke(op, new Object[]{param});
			     			 return true;
			     		 }
			     		 catch(Exception cex){
			     			Status errorStatus = new Status(Status.ERROR, "ws_ant", 5094, cex.getMessage(), cex);
			     			getStatusHandler().reportError(errorStatus);
			     			getLog().log(ILog.ERROR, "ws_ant", 5094, this, "callSetter", EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_CALL_SETTER, setterMethodName));
			     			throw new CoreException(errorStatus);
			     		 }
			       }  
				}
			}
		    return false;
	   }
	   
	
	   // look for setter with primitive type parameter - if find one, convert String propertyValue and call it
	   private boolean callPrimitiveSetter(PropertyDataHolder mapping) throws CoreException
	   {			
			for (Iterator iterator = classEntry.setterList_.iterator(); iterator.hasNext();) {
				Method element = (Method) iterator.next();
				Class[] parmTypes = element.getParameterTypes();
				if (parmTypes.length==1 && parmTypes[0].isPrimitive())
				{										
					Class parmType = parmTypes[0].getClass();
					Object setterParm = null;
					if (parmType.equals(Integer.class))
					{
						setterParm = Integer.valueOf(mapping.value_);
					}
					else if (parmType.equals(Boolean.class))
					{
						setterParm = Boolean.valueOf(mapping.value_);
					}
					else if (parmType.equals(Character.class) && mapping.value_.length() == 1)
					{
						setterParm = new Character(mapping.value_.charAt(0));  
					}
					else if (parmType.equals(Byte.class))
					{
						setterParm = Byte.valueOf(mapping.value_);
					}
					else if (parmType.equals(Short.class))
					{
						setterParm = Short.valueOf(mapping.value_);
					}
					else if (parmType.equals(Long.class))
					{
						setterParm = Long.valueOf(mapping.value_);
					}
					else if (parmType.equals(Float.class))
					{
						setterParm = Float.valueOf(mapping.value_);
					}
					else if (parmType.equals(Double.class))
					{
						setterParm = Double.valueOf(mapping.value_);
					}
														
				    if (setterParm != null)
				    {	
				    	try
				    	{
				    		element.invoke(mapping.operation_, new Object[]{setterParm});
					    	return true;	
				    	}
				    	catch(Exception e){
				    		Status errorStatus = new Status(Status.ERROR, "ws_ant", 5095, e.getMessage(), e);
				    		getStatusHandler().reportError(errorStatus);
				    		getLog().log(ILog.ERROR, "ws_ant", 5095, this, "callPrimitiveSetter", EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_CALL_SETTER, element.getName()));
				    		throw new CoreException(errorStatus);
				    	}
				    }			
				}
		    }   
			return false;
	   }
		
		//check for setter with parameter type that takes a String to construct
		// construct the parameter using String & call the setter  
	   private boolean callSetterConstructor(PropertyDataHolder mapping) throws CoreException
	   {	
			for (Iterator iterator = classEntry.setterList_.iterator(); iterator.hasNext();) {
				Method element = (Method) iterator.next();
				Class[] parmTypes = element.getParameterTypes();
				Class[] stringParm = new Class[]{String.class};
				if (parmTypes.length==1)
				{
					try
					{			
						Constructor ctor = parmTypes.getClass().getConstructor(stringParm);
						Object parameter = ctor.newInstance(new Object[]{mapping.value_});
						element.invoke(mapping.operation_, new Object[]{parameter});						
					}
					catch (Exception exc)
					{
						Status errorStatus = new Status(Status.ERROR, "ws_ant", 5096, exc.getMessage(), exc);
						getStatusHandler().reportError(errorStatus);
						getLog().log(ILog.ERROR, "ws_ant", 5096, this, "callSetterConstructor", EnvironmentMessages.bind(EnvironmentMessages.MSG_ERROR_ANT_CALL_SETTER, element.getName()));
						throw new CoreException(errorStatus);
					}
				}
	        }			
		   return false;
	   }

	   /**
	    * Returns an object that helps manage execution/undoing of Commands.
	    */
	   public CommandManager getCommandManager (){
		   return controller_.getOperationManager();
	   } 
	   
	   public CommandFragment getRootCommandFragment() 
       {
    	   
    	   //look up the commandFragment in the scenarioRegistry extension point with an ID corresponding to the scenario property in the propertytable
    	   String scenarioProperty = (String)getProperty(SCENARIO_TYPE_PROPERTY);
    	   IExtensionRegistry reg = Platform.getExtensionRegistry();
		   IExtensionPoint extPt = reg.getExtensionPoint(EXT_PT_NAMESPACE, SCENARIO_EXT_PT);
		   
		   IConfigurationElement[] elements = extPt.getConfigurationElements();
		   
		   for (int i = 0; i < elements.length; i++) {
			
			 IConfigurationElement configElement = elements[i];			 
			 if (configElement.getAttribute(SCENARIO_TYPE_ATTRIBUTE).equals(scenarioProperty))
			 {				 
				 try
				 {					 
					Object obj = configElement.createExecutableExtension(SCENARIO_CLASS_ATTRIBUTE);
		
					if (obj instanceof org.eclipse.wst.command.internal.env.core.fragment.CommandFragment)
					{
						return (org.eclipse.wst.command.internal.env.core.fragment.CommandFragment)obj;
					}
				 }
				 catch (Exception exception)
				 {
					 Status errorStatus = new Status(Status.ERROR, "ws_ant", 5097, exception.getMessage(), exception);
					 getStatusHandler().reportError(errorStatus);
					 getLog().log(ILog.ERROR, "ws_ant", 5097, this, "getRootCommandFragment", EnvironmentMessages.MSG_ERROR_ANT_CMD_FRAGMENT);					 
				 }				 
			  }    	   
           }
		   return null;
       }
  }
