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
package org.eclipse.jst.ws.internal.cxf.creation.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.SimpleJavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIMessages;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.jst.ws.internal.cxf.creation.ui.viewers.AnnotationColumnLabelProvider;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;

@SuppressWarnings("restriction")
public class JAXWSAnnotateJavaWidget extends SimpleWidgetDataContributor {
    IStatus status = Status.OK_STATUS;

    private IType type;
    private Java2WSDataModel model;
    private TreeViewer javaTreeViewer;
    private SourceViewer annotationPreviewViewer;

    private TreeViewerColumn webMethodViewerColumn;
    private TreeViewerColumn webParamViewerColumn;
    private TreeViewerColumn requestWrapperViewerColumn;
    private TreeViewerColumn responceWrapperViewerColumn;
    private TreeViewerColumn webResultViewerColumn;

    public JAXWSAnnotateJavaWidget() {
    }

    public void setJava2WSDataModel(Java2WSDataModel model) {
        this.model = model;
    }

    @Override
    public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
        SashForm sashForm = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
        GridLayout gridLayout = new GridLayout(1, true);
        sashForm.setLayout(gridLayout);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        sashForm.setLayoutData(gridData);

        Composite javaTreecomposite = new Composite(sashForm, SWT.NONE);
        gridLayout = new GridLayout(1, true);
        javaTreecomposite.setLayout(gridLayout);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        javaTreecomposite.setLayoutData(gridData);

        Label outlineLabel = new Label(javaTreecomposite, SWT.NONE);
        outlineLabel.setText(CXFCreationUIMessages.JAXWS_ANNOTATE_JJAVA_WIDGET_SELECT_METHOD_TO_ANNOTATE);

        Tree javaTree = new Tree(javaTreecomposite, SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL);
        javaTreeViewer = new TreeViewer(javaTree);

        javaTree.setHeaderVisible(true);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 10;
        gridData.widthHint = 200;
        javaTree.setLayoutData(gridData);

        TreeViewerColumn classViewerColumn = new TreeViewerColumn(javaTreeViewer, SWT.NONE);
        TreeColumn classColumn = classViewerColumn.getColumn();
        classColumn.setWidth(200);
        classColumn.setMoveable(false);

        final JavaElementLabelProvider javaElementLabelProvider = new JavaElementLabelProvider();
        classViewerColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public Image getImage(Object element) {
                return javaElementLabelProvider.getImage(element);
            }

            @Override
            public String getText(Object element) {
                return javaElementLabelProvider.getText(element);
            }
        });

        javaTreeViewer.setContentProvider(new StandardJavaElementContentProvider(true) {
            @Override
            public Object[] getChildren(Object element) {
                if (element instanceof ICompilationUnit) {
                    try {
                        ICompilationUnit compilationUnit = (ICompilationUnit) element;
                        IType[] types = compilationUnit.getTypes();
                        for (IType type : types) {
                            if (type instanceof IType) {
                                return new Object[] {type};
                            }
                        }
                    } catch (JavaModelException jme) {
                        CXFCreationUIPlugin.log(jme.getStatus());
                    }
                }

                if (element instanceof IType) {
                    try {
                        IType sourceType = (IType) element;
                        List<IMethod> publicMethods = new ArrayList<IMethod>();
                        IMethod[] methods = sourceType.getMethods();
                        if (sourceType.isInterface()) {
                            return methods;
                        } else if (sourceType.isClass()) {
                            for (IMethod method : methods) {
                                if (JDTUtils.isPublicMethod(method)) {
                                    publicMethods.add(method);
                                }
                            }
                        }
                        return publicMethods.toArray(new Object[publicMethods.size()]);
                    } catch (JavaModelException jme) {
                        CXFCreationUIPlugin.log(jme.getStatus());
                    }
                }
                return NO_CHILDREN;
            }

            @Override
            public boolean hasChildren(Object element) {
                return getChildren(element).length > 0;
            }
        });

        javaTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection() instanceof ITreeSelection) {
                    ITreeSelection treeSelection = (TreeSelection) event.getSelection();
                    Object firstElement = treeSelection.getFirstElement();

                    IDocument document = annotationPreviewViewer.getDocument();
                    FindReplaceDocumentAdapter findReplaceDocumentAdapter =
                    	new FindReplaceDocumentAdapter(document);
                    try {
                        if (firstElement instanceof IType) {
                            IType sourceType = (IType) firstElement;
                            String elementName = sourceType.getElementName();

                            StringBuilder regex = new StringBuilder("\\bpublic\\W+(?:\\w+\\W+){1,3}?");
                            regex.append(elementName);
                            regex.append("\\b");
                            IRegion region = findReplaceDocumentAdapter.find(0, regex.toString(), true, true,
                                    false, true);

                            annotationPreviewViewer.setSelectedRange(region.getOffset(), region.getLength());
                            annotationPreviewViewer.revealRange(region.getOffset(), region.getLength());
                        } else if (firstElement instanceof IMethod) {
                            IMethod sourceMethod = (IMethod) firstElement;
                            IType sourceType = (IType) sourceMethod.getParent();

                            String elementName = sourceMethod.getElementName();

                            StringBuilder regex = new StringBuilder();

                            if (sourceType.isClass()) {
                                regex.append("\\bpublic");
                            }

                            regex.append("\\W+(?:\\w+\\W+){1,3}?");
                            regex.append(elementName);
                            regex.append("\\s*?\\(\\s*?.*?");

                            String[] parameterTypes = sourceMethod.getParameterTypes();
                            String[] paramterNames = sourceMethod.getParameterNames();

                            for (int i = 0; i < parameterTypes.length; i++) {
                                regex.append("\\s*?");
                                String typeName = Signature.toString(parameterTypes[i]);
                                regex.append(typeName);
                                regex.append("\\s*?");
                                regex.append(paramterNames[i]);
                            	if (i < parameterTypes.length - 1) {
                            	    regex.append("\\s*?,\\s*?.*?");
                            	}
							}
                            regex.append("\\s*?\\)");

                        	IRegion region = findReplaceDocumentAdapter.find(0, regex.toString(), true, true,
                        	        false, true);

                            if (region != null) {
                                IRegion elementNameRegion = findReplaceDocumentAdapter.find(
                                		region.getOffset(), elementName, true, true, true, false);
                                if (elementNameRegion != null) {
                                	annotationPreviewViewer.setSelectedRange(elementNameRegion.getOffset(),
                                    		elementNameRegion.getLength());
                                    annotationPreviewViewer.revealRange(elementNameRegion.getOffset(),
                                    		elementNameRegion.getLength());
                                }
                            }
                        }
                    } catch (BadLocationException ble) {
                        CXFCreationUIPlugin.log(ble);
                    } catch (JavaModelException jme) {
                        CXFCreationUIPlugin.log(jme);
					}
                }
            }
        });

        createWebMethodViewerColumn(javaTreeViewer);
        createWebParamViewerColumn(javaTreeViewer);
        createRequestWrapperViewerColumn(javaTreeViewer);
        createResponseWrapperViewerColumn(javaTreeViewer);
        createWebResultViewerColumn(javaTreeViewer);

        javaTreeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
        javaTreeViewer.expandAll();

        Composite previewComposite = new Composite(sashForm, SWT.NONE);
        gridLayout = new GridLayout(1, true);
        previewComposite.setLayout(gridLayout);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        javaTreecomposite.setLayoutData(gridData);

        Label previewLabel = new Label(previewComposite, SWT.NONE);
        previewLabel.setText(CXFCreationUIMessages.JAXWS_ANNOTATE_JJAVA_WIDGET_PREVIEW);

        annotationPreviewViewer = createAnnotationPreviewer(previewComposite, SWT.READ_ONLY | SWT.V_SCROLL
                | SWT.H_SCROLL | SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 10;
        gridData.widthHint = 200;
        annotationPreviewViewer.getControl().setLayoutData(gridData);

        sashForm.setWeights(new int[]{50, 50});

        return this;
    }

    @Override
    public void internalize() {
        type = null;
        updateLabelProviders();
        javaTreeViewer.setInput(getType().getTypeRoot());
        javaTreeViewer.refresh();
        handleAnnotation(getType());
    }

    private IType getType() {
        if (type != null) {
            return type;
        }
        if (model.getFullyQualifiedJavaInterfaceName() != null) {
            type = JDTUtils.findType(JDTUtils.getJavaProject(model.getProjectName()), model
                    .getFullyQualifiedJavaInterfaceName());
        } else if (model.getFullyQualifiedJavaClassName() != null) {
            type =JDTUtils.findType(JDTUtils.getJavaProject(model.getProjectName()), model
                    .getFullyQualifiedJavaClassName());
        }

        if (type == null) {
            type = JDTUtils.findType(JDTUtils.getJavaProject(model.getProjectName()), model
                    .getJavaStartingPoint());
        }

        model.setMethodMap(CXFModelUtils.getMethodMap(type, model));

        return type;
    }

    private TreeViewerColumn createWebMethodViewerColumn(TreeViewer treeViewer) {
        webMethodViewerColumn = new TreeViewerColumn(treeViewer, SWT.CENTER);
        TreeColumn webMethodColumn = webMethodViewerColumn.getColumn();
        webMethodColumn.setText("@" + CXFModelUtils.WEB_METHOD); //$NON-NLS-1$
        webMethodColumn.setWidth(100);
        webMethodColumn.setAlignment(SWT.CENTER);
        webMethodColumn.setMoveable(false);
        webMethodViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_METHOD, getType()));
        webMethodViewerColumn.setEditingSupport(new AnnotationEditingSupport(treeViewer,
                CXFModelUtils.WEB_METHOD));
        return webMethodViewerColumn;
    }

    private TreeViewerColumn createWebParamViewerColumn(TreeViewer treeViewer) {
        webParamViewerColumn = new TreeViewerColumn(treeViewer, SWT.CENTER);
        TreeColumn webParamColumn = webParamViewerColumn.getColumn();
        webParamColumn.setText("@" + CXFModelUtils.WEB_PARAM); //$NON-NLS-1$
        webParamColumn.setWidth(100);
        webParamColumn.setAlignment(SWT.CENTER);
        webParamColumn.setMoveable(false);
        webParamViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_PARAM, getType()));
        webParamViewerColumn.setEditingSupport(new AnnotationEditingSupport(treeViewer,
                CXFModelUtils.WEB_PARAM));
        return webParamViewerColumn;
    }

    private TreeViewerColumn createRequestWrapperViewerColumn(TreeViewer treeViewer) {
        requestWrapperViewerColumn = new TreeViewerColumn(treeViewer, SWT.CENTER);
        TreeColumn createRequestWrapperColumn = requestWrapperViewerColumn.getColumn();
        createRequestWrapperColumn.setText("@" + CXFModelUtils.REQUEST_WRAPPER); //$NON-NLS-1$
        createRequestWrapperColumn.setWidth(100);
        createRequestWrapperColumn.setMoveable(false);
        requestWrapperViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.REQUEST_WRAPPER, getType()));
        requestWrapperViewerColumn.setEditingSupport(new AnnotationEditingSupport(treeViewer,
                CXFModelUtils.REQUEST_WRAPPER));
        return requestWrapperViewerColumn;
    }

    private TreeViewerColumn createResponseWrapperViewerColumn(TreeViewer treeViewer) {
        responceWrapperViewerColumn = new TreeViewerColumn(treeViewer, SWT.CENTER);
        TreeColumn responceWrapperColumn = responceWrapperViewerColumn.getColumn();
        responceWrapperColumn.setText("@" + CXFModelUtils.RESPONSE_WRAPPER); //$NON-NLS-1$
        responceWrapperColumn.setWidth(100);
        responceWrapperColumn.setMoveable(false);
        responceWrapperViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.RESPONSE_WRAPPER, getType()));
        responceWrapperViewerColumn.setEditingSupport(new AnnotationEditingSupport(treeViewer,
                CXFModelUtils.RESPONSE_WRAPPER));
        return responceWrapperViewerColumn;
    }

    private TreeViewerColumn createWebResultViewerColumn(TreeViewer treeViewer) {
        webResultViewerColumn = new TreeViewerColumn(treeViewer, SWT.CENTER);
        TreeColumn webResultColumn = webResultViewerColumn.getColumn();
        webResultColumn.setText("@" + CXFModelUtils.WEB_RESULT); //$NON-NLS-1$
        webResultColumn.setWidth(100);
        webResultColumn.setMoveable(false);
        webResultViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_RESULT, getType()));
        webResultViewerColumn.setEditingSupport(new AnnotationEditingSupport(treeViewer,
                CXFModelUtils.WEB_RESULT));
        return webResultViewerColumn;
    }

    private void updateLabelProviders() {
        webMethodViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_METHOD, getType()));
        webParamViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_PARAM, getType()));
        requestWrapperViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.REQUEST_WRAPPER, getType()));
        responceWrapperViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.RESPONSE_WRAPPER, getType()));
        webResultViewerColumn.setLabelProvider(new AnnotationColumnLabelProvider(model,
                CXFModelUtils.WEB_RESULT, getType()));
    }

    private void handleAnnotation(IType type) {
        try {
            annotationPreviewViewer.setRedraw(false);

            ICompilationUnit compilationUnit = type.getCompilationUnit();
            IProgressMonitor monitor =  new NullProgressMonitor();

            TextFileChange textFileChange = new TextFileChange("Annotation Changes",
                    (IFile)compilationUnit.getResource());
            MultiTextEdit multiTextEdit = new MultiTextEdit();
            textFileChange.setEdit(multiTextEdit);
            textFileChange.setKeepPreviewEdits(true);

            CXFModelUtils.getImportsChange(compilationUnit, model, textFileChange, false);
            CXFModelUtils.getWebServiceAnnotationChange(type, model, textFileChange);

            IMethod[] typeMethods = JDTUtils.getPublicMethods(type);
            for (int i = 0; i < typeMethods.length; i++) {
                IMethod method = typeMethods[i];
                Map<String, Boolean> methodAnnotationMap = model.getMethodMap().get(method);
                if (methodAnnotationMap.get(CXFModelUtils.WEB_METHOD)) {
                    CXFModelUtils.getWebMethodAnnotationChange(type, method, textFileChange);
                }
                if (methodAnnotationMap.get(CXFModelUtils.REQUEST_WRAPPER)) {
                    CXFModelUtils.getRequestWrapperAnnotationChange(type, method, textFileChange);
                }
                if (methodAnnotationMap.get(CXFModelUtils.RESPONSE_WRAPPER)) {
                    CXFModelUtils.getResponseWrapperAnnotationChange(type, method, textFileChange);
                }
                if (methodAnnotationMap.get(CXFModelUtils.WEB_RESULT)) {
                    CXFModelUtils.getWebResultAnnotationChange(type, method, textFileChange);
                }
                if (methodAnnotationMap.get(CXFModelUtils.WEB_PARAM)) {
					List<SingleVariableDeclaration> parameters = AnnotationUtils.getSingleVariableDeclarations(method);
					for (SingleVariableDeclaration parameter : parameters) {
						CXFModelUtils.getWebParamAnnotationChange(type, method,
								(ILocalVariable) parameter.resolveBinding().getJavaElement(), textFileChange);
					}
                }
            }

            annotationPreviewViewer.getDocument().set(textFileChange.getPreviewContent(monitor));

            annotationPreviewViewer.setRedraw(true);
        } catch (CoreException ce) {
            CXFCreationUIPlugin.log(ce.getStatus());
        } catch (MalformedTreeException mte) {
            CXFCreationUIPlugin.log(mte);
        }
    }

    private SourceViewer createAnnotationPreviewer(Composite parent, int styles) {
        JavaPlugin javaPlugin = JavaPlugin.getDefault();
        IPreferenceStore store = javaPlugin.getCombinedPreferenceStore();
        JavaTextTools javaTextTools = javaPlugin.getJavaTextTools();
        annotationPreviewViewer = new JavaSourceViewer(parent, null, null, true, styles, store);
        IColorManager colorManager = javaTextTools.getColorManager();
        SimpleJavaSourceViewerConfiguration configuration = new SimpleJavaSourceViewerConfiguration(
                colorManager, store, null, IJavaPartitions.JAVA_PARTITIONING, true);

        annotationPreviewViewer.configure(configuration);

        Font font = JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT);
        annotationPreviewViewer.getTextWidget().setFont(font);
        annotationPreviewViewer.setEditable(false);


        String source = getSourceFromType(getType());
        IDocument document = new Document(source);

        JavaPlugin.getDefault().getJavaTextTools().setupJavaDocumentPartitioner(document,
                IJavaPartitions.JAVA_PARTITIONING);
        annotationPreviewViewer.setDocument(document);

        return annotationPreviewViewer;
    }

    public String getSourceFromType(IType type) {
        try {
            return type.getCompilationUnit().getBuffer().getContents();
        } catch (JavaModelException jme) {
            CXFCreationUIPlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public IStatus getStatus() {
        return status;
    }

    private class AnnotationEditingSupport extends EditingSupport {
        private CheckboxCellEditor checkboxCellEditor;

        private String annotationKey;

        public AnnotationEditingSupport(TreeViewer viewer, String annotationKey) {
            super(viewer);
            this.annotationKey = annotationKey;
            checkboxCellEditor = new CheckboxCellEditor(viewer.getTree());
        }

        @Override
        protected boolean canEdit(Object element) {
            if (element instanceof IMethod) {
                IMethod method = (IMethod) element;
                return !AnnotationUtils.isAnnotationPresent(type.findMethods(method)[0],
                        annotationKey);
            }
            return false;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return checkboxCellEditor;
        }

        @Override
        protected Object getValue(Object element) {
            Boolean annotate = model.getMethodMap().get(element).get(annotationKey);
            return annotate;
        }

        @Override
        protected void setValue(Object element, Object value) {
            if (element instanceof IMethod) {
                IMethod method = (IMethod) element;
                Boolean annotate = (Boolean) value;
                Map<String, Boolean> annotationMap = model.getMethodMap().get(element);
                annotationMap.put(annotationKey, annotate);
                handleAnnotation(method.getDeclaringType());
                getViewer().refresh(true);
            }
        }
    }
}
