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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class SkeletonConfigWidget extends SimpleWidgetDataContributor
{
  private JavaWSDLParameter javaWSDLParam;

  /* CONTEXT_ID PBSC0001 for the Skeleton Config Page */
  private static final String INFOPOP_PBSC_PAGE = "PBSC0001"; //$NON-NLS-1$
  private static final String TOOLTIP_PBSC_PAGE = "TOOLTIP_PBSC_PAGE";
  
  // private Text uriText_;
  /* CONTEXT_ID PBCF0002 for the URI field of the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_URI = "PBCF0002"; //$NON-NLS-1$
  private final String TOOLTIP_PBCF_TEXT_URI = "TOOLTIP_PBCF_TEXT_URI";
  
  private Text wsdlFolderText_;
  /* CONTEXT_ID PBCF0006 for the WSDL Folder field in the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_WSDL_FOLDER = "PBCF0006"; //$NON-NLS-1$
  private final String TOOLTIP_PBCF_TEXT_WSDL_FOLDER = "TOOLTIP_PBCF_TEXT_WSDL_FOLDER";
  
  private Text wsdlFileText_;
  /* CONTEXT_ID PBCF0007 for the WSDL File field of the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_WSDL_FILE = "PBCF0007"; //$NON-NLS-1$
  private final String TOOLTIP_PBCF_TEXT_WSDL_FILE = "TOOLTIP_PBCF_TEXT_WSDL_FILE";
  
  private Combo skeletonFolderText_;
  /* CONTEXT_ID PBSC0004 for the Skeleton Folder field of the Skeleton Config Page */
  private static final String INFOPOP_PBSC_TEXT_SKELETON_FOLDER = "PBSC0004"; //$NON-NLS-1$
  private static final String TOOLTIP_PBSC_TEXT_SKELETON_FOLDER = "TOOLTIP_PBSC_TEXT_SKELETON_FOLDER";
  //

  /* CONTEXT_ID PBSC0005 for the Skeleton Folder Browse button of the Skeleton Config Page */
  // private static final String INFOPOP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE = "PBSC0005"; //$NON-NLS-1$
  // private static final String TOOLTIP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE = "TOOLTIP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE";
  
  private Button showMappingsCheckbox_;
  /* CONTEXT_ID PBSC0016 for the Show Mappings checkbox of the Bean Methods Page  */
  private String INFOPOP_N2P_SHOW_MAPPINGS = "PBSC0016"; //$NON-NLS-1$
	
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId        = "org.eclipse.jst.ws.axis.creation.ui";
    String       conPluginId     = "org.eclipse.jst.ws.axis.consumption.ui";
    String       baseConPluginId = "org.eclipse.jst.ws.consumption.ui";
    
    MessageUtils msgUtils        = new MessageUtils( pluginId + ".plugin", this );
    MessageUtils conMsgUtils     = new MessageUtils( conPluginId + ".plugin", this );
    MessageUtils baseConMsgUtils = new MessageUtils( baseConPluginId + ".plugin", this );
    UIUtils      uiUtils         = new UIUtils( msgUtils, pluginId );
    UIUtils      conUiUtils      = new UIUtils( conMsgUtils, pluginId );
    UIUtils      baseConUiUtils  = new UIUtils( baseConMsgUtils, pluginId );

  	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId + "." +  INFOPOP_PBSC_PAGE );
  	parent.setToolTipText( msgUtils.getMessage( TOOLTIP_PBSC_PAGE ) );
    
    Composite textGroup = uiUtils.createComposite( parent, 2, 0, 0 );

    /*
    uriText_ = uiUtils.createText( textGroup, "LABEL_URI",
                                   TOOLTIP_PBCF_TEXT_URI,
                                   INFOPOP_PBCF_TEXT_URI,
                                   SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    */

    wsdlFolderText_ = uiUtils.createText( textGroup, "LABEL_OUTPUT_FOLDER_NAME",
                                          TOOLTIP_PBCF_TEXT_WSDL_FOLDER,
                                          INFOPOP_PBCF_TEXT_WSDL_FOLDER,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    wsdlFileText_ = uiUtils.createText( textGroup, "LABEL_OUTPUT_FILE_NAME",
                                        TOOLTIP_PBCF_TEXT_WSDL_FILE,
                                        INFOPOP_PBCF_TEXT_WSDL_FILE,
                                        SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    skeletonFolderText_ = baseConUiUtils.createCombo( textGroup, "LABEL_SKELETON_ROOT_NAME",
        TOOLTIP_PBSC_TEXT_SKELETON_FOLDER,
        INFOPOP_PBSC_TEXT_SKELETON_FOLDER,
        SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    Label separator = uiUtils.createHorizontalSeparator( parent, 5 );

    showMappingsCheckbox_ = conUiUtils.createCheckbox( parent, "LABEL_EXPLORE_MAPPINGS_XML2BEAN",
                                                       "TOOLTIP_N2P_SHOW_MAPPINGS",
                                                       INFOPOP_N2P_SHOW_MAPPINGS );
        
    return this;
  }
  
  public void setEndpointURI(String endpointURI)
  {
  	/*
    if (endpointURI != null)
      uriText_.setText(endpointURI);
    */
  }
  
  public void setOutputWSDLFolder(String outputWSDLFolder)
  {
    if (outputWSDLFolder != null)
      wsdlFolderText_.setText(outputWSDLFolder);
  }
  
  public void setOutputWSDLFile(String outputWSDLFile)
  {
    if (outputWSDLFile != null)
      wsdlFileText_.setText(outputWSDLFile);
  }
  
  public void setOutputJavaFolder(String outputJavaFolder)
  {
    if (outputJavaFolder != null)
    {
      int index = skeletonFolderText_.indexOf(outputJavaFolder);
      if (index != -1)
        skeletonFolderText_.select(index);
      else if (skeletonFolderText_.getItemCount() <= 0)
      {
        String root = getWorkspaceRootLocation();
        if (outputJavaFolder.startsWith(root))
          skeletonFolderText_.setText(outputJavaFolder.substring(root.length()));
        else
          skeletonFolderText_.setText(outputJavaFolder);
      }
    }
  }
  
  public void setShowMapping(boolean showMapping)
  {
    showMappingsCheckbox_.setSelection(showMapping);
  }
  
  public boolean getShowMapping()
  {
    return showMappingsCheckbox_.getSelection();
  }

  /**
   * @return Returns the javaWSDLParam.
   */
  public JavaWSDLParameter getJavaWSDLParam() {
    String root = getWorkspaceRootLocation();
    File file = new File(root);
    try
    {
      root = file.toURL().toString();
      char lastChar = root.charAt(root.length()-1);
      if (lastChar == '/' || lastChar == '\\')
        root = root.substring(0, root.length()-1);
    }
    catch (MalformedURLException murle)
    {
    }
    javaWSDLParam.setOutput(root + wsdlFolderText_.getText());
    javaWSDLParam.setJavaOutput(root + skeletonFolderText_.getText());
	  return javaWSDLParam;
  }

  /**
   * @param javaWSDLParam The javaWSDLParam to set.
   */
  public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
	  this.javaWSDLParam = javaWSDLParam;
  }
  
  public void setServerProject(IProject serviceProject)
  {
    String originalSkeletonFolder = skeletonFolderText_.getText();
    skeletonFolderText_.removeAll();
    IPath[] paths = ResourceUtils.getAllJavaSourceLocations(serviceProject);
    for (int i = 0; i < paths.length ; i++)
      skeletonFolderText_.add(paths[i].toString());
    int index = skeletonFolderText_.indexOf(originalSkeletonFolder);
    if (index != -1)
      skeletonFolderText_.select(index);
    else
      skeletonFolderText_.select(0);
  }
  
  private String getWorkspaceRootLocation()
  {
  	return ResourcesPlugin.getWorkspace().getRoot().getLocation().removeTrailingSeparator().toString();
  }
}