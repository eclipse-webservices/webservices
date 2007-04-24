/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070423   183075 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.common;

import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.ui.WSUIPluginMessages;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
* This class provides a dialog for browsing workspace resources.
*/
public class DialogResourceBrowser extends Dialog
{
  private final String INFOPOP_RESOURCE_TREE = WebServiceUIPlugin.ID + ".DRES0001";

  private IResource root_;
  private IFilter[] filters_;
  private boolean multipleSelectionEnabled_;
  private IResource[] selection_;
  private Tree resourceTree_;
  private TreeViewer fileViewer_;
  private HashMap    visitedContainers_;
  private boolean    foldersSelectable_;

  public DialogResourceBrowser(Shell shell, IResource root, IFilter filter)
  {
    this(shell, root, new IFilter[]{filter}, false );
  }

  /**
  * Constructs a new <code>DialogResourceBrowser</code>
  * under the given <code>parent Shell</code>.
  * The dialog renders all resources including and
  * under the given <code>root IResource</code>
  * @param parent The parent {@link org.eclipse.swt.widgets.Shell}.
  * or null to create a top-level shell.
  * @param root The root {@link org.eclipse.core.resources.IResource},
  * or null to begin with the workspace root.
  * @param filters An array of java.lang.String
  */
  public DialogResourceBrowser(Shell shell, IResource root, IFilter[] filters)
  {
    this( shell, root, filters, false );
  }
  
  public DialogResourceBrowser(Shell shell, IResource root, IFilter[] filters, boolean foldersSelectable )
  {
    super(shell);

    IResource moduleRoot = root;
    if (root instanceof IProject)
    {
      IProject p = (IProject)moduleRoot;
     
        moduleRoot = p;
    
    }

    root_ = (moduleRoot == null) ? ResourcesPlugin.getWorkspace().getRoot() : moduleRoot;
    filters_ = (filters == null) ? new IFilter[0] : filters;
    visitedContainers_ = new HashMap();
    multipleSelectionEnabled_ = false;
    foldersSelectable_ = foldersSelectable;
    setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
  }

  /**
  * Enables or disables multiple selection in the tree view.
  * Multiple selection is disabled by default.
  * @param multipleSelectionEnabled True to enabled or false to disable.
  */
  public void setMultipleSelectionEnabled(boolean multipleSelectionEnabled)
  {
    multipleSelectionEnabled_ = multipleSelectionEnabled;
  }

  /**
  * Returns the selections made in the dialog if OK was pressed,
  * or null if Cancel was pressed. Returns null if the dialog
  * has never been opened.
  * @return An array of selected resources, possibly empty, possibly null.
  */
  public IResource[] getSelection()
  {
    return selection_;
  }

  public IResource getFirstSelection()
  {
    if (selection_ != null && selection_.length > 0)
      return selection_[0];
    else
      return null;
  }

   /**
  * Called when the Cancel button is pressed.
  * Insures that {@link #getResult} will return null.
  */
  protected void cancelPressed()
  {
    selection_ = null;
    setReturnCode(Dialog.CANCEL);
    super.cancelPressed();
  }

