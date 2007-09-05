/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.uddi.constants;

public class UDDIModelConstants
{
  // Registries under UDDIMain.
  public static final String REL_REGISTRIES = "registries";

  // Executed Queries folder under a registry.
  public static final String REL_QUERIES_PARENT = "queriesParent";

  // Published Businesses folder under a registry.
  public static final String REL_PUBLISHED_BUSINESSES_PARENT = "publishedBusinessesParent";

  // Published Services folder under a registry.
  public static final String REL_PUBLISHED_SERVICES_PARENT = "publishedServicesParent";

  // Published Service interfaces folder under a registry.
  public static final String REL_PUBLISHED_SERVICE_INTERFACES_PARENT = "publishedServiceInterfacesParent";

  // Queries under the Queries folder.
  public static final String REL_QUERIES = "queries";
  public static final String INITIAL_RESULTS = "initialResults";
  public static final String QUERY_TYPE = "queryType";
  public static final String SUBQUERY_TRANSFER_TARGET = "subQueryTransferTarget";

  // Objects under a Query node.
  public static final String REL_QUERY_RESULTS = "queryResults";

  // Categories
  public static final String REL_SUBCATEGORIES = "subCategories";

  // Published items inside the Published items folder.
  public static final String REL_PUBLISHED_ITEMS = "publishedItems";

  // Businesses
  public static final String IS_BUSINESS_OWNED = "isBusinessOwned";
}
