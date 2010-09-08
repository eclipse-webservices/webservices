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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceManipulation;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;

public class ModelSynchronizationTests extends SynchronizationTestFixture
{
	private void assertBeforeAfterAreTheSame()
	{	final IWebServiceProject newRefWsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		assertEquals(wsPrj1, newRefWsPrj1);
		assertEquals(ws1, domUtil.findWsByImplName(newRefWsPrj1, ws1ImplName));
		assertEquals(sei1, domUtil.findSeiByImplName(newRefWsPrj1, sei1ImplName));
		
		final IWebServiceProject newRefWsPrj2 = domUtil.findProjectByName(target.getDOM(), testPrj2.getJavaProject().getElementName());
		assertEquals(wsPrj2, newRefWsPrj2);
		assertEquals(ws2, domUtil.findWsByImplName(newRefWsPrj2, ws2ImplName));
		assertEquals(sei2, domUtil.findSeiByImplName(newRefWsPrj2, sei2ImplName));
	}
	
	public void test_openedWsProjectSynched() throws Exception
	{
		final TestProject testPrj3 = new TestProject(new TestEjb3Project("TestEJBProject3").getProject());
		try {
			allowedProjects.add(testPrj3.getJavaProject().getElementName());
			final IPackageFragment modelSync3 = testPrj3.createPackage("com.sap.test.modelsync3");
			testPrj3.createType(modelSync3, "Sei3.java", "@javax.jws.WebService(name=\"Sei3Name\") public interface Sei3 {}");
			testPrj3.createType(modelSync3, "WS3.java", "@javax.jws.WebService(serviceName=\"WS3Name\", endpointInterface=\"com.sap.test.modelsync1.Sei3Name\") public class WS3 {}");
			testPrj3.getProject().close(null);
			JobUtils.waitForJobs();
			
			target.startSynchronizing();
			testPrj3.getProject().open(null);
			JobUtils.waitForJobs();
			
			assertBeforeAfterAreTheSame();
			final IWebServiceProject wsPrj3 = domUtil.findProjectByName(target.getDOM(), testPrj3.getJavaProject().getElementName());
			assertNotNull("Web Service Project not synchronized after opened",wsPrj3);
			final IWebService ws3 = domUtil.findWsByImplName(wsPrj3, "com.sap.test.modelsync3.WS3");
			assertNotNull("Web Service not synchronized after containing project was oppened", ws3);
			final IServiceEndpointInterface sei3 = domUtil.findSeiByImplName(wsPrj3, "com.sap.test.modelsync3.Sei3");
			assertNotNull("Service Endpoint interface not synchronized after containing project was oppened", sei3);
		} finally
		{
			testPrj3.dispose();
		}
	}
		
	public void test_openedProjectWithEnumsInitialySynched() throws CoreException, Exception
	{
		final TestProject testPrj3 = new TestProject(new TestEjb3Project("TestEJBProject3").getProject());
		try {
			allowedProjects.add(testPrj3.getJavaProject().getElementName());
			final IPackageFragment modelSync3 = testPrj3.createPackage("com.sap.test.modelsync3");
			testPrj3.createType(modelSync3, "TestEnum.java", "public enum TestEnum {A, B, C}");
			target = createTarget();
			JobUtils.waitForJobs();
			
			target.load(null);
			final IWebServiceProject wsPrj3 = domUtil.findProjectByName(target.getDOM(), testPrj3.getJavaProject().getElementName());
			assertNotNull("Web Service Project not synchronized after created",wsPrj3);
		} finally 
		{
			testPrj3.dispose();
		}
	}