  /**
  * Called when the OK button is pressed.
  * Squirrels away the list of zero or more selected
  * <code>IResource</code>s to be returned by {@link #getResult}.
  */
  protected void okPressed()
  {
    ISelection selection = fileViewer_.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection)selection;
      selection_ = new IResource[structuredSelection.size()];
      int i = 0;
      Iterator it = structuredSelection.iterator();
      while (it.hasNext())
      {
        Object object = it.next();
        if (object instanceof IResource)
          selection_[i++] = (IResource)object;
      }
    }
    setReturnCode(Dialog.OK);
    super.okPressed();
  }

  /**
  * See {@link org.eclipse.jface.window.Window#configureShell}.
  * @param shell The shell.
  */
  protected void configureShell(Shell shell)
  {
    super.configureShell(shell);
    shell.setText(WSUIPluginMessages.DIALOG_TITLE_RESOURCE_BROWSER);
  }

  /** 
  * Creates the dialog area.
  * @param parent The parent composite.
  * @return The control area.
  */
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite)super.createDialogArea(parent);
    GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    gd.widthHint = 400;
    gd.heightHint = 300;
    gd.grabExcessVerticalSpace = true;
    gd.grabExcessHorizontalSpace = true;
    composite.setLayoutData(gd);

    if (multipleSelectionEnabled_)
    {
      resourceTree_ = new Tree(composite,SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    }
    else
    {
      resourceTree_ = new Tree(composite,SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    }
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    gd.grabExcessVerticalSpace = true;
    gd.grabExcessHorizontalSpace = true;
    resourceTree_.setLayoutData(gd);
    resourceTree_.setToolTipText(WSUIPluginMessages.TOOLTIP_RESOURCE_TREE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(resourceTree_, INFOPOP_RESOURCE_TREE);

    fileViewer_ = new TreeViewer(resourceTree_);
    fileViewer_.setContentProvider(new WorkbenchContentProvider());
    fileViewer_.setLabelProvider(new DecoratingLabelProvider(new WorkbenchLabelProvider(), WebServiceUIPlugin.getInstance().getWorkbench().getDecoratorManager().getLabelDecorator()));
    fileViewer_.addFilter(
      new ViewerFilter()
      {
        public boolean select(Viewer viewer, Object parentObject, Object object)
        {
          if( object instanceof IResource )
          {
            return isValidResource( (IResource)object );
          }
          
          return false;
        }
      }
    );
    
    if( !foldersSelectable_ )
    {
      fileViewer_.addSelectionChangedListener( new ISelectionChangedListener()
                                               {
                                                 public void selectionChanged(SelectionChangedEvent event)
                                                 {
                                                   handleSelectionChanged();
                                                 }
                                               } );
    }
    
    fileViewer_.setInput(root_);
    return composite;
  }

  private void handleSelectionChanged()
  {
    ISelection selection      = fileViewer_.getSelection();
    boolean    validSelection = true;
    
    if( selection instanceof IStructuredSelection )
    {
      IStructuredSelection strucSelection = (IStructuredSelection)selection;
      Iterator             iter           = strucSelection.iterator();
      
      // Ensure that all selected items are valid.
      while( iter.hasNext() )
      {
        Object object = iter.next();
        
        if( object instanceof IFile )
        {
          validSelection = acceptsFile( (IFile)object );
        }
        else
        {
          validSelection = false;
        }
        
        if( !validSelection ) break;
      }
    }
    else
    {
      validSelection = false;
    }
    
    Button okButton = getButton( Dialog.OK );
    okButton.setEnabled( validSelection );
  }
  
  private boolean acceptsFile( IResource file )
  {
    boolean result = false;
    
    for (int i = 0; i < filters_.length; i++)
    {
      if( filters_[i].accepts( file ) )
      {
        result = true;
        break;
      }
    }
    
    return result;
  }
  
  private boolean isValidResource( IResource resource )
  {
    boolean result = false;
    
    if( resource instanceof IFile )
    {
      // Loop over all filters.  If one is acceptable then we will
      // return true.
      result = acceptsFile( resource );
    }
    else if( resource instanceof IContainer )
    {
      // Now see if this container contains at least one valid file.
      Boolean validContainer = (Boolean)visitedContainers_.get( resource );
      
      if( validContainer == null )
      {
        // We haven't seen this container before.
        try 
        {
          IContainer  container = (IContainer)resource;
          IResource[] members   = container.members();
          
          visitedContainers_.put( container, Boolean.FALSE );
          
          for( int index = 0; index < members.length; index++) 
          {
            if( isValidResource(members[index]) ) 
            {
              visitedContainers_.put( container, Boolean.TRUE );
              result = true;
              break;
            }
          }
        } 
        catch( CoreException exc ) 
        {
          // Do nothing
        }
      }
      else
      {
        result = validContainer.booleanValue();
      }
    }
      
    return result;
  }
}
