/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.datamodel;

import org.uddi4j.datatype.assertion.PublisherAssertion;
import org.uddi4j.util.KeyedReference;

import org.eclipse.wst.ws.internal.explorer.platform.datamodel.ListElement;

public class PublisherAssertionElement {

  // owningBusinessKey_ is the key of the business element that owns this
  // publisherAssertionElement
  private String fromKey_;
  private String toKey_;
  private String owningBusinessKey_;

  // the other business that the containing business is asserted from or to
  private ListElement sp_;
  private int subQueryItemId_;

  // status of this publisherAssertion
  private String status_;
  private KeyedReference keyedReference_;

  public PublisherAssertionElement(String fromKey, String toKey, String owningBusinessKey, ListElement sp, int subQueryItemId, String status, KeyedReference keyedRef) {
    fromKey_ = fromKey;
    toKey_ = toKey;
    owningBusinessKey_ = owningBusinessKey;
    sp_ = sp;
    subQueryItemId_ = subQueryItemId;
    status_ = status;
    keyedReference_ = keyedRef;
  }

  public void setFromKey(String fromKey) {
    fromKey_ = fromKey;
  }

  public String getFromKey() {
    return fromKey_;
  }

  public void setToKey(String toKey) {
    toKey_ = toKey;
  }

  public String getToKey() {
    return toKey_;
  }

  public void setOwningBusinessKey(String key) {
    owningBusinessKey_ = key;
  }

  public String getOwningBusinessKey() {
    return owningBusinessKey_;
  }

  public void setServiceProvider(ListElement sp) {
    sp_ = sp;
  }

  public ListElement getServiceProvider() {
    return sp_;
  }

  public void setSubQueryItemId(int id) {
    subQueryItemId_ = id;
  }

  public int getSubQueryItemId() {
    return subQueryItemId_;
  }

  public void setStatus(String status) {
    status_ = status;
  }

  public String getStatus() {
    return status_;
  }

  public void setKeyedRef(KeyedReference keyedRef) {
    keyedReference_ = keyedRef;
  }

  public KeyedReference getKeyedRef() {
    return keyedReference_;
  }

  public PublisherAssertion getPublisherAssertion() {
    if (fromKey_ == null ||
        toKey_ == null ||
        keyedReference_ == null) {
      return null;
    }

    PublisherAssertion pubAssertion = new PublisherAssertion(fromKey_, toKey_, keyedReference_);
    return pubAssertion;
  }

}