	public void test_newSeiCompilationUnitsSynched() throws JavaModelException, CoreException, InterruptedException
	{
		verifySetUpModelEntitesNotChanged();
		final IPackageFragment pf = testPrj1.createPackage("com.sap.test.newsei");
		JobUtils.waitForJobs();
		
		target.startSynchronizing();
		testPrj1.createType(pf, "Sei3.java", "@javax.jws.WebService(name=\"Sei3Name\", targetNamespace=\"http://com.sap.test/\") public interface Sei3 {}");
		JobUtils.waitForJobs();
		
		assertBeforeAfterAreTheSame();
		final IWebServiceProject wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		final IServiceEndpointInterface sei3 = domUtil.findSeiByImplName(wsPrj1, "com.sap.test.newsei.Sei3");
		assertNotNull("Newly added SEI not synched into the model",sei3);
		assertEquals("Java Intf for newly added SEI does not match the expected", "com.sap.test.newsei.Sei3", sei3.getImplementation());
		assertEquals("Target NS for newly added SEI does not match the expected", "http://com.sap.test/", sei3.getTargetNamespace());
		assertEquals("The name of the newly added sei doesn't match", "Sei3Name", sei3.getName());
	}

	public void test_unreSolvedSeiRefGetsResolvedAfterSeiAdded() throws JavaModelException, CoreException, InterruptedException
	{
		verifySetUpModelEntitesNotChanged();
		final IPackageFragment pf = testPrj1.createPackage("com.sap.test.newws");
		JobUtils.waitForJobs();
		//Make the WS be created with a reference to "com.sap.test.newsei.Sei3" which doesn't exist yet. 
		target.startSynchronizing();
		testPrj1.createType(pf, "WS3.java", "@javax.jws.WebService(serviceName=\"WS3Name\", endpointInterface=\"com.sap.test.newsei.Sei3\") public class WS3 {}");
		JobUtils.waitForJobs();
		
		assertBeforeAfterAreTheSame();
		
		final IWebServiceProject wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		final IWebService ws3 = domUtil.findWsByImplName(wsPrj1, "com.sap.test.newws.WS3");
		assertNotNull("Web Service not synchronized after new compilation unit with ws was added", ws3);
		//add a new sei and test that it has been synched.
		test_newSeiCompilationUnitsSynched();
		//now check taht the Sei ref in ws3 has been properly resolved
		final IServiceEndpointInterface newlyAddedSei = domUtil.findSeiByImplName(wsPrj1, "com.sap.test.newsei.Sei3");
		assertNotNull("Newly added sei not found in the model. Probably the test_newSeiCompilationUnitsSynched() test method has changed the name of the interface it works with", newlyAddedSei);
		assertSame("Web Service Reference not resolved properly", newlyAddedSei, ws3.getServiceEndpoint());
	}
	
	public void test_newSourceFolderWithJavaContentSynchedWhenAdded() throws JavaModelException, CoreException, InterruptedException
	{
	//	verifySetUpModelEntitesNotChanged();
		//create new folder, which will later on be made a soure folder
		IFolder folder = testPrj1.getProject().getFolder("src1");
		folder.create(true, true, null);
		//make the folder a source folder now in order to create the type in there.
		final IClasspathEntry[] oldEntries = testPrj1.getJavaProject().getRawClasspath();
		final IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		//create a new package fragment folder. Note that it cannot be used to create type, as the folder is not source folder yet
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		final IPackageFragmentRoot newSourceFolder = testPrj1.getJavaProject().getPackageFragmentRoot(folder);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(newSourceFolder.getPath());
		testPrj1.setClasspath(newEntries);
		final IPackageFragment pf = newSourceFolder.createPackageFragment("com.sap.test.newws", true, null);
		//now create the types
		testPrj1.createType(pf, "WS3.java", "@javax.jws.WebService(serviceName=\"WS3Name\", endpointInterface=\"com.sap.test.newsei.Sei3\") public class WS3 {}");
		testPrj1.createType(pf, "Sei3.java", "@javax.jws.WebService(name=\"Sei3Name\") public interface Sei3 {}");
		//after the type is created make the source folder an ordinary source again.
		testPrj1.setClasspath(oldEntries);
		JobUtils.waitForJobs();
		//then start synchronizing
		target.startSynchronizing();
		//finally the resource is sychnronizing and in the same time we have a folder with source content. So just make it a source folder again.
		testPrj1.setClasspath(newEntries);
		JobUtils.waitForJobs();
		
		final IWebServiceProject wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		final IWebService ws3 = domUtil.findWsByImplName(wsPrj1, "com.sap.test.newws.WS3");
		assertNotNull("Web Service not synchronized after a source folder in which it was contained was added", ws3);
		final IServiceEndpointInterface sei3 = domUtil.findSeiByImplName(wsPrj1, "com.sap.test.newws.Sei3");
		assertNotNull("Sei not synched after a source folder in which it was contained was added", sei3);
		
	}
	
