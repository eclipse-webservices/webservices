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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.wst.common.ui.properties.internal.provisional.ISection;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetPage;
import org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.graph.model.WSDLGraphModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapter;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.w3c.dom.Element;

public class AbstractSection implements ISection, IPropertyChangeListener, Listener, SelectionListener
{
	private TabbedPropertySheetWidgetFactory factory;
	private WSDLElement element;
  protected boolean isReadOnly = false;
  protected Composite composite;
  protected int rightMarginSpace;
  protected int tableMinimumWidth = 50;
	
  public void createControls(Composite parent,	TabbedPropertySheetPage tabbedPropertySheetPage)
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
		
		if (input instanceof WSDLElement)
		{
		  this.element = (WSDLElement)input;
      ModelAdapter adapter = WSDLGraphModelAdapterFactory.getWSDLGraphModelAdapterFactory().getAdapter(input);
      if (adapter != null)
      {
        isReadOnly = Boolean.TRUE.equals(adapter.getProperty(input, "isReadOnly")); //$NON-NLS-1$
      }
		}
		else if (input instanceof WSDLGroupObject)
		{
		  this.element = ((WSDLGroupObject)input).getDefinition();
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

  public WSDLElement getElement()
  {
    return element;
  }
  
	/**
	 * Get the widget factory.
	 * @return the widget factory.
	 */
	public TabbedPropertySheetWidgetFactory getWidgetFactory() {
		return factory;
	}

	public void propertyChange(PropertyChangeEvent event)
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
    if (isListenerEnabled() &&
        getElement() != null &&
        !isInDoHandle) 
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
    if (isListenerEnabled() &&
        getElement() != null &&
        !isInDoHandle) 
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

  
  static protected IEditorPart getActiveEditor()
  {
    IWorkbench workbench = WSDLEditorPlugin.getInstance().getWorkbench();
    IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
    IEditorPart editorPart = workbenchWindow.getActivePage().getActiveEditor();

    return editorPart;
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

  public DocumentImpl getDocument(Element element)
  {
    return (DocumentImpl) element.getOwnerDocument();
  }

  public void beginRecording(String description, Element element)
  {
    getDocument(element).getModel().beginRecording(this, description);
  }
  
  public void endRecording(Element element)
  {
    DocumentImpl doc = (DocumentImpl) getDocument(element);
    
    doc.getModel().endRecording(this);    
  }
}
