/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core.env.ant;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class BindingsTransformer implements Transformer {

    public Object transform(Object value) {
        List<String> bindingFilesList = new ArrayList<String>();
        String[] absolutePaths = value.toString().split(",");
        for (String absoulutePath : absolutePaths) {
            IPath path = new Path(absoulutePath);
            if (path.getFileExtension() != null && path.getFileExtension().equals("xml")) {
                bindingFilesList.add(absoulutePath);
            }
        }
        return bindingFilesList;
    }

}
