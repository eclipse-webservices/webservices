/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.tests.dom.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryLabelProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.validation.DomValidationHelper;
import org.eclipse.jst.ws.jaxws.dom.integration.validation.DomValidationManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IDomValidator;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

@SuppressWarnings("nls")
public class DomValidationManagerTest extends TestCase 
{
	private static final String WST_VALIDATION_XP = "org.eclipse.wst.validation.validator";

	public void testRegistered()
	{
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(WST_VALIDATION_XP);
		IExtension extension = extensionPoint.getExtension("org.eclipse.jst.ws.jaxws.dom.integration.wsValidator");
		assertNotNull(extension);
		
		IConfigurationElement element = findElement(extension.getConfigurationElements(), "validator");
		element = findElement(element.getChildren(), "run");
		assertEquals(DomValidationManager.class.getName(), element.getAttribute("class"));
	}
	
	private IConfigurationElement findElement(IConfigurationElement [] configElements, String name) 
	{
		for (IConfigurationElement configElement : configElements) {
			if (configElement.getName().equals(name)) {
				return configElement;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("restriction")
	public void testValidateCalled() throws Exception
	{
		TestEjb3Project ejbProject = new TestEjb3Project("EjbProject" + System.currentTimeMillis());
		TestProject testProject = new TestProject(ejbProject.getProject());
		IPackageFragment pack = testProject.getSourceFolder().createPackageFragment("org.eclipse.test", true, null);
		testProject.createType(pack, "Sei.java", "@javax.jws.WebService public interface Sei {}");
		
		DomValidationHelper helper = new DomValidationHelper();
		helper.setProject(testProject.getProject());
	
		MyDomValidationManager manager = new MyDomValidationManager("org.eclipse.jst.ws.jaxws.dom.jee5");
		manager.validate(helper, null);
		assertTrue(manager.validationCalled);
		
		manager = new MyDomValidationManager("sample.not.existing");
		manager.validate(helper, null);
		assertFalse(manager.validationCalled);
	}
	
	@SuppressWarnings("restriction")
	public void testValidateCleansUpErrorMarkers() throws Exception
	{
		final TestProject testProject = createProject();
		IType seiType = testProject.getJavaProject().findType("org.eclipse.test.Sei");
		seiType.getResource().createMarker(DomValidationConstants.MARKER_ID);
		IMarker [] markers = seiType.getResource().findMarkers(DomValidationConstants.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(1, markers.length);
		
		DomValidationHelper helper = new DomValidationHelper();
		helper.setProject(testProject.getProject());		
		MyDomValidationManager manager = new MyDomValidationManager("org.eclipse.jst.ws.jaxws.dom.jee5");
		manager.validate(helper, null);	
		
		markers = seiType.getResource().findMarkers(DomValidationConstants.MARKER_ID, false, IResource.DEPTH_ZERO);
		assertEquals(0, markers.length);
	}
	
	public void testRefreshTree() throws Exception
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();

		IWebMethod wm = DomFactory.eINSTANCE.createIWebMethod();
		wm.getParameters().add(DomFactory.eINSTANCE.createIWebParam());

		sei.getImplementingWebServices().add(ws);
		sei.getWebMethods().add(wm);
		
		IWebServiceProject wsProject = DomFactory.eINSTANCE.createIWebServiceProject(); 
		wsProject.getWebServices().add(ws);
		wsProject.getServiceEndpointInterfaces().add(sei);
		
		final Set<Object> elements = new HashSet<Object>();
		DOMAdapterFactoryLabelProvider labelProvider = new DOMAdapterFactoryLabelProvider() 
		{			
			@Override
			public void fireLabelProviderChanged (Object element) {
				elements.add(element.getClass().getInterfaces()[0]);
			}	
		};

		Runnable refresh = new MyDomValidationManager("").getRefreshRunnable(labelProvider, wsProject);
		refresh.run();
		
		assertTrue(elements.contains(IWebServiceProject.class));
		assertTrue(elements.contains(IWebServiceChildList.class));
		assertTrue(elements.contains(ISEIChildList.class));
		assertTrue(elements.contains(IServiceEndpointInterface.class));		
		assertTrue(elements.contains(IWebService.class));
		assertTrue(elements.contains(IWebMethod.class));
		assertTrue(elements.contains(IWebParam.class));		
	}
	
	//FIXME Tmp comment out. Failing in local build on Eclipse 3.6 M4
//	public void testValidationFrameworkTriggersValidation() throws Exception
//	{
//		final TestEjb3Project ejbProject = new TestEjb3Project("EjbProject" + System.currentTimeMillis());
//		final TestProject testProject = new TestProject(ejbProject.getProject());
//		final IPackageFragment pack = testProject.getSourceFolder().createPackageFragment("org.eclipse.test", true, null);
//		final IType wsType = testProject.createType(pack, "Ws.java", "@javax.jws.WebService(name=\"Test\") public class Ws {}");
//		testProject.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
//		
//		boolean autoBuild = ResourcesPlugin.getWorkspace().getDescription().isAutoBuilding();
//		try {
//			setAutoBuild(false);
//			setContents(wsType.getCompilationUnit(), "@javax.jws.WebService(name=\"^^^\") public class Ws {}");
//			testProject.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
//			Assertions.waitAssert(new IWaitCondition() {
//				public boolean checkCondition() throws ConditionCheckException {
//					try {
//						IMarker[] markers= wsType.getResource().findMarkers(DomValidationConstants.MARKER_ID, false, IResource.DEPTH_ZERO);
//						for(IMarker m : markers)
//						{
//							if((m.getAttribute(IMarker.SEVERITY) == null) || (m.getAttribute(IMarker.MESSAGE) == null))
//							{
//								return false;
//							}
//							if((Integer)m.getAttribute(IMarker.SEVERITY) == IMarker.SEVERITY_ERROR || JaxWsUtilMessages.WsdlNamesValidator_InvalidNCName2.equals(m.getAttribute(IMarker.MESSAGE)))
//							{
//								return true;
//							}
//						}
//						return false;
//					} catch (CoreException e) {
//						throw new ConditionCheckException(e);
//					}
//				}
//			}, 
//			"Expected error marker was not found");
//		}
//		finally {
//			setAutoBuild(autoBuild);
//		}
//	}
	
	public void setContents(ICompilationUnit cu, String contents) throws JavaModelException
	{
		final String contentToSet = "package "+cu.getParent().getElementName()+";\n"+contents;
		FileUtils.getInstance().setCompilationUnitContent(cu, contentToSet, true, null);
	}
		
	private void setAutoBuild(boolean auto) throws CoreException 
	{
		final IWorkspaceDescription desc = ResourcesPlugin.getWorkspace().getDescription();
		desc.setAutoBuilding(auto);
		ResourcesPlugin.getWorkspace().setDescription(desc);
	}
	
	private TestProject createProject() throws Exception
	{
		TestEjb3Project ejbProject = new TestEjb3Project("EjbProject" + System.currentTimeMillis());
		TestProject testProject = new TestProject(ejbProject.getProject());
		IPackageFragment pack = testProject.getSourceFolder().createPackageFragment("org.eclipse.test", true, null);
		testProject.createType(pack, "Sei.java", "@javax.jws.WebService(endpointInterface=\"\") public interface Sei {}");
		
		return testProject;
	}
	
	protected class MyDomValidationManager extends DomValidationManager
	{
		public boolean validationCalled;
		private String domId;
		
		public MyDomValidationManager(String domId)
		{
			this.domId = domId; 
		}
		
		@Override
		protected Collection<IDomValidator> getValidators()
		{
			IDomValidator validator = new IDomValidator()
			{
				public String getSupportedDomRuntime() {
					return domId;
				}

				public IStatus validate(EObject object) {
					validationCalled = true;
					return null;
			}};
			
			List<IDomValidator> validators = new ArrayList<IDomValidator>();
			validators.add(validator);
			
			return validators;
		}
		
		public RefreshRunnable getRefreshRunnable(DOMAdapterFactoryLabelProvider labelProvider, IWebServiceProject wsProject)
		{
			return new RefreshRunnable(labelProvider, wsProject);
		}
	}
}
