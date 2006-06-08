/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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
 * 20060524   128601 andyzhai@ca.ibm.com - Andy Zhai
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.widgets.bean;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.creation.ui.AxisCreationUIMessages;
import org.eclipse.jst.ws.internal.consumption.ui.ConsumptionUIMessages;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;


public class BeanConfigWidget extends SimpleWidgetDataContributor 
{

  private Button   rpcEncodedButton_;
  private Button   rpcLiteralButton_;
  private Button   docLiteralButton_;
  
  private Listener statusListener_;
  
  private JavaWSDLParameter javaParameter_;
  
  /* CONTEXT_ID PBCF0001 for the Bean Config Page */
  private final String INFOPOP_PBCF_PAGE = "PBCF0001"; //$NON-NLS-1$
	
  private String wsdlFolder_;
  
  private Text wsdlFileText_;
  /* CONTEXT_ID PBCF0007 for the WSDL File field of the Bean Config Page */
  private final String INFOPOP_PBCF_TEXT_WSDL_FILE = "PBCF0007"; //$NON-NLS-1$
	
  private Tree methodsTree_;
  /* CONTEXT_ID PBME0002 for the Methods tree of the Bean Methods Page */
  private final String INFOPOP_PBME_TREE_METHODS = "PBME0002"; //$NON-NLS-1$
	
  private Button selectAllMethodsButton_;
  /* CONTEXT_ID PBME0010 for the Select All button of the Bean Methods Page */
  private final String INFOPOP_PBME_BUTTON_SELECT_ALL = "PBME0010"; //$NON-NLS-1$
	
  private Button deselectAllMethodsButton_;
  /* CONTEXT_ID PBME0011 for the Deselect All button of the Bean Methods Page */
  private final String INFOPOP_PBME_BUTTON_DESELECT_ALL = "PBME0011"; //$NON-NLS-1$
	
  private Button showMappingsCheckbox_;
  /* CONTEXT_ID PBCF0016 for the Show Mappings checkbox of the Bean Methods Page */   
  private String INFOPOP_P2N_SHOW_MAPPINGS = "PBCF0016"; //$NON-NLS-1$

  public WidgetDataEvents addControls( Composite parent, Listener statusListener )
  {
	String pluginId_ = "org.eclipse.jst.ws.axis.creation.ui";	  

    UIUtils      uiUtils        = new UIUtils( pluginId_ );
    UIUtils      baseConUiUtils = new UIUtils( pluginId_ );
    
    statusListener_ = statusListener;    
	parent.setToolTipText( AxisCreationUIMessages.TOOLTIP_PBCF_PAGE  );
	PlatformUI.getWorkbench().getHelpSystem().setHelp( parent, pluginId_ + "." + INFOPOP_PBCF_PAGE );
    
    Composite configGroup = uiUtils.createComposite( parent, 2 );    
    
    wsdlFileText_ = uiUtils.createText( configGroup, AxisCreationUIMessages.LABEL_OUTPUT_FILE_NAME,
    		AxisCreationUIMessages.TOOLTIP_PBCF_TEXT_WSDL_FILE,
                                        INFOPOP_PBCF_TEXT_WSDL_FILE,
                                        SWT.SINGLE | SWT.BORDER  );
    wsdlFileText_.addListener( SWT.Modify, statusListener );
    
    // TODO this group has no TOOLTIP or INFOPOP.
    Group    methodsGroup = baseConUiUtils.createGroup( parent, ConsumptionUIMessages.LABEL_METHODS, null, null );
	methodsGroup.setLayoutData( uiUtils.createFillAll() );
	
	GridLayout layout = new GridLayout();
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	methodsGroup.setLayout( layout );
	
	methodsTree_ = uiUtils.createTree( methodsGroup, AxisCreationUIMessages.TOOLTIP_PBME_TREE_METHODS, 
										INFOPOP_PBME_TREE_METHODS,
	                   				   SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL	| SWT.CHECK );
	methodsTree_.addListener( SWT.Selection, statusListener );

    Composite selectButtons = uiUtils.createComposite( methodsGroup, 2 );
    
    selectAllMethodsButton_ 
      = baseConUiUtils.createPushButton( selectButtons, ConsumptionUIMessages.BUTTON_SELECT_ALL, 
    		  ConsumptionUIMessages.TOOLTIP_PBME_BUTTON_SELECT_ALL,
                                  INFOPOP_PBME_BUTTON_SELECT_ALL );
    selectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                  {
                                                    public void widgetSelected( SelectionEvent evt )
                                                    {
                                                      handleSelectAll( true );
                                                    }
                                                  });
    
    deselectAllMethodsButton_ 
      = baseConUiUtils.createPushButton( selectButtons, ConsumptionUIMessages.BUTTON_DESELECT_ALL, 
    		  ConsumptionUIMessages.TOOLTIP_PBME_BUTTON_DESELECT_ALL,
                                  INFOPOP_PBME_BUTTON_DESELECT_ALL );
    deselectAllMethodsButton_.addSelectionListener( new SelectionAdapter() 
                                                    {
                                                      public void widgetSelected( SelectionEvent evt )
                                                      {
                                                        handleSelectAll( false );
                                                      }
                                                    });
    
    // TODO this group has no TOOLTIP or INFOPOP.
    Group styleGroup = uiUtils.createGroup( parent, AxisCreationUIMessages.LABEL_STYLE_USE, null, null );
    
    // TODO radio buttons have no TOOLTIP or INFOPOP.
    docLiteralButton_ = uiUtils.createRadioButton( styleGroup, AxisCreationUIMessages.STYLE_DOC_LITERAL, null, null );
    rpcLiteralButton_ = uiUtils.createRadioButton( styleGroup, AxisCreationUIMessages.STYLE_RPC_LITERAL, null, null );
    rpcEncodedButton_ = uiUtils.createRadioButton( styleGroup, AxisCreationUIMessages.STYLE_RPC_ENCODED, null, null );
    
    showMappingsCheckbox_ = uiUtils.createCheckbox( parent, AxisCreationUIMessages.LABEL_EXPLORE_MAPPINGS_BEAN2XML,
    		AxisCreationUIMessages.TOOLTIP_P2N_SHOW_MAPPINGS,
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
	IPath wsdlPath = new Path( wsdlLocation );
	
	wsdlFolder_ = wsdlPath.removeLastSegments(1).toString();    
	wsdlFileText_.setText(wsdlPath.lastSegment());
    
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
    = new Path( wsdlFolder_.trim() ).append( wsdlFileText_.getText().trim() );
    
	// TODO Do we need to go to the eclipse file system??
	//String wsdlLocation = workspace.getFile(wsdlPath).getLocation().toString();
	String wsdlLocation = wsdlPath.toString();
	
	javaParameter_.setOutputWsdlLocation(wsdlLocation);
	javaParameter_.setInputWsdlLocation(new File(wsdlLocation).toURI().toString());
	
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
  public IStatus getStatus() 
  {
    IStatus       result   = null;
    
    if( wsdlFileText_.getText().equals( "" ) )
    {
      result = StatusUtils.errorStatus( AxisCreationUIMessages.PAGE_MSG_NO_FILE_SPECIFIED ); 
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
        result = StatusUtils.errorStatus( AxisCreationUIMessages.PAGE_MSG_NO_METHOD_SELECTED ); 
      }
    }
    
    return result;
  }
}
