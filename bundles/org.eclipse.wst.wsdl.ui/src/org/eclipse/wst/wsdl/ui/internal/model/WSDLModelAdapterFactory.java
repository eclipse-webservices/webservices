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
package org.eclipse.wst.wsdl.ui.internal.model;
        
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.OperationType;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.OperationImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.extension.ITreeChildProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WSDLModelAdapterFactory implements ModelAdapterFactory
{                                      
  protected static WSDLModelAdapterFactory wsdlModelAdapterFactoryInstance;

  public AdapterFactory adapterFactory;
                       
  public WSDLModelAdapterFactory()
  {
    adapterFactory = createAdapterFactory();
  }   
                                                 
  protected boolean isExtensibilityElementConsideredChild()
  {
    return true;
  }

  protected void addExtensiblityElementChildren(List list, ExtensibleElement extensibleElement)
  {
    if (isExtensibilityElementConsideredChild())
    {
      //list.addAll(WSDLEditorUtil.getInstance().getExtensibilityElementNodes(extensibleElement));
      list.addAll(WSDLEditorUtil.getInstance().getExtensibilityElementNodes(extensibleElement));
    }
  }

  public static WSDLModelAdapterFactory getWSDLModelAdapterFactory()
  {                
    if (wsdlModelAdapterFactoryInstance == null)
    {
      wsdlModelAdapterFactoryInstance = new WSDLModelAdapterFactory();
    }
    return wsdlModelAdapterFactoryInstance;
  }  
    

  protected AdapterFactory createAdapterFactory()
  {
    return new WSDLAdapterFactoryImpl();
  }            


  public ModelAdapter getAdapter(Object o)
  {                             
    ModelAdapter adapter = null;
    if (o instanceof WSDLElement)                     
    {
      adapter = (ModelAdapter)adapterFactory.adapt((WSDLElement)o, adapterFactory);
    }
    else if (o instanceof WSDLGroupObject)
    {           
      adapter = (WSDLGroupObject)o;
    }
    return adapter;
  }                                    

  //
  //
  //
  public class WSDLAdapterFactoryImpl extends AdapterFactoryImpl
  {                     
    public Adapter createAdapter(Notifier target)
    {                     
      WSDLSwitch wsdlSwitch = new WSDLSwitch()
      {                   
      	public Object caseWSDLElement(WSDLElement wsdlElement)
        {   
          return createWSDLElementAdapter();
	      }   

      	public Object caseBinding(Binding binding)
        {   
          return createBindingAdapter();
	      } 

      	public Object caseBindingFault(BindingFault bindingFault)
        {   
          return createBindingFaultAdapter();
	      } 

      	public Object caseBindingInput(BindingInput bindingInput)
        {   
          return createBindingInputAdapter();
	      } 

      	public Object caseBindingOutput(BindingOutput bindingOutput)
        {   
          return createBindingOutputAdapter();
	      } 
                           
      	public Object caseBindingOperation(BindingOperation bindingOperation)
        {   
          return createBindingOperationAdapter();
	      } 
 
      	public Object caseDefinition(Definition definition)
        {   
          return createDefinitionAdapter();
	      }           

      	public Object caseFault(Fault fault)
        {   
          return createFaultAdapter();
	      } 

      	public Object caseImport(Import i)
        {   
          return createImportAdapter();
	      } 

      	public Object caseInput(Input input)
        {   
          return createInputAdapter();
	      } 	

      	public Object caseOutput(Output output)
        {   
          return createOutputAdapter();
	      } 

      	public Object caseMessage(Message message)
        {   
          return createMessageAdapter();
	      }       

      	public Object caseOperation(Operation operation)
        {   
          return createOperationAdapter();
	      } 

      	public Object casePart(Part part)
        {   
          return createPartAdapter();
	      }  

      	public Object casePort(Port port)
        {   
          return createPortAdapter();
	      }  

      	public Object casePortType(PortType portType)
        {   
          return createPortTypeAdapter();
	      }  

      	public Object caseService(Service service)
        {   
          return createServiceAdapter();
	      }  

      	public Object caseTypes(Types types)
        {   
          return createTypesAdapter();
	      } 
        
        public Object caseUnknownExtensibilityElement(UnknownExtensibilityElement uee)
        {   
          return createUnknownExtensibilityElementAdapter();
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
      return result;
    }      
                               
  
    public Adapter createWSDLElementAdapter()
    {
      return new WSDLElementAdapter();
    }

  	public Adapter createBindingAdapter()
    {
      return new BindingAdapter();
    }   

 	  public Adapter createBindingFaultAdapter()
    {                                      
      return new BindingFaultAdapter();
    }           

 	  public Adapter createBindingInputAdapter()
    {                                      
      return new BindingInputAdapter();
    }

    public Adapter createBindingOutputAdapter()
    {
      return new BindingOutputAdapter();
	  } 
                
    public Adapter createBindingOperationAdapter()
    {     
      return new BindingOperationAdapter();
    }
          
  	public Adapter createDefinitionAdapter() 
    {
		  return new DefinitionAdapter();
	  }

  	public Adapter createFaultAdapter() 
    {
		  return new FaultAdapter();
	  }

  	public Adapter createImportAdapter() 
    {
		  return new ImportAdapter();
	  }

  	public Adapter createInputAdapter() 
    {
		  return new InputAdapter();
	  }

  	public Adapter createMessageAdapter() 
    {
		  return new MessageAdapter();
	  }


  	public Adapter createOperationAdapter() 
    {
		  return new OperationAdapter();
	  }

  	public Adapter createOutputAdapter() 
    {
		  return new OutputAdapter();
	  }

  	public Adapter createPartAdapter() 
    {
		  return new PartAdapter();
	  }

  	public Adapter createPortAdapter() 
    {
		  return new PortAdapter();
	  }

  	public Adapter createPortTypeAdapter() 
    {
		  return new PortTypeAdapter();
	  }

  	public Adapter createServiceAdapter() 
    {                    
		  return new ServiceAdapter();
	  } 

  	public Adapter createTypesAdapter() 
    {                    
		  return new TypesAdapter();
	  } 
    
    public Adapter createUnknownExtensibilityElementAdapter()
    {
      return new UnknownExtensibilityElementAdapter();
    }
    
    // convenience method
    //
    public Adapter adapt(Notifier target)
    {
      return adapt(target, this);
    }
  }
    
  //
  //
  //
  protected class WSDLElementAdapter extends AdapterImpl implements ModelAdapter
  {   
    protected List listenerList = new ArrayList();


    public WSDLElementAdapter()
    {
    }  
                                 
    public boolean isAdapterForType(Object type)
    {
      return type == adapterFactory;
    }                                             

    public void addListener(ModelAdapterListener listener)
    {
      if (!listenerList.contains(listener))
      {
        listenerList.add(listener);
      }  
    }  

    public void removeListener(ModelAdapterListener listener)
    {
      if (listenerList.contains(listener))
      {
        listenerList.remove(listener);
      }
    } 

    public void firePropertyChanged(Object notifier, String property)
    {         
      List list = new ArrayList();
      list.addAll(listenerList);               
      for (Iterator i = list.iterator(); i.hasNext(); )
      {
        ModelAdapterListener listener = (ModelAdapterListener)i.next();
        listener.propertyChanged(getTarget(), property);
      }
    }   

    public void notifyChanged(Notification msg)
    {                        
      if (msg.getEventType() != Notification.RESOLVE)
      {        
        firePropertyChanged(msg.getNotifier(), null);
      }
    }  

       
    public Object getProperty(Object modelObject, String propertyName)
    {           
      Object result = null;
      if (propertyName.equals(CHILDREN_PROPERTY))
      {
        result = getChildren();      
      }                    
      else if (propertyName.equals(LABEL_PROPERTY))
      {
        result = getLabel();      
      }                    
      else if (propertyName.equals(IMAGE_PROPERTY))
      {
        result = getImage();
      } 
      else if (propertyName.equals("extensibilityElements"))
      { 
        if (modelObject instanceof ExtensibleElement)
        {
          result = WSDLEditorUtil.getInstance().getExtensibilityElementNodes((ExtensibleElement)modelObject);
        }
      } 
      else if (propertyName.equals("isReadOnly"))
      {
      	Element element = WSDLEditorUtil.getInstance().getElementForObject(target); 
     	  result = (element instanceof IDOMNode) ? Boolean.FALSE : Boolean.TRUE;    
      }
      return result;
    } 

    protected List getChildren()
    {
      return Collections.EMPTY_LIST;
    }                        

    protected String getLabel()
    {  
      return "todo";
    }

    protected Image getImage()
    {
      return null;
    }                       
  }                     
  

  protected class BindingAdapter extends WSDLElementAdapter
  {                
    protected Binding binding;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.binding = (Binding)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();                 
      addExtensiblityElementChildren(list, binding);
      List operationsList = binding.getEBindingOperations();
      List tempList = new ArrayList();
      tempList.addAll(operationsList);
      
      Collections.sort(tempList, new Comparator() {
    	public int compare(Object o1, Object o2) {
    		String s1 = ((BindingOperation) o1).getName();
    		String s2 = ((BindingOperation) o2).getName();
    		
    		if (s1 == null) {
    			s1 = "";
    		}
    		if (s2 == null) {
    			s2 = "";
    		}
    		
    		boolean boo = true;
      		return s1.compareToIgnoreCase(s2);
      	}
      });
      
      list.addAll(tempList);
      
      return list;
    }     

    protected String getLabel()
    {  
      return binding.getQName().getLocalPart();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/binding_obj.gif");
    }                             
  }  
    

  protected class BindingFaultAdapter extends WSDLElementAdapter
  {                
    protected BindingFault bindingFault;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.bindingFault = (BindingFault)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();    
      addExtensiblityElementChildren(list, bindingFault);
      return list;
    }     

    protected String getLabel()
    {  
      return bindingFault.getName();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/fault_obj.gif");
    }               
  } 


  protected class BindingInputAdapter extends WSDLElementAdapter
  {                
    protected BindingInput bindingInput;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.bindingInput = (BindingInput)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();    
      addExtensiblityElementChildren(list, bindingInput);
      return list;
    }     

    protected String getLabel()
    {  
      return "input";
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/input_obj.gif");
    }                             
  }  

 
  protected class BindingOutputAdapter extends WSDLElementAdapter
  {                
    protected BindingOutput bindingOutput;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.bindingOutput = (BindingOutput)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();    
      addExtensiblityElementChildren(list, bindingOutput);
      return list;
    }     

    protected String getLabel()
    {  
      return "output";
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/output_obj.gif");
    }    
  }  
                              

  protected class BindingOperationAdapter extends WSDLElementAdapter
  {                
    protected BindingOperation bindingOperation;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.bindingOperation = (BindingOperation)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();         
                  
      addExtensiblityElementChildren(list, bindingOperation); 

      if (bindingOperation.getBindingInput() != null)
      {
        list.add(bindingOperation.getBindingInput());
      }
      if (bindingOperation.getBindingOutput() != null)
      {
        list.add(bindingOperation.getBindingOutput());
      }                             
      list.addAll(bindingOperation.getEBindingFaults());
      
      return list;
    }        
                

    protected String getLabel()
    {  
      return bindingOperation.getName();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/operationbinding_obj.gif");
    }               
  }  

  //
  //
  //
  protected class DefinitionAdapter extends WSDLElementAdapter implements ModelAdapterListener
  {                
    protected List permanentWSDLGroupObjectList;
    protected Definition definition;

    protected WSDLGroupObject typesGroup;
    protected WSDLGroupObject extensibilityElementsGroup;
    protected Types types; 
     
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.definition = (Definition)newTarget;
     
      // this group is added and removed from the child list dynamically
      // dependant on the presence of an 'actual' types element
      //
      typesGroup = new WSDLGroupObject(definition, WSDLGroupObject.TYPES_GROUP, getModelAdapterFactory());
                                                          
      // these groups always exist in the child list      
      //
      permanentWSDLGroupObjectList = new ArrayList();       
      permanentWSDLGroupObjectList.add(new WSDLGroupObject(definition, WSDLGroupObject.IMPORTS_GROUP));
      permanentWSDLGroupObjectList.add(typesGroup);  
      permanentWSDLGroupObjectList.add(new WSDLGroupObject(definition, WSDLGroupObject.MESSAGES_GROUP));
      permanentWSDLGroupObjectList.add(new WSDLGroupObject(definition, WSDLGroupObject.PORT_TYPES_GROUP));
      permanentWSDLGroupObjectList.add(new WSDLGroupObject(definition, WSDLGroupObject.BINDINGS_GROUP));
      permanentWSDLGroupObjectList.add(new WSDLGroupObject(definition, WSDLGroupObject.SERVICES_GROUP));
      // we only add this group when it has content
      //
      extensibilityElementsGroup = new WSDLGroupObject(definition, WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP);

      updateTypes();
    }
                                                      
    protected List getChildren()
    {       
      List list = new ArrayList();
      list.addAll(permanentWSDLGroupObjectList);   
      //if (extensibilityElementsGroup.getChildren().size() > 0)
      //{
      list.add(extensibilityElementsGroup);
      //}
      return list;
    }

    public void notifyChanged(Notification msg)
    {     
      if (msg.getEventType() != Notification.RESOLVE)
      {                           
        updateTypes();
        firePropertyChangedHelper();   
      }
    }        

    protected void updateTypes()
    {
      if (types != definition.getETypes())
      {
        if (types != null)
        {
          removeModelAdapterListener(types, this);
        }                                         
        types = definition.getETypes();
        if (types != null)
        {
          addModelAdapterListener(types, this);
        }
      }  
    }

    protected void firePropertyChangedHelper()
    {                        
      firePropertyChanged(getTarget(), null);

      for (Iterator i = getChildren().iterator(); i.hasNext(); )
      {                                                                              
        ModelAdapter adapter = getAdapter(i.next());
        adapter.firePropertyChanged(adapter, null);
      }                                             
    } 

    // propagate changes to 'types' object
    //
    public void propertyChanged(Object object, String property)
    {
      typesGroup.firePropertyChanged(typesGroup, property);
    } 

    protected ModelAdapterFactory getModelAdapterFactory()
    {
      return getWSDLModelAdapterFactory();
    }
    
    protected String getLabel()
    {  
      return definition.getQName().getLocalPart();
    }

    protected Image getImage()
    {
      return null;
    }                       
  }

 

  protected class FaultAdapter extends WSDLElementAdapter
  {                
    protected Fault fault;
                                    
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.fault = (Fault)newTarget;
    }
                                                      
    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/fault_obj.gif");
    }   

    protected String getLabel()
    {  
      return fault.getName();
    }               
  }   
      

  //
  //
  //
  protected class ImportAdapter extends WSDLElementAdapter
  {                
    protected Import theImport;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.theImport = (Import)newTarget;
    }
                                                       
    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/import_obj.gif");
    }   

    protected String getLabel()
    {  
      String result = theImport.getLocationURI();
      if (result == null || result.length() == 0)
      {
      	result = WSDLEditorPlugin.getWSDLString("_UI_NO_IMPORT_SPECIFIED");
      }
      return result;
    }               
  } 


  //
  //
  //
  protected class InputAdapter extends WSDLElementAdapter
  {                
    protected Input input;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.input = (Input)newTarget;
    }
                                                       
    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/input_obj.gif");
    }   

    protected String getLabel()
    {  
      return "input";
    }               
  }    

   
 
  //
  //
  //
  protected class MessageAdapter extends WSDLElementAdapter
  {                
    protected Message message;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.message = (Message)newTarget;
    }
                                                      
    protected List getChildren()
    {
      List list = new ArrayList();
      list.addAll(message.getEParts());
      return list;
    }                        

    protected String getLabel()
    {  
      if (message.getQName() == null) return ""; // revisit...it shouldn't be null 
      return message.getQName().getLocalPart();
    
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/message_obj.gif");
    }
  }


  //
  //
  //
  protected class OutputAdapter extends WSDLElementAdapter
  {                
    protected Output output;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.output = (Output)newTarget;
    }                     
        
    protected String getLabel()
    {  
      return "output";
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/output_obj.gif");
    }                                                        
  }  

  //
  //
  //
  protected class OperationAdapter extends WSDLElementAdapter
  {                
    protected Operation operation;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.operation = (Operation)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();
      Input input = operation.getEInput();
      Output output = operation.getEOutput();
      
      OperationType operationType = ((OperationImpl) operation).getStyle();
      if (operationType != null) {
      	if (operationType.equals(OperationType.REQUEST_RESPONSE) && input != null && output != null) {
      		// Input, Output
      		list.add(input);
      		list.add(output);
      	}
      	else if (operationType.equals(OperationType.SOLICIT_RESPONSE) && input != null && output != null) {
      		// Output, Input
      		list.add(output);
      		list.add(input);
      	}
      	else if (operationType.equals(OperationType.ONE_WAY) && input != null) {
      		// Input
      		list.add(input);
      	}
      	else if (operationType.equals(OperationType.NOTIFICATION) && output != null) {
      		//Output
      		list.add(output);
      	}
      }
      list.addAll(operation.getFaults().values());
      
      return list;
    }
      
	private int getNodeIndex(NodeList nodeList, Node node) {
		int index = 0;
		while (index < nodeList.getLength() && !(nodeList.item(index).equals(node))) {
			index++;
		}
		
		if (index >= nodeList.getLength()) {
			index = -1;
		}
		
		return index;
	}

    protected String getLabel()
    {  
      return operation.getName();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/operation_obj.gif");
    }               
  }  
   

  //
  //
  //
  protected class PartAdapter extends WSDLElementAdapter
  {                
    protected Part part;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.part = (Part)newTarget;
    }                                                       

    protected String getLabel()
    {
      return part.getName();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/part_obj.gif");
    }                             
  }     

  //
  //
  //
  protected class PortAdapter extends WSDLElementAdapter implements ModelAdapterListener
  {                
    protected Port port;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.port = (Port)newTarget;
    }
                                                                             
    protected String getLabel()
    {  
      return port.getName();
    }

    protected Image getImage()
    {                                
      return WSDLEditorPlugin.getInstance().getImage("icons/port_obj.gif");
    }    

    protected List getChildren()
    {
      return WSDLEditorUtil.getInstance().getExtensibilityElementNodes(port);  
    }
    // propagate changes to the binding object
    //
    public void propertyChanged(Object object, String property)
    {
      firePropertyChanged(port, property);
    }
  } 
     
  protected class UnknownExtensibilityElementAdapter extends WSDLElementAdapter implements ModelAdapterListener
  {                
    protected UnknownExtensibilityElement uee;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.uee = (UnknownExtensibilityElement)newTarget;
    }
                                                                             
    protected String getLabel()
    {  
      String result = null;
      Node node = (Node) uee.getElement();

      ILabelProvider labelProvider = getExtensibilityLabelProvider(node);
      if (labelProvider != null)
      {
        result = labelProvider.getText(node);
      }

      if (result == null)
      {
        result = node.getNodeName();
      }
      return result;
    }

    protected Image getImage()
    {                                
      Image image = null;
      Node node = (Node) uee.getElement();

      ILabelProvider labelProvider = getExtensibilityLabelProvider(node);
      if (labelProvider != null)
      {
        image = labelProvider.getImage(node);
      }

      if (image == null)
      {
        image = WSDLEditorPlugin.getInstance().getImage("icons/element_obj.gif");
      }
      return image;
    }    

    protected List getChildren()
    {
      List list = null;
      //Node node = (Node) uee.getElement();
      //ITreeChildProvider childProvider = getExtensibilityContentProvider(node);
      // TBD - Discuss with Craig why we would need childProvider
      //if (childProvider != null)
      //{
      //  Object[] array = childProvider.getChildren(node);
      //  list = Arrays.asList(array);
      //}
      //else
      {
        list = uee.getChildren();
      }
      return list; 
    }

    public void propertyChanged(Object object, String property)
    {
      firePropertyChanged(uee, property);
    }
    
    protected ILabelProvider getExtensibilityLabelProvider(Node node)
    {
      ILabelProvider result = null;
      String namespaceURI = node.getNamespaceURI();
      if (namespaceURI != null)
      {
        result = WSDLEditorPlugin.getInstance().getExtensibilityItemTreeProviderRegistry().getLabelProvider(namespaceURI);
      }
      return result;
    }
    
    protected ITreeChildProvider getExtensibilityContentProvider(Node node)
    {
      ITreeChildProvider result = null;
      String namespaceURI = node.getNamespaceURI();
      if (namespaceURI != null)
      {
        result = WSDLEditorPlugin.getInstance().getExtensibilityItemTreeProviderRegistry().getContentProvider(namespaceURI);
      }
      return result;
    }

    boolean isReadOnly()
    {
      return true;
    }
  } 


  //
  //
  //
  protected class PortTypeAdapter extends WSDLElementAdapter
  {                
    protected PortType portType;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.portType = (PortType)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();
      list.addAll(portType.getOperations());
      
      Collections.sort(list, new Comparator()
      {
        public int compare(Object o1, Object o2)
        {
          String o1Name = ((Operation) o1).getName();
          String o2Name = ((Operation) o2).getName();
          if (o1Name == null) o1Name = "";
          if (o2Name == null) o2Name = "";
          return (o1Name.compareToIgnoreCase(o2Name));
        }
      });
      return list; 
    }                    
      
    protected String getLabel()
    {  
      return portType.getQName().getLocalPart();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/porttype_obj.gif");
    }
  } 
     

  protected class ServiceAdapter extends WSDLElementAdapter
  {                
    protected Service service;
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.service = (Service)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();
      list.addAll(service.getEPorts());
      return list; 
    }                    
      
    protected String getLabel()
    {  
      return service.getQName().getLocalPart();
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/service_obj.gif");
    }   
  } 
     
  //
  //
  //
  public class TypesAdapter extends WSDLElementAdapter
  {
    protected Types types; 
                         
    public void setTarget(Notifier newTarget) 
    {
      super.setTarget(newTarget);
      this.types = (Types)newTarget;
    }
                                                      
    protected List getChildren()
    {                  
      List list = new ArrayList();  
      list.addAll(WSDLEditorUtil.getInstance().getExtensibilityElementNodes(types));
      return list; 
    }                    
      
    protected String getLabel()
    {  
      return "Types";
    }

    protected Image getImage()
    {
      return WSDLEditorPlugin.getInstance().getImage("icons/types_obj.gif");
    }                                                                                      
  }

  public static void addModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {                                                               
// TODO: port check
    ModelAdapter adapter = getWSDLModelAdapterFactory().getAdapter(modelObject);
//    ModelAdapter adapter = EcoreUtil.getAdapter(getWSDLModelAdapterFactory().eAdapters(),modelObject);
    if (adapter != null)
    {
      adapter.addListener(listener);
    }
  }    

  public static void removeModelAdapterListener(Object modelObject, ModelAdapterListener listener)
  {                                                            
// TODO: port check
    ModelAdapter adapter = getWSDLModelAdapterFactory().getAdapter(modelObject);
//    ModelAdapter adapter = EcoreUtil.getAdapter(getWSDLModelAdapterFactory().eAdapters(),modelObject);
    if (adapter != null)
    {
      adapter.removeListener(listener);
    }
  }            
}