/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;
        
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;


public class ReferenceManager
{                            
  protected static InternalAdapterFactory internalAdapterFactory = new InternalAdapterFactory();

                                                 
  public interface Listener
  {
    public void bindingsChanged( Operation operation);
  }     

            
  public static void adaptDefinition(Definition definition)
  {
    internalAdapterFactory.adapt(definition);
    for (Iterator i = definition.getEBindings().iterator(); i.hasNext(); )
    {
      Binding binding = (Binding)i.next();
      internalAdapterFactory.adaptBinding(binding);
    }
  }

                               
  public static void addBindingListener(Operation operation, Listener listener)
  {                 
    OperationAdapter adapter = (OperationAdapter)internalAdapterFactory.adapt(operation);
    if (adapter != null)
    {
      adapter.addListener(listener); 
    }
  }


  public static void removeBindingListener(Operation operation, Listener listener)
  { 
    OperationAdapter adapter = (OperationAdapter)internalAdapterFactory.adapt(operation);
    if (adapter != null)
    {
      adapter.removeListener(listener); 
    }
  }

  protected static void fireNotificationForPortType(PortType portType)
  {
    // get the adapter for each operation and notify listeners
    if (portType != null)
    {
      for (Iterator i = portType.getEOperations().iterator();  i.hasNext(); )
      {
        fireNotificationForOperation((Operation)i.next());
      }
    }
  }


  protected static void fireNotificationForOperation(Operation operation)
  {
    if (operation != null)
    {
      OperationAdapter adapter = (OperationAdapter)internalAdapterFactory.adapt(operation);
      adapter.fireBindingsChanged();
    }
  }


  public static class InternalAdapterFactory extends AdapterFactoryImpl
  {                     
    public Adapter createAdapter(Notifier target)
    {                     
      WSDLSwitch wsdlSwitch = new WSDLSwitch()
      {                
      	public Object caseBinding(Binding binding)
        {   
          return createBindingAdapter();
	      }

 	      public Object caseBindingFault(BindingFault bindingFault)
        {                                      
          return createBindingFaultAdapter();
        }   

	      public Object caseBindingOperation(BindingOperation bindingOperation)
        {   
          return createBindingOperationAdapter();
	      } 
   
      	public Object caseDefinition(Definition definition)
        {   
          return createDefinitionAdapter();
	      }  

      	public Object caseOperation(Operation operation)
        {   
          return createOperationAdapter();
	      }                
      };   

      Object o = wsdlSwitch.doSwitch((EObject)target);

      Adapter result = null;
      if (o instanceof Adapter)
      {
        result  = (Adapter)o;
      }
      else
      {          
        System.out.println("did not create adapter for target : " + target);
        Thread.dumpStack();
      }                   
      //System.out.println("createAdapter(" + target + ") " + result);
      return result;
    }      
                               
               
  	public Adapter createBindingAdapter()
    {
      return new BindingAdapter();
    }        

 	  public Adapter createBindingFaultAdapter()
    {                                      
      return new BindingFaultAdapter();
    }     

    public Adapter createBindingOperationAdapter()
    {     
      return new BindingOperationAdapter();
    }      

  	public Adapter createDefinitionAdapter() 
    {
		  return new DefinitionAdapter();
	  } 

  	public Adapter createOperationAdapter() 
    {
		  return new OperationAdapter();
	  }                                      

    public Adapter adapt(Notifier target)
    {
      return target != null ? adapt(target, internalAdapterFactory) : null;
    }

    protected void adaptBinding(Binding binding)     
    {
      adapt(binding); 
      for (Iterator i = binding.getEBindingOperations().iterator(); i.hasNext(); )
      {
        BindingOperation bindingOperation = (BindingOperation)i.next();
        adapt(bindingOperation);    
        for (Iterator j = bindingOperation.getEBindingFaults().iterator(); j.hasNext(); )
        {
          BindingFault bindingFault = (BindingFault)j.next();
          adapt(bindingFault);  
        }
      }
    }  
  }  
  

  protected static abstract class BaseAdapter extends AdapterImpl
  {
    public boolean isAdapterForType(Object type)
    {
      return type == internalAdapterFactory;
    }   

    public WSDLPackage getPackage()
    {
      return WSDLPackage.eINSTANCE;                        
    }
  }

    
  // DefinitionAdapter
  //
  protected static class DefinitionAdapter extends BaseAdapter
  {                                                                                  
    public void notifyChanged(Notification msg)
    {                                
      if ((EStructuralFeature)msg.getFeature() == getPackage().getDefinition_EBindings())
      {                           
        Object newValue = msg.getNewValue();
        switch (msg.getEventType())
        {
          case Notification.ADD:
          {                                 
            handleAdd((Binding)newValue);
            break;
          }
          case Notification.ADD_MANY:
          {
            for (Iterator newValues = ((Collection)newValue).iterator();  newValues.hasNext(); )
            {
              handleAdd((Binding)newValues.next());
            }
            break;
          }       
        }                                           
      }    
    }     

    protected void handleAdd(Binding binding)
    {  
      internalAdapterFactory.adaptBinding(binding);
      PortType portType = binding.getEPortType();
      if (portType != null)
      {
        fireNotificationForPortType(portType);
      }
    }
  }     
          
  
  protected static class BindingAdapter extends BaseAdapter
  {                      
    public void notifyChanged(Notification msg)
    { 
      if ((EStructuralFeature)msg.getFeature() == getPackage().getBinding_EPortType())
      {
        // fireNotifcation for all of the operation of the old and new portType
        fireNotificationForPortType((PortType)msg.getNewValue());
        fireNotificationForPortType((PortType)msg.getOldValue());
      }                                                                        
      else if ((EStructuralFeature)msg.getFeature() == getPackage().getBinding_EBindingOperations())
      {
        // fireNotifcation for all operations that were added or removed
        Object newValue = msg.getNewValue();
        Object oldValue = msg.getNewValue();
        switch (msg.getEventType())
        {
          case Notification.ADD:
          {                                                                    
            handleAdd((BindingOperation)newValue);
            break;
          }
          case Notification.ADD_MANY:
          {
            for (Iterator newValues = ((Collection)newValue).iterator();  newValues.hasNext(); )
            {
              handleAdd((BindingOperation)newValues.next());
            }
            break;
          }       
          case Notification.REMOVE:
          {                                                                    
            handleRemove((BindingOperation)oldValue);
            break;
          }
          case Notification.REMOVE_MANY:
          {
            for (Iterator oldValues = ((Collection)oldValue).iterator();  oldValues.hasNext(); )
            {
              handleRemove((BindingOperation)oldValues.next());
            }
            break;
          }  
        }                  
      }
    }    

    protected void handleAdd(BindingOperation bindingOperation)
    {
      internalAdapterFactory.adapt(bindingOperation);
      Operation operation = ComponentReferenceUtil.computeOperation(bindingOperation);                                                            
      fireNotificationForOperation(operation);
    }               

    protected void handleRemove(BindingOperation bindingOperation)
    {                 
      if (bindingOperation != null)
      {
        Operation operation = ComponentReferenceUtil.computeOperation(bindingOperation);                                                            
        fireNotificationForOperation(operation);
      }
    }   
  }       
  


  protected static class BindingOperationAdapter extends BaseAdapter
  {                                      
    public void notifyChanged(Notification msg)
    {                                
      BindingOperation bindingOperation = (BindingOperation)getTarget();
      Operation operation = ComponentReferenceUtil.computeOperation(bindingOperation);                                                            
      fireNotificationForOperation(operation);
    }                
  }
  

  protected static class BindingFaultAdapter extends BaseAdapter
  {                                  
    public void notifyChanged(Notification msg)
    {                                       
      BindingFault bindingFault = (BindingFault)getTarget();
      BindingOperation bindingOperation = (BindingOperation)bindingFault.eContainer();
      Operation operation = ComponentReferenceUtil.computeOperation(bindingOperation);                                                            
      fireNotificationForOperation(operation);
    }                
  } 
  

  protected static class OperationAdapter extends BaseAdapter 
  {     
    protected Definition definition;
    protected List listeners = new ArrayList();

    public void addListener(Listener listener)
    {
      listeners.add(listener);
    }

    public void removeListener(Listener listener)
    {
      listeners.remove(listener);
    }   

    public void fireBindingsChanged()
    {                            
      Operation operation = (Operation)getTarget();
      for (Iterator i = listeners.iterator(); i.hasNext(); )
      {                                   
        Listener listener = (Listener)i.next();
        listener.bindingsChanged(operation);
      }
    }         
  }   
}