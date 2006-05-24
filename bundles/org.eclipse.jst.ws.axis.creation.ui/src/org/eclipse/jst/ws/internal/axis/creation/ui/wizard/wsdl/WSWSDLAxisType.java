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
 * 20060321   128827 joan - Joan Haggarty, remove redundant wsdl URI, folder and file controls
 * 20060523   143284 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.wizard.wsdl;


import org.eclipse.jst.ws.internal.axis.consumption.core.command.WSDL2JavaCommand;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsFragment;
import org.eclipse.jst.ws.internal.axis.consumption.ui.widgets.AxisMappingsWidget;
import org.eclipse.jst.ws.internal.axis.creation.ui.command.TDCodeGenOperation;
import org.eclipse.jst.ws.internal.axis.creation.ui.task.BackupSkelImplCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.AxisSkeletonDefaultingCommand;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidget;
import org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton.SkeletonConfigWidgetDefaultingCommand;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.command.data.ProjectName2IProjectTransformer;
import org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions.ServerExtensionDefaultingCommand;
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
* This is the class for the Axis Web service type.
*/
public class WSWSDLAxisType implements CommandWidgetBinding 
{
	private String serverName_;
	private String runtimeName_;
	private boolean isWebModuleRequired_;

	/**
	* Returns a locale specific label for the Server supported by this Web Service type
	* @return A locale specific server label from server plugins
	*/
	public String getServerLabel() {
		return serverName_;
	}

	/**
	* Sets the server label to the appropriate name found in the server plugins.
	* @param server name
	*/
	public void setServerLabel(String serverLabel) {
		serverName_ = serverLabel;
	}

	/**
	* Returns a locale specific label for the deployment runtime of this Web Service type
	* @return A locale specific label for the deployment runtime
	*/
	public String getRuntimeLabel() {
		return runtimeName_;
	}

	/**
	* Sets the runtime label for the appropriate runtime
	* @param server name
	*/
	public void setRuntimeLabel(String runtimeLabel) {
		runtimeName_ = runtimeLabel;
	}

	/**
	* Returns whether or not a Web Module is required for this WebServiceServerRuntimeType
	* @return true if requireWebModule
	*/
	public boolean isWebModuleRequired() {
		return isWebModuleRequired_;
	}

	/**
	* Sets the boolean value at runtime according to requireWebModule attribute from the manifest file
	* @param requireWebModule 
	*/
	public void setWebModuleRequired(boolean requireWebModule) {
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
    // SkeletonConfigWidget
    dataRegistry.addMapping(ServerExtensionDefaultingCommand.class, "ServerProject", SkeletonConfigWidget.class, "ServerProject", new ProjectName2IProjectTransformer());
    dataRegistry.addMapping(AxisSkeletonDefaultingCommand.class, "JavaWSDLParam", SkeletonConfigWidget.class);
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "EndpointURI", SkeletonConfigWidget.class);
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "OutputJavaFolder", SkeletonConfigWidget.class);
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "ShowMapping", SkeletonConfigWidget.class);

    //  BackupSkelImplCommand     
    dataRegistry.addMapping(SkeletonConfigWidget.class, "JavaWSDLParam", BackupSkelImplCommand.class);
    
    // AxisMappingsFragment
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "ShowMapping", AxisMappingsFragment.class);
    dataRegistry.addMapping(SkeletonConfigWidget.class, "ShowMapping", AxisMappingsFragment.class);

    // AxisMappingsWidget
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "JavaWSDLParam", AxisMappingsWidget.class, "JavaParameter", null);
    dataRegistry.addMapping(SkeletonConfigWidget.class, "JavaWSDLParam", AxisMappingsWidget.class, "JavaParameter", null);

    // WSDL2JavaCommand
    dataRegistry.addMapping(SkeletonConfigWidgetDefaultingCommand.class, "JavaWSDLParam", WSDL2JavaCommand.class);
    dataRegistry.addMapping(SkeletonConfigWidget.class, "JavaWSDLParam", WSDL2JavaCommand.class);
    dataRegistry.addMapping(AxisMappingsWidget.class, "JavaParameter", TDCodeGenOperation.class, "JavaWSDLParam", null);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.CommandWidgetBinding#registerWidgetMappings(org.eclipse.wst.command.env.ui.widgets.WidgetRegistry)
   */
  public void registerWidgetMappings(WidgetRegistry widgetRegistry) 
  {
   
    /*
    widgetRegistry.add( "WSDLSelection", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_SELECTION,
                        ConsumptionUIMessages.PAGE_DESC_WS_SELECTION,
		                new WidgetContributorFactory()
                        {
		                  public WidgetContributor create()
		                  {
		                    return new WSDLSelectionWidget();
		                  }
		                } );

    widgetRegistry.add( "WSDLSelectionTreeWidget", 
        ConsumptionUIMessages.PAGE_TITLE_WSDL_SELECTION,
        ConsumptionUIMessages.PAGE_DESC_WSDL_SELECTION,
        new WidgetContributorFactory()
        {
          public WidgetContributor create()
        {
            return new WSDLSelectionTreeWidget();
        }
        });
    */

    widgetRegistry.add( "SkeletonConfig", 
                        ConsumptionUIMessages.PAGE_TITLE_WSSKEL_CONFIG,
                        ConsumptionUIMessages.PAGE_DESC_WSSKEL_CONFIG,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new SkeletonConfigWidget();
                          }
                        } );

    widgetRegistry.add( "AxisMappingsWidget", 
                        ConsumptionUIMessages.PAGE_TITLE_WS_XML2BEAN,
                        ConsumptionUIMessages.PAGE_DESC_N2P_MAPPINGS,
                        new WidgetContributorFactory()
                        {
                          public WidgetContributor create()
                          {
                            return new AxisMappingsWidget(AxisMappingsWidget.MODE_XML2BEAN);
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
