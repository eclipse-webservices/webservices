/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060424   120137 kathy@ca.ibm.com - Kathy Chan
 * 20060523   143284 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.wizard.beans;


import org.eclipse.jst.ws.internal.axis.consumption.core.command.Java2WSDLCommand;
import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsWidget;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.BUCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.JavaToWSDLMethodCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.DefaultsForServerJavaWSDLCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean.BeanConfigWidget;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.object.ObjectSelectionWidget;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactory;
import org.eclipse.wst.command.internal.env.core.fragment.SimpleFragment;
import org.eclipse.wst.command.internal.env.ui.widgets.CanFinishRegistry;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetContributorFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetRegistry;


/**
* This is the class for the
* Axis bottom up Java Bean scenario
* 
*/
public class WSBeanAxisType implements CommandWidgetBinding
{	
  private String serverName_;
  private String runtimeName_;
  private boolean isWebModuleRequired_;


  /**
  * Returns a locale specific label for the Server supported by this Web Service type
  * @return A locale specific server label from server plugins
  */
  public String getServerLabel()
  {
    return serverName_;
  }

  /**
  * Sets the server label to the appropriate name found in the server plugins.
  * @param server name
  */
  public void setServerLabel(String serverLabel)
  {
    serverName_ = serverLabel;
  }
  
  /**
  * Returns a locale specific label for the deployment runtime of this Web Service type
  * @return A locale specific label for the deployment runtime
  */
  public String getRuntimeLabel()
  {
    return runtimeName_;
  }

  /**
  * Sets the runtime label for the appropriate runtime
  * @param server name
  */
  public void setRuntimeLabel(String runtimeLabel)
  {
    runtimeName_ = runtimeLabel;
  }


  /**
  * Returns whether or not a Web Module is required for this WebServiceServerRuntimeType
  * @return true if requireWebModule
  */
  public boolean isWebModuleRequired()
  {
    return isWebModuleRequired_;
  }

  /**
  * Sets the boolean value at runtime according to requireWebModule attribute from the manifest file
  * @param requireWebModule 
  */
  public void setWebModuleRequired(boolean requireWebModule)
  {
    isWebModuleRequired_ = requireWebModule;
  }

  
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeType#isEJBModuleRequired()
	 */
	public boolean isEJBModuleRequired() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.consumption.ui.wizard.WebServiceServerRuntimeType#setEJBModuleRequired(boolean)
	 */
	public void setEJBModuleRequired(boolean arg0) {
		return;

	}

  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerCanFinish(org.eclipse.wst.command.env.ui.widgets.CanFinishRegistry)
   */
  public void registerCanFinish(CanFinishRegistry canFinishRegistry) 
  {
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerDataMappings(org.eclipse.wst.command.internal.env.core.data.DataMappingRegistry)
   */
  public void registerDataMappings(DataMappingRegistry dataRegistry) 
  {
    /*
    dataRegistry.addMapping( CurrentPageCommand.class, "CurrentPage", BeanClassWidget.class );
    //BeanClassWidget - as target
    dataRegistry.addMapping(BUAxisDefaultingCommand.class, "JavaBeanName", BeanClassWidget.class, "BeanClassName", null);
    //BeanClassWidget - as source
    dataRegistry.addMapping(BeanClassWidget.class, "BeanClassName", DefaultsForServerJavaWSDLCommand.class, "JavaBeanName", null);    //BeanConfigWidget
    */
    // ObjectSelectionWidget
    dataRegistry.addMapping(ObjectSelectionWidget.class, "ObjectSelection", DefaultsForServerJavaWSDLCommand.class);
    
    //BeanConfigWidget - as target
    dataRegistry.addMapping(JavaToWSDLMethodCommand.class, "JavaWSDLParam", BeanConfigWidget.class, "JavaParameter", null );
    //BeanConfigWidget - as source

    dataRegistry.addMapping(BeanConfigWidget.class, "JavaParameter", Java2WSDLCommand.class, "JavaWSDLParam", null);       
    dataRegistry.addMapping(BeanConfigWidget.class, "JavaParameter", WSDL2JavaCommand.class, "JavaWSDLParam", null);

    //AxisMappingsWidget - as target
    dataRegistry.addMapping(BeanConfigWidget.class, "JavaParameter", AxisMappingsWidget.class);
    
    //AxisMappingsWidget - as source

    dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", BUCodeGenOperation.class, "JavaWSDLParam", null);       
  
    
    //dataRegistry.addMapping( DefaultsForConfig.class, "JavaParameter", BeanConfigWidget.class );
    //dataRegistry.addMapping( DefaultsForConfig.class, "CustomizeServiceMappings", BeanConfigWidget.class );
  }
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) 
  {
   
    /*
    widgetRegistry.add( "BeanSelection", 
                        AxisCreationUIMessages.PAGE_TITLE_WSBEAN_CLASS,
                        AxisCreationUIMessages.PAGE_DESC_WSBEAN_CLASS,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new BeanClassWidget();
                          }
                        } );
    */

    widgetRegistry.add( "BeanConfig", 
                        AxisCreationUIMessages.PAGE_TITLE_WSBEAN_CONFIG,
                        AxisCreationUIMessages.PAGE_DESC_WSBEAN_CONFIG,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new BeanConfigWidget();
                          }
                        } );


    widgetRegistry.add( "AxisServiceBeanMapping", 
    		AxisConsumptionUIMessages.PAGE_TITLE_WS_BEAN2XML,
    		AxisConsumptionUIMessages.PAGE_DESC_P2N_MAPPINGS,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new AxisMappingsWidget(AxisMappingsWidget.MODE_BEAN2XML );
                          }
                        } );
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.internal.env.core.fragment.CommandFragmentFactoryFactory#create()
   */
  public CommandFragmentFactory create() 
  {
    return new CommandFragmentFactory()
    {
      public CommandFragment create()
      {
          //dead code - doesn't matter what gets returned here.
    	  return new SimpleFragment();
      }
    };
  }
    
}



