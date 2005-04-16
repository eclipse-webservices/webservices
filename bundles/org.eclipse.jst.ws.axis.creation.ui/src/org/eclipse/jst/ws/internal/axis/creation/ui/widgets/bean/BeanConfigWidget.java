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
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.env.ui.widgets.WidgetDataEvents;


public class BeanConfigWidget extends SimpleWidgetDataContributor 
{
  private String pluginId_ = "org.eclipse.jst.ws.axis.creation.ui";
  
  private Button   rpcEncodedButton_;
  private Button   rpcLiteralButton_;
  private Button   docLiteralButton_;
  
  private Listener statusListener_;
  
  private JavaWSDLParameter javaParameter_;
  
  /* CONTEXT_ID PBCF0001 for the Bean Config Page */
  private final String INFOPOP_PBCF_PAGE = "PBCF0001"; //$NON-NLS-1$
  private final String TOOLTIP_PBCF_PAGE = "TOOLTIP_PBCF_PAGE";
	
  private Text uriText_;
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
	
  private Tree methodsTree_;
  /* CONTEXT_ID PBME0002 for the Methods tree of the Bean Methods Page */
  private final String INFOPOP_PBME_TREE_METHODS = "PBME0002"; //$NON-NLS-1$
  private final String TOOLTIP_PBME_TREE_METHODS = "TOOLTIP_PBME_TREE_METHODS";
	
  private Button selectAllMethodsButton_;
  /* CONTEXT_ID PBME0010 for the Select All button of the Bean Methods Page */
  private final String INFOPOP_PBME_BUTTON_SELECT_ALL = "PBME0010"; //$NON-NLS-1$
  private final String TOOLTIP_PBME_BUTTON_SELECT_ALL = "TOOLTIP_PBME_BUTTON_SELECT_ALL";
	
  private Button deselectAllMethodsButton_;
  /* CONTEXT_ID PBME0011 for the Deselect All button of the Bean Methods Page */
  private final String INFOPOP_PBME_BUTTON_DESELECT_ALL = "PBME0011"; //$NON-NLS-1$
  private final String TOOLTIP_PBME_BUTTON_DESELECT_ALL = "TOOLTIP_PBME_BUTTON_DESELECT_ALL";
	
  private Button showMappingsCheckbox_;
  /* CONTEXT_ID PBCF0016 for the Show Mappings checkbox of the Bean Methods Page */   
  private String INFOPOP_P2N_SHOW_MAPPINGS = "PBCF0016"; //$NON-NLS-1$

  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
    String       baseConPluginId = "org.eclipse.jst.ws.consumption.ui";
    MessageUtils msgUtils     = new MessageUtils( pluginId_ + ".plugin", this );
    MessageUtils baseConUtils = new MessageUtils( baseConPluginId + ".plugin", this );
    UIUtils      uiUtils        = new UIUtils(msgUtils, pluginId_ );
    UIUtils      baseConUiUtils = new UIUtils( baseConUtils, pluginId_ );
    
