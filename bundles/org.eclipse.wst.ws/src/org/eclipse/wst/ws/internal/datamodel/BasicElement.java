/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.datamodel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class BasicElement implements Element
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  private String fName;
  private String fMUID;
  private Model fModel;
  private Hashtable fRels;
  private Hashtable fProperties;
  private Vector fListeners;

  public BasicElement ( String name, Model model )
  {
    fName = name;
    setMUID(model.makeMUID(name));
    model.addElement(this);
    fModel = model;
    fRels = new Hashtable();
    fListeners = new Vector();
    fProperties = new Hashtable();
    fProperties.put(Property.NAME,new BasicProperty(Property.NAME,fName));
  }

  public BasicElement ( String name, Element element, String outboundRelName, String inboundRelName )
  {
    this(name,element.getModel());
    connect(element,outboundRelName,inboundRelName);
  }

  /**
  * Returns a clone of this <code>Element</code>
  * of the same class and with the same name and model,
  * but with no Properties, Rels or Listeners except for
  * the {@link Property#NAME NAME} property.
  * @return Object A new Element.
  */
  public Element shallowClone ()
  {
    return new BasicElement(fName,fModel);
  }

  /**
  * Returns a clone of this <code>Element</code>
  * of the same class and with the same name and model
  * and with a copy of the original Element's Properties.
  * The Rels and Listeners are not cloned.
  * @return Element A new Element.
  */
  public final Element deepClone ()
  {
    Element newElement = shallowClone();
    Enumeration e = getProperties();
    while (e.hasMoreElements())
    {
      Property property = (Property)e.nextElement();
      Property newProperty = (Property)property.shallowClone();
      newElement.setProperty(newProperty);
    }
    return newElement;
  }

  /**
  * Model-View-Controller: All property changes to the model
  * (such as those made by a wizard in the role of a Controller)
  * should yield events to which an interested party can listen
  * (such as a wizard in the role of a Viewer).
  * @param tml The listener to add.
  */
  public void addListener ( ElementListener listener )
  {
    fListeners.add(listener);
  }

  public Model getModel ()
  {
    return fModel;
  }

  public boolean isValid ()
  {
    return (fModel != null && fModel.containsElement(this));
  }

  public boolean remove ()
  {
    return fModel.removeElement(this);
  }

  public void setName ( String name )
  {
    fName = name;
    fProperties.put(Property.NAME,new BasicProperty(Property.NAME,fName));
  }

  public String getName ()
  {
    return fName;
  }

  /*
  * The getter for the MUID
  * The MUID is the Model Unique Identifier, made from the name and an
  * appended unique number 
  * @return String a unique id for this element within the context of the model 
  */
  public String getMUID()
  {
    return fMUID;
  }

  /*
  *
  *
  */
  public int getNumberID()
  {
    String number = getMUID().substring(getName().length());  
    return Integer.parseInt(number);
  }

  /*
  * Set the MUID which was made by the model
  * Should only be settable within this element
  * Set once upon creation, you can change the name
  * but the id will remain constant.
  */
  private void setMUID(String MUID)
  {
    fMUID = MUID;
  }
 
  public void setProperty ( Property property )
  {
    String propertyName = property.getName();
    Property oldProperty = getProperty(propertyName);
    fProperties.put(propertyName,property);
    if (oldProperty == null)
    {
      PropertyAddEvent event = new PropertyAddEvent(this,property);
      Enumeration e = fListeners.elements();
      while (e.hasMoreElements())
      {
        ((ElementListener)e.nextElement()).propertyAdded(event);
      }
    }
    else
    {
      Object oldValue = oldProperty.getValue();
      Object newValue = property.getValue();
      if (oldValue != newValue)
      {
        if (oldValue == null || newValue == null || (!oldValue.equals(newValue)))
        {
          PropertyChangeEvent event = new PropertyChangeEvent(this,property,oldProperty);
          Enumeration e = fListeners.elements();
          while (e.hasMoreElements())
          {
            ((ElementListener)e.nextElement()).propertyChanged(event);
          }
        }
      }
    }
    if (propertyName.equals(Property.NAME)) fName = property.getValue().toString();
  }

  public void setPropertyAsObject ( String name, Object value )
  {
    setProperty(new BasicProperty(name,value));
  }

  public void setPropertyAsString ( String name, String value )
  {
    setProperty(new BasicProperty(name,value));
  }

  public Property getProperty ( String name )
  {
    return (Property)fProperties.get(name);
  }

  public Object getPropertyAsObject ( String name )
  {
    Property property = getProperty(name);
    return (property == null ? null : property.getValue());
  }

  public String getPropertyAsString ( String name )
  {
    Property property = getProperty(name);
    return (property == null ? null : property.getValueAsString());
  }

  public Enumeration getProperties ()
  {
    return fProperties.elements();
  }

  public boolean connect ( Element targetElement, String outboundRelName, String inboundRelName )
  {
    if (fModel == null || fModel != targetElement.getModel()) return false;

    Element sourceElement = this;
    Rel outboundRel = sourceElement.getRel(outboundRelName);
    Rel inboundRel = targetElement.getRel(inboundRelName);
    Connection[] pair = BasicConnection.createPair(outboundRel,targetElement,inboundRel,sourceElement);
    outboundRel.addConnection(pair[BasicConnection.OUTBOUND]);
    inboundRel.addConnection(pair[BasicConnection.INBOUND]);
    RelAddEvent event = new RelAddEvent(pair);
    Enumeration eSource = getListeners();
    Enumeration eTarget = targetElement.getListeners();
    while (eSource.hasMoreElements())
    {
      ((ElementListener)eSource.nextElement()).relAdded(event);
    }

    while (eTarget.hasMoreElements())
    {
      ((ElementListener)eTarget.nextElement()).relAdded(event);
    }


    return true;
  }

  public boolean disconnect ( Element targetElement, String outboundRelName )
  {
    return disconnect(targetElement,getRel(outboundRelName));
  }

  public boolean disconnectRel ( String outboundRelName )
  {
    return disconnectRel(getRel(outboundRelName));
  }

  public boolean disconnectAll ()
  {
    if (fModel == null) return false;
    Enumeration e = fRels.elements();
    while (e.hasMoreElements())
    {
      Rel rel = (Rel)e.nextElement();
      disconnectRel(rel);
    }
    return true;
  }

  public Enumeration getElements ( String relName )
  {
    return getRel(relName).getTargetElements();
  }

  public int getNumberOfElements ( String relName )
  {
    return getRel(relName).getNumberOfTargetElements();
  }

  public Rel getRel ( String relName )
  {
    Rel rel = (Rel)fRels.get(relName);
    if (rel == null)
    {
      rel = new BasicRel(relName,this);
      fRels.put(relName,rel);
    }
    return rel;
  }

  public Enumeration getRels ()
  {
    return fRels.elements();
  }

  public Enumeration getListeners()
  {
    return fListeners.elements();
  }

  protected boolean disconnect ( Element targetElement, Rel outboundRel )
  {
    if (fModel == null || fModel != targetElement.getModel() || outboundRel == null) return false;
    Connection outboundConnection = outboundRel.getConnectionTo(targetElement);
    if (outboundConnection == null) return false;
    Connection inboundConnection = outboundConnection.getOpposingConnection();
    RelRemoveEvent event = new RelRemoveEvent(new Connection [] {outboundConnection,inboundConnection});
      Enumeration e = fListeners.elements();
      while (e.hasMoreElements())
      {
        ((ElementListener)e.nextElement()).relRemoved(event);
      }

    Enumeration eTarget = targetElement.getListeners();
    while (eTarget.hasMoreElements())
    {
      ((ElementListener)eTarget.nextElement()).relRemoved(event);
    }

    Rel inboundRel = inboundConnection.getRel();
    boolean done = outboundRel.removeConnection(outboundConnection);
    inboundRel.removeConnection(inboundConnection);
    return done;
  }

  protected boolean disconnectRel ( Rel outboundRel )
  {
    if (fModel == null || outboundRel == null) return false;
    Element[] elements = new Element[outboundRel.getNumberOfTargetElements()];
    int i = 0;
    Enumeration e = outboundRel.getTargetElements();
    while (e.hasMoreElements())
    {
      elements[i++] = (Element)e.nextElement();
    }
    while (i-- > 0)
    {
      disconnect(elements[i],outboundRel);
    }
    return true;
  }

  public String toString ()
  {
    return getName();
  }
}

