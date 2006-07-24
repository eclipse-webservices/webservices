/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddImportAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddMessageAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddSchemaAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddServiceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ICategoryAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class W11CategoryAdapter extends WSDLBaseAdapter implements ICategoryAdapter, ITreeElement, IASDObjectListener {
	  public final static int IMPORTS = 1;
	  public final static int TYPES = 2;
	  public final static int SERVICES = 3;
	  public final static int BINDINGS = 5;
	  public final static int INTERFACES = 6;
	  public final static int MESSAGES = 7;
	  
	  public static String IMPORTS_HEADER_TEXT = Messages.getString("_UI_FOLDER_IMPORTS"); //$NON-NLS-1$
	  public static String TYPES_HEADER_TEXT = Messages.getString("_UI_FOLDER_TYPES"); //$NON-NLS-1$
	  public static String SERVICE_HEADER_TEXT = Messages.getString("_UI_FOLDER_SERVICES"); //$NON-NLS-1$
	  public static String BINDING_HEADER_TEXT = Messages.getString("_UI_FOLDER_BINDINGS"); //$NON-NLS-1$
	  public static String INTERFACE_HEADER_TEXT = Messages.getString("_UI_PORT_TYPE"); //$NON-NLS-1$
	  public static String MESSAGE_HEADER_TEXT = Messages.getString("_UI_FOLDER_MESSAGES"); //$NON-NLS-1$

	  protected String text;
	  protected Image image;
	  protected int groupType;
	  protected Collection children;
	  protected IDescription description;
	  
	  public W11CategoryAdapter(IDescription description, String label, Image image, Collection children, int groupType) {
	    this.text = label;
	    this.image = image;
	    this.description = description;
	    this.target = ((W11Description) description).getTarget();
	    this.children = children;
	    this.groupType = groupType;
	  }
	  
	  public Definition getDefinition() {
	    return (Definition) target;
	  }
	  
	  public IDescription getOwnerDescription() {
		  return description;
	  }

	  public int getGroupType() {
	    return groupType;
	  }

	  public Image getImage() {
	    return image;
	  }

	  public String getText() {
	    return text;
	  }

	  public ITreeElement[] getChildren() {
	    return (ITreeElement[]) children.toArray(new ITreeElement[0]);
	  }

	  public void setChildren(Collection list) {
	    children = list;
	  }

	  public Object getParent(Object element) {
	    return null;
	  }

	  public boolean hasChildren(Object element) {
	    return true;
	  }
	  
	  public String[] getActions(Object object) {    
	    Collection actionIDs = new ArrayList();
	    
	    switch (groupType) {
	      case IMPORTS : {
	        actionIDs.add(ASDAddImportAction.ID);
	        break;
	      }
	      case TYPES : {
	        actionIDs.add(ASDAddSchemaAction.ID);
	        break;
	      }
	      case SERVICES : {
	        actionIDs.add(ASDAddServiceAction.ID);
	        break;
	      }
	      case BINDINGS : {
	        actionIDs.add(ASDAddBindingAction.ID);
	        break;
	      }
	      case INTERFACES : {
	        actionIDs.add(ASDAddInterfaceAction.ID);
	        break;
	      }
	      case MESSAGES : {
	    	actionIDs.add(ASDAddMessageAction.ID);
	        break;
	      }
	    }
	    return (String [])actionIDs.toArray(new String[0]);
	  }
	  
	  public void propertyChanged(Object object, String property) {
	    if (getText().equals(property))
	      notifyListeners(this, property);
	  }
	  
	  public ITreeElement getParent() {
		  return null;
	  }
	  
	  public boolean hasChildren() {
		  if (getChildren().length > 0) {
			  return true;
		  }
		  
		  return false;
	  }
}