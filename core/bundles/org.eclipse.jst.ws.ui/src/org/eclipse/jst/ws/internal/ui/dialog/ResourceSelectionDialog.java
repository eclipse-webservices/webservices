/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.ui.dialog;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.common.AnyFilter;
import org.eclipse.jst.ws.internal.common.Filter;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.ui.WSUIPluginMessages;
import org.eclipse.jst.ws.internal.ui.plugin.WebServiceUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;


/**
* This class provides a dialog for browsing workspace resources.
* See also {@link DialogUtils#browseResources}.
*/
public class ResourceSelectionDialog extends Dialog implements Listener
{

  // Dialog controls.
  //
  private Label filterLabel_;
  private Combo filterCombo_;
  /*CONTEXT_ID DRES0001 for the Resource Type combo box of the Resource Selection Dialog*/
  private static final String INFOPOP_DRES_COMBO_RESOURCE_TYPE = WebServiceUIPlugin.ID + ".DRES0001";

  private Tree resourceTree_;
  private TreeViewer fileViewer_;
  /*CONTEXT_ID DRES0002 for the Resource tree of the Resource Selection Dialog*/
  private static final String INFOPOP_DRES_TREE_RESOURCE = WebServiceUIPlugin.ID + ".DRES0002";

  //
  // Essential goodies.
  //
  private IResource root_;
  private IResource initialSelection_;
  private Filter[] filters_;
  private Filter currentFilter_;
  private IResource[] selection_;
  private boolean multipleSelectionEnabled_;

  /**
  * Constructs a new <code>ResourceSelectionDialog</code>
  * under the given <code>parent Shell</code>.
  * The dialog renders all resources including and
  * under the given <code>root IResource</code>.
  * @param parent The parent {@link org.eclipse.swt.widgets.Shell},
  * or null to create a top-level shell.
  * @param root The root {@link org.eclipse.core.resources.IResource},
  * or null to begin with the workspace root.
  * @param initialSelection The initially selected object or null if none.
  */
  public ResourceSelectionDialog ( Shell parent, IResource root, IResource initialSelection )
  {
    this(parent,root,initialSelection,new Filter[] {new AnyFilter()});
  }

  /**
  * Constructs a new <code>ResourceSelectionDialog</code>
  * under the given <code>parent Shell</code>.
  * The dialog renders all resources including and
  * under the given <code>root IResource</code> that
  * are accepted by the given filter.
  * See {@link org.eclipse.jst.ws.internal.common.Filter#accepts}.
  * @param parent The parent {@link org.eclipse.swt.widgets.Shell},
  * or null to create a top-level shell.
  * @param root The root {@link org.eclipse.core.resources.IResource},
  * or null to begin with the workspace root.
  * @param initialSelection The initially selected object or null if none.
  * @param filter The {@link org.eclipse.jst.ws.internal.common.Filter},
  * or null to default to all resources under the <code>root</code>.
  */
  public ResourceSelectionDialog ( Shell parent, IResource root, IResource initialSelection, Filter filter )
  {
    this(parent,root,initialSelection,new Filter[] {filter});
  }

  /**
  * Constructs a new <code>ResourceSelectionDialog</code>
  * under the given <code>parent Shell</code>.
  * The dialog renders all resources including and
  * under the given <code>root IResource</code> that
  * are accepted by the given filters.
  * See {@link org.eclipse.jst.ws.internal.common.Filter#accepts}.
  * @param parent The parent {@link org.eclipse.swt.widgets.Shell}.
  * or null to create a top-level shell.
  * @param root The root {@link org.eclipse.core.resources.IResource},
  * or null to begin with the workspace root.
  * @param initialSelection The initially selected object or null if none.
  * @param filters An array of {@link org.eclipse.jst.ws.internal.common.Filter}
  * objects.
  * If the array is null or of length zero, then all resources are accepted
  * (ie. equivalent to <code>ResourceSelectionDialog(parent,root)</code>.
  * If the array is of length one, then the dialog will consist of a single
  * control showing a list of all filtered resources.
  * If the array is of length greater than one, then the dialog will
  * consist of a combo box containing the names of the filters and with the
  * first filter (ie. <code>filters[0]</code>) selected by default, followed
  * by a control showing a list of all resources filtered by the selected filter. 
  */
  public ResourceSelectionDialog ( Shell parent, IResource root, IResource initialSelection, Filter[] filters )
  {
    super(parent);
    root_ = root == null ? ResourceUtils.getWorkspaceRoot() : root;
    initialSelection_ = initialSelection;
    filters_ = (filters == null || filters.length == 0) ? new Filter[] {new AnyFilter()} : filters;
    currentFilter_ = filters_[0];
    multipleSelectionEnabled_ = false;
    setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
  }

