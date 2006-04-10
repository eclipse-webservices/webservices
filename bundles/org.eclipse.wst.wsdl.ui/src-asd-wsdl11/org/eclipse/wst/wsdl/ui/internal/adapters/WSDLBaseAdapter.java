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
package org.eclipse.wst.wsdl.ui.internal.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11RenameCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.IActionProvider;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class WSDLBaseAdapter extends AdapterImpl implements IASDObject, IActionProvider {
	protected List listenerList = new ArrayList();
	protected WSDLBaseAdapter owner;
	
	public void registerListener(IASDObjectListener listener) {
       if (!listenerList.contains(listener))
       {
		 listenerList.add(listener);
       }  
	}

	public void unregisterListener(IASDObjectListener listener) {
		listenerList.remove(listener);
	}

	public void populateAdapterList(List notifierList, List adapterList) {
		for (Iterator i = notifierList.iterator(); i.hasNext();)   {
            Object o = i.next();
            if (o instanceof Notifier)
            {  
			Notifier component = (Notifier)o;      
			Adapter adapter = WSDLAdapterFactoryHelper.getInstance().adapt(component);
			adapterList.add(adapter);
			
			if (adapter instanceof WSDLBaseAdapter) {
				((WSDLBaseAdapter) adapter).setOwner(this);
			}
            }
            else
            {
              System.out.println("populateAdapterListError" + o);
            }  
		}
	}
	
	public Adapter createAdapter(Notifier notifier) {
		Adapter adapter = WSDLAdapterFactoryHelper.getInstance().adapt(notifier);

		if (adapter instanceof WSDLBaseAdapter && ((WSDLBaseAdapter) adapter).owner == null) {
			((WSDLBaseAdapter) adapter).setOwner(this);
		}
		
		return adapter;
	}
	
	public String getName() {
		return null;
	}
	
	public void setOwner(WSDLBaseAdapter owner) {
		this.owner = owner;
	}
	
	public boolean isAdapterForType(Object type) {
		return type == WSDLAdapterFactoryHelper.getInstance().getWSDLAdapterFactory();
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[0];
		
		return actionIDs;
	}
    
	public void notifyChanged(Notification msg)
	{
	  super.notifyChanged(msg);
	  notifyListeners(this, null);
	}
	
	protected void notifyListeners(Object changedObject, String property)
	{
	  List clonedListenerList = new ArrayList();
	  clonedListenerList.addAll(listenerList);
	  for (Iterator i = clonedListenerList.iterator(); i.hasNext(); )
	  {
	    IASDObjectListener listener = (IASDObjectListener)i.next();
	    listener.propertyChanged(this, null);
	  }      
	}
	
	public Command getSetNameCommand(String newName) {
		return new W11RenameCommand(this, newName);
	}
}
