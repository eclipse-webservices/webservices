/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.common;

import java.util.Set;

public class FacetMatcher
{
  private boolean match;
  private Set facetsTested;
  private Set facetsThatMatched;
  private Set facetsToAdd;
  
  public Set getFacetsThatMatched()
  {
    return facetsThatMatched;
  }
  public void setFacetsThatMatched(Set facetsThatMatched)
  {
    this.facetsThatMatched = facetsThatMatched;
  }
  public Set getFacetsToAdd()
  {
    return facetsToAdd;
  }
  public void setFacetsToAdd(Set facetsToAdd)
  {
    this.facetsToAdd = facetsToAdd;
  }
  public boolean isMatch()
  {
    return match;
  }
  public void setMatch(boolean match)
  {
    this.match = match;
  }
  public Set getFacetsTested()
  {
    return facetsTested;
  }
  public void setFacetsTested(Set facetsTested)
  {
    this.facetsTested = facetsTested;
  }
  
  

  
  

}
