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
 * 20060223   129232 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.widgets.popup;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.context.PersistentActionDialogsContext;
import org.eclipse.wst.command.internal.env.context.PersistentResourceContext;
import org.eclipse.wst.command.internal.env.core.data.DataFlowManager;
import org.eclipse.wst.command.internal.env.core.data.DataMappingRegistryImpl;
import org.eclipse.wst.command.internal.env.core.fragment.CommandFragment;
import org.eclipse.wst.command.internal.env.eclipse.EclipseEnvironment;
import org.eclipse.wst.command.internal.env.ui.eclipse.EclipseStatusHandler;
import org.eclipse.wst.command.internal.env.ui.widgets.CommandWidgetBinding;
import org.eclipse.wst.command.internal.env.ui.widgets.DynamicWizard;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleCommandEngineManager;
import org.eclipse.wst.command.internal.env.ui.widgets.SimplePopupPageFactory;
import org.eclipse.wst.command.internal.env.ui.widgets.WizardPageFactory;


/**
 * This class is used to popup dynamic wizards.  The popupMenus extension is used
 * the contribute a popup to Eclipse.  The class in the action element should reference
 * this DynamicPopupWizard class.  This id attribute in the action element should contain
 * an id that references a dynamicWizard extension point.  This is how the DynamicPopupWizard
 * class knows which wizard to display.
 *   
 * Note: the id attribute in the objectContribution element refers to an
 *       actionDialogPreferenceType extension point.  The ids in objectContribution
 *       element and the action element need not be the same as they are in the
 *       example below.
 *
 *<pre>
 *   &lt;extension
 *         point="org.eclipse.ui.popupMenus"&gt;
 *&lt;!-- IFile *.wsdl popup menu --&gt;
 *     &lt;objectContribution
 *           objectClass="org.eclipse.core.resources.IFile"
 *           nameFilter="*.wsdl"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard"&gt;
 *&lt;!-- WSDL To Java Bean Proxy --&gt;
 *        &lt;action
 *              label="%ACTION_GENERATE_JAVA_PROXY"
 *              class="org.eclipse.wst.command.internal.env.ui.widgets.popup.DynamicPopupWizard"
 *              menubarPath="org.eclipse.jst.ws.atk.ui.webservice.category.popupMenu/popupActions"
 *              id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard"&gt;
 *        &lt;/action&gt;
 *     &lt;/objectContribution&gt;
 *  &lt;/extension&gt;
 *
 *   &lt;extension
 *        point="org.eclipse.wst.command.env.dynamicWizard"&gt;
 *     &lt;dynamicWizard
 *           iconbanner="icons/full/wizban/webservicesclient_wiz.gif"
 *           class="org.eclipse.jst.ws.consumption.ui.widgets.binding.ClientWidgetBinding"
 *           title="%WIZARD_TITLE_WSC"
 *           id="org.eclipse.jst.ws.consumption.ui.wizard.client.clientwizard"&gt;
 *     &lt;/dynamicWizard&gt;
 *  &lt;/extension&gt;
 *</pre>
 *
 */
public class DynamicPopupWizard extends DynamicWizard implements IActionDelegate
{
  private ISelection selection_;
  
  public DynamicPopupWizard()
  {
	super();
  }
  
  /**
   * 
   * @see org.eclipse.wst.command.internal.env.ui.widgets.DynamicWizard#getWizardPageFactory()
   */
  protected WizardPageFactory getWizardPageFactory()
  {
    return new SimplePopupPageFactory( getId() );
  }
  
  private String getId()
  {
    return ((IConfigurationElement)originalElement_.getParent()).getAttribute( "id" );  
  }
  
  /**
   * @see IActionDelegate#run
   */
  public void run(IAction action) 
  {
    PersistentActionDialogsContext context = PersistentActionDialogsContext.getInstance();
    String                         id      = getId();
    
	if( context.showDialog(id) )
	{
	  // Launch the wizard.
	  init( PlatformUI.getWorkbench(), getSelection() );  
	    
	  WizardDialog dialog= new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), this);
	  dialog.setPageSize( 400, 500 );
	  dialog.create();
    
    if( getStartingPage() != null )
    {
	    dialog.open();
    }
	}
	else
	{
	  final ProgressMonitorDialog monitor = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
	  
	  try
	  {
	    monitor.run( false, false, new IRunnableWithProgress()
	                               {
	                                 public void run( IProgressMonitor progressMonitor )
	                                 {
	                             	     runHeadLess( getSelection(), monitor );
	                                 }
	                               } );
	  }
	  catch( InterruptedException exc )
	  { 
	  }
	  catch( InvocationTargetException exc )
	  {
	  }
	}
  }
  
  /**
   * Runs only the Commands associated with the dynamic wizard.
   * 
   * @param selection the initial selection from the user.
   * @param context the context that this execution should take place in.  This
   *                is usually a progress context so that the user can get feedback
   *                on how execution is progressing.  The context may be null.
   */
  public void runHeadLess( IStructuredSelection selection, IRunnableContext context )
  {
	PersistentResourceContext  resourceContext = PersistentResourceContext.getInstance();
	EclipseStatusHandler       handler         = new EclipseStatusHandler();
	EclipseEnvironment         environment     = new EclipseEnvironment( null, resourceContext, handler );
	    
	DataMappingRegistryImpl    dataRegistry_   = new DataMappingRegistryImpl();	    
	DataFlowManager            dataManager     = new DataFlowManager( dataRegistry_, environment );
	SimpleCommandEngineManager manager         = new SimpleCommandEngineManager(environment, dataManager );
	
    try
    {
      commandWidgetBinding_ = (CommandWidgetBinding)wizardElement_.createExecutableExtension( "class" );
    }
    catch( CoreException exc )
    {
      exc.printStackTrace();
    }
	  
	commandWidgetBinding_.registerDataMappings( dataRegistry_ );
	
	CommandFragment rootFragment = getRootFragment( selection, null );
	
	manager.setRootFragment( rootFragment );
	manager.runForwardToNextStop( context );
  }
  
  /**
   * @see IActionDelegate#selectionChanged
   */
  public void selectionChanged(IAction action, ISelection selection) 
  {
      selection_ = selection;
  }
  
  private IStructuredSelection getSelection()
  {
    IStructuredSelection result = new StructuredSelection();
    
	if( selection_ != null) 
	{	
	  if( selection_ instanceof IStructuredSelection ) 
	  {
	    result = (IStructuredSelection)selection_;
	  }
	}
	else 
	{
	  IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	  
	  if( window != null ) 
	  {
		ISelection selection = window.getSelectionService().getSelection();
		
		if( selection instanceof IStructuredSelection ) 
		{
		  result = (IStructuredSelection)selection;
		}
	  }
	}
	
	return result;
  }  
}
