/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.ui.wizards;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.internal.cxf.core.CXFClasspathContainer;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.context.CXFPersistentContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.core.utils.LaunchUtils;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.osgi.framework.Version;

public class CXFInstallWizardPage extends WizardPage {
    private IStatus CXF_LOCATION_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, null);
    private IStatus CXF_TYPE_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, null);
    private IStatus CXF_VERSION_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, null);
    private IStatus OK_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$

    private Pattern digitPattern = Pattern.compile("\\d"); //$NON-NLS-1$

    private Button browseButton;
    private Text cxfHomeDirText;
    private Text cxfTypeText;
    private Text cxfVersionText;

    String cxfRuntimeVersion = ""; //$NON-NLS-1$
    String cxfRuntimeLocation = ""; //$NON-NLS-1$
    String cxfRuntimeType = ""; //$NON-NLS-1$

    private CXFInstall cxfInstall;

    protected CXFInstallWizardPage() {
        super("cxf.intall.wizard.page"); //$NON-NLS-1$
        setTitle(CXFUIMessages.CXF_INSTALL_WIZARD_PAGE_TITLE);
        setDescription(CXFUIMessages.CXF_INSTALL_WIZARD_PAGE_DESCRIPTION);
        setImageDescriptor(CXFUIPlugin.imageDescriptorFromPlugin(CXFUIPlugin.PLUGIN_ID, "icons/wizban/library_wiz.png"));
    }

    public void createControl(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);

        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);

        Label label = new Label(composite, SWT.NONE);
        label.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_CXF_HOME_DIR_LABEL);

        cxfHomeDirText = new Text(composite, SWT.BORDER);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        cxfHomeDirText.setLayoutData(gridData);

        cxfHomeDirText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateLocationStatus();
            }
        });

        browseButton = new Button(composite, SWT.NONE);
        browseButton.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_BROWSE_BUTTON_LABEL);

        browseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog fileDialog = new DirectoryDialog(composite.getShell());
                String fileName = fileDialog.open();
                if (fileName != null) {
                    cxfHomeDirText.setText(fileName);
                }
            }
        });

        //CXF Version
        Label cxfVersionLabel = new Label(composite, SWT.NONE);
        cxfVersionLabel.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_CXF_VERSION_LABEL);

        cxfVersionText = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        cxfVersionText.setLayoutData(gridData);
        cxfVersionText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateVersionStatus();
            }
        });

        //Blank label
        new Label(composite, SWT.NONE);

        //CXF Type
        Label cxfTypeLabel = new Label(composite, SWT.NONE);
        cxfTypeLabel.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_CXF_TYPE_LABEL);

        cxfTypeText = new Text(composite, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        cxfTypeText.setLayoutData(gridData);
        cxfTypeText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateTypeStatus();
            }
        });

        if (cxfInstall != null) {
            cxfHomeDirText.setText(cxfInstall.getLocation());
            cxfVersionText.setText(cxfInstall.getVersion());
            cxfTypeText.setText(cxfInstall.getType());
        }

        setControl(composite);
        Dialog.applyDialogFont(composite);
    }

    public void setCXFInstall(CXFInstall cxfInstall) {
        this.cxfInstall = cxfInstall;
    }

    private void updateLocationStatus() {
        CXF_LOCATION_STATUS = checkRuntimeExist(cxfHomeDirText.getText());
        applyStatusToPage(findMostSevere());
    }

    private void updateTypeStatus() {
        CXF_TYPE_STATUS = validateTypeName(cxfTypeText.getText());
        applyStatusToPage(findMostSevere());
    }

    private void updateVersionStatus() {
        CXF_VERSION_STATUS = validateVersion(cxfVersionText.getText());
        applyStatusToPage(findMostSevere());
    }

    private void applyStatusToPage(IStatus status) {
        String message = status.getMessage();
        if (status.getSeverity() > IStatus.OK) {
            setErrorMessage(message);
            setPageComplete(false);

        } else {
            setMessage(getDescription());
            setErrorMessage(null);
            setPageComplete(true);
        }
    }

    private IStatus findMostSevere() {
        if (CXF_TYPE_STATUS.getSeverity() > CXF_LOCATION_STATUS.getSeverity()) {
            return CXF_TYPE_STATUS;
        }
        if (CXF_VERSION_STATUS.getSeverity() > CXF_LOCATION_STATUS.getSeverity()) {
            return CXF_VERSION_STATUS;
        }
        if (CXF_TYPE_STATUS.getSeverity() == CXF_LOCATION_STATUS.getSeverity()) {
            return CXF_LOCATION_STATUS;
        }
        if (CXF_VERSION_STATUS.getSeverity() == CXF_LOCATION_STATUS.getSeverity()) {
            return CXF_LOCATION_STATUS;
        }

        return CXF_LOCATION_STATUS;
    }

    private IStatus checkRuntimeExist(String path) {
        File cxfHomeDir = new File(path);
        if (cxfHomeDirText.getText().trim().equals("")) { //$NON-NLS-1$
            CXF_LOCATION_STATUS = new Status(IStatus.ERROR, CXFUIPlugin.PLUGIN_ID,
                    CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_NOT_SET);
        }
        String cxfLibPath = cxfHomeDir + System.getProperty("file.separator") + "lib"; //$NON-NLS-1$ //$NON-NLS-2$
        if (cxfHomeDir.isDirectory()) {
            File cxfLibFolder = new File(cxfLibPath);
            if (cxfLibFolder.isDirectory()) {
                String[] cxfJarFiles = getCXFJarFiles(cxfLibFolder);
                if (cxfJarFiles != null && cxfJarFiles.length > 0) {
                    IStatus toolVersionStatus = getToolVersion(cxfLibPath);
                    processToolVersion(toolVersionStatus);
                    return CXF_LOCATION_STATUS = OK_STATUS;
                }
            }
        }
        cxfTypeText.setText("");
        cxfVersionText.setText("");
        CXF_LOCATION_STATUS = new Status(Status.ERROR, CXFUIPlugin.PLUGIN_ID,
                CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_NOT_SET);
        return CXF_LOCATION_STATUS;
    }

    private IStatus validateTypeName(String typeName) {
        if (typeName.trim().length() == 0) {
            CXF_TYPE_STATUS = new Status(Status.ERROR, CXFUIPlugin.PLUGIN_ID, "Enter Type Name");
        } else {
            CXF_TYPE_STATUS = OK_STATUS;
        }
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IStatus result = workspace.validateName(typeName, IResource.FOLDER);
        if (!result.isOK()) {
            return result;
        }
        //        if (!typeName.matches("[a-zA-Z0-9_\\-\\s]")) {
        //            CXF_TYPE_STATUS = new Status(Status.ERROR, CXFUIPlugin.PLUGIN_ID, "Invalid type name");
        //        } else {
        //            CXF_TYPE_STATUS = OK_STATUS;
        //        }
        return CXF_TYPE_STATUS;
    }

    private IStatus validateVersion(String version) {
        if (CXFCorePlugin.getDefault().getJava2WSContext().getInstallations().containsKey(version)
                && cxfInstall != null && !cxfInstall.getVersion().equals(version)) {
            CXF_VERSION_STATUS = new Status(IStatus.ERROR, CXFUIPlugin.PLUGIN_ID, "Version already installed");
        } else {
            CXF_VERSION_STATUS = OK_STATUS;
        }
        return CXF_VERSION_STATUS;
    }

    private String[] getCXFJarFiles(File directory) {
        String[] cxfJarFiles = directory.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.startsWith("cxf") && name.endsWith(".jar") //$NON-NLS-1$ //$NON-NLS-2$
                        && digitPattern.matcher(name).find()) {
                    return true;
                }
                return false;
            }
        });
        return cxfJarFiles;
    }

    private IStatus getToolVersion(String cxLibFolderPath) {
        File cxfLibFolder = new File(cxLibFolderPath);
        List<String> cxfLib = new ArrayList<String>();
        String[] jarFiles = cxfLibFolder.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(".jar")) { //$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });

        for (String jarFile : jarFiles) {
            cxfLib.add(cxLibFolderPath + System.getProperty("file.separator") + jarFile); //$NON-NLS-1$
        }

        IStatus toolVersionStatus = LaunchUtils.launch(cxfLib.toArray(new String[cxfLib.size()]),
                "org.apache.cxf.tools.wsdlto.WSDLToJava", new String[] { "-v" }); //$NON-NLS-1$ //$NON-NLS-2$
        return toolVersionStatus;
    }

    private void processToolVersion(IStatus toolVersionStatus) {
        if (toolVersionStatus.getSeverity() == IStatus.INFO) {
            String cxfToolVersion = toolVersionStatus.getMessage();

            cxfToolVersion = cxfToolVersion.substring(cxfToolVersion.indexOf("-") + 1, //$NON-NLS-1$
                    cxfToolVersion.length()).trim();

            Matcher matcher = digitPattern.matcher(cxfToolVersion);
            if (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                while (matcher.find()) {
                    end = matcher.end();
                }
                cxfRuntimeType = cxfToolVersion.substring(0, start).trim();
                cxfRuntimeVersion = cxfToolVersion.substring(start, end);
            }

            cxfVersionText.setText(cxfRuntimeVersion);
            cxfTypeText.setText(cxfRuntimeType);
            CXFCorePlugin.getDefault().setCurrentRuntimeVersion(new Version(cxfRuntimeVersion));
        }
    }

    public boolean finish() {
        CXFPersistentContext context = CXFCorePlugin.getDefault().getJava2WSContext();
        Map<String, CXFInstall> installs = context.getInstallations();
        CXFInstall install = CXFFactory.eINSTANCE.createCXFInstall();
        install.setVersion(cxfVersionText.getText());
        install.setLocation(cxfHomeDirText.getText());
        install.setType(cxfTypeText.getText());
        installs.put(cxfRuntimeVersion, install);
        context.setInstallations(installs);
        if (isUpdateRequired(install)) {
            updateProjects(install);
        }
        return true;
    }

    public boolean isUpdateRequired(CXFInstall install) {
        if (cxfInstall == null) {
            return false;
        }
        if (!cxfInstall.getLocation().equals(install.getLocation())
                || !cxfInstall.getType().equals(install.getType())) {
            return true;
        }
        return false;
    }

    public void updateProjects(CXFInstall install) {
        try {
            Set<IFacetedProject> cxfProjects = ProjectFacetsManager.getFacetedProjects(ProjectFacetsManager.getProjectFacet("cxf.core"));
            Iterator<IFacetedProject> projIter = cxfProjects.iterator();
            while (projIter.hasNext()) {
                IFacetedProject cxfProject = projIter.next();
                String installedVersion = CXFCorePlugin.getDefault().getCXFRuntimeVersion(cxfProject.getProject());
                if (installedVersion.equals(install.getVersion())) {
                    ClasspathContainerInitializer classpathContainerInitializer = JavaCore.getClasspathContainerInitializer(
                            CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID);
                    if (classpathContainerInitializer != null) {
                        IPath containerPath = new Path(CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID);
                        IJavaProject javaProject = JavaCore.create(cxfProject.getProject());
                        CXFClasspathContainer cxfClasspathContainer = new CXFClasspathContainer(containerPath, javaProject);
                        classpathContainerInitializer.requestClasspathContainerUpdate(containerPath, javaProject,
                                cxfClasspathContainer);
                    }
                }
            }
        } catch (CoreException ce) {
            CXFUIPlugin.log(ce.getStatus());
        }
    }
}