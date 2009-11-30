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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

public class MappingsTransformer implements Transformer {

    @SuppressWarnings("unchecked")
    public Object transform(Object value) {
        Map map = new HashMap();
        IPath path = new Path((String)value);
        IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        if (resource != null) {
            try {
                Properties props = new Properties();
                props.load(resource.getContents());
                map.putAll(props);
            } catch (IOException ioe)  {
                CXFCorePlugin.log(ioe);
            } catch (CoreException ce) {
                CXFCorePlugin.log(ce.getStatus());
            }
        }
        return map;
    }

}
