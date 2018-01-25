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
package org.eclipse.jst.ws.internal.jaxws.ui.wizards;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSHandlerUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.jst.ws.internal.jaxws.ui.filters.NewHandlerChainViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class OrderHandlerChainPage extends WizardPage {
    private static final String LOGICAL_HANDLER = "javax.xml.ws.handler.LogicalHandler"; //$NON-NLS-1$
    private static final String SOAP_HANDLER = "javax.xml.ws.handler.soap.SOAPHandler"; //$NON-NLS-1$

    private IStatus ok_status = new Status(IStatus.OK, JAXWSUIPlugin.PLUGIN_ID, "");  //$NON-NLS-1$

    private TreeViewer treeViewer;

    private Document doc;
    private Element root;

    private IPath handlerChainPath;
    private IJavaProject javaProject;

    private boolean configure;

    private String newHandlerClassName;
    private String newHandlerType;

    public OrderHandlerChainPage() {
        super("order.handlerchain.wizard.page"); //$NON-NLS-1$
        setTitle(JAXWSUIMessages.JAXWS_ORDER_HANDLER_WIZARD_PAGE_TITLE);
        setDescription(JAXWSUIMessages.JAXWS_ORDER_HANDLER_WIZARD_PAGE_DESCRIPTION);
    }

    public OrderHandlerChainPage(IPath handlerChainPath, IJavaProject javaProject) {
        this();
        this.handlerChainPath = handlerChainPath;
        this.javaProject = javaProject;
        this.configure = true;
    }

    public Document getDocument() {
        return doc;
    }

    public void refreshViewer() {
        treeViewer.refresh();
    }

    public void setSelection(ISelection selection) {
        treeViewer.setSelection(selection, true);
    }

    public void createControl(Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);

        treeViewer = new TreeViewer(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        treeViewer.getTree().setHeaderVisible(true);
        treeViewer.getTree().setLinesVisible(true);
        treeViewer.setContentProvider(new HandlerChainContentProvider());

        TreeViewerColumn nameViewerColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
        TreeColumn nameColumn = nameViewerColumn.getColumn();
        nameColumn.setWidth(200);
        nameColumn.setMoveable(false);
        nameColumn.setText(JAXWSUIMessages.JAXWS_HANDLER_NAME_COLUMN);

        nameViewerColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof Element) {
                    Element node = (Element) element;
                    if (node.getName().equals(JAXWSHandlerUtils.HANDLER)) {
                        Element handlerNameElement = node.getChild(JAXWSHandlerUtils.HANDLER_NAME, JAXWSHandlerUtils.JAVAEE_NS);
                        if (handlerNameElement != null) {
                            return handlerNameElement.getText().trim();
                        }
                    }
                    if (node.getName().equals(JAXWSHandlerUtils.HANDLER_CHAIN)) {
                        return node.getName();
                    }
                }
                return null;
            }

            @Override
            public Image getImage(Object element) {
                if (element instanceof Element) {
                    return PlatformUI.getWorkbench().getSharedImages().getImage(
                            org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
                }
                return null;
            }
        });

        TreeViewerColumn classViewerColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
        TreeColumn classColumn = classViewerColumn.getColumn();
        classColumn.setWidth(200);
        classColumn.setMoveable(false);
        classColumn.setText(JAXWSUIMessages.JAXWS_HANDLER_CLASS_COLUMN);

        classViewerColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof Element) {
                    Element node = (Element) element;
                    if (node.getName().equals(JAXWSHandlerUtils.HANDLER)) {
                        Element handlerNameElement = node.getChild(JAXWSHandlerUtils.HANDLER_CLASS, JAXWSHandlerUtils.JAVAEE_NS);
                        if (handlerNameElement != null) {
                            return handlerNameElement.getText().trim();
                        }
                    }
                }
                return null;
            }
        });

        TreeViewerColumn typeViewerColumn = new TreeViewerColumn(treeViewer, SWT.LEFT);
        TreeColumn typeColumn = typeViewerColumn.getColumn();
        typeColumn.setWidth(100);
        typeColumn.setMoveable(false);
        typeColumn.setText(JAXWSUIMessages.JAXWS_HANDLER_TYPE_COLUMN);

        typeViewerColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                if (element instanceof Element) {
                    Element node = (Element) element;
                    if (node.getName().equals(JAXWSHandlerUtils.HANDLER)) {
                        Element handlerClassElement = node.getChild(JAXWSHandlerUtils.HANDLER_CLASS, JAXWSHandlerUtils.JAVAEE_NS);
                        if (handlerClassElement != null) {
                            if (newHandlerClassName != null && newHandlerClassName.equals(handlerClassElement.getValue().trim())) {
                                return newHandlerType;
                            }
                            try {
                                IType handler = javaProject.findType(handlerClassElement.getText().trim());
                                if (handler != null) {
                                    ITypeHierarchy typeHierarchy = handler.newTypeHierarchy(javaProject, null);
                                    IType[] allInterfaces = typeHierarchy.getAllInterfaces();
                                    for (IType aInterface : allInterfaces) {
                                        if (aInterface.getFullyQualifiedName().equals(LOGICAL_HANDLER)) {
                                            return JAXWSUIMessages.JAXWS_LOGICAL;
                                        }
                                        if (aInterface.getFullyQualifiedName().equals(SOAP_HANDLER)) {
                                            return JAXWSUIMessages.JAXWS_PROTOCOL;
                                        }
                                    }
                                }
                            } catch (JavaModelException jme) {
                                JAXWSUIPlugin.log(jme.getStatus());
                            }
                        }
                    }
                }
                return null;
            }
        });

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.verticalSpan = 6;
        treeViewer.getTree().setLayoutData(gridData);

        if (configure) {
            Button addButton = new Button(composite, SWT.PUSH);
            addButton.setText(JAXWSUIMessages.JAXWS_HANDLER_ADD);
            gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            addButton.setLayoutData(gridData);
            addButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(),
                            new JavaElementLabelProvider(), new StandardJavaElementContentProvider());
                    dialog.setTitle(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_HANDLER_DIALOG_TITLE);
                    dialog.setMessage(JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_HANDLER_DIALOG_DESCRIPTION);
                    dialog.setAllowMultiple(false);
                    dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));

                    dialog.addFilter(new NewHandlerChainViewerFilter(javaProject, true, false));
                    dialog.setValidator(new ISelectionStatusValidator() {

                        public IStatus validate(Object[] selection) {
                            if (selection.length == 1) {
                                if (selection[0] instanceof ICompilationUnit) {
                                    ICompilationUnit compilationUnit = (ICompilationUnit) selection[0];
                                    if (isHandler(compilationUnit)) {
                                        return ok_status;
                                    } else {
                                        return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID,
                                                JAXWSUIMessages.JAXWS_HANDLER_CONFIGURATION_HANDLER_DIALOG_INVALID);
                                    }
                                }
                            }
                            return new Status(IStatus.ERROR, JAXWSUIPlugin.PLUGIN_ID, ""); //$NON-NLS-1$
                        }
                    });

                    if (dialog.open() == Window.OK) {
                        ICompilationUnit selectedHandler = (ICompilationUnit) dialog.getFirstResult();
                        addHandler(selectedHandler.findPrimaryType().getElementName(),
                                selectedHandler.findPrimaryType().getPackageFragment().getElementName());
                    }

                }
            });

            Button removeButton = new Button(composite, SWT.PUSH);
            removeButton.setText(JAXWSUIMessages.JAXWS_HANDLER_REMOVE);
            gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            removeButton.setLayoutData(gridData);
            removeButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                    if (!selection.isEmpty()) {
                        Element selected = (Element) selection.getFirstElement();
                        if (selected.getName().equals(JAXWSHandlerUtils.HANDLER)) {
                            Element handlerChain = (Element) selected.getParent();
                            if (handlerChain != null) {
                                handlerChain.removeContent(selected);
                                treeViewer.refresh();
                            }
                        }
                    }
                }
            });
        }

        Button moveUpButton = new Button(composite, SWT.PUSH);
        moveUpButton.setText(JAXWSUIMessages.JAXWS_HANDLER_MOVE_UP);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        moveUpButton.setLayoutData(gridData);
        moveUpButton.addSelectionListener(new SelectionAdapter() {

            @Override
            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                if (!selection.isEmpty()) {
                    Element selected = (Element) selection.getFirstElement();
                    Element parent = (Element) selected.getParent();
                    List children = parent.getChildren();
                    int index = children.indexOf(selected);
                    if (index > 0) {
                        children.remove(selected);
                        children.add(index - 1, selected);
                    } else if (index == 0) {
                        Element root = (Element) parent.getParent();
                        List handlerChains = root.getChildren();
                        int topIndex = handlerChains.indexOf(parent);
                        if (topIndex > 0 && handlerChains.get(topIndex - 1) != null) {
                            children.remove(selected);
                            Element topChild = (Element) handlerChains.get(topIndex - 1);
                            topChild.getChildren().add(topChild.getChildren().size(), selected);
                        }
                    }
                    treeViewer.refresh();
                }
            }
        });

        Button moveDownButton = new Button(composite, SWT.PUSH);
        moveDownButton.setText(JAXWSUIMessages.JAXWS_HANDLER_MOVE_DOWN);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        moveDownButton.setLayoutData(gridData);
        moveDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            @SuppressWarnings("unchecked")
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                if (!selection.isEmpty()) {
                    Element selected = (Element) selection.getFirstElement();
                    Element parent = (Element) selected.getParent();
                    List children = parent.getChildren();
                    int index = children.indexOf(selected);
                    if (index >= 0 && index < children.size() - 1) {
                        children.remove(selected);
                        children.add(index + 1, selected);
                    } else if (index == children.size() - 1) {
                        Element root = (Element) parent.getParent();
                        List handlerChains = root.getChildren();
                        int hIndex = handlerChains.indexOf(parent);
                        if (hIndex >= 0 && hIndex < handlerChains.size() - 1 && handlerChains.get(hIndex + 1) != null) {
                            children.remove(selected);
                            Element topChild = (Element) handlerChains.get(hIndex + 1);
                            topChild.getChildren().add(0, selected);
                        }
                    }
                    treeViewer.refresh();
                }
            }
        });

        if (handlerChainPath != null) {
            setInput(handlerChainPath);
        }

        treeViewer.expandAll();

        composite.pack();
        setControl(composite);
        Dialog.applyDialogFont(composite);
    }


    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setInput(handlerChainPath);
        }
    }

    public void setHandlerChainPath(IPath handlerChainPath) {
        this.handlerChainPath = handlerChainPath;
    }

    private boolean isHandler(ICompilationUnit source) {
        final List<String> interfaces = new ArrayList<String>();
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source);
        parser.setResolveBindings(true);
        CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(TypeDeclaration typeDeclaration) {
                @SuppressWarnings("unchecked")
                List superInterfaces = typeDeclaration.superInterfaceTypes();
                for (Object object : superInterfaces) {
                    if (object instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) object;
                        ITypeBinding binding = parameterizedType.resolveBinding();
                        if (binding != null) {
                            String qualifiedName = binding.getErasure().getQualifiedName();
                            if (qualifiedName.equals(LOGICAL_HANDLER) || qualifiedName.equals(SOAP_HANDLER)) {
                                interfaces.add(qualifiedName);
                                break;
                            }
                        }
                    }
                }
                return false;
            }
        });
        return interfaces.size() > 0;
    }

    public void setInput(IPath handlerChainPath) {
        if (handlerChainPath == null) {
            return;
        }
        if (!handlerChainPath.isEmpty()) {
            try {
                IFile handlerChainFile = ResourcesPlugin.getWorkspace().getRoot().getFile(handlerChainPath);
                if (handlerChainFile.getFileExtension().equals("xml")) { //$NON-NLS-1$
                    if (handlerChainFile.exists() && JAXWSHandlerUtils.isHandlerChainFile(handlerChainFile)) {
                        FileInputStream handlerInputStream = new FileInputStream(handlerChainFile.getLocation().toFile());
                        SAXBuilder builder = new SAXBuilder();
                        doc = builder.build(handlerInputStream);
                        root = doc.getRootElement();
                        treeViewer.setInput(root);
                        treeViewer.expandAll();
                    } else {
                        JAXWSHandlerUtils.createHandlerChainFile(handlerChainPath);
                        setInput(handlerChainPath);
                    }
                }
            } catch (IOException ioe) {
                JAXWSUIPlugin.log(ioe);
            } catch (JDOMException jdome) {
                JAXWSUIPlugin.log(jdome);
            }
        }
    }

    public void setJavaProject(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }

    public void addHandler(String newHandlerName, String packageName) {
        if (root != null) {
            Element handlerChainElement = root.getChild(JAXWSHandlerUtils.HANDLER_CHAIN, JAXWSHandlerUtils.JAVAEE_NS);
            if (handlerChainElement == null) {
                handlerChainElement = new Element(JAXWSHandlerUtils.HANDLER_CHAIN, JAXWSHandlerUtils.JAVAEE_NS);
                root.addContent(handlerChainElement);
            }
            Element handlerElement = new Element(JAXWSHandlerUtils.HANDLER, JAXWSHandlerUtils.JAVAEE_NS);

            Element handlerNameElement = new Element(JAXWSHandlerUtils.HANDLER_NAME, JAXWSHandlerUtils.JAVAEE_NS);
            handlerNameElement.setText(newHandlerName);

            Element handlerClassElement = new Element(JAXWSHandlerUtils.HANDLER_CLASS, JAXWSHandlerUtils.JAVAEE_NS);
            if (packageName.trim().length() > 0) {
                handlerClassElement.setText(packageName + "." + newHandlerName);
            } else {
                handlerClassElement.setText(newHandlerName);
            }

            handlerElement.addContent(handlerNameElement);
            handlerElement.addContent(handlerClassElement);
            handlerChainElement.addContent(handlerElement);
            treeViewer.refresh();
            treeViewer.setSelection(new StructuredSelection(handlerElement));
        }
    }

    public void addHandler(String newHandlerName, String packageName, String newHandlerType) {
        addHandler(newHandlerName, packageName);
        this.newHandlerClassName = packageName + "." + newHandlerName;
        this.newHandlerType = newHandlerType;
        if (newHandlerType.equals(JAXWSUIMessages.JAXWS_LOGICAL_HANDLER)) {
            this.newHandlerType = JAXWSUIMessages.JAXWS_LOGICAL;
        } else if (newHandlerType.equals(JAXWSUIMessages.JAXWS_SOAP_HANDLER)) {
            this.newHandlerType = JAXWSUIMessages.JAXWS_PROTOCOL;
        }
    }

    private class HandlerChainContentProvider implements ITreeContentProvider {

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Element) {
                Element element = (Element) parentElement;
                return element.getChildren().toArray();
            }
            return new Object[] {};
        }

        public Object getParent(Object element) {
            if (element instanceof Element) {
                Element node = (Element) element;
                return node.getParent();
            }
            return null;
        }

        public boolean hasChildren(Object element) {
            if (element instanceof Element) {
                Element node = (Element) element;
                if (node.getName().equals(JAXWSHandlerUtils.HANDLER_CHAIN)) {
                    return node.getChildren().size() > 0;
                }
            }
            return false;
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof Element) {
                Element element = (Element) inputElement;
                return element.getChildren().toArray();
            }
            return new Object[] {};
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

}