	public void test_removedSourceFolderSyched() throws CoreException, InterruptedException
	{
		test_newSourceFolderWithJavaContentSynchedWhenAdded();
		final IClasspathEntry[] oldEntries = testPrj1.getJavaProject().getRawClasspath();
		final IClasspathEntry[]  newEntries = new IClasspathEntry[oldEntries.length - 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, newEntries.length);
		testPrj1.setClasspath(newEntries);
		JobUtils.waitForJobs();
		
		assertNull(domUtil.findWsByImplName(wsPrj1, "com.sap.test.newws.WS3"));
		assertNull(domUtil.findSeiByImplName(wsPrj1, "com.sap.test.newws.Sei3"));
	}
	
	public void test_removedOrClosedProjectSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync1 = testPrj1.createPackage("com.sap.test.modelsync1_1");
		testPrj1.createType(modelSync1, "WS3.java", "@javax.jws.WebService(serviceName=\"WS3Name\", endpointInterface=\"com.sap.test.modelsync2_1.Sei3\") public class WS3 {}");
	
		final IPackageFragment modelSync2 = testPrj2.createPackage("com.sap.test.modelsync2_1");
		testPrj2.createType(modelSync2, "Sei3.java", "@javax.jws.WebService(name=\"Sei3Name\") public interface Sei3 {}");
		JobUtils.waitForJobs();
		
		
		final IWebService ws3 = domUtil.findWsByImplName(wsPrj1, "com.sap.test.modelsync1_1.WS3");
		assertNotNull(ws3);
		