    statusListener_ = statusListener;    
	parent.setToolTipText( msgUtils.getMessage( TOOLTIP_PBCF_PAGE ) );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PBCF_PAGE );
    
    Composite configGroup = uiUtils.createComposite( parent, 2 );
    
    uriText_ = uiUtils.createText( configGroup, "LABEL_URI",
                                   TOOLTIP_PBCF_TEXT_URI,
                                   INFOPOP_PBCF_TEXT_URI,
                                   SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    wsdlFolderText_ = uiUtils.createText( configGroup, "LABEL_OUTPUT_FOLDER_NAME",
                                          TOOLTIP_PBCF_TEXT_WSDL_FOLDER,
                                          INFOPOP_PBCF_TEXT_WSDL_FOLDER,
                                          SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
    
    wsdlFileText_ = uiUtils.createText( configGroup, "LABEL_OUTPUT_FILE_NAME",
                                        TOOLTIP_PBCF_TEXT_WSDL_FILE,
                                        INFOPOP_PBCF_TEXT_WSDL_FILE,
                                        SWT.SINGLE | SWT.BORDER  );
    wsdlFileText_.addListener( SWT.Modify, statusListener );
    
    Label separator = uiUtils.createHorizontalSeparator( parent, 6 );
    
    // TODO this group has no TOOLTIP or INFOPOP.
    Group    methodsGroup = baseConUiUtils.createGroup( parent, "LABEL_METHODS", null, null );
	methodsGroup.setLayoutData( uiUtils.createFillAll() );
	
	GridLayout layout = new GridLayout();
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	methodsGroup.setLayout( layout );
	
	methodsTree_ = uiUtils.createTree( methodsGroup, TOOLTIP_PBME_TREE_METHODS, 
	                                   INFOPOP_PBME_TREE_METHODS,
	                   				   SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.CHECK );
	methodsTree_.addListener( SWT.Selection, statusListener );

    Composite selectButtons = uiUtils.createComposite( methodsGroup, 2 );
    
    selectAllMethodsButton_ 
      = baseConUiUtils.createPushButton( selectButtons, "BUTTON_SELECT_ALL", 
                                  TOOLTIP_PBME_BUTTON_SELECT_ALL,
                                  INFOPOP_PBME_BUTTON_SELECT_ALL );
    selectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                  {
                                                    public void widgetSelected( SelectionEvent evt )
                                                    {
                                                      handleSelectAll( true );
                                                    }
                                                  });
    
    deselectAllMethodsButton_ 
      = baseConUiUtils.createPushButton( selectButtons, "BUTTON_DESELECT_ALL", 
                                  TOOLTIP_PBME_BUTTON_DESELECT_ALL,
                                  INFOPOP_PBME_BUTTON_DESELECT_ALL );
    deselectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSelectAll( false );
                                                      }
                                                    });
    
    // TODO this group has no TOOLTIP or INFOPOP.
    Group styleGroup = uiUtils.createGroup( parent, "LABEL_STYLE_USE", null, null );
    
    // TODO radio buttons have no TOOLTIP or INFOPOP.
    docLiteralButton_ = uiUtils.createRadioButton( styleGroup, "STYLE_DOC_LITERAL", null, null );
    rpcLiteralButton_ = uiUtils.createRadioButton( styleGroup, "STYLE_RPC_LITERAL", null, null );
    rpcEncodedButton_ = uiUtils.createRadioButton( styleGroup, "STYLE_RPC_ENCODED", null, null );
    
    showMappingsCheckbox_ = uiUtils.createCheckbox( parent, "LABEL_EXPLORE_MAPPINGS_BEAN2XML",
                                                    "TOOLTIP_P2N_SHOW_MAPPINGS",
                                                    INFOPOP_P2N_SHOW_MAPPINGS );
                                            
    return this;
  }
  
  public void handleSelectAll( boolean value )
  {
	TreeItem[] items = methodsTree_.getItems();
	
	for( int i = 0; i < items.length; i++ ) 
	{
	  items[i].setChecked(value);
	}
	
	statusListener_.handleEvent( null );
  }
  
  public void setCustomizeServiceMappings( boolean value )
  {
    showMappingsCheckbox_.setSelection( value );
  }
  
  public boolean getCustomizeServiceMappings()
  {
    return showMappingsCheckbox_ == null ? false : showMappingsCheckbox_.getSelection();
  }
  
  public void setJavaParameter( JavaWSDLParameter javaParameter )
  {
    javaParameter_ = javaParameter;
    
	String wsdlLocation = javaParameter.getOutputWsdlLocation();

	// TODO Verify that we need to find the WSDL in the eclipse file system.
	//IFile file 
	//  = ResourceUtils.getWorkspaceRoot().getFileForLocation(new Path(wsdlLocation));
	//IPath wsdlPath = file.getFullPath();
	IPath wsdlPath = new Path( wsdlLocation );
	
	wsdlFolderText_.setText(wsdlPath.removeLastSegments(1).toString());
	wsdlFileText_.setText(wsdlPath.lastSegment());
	
	String location = javaParameter.getUrlLocation();
	uriText_.setText(location == null ? "" : location);  
	
	methodsTree_.removeAll();
	Hashtable methods = javaParameter.getMethods();
	Enumeration e = methods.keys();
	
	while( e.hasMoreElements() )
	{
	  String   name = (String) e.nextElement();
      TreeItem item = new TreeItem(methodsTree_, SWT.NULL);
      
   	  item.setData(name);
      item.setText(name);
      item.setChecked((((Boolean) methods.get(name)).booleanValue()));
	}
	
	TreeItem[] items = methodsTree_.getItems();
	
	if( items.length > 0 ) 
	{
	  methodsTree_.setSelection( new TreeItem[]{ items[0] } );
	}
	
	String style = javaParameter.getStyle();
	
	if( style.equals( JavaWSDLParameter.STYLE_RPC ) )
	{
	  rpcEncodedButton_.setSelection(true);
	}
	else if( style.equals( JavaWSDLParameter.STYLE_DOCUMENT ) )
	{
	  rpcLiteralButton_.setSelection(true);	  
	}
	else
	{
	  docLiteralButton_.setSelection(true);  
	}
  }
  
  public JavaWSDLParameter getJavaParameter()
  {
    IPath wsdlPath 
      = new Path( wsdlFolderText_.getText().trim() ).append( wsdlFileText_.getText().trim() );
	IWorkspaceRoot workspace = ResourceUtils.getWorkspaceRoot();
    
	// TODO Do we need to go to the eclipse file system??
	//String wsdlLocation = workspace.getFile(wsdlPath).getLocation().toString();
	String wsdlLocation = wsdlPath.toString();
	
	javaParameter_.setOutputWsdlLocation(wsdlLocation);
	javaParameter_.setInputWsdlLocation(wsdlLocation);
	
	String fileURL = PlatformUtils.getFileURLFromPath(new Path(wsdlLocation));

	Hashtable methods = new Hashtable();

	TreeItem[] items = methodsTree_.getItems();
	
	for( int i = 0; i < items.length; i++ ) 
	{
	  TreeItem item = items[i];
	  methods.put((String)item.getData(), new Boolean(item.getChecked()));
	}
	
	javaParameter_.setMethods(methods);
	
	if( rpcEncodedButton_.getSelection()) 
	{
	  javaParameter_.setStyle(JavaWSDLParameter.STYLE_RPC);
	  javaParameter_.setUse(JavaWSDLParameter.USE_ENCODED);
	} 
	else if( rpcLiteralButton_.getSelection()) 
	{
	  javaParameter_.setStyle(JavaWSDLParameter.STYLE_DOCUMENT);
	  javaParameter_.setUse(JavaWSDLParameter.USE_LITERAL);
	} 
	else 
	{
	  javaParameter_.setStyle(JavaWSDLParameter.STYLE_WRAPPED);
	  javaParameter_.setUse(JavaWSDLParameter.USE_LITERAL);
	}
    
	return javaParameter_;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.command.env.ui.widgets.WidgetContributor#getStatus()
   */
  public Status getStatus() 
  {
    Status       result   = null;
    MessageUtils msgUtils = new MessageUtils( pluginId_ + ".plugin", this );
    
    if( wsdlFileText_.getText().equals( "" ) )
    {
      result = new SimpleStatus( "", msgUtils.getMessage( "PAGE_MSG_NO_FILE_SPECIFIED" ), Status.ERROR ); 
    }
    else
    {
      TreeItem[] items        = methodsTree_.getItems();
      boolean    itemSelected = false;
      
      for( int index = 0; index < items.length; index++ )
      {
        if( items[index].getChecked() )
        {
          itemSelected = true;
          break;
        }
      }
      
      if( !itemSelected )
      {
        result = new SimpleStatus( "", msgUtils.getMessage( "PAGE_MSG_NO_METHOD_SELECTED" ), Status.ERROR ); 
      }
    }
    
    return result;
  }
}
