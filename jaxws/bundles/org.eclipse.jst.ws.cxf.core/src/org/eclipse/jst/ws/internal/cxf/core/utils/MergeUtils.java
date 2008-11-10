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
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.merge.java.JControlModel;
import org.eclipse.emf.codegen.merge.java.JMerger;
import org.eclipse.emf.codegen.merge.java.facade.FacadeHelper;
import org.eclipse.emf.codegen.merge.java.facade.JCompilationUnit;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;

/**
 * @author sclarke
 */
public final class MergeUtils {

    private MergeUtils() {
        
    }
    
    private static JMerger getJMerger() {
        FacadeHelper facadeHelper = CodeGenUtil.instantiateFacadeHelper(JMerger.DEFAULT_FACADE_HELPER_CLASS);
        URL mergeFileURL = FileLocator.find(CXFCorePlugin.getDefault().getBundle(), new Path(
                "/jmerger/merge.xml"), null); //$NON-NLS-1$
        JControlModel controlModel = new JControlModel();
        controlModel.initialize(facadeHelper, mergeFileURL.toString());
        JMerger jmerger = new JMerger(controlModel);

        return jmerger;
    }

    public static void merge(File sourceFile, File targetFile) throws IOException {
        JMerger merger = getJMerger();

        // set source
        FileInputStream sourceInputStream = new FileInputStream(sourceFile);
        JCompilationUnit inputCompilationUnit = merger
                .createCompilationUnitForInputStream(sourceInputStream);
        merger.setSourceCompilationUnit(inputCompilationUnit);

        // set target
        FileInputStream targetInputStream = new FileInputStream(targetFile);
        JCompilationUnit targetCompilationUnit = merger
                .createCompilationUnitForInputStream(targetInputStream);
        merger.setTargetCompilationUnit(targetCompilationUnit);

        // merge source and target
        merger.merge();

        // extract merged contents
        // InputStream mergedContents = new ByteArrayInputStream(
        // merger.getTargetCompilationUnit().getContents().getBytes());

        OutputStream targetOutputStream = new FileOutputStream(targetFile);
        byte[] bytes = merger.getTargetCompilationUnit().getContents().getBytes();
        targetOutputStream.write(bytes, 0, bytes.length);

        // overwrite the target with the merged contents
        // targetFile.setContents(mergedContents, true, false, new
        // NullProgressMonitor());
        sourceInputStream.close();
        targetInputStream.close();
        targetOutputStream.close();
    }
}
