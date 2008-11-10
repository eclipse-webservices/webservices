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
package org.eclipse.jst.ws.internal.cxf.ui.preferences;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.utils.LaunchUtils;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIMessages;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIPlugin;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.AnnotationsComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.BlankRuntimePreferencesComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.CXF20WSDL2JavaPreferencesComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.CXF21WSDL2JavaPreferencesComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSDLRuntimePreferencesComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.Java2WSRuntimePreferencesComposite;
import org.eclipse.jst.ws.internal.cxf.ui.widgets.SpringConfigComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author sclarke
 */
public class CXFRuntimePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
    private IStatus CXF_LOCATION_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, null);
    private IStatus OK_STATUS = new Status(IStatus.OK, CXFUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$
    
    private Pattern digitPattern = Pattern.compile("\\d"); //$NON-NLS-1$
    
    private Button browseButton;
    private Text cxfHomeDirText;

    private Label cxfToolVersionLabel;

    private CXFContext context;

    private Java2WSDLRuntimePreferencesComposite java2WSDLRuntimePreferencesComposite;
    private Java2WSRuntimePreferencesComposite java2WSRuntimePreferencesComposite ;
    
    private CXF20WSDL2JavaPreferencesComposite cxf20WSDL2JavaPreferencesComposite;
    private CXF21WSDL2JavaPreferencesComposite cxf21WSDL2JavaPreferencesComposite;
    
    private AnnotationsComposite annotationsComposite;
    
    private SpringConfigComposite springConfigComposite;
    
    private StackLayout java2WSStackLayout;
    private StackLayout wsdl2javaStackLayout;
    private StackLayout jaxwsStackLayout;
    private StackLayout springConfigStackLayout;
    
    private Composite java2WSPreferncesGroup;
    private Composite wsdl2JavaPreferencesGroup;
    private Composite jaxwsPreferencesGroup;
    private Composite springConfigPreferncesGroup;
    
    private Composite java2WSDLPreferencesComposite;
    private Composite java2WSPreferencesComposite;
    
    private Composite jaxwsPreferencesComposite;
    
    private Composite wsdl2Java20PreferencesComposite;
    private Composite wsdl2Java21PreferencesComposite;

    private Composite springPreferencesComposite;
    
    private BlankRuntimePreferencesComposite java2WSBlankPreferencesComposite;
    private BlankRuntimePreferencesComposite wsdl2JavaBlankPreferencesComposite;
    private BlankRuntimePreferencesComposite jaxwsBlankPreferencesComposite;
    private BlankRuntimePreferencesComposite springConfigBlankPreferencesComposite;

    public CXFRuntimePreferencePage() {
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    protected Control createContents(Composite parent) {
        context = CXFCorePlugin.getDefault().getJava2WSContext();

        final Composite composite = new Composite(parent, SWT.NONE);

        GridLayout mainLayout = new GridLayout();
        composite.setLayout(mainLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gridData);

        TabFolder cxfPreferenceTab = new TabFolder(composite, SWT.NONE);
        gridData = new GridData(GridData.FILL_BOTH);
        cxfPreferenceTab.setLayoutData(gridData);

        //CXF Runtime Location
        TabItem runtimeInstalLocationItem = new TabItem(cxfPreferenceTab, SWT.NONE);
        runtimeInstalLocationItem.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_HOME_TAB_NAME);
        runtimeInstalLocationItem
                .setToolTipText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_HOME_TAB_TOOLTIP);

        final Composite runtimeGroup = new Composite(cxfPreferenceTab, SWT.NONE);

        runtimeInstalLocationItem.setControl(runtimeGroup);
        runtimeGroup.setToolTipText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_HOME_TAB_TOOLTIP);

        GridLayout runtimeLoactionlayout = new GridLayout();

        runtimeLoactionlayout.numColumns = 3;
        runtimeLoactionlayout.marginHeight = 10;
        runtimeGroup.setLayout(runtimeLoactionlayout);
        gridData = new GridData(GridData.FILL_BOTH);
        runtimeGroup.setLayoutData(gridData);

        Label label = new Label(runtimeGroup, SWT.NONE);
        label.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_CXF_HOME_DIR_LABEL);

        cxfHomeDirText = new Text(runtimeGroup, SWT.BORDER);
        if (context.getCxfRuntimeLocation() != null) {
            cxfHomeDirText.setText(context.getCxfRuntimeLocation());
        }

        gridData = new GridData(GridData.FILL_HORIZONTAL);

        cxfHomeDirText.setLayoutData(gridData);
        cxfHomeDirText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                context.setCxfRuntimeLocation(cxfHomeDirText.getText());
                updateStatus();
                handlePreferenceControls();
            }
        });

        browseButton = new Button(runtimeGroup, SWT.NONE);
        browseButton.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_BROWSE_BUTTON_LABEL);

        browseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog fileDialog = new DirectoryDialog(composite.getShell());
                String fileName = fileDialog.open();
                if (fileName != null) {
                    cxfHomeDirText.setText(fileName);
                    context.setCxfRuntimeLocation(cxfHomeDirText.getText());
                }
            }
        });

        // CXF Version
        Label cxfVersionLabel = new Label(runtimeGroup, SWT.NONE);
        cxfVersionLabel.setText(CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_CXF_VERSON_LABEL);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        cxfVersionLabel.setLayoutData(gridData);

        cxfToolVersionLabel = new Label(runtimeGroup, SWT.NONE);
        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        cxfToolVersionLabel.setLayoutData(gridData);
        if (context.getCxfRuntimeVersion() != null) {
            cxfToolVersionLabel.setText(context.getCxfRuntimeEdition() + " " + context.getCxfRuntimeVersion()); //$NON-NLS-1$
        }
         
        //Java2WS
        TabItem java2WSTabItem = new TabItem(cxfPreferenceTab, SWT.NONE);
        java2WSTabItem.setText(CXFUIMessages.JAVA2WS_PREFERENCES_TAB_NAME);
        java2WSTabItem.setToolTipText(CXFUIMessages.JAVA2WS_PREFERENCES_TAB_TOOLTIP);

        java2WSPreferncesGroup = new Composite(cxfPreferenceTab, SWT.NONE);

        java2WSStackLayout = new StackLayout();
        java2WSPreferncesGroup.setLayout(java2WSStackLayout);

        java2WSDLPreferencesComposite = new Composite(java2WSPreferncesGroup, SWT.NONE);
        GridLayout java2WSGridLayout = new GridLayout(1, true);
        java2WSDLPreferencesComposite.setLayout(java2WSGridLayout);

        java2WSDLRuntimePreferencesComposite = new Java2WSDLRuntimePreferencesComposite(
                java2WSDLPreferencesComposite, SWT.NONE, cxfPreferenceTab);
        java2WSDLRuntimePreferencesComposite.addControls();

        java2WSPreferencesComposite = new Composite(java2WSPreferncesGroup, SWT.NONE);
        GridLayout java2WSDLGridLayout = new GridLayout(1, true);
        java2WSPreferencesComposite.setLayout(java2WSDLGridLayout);

        java2WSRuntimePreferencesComposite = new Java2WSRuntimePreferencesComposite(
                java2WSPreferencesComposite, SWT.NONE, cxfPreferenceTab);
        java2WSRuntimePreferencesComposite.addControls();

        java2WSBlankPreferencesComposite = new BlankRuntimePreferencesComposite(java2WSPreferncesGroup,
                SWT.NONE);

        java2WSTabItem.setControl(java2WSPreferncesGroup);
        
        //WSDL2Java
        TabItem wsdl2JavaTabItem = new TabItem(cxfPreferenceTab, SWT.NONE);
        wsdl2JavaTabItem.setText(CXFUIMessages.WSDL2JAVA_PREFERENCES_TAB_NAME);
        wsdl2JavaTabItem.setToolTipText(CXFUIMessages.WSDL2JAVA_PREFERENCES_TAB_TOOLTIP);

        wsdl2JavaPreferencesGroup = new Composite(cxfPreferenceTab, SWT.NONE);
        
        wsdl2javaStackLayout = new StackLayout();
        wsdl2JavaPreferencesGroup.setLayout(wsdl2javaStackLayout);

        wsdl2Java20PreferencesComposite = new Composite(wsdl2JavaPreferencesGroup, SWT.NONE);
        GridLayout wsdl2Java20GridLayout = new GridLayout(1, true);
        wsdl2Java20PreferencesComposite.setLayout(wsdl2Java20GridLayout);

        cxf20WSDL2JavaPreferencesComposite = new CXF20WSDL2JavaPreferencesComposite(
                wsdl2Java20PreferencesComposite, SWT.NONE);
        cxf20WSDL2JavaPreferencesComposite.addControls();

        wsdl2Java21PreferencesComposite = new Composite(wsdl2JavaPreferencesGroup, SWT.NONE);
        GridLayout wsdl2Java21GridLayout = new GridLayout(1, true);
        wsdl2Java21PreferencesComposite.setLayout(wsdl2Java21GridLayout);

        cxf21WSDL2JavaPreferencesComposite = new CXF21WSDL2JavaPreferencesComposite(
                wsdl2Java21PreferencesComposite, SWT.NONE);
        cxf21WSDL2JavaPreferencesComposite.addControls();

        wsdl2JavaBlankPreferencesComposite = new BlankRuntimePreferencesComposite(wsdl2JavaPreferencesGroup,
                SWT.NONE);

        wsdl2JavaTabItem.setControl(wsdl2JavaPreferencesGroup);
        
        //JAX-WS
        TabItem annotationsTabItem = new TabItem(cxfPreferenceTab, SWT.NONE);
        annotationsTabItem.setText(CXFUIMessages.ANNOTATIONS_PREFERENCES_TAB_NAME);
        annotationsTabItem.setToolTipText(CXFUIMessages.ANNOTATIONS_PREFERENCES_TAB_TOOLTIP);
        
        jaxwsPreferencesGroup = new Composite(cxfPreferenceTab, SWT.NONE);
        
        jaxwsStackLayout = new StackLayout();
        jaxwsPreferencesGroup.setLayout(jaxwsStackLayout);
        
        jaxwsPreferencesComposite = new Composite(jaxwsPreferencesGroup, SWT.NONE);
        GridLayout jaxwsGridLayout = new GridLayout(1, true);
        jaxwsPreferencesComposite.setLayout(jaxwsGridLayout);
        annotationsComposite = new AnnotationsComposite(jaxwsPreferencesComposite,  SWT.SHADOW_IN);
        
        jaxwsBlankPreferencesComposite = new BlankRuntimePreferencesComposite(jaxwsPreferencesGroup,
                SWT.NONE);
        
        annotationsTabItem.setControl(jaxwsPreferencesGroup);
        
        //Spring Config
        TabItem springConfigTabItem = new TabItem(cxfPreferenceTab, SWT.NONE);
        springConfigTabItem.setText(CXFUIMessages.SPRING_CONFIG_PREFERENCES_TAB_NAME);
        springConfigTabItem.setToolTipText(CXFUIMessages.SPRING_CONFIG_PREFERENCES_TAB_TOOLTIP);
        
        springConfigPreferncesGroup = new Composite(cxfPreferenceTab, SWT.NONE);
        
        springConfigStackLayout = new StackLayout();
        springConfigPreferncesGroup.setLayout(springConfigStackLayout);
        
        springPreferencesComposite = new Composite(springConfigPreferncesGroup, SWT.NONE);
        GridLayout springGridLayout = new GridLayout(1, true);
        springPreferencesComposite.setLayout(springGridLayout);
        springConfigComposite = new SpringConfigComposite(springPreferencesComposite, SWT.SHADOW_IN);
        
        springConfigBlankPreferencesComposite = new BlankRuntimePreferencesComposite(springConfigPreferncesGroup,
                SWT.NONE);

        springConfigTabItem.setControl(springConfigPreferncesGroup);

        handlePreferenceControls();
        
        return composite;
    }

    private void handlePreferenceControls() {
        if (context.getCxfRuntimeLocation().equals("") || context.getCxfRuntimeVersion().equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            java2WSStackLayout.topControl = java2WSBlankPreferencesComposite;
            wsdl2javaStackLayout.topControl = wsdl2JavaBlankPreferencesComposite;
            jaxwsStackLayout.topControl = jaxwsBlankPreferencesComposite;
            springConfigStackLayout.topControl = springConfigBlankPreferencesComposite;
        } else if (context.getCxfRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
            java2WSStackLayout.topControl = java2WSPreferencesComposite;
            wsdl2javaStackLayout.topControl = wsdl2Java21PreferencesComposite;
            jaxwsStackLayout.topControl = jaxwsPreferencesComposite;
            springConfigStackLayout.topControl = springPreferencesComposite;
        } else {
            java2WSStackLayout.topControl = java2WSDLPreferencesComposite;
            wsdl2javaStackLayout.topControl = wsdl2Java20PreferencesComposite;
            jaxwsStackLayout.topControl = jaxwsPreferencesComposite;
            springConfigStackLayout.topControl = springPreferencesComposite;
        }
        java2WSPreferncesGroup.layout();
        wsdl2JavaPreferencesGroup.layout();
        jaxwsPreferencesGroup.layout();
        springConfigPreferncesGroup.layout();
    }

    private IStatus checkRuntimeExist(String path) {
        File cxfHomeDir = new File(path);
        if (cxfHomeDirText.getText().equals("")) { //$NON-NLS-1$
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
                    CXF_LOCATION_STATUS = OK_STATUS;
                    return CXF_LOCATION_STATUS;
                }
            }
        }
        context.setCxfRuntimeLocation(""); //$NON-NLS-1$
        context.setCxfRuntimeVersion(""); //$NON-NLS-1$
        cxfToolVersionLabel.setText(""); //$NON-NLS-1$
        CXF_LOCATION_STATUS = new Status(Status.ERROR, CXFUIPlugin.PLUGIN_ID,
                CXFUIMessages.CXF_RUNTIME_PREFERENCE_PAGE_RUNTIME_NOT_SET);
        return CXF_LOCATION_STATUS;
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

    private void processToolVersion(IStatus toolVersionStatus) {
        if (toolVersionStatus.getSeverity() == IStatus.INFO) {
            String cxfToolVersion = toolVersionStatus.getMessage();

            cxfToolVersion = cxfToolVersion.substring(cxfToolVersion.indexOf("-") + 1, //$NON-NLS-1$
                    cxfToolVersion.length()).trim();

            String cxfRuntimeEdition = ""; //$NON-NLS-1$
            String cxfRuntimeVersion = ""; //$NON-NLS-1$

            Matcher matcher = digitPattern.matcher(cxfToolVersion);
            if (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                while (matcher.find()) {
                    end = matcher.end();
                }
                cxfRuntimeEdition = cxfToolVersion.substring(0, start).trim();
                cxfRuntimeVersion = cxfToolVersion.substring(start, end);
            }
            
            cxfToolVersionLabel.setText(cxfRuntimeEdition + " " + cxfRuntimeVersion); //$NON-NLS-1$
            context.setCxfRuntimeVersion(cxfRuntimeVersion);
            context.setCxfRuntimeEdition(cxfRuntimeEdition);
        }
    }

    private void updateStatus() {
        CXF_LOCATION_STATUS = checkRuntimeExist(cxfHomeDirText.getText());
        applyStatusToPage(findMostSevere());
    }

    private void applyStatusToPage(IStatus status) {
        String message = status.getMessage();
        if (status.getSeverity() > IStatus.OK) {
            setErrorMessage(message);
        } else {
            setMessage(getTitle());
            setErrorMessage(null);
        }

    }

    private IStatus findMostSevere() {
        return CXF_LOCATION_STATUS;
    }

    private void setDefaults() {
        java2WSDLRuntimePreferencesComposite.setDefaults();
        java2WSRuntimePreferencesComposite.setDefaults();
        cxf20WSDL2JavaPreferencesComposite.setDefaults();
        cxf21WSDL2JavaPreferencesComposite.setDefaults();
        annotationsComposite.setDefaults();
        springConfigComposite.setDefaults();
    }
    
    @Override
    protected void performApply() {
        super.performApply();
    }

    @Override
    public boolean performCancel() {
        return super.performCancel();
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        setDefaults();
    }

    @Override
    public boolean performOk() {
        return super.performOk();
    }
}
