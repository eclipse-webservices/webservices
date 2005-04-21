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
/**
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.common.FolderResourceFilter;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.jst.ws.internal.ui.dialog.DialogUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.selection.BooleanSelection;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionList;


public class ClientTestWidget extends SimpleWidgetDataContributor
{
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  /*CONTEXT_ID PWSM0001 for the Sample Page*/
  private String INFOPOP_PWSM_PAGE =  "PWSM0001";
  //
  private Button testCheckbox_;
  /*CONTEXT_ID PWSM0002 for the Test check box of the Sample Page*/
  private String INFOPOP_PWSM_CHECKBOX_TEST = "PWSM0002";

  private Combo testTypeCombo_;
   /*CONTEXT_ID PWSM0003 for the Test Type Combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBOBOX_TEST = "PWSM0003";

  private Text  jspFolderText_;
  /*CONTEXT_ID PWSM0008 for the JSP Folder field of the Sample Page*/
  private String INFOPOP_PWSM_TEXT_JSP_FOLDER = "PWSM0008";

  private Button sampleFolderBrowseButton_;
  /*CONTEXT_ID PWSM0009 for the JSP Folder Browse button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_JSP_FOLDER_BROWSE = "PWSM0009";

  private Combo projectCombo_;
  /*CONTEXT_ID PWSM0010 for the Project combo box of the Sample Page*/
  private String INFOPOP_PWSM_COMBO_PROJECT = "PWSM0010";

  private Tree methodsTree_;
  /*CONTEXT_ID PWSM0005 for the Methods tree of the Sample Page*/
  private String INFOPOP_PWSM_TREE_METHODS = "PWSM0005";

  private Button selectAllMethodsButton_;
  /*CONTEXT_ID PWSM0006 for the Select All button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_SELECT_ALL = "PWSM0006";

  private Button deselectAllMethodsButton_;
  /*CONTEXT_ID PWSM0007 for the Deselect All button of the Sample Page*/
  private String INFOPOP_PWSM_BUTTON_DESELECT_ALL = "PWSM0007";

  private Text sampleFolderText_;
  /*CONTEXT_ID PWSM0014 for the Folder field of the Sample Page*/   
  private String INFOPOP_PWSM_TEXT_SAMPLE_FOLDER = "PWSM0014";
  //
  private Button runTestCheckbox_;  
  /*CONTEXT_ID PWSM0015 for the run test check box of the Sample Page*/
  private String INFOPOP_PWSM_CHECKBOX_LAUNCH = "PWSM0015";
  //
  private Combo earCombo_;
  /*CONTEXT_ID PWSM0016 for the EAR combo box of the Sample Page*/
  private String INFOPOP_PWSM_EAR_COMBO = "PWSM0016";
  
  private Label                projectExistsLabel_;
  private Label                earExistsLabel_;
  private Composite            comboGroup_; 
  private SelectionList        testFacilities_;
  private FolderResourceFilter folderFilter_ = new FolderResourceFilter();
  private IStructuredSelection initialSelection_;
  private String launchedServiceTestName;
  private boolean isTestWidget = false;
  
  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    isTestWidget = true;
  	MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    UIUtils      uiUtils  = new UIUtils(msgUtils, pluginId_ );
        
	parent.setToolTipText( msgUtils.getMessage( "TOOLTIP_PWSM_PAGE" ) );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PWSM_PAGE );
    
    testCheckbox_ = uiUtils.createCheckbox( parent, "BUTTON_TEST",
                                            "TOOLTIP_PWSM_CHECKBOX_TEST",
                                            INFOPOP_PWSM_CHECKBOX_TEST );
    testCheckbox_.addSelectionListener( new SelectionAdapter()
                                        {
                                          public void widgetSelected( SelectionEvent evt )
                                          {
                                            handleTestButton();
                                          }
                                        });
    
    comboGroup_ = uiUtils.createComposite( parent, 3, 5, 0 );
    
    testTypeCombo_ = uiUtils.createCombo( comboGroup_, "LABEL_TEST_TYPES",
                                          "TOOLTIP_PWSM_COMBOBOX_TEST",
                                          INFOPOP_PWSM_COMBOBOX_TEST,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    Label dummyLabel = new Label( comboGroup_, SWT.NONE );
    
    projectCombo_ = uiUtils.createCombo( comboGroup_, "LABEL_JSP_PROJECT_NAME",
                                          "TOOLTIP_PWSM_COMBO_PROJECT",
                                          INFOPOP_PWSM_COMBO_PROJECT,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    projectCombo_.setEnabled( false );
    projectExistsLabel_ = new Label( comboGroup_, SWT.NONE );
    
    
    earCombo_ = uiUtils.createCombo( comboGroup_, "LABEL_EAR_PROJECTS",
                                     "TOOLTIP_PWSM_EAR_PROJECT",
                                     INFOPOP_PWSM_EAR_COMBO,
                                     SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    earCombo_.setEnabled( false );
    earExistsLabel_ = new Label( comboGroup_, SWT.NONE );
    
    
    
    sampleFolderText_ = uiUtils.createText( comboGroup_, "LABEL_FOLDER_NAME",
                                            "TOOLTIP_PWSM_TEXT_SAMPLE_FOLDER",
                                            INFOPOP_PWSM_TEXT_SAMPLE_FOLDER,
                                            SWT.SINGLE | SWT.BORDER );
    
    sampleFolderText_.addModifyListener( new ModifyListener()
        {
        public void modifyText( ModifyEvent evt )
        {
          handleFolderText(); 
        }
      });
    
       
    sampleFolderBrowseButton_ = uiUtils.createPushButton( comboGroup_, "BUTTON_BROWSE", 
                                                          "TOOLTIP_PWSM_BUTTON_JSP_FOLDER_BROWSE",
                                                          INFOPOP_PWSM_BUTTON_JSP_FOLDER_BROWSE );
    sampleFolderBrowseButton_.addSelectionListener( new SelectionAdapter()
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSampleBrowse(); 
                                                      }
                                                    });
    
    jspFolderText_ = uiUtils.createText( comboGroup_, "LABEL_JSP_FOLDER_NAME",
            "TOOLTIP_PWSM_TEXT_JSP_FOLDER",
            INFOPOP_PWSM_TEXT_JSP_FOLDER,
            SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    jspFolderText_.setEnabled( false );
    
    Group methodsGroup = uiUtils.createGroup( parent, "LABEL_METHODS",  
                                             "TOOLTIP_PWSM_TREE_METHODS", null );
    
	methodsGroup.setLayoutData( uiUtils.createFillAll() );
	
	GridLayout layout = new GridLayout();
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	methodsGroup.setLayout( layout );
	
	// TODO No infopop for this tree.
	methodsTree_ = uiUtils.createTree( methodsGroup, "TOOLTIP_PWSM_TREE_METHODS", 
	                                   null,
	                   				   SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.CHECK );

    Composite selectButtons = uiUtils.createComposite( methodsGroup, 2 );
    
    selectAllMethodsButton_ 
      = uiUtils.createPushButton( selectButtons, "BUTTON_SELECT_ALL", 
                                                 "TOOLTIP_PWSM_BUTTON_SELECT_ALL",
                                                 INFOPOP_PWSM_BUTTON_SELECT_ALL );
    selectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                  {
                                                    public void widgetSelected( SelectionEvent evt )
                                                    {
                                                      handleSelectAll( true );
                                                    }
                                                  });
    
    deselectAllMethodsButton_ 
      = uiUtils.createPushButton( selectButtons, "BUTTON_DESELECT_ALL", 
                                  "TOOLTIP_PWSM_BUTTON_DESELECT_ALL",
                                  INFOPOP_PWSM_BUTTON_DESELECT_ALL );
    deselectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSelectAll( false );
                                                      }
                                                    });
    
    runTestCheckbox_ = uiUtils.createCheckbox( parent, "BUTTON_RUN_TEST",
                                               "TOOLTIP_PWSM_CHECKBOX_LAUNCH",
                                               INFOPOP_PWSM_CHECKBOX_LAUNCH );
    
    return this;
  }
  
  private void handleFolderText()
  {
  	String folder = sampleFolderText_.getText();	
    String jspFolder = jspFolderText_.getText();
    int index = jspFolder.lastIndexOf(Path.SEPARATOR);
    if(index == -1) return;
    jspFolder = jspFolder.substring(0,index + 1);
    jspFolder = jspFolder + folder;
    jspFolderText_.setText(jspFolder);
  }
  
  private void handleSampleBrowse()
  {
    IPath      selectedPath       = null;
    IResource  initialResource    = null;
    IPath      projectPath        = new Path( projectCombo_.getText() ).makeAbsolute();
    IResource  project            = ResourceUtils.findResource( projectPath );
//    IContainer webModuleContainer = ResourceUtils.getWebModuleServerRoot( (IProject)project);
    IContainer webModuleContainer = J2EEUtils.getFirstWebContentContainer((IProject)project);  
    try
    {
      initialResource = ResourceUtils.getResourceFromSelection( initialSelection_ );
    }
    catch( CoreException exc )
    {  
    }
    
    if( initialResource == null )initialResource = project;
    
    IResource resource = DialogUtils.browseResources( comboGroup_.getShell(), 
                                                      project,
                                                      initialResource, 
                                                      folderFilter_ );
    
    if( resource != null )
    {
      selectedPath = resource.getFullPath();   
    
      int webModuleSegments = selectedPath.matchingFirstSegments(webModuleContainer.getFullPath());
      
      if ( webModuleSegments < 2 )
      {
        sampleFolderText_.setText("");
        handleFolderText();
      }
      else
      {
        sampleFolderText_.setText(selectedPath.removeFirstSegments(webModuleSegments).toString());
        handleFolderText();
      }
    }
  }
  
  private void handleTestButton()
  {
    boolean enabled = testCheckbox_.getSelection();
    
    testTypeCombo_.setEnabled( enabled );
    sampleFolderText_.setEnabled( enabled );
    methodsTree_.setEnabled( enabled );
    runTestCheckbox_.setEnabled( enabled );
    selectAllMethodsButton_.setEnabled( enabled );
    deselectAllMethodsButton_.setEnabled( enabled );
    sampleFolderBrowseButton_.setEnabled( enabled );
  }
  
  private void handleSelectAll( boolean value )
  {
	TreeItem[] items = methodsTree_.getItems();
	
	for( int i = 0; i < items.length; i++ ) 
	{
	  items[i].setChecked(value);
	}
  } 
  
  // Here are the getters and setters for this widget.
  public void setTestService( Boolean testService )
  {
    testCheckbox_.setSelection( testService.booleanValue() );
    handleTestButton();
  }
  
  public Boolean getTestService()
  {
    return new Boolean( testCheckbox_.getSelection() );
  }
  
  public void setTestFacility( SelectionList testFacilities )
  {
    testFacilities_ = testFacilities;
    testTypeCombo_.setItems( testFacilities.getList() );
    testTypeCombo_.select( testFacilities.getIndex() );
  }
  
  public SelectionList getTestFacility()
  {
    testFacilities_.setIndex( testTypeCombo_.getSelectionIndex() );
    return testFacilities_;
  }
  
  public String getTestID()
  {
    return testTypeCombo_.getText();
  }
  
  public void setSampleProject(String clientProject) 
  {
	projectCombo_.setItems( new String[]{ clientProject } );
	projectCombo_.select(0);
  }
  
  public void setSampleProjectEAR(String clientProjectEAR) 
  {
    earCombo_.setItems( new String[]{ clientProjectEAR } );
    earCombo_.select(0);
  }
  
  public String getFolder()
  {
    return sampleFolderText_.getText();
  }
  
  public void setFolder( String folder )
  {
    sampleFolderText_.setText( folder );
  }
  
  public boolean getIsTestWidget()
  {
  	return isTestWidget;
  }
  
  public void setJspFolder( String folder )
  {
    jspFolderText_.setText( folder );
  }
  
  public void setLaunchedServiceTestName (String launchedServiceTestName)
  {
  	this.launchedServiceTestName = launchedServiceTestName;
  }
  
  public boolean getRunClientTest()
  {
    return runTestCheckbox_.getSelection();
  }
  
  public void setRunClientTest( boolean value )
  {
    runTestCheckbox_.setSelection( value );
  }
  
  public BooleanSelection[] getMethods()
  {
    TreeItem[]         items   = methodsTree_.getItems();
    BooleanSelection[] methods = new BooleanSelection[items.length];
    
    for( int index = 0; index < items.length; index++ )
    {
      methods[index] = new BooleanSelection( items[index].getText(), items[index].getChecked() );
    }
    
    return methods;
  }
  
  public void setMethods( BooleanSelection[] methods )
  {
    methodsTree_.removeAll();
    
    for( int index = 0; index < methods.length; index++ )
    {
      if(methods[index] == null)continue;
      TreeItem item = new TreeItem( methodsTree_, SWT.NULL );
      item.setText( methods[index].getValue() );
      item.setChecked( methods[index].isSelected() );
    }
  }
  
  public void setInitialSelection( IStructuredSelection selection )
  {
    initialSelection_ = selection;
  }
}
