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
package org.eclipse.wst.wsdl.ui.internal.graph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;

public class ViewMode 
{
  public static final int BINDING     =   1;
  public static final int MESSAGE     =   2;
  public static final int PORT_TYPE   =   3;
  public static final int SERVICE     =   4;
  public static final int TYPES       =   5;

  public static final String BINDING_DESCRIPTION     = WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING");
  public static final String MESSAGE_DESCRIPTION     = WSDLEditorPlugin.getWSDLString("_UI_LABEL_MESSAGE");
  public static final String PORT_TYPE_DESCRIPTION   = WSDLEditorPlugin.getWSDLString("_UI_LABEL_PORTTYPE");
  public static final String SERVICE_DESCRIPTION     = WSDLEditorPlugin.getWSDLString("_UI_LABEL_SERVICE");
  public static final String TYPES_DESCRIPTION       = WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPES");


  protected boolean isBindingVisible = true;
  protected int mode = SERVICE;
  protected List listenerList = new ArrayList();

  public ViewMode()
  {
  }                              

  public void setBindingVisible(boolean isVisible)
  {
    if (isBindingVisible != isVisible)
    {
      isBindingVisible = isVisible;
      fireChangeNotification();
    }
  }

  public boolean isBindingVisible()
  {                                                   
    return isBindingVisible;
  }

  public static int getModeForDescription(String description)
  {               
    int result = SERVICE;
    if (description.equals(BINDING_DESCRIPTION))
    {               
      result = BINDING; 
    }
    else if (description.equals(MESSAGE_DESCRIPTION))
    {        
      result = MESSAGE;
    }
    else if (description.equals(PORT_TYPE_DESCRIPTION))
    { 
      result = PORT_TYPE;
    }
    else if (description.equals(SERVICE_DESCRIPTION))
    {    
      result = SERVICE;
    }
    else if (description.equals(TYPES_DESCRIPTION))
    {  
      result = TYPES;
    }    
    return result;
  }

  public static String getDescriptionForMode(int mode)
  {               
    String result = SERVICE_DESCRIPTION;
    switch (mode)
    {
      case BINDING :
      {             
        result = BINDING_DESCRIPTION;
        break;
      }
      case MESSAGE : 
      {             
        result = MESSAGE_DESCRIPTION;
        break;
      }
      case PORT_TYPE : 
      {             
        result = PORT_TYPE_DESCRIPTION;
        break;
      }
      case SERVICE : 
      {             
        result = SERVICE_DESCRIPTION;
        break;
      }
      case TYPES :  
      {             
        result = TYPES_DESCRIPTION;
        break;
      }
    }
    return result;
  }

  public interface Listener
  {
    public void viewModeChanged(ViewMode mode);
  }

  public void setMode(int mode)
  {
    this.mode = mode;
    fireChangeNotification();
  }

  public void setMode(String description)
  {
    this.mode = getModeForDescription(description);
    fireChangeNotification();
  }

  public int getMode()
  {
    return mode;
  } 

  public String getModeDescription()
  {
    return getDescriptionForMode(mode);
  }

  public void addListener(Listener listener)
  {
    if (!listenerList.contains(listener))
    { 
      listenerList.add(listener);
    }  
  }

  public void removeListener(Listener listener)
  {
    listenerList.remove(listener);
  }  

  public void fireChangeNotification()
  {
    for (Iterator i = listenerList.iterator(); i.hasNext(); )
    {
      Listener listener = (Listener)i.next();
      listener.viewModeChanged(this);
    }
  }
}