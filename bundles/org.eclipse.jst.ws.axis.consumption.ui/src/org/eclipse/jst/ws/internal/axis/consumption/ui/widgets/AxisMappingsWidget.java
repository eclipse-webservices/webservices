/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.TableViewerWidget;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class AxisMappingsWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.consumption.ui";
  
  private TableViewerWidget mappings_;

  private byte mode_;
  private JavaWSDLParameter javaParameter_;

  public static final byte MODE_BEAN2XML = (byte)0;
  public static final byte MODE_XML2BEAN = (byte)1;
  public static final byte MODE_XML2PROXY = (byte)2;
  public static final byte MODE_XML2EJB = (byte)3;
  public static final byte MODE_EJB2XML = (byte)4;
  
  private final String DEFAULT_PACKAGE = "default.javapackage";
  private final String DEFAULT_NAMESPACE = "http://default.namespace";
  
  /*CONTEXT_ID PWJM0001 for the WSDL to Java Mappings Page*/
  private String INFOPOP_PWJM_PAGE = "PWJM0001"; //$NON-NLS-1$
  
  public AxisMappingsWidget( byte mode )
  {
    mode_ = mode;
  }
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, pluginId_ );
        
    // TODO The TOOLTIP_PWJM_PAGE key doesn't seem to exist anywhere???
	//parent.setToolTipText( msgUtils.getMessage( "TOOLTIP_PWJM_PAGE" ) );
    PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PWJM_PAGE );
    
    // TODO No tooltip or infopop for this group.
    Group mappingGroup = uiUtils.createGroup( parent, "LABEL_MAPPING_PAIRS", null, null );
                                               
    List initValues = new ArrayList();
    
    if( mode_ == MODE_BEAN2XML || mode_ == MODE_EJB2XML) 
    {
		String[] columns = { msgUtils.getMessage("TABLE_COLUMN_LABEL_PACKAGE"),
		                     msgUtils.getMessage("TABLE_COLUMN_LABEL_NAMESPACE" )};
		mappings_ = new TableViewerWidget( columns, initValues, new String[] {DEFAULT_PACKAGE, DEFAULT_NAMESPACE}, TableViewerWidget.MAP_ONE_TO_ONE); //$NON-NLS-1$
    }
	else 
	{
      	String[] columns = { msgUtils.getMessage("TABLE_COLUMN_LABEL_NAMESPACE"),
		                     msgUtils.getMessage("TABLE_COLUMN_LABEL_PACKAGE")};
		mappings_ = new TableViewerWidget( columns, initValues, new String[] {DEFAULT_NAMESPACE, DEFAULT_PACKAGE }, TableViewerWidget.MAP_MANY_TO_ONE); //$NON-NLS-1$
	}
    
	mappings_.addControls( mappingGroup, statusListener );
   
    return this;
  }
  
  public Status getStatus()
  {
    return mappings_.getStatus();  
  }
  
  public void setJavaParameter( JavaWSDLParameter parameter )
  {
    javaParameter_ = parameter;
  }
  
  public JavaWSDLParameter getJavaParameter()
  {
    if( mode_ == MODE_BEAN2XML || mode_ == MODE_EJB2XML || mode_ == MODE_XML2BEAN || mode_ == MODE_XML2PROXY)
    {
      //Set the mappings on javaParameter
      TableItem[] pairs = mappings_.getItems();
  	  HashMap map = new HashMap();
  	  for (int i=0; i<pairs.length; i++)
  	  {
  		map.put(pairs[i].getText(0),pairs[i].getText(1));
  	  }
  	  javaParameter_.setMappings(map);
  	  
  	  //Set the namespace on the javaParameter
  	  String beanName = javaParameter_.getBeanName();
	  if(beanName != null && !beanName.equals(""))
	  {
		String packageName = beanName.substring(0, beanName.lastIndexOf('.'));
		if(map.containsKey(packageName))
		{
		  String tns = (String)map.get(packageName);
		  javaParameter_.setNamespace(tns);
		}		
	  }

    }
    return javaParameter_;
  }
}
