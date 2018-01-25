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
import java.io.FileNotFoundException;
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

    public static void merge(File sourceFile, File targetFile) {
        JMerger merger = getJMerger();

        FileInputStream sourceInputStream = null;
        FileInputStream targetInputStream = null;
        OutputStream targetOutputStream = null;
        try {
            // set source
            sourceInputStream = new FileInputStream(sourceFile);
            JCompilationUnit inputCompilationUnit = merger
                    .createCompilationUnitForInputStream(sourceInputStream);
            merger.setSourceCompilationUnit(inputCompilationUnit);

            // set target
            targetInputStream = new FileInputStream(targetFile);
            JCompilationUnit targetCompilationUnit = merger
                    .createCompilationUnitForInputStream(targetInputStream);
            merger.setTargetCompilationUnit(targetCompilationUnit);

            // merge source and target
            merger.merge();

            // write merged contents
            targetOutputStream = new FileOutputStream(targetFile);
            byte[] bytes = merger.getTargetCompilationUnit().getContents().getBytes();
            targetOutputStream.write(bytes, 0, bytes.length);
        } catch (FileNotFoundException fnfe) {
            CXFCorePlugin.log(fnfe);
        } catch (IOException ioe) {
            CXFCorePlugin.log(ioe);
        } finally {
            try {
                if (sourceInputStream != null) {
                    sourceInputStream.close();
                }
                if (targetInputStream != null) {
                    targetInputStream.close();
                }
                if (targetOutputStream != null) {
                    targetOutputStream.close();
                }
            } catch (IOException ioe) {
                CXFCorePlugin.log(ioe);
            }
        }
    }
}
