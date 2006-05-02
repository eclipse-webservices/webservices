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
package org.eclipse.wst.wsdl.ui.internal.asd.properties.sections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.ui.internal.asd.design.DesignViewGraphicsConstants;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;

public class ASDAbstractSection implements ISection, IASDObjectListener, Listener, SelectionListener
{
	private TabbedPropertySheetWidgetFactory factory;
	private Object elementModel;
	protected boolean isReadOnly = false;
	protected Composite composite;
	protected int rightMarginSpace;
	protected int tableMinimumWidth = 50;
	
	protected List listeners = new ArrayList();
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage)
	{
		createControls(parent, tabbedPropertySheetPage.getWidgetFactory());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory aFactory)
	{
		this.factory = aFactory;
		GC gc = new GC(parent);
		Point extent = gc.textExtent("  ...  "); //$NON-NLS-1$
		rightMarginSpace = extent.x;
		gc.dispose();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection)
	{
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection)selection).getFirstElement();
		elementModel = input;
		attachListener(elementModel);
		
		if (input instanceof IASDObject) {
			isReadOnly = ((IASDObject) input).isReadOnly();
		}
		
		refresh();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#aboutToBeShown()
	 */
	public void aboutToBeShown()
	{
		refresh();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#aboutToBeHidden()
	 */
	public void aboutToBeHidden()
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#dispose()
	 */
	public void dispose()
	{
		unattachAllListeners();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#getMinimumHeight()
	 */
	public int getMinimumHeight()
	{
		return SWT.DEFAULT;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#shouldUseExtraSpace()
	 */
	public boolean shouldUseExtraSpace()
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ISection#refresh()
	 */
	public void refresh()
	{
		if (isReadOnly)
		{
			composite.setEnabled(false);
		}
		else
		{
			composite.setEnabled(true);
		}
	}
	
	/**
	 * Get the widget factory.
	 * @return the widget factory.
	 */
	public TabbedPropertySheetWidgetFactory getWidgetFactory() {
		return factory;
	}
	
	public void propertyChanged(Object object, String property)
	{
		refresh();
	}
	
	
	public void doWidgetDefaultSelected(SelectionEvent e)
	{}
	
	public void doWidgetSelected(SelectionEvent e)
	{}
	
	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e)
	{
		if (isListenerEnabled() && !isInDoHandle) 
		{
			isInDoHandle = true;
			doWidgetDefaultSelected(e);
			isInDoHandle = false;
		}
		
	}
	
	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e)
	{
		if (isListenerEnabled() && !isInDoHandle) 
		{
			isInDoHandle = true;
			doWidgetSelected(e);
			isInDoHandle = false;
		}
		
	}
	
	boolean listenerEnabled = true;
	/**
	 * Get the value of listenerEnabled.
	 * @return value of listenerEnabled.
	 */
	public boolean isListenerEnabled() 
	{
		return listenerEnabled;
	}
	
	/**
	 * Set the value of listenerEnabled.
	 * @param v  Value to assign to listenerEnabled.
	 */
	public void setListenerEnabled(boolean  v) 
	{
		this.listenerEnabled = v;
	}
	
	public void handleEvent(Event event)
	{
		if (isListenerEnabled() && !isInDoHandle) 
		{
			isInDoHandle = true;
			startDelayedEvent(event);
			isInDoHandle = false;
		} // end of if ()
	}
	
	public void doHandleEvent(Event event)
	{
		
	}
	
	protected DelayedEvent delayedTask;
	
	protected void startDelayedEvent(Event e)
	{
		if (delayedTask == null ||
				delayedTask.getEvent() == null)
		{
			delayedTask = new DelayedEvent();
			delayedTask.setEvent(e);
			Display.getDefault().timerExec(500,delayedTask);
		}
		else
		{
			Event delayedEvent = delayedTask.getEvent();
			
			if (e.widget == delayedEvent.widget &&
					e.type == delayedEvent.type)
			{
				// same event, just different data, delay new event
				delayedTask.setEvent(null);
			}
			delayedTask = new DelayedEvent();
			delayedTask.setEvent(e);
			Display.getDefault().timerExec(500,delayedTask);
		}
	}
	
	class DelayedEvent implements Runnable
	{
		protected Event event;
		
		/*
		 * @see Runnable#run()
		 */
		public void run()
		{
			if (event != null)
			{
				isInDoHandle = true;
				doHandleEvent(event);
				isInDoHandle = false;
				event = null;
			}
		}
		
		/**
		 * Gets the event.
		 * @return Returns a Event
		 */
		public Event getEvent()
		{
			return event;
		}
		
		/**
		 * Sets the event.
		 * @param event The event to set
		 */
		public void setEvent(Event event)
		{
			this.event = event;
		}
		
	}
	
	boolean isInDoHandle;
	/**
	 * Get the value of isInDoHandle.
	 * @return value of isInDoHandle.
	 */
	public boolean isInDoHandle() 
	{
		return isInDoHandle;
	}
	
	static protected IStatusLineManager getStatusLineManager(IEditorPart editorPart)
	{ 
		IStatusLineManager result = null;
		try
		{                       
			EditorActionBarContributor contributor = (EditorActionBarContributor)editorPart.getEditorSite().getActionBarContributor();
			result = contributor.getActionBars().getStatusLineManager();
		}
		catch (Exception e)
		{
		}  
		return result;
	}
	
	public Object getModel() {
		return elementModel;
	}
	
	protected void attachListener(Object object) {
		if (object instanceof IASDObject && !listeners.contains(object)) {
			((IASDObject) object).registerListener(this);
			listeners.add(object);
		}
	}
	
	protected void unattachAllListeners() {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			((IASDObject) item).unregisterListener(this);
		}
	}
	
	protected void setControlForegroundColor(Control control) {
		if (control != null) {
			if (isReadOnly) {
				control.setForeground(DesignViewGraphicsConstants.readOnlyLabelColor);
			}
			else {
				control.setForeground(DesignViewGraphicsConstants.labelColor);
			}
		}
	}
    
    protected void executeCommand(Command command) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        
        if ((CommandStack) page.getActiveEditor().getAdapter(CommandStack.class) != null) {
            CommandStack stack = (CommandStack) page.getActiveEditor().getAdapter(CommandStack.class);
            stack.execute(command);
        }
        else {
            command.execute();
        }
    }
}