  /**
  * Enables or disables multiple selection in the tree view.
  * Multiple selection is disabled by default.
  * @param multipleSelectionEnabled True to enabled or false to disable.
  */
  public void setMultipleSelectionEnabled ( boolean multipleSelectionEnabled )
  {
    multipleSelectionEnabled_ = multipleSelectionEnabled;
  }

  /**
  * Returns the selections made in the dialog if OK was pressed,
  * or null if Cancel was pressed. Returns null if the dialog
  * has never been opened.
  * @return An array of selected resources, possibly empty, possibly null.
  */
  public IResource[] getResult ()
  {
    return selection_;
  }

  /**
  * Called when the Cancel button is pressed.
  * Insures that {@link #getResult} will return null.
  */
  protected void cancelPressed ()
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
  protected void okPressed ()
  {
    ISelection selection = fileViewer_.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection)selection;
      selection_ = new IResource[structuredSelection.size()];
      int i=0;
      Iterator iter = structuredSelection.iterator();
      while (iter.hasNext())
      {
        Object object = iter.next();
        if (object instanceof IResource)
        {
          selection_[i++] = (IResource)object;
        }
      }
    }
    setReturnCode(Dialog.OK);
    super.okPressed();
  }

  /**
  * See {@link org.eclipse.jface.window.Window#configureShell}.
  * @param shell The shell.
  */
  protected void configureShell ( Shell shell )
  {
    super.configureShell(shell);
    shell.setText(WSUIPluginMessages.DIALOG_TITLE_RESOURCE_BROWSE);
  }

  /** 
  * Creates the dialog area.
  * @param parent The parent composite.
  * @return The control area.
  */
  protected Control createDialogArea ( Composite parent )
  {
    GridLayout gl;
    GridData gd;

    Composite composite = (Composite)super.createDialogArea(parent);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    gd.widthHint = 400;
    gd.heightHint = 300;
    gd.grabExcessVerticalSpace = true;
    gd.grabExcessHorizontalSpace = true;
    composite.setLayoutData(gd);

    if (filters_.length > 1)
    {
      Composite f = new Composite(composite,SWT.NONE);
      gl = new GridLayout();
      gl.numColumns = 2;
      gl.marginHeight = 0;
      gl.marginWidth = 0;
      f.setLayout(gl);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
      f.setLayoutData(gd);

      filterLabel_ = new Label(f,SWT.WRAP);
      filterLabel_.setText(WSUIPluginMessages.LABEL_RESOURCE_FILTER);
      filterLabel_.setToolTipText(WSUIPluginMessages.TOOLTIP_DRES_COMBO_RESOURCE_TYPE);

      filterCombo_ = new Combo(f,SWT.DROP_DOWN | SWT.READ_ONLY);
      gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
      filterCombo_.setLayoutData(gd);
      filterCombo_.addListener(SWT.Selection,this);
      filterCombo_.setToolTipText(WSUIPluginMessages.TOOLTIP_DRES_COMBO_RESOURCE_TYPE);
      PlatformUI.getWorkbench().getHelpSystem().setHelp(filterCombo_,INFOPOP_DRES_COMBO_RESOURCE_TYPE);
    }

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
//  resourceTree_.addListener(SWT.Selection,this);
    resourceTree_.setToolTipText(WSUIPluginMessages.TOOLTIP_DRES_TREE_RESOURCE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(resourceTree_,INFOPOP_DRES_TREE_RESOURCE);

    fileViewer_ = new TreeViewer(resourceTree_);
    fileViewer_.setContentProvider(new WorkbenchContentProvider());
    fileViewer_.setLabelProvider(
                new DecoratingLabelProvider(
				        new WorkbenchLabelProvider(), 
				        WebServiceUIPlugin.getInstance().getWorkbench().getDecoratorManager().getLabelDecorator()));
    fileViewer_.addFilter(
      new ViewerFilter ()
      {
        public boolean select ( Viewer viewer, Object parentObject, Object object )
        {
          return
          (
            ((object instanceof IResource) && ((IResource)object).getType() != IResource.FILE)
            || (ResourceSelectionDialog.this.currentFilter_.accepts(object))
          );
        }
      }
    );
    fileViewer_.setInput(root_);
    fileViewer_.addSelectionChangedListener(
      new ISelectionChangedListener ()
      {
        public void selectionChanged ( SelectionChangedEvent event )
        {
//        ResourceSelectionDialog.this.validatePageToStatus();
        }
      }
    );
    if (initialSelection_ != null)
    {
      fileViewer_.setSelection(new StructuredSelection(ResourceSelectionDialog.this.initialSelection_),true);
    }

    return composite;
  }

  /**
  * Called when an event occurs on the page.
  * Handles the event and revalidates the page.
  * @param event The event that occured.
  */
  public void handleEvent ( Event event )
  {
//  validatePageToStatus();
  }
}