		final IServiceEndpointInterface sei3 = domUtil.findSeiByImplName(wsPrj2, "com.sap.test.modelsync2_1.Sei3");
		assertNotNull(sei3);
		assertEquals(sei3, ws3.getServiceEndpoint());
		final String testPrj2Name = testPrj2.getJavaProject().getElementName();
		testPrj2.getProject().close(null);
		JobUtils.waitForJobs();
		assertNull(DomUtil.INSTANCE.findProjectByName(target.getDOM(), testPrj2Name));
		assertNull("The web service returns a sei instance which is contained in a closed project", ws3.getServiceEndpoint());
	}
	
	public void test_deletedCUWithSeiSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit sei1CU = testPrj1.getJavaProject().findType(sei1ImplName).getCompilationUnit();
		delete(sei1CU, true);
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getServiceEndpointInterfaces().contains(sei1));
		assertNull(ws1.getServiceEndpoint());
	}
	
	public void test_deletedCUWithWSSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1ImplName).getCompilationUnit();
		delete(ws1CU, true);
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getWebServices().contains(ws1));
		assertFalse(sei1.getImplementingWebServices().contains(ws1));
	}

	public void test_deletedCuContentWithSeiSynched() throws CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit sei1Cu = testPrj1.getJavaProject().findType(sei1ImplName).getCompilationUnit();
		testUtil.setContents(sei1Cu, "");
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getWebServices().contains(sei1));
		assertNull(ws1.getServiceEndpoint());
	}
	
	public void test_deletedCuContentWithWsSynched() throws CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1ImplName).getCompilationUnit();
		testUtil.setContents(ws1CU, "");
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getWebServices().contains(ws1));
		assertFalse(sei1.getImplementingWebServices().contains(ws1));
	}	
	
	public void test_movedCUWithWSInOtherProjectWithSameJavaNameSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1ImplName).getCompilationUnit();
		final IPackageFragment moveIn = testPrj2.getSourceFolder().createPackageFragment(ws1CU.getParent().getElementName(), true, null);
		ws1CU.move(moveIn, null, null, false, null);
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getWebServices().contains(ws1));
		assertTrue(wsPrj2.getWebServices().contains(ws1));
		assertTrue(sei1.getImplementingWebServices().contains(ws1));
	}
	
	public void test_movedCUWithSeiInOtherProjectWithSameJavaNameSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit se1CU = testPrj1.getJavaProject().findType(sei1ImplName).getCompilationUnit();
		final IPackageFragment moveIn = testPrj2.getSourceFolder().createPackageFragment(se1CU.getParent().getElementName(), true, null);
		se1CU.move(moveIn, null, null, false, null);
		JobUtils.waitForJobs();
		
		assertFalse(wsPrj1.getServiceEndpointInterfaces().contains(sei1));
		assertTrue(wsPrj2.getServiceEndpointInterfaces().contains(sei1));
		assertSame(ws1.getServiceEndpoint(), sei1);
	}
	
	public void test_renamedCUWithSeiSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit se1CU = testPrj1.getJavaProject().findType(sei1ImplName).getCompilationUnit();
		se1CU.findPrimaryType().rename("Sei1NewName", false, null);
		JobUtils.waitForJobs();
		final ICompilationUnit se1CURenamed = testPrj1.getJavaProject().findType("com.sap.test.modelsync1.Sei1NewName").getCompilationUnit();
		
		assertEquals("Sei1NewName.java", se1CURenamed.getElementName());
		IServiceEndpointInterface renamedSei1 = domUtil.findSeiByImplName(wsPrj1, se1CURenamed.findPrimaryType().getFullyQualifiedName());
		assertNotNull(renamedSei1);
		assertNull(ws1.getServiceEndpoint());
	}

	public void test_renamedCUWithWSSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1ImplName).getCompilationUnit();
		ws1CU.findPrimaryType().rename("WS1NewName", false, null);
		JobUtils.waitForJobs();
		final ICompilationUnit ws1CURenamed = testPrj1.getJavaProject().findType("com.sap.test.modelsync1.WS1NewName").getCompilationUnit();
		
		assertEquals("WS1NewName.java", ws1CURenamed.getElementName());
		IWebService renamedWS1 = domUtil.findWsByImplName(wsPrj1, ws1CURenamed.findPrimaryType().getFullyQualifiedName());
		assertNotNull(renamedWS1);
		assertFalse(sei1.getImplementingWebServices().contains(ws1));
		assertTrue(sei1.getImplementingWebServices().contains(renamedWS1));
		assertSame(sei1, renamedWS1.getServiceEndpoint());
	}
	
	public void test_removedWSAnnotationFromSeiSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit se1CU = testPrj1.getJavaProject().findType(sei1ImplName).getCompilationUnit();
		testUtil.setContents(se1CU, "public interface Sei1 {}");
		JobUtils.waitForJobs();
		assertNull(domUtil.findSeiByImplName(wsPrj1, sei1ImplName));
		assertNull(ws1.getServiceEndpoint());
	}
	
	public void test_removedWSAnnotationFromWSSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit se1CU = testPrj1.getJavaProject().findType(ws1ImplName).getCompilationUnit();
		testUtil.setContents(se1CU, "public class WS1 {}");
		JobUtils.waitForJobs();
		assertNull(domUtil.findSeiByImplName(wsPrj1, ws1ImplName));
		assertTrue(sei1.getImplementingWebServices().size() == 0);
	}
	
	public void test_addedWSAnnotaionToClassSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync4 = testPrj1.createPackage("com.sap.test.modelsync4");
		final ICompilationUnit ws4CU = testPrj1.createType(modelSync4, "WS4.java", "public class WS4 {}").getCompilationUnit();
		testUtil.setContents(ws4CU, "@javax.jws.WebService(serviceName=\"WS4Name\", endpointInterface=\"com.sap.test.modelsync1.Sei1\", portName=\"TestPortName\", targetNamespace=\"http://com.sap/test\", wsdlLocation=\"C:/test/wsdl/location\") public class WS4 {}");
		JobUtils.waitForJobs();
		
		final IWebService addedWS4 = domUtil.findWsByImplName(wsPrj1, ws4CU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(addedWS4);
		assertTrue(addedWS4.getServiceEndpoint() == sei1);
		assertEquals("portName not same", "TestPortName", addedWS4.getPortName());
		assertEquals("targetNamespace not same", "http://com.sap/test", addedWS4.getTargetNamespace());
		assertEquals("wsdlLocation not same", "C:/test/wsdl/location", addedWS4.getWsdlLocation());
	}
	
	public void test_addedWSAnnotationToSeiSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync4 = testPrj1.createPackage("com.sap.test.modelsync4");
		
		final IType ws4Type = testPrj1.createType(modelSync4, "WS4.java", "@javax.jws.WebService(serviceName=\"WS4Name\", endpointInterface=\"com.sap.test.modelsync4.Sei4\") public class WS4 {}");
		JobUtils.waitForJobs();
		
		final IWebService ws4 = domUtil.findWsByImplName(wsPrj1, ws4Type.getFullyQualifiedName());
		assertNotNull(ws4);
		assertNull(ws4.getServiceEndpoint());
		
		final ICompilationUnit sei4CU = testPrj1.createType(modelSync4, "Sei4.java", "public interface Sei4 {}").getCompilationUnit();
		testUtil.setContents(sei4CU, "@javax.jws.WebService(name=\"Sei4Name\") public interface Sei4 {}");
		JobUtils.waitForJobs();
		
		final IServiceEndpointInterface addedWS4 = domUtil.findSeiByImplName(wsPrj1, sei4CU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(addedWS4);
		assertSame(ws4.getServiceEndpoint(), addedWS4);
	}
	
	public void test_changedSeiRefSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		testUtil.setContents(ws1CU, "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync2.Sei2\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertSame(sei2, ws1.getServiceEndpoint());
		assertSame(ws1, domUtil.findWsByImplName(wsPrj1, ws1.getImplementation()));
	}
	
	public void test_changedSeiRefWithUnxesistingSynched() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		testUtil.setContents(ws1CU, "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync2.SeiUnexisting\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertNull(ws1.getServiceEndpoint());
		assertSame(ws1, domUtil.findWsByImplName(wsPrj1, ws1.getImplementation()));
	}	
	
	public void test_changedFromExplicitToImplicit() throws JavaModelException, CoreException
	{
		target.startSynchronizing();
		final ICompilationUnit ws1CU = testPrj1.getJavaProject().findType(ws1.getImplementation()).getCompilationUnit();
		testUtil.setContents(ws1CU, "@javax.jws.WebService(serviceName=\"WS1Name\", name=\"ImplicitInterfaceName\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		assertNotSame(sei2, ws1.getServiceEndpoint());
		assertTrue(ws1.getServiceEndpoint().isImplicit());
		assertEquals("ImplicitInterfaceName", ws1.getServiceEndpoint().getName());
	}
	
	public void test_deletedPackageSynched() throws CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync3 = testPrj1.createPackage("com.sap.test.modelsync3");
		testPrj1.createType(modelSync3, "WS4.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS4 {}");
		JobUtils.waitForJobs();
		
		final IWebService ws4 = domUtil.findWsByImplName(wsPrj1, "com.sap.test.modelsync3.WS4");
		assertNotNull(ws4);
		assertSame(sei1, ws4.getServiceEndpoint());
		final IPackageFragment pf = testPrj1.getSourceFolder().getPackageFragment("com.sap.test.modelsync1");
		delete(pf, true);
		JobUtils.waitForJobs();
		
		assertNull(domUtil.findSeiByImplName(wsPrj1, sei1ImplName));
		assertNull(domUtil.findWsByImplName(wsPrj1, ws1ImplName));
		assertNull(ws4.getServiceEndpoint());
	}
	
	public void test_addedPackageWithCompilationUnitsSynched() throws CoreException, IOException
	{
		target.startSynchronizing();
		final File ioPackage = new File(testPrj1.getSourceFolder().getResource().getRawLocation().toOSString(), "com/sap/test/modelsync4");
		ioPackage.mkdirs();
		final FileWriter writer = new FileWriter(new File(ioPackage, "WS4.java"));
		try {
			writer.write("package com.sap.test.modelsync4;\n @javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS4 {}");
			writer.flush();
		} finally 
		{
			writer.close();
		}
		testPrj1.getSourceFolder().getResource().refreshLocal(IResource.DEPTH_INFINITE,null);
		JobUtils.waitForJobs();
		
		assertNotNull(domUtil.findWsByImplName(wsPrj1, "com.sap.test.modelsync4.WS4"));
	}

	public void test_changedClassToInterface() throws CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync = testPrj1.createPackage("com.sap.test.modelsync4");		
		final ICompilationUnit wsCU = testPrj1.createType(modelSync, "WS4.java", "@javax.jws.WebService() public class WS4 {}").getCompilationUnit();
		JobUtils.waitForJobs();
		IWebService ws4 = domUtil.findWsByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(ws4);
		testUtil.setContents(wsCU, "@javax.jws.WebService() public interface WS4 {}");
		JobUtils.waitForJobs();
		ws4 = domUtil.findWsByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNull(ws4);
		IServiceEndpointInterface sei = domUtil.findSeiByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(sei);
		assertFalse(sei.isImplicit());
	}

	public void test_changedInterfaceToClass() throws CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync = testPrj1.createPackage("com.sap.test.modelsync4");		
		final ICompilationUnit wsCU = testPrj1.createType(modelSync, "WS4.java", "@javax.jws.WebService() public interface WS4 {}").getCompilationUnit();
		JobUtils.waitForJobs();
		
		IServiceEndpointInterface sei = domUtil.findSeiByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(sei);
		assertFalse(sei.isImplicit());		
		testUtil.setContents(wsCU, "@javax.jws.WebService() public class WS4 {}");
		JobUtils.waitForJobs();
		
		sei = domUtil.findSeiByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(sei);
		assertTrue(sei.isImplicit());
		IWebService ws4 = domUtil.findWsByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(ws4);
	}
	
	public void test_changedInterfaceAndClassWithSimilarNamesNotRemovedFromDom() throws CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync = testPrj1.createPackage("com.sap.test.modelsync5");		
		final ICompilationUnit wsCU = testPrj1.createType(modelSync, "Test.java", "@javax.jws.WebService() public class Test {}").getCompilationUnit();
		JobUtils.waitForJobs();
		
		assertNotNull(domUtil.findWsByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName()));		
		final ICompilationUnit seiCU = testPrj1.createType(modelSync, "TestSei.java", "@javax.jws.WebService() public interface TestSei {}").getCompilationUnit();
		JobUtils.waitForJobs();
		assertNotNull(domUtil.findSeiByImplName(wsPrj1, seiCU.findPrimaryType().getFullyQualifiedName()));
		
		testUtil.setContents(wsCU, "@javax.jws.WebService(serviceName=\"TestService\") public class Test {}");
		JobUtils.waitForJobs();
		assertNotNull(domUtil.findSeiByImplName(wsPrj1, seiCU.findPrimaryType().getFullyQualifiedName()));
		assertNotNull(domUtil.findWsByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName()));		
	}	

	public void test_WsChangedToUseExplicitSEI() throws CoreException
	{
		target.startSynchronizing();
		final IPackageFragment modelSync = testPrj1.createPackage("com.sap.test.modelsync4");		
		final ICompilationUnit wsCU = testPrj1.createType(modelSync, "WS4.java", "@javax.jws.WebService() public class WS4 {}").getCompilationUnit();
		JobUtils.waitForJobs();
		IServiceEndpointInterface sei = domUtil.findSeiByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNotNull(sei);
		assertTrue(sei.isImplicit());		
		
		testUtil.setContents(wsCU, "@javax.jws.WebService(endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS4 {}");
		JobUtils.waitForJobs();
		sei = domUtil.findSeiByImplName(wsPrj1, wsCU.findPrimaryType().getFullyQualifiedName());
		assertNull(sei);		
	}
	
	public void test_WsOpenedInWorkingCopyContentChangedReconcileCalled() throws CoreException
	{	
		target.startSynchronizing();
		final IPackageFragment modelSync = testPrj1.createPackage("com.sap.test.modelsync4");		
		final ICompilationUnit wsCu = testPrj1.createType(modelSync, "WS3.java", "@javax.jws.WebService() public class WS3 {}").getCompilationUnit();
		JobUtils.waitForJobs();
		IWebService ws3 = domUtil.findWsByImplName(wsPrj1, wsCu.findPrimaryType().getFullyQualifiedName());
		
		assertEquals(0, ws3.getServiceEndpoint().getWebMethods().size());
		// update compilation unit without updating the underlying resource
		wsCu.becomeWorkingCopy(null);
		wsCu.getBuffer().setContents("@javax.jws.WebService public class WS3 {" +
				"public void test() {}" +
				"}");
		wsCu.reconcile(ICompilationUnit.NO_AST, false, null, null);
		JobUtils.waitForJobs();
		
		assertEquals(1, ws3.getServiceEndpoint().getWebMethods().size());
	}
	
	public void test_WsOpenedInWorkingCopyContentChangedReconcileCalledOnNonJavaCU() throws CoreException
	{	
		target.startSynchronizing();
		
		IPackageFragmentRoot root = testPrj1.getJavaProject().getPackageFragmentRoot(testPrj1.getProject());
		IPackageFragment pf = root.getPackageFragment("");
		ICompilationUnit cu = pf.createCompilationUnit("NotWS.java", "@javax.jws.WebService public class NotWS {}", true, null);
		cu.becomeWorkingCopy(null);
		cu.getBuffer().setContents("@javax.jws.WebService(serviceName=\"NewNotWs\") public class NotWS {}");
		cu.reconcile(ICompilationUnit.NO_AST, false, null, null);
		JobUtils.waitForJobs();
		
		for (IWebService ws : wsPrj1.getWebServices()) {
			if (ws.getName().equals("NewNotWs")) {
				fail("reconcile on java file which is not on project's build path processed incorrectly");
			}
		}
	}
	
	private JaxWsWorkspaceResource createTarget()
	{
		return new JaxWsWorkspaceResource(javaModel) 
		{

			@Override
			public boolean approveProject(IJavaProject prj) {
				for (String allowedPrj : allowedProjects)
				{
					if (prj.getElementName().equals(allowedPrj))
					{
						return true;
					}
				}
				return false;
			}
		};
	}

	private void verifySetUpModelEntitesNotChanged()
	{
		final Adapter adapter = new AdapterImpl()
		{

			@Override
			public void notifyChanged(Notification msg)
			{
				fail("should not be called, because the objects created in the setup should not have been changed during this test");
				super.notifyChanged(msg);
			}
			
		};
		this.ws1.eAdapters().add(adapter);
		this.sei1.eAdapters().add(adapter);
		this.ws2.eAdapters().add(adapter);
		this.sei2.eAdapters().add(adapter);
	}
	
	private void delete(final ISourceManipulation source, final boolean force) throws CoreException
	{
		final IWorkspaceRunnable deleteRunable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				source.delete(force, monitor);
			}
		};
		
		TestProjectsUtils.executeWorkspaceRunnable(deleteRunable);
	}
}
