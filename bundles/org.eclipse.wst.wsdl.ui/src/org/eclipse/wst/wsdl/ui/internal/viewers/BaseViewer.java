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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.util.ui.BaseDesignWindow;
import org.eclipse.wst.wsdl.ui.internal.util.ui.BorderPainter;
import org.eclipse.wst.wsdl.ui.internal.util.ui.FlatViewUtility;
import org.eclipse.wst.xml.core.document.XMLNode;
import org.eclipse.wst.xml.core.format.FormatProcessorXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class BaseViewer extends BaseDesignWindow implements Listener, SelectionListener
{
//  protected Object input; 

  // for use in subclasses
  protected FlatViewUtility flatViewUtility = new FlatViewUtility(true);

  public BaseViewer(IStatusLineManager statusLine)
  {
    super(statusLine);
  }

  public Object getInput()
  {
    return input;
  }

  // gets called only if we currently are not handling an event
  abstract public void doSetInput(Object input);
  
  /**
   * @see org.eclipse.jface.viewers.Viewer#setInput(Object)
   */
  static Object oldInput = null;
  ModelAdapterListener oldModelAdapterListener = null;

  public void setInput(Object input)
  {
    super.setInput(input);

    doSetInput(input);
/*
    if (!isInDoHandle() && !getControl().isDisposed())
    {
      final Object obj = input;
      getControl().getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          doSetInput(obj);
        }
      });
    }
*/
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
    if (isListenerEnabled() &&
        getInput() != null &&
        !isInDoHandle) 
    {
      isInDoHandle = true;
      startDelayedEvent(event);
      isInDoHandle = false;
    } // end of if ()
  }

  abstract public void doHandleEvent(Event event);
  abstract public void createControl(Composite parent);

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

  /*
   * @see Viewer#getControl()
   */
  public Control getControl()
  {
    return mainUIComponent;
  }


  /*
   * @see BaseDesignWindow#createDesignPane(Composite, int)
   */
  protected Control createDesignPane(Composite arg0, int arg1)
  {
    Control control = super.createDesignPane(arg0, arg1);
    
    Composite client = super.getControlsContainer();
    
    client.addPaintListener(new BorderPainter());
    
    client.setBackground(new Color(Display.getCurrent(),255,255,255));

    return control;
  }


  public ISelection getSelection()
  {
    return null;
  }
  
  public void refresh()
  {
  }

  public void setSelection(ISelection selection, boolean reveal) 
  {
  }
   
  
//  public abstract Control getControl();


  public Node getNode()
  {
    return (Node) getInput();
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
        getInput() != null &&
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
        getInput() != null &&
        !isInDoHandle) 
    {
      isInDoHandle = true;
      doWidgetSelected(e);
      isInDoHandle = false;
    }
    
  }

  public Node getChildNode(Element parent, String childName)
  {
    NodeList list = null;
    if (parent != null)
    {
      list = parent.getChildNodes();
    }
   
    String name = null;
    if (list != null)
    {
      // Performance issue perhaps?
      for (int i = 0; i < list.getLength(); i++)
      {
        if (list.item(i) instanceof Element)
        {
          if (list.item(i).getLocalName().equals(childName))
          {
            return list.item(i);
          }
        }
      }
    }
    return null;
  }


  protected Element performAddElement(Node parentNode, String namespaceName, String localName)
  {
    if (parentNode != null)
    {                              
      Element newElement = createElement(parentNode, namespaceName, localName);
      parentNode.appendChild(newElement);       
      format(parentNode);
      return newElement;
    }  
    return null;
  }

  protected Element createElement(Node parentNode, String namespaceName, String localName)
  {
    Document document = parentNode.getOwnerDocument();
    Element element = document.createElement(localName);
    return element;   
  }   

  protected void addAttributes(Element newElement, String attr, String value)
  {                                              
    newElement.setAttribute(attr, value);
  }

  protected void format(Node parentNode)
  {
    if (parentNode instanceof XMLNode) 
    {
      // format selected node                                                    
      FormatProcessorXML formatProcessorXML = new FormatProcessorXML();
      formatProcessorXML.formatNode((XMLNode)parentNode);
    }
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
  
}