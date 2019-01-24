/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsil.datamodel;

import java.util.Vector;
import org.apache.wsil.Abstract;
import org.apache.wsil.Service;
import org.apache.wsil.ServiceName;
import org.eclipse.wst.ws.internal.datamodel.Model;

/**
* The data model element that represents 
* a WSIL Service
*/
public class WsilServiceElement extends WsilCommonElement
{
    protected Service service_;

    public WsilServiceElement(String name, Model model, Service service) {
        super(name, model);
        service_ = service;
    }

    public Vector getServiceNameLangs() {
        Vector v = new Vector();
        ServiceName[] serviceNames = service_.getServiceNames();
        for (int i = 0; i < serviceNames.length; i++) {
            v.add(serviceNames[i].getLang());
        }
        return v;
    }

    public Vector getServiceNames() {
        Vector v = new Vector();
        ServiceName[] serviceNames = service_.getServiceNames();
        for (int i = 0; i < serviceNames.length; i++) {
            v.add(serviceNames[i].getText());
        }
        return v;
    }

    public Vector getServiceAbstractLangs() {
        Vector v = new Vector();
        Abstract[] abstracts = service_.getAbstracts();
        for (int i = 0; i < abstracts.length; i++) {
            v.add(abstracts[i].getLang());
        }
        return v;
    }

    public Vector getServiceAbstracts() {
        Vector v = new Vector();
        Abstract[] abstracts = service_.getAbstracts();
        for (int i = 0; i < abstracts.length; i++) {
            v.add(abstracts[i].getText());
        }
        return v;
    }
}
