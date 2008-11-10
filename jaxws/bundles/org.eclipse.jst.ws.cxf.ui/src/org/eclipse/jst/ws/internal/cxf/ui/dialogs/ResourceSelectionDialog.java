/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.ui.dialogs;

import java.io.File;
import java.util.Arrays;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * A <code>SelectionStatusDialog</code> which allows the selection of files
 * with a specific extension from the workspace or file system.
 * 
 * @author sclarke
 */
public class ResourceSelectionDialog extends SelectionStatusDialog {
    private IStatus OK_FILE_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$

    private IStatus ERROR_FILE_STATUS = new Status(IStatus.ERROR, CXFUIPlugin.PLUGIN_ID,
            CXFUIMessages.RESOURCE_SELECTIN_DIALOG_BROWSE_FILE_NOT_FOUND_ERROR);

    private IStatus VALIDATE_SELECTION_STATUS = new Status(IStatus.ERROR, CXFUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$

    private Text resourceText;

    private String filterExtension;
    private String filterName;

    /**
     * Creates an instance of <code>ResourceSelectionDialog</code>
     * 
     * @param parent
     * @param filterExtension
     *            the file name extension which the dialogs will use to filter
     *            files
     * @param filterName
     *            the name that describes the filterExtension
     * @param dialogTitle
     *            the dialog title
     */
    public ResourceSelectionDialog(Shell parent, String filterExtension, String filterName, String dialogTitle) {
        super(parent);

        this.filterExtension = filterExtension;
        this.filterName = filterName;

        setTitle(dialogTitle);
        setStatusLineAboveButtons(true);
        int shellStyle = getShellStyle();
        setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);
        updateStatus(VALIDATE_SELECTION_STATUS);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);

        Composite buttonComposite = new Composite(composite, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.widthHint = 500;
        buttonComposite.setLayoutData(gridData);

        GridLayout gridLayout = new GridLayout(3, false);
        buttonComposite.setLayout(gridLayout);

        Label messageLabel = createMessageArea(buttonComposite);
        gridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        messageLabel.setLayoutData(gridData);

        Button browseWorkspace = new Button(buttonComposite, SWT.PUSH);
        browseWorkspace.setText(CXFUIMessages.RESOURCE_SELECTIN_DIALOG_BROWSE_WORKSPACE_BUTTON);
        browseWorkspace.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                SelectionDialog dialog = createWorkspaceResourceDialog(composite.getShell());
                if (dialog.open() == SelectionDialog.OK) {
                    Object[] result = dialog.getResult();
                    IResource resource = (IResource) result[0];
                    resourceText.setText(resource.getLocation().toOSString());
                }
            }
        });

        gridData = new GridData(SWT.END, SWT.FILL, false, false);
        browseWorkspace.setLayoutData(gridData);

        Button browseFileSystem = new Button(buttonComposite, SWT.PUSH);
        browseFileSystem.setText(CXFUIMessages.RESOURCE_SELECTIN_DIALOG_BROWSE_FILE_SYSTEM_BUTTON);
        browseFileSystem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                FileDialog dialog = createFileSystemResourceDialog(composite.getShell());
                String result = dialog.open();
                if (result == null) {
                    return;
                }
                IPath filterPath = new Path(dialog.getFilterPath());
                String buildFileName = dialog.getFileName();
                IPath path = filterPath.append(buildFileName).makeAbsolute();

                resourceText.setText(path.toOSString());
            }
        });
        gridData = new GridData(SWT.END, SWT.FILL, false, false);
        browseFileSystem.setLayoutData(gridData);

        resourceText = new Text(buttonComposite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        resourceText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                String resource = resourceText.getText();
                File file = new File(resource);
                if (file.exists()) {
                    setResult(Arrays.asList(resource));
                    updateStatus(OK_FILE_STATUS);
                } else {
                    updateStatus(ERROR_FILE_STATUS);
                }
            }
        });
        gridData.horizontalSpan = 3;
        resourceText.setLayoutData(gridData);

        return composite;
    }

    private SelectionDialog createWorkspaceResourceDialog(Shell shell) {
        ElementTreeSelectionDialog workspaceResourceDialog = new ElementTreeSelectionDialog(shell,
                new WorkbenchLabelProvider(), new WorkbenchContentProvider());

        workspaceResourceDialog.setTitle(getMessage());
        workspaceResourceDialog.setMessage(filterName);
        workspaceResourceDialog.setAllowMultiple(false);

        workspaceResourceDialog.addFilter(new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFile) {
                    IFile file = (IFile) element;
                    return file.isAccessible() && file.getFileExtension().equals(filterExtension);
                }

                if (element instanceof IProject && !((IProject) element).isOpen()) {
                    IProject project = (IProject) element;
                    return project.isAccessible();
                }

                if (element instanceof IContainer) {
                    try {
                        IContainer container = (IContainer) element;
                        IResource[] resources = container.members();
                        for (IResource resource : resources) {
                            if (select(viewer, parentElement, resource)) {
                                return true;
                            }
                        }
                    } catch (CoreException ce) {
                        CXFUIPlugin.log(ce.getStatus());
                    }
                }
                return false;
            }
        });

        workspaceResourceDialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                if (selection.length > 0) {
                    Object selected = selection[0];
                    if (selected instanceof IFile) {
                        IFile file = (IFile) selected;
                        if (file.getFileExtension().equals(filterExtension)) {
                            return OK_FILE_STATUS;
                        }
                    }
                }
                return VALIDATE_SELECTION_STATUS;
            }
        });

        workspaceResourceDialog.setInput(ResourcesPlugin.getWorkspace().getRoot());

        return workspaceResourceDialog;
    }

    private FileDialog createFileSystemResourceDialog(Shell shell) {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*" + filterExtension }); //$NON-NLS-1$;
        dialog.setFilterNames(new String[] { filterName });
        return dialog;
    }

    @Override
    protected void computeResult() {
        // do nothing
    }

}
