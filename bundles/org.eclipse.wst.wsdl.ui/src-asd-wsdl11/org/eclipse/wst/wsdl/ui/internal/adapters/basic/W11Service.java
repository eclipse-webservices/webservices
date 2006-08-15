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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddEndPointCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddEndPointAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IService;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class W11Service extends WSDLBaseAdapter implements IService {

	public List getEndPoints() {
		List adapterList = new ArrayList();
		List endPoints = new ArrayList();
		Iterator it = ((Service) target).getEPorts().iterator();
		while (it.hasNext()) {
			endPoints.add(it.next());
		}
		
		Collections.sort(endPoints, new EndPointComparator());
		populateAdapterList(endPoints, adapterList);

		return adapterList;
	}

	public String getName() {
		return ((Service) target).getQName().getLocalPart();
	}
	
	public List getFields() {
		return getEndPoints();
	}
	
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}
	
	public String[] getActions(Object object) {
    List actionIDs = new ArrayList();
		actionIDs.add(ASDAddEndPointAction.ID);
		actionIDs.add(ASDDeleteAction.ID);

    if (isReadOnly()) {
      actionIDs.add(OpenInNewEditor.ID);
    }
    return (String [])actionIDs.toArray(new String[0]);
	}
    
    public Command getAddEndPointCommand() {
      return new W11AddEndPointCommand((Service)target);
    }
    
    public Command getDeleteCommand() {
    	return new W11DeleteCommand(this);
    }
    
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/service_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "service";
	}
	
	public ITreeElement[] getChildren() {
		List endPoints = getEndPoints();
		ITreeElement[] treeElements = new ITreeElement[endPoints.size()];
		
		for (int index = 0; index < endPoints.size(); index++) {
			treeElements[index] = (ITreeElement) endPoints.get(index);
		}
		
		return treeElements;
	}

	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
    
	private class EndPointComparator implements Comparator {
	  public int compare(Object o1, Object o2) {
	    if (o1 instanceof Port && o2 instanceof Port) {
	      Binding binding1 = ((Port) o1).getEBinding();
	      Binding binding2 = ((Port) o2).getEBinding();

	      if (binding1 != null && binding2 == null) {
	        return -1;
	      }
	      else if (binding1 == null && binding2 != null) {
	        return 1;
	      }

	      PortType portType1 = binding1.getEPortType();
	      PortType portType2 = binding2.getEPortType();

	      if (portType1 != null && portType2 != null) {
	        String name1 = portType1.getQName().getLocalPart();
	        String name2 = portType2.getQName().getLocalPart();
	        return name1.compareTo(name2);
	      }
	      else if (portType1 != null && portType2 == null) {
	        return -1;
	      }
	      else if (portType1 == null && portType2 != null) {
	        return 1;
	      }
	    }

	    return 0;
	  }
	}    
}
