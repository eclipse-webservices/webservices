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
package org.eclipse.wst.wsdl.ui.internal.graph.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.graph.ViewMode;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;


public class WSDLGraphModelAdapterFactory extends WSDLModelAdapterFactory
{          
  protected static WSDLGraphModelAdapterFactory wsdlGraphModelAdapterFactoryInstance;

  public static WSDLGraphModelAdapterFactory getWSDLGraphModelAdapterFactory()
  {                
    if (wsdlGraphModelAdapterFactoryInstance == null)
    {
      wsdlGraphModelAdapterFactoryInstance = new WSDLGraphModelAdapterFactory();
    }
    return wsdlGraphModelAdapterFactoryInstance;
  }  
  
  protected AdapterFactory createAdapterFactory()
  {
    return new GraphAdapterFactory();
  }  

  //protected boolean isExtensibilityElementConsideredChild()
  //{
  //  return false;
  //}
        

  // There are many adapters associated with this factory.  Each DOM Node has its own adapter.
  //
  protected class GraphAdapterFactory extends WSDLAdapterFactoryImpl
  {                                                                

    public Adapter createDefinitionAdapter()
    {
      return new GraphDefinitionAdapter();
    } 
    
    public Adapter createBindingAdapter()
    {
      return new GraphBindingAdapter();
    } 

    public Adapter createPartAdapter()
    {
      return new GraphPartAdapter();
    }                                         

    //public Adapter createPortAdapter()
    //{
    //  return new GraphPortAdapter();
    //}

    public Adapter createPortTypeAdapter()
    {
      return new GraphPortTypeAdapter();
    }

    //public Adapter createInputAdapter()
    //{
    //  return new GraphInputAdapter();
    //} 

    //public Adapter createOutputAdapter()
    //{
    //  return new GraphOutputAdapter();
    //}   

    //public Adapter createFaultAdapter()
    //{
    //  return new GraphFaultAdapter();
    //}

  	public Adapter createTypesAdapter() 
    {                    
      return new GraphTypesAdapter();
    }
  }      

        
  
  protected class GraphDefinitionAdapter extends DefinitionAdapter implements ViewMode.Listener
  {                
    protected ViewMode viewMode = new ViewMode();  
 
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);   
      viewMode.addListener(this);         
    }
                                                      
    protected List getChildren()
    {             
      List list = new ArrayList();
      list.add(permanentWSDLGroupObjectList.get(0));
      list.add(permanentWSDLGroupObjectList.get(1));  
      list.add(permanentWSDLGroupObjectList.get(5));
      if (viewMode.isBindingVisible())
      {
        list.add(permanentWSDLGroupObjectList.get(4));
      } 
      list.add(permanentWSDLGroupObjectList.get(3));  
      list.add(permanentWSDLGroupObjectList.get(2));  
      if (extensibilityElementsGroup.getChildren().size() > 0)
      {
        list.add(extensibilityElementsGroup);
      }
      return list;      
    }

  
    public void viewModeChanged(ViewMode mode)
    {
      firePropertyChangedHelper();
    }    

    public ViewMode getViewMode()
    {
      return viewMode;
    }    

    protected ModelAdapterFactory getModelAdapterFactory()
    {
      return getWSDLGraphModelAdapterFactory();
    }
  }




  protected class GraphBindingAdapter extends BindingAdapter
  { /*                                  
    protected List getChildren()
    {                
      List list = new ArrayList();   
      if (binding.getPortType() != null)
      {
        list.add(binding.getPortType());
      }
      return list;
    }*/  
  }
      
                        
  protected class GraphPartAdapter extends PartAdapter
  { /*  
    protected List getChildren()
    {                  
      List list = new ArrayList();

      Object component = part.getType();
      if (component != null) 
      {
        // TODO... i'm pulling a fast one here.... we should call the extension
        // to see if he wants to show a child here
        if (component instanceof org.eclipse.xsd.XSDComplexTypeDefinition)
        {
          list.add(component);
        }
      }
      else
      {
        component = part.getElement();
        if (component != null)
        {
          list.add(component);
        }
      }

      return list;
    }*/   
  }
    
  protected class GraphPortTypeAdapter extends PortTypeAdapter
  {   
    public Object getProperty(Object modelObject, String propertyName)
    {           
      Object result = null;
      if (propertyName.equals("bindings"))
      {    
        ComponentReferenceUtil util = new ComponentReferenceUtil(portType.getEnclosingDefinition());
        result = util.getBindings(portType);
      }
      else
      {
        result = super.getProperty(modelObject, propertyName);
      }   
      return result;
    }  
  }

  protected class GraphPortAdapter extends PortAdapter implements ModelAdapterListener
  {                                                
    protected List getChildren()
    {                
      List list = new ArrayList(); 
    
      Binding binding = (Binding)port.getBinding();

      if (binding != null)
      {
        list.add(binding);
      }
     
      return list;
    }
                     
    // propagate changes to the binding object
    //
    public void propertyChanged(Object object, String property)
    {
      firePropertyChanged(port, null);
    }
  }   
            

  protected class GraphInputAdapter extends InputAdapter
  {
    protected List getChildren()
    {     
      List list = new ArrayList();
      if (input.getMessage() != null)
      {
        list.add(input.getMessage());
      }       
      return list;
    }     

    public Object getProperty(Object modelObject, String propertyName)
    {           
      Object result = null;
      if (propertyName.equals("bindings"))
      {    
        ComponentReferenceUtil util = new ComponentReferenceUtil(input.getEnclosingDefinition());
        result = util.getBindingInputs(input);
      }
      else
      {
        result = super.getProperty(modelObject, propertyName);
      }   
      return result;
    }   
  }


  protected class GraphOutputAdapter extends OutputAdapter
  {
    protected List getChildren()
    {     
      List list = new ArrayList();
      if (output.getMessage() != null)
      {
        list.add(output.getMessage());
      }       
      return list;
    }  

    public Object getProperty(Object modelObject, String propertyName)
    {           
      Object result = null;
      if (propertyName.equals("bindings"))
      {    
        ComponentReferenceUtil util = new ComponentReferenceUtil(output.getEnclosingDefinition());
        result = util.getBindingOutputs(output);
      }
      else
      {
        result = super.getProperty(modelObject, propertyName);
      }   
      return result;
    }   
  }

  protected class GraphFaultAdapter extends FaultAdapter
  {
    protected List getChildren()
    {     
      List list = new ArrayList();
      if (fault.getMessage() != null)
      {
        list.add(fault.getMessage());
      }       
      return list;
    }  

    public Object getProperty(Object modelObject, String propertyName)
    {           
      Object result = null;
      if (propertyName.equals("bindings"))
      {    
        ComponentReferenceUtil util = new ComponentReferenceUtil(fault.getEnclosingDefinition());
        result = util.getBindingFaults(fault);
      }
      else
      {
        result = super.getProperty(modelObject, propertyName);
      }   
      return result;
    }                                     
  }     

  public class GraphTypesAdapter extends TypesAdapter
  {                                             
    protected List getChildren()
    {  
      List list = new ArrayList();
      for (Iterator i = types.getEExtensibilityElements().iterator(); i.hasNext(); )
      {
        Object o = i.next();
        if (o instanceof XSDSchemaExtensibilityElement)
        {
          XSDSchemaExtensibilityElement s = (XSDSchemaExtensibilityElement)o;
          Object schema = s.getSchema();
          if (schema != null)
          {
            list.add(schema);
          }
        } 
        else
        {
          list.add(o);
        }
      }
      return list;
    }                                                                                                         
  }
  /*
  protected class GraphBindingAdapter extends BindingAdapter
  {                                   
    protected List getChildren()
    {                
      List list = new ArrayList();   
      if (binding.getPortType() != null)
      {
        list.add(binding.getPortType());
      }
      return list;
    }  
  }*/

  // convenience methods
  //
  public static void addModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {                                                               
// TODO: port check
    ModelAdapter adapter = getWSDLGraphModelAdapterFactory().getAdapter(modelObject);
//    ModelAdapter adapter = EcoreUtil.getAdapter(getWSDLGraphModelAdapterFactory().eAdapters(),modelObject);
    if (adapter != null)
    {
      adapter.addListener(listener);
    }
  }    

  public static void removeModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {                                                            
    ModelAdapter adapter = getWSDLGraphModelAdapterFactory().getAdapter(modelObject);
    if (adapter != null)
    {
      adapter.removeListener(listener);
    }
  } 

  public static ViewMode getViewMode(Definition definition)
  {
    GraphDefinitionAdapter adapter = (GraphDefinitionAdapter)getWSDLGraphModelAdapterFactory().getAdapter(definition);
    return adapter.getViewMode();
  }           
}