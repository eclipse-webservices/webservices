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
import java.util.Vector;


import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;


  
// Note that the TYPES_GROUP group is just a place holder.  If the Types obejct actually exists 
// for the root WSDL document, it will be used instead of this model group
//
public class WSDLGroupObject implements ModelAdapter
{
  public static final int IMPORTS_GROUP = 1;
  public static final int MESSAGES_GROUP = 2;
  public static final int SERVICES_GROUP = 3;
  public static final int PORT_TYPES_GROUP = 4;
  public static final int BINDINGS_GROUP = 5;
  public static final int TYPES_GROUP = 6;  
  public static final int EXTENSIBILITY_ELEMENTS_GROUP = 7;
         
  protected Definition definition;
  protected int type;       
  protected List listenerList = new ArrayList();
  protected ModelAdapterFactory modelAdapterFactory;

  public WSDLGroupObject(Definition definition, int type)
  {
    this(definition, type, null);
  }  

  public WSDLGroupObject(Definition definition, int type, ModelAdapterFactory modelAdapterFactory)
  {
    this.definition = definition;
    this.type = type;        
    this.modelAdapterFactory = modelAdapterFactory;
  }
     
  public Definition getParent()
  {
    return definition;
  }               

  public Definition getDefinition()
  {
    return definition;
  }               


  public int getType()
  {
    return type;
  }

  public String getLabel()
  {            
    String name = "";
    switch (type)
    {
      case IMPORTS_GROUP :
      {           
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_IMPORTS");  //$NON-NLS-1$
        break;
      }
      case MESSAGES_GROUP :
      {    
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_MESSAGES"); //$NON-NLS-1$
        break;
      }
      case SERVICES_GROUP :
      {
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_SERVICES"); //$NON-NLS-1$
        break;
      }
      case BINDINGS_GROUP :
      {                   
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_BINDINGS"); //$NON-NLS-1$
        break;
      }    
      case PORT_TYPES_GROUP :
      {                   
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_PORTTYPES"); //$NON-NLS-1$
        break;
      }  
      case TYPES_GROUP :
      {                   
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_TYPES"); //$NON-NLS-1$
        break;
      } 
      case EXTENSIBILITY_ELEMENTS_GROUP:
      {
        name = WSDLEditorPlugin.getWSDLString("_UI_FOLDER_EXTENSIBILITY_ELEMENTS"); //$NON-NLS-1$
        break;
      }
    }  
    return name;
  }    
         

  public Image getImage()
  {                
    Image image = null;
    switch (type)
    {
      case IMPORTS_GROUP :
      {    
        image = WSDLEditorPlugin.getInstance().getImage("icons/importheader_obj.gif"); //$NON-NLS-1$
        break;
      }
      case MESSAGES_GROUP :
      {    
        image = WSDLEditorPlugin.getInstance().getImage("icons/messageheader_obj.gif"); //$NON-NLS-1$
        break;
      }
      case SERVICES_GROUP :
      {
        image = WSDLEditorPlugin.getInstance().getImage("icons/serviceheader_obj.gif"); //$NON-NLS-1$
        break;
      }
      case PORT_TYPES_GROUP :
      {                   
        image = WSDLEditorPlugin.getInstance().getImage("icons/porttypeheader_obj.gif"); //$NON-NLS-1$
        break;
      }
      case BINDINGS_GROUP :
      {                   
        image = WSDLEditorPlugin.getInstance().getImage("icons/bindingheader_obj.gif"); //$NON-NLS-1$
        break;
      }    
      case TYPES_GROUP :
      {                   
        image = WSDLEditorPlugin.getInstance().getImage("icons/types_obj.gif"); //$NON-NLS-1$
        break;
      } 
      case EXTENSIBILITY_ELEMENTS_GROUP:
      {
        image = WSDLEditorPlugin.getInstance().getImage("icons/fldr_el.gif"); //$NON-NLS-1$
        break;
      }
    }  
    return image;
  }   


  // implement ModelAdapter                                                             
  public void addListener(ModelAdapterListener listener)
  {
    if (!listenerList.contains(listener))
    {
      listenerList.add(listener);
    }
  }   

  public void removeListener(ModelAdapterListener listener)
  {
    listenerList.remove(listener);
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
    return result;
  }


  public  void firePropertyChanged(Object notifier, String property)
  {                       
    List list = new ArrayList();
    list.addAll(listenerList);
    for (Iterator i = list .iterator(); i.hasNext(); )
    {
      ModelAdapterListener listener = (ModelAdapterListener)i.next();
      listener.propertyChanged(notifier, property);
    }
  } 
   

  public List getChildren()
  {
    List list = Collections.EMPTY_LIST;
              
    ComponentReferenceUtil util = new ComponentReferenceUtil(definition);
    switch (getType())
    {
      case WSDLGroupObject.IMPORTS_GROUP : 
      {   
        list = definition.getEImports();
        break;
      }
      case WSDLGroupObject.MESSAGES_GROUP : 
      {
        try
        {
          list = sortMessages(util.getMessages());
        }
        catch(Exception e)
        {
          list = util.getMessages();
        }  
        break;
      }
      case WSDLGroupObject.SERVICES_GROUP : 
      {
        list = util.getServices();
        break;
      }
      case WSDLGroupObject.PORT_TYPES_GROUP : 
      {       
        list = util.getPortTypes();
        break;
      }
      case WSDLGroupObject.BINDINGS_GROUP : 
      {
        list = util.getBindings();
        break;
      }
      case WSDLGroupObject.TYPES_GROUP : 
      {                     
        Object types = definition.getETypes();
        if (types != null)                                                   
        {
          list = (List)modelAdapterFactory.getAdapter(types).getProperty(types, ModelAdapter.CHILDREN_PROPERTY);
        }
        break;
      }     
      case WSDLGroupObject.EXTENSIBILITY_ELEMENTS_GROUP:
      {
        list = WSDLEditorUtil.getInstance().getExtensibilityElementNodes(definition);
        break;
      }
    }   
    return list;
  }
  
  private List initialSort(List inputList) {
  	List messageGroups = new ArrayList();
  	Iterator iterator = inputList.iterator();
  	
  	while (iterator.hasNext()) {
  		Message newMessage = (Message) iterator.next();
  		
  		boolean messageAdded = false;
  		for (int index = 0; index < messageGroups.size(); index++) {
  			MessageGroup messageItem = (MessageGroup) messageGroups.get(index);
  			
  			if(messageItem.addMessage(newMessage)) {
  				messageAdded = true;
  				break;
  			}
  		}
  		
  		// Do we need to create a new MessageGroup object?
  		if (!messageAdded) {
  			MessageGroup msGroup = new MessageGroup(newMessage.getQName().getLocalPart());
			msGroup.addMessage(newMessage);
  			messageGroups.add(msGroup);
  		}
  	}

  	return messageGroups;
  }
  
  private List buildMessageList(List list) {
  	List sortedMessageList = new ArrayList();
  	
  	Iterator iterator = list.iterator();
  	while (iterator.hasNext()) {
  		MessageGroup mGroup = (MessageGroup) iterator.next();
  		sortedMessageList.addAll(mGroup.getMessages());
  	}
  	
  	return sortedMessageList;
  }

  private List sortMessages(List inputList) {
  	List sortedList = new ArrayList();
  	sortedList.addAll(inputList);
  	
  	// Get a sorted list of MessageGroups
  	sortedList = initialSort(inputList);

  	Collections.sort(sortedList, new Comparator() {
  		public int compare(Object o1, Object o2) {
  			String name1 = ((MessageGroup) o1).getBaseName();
  			String name2 = ((MessageGroup) o2).getBaseName();
  			
  			return name1.compareToIgnoreCase(name2);
      	}  	
  	});
  	
  	sortedList = buildMessageList(sortedList);
  	return sortedList;
  }
  
  private class MessageGroup {
  	private String baseName;
  	private Vector messages;
  	
  	public MessageGroup(String name) {
  		this.baseName = computeBaseName(name);
  		messages = new Vector();
  	}
  	
  	public boolean addMessage(Message newMessage) {
  		// do check if it belongs here?......
  		String newMessageBaseName = computeBaseName(newMessage.getQName().getLocalPart());
  		
  		if (newMessageBaseName.equalsIgnoreCase(getBaseName())) {
  			addToSortedList(newMessage);
  			
  			return true;
  		}
  		
  		return false;
  	}
  	
  	public Vector getMessages() {
  		return messages;
  	}
  	
  	public String getBaseName() {
  		return baseName;
  	}
  	
  	private int getFirstInstance(String subString) {
  		for (int index = 0; index < messages.size(); index++) {
  			Message message = (Message) messages.get(index);
  			String messageName = message.getQName().getLocalPart();
  			
  			if (messageName.indexOf(subString, 1) > 0) {
  				return index;
  			}
  		}
  		  		
  		return -1;
  	}
  	
  	private void addToSortedList(Message message) {
  		// We need to add the new Message in the right position
  		int index = -1;
  		String name = message.getQName().getLocalPart();
  		if (name.indexOf("Request", 1) > 0) {
  			// Add after first instance of 'Response'
  			index = getFirstInstance("Response");
  		}
  		else if (name.indexOf("Response", 1) > 0) {
  			// Add after first instance of 'Request'
  			index = getFirstInstance("Request");
  			if (index >= 0)
  				index++;
  		}
  		
  		if (index >= 0) {
  			messages.add(index, message);
  		}
  		else {
  			messages.add(message);
  		}
  	}
  	
  	private String computeBaseName(String name) {
  		int resReqIndex = name.indexOf("Request", 1);
  		int resReqLength;
  		if (resReqIndex == -1) {
  			resReqIndex = name.indexOf("Response", 1);
  			resReqLength = "Response".length();
  		}
  		else {
  			resReqLength = "Request".length();
  		}
  				
  		// Did we even find a Request/Response in both strings?
  		if (resReqIndex != -1) {
  			return name.substring(0, resReqIndex) + name.substring(resReqIndex + resReqLength, name.length());
  		}
  		else {  		
  			return name;
  		}
  	}
  }  
}