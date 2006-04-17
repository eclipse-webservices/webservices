/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.common.ui.internal.UIPlugin;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.binding.http.internal.generator.HTTPContentGenerator;
import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.binding.soap.internal.generator.SOAPContentGenerator;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.ui.internal.InternalWSDLMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNodeList;
import org.eclipse.wst.xml.core.internal.contentmodel.internal.util.DOMValidator;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMVisitor;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.w3c.dom.Element;

public class NewWSDLWizard extends Wizard implements INewWizard {
	private WSDLNewFilePage newFilePage;
	private WSDLNewFileOptionsPage optionsPage;
	private IStructuredSelection selection;

	/**
	 * Constructor for NewWSDLWizard.
	 */
	public NewWSDLWizard() {
		super();
	}

	/**
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		IFile file = newFilePage.createNewFile();

		Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
		String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
		if (charSet == null || charSet.trim().equals("")) { //$NON-NLS-1$
			charSet = "UTF-8"; //$NON-NLS-1$
		}

		String wsdlPrefix = "wsdl"; //$NON-NLS-1$
		Vector namespaces = optionsPage.getNamespaceInfo();

		String prefix = optionsPage.getPrefix();
		String definitionName = optionsPage.getDefinitionName();

		URI uri2 = URI.createPlatformResourceURI(file.getFullPath().toOSString());
		ResourceSet resourceSet = new ResourceSetImpl();
		WSDLResourceImpl resource = (WSDLResourceImpl) resourceSet.createResource(URI.createURI("*.wsdl")); //$NON-NLS-1$
		resource.setURI(uri2);
		
		WSDLFactoryImpl factory = new WSDLFactoryImpl();
		DefinitionImpl definition = (DefinitionImpl) factory.createDefinition();
		resource.getContents().add(definition);
		
		definition.setTargetNamespace(optionsPage.getTargetNamespace());
		definition.setLocation(file.getLocation().toString());
		definition.setEncoding(charSet);
		definition.setQName(new QName(wsdlPrefix, definitionName));
		definition.addNamespace(prefix, optionsPage.getTargetNamespace());

		for (int i = 0; i < namespaces.size(); i++) {
			NamespaceInfo info = (NamespaceInfo) namespaces.get(i);

			if (info.prefix.length() > 0) {
				definition.addNamespace(info.prefix, info.uri);
			}
			else {
				definition.addNamespace(null, info.uri);
			}
		}
		// TODO : cs... why do we need this?  these calls are evil!
		definition.updateElement(true);
		try {
			if (optionsPage.getCreateSkeletonBoolean()) {
				if (optionsPage.isSoapDocLiteralProtocol()) {
					CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.PART_INFO_ELEMENT_DECLARATION;
				}
				else {
					CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.PART_INFO_TYPE_DEFINITION;
				}

				CreateWSDLElementHelper.serviceName = definitionName;
				CreateWSDLElementHelper.portName = definitionName + optionsPage.getProtocol();
				Service service = CreateWSDLElementHelper.createService(definition);


				// Generate Binding
				Iterator bindingIt = definition.getEBindings().iterator();
				Binding binding = null;
				if (bindingIt.hasNext()) {
					binding = (Binding) bindingIt.next();
				}
				BindingGenerator bindingGenerator = new BindingGenerator(definition, binding, SOAPConstants.SOAP_NAMESPACE_URI);
				Port port = (Port) service.getEPorts().iterator().next();
				bindingGenerator.setName(ComponentReferenceUtil.getName(port.getEBinding()));
				bindingGenerator.setRefName(ComponentReferenceUtil.getPortTypeReference(port.getEBinding()));
				bindingGenerator.setOverwrite(true);
				
				if (optionsPage.getProtocol().equals("SOAP")) { //$NON-NLS-1$
					String namespace = SOAPConstants.SOAP_NAMESPACE_URI;
					bindingGenerator.setContentGenerator(BindingGenerator.getContentGenerator(namespace));

					SOAPContentGenerator soapGen = (SOAPContentGenerator) bindingGenerator.getContentGenerator();
					Boolean booleanValue = (Boolean) optionsPage.getProtocolOptions()[0];
					Boolean booleanValue2 = (Boolean) optionsPage.getProtocolOptions()[2];
					if (booleanValue.booleanValue()) {
						// Document Literal
						soapGen.setStyle(SOAPContentGenerator.STYLE_DOCUMENT);
						soapGen.setUse(SOAPContentGenerator.USE_LITERAL);
					}
					else if (booleanValue2.booleanValue()){
						// RPC Literal
						soapGen.setStyle(SOAPContentGenerator.STYLE_RPC);
						soapGen.setUse(SOAPContentGenerator.USE_LITERAL);
					}
					else {
						// RPC Encoded
						soapGen.setStyle(SOAPContentGenerator.STYLE_RPC);
						soapGen.setUse(SOAPContentGenerator.USE_ENCODED);
					}
				}
				else if (optionsPage.getProtocol().equals("HTTP")) { //$NON-NLS-1$
					String namespace = HTTPConstants.HTTP_NAMESPACE_URI;
					bindingGenerator.setContentGenerator(BindingGenerator.getContentGenerator(namespace));

					Boolean booleanValue = (Boolean) optionsPage.getProtocolOptions()[0];
					if (booleanValue.booleanValue()) {
						// Post
						((HTTPContentGenerator) bindingGenerator.getContentGenerator()).setVerb(HTTPContentGenerator.VERB_POST);
					}
					else {
						// Get
						((HTTPContentGenerator) bindingGenerator.getContentGenerator()).setVerb(HTTPContentGenerator.VERB_GET);
					}
				}
				
				bindingGenerator.generateBinding();
				bindingGenerator.generatePortContent();				
			}
			resource.save(null);
		}
		catch (Exception e) {
			System.out.println("\nCould not write new WSDL file in WSDL Wizard: " + e); //$NON-NLS-1$
		}

		/*
		 * if (file != null) { final ISelection selection = new
		 * StructuredSelection(file); if (selection != null) { IWorkbench
		 * workbench = UIPlugin.getDefault().getWorkbench(); final
		 * IWorkbenchWindow workbenchWindow =
		 * workbench.getActiveWorkbenchWindow(); final IWorkbenchPart
		 * focusPart = workbenchWindow.getActivePage().getActivePart(); if
		 * (focusPart instanceof ISetSelectionTarget) {
		 * Display.getCurrent().asyncExec (new Runnable() { public void run() {
		 * ((ISetSelectionTarget)focusPart).selectReveal(selection); } }); } } }
		 */
		openEditor(file);

		return true;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(IWorkbench,
	 *      IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;

		// Need new icon
		this.setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(InternalWSDLMultiPageEditor.class, "icons/new_wsdl_wiz.gif")); //$NON-NLS-1$
		this.setWindowTitle(Messages.getString("_UI_TITLE_NEW_WSDL_FILE")); //$NON-NLS-1$
	}

	public void addPages() {
		newFilePage = new WSDLNewFilePage(selection);
		optionsPage = new WSDLNewFileOptionsPage(Messages.getString("_UI_TITLE_OPTIONS"), Messages.getString("_UI_TITLE_OPTIONS"), null); //$NON-NLS-1$ //$NON-NLS-2$
		addPage(newFilePage);
		addPage(optionsPage);
	}

	public IPath getNewFilePath() {
		String fileName = newFilePage.getFileName();
		return fileName != null ? new Path(fileName) : null;
	}

	public boolean canFinish() {
		if (newFilePage.isPageComplete() && optionsPage.isPageComplete()) {
			return true;
		}
		return false;
	}

	static public void openEditor(final IFile iFile) {
		if (iFile != null) {
			IWorkbench workbench = UIPlugin.getDefault().getWorkbench();
			final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					try {
						String editorId = null;
						IEditorDescriptor editor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(iFile.getLocation().toOSString(), iFile.getContentDescription().getContentType());
						if (editor != null) {
							editorId = editor.getId();
						}
						workbenchWindow.getActivePage().openEditor(new FileEditorInput(iFile), editorId);
					}
					catch (PartInitException ex) {
						// B2BGUIPlugin.getPlugin().getMsgLogger().write("Exception
						// encountered when attempting to open file: " + iFile
						// + "\n\n" + ex);
					}
					catch (CoreException ex) {
					}
				}
			});
		}
	}

	public class AvailableContentCMVisitor extends CMVisitor {
		public static final int INCLUDE_ATTRIBUTES = ModelQuery.INCLUDE_ATTRIBUTES;
		public static final int INCLUDE_CHILD_NODES = ModelQuery.INCLUDE_CHILD_NODES;
		public static final int INCLUDE_SEQUENCE_GROUPS = ModelQuery.INCLUDE_SEQUENCE_GROUPS;

		public Hashtable childNodeTable = new Hashtable();
		public Hashtable attributeTable = new Hashtable();
		public Element rootElement;
		public CMElementDeclaration rootElementDeclaration;
		public boolean isRootVisited;
		protected boolean includeSequenceGroups;
		public DOMValidator validator;

		public AvailableContentCMVisitor(Element rootElement, CMElementDeclaration rootElementDeclaration) {
			this.rootElement = rootElement;
			this.rootElementDeclaration = rootElementDeclaration;
			validator = new DOMValidator();
		}

		protected String getKey(CMNode cmNode) {
			String key = cmNode.getNodeName();
			CMDocument cmDocument = (CMDocument) cmNode.getProperty("CMDocument"); //$NON-NLS-1$
			if (cmDocument != null) {
				String namespaceURI = (String) cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI"); //$NON-NLS-1$
				if (namespaceURI != null) {
					key = "[" + namespaceURI + "]" + key; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return key;
		}

		public List computeAvailableContent(int includeOptions) {
			Vector v = new Vector();

			int contentType = rootElementDeclaration.getContentType();
			includeSequenceGroups = ((includeOptions & INCLUDE_SEQUENCE_GROUPS) != 0);
			visitCMNode(rootElementDeclaration);

			if ((includeOptions & INCLUDE_ATTRIBUTES) != 0) {
				v.addAll(attributeTable.values());
				CMAttributeDeclaration nillableAttribute = (CMAttributeDeclaration) rootElementDeclaration.getProperty("http://org.eclipse.wst/cm/properties/nillable"); //$NON-NLS-1$
				if (nillableAttribute != null) {
					v.add(nillableAttribute);
				}
			}

			if ((includeOptions & INCLUDE_CHILD_NODES) != 0) {
				if (contentType == CMElementDeclaration.MIXED || contentType == CMElementDeclaration.ELEMENT) {
					v.addAll(childNodeTable.values());
				}
				else if (contentType == CMElementDeclaration.ANY) {
					CMDocument cmDocument = (CMDocument) rootElementDeclaration.getProperty("CMDocument"); //$NON-NLS-1$
					if (cmDocument != null) {
						CMNamedNodeMap elements = cmDocument.getElements();
						for (Iterator i = elements.iterator(); i.hasNext();) {
							v.add((CMElementDeclaration) i.next());
						}
					}
				}

				if (contentType == CMElementDeclaration.MIXED || contentType == CMElementDeclaration.PCDATA || contentType == CMElementDeclaration.ANY) {
					CMDataType dataType = rootElementDeclaration.getDataType();
					if (dataType != null) {
						v.add(dataType);
					}
				}
			}
			return v;
		}

		/*
		 * public void visitCMAnyElement(CMAnyElement anyElement) { String uri =
		 * anyElement.getNamespaceURI(); List list =
		 * getCMDocumentList(rootElement, rootElementDeclaration, uri); for
		 * (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
		 * CMDocument cmdocument = (CMDocument)iterator.next(); if (cmdocument !=
		 * null) { CMNamedNodeMap map = cmdocument.getElements(); int size =
		 * map.getLength(); for (int i = 0; i < size; i++) { CMNode ed =
		 * map.item(i);
		 * 
		 * childNodeTable.put(getKey(ed), ed); } } } }
		 */
		public void visitCMAttributeDeclaration(CMAttributeDeclaration ad) {
			super.visitCMAttributeDeclaration(ad);
			attributeTable.put(ad.getNodeName(), ad);
		}

		public void visitCMElementDeclaration(CMElementDeclaration ed) {
			if (ed == rootElementDeclaration && !isRootVisited) {
				isRootVisited = true;
				super.visitCMElementDeclaration(ed);
			}
			else {
				if (!Boolean.TRUE.equals(ed.getProperty("Abstract"))) { //$NON-NLS-1$
					childNodeTable.put(getKey(ed), ed);
				}

				CMNodeList substitutionGroup = (CMNodeList) ed.getProperty("SubstitutionGroup"); //$NON-NLS-1$
				if (substitutionGroup != null) {
					handleSubstitutionGroup(substitutionGroup);
				}
			}
		}

		protected void handleSubstitutionGroup(CMNodeList substitutionGroup) {
			int substitutionGroupLength = substitutionGroup.getLength();
			if (substitutionGroupLength > 1) {
				for (int i = 0; i < substitutionGroupLength; i++) {
					CMNode ed = substitutionGroup.item(i);
					if (!Boolean.TRUE.equals(ed.getProperty("Abstract"))) { //$NON-NLS-1$
						childNodeTable.put(getKey(ed), ed);
					}
				}
			}
		}

		public void visitCMGroup(CMGroup group) {
			if (includeSequenceGroups) {
				if (group.getOperator() == CMGroup.SEQUENCE && group.getChildNodes().getLength() > 1 && includesRequiredContent(group)) {
					childNodeTable.put(group, group);
				}
			}
			super.visitCMGroup(group);
		}

		public boolean includesRequiredContent(CMGroup group) {
			List list = getValidator().createContentSpecificationList(group);
			return list.size() > 1;
		}

		public DOMValidator getValidator() {
			return validator;
		}
	} // ///////////////////////// here
}
