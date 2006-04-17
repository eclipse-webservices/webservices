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
package org.eclipse.wst.wsdl.ui.internal.edit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.common.core.search.SearchMatch;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.common.ui.internal.search.dialogs.IComponentDescriptionProvider;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;

public class WSDLComponentDescriptionProvider extends LabelProvider implements IComponentDescriptionProvider {
	  private static final Image BINDING_IMAGE =  WSDLEditorPlugin.getInstance().getImage("icons/binding_obj.gif"); //$NON-NLS-1$
	  private static final Image PORTTYPE_IMAGE = WSDLEditorPlugin.getInstance().getImage("icons/porttype_obj.gif"); //$NON-NLS-1$
	  private static final Image MESSAGE_IMAGE = WSDLEditorPlugin.getInstance().getImage("icons/message_obj.gif"); //$NON-NLS-1$

	public boolean isApplicable(Object component) {
		return true;
	}

	public String getQualifier(Object component) {
	    String result = null;
	    if (component instanceof ComponentSpecification)
	    {
	      result = ((ComponentSpecification)component).getQualifier();
	    }  
	    else if (component instanceof WSDLElement)
	    {
	      result = ((WSDLElement) component).getEnclosingDefinition().getTargetNamespace(); 
	    }  
	    else if (component instanceof SearchMatch)
	    {
	      QualifiedName qualifiedName = getQualifiedNameForSearchMatch((SearchMatch)component);
	      if (qualifiedName != null)
	      {  
	        result = qualifiedName.getNamespace();
	      }    
	    }  
	    return result;
	}

	public String getName(Object component) {
	    String result = null;
	    if (component instanceof ComponentSpecification)
	    {
	      result = ((ComponentSpecification)component).getName();
	    }  
	    else if (component instanceof WSDLElement)
	    {
	      result = ((WSDLElement)component).getElement().getAttribute("name");  //$NON-NLS-1$
	    }  
	    else if (component instanceof SearchMatch)
	    {
	      QualifiedName qualifiedName = getQualifiedNameForSearchMatch((SearchMatch)component);
	      if (qualifiedName != null)
	      {  
	        result = qualifiedName.getLocalName();
	      }    
	    }      
	    return result;
	}

	public Image getFileIcon(Object component) {
		return WSDLEditorPlugin.getInstance().getImage("icons/wsdl_file_obj.gif"); //$NON-NLS-1$
	}

	public IFile getFile(Object component) {
	    IFile result = null;
	    if (component instanceof ComponentSpecification)
	    {
	      result = ((ComponentSpecification)component).getFile();
	    }  
	    else if (component instanceof SearchMatch)
	    {
	      result = ((SearchMatch)component).getFile();
	    }  
	    else if (component instanceof WSDLElement)
	    {
	      WSDLElement concreteComponent = (WSDLElement) component;
	      Definition definition = concreteComponent.getEnclosingDefinition();
	      if (definition != null)
	      {
	        // TODO (cs) revisit and test more
	        //
	        String location = definition.getLocation();
	        String platformResource = "platform:/resource"; //$NON-NLS-1$
	        if (location != null && location.startsWith(platformResource))
	        {
	          Path path = new Path(location.substring(platformResource.length()));
	          result = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	        }
	      }  
	    }
	    return result;
	}

	public ILabelProvider getLabelProvider() {
		return this;
	}
	
	  public String getText(Object element) {
	    String result = ""; //$NON-NLS-1$
	    String name = getName(element);
	    if (name != null) {
	      result += name;
	    }
	    return result;
	  } 
	  
	  public Image getImage(Object component)
	  {
	    Image result = null;
	    if (component instanceof ComponentSpecification)
	    {
	    	QualifiedName qualifiedName = ((ComponentSpecification) component).getMetaName();
	    	if ( qualifiedName != null ){
	    		if ( qualifiedName.equals(IWSDLSearchConstants.BINDING_META_NAME))
	    			result = BINDING_IMAGE;
	    		else if ( qualifiedName.equals(IWSDLSearchConstants.PORT_TYPE_META_NAME))
	    			result = PORTTYPE_IMAGE;
	    		else if ( qualifiedName.equals(IWSDLSearchConstants.MESSAGE_META_NAME))
	    			result = MESSAGE_IMAGE;

	    	}
	    }
	    else if (component instanceof SearchMatch)
	    {
	      SearchMatch searchMatch = (SearchMatch)component;
	      QualifiedName qualifiedName = (QualifiedName)searchMatch.map.get("metaName"); //$NON-NLS-1$
	      if ( qualifiedName != null ){
	    	  if ( qualifiedName.equals(IWSDLSearchConstants.BINDING_META_NAME))
	    		  result = BINDING_IMAGE;
	    	  else if ( qualifiedName.equals(IWSDLSearchConstants.PORT_TYPE_META_NAME))
	    		  result = PORTTYPE_IMAGE;
	    		else if ( qualifiedName.equals(IWSDLSearchConstants.MESSAGE_META_NAME))
	    			result = MESSAGE_IMAGE;

	      }
	    }      
	    else if (component instanceof Binding)
	      result = BINDING_IMAGE;      
	    else if (component instanceof PortType)
	      result = PORTTYPE_IMAGE;
	    else if (component instanceof Message)
		      result = MESSAGE_IMAGE;

	    return result;
	  }
	
	private QualifiedName getQualifiedNameForSearchMatch(SearchMatch match) {
		QualifiedName qualifiedName = null;
		Object o = match.map.get("name"); //$NON-NLS-1$
		if (o != null && o instanceof QualifiedName)
		{  
			qualifiedName = (QualifiedName)o;
		}      
		return qualifiedName;
	}
}
