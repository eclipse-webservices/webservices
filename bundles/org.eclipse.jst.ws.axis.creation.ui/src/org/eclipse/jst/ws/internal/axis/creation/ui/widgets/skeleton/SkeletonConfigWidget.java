/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060106   121199 jesper@selskabet.org - Jesper Møller
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.skeleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class SkeletonConfigWidget extends SimpleWidgetDataContributor
{
  private JavaWSDLParameter javaWSDLParam;

  /* CONTEXT_ID PBSC0001 for the Skeleton Config Page */
  private static final String INFOPOP_PBSC_PAGE = "PBSC0001"; //$NON-NLS-1$
  
  // private Text uriText_;
  /* CONTEXT_ID PBCF0002 for the URI field of the Bean Config Page */
  //private final String INFOPOP_PBCF_TEXT_URI = "PBCF0002"; //$NON-NLS-1$

  
  private Text wsdlFolderText_;
  /* CONTEXT_ID PBCF0006 for the WSDL Folder field in the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_WSDL_FOLDER = "PBCF0006"; //$NON-NLS-1$
  
  private Text wsdlFileText_;
  /* CONTEXT_ID PBCF0007 for the WSDL File field of the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_WSDL_FILE = "PBCF0007"; //$NON-NLS-1$

  
  private Combo skeletonFolderText_;
  /* CONTEXT_ID PBSC0004 for the Skeleton Folder field of the Skeleton Config Page */
  private static final String INFOPOP_PBSC_TEXT_SKELETON_FOLDER = "PBSC0004"; //$NON-NLS-1$


  /* CONTEXT_ID PBSC0005 for the Skeleton Folder Browse button of the Skeleton Config Page */
  // private static final String INFOPOP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE = "PBSC0005"; //$NON-NLS-1$
  // private static final String TOOLTIP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE = "TOOLTIP_PBSC_BUTTON_SKELETON_FOLDER_BROWSE";
  
  private Button showMappingsCheckbox_;
  /* CONTEXT_ID PBSC0016 for the Show Mappings checkbox of the Bean Methods Page  */
  private String INFOPOP_N2P_SHOW_MAPPINGS = "PBSC0016"; //$NON-NLS-1$
  
  private IProject serverProject_;
	
  public WidgetDataEvents addControls( Composite parent, Listener statusListener)
  {
    String       pluginId        = "org.eclipse.jst.ws.axis.creation.ui";
    
    UIUtils      uiUtils         = new UIUtils( pluginId );
    UIUtils      conUiUtils      = new UIUtils( pluginId );
    UIUtils      baseConUiUtils  = new UIUtils( pluginId );

  	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId + "." +  INFOPOP_PBSC_PAGE );
  	parent.setToolTipText( AxisCreationUIMessages.TOOLTIP_PBSC_PAGE );
    
    Composite textGroup = uiUtils.createComposite( parent, 2, 0, 0 );

    /*
    uriText_ = uiUtils.createText( textGroup, "LABEL_URI",
                                   TOOLTIP_PBCF_TEXT_URI,
                                   INFOPOP_PBCF_TEXT_URI,
                                   SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    */

    wsdlFolderText_ = uiUtils.createText( textGroup, AxisCreationUIMessages.LABEL_OUTPUT_FOLDER_NAME,
    		AxisCreationUIMessages.TOOLTIP_PBCF_TEXT_WSDL_FOLDER,
                                          INFOPOP_PBCF_TEXT_WSDL_FOLDER,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    wsdlFileText_ = uiUtils.createText( textGroup, AxisCreationUIMessages.LABEL_OUTPUT_FILE_NAME,
    		AxisCreationUIMessages.TOOLTIP_PBCF_TEXT_WSDL_FILE,
                                        INFOPOP_PBCF_TEXT_WSDL_FILE,
                                        SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    skeletonFolderText_ = baseConUiUtils.createCombo( textGroup, ConsumptionUIMessages.LABEL_SKELETON_ROOT_NAME,
    		ConsumptionUIMessages.TOOLTIP_PBSC_TEXT_SKELETON_FOLDER,
        INFOPOP_PBSC_TEXT_SKELETON_FOLDER,
        SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );

    uiUtils.createHorizontalSeparator( parent, 5 );

    showMappingsCheckbox_ = conUiUtils.createCheckbox( parent, AxisConsumptionUIMessages.LABEL_EXPLORE_MAPPINGS_XML2BEAN,
    													AxisConsumptionUIMessages.TOOLTIP_N2P_SHOW_MAPPINGS,
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

//	  Do not base Java output directory on workspace root since the project could be not 
//	  physically located in the workspace root, get the project specific root instead
	  
	  String projectSpecificRoot = serverProject_.getLocation().toString();
	  String skeletonFolder = skeletonFolderText_.getText();
	  String projectPathString = serverProject_.getFullPath().toString();
	  if (skeletonFolder.startsWith(projectPathString)) {
		  skeletonFolder = skeletonFolder.substring(projectPathString.length());
	  }
	  javaWSDLParam.setJavaOutput(projectSpecificRoot + skeletonFolder);
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
	  serverProject_ = serviceProject;
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