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
package org.eclipse.wst.wsdl.ui.internal.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.wst.wsdl.ui.internal.actions.IWSDLToolbarAction;

public class WSDLEditorConfiguration {
	  public static final String WSDLEDITORCONFIGURATIONEXTENSIONID = "org.eclipse.wst.wsdl.ui.WSDLEditorExtensionConfiguration"; //$NON-NLS-1$
	  public static final String CLASSNAME = "class"; //$NON-NLS-1$
	  public static final String ADAPTERFACTORY = "adapterFactory"; //$NON-NLS-1$
	  public static final String TOOLBARACTION = "toolbarAction"; //$NON-NLS-1$
	  public static final String FIGUREFACTORY = "figureFactory"; //$NON-NLS-1$
	  public static final String EDITPARTFACTORY = "editPartFactory"; //$NON-NLS-1$

	  List definedExtensionsList = null;

	  public WSDLEditorConfiguration()
	  {

	  }

	  public AdapterFactory getAdapterFactory()
	  {
	    if (definedExtensionsList == null)
	    {
	      readWSDLConfigurationRegistry();
	    }
	    if (!definedExtensionsList.isEmpty())
	    {
	      return ((WSDLEditorExtensionProperties) definedExtensionsList.get(0)).getAdapterFactory();
	    }
	    return null;
	  }

	  public EditPartFactory getEditPartFactory()
	  {
	    if (definedExtensionsList == null)
	    {
	      readWSDLConfigurationRegistry();
	    }
	    if (!definedExtensionsList.isEmpty())
	    {
	      return ((WSDLEditorExtensionProperties) definedExtensionsList.get(0)).getEditPartFactory();
	    }
	    return null;
	  }

//	  public IExtendedFigureFactory getFigureFactory()
//	  {
//	    if (definedExtensionsList == null)
//	    {
//	      readWSDLConfigurationRegistry();
//	    }
//	    if (!definedExtensionsList.isEmpty())
//	    {
//	      return ((WSDLEditorExtensionProperties) definedExtensionsList.get(0)).getFigureFactory();
//	    }
//	    return null;
//	  }

	  public List getToolbarActions()
	  {
	    if (definedExtensionsList == null)
	    {
	      readWSDLConfigurationRegistry();
	    }
	    if (!definedExtensionsList.isEmpty())
	    {
	      return ((WSDLEditorExtensionProperties) definedExtensionsList.get(0)).getActionList();
	    }
	    return Collections.EMPTY_LIST;
	  }

	  protected Object loadClass(IConfigurationElement element, String classString)
	  {
	    String pluginId = element.getDeclaringExtension().getContributor().getName();

	    try
	    {
	      Class theClass = Platform.getBundle(pluginId).loadClass(classString);
	      Object instance = theClass.newInstance();

	      return instance;
	    }
	    catch (Exception e)
	    {

	    }
	    return null;
	  }

	  public void readWSDLConfigurationRegistry()
	  {
	    IConfigurationElement[] wsdlEditorExtensionList = Platform.getExtensionRegistry().getConfigurationElementsFor(WSDLEDITORCONFIGURATIONEXTENSIONID);

	    boolean definedExtensionsExist = (wsdlEditorExtensionList != null && wsdlEditorExtensionList.length > 0);

	    definedExtensionsList = new ArrayList();

	    if (definedExtensionsExist)
	    {
	      for (int i = 0; i < wsdlEditorExtensionList.length; i++)
	      {
	        WSDLEditorExtensionProperties properties = new WSDLEditorExtensionProperties();
	        definedExtensionsList.add(properties);

	        IConfigurationElement element = wsdlEditorExtensionList[i];
	        String adapterFactoryClass = element.getAttribute(ADAPTERFACTORY);
	        if (adapterFactoryClass != null)
	        {
	          Object object = loadClass(element, adapterFactoryClass);
	          if (object instanceof AdapterFactory)
	          {
	            properties.setAdapterFactory((AdapterFactory) object);
	          }
	        }

//	        String figureFactoryClass = element.getAttribute(FIGUREFACTORY);
//	        if (figureFactoryClass != null)
//	        {
//	          Object object = loadClass(element, figureFactoryClass);
//	          IExtendedFigureFactory figureFactory = null;
//	          if (object instanceof IExtendedFigureFactory)
//	          {
//	            figureFactory = (IExtendedFigureFactory) object;
//	            properties.setFigureFactoryList(figureFactory);
//	          }
//	        }
	        
	        String editPartFactoryClass = element.getAttribute(EDITPARTFACTORY);
	        if (editPartFactoryClass != null)
	        {
	          Object object = loadClass(element, editPartFactoryClass);
	          EditPartFactory editPartFactory = null;
	          if (object instanceof EditPartFactory)
	          {
	            editPartFactory = (EditPartFactory) object;
	            properties.setEditPartFactoryList(editPartFactory);
	          }
	        }

	        IConfigurationElement[] toolbarActions = element.getChildren(TOOLBARACTION);
	        List actionList = new ArrayList();
	        if (toolbarActions != null)
	        {
	          for (int j = 0; j < toolbarActions.length; j++)
	          {
	            IConfigurationElement actionElement = toolbarActions[j];
	            String actionClass = actionElement.getAttribute(CLASSNAME);
	            IWSDLToolbarAction action = null;
	            if (actionClass != null)
	            {
	              Object object = loadClass(actionElement, actionClass);
	              if (object instanceof IWSDLToolbarAction)
	              {
	                action = (IWSDLToolbarAction) object;
	                actionList.add(action);
	              }
	            }
	          }
	        }
	        properties.setActionList(actionList);



	      }
	    }
	  }
	}
