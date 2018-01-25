/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.datamodel;

public class BasicConnection implements Connection
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private Rel fRel;
  private Element fElement;
  private Connection fOpposingConnection;

  public static final int OUTBOUND = 0;
  public static final int INBOUND = 1;

  public static Connection[] createPair ( Rel outboundRel, Element targetElement, Rel inboundRel, Element sourceElement )
  {
    BasicConnection outboundConnection = new BasicConnection(outboundRel,targetElement);
    BasicConnection inboundConnection = new BasicConnection(inboundRel,sourceElement);
    outboundConnection.fOpposingConnection = inboundConnection;
    inboundConnection.fOpposingConnection = outboundConnection;
    return new Connection[] {outboundConnection,inboundConnection};
  }

  public BasicConnection ( Rel rel, Element element )
  {
    fRel = rel;
    fElement = element;
    fOpposingConnection = null;
  }

  public Rel getRel ()
  {
    return fRel;
  }

  public Element getElement ()
  {
    return fElement;
  }

  public Connection getOpposingConnection ()
  {
    return fOpposingConnection;
  }

  public String toString ()
  {
    return fElement.getName();
  }
}

