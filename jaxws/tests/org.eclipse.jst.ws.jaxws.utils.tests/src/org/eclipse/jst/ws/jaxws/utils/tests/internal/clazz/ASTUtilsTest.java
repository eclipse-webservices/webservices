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
package org.eclipse.jst.ws.jaxws.utils.tests.internal.clazz;

import java.io.File;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jst.ws.jaxws.testutils.files.TestFileUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.clazz.ASTUtils;

/**
 * Tests for {@link ASTUtils} class.
 * 
 * @author Georgi Vachkov
 */
public class ASTUtilsTest extends TestCase
{
	private static final String PCK = "org.eclipse.test";
	private static final String SRC = 
			"import javax.xml.namespace.QName;" + "\n" +
			"public class Test {" + "\n" +
			"	private final static String name = \"are\";" + "\n" +
			"   public Test() {}" + "\n" +
			"	public int createInt() {return 0;}" + "\n" +
			"}";	

	private TestProject project;
	
	public void manualSetUp() throws Exception
	{
		project = new TestProject("JavaProj_" + System.currentTimeMillis());
		project.createSourceFolder("src");
	}
	
	@Override
	public void tearDown() throws Exception
	{
		if (project != null) {
			project.dispose();
		}
	}
	
	public void testCreateASTFileNpe() throws Exception
	{
		try {
			ASTUtils.getInstance().createAST((File)null, null);
		} catch(NullPointerException _) {}
	}

	public void testCreateASTFile() throws Exception
	{
		final File tempJavaFile = createTempJavaFile();
		ASTNode node = ASTUtils.getInstance().createAST(tempJavaFile, null);
		assertNotNull(node);
		assertTrue(node instanceof CompilationUnit);	
	}

	public void testCreateASTITypeNpe() throws Exception
	{
		try {
			ASTUtils.getInstance().createCompilationUnit((ICompilationUnit)null, null);
		} catch(NullPointerException _) {}
	}
	
	public void testCreateASTIType() throws Exception
	{
		manualSetUp();
		final IType sourceType = project.createType(project.createPackage(PCK), "Test.java", SRC);
		ASTNode node = ASTUtils.getInstance().createCompilationUnit(sourceType.getCompilationUnit(), null);
		assertNotNull(node);
		assertTrue(node instanceof CompilationUnit);
	}	
	
	public void testGetTypeDeclaration() throws Exception
	{
		manualSetUp();
		final IType sourceType = project.createType(project.createPackage(PCK), "Test.java", SRC);
		CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(sourceType.getCompilationUnit(), null);
		
		AbstractTypeDeclaration abstractType = ASTUtils.getInstance().getTypeDeclaration("Test", unit);
		assertNotNull(abstractType);
	}
	
	public void testGetTypeDeclarationForInnerType() throws Exception
	{
		manualSetUp();
		String source = "public class Test {" +
			"public static class Parameters {}" +
		"}";		
		
		final IType sourceType = project.createType(project.createPackage(PCK), "Test.java", source);
		CompilationUnit unit = ASTUtils.getInstance().createCompilationUnit(sourceType.getCompilationUnit(), null);
		
		AbstractTypeDeclaration abstractType = ASTUtils.getInstance().getTypeDeclaration("Parameters", unit);
		assertNotNull(abstractType);
	}

	private File createTempJavaFile() throws Exception
	{
		File dir = TestFileUtils.createTempDirectory("" + System.currentTimeMillis());
		File tempClassFile = new File(dir, "Test.java");
		FileWriter fw = new FileWriter(tempClassFile);
		fw.write("package " + PCK + ";\n" + SRC);
		fw.close();
		
		return tempClassFile;
	}
}
