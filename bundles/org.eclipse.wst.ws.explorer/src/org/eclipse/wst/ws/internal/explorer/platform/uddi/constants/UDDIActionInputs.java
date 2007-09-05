/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.uddi.constants;

public class UDDIActionInputs
{
  // OpenRegistryAction
  public static final String REGISTRY_NAME = "registryName";
  public static final String INQUIRY_URL = "inquiryURL";
  public static final String PUBLISH_URL = "publishURL";
  public static final String REGISTRATION_URL = "registrationURL";
  public static final String CHECK_USER_DEFINED_CATEGORIES = "checkUserDefinedCategories";
  public static final String UDDI_USERNAME = "uddiUsername";
  public static final String UDDI_PASSWORD = "uddiPassword";

  // Fixed and user-defined categories
  public static final String CATEGORIES_DIRECTORY = "categoriesDirectory";
  public static final String CATEGORY_TMODEL_KEY = "categoryTModelKey";
  public static final String CATEGORY_FILENAME = "categoryFileName";
  public static final String USER_DEFINED_CATEGORIES = "userDefinedCategories";

  // Discovery URL use type
  public static final String DISCOVERY_URL_TYPE = "businessEntity";

  // Results (NODEID)
  public static final String NODEID_SERVICE_INTERFACE = "nodeId_ServiceInterface";
  public static final String NODEID_SERVICE = "nodeId_Service";
  public static final String NODEID_BUSINESS = "nodeId_Business";

  // RegFind..Actions/RegPublish..Actions
  public static final String QUERY_INPUT_OVERRIDE_ADD_QUERY_NODE = "overrideAddQueryNode";
  public static final String QUERY_NAME = "queryName";
  public static final String QUERY_ITEM = "queryItem";
  public static final int QUERY_ITEM_QUERIES = -1;
  public static final int QUERY_ITEM_BUSINESSES = 0;
  public static final int QUERY_ITEM_SERVICES = 1;
  public static final int QUERY_ITEM_SERVICE_INTERFACES = 2;
  public static final int QUERY_STYLE_SIMPLE = 0;
  public static final int QUERY_STYLE_ADVANCED = 1;
  public static final int QUERY_STYLE_UUID = 2;
  public static final String QUERY_INPUT_ADVANCED_OWNED = "advancedOwned";
  public static final String QUERY_INPUT_ADVANCED_PUBLISH_URL = "advancedPublishURL";
  public static final String QUERY_INPUT_ADVANCED_USERID = "advancedUserId";
  public static final String QUERY_INPUT_ADVANCED_PASSWORD = "advancedPassword";
  public static final String QUERY_INPUT_ADVANCED_NAME_LANGUAGE = "advancedNameLanguage";
  public static final String QUERY_INPUT_ADVANCED_NAME = "advancedName";
  public static final String QUERY_INPUT_ADVANCED_IDENTIFIER_TYPE = "advancedIdentifierType";
  public static final String QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_NAME = "advancedIdentifierKeyName";
  public static final String QUERY_INPUT_ADVANCED_IDENTIFIER_KEY_VALUE = "advancedIdentifierKeyValue";
  public static final String QUERY_INPUT_ADVANCED_CATEGORY_TYPE = "advancedCategoryType";
  public static final String QUERY_INPUT_ADVANCED_CATEGORY_KEY_NAME = "advancedCategoryKeyName";
  public static final String QUERY_INPUT_ADVANCED_CATEGORY_KEY_VALUE = "advancedCategoryKeyValue";
  public static final String QUERY_INPUT_ADVANCED_DISCOVERYURL = "advancedDiscoveryURL";
  public static final String QUERY_INPUT_ADVANCED_FINDQUALIFIER = "advancedFindQualifier";
  public static final String QUERY_INPUT_ADVANCED_SORT_BY_NAME = "advancedSortByName";
  public static final String QUERY_INPUT_ADVANCED_SORT_BY_DATE = "advancedSortByDate";
  public static final String QUERY_INPUT_ADVANCED_MAX_SEARCH_SET = "advancedMaxSearchSet";
  public static final int QUERY_MAX_SEARCH_SET = 100;
  public static final String QUERY_INPUT_ADVANCED_MAX_RESULTS = "advancedMaxResults";
  public static final int QUERY_MAX_RESULTS = 10;

  public static final String QUERY_STYLE_BUSINESSES = "queryStyleBusinesses";
  public static final String QUERY_INPUT_SIMPLE_BUSINESS_NAME = "simpleBusinessName";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_OWNED = "advancedBusinessOwned";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_NAMES = "advancedBusinessNames";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_IDENTIFIERS = "advancedBusinessIdentifiers";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_CATEGORIES = "advancedBusinessCategories";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_DISCOVERYURLS = "advancedBusinessDiscoveryURLs";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES = "advancedBusinessServiceInterfaces";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_SERVICE_INTERFACES_COPY = "advancedBusinessServiceInterfacesCopy";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_TMODELBAG = "advancedBusinessTModelBag";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_FINDQUALIFIERS = "advancedBusinessFindQualifiers";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_MAX_SEARCH_SET = "advancedBusinessMaxSearchSet";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_MAX_RESULTS = "advancedBusinessMaxResults";

  public static final String QUERY_STYLE_SERVICES = "queryStyleServices";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_NAME = "simpleServiceName";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_OWNED = "advancedServiceOwned";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_BUSINESS = "advancedServiceBusiness";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_BUSINESS_COPY = "advancedServiceBusinessCopy";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_SERVICE_PROVIDER = "advancedServiceServiceProvider";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_NAMES = "advancedServiceNames";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_CATEGORIES = "advancedServiceCategories";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES = "advancedServiceServiceInterfaces";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_SERVICE_INTERFACES_COPY = "advancedServiceServiceInterfacesCopy";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_TMODEL = "advancedServiceTModel";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_TMODELBAG = "advancedServiceTModelBag";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_FINDQUALIFIERS = "advancedServiceFindQualifiers";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_MAX_SEARCH_SET = "advancedServiceMaxSearchSet";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_MAX_RESULTS = "advancedServiceMaxResults";

  public static final String QUERY_STYLE_SERVICE_INTERFACES = "queryStyleServiceInterfaces";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_NAME = "simpleServiceInterfaceName";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_USE_SERVICE = "advancedUseService";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE = "advancedServiceInterfaceService";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_SERVICE_COPY = "advancedServiceInterfaceServiceCopy";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_BUSINESS_SERVICE = "advancedServiceInterfaceBusinessService";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_OWNED = "advancedServiceInterfaceOwned";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_NAME = "advancedServiceInterfaceName";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_IDENTIFIERS = "advancedServiceInterfaceIdentifiers";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_CATEGORIES = "advancedServiceInterfaceCategories";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_FINDQUALIFIERS = "advancedServiceInterfaceFindQualifiers";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_SEARCH_SET = "advancedServiceInterfaceMaxSearchSet";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_MAX_RESULTS = "advancedServiceInterfaceMaxResults";
  
  public static final String QUERY_INPUT_ADVANCED_REGISTRY_NAME = "advancedRegistryName";

  public static final String QUERY_INPUT_UUID_KEY = "uuidKey";
  public static final String QUERY_INPUT_UUID_BUSINESS_KEY = "uuidBusinessKey";
  public static final String QUERY_INPUT_UUID_SERVICE_KEY = "uuidServiceKey";
  public static final String QUERY_INPUT_UUID_SERVICE_INTERFACE_KEY = "uuidServiceInterfaceKey";
  
  public static final String QUERY_OUTPUT_SAVED_TMODEL = "savedTModel";

  // Sub Queries:
  public static final String SUBQUERIES_PROPERTIES = "subQueriesProperties";
  public static final String SUBQUERY_KEY = "subQueryKey";
  public static final String NEW_SUBQUERY_INITIATED = "newSubQueryInitiated";
  public static final String NEW_SUBQUERY_QUERY_ITEM = "newSubQueryQueryItem";
  public static final String SELECTED_NODEIDS = "selectedNodeIds";
  public static final String SUBQUERY_LIST_KEY = "subQueryListKey";
  public static final String SUBQUERY_LIST_ITEMID = "subQueryListItemId";
  public static final String SHOW_RESULTS_TARGET = "showResultsTarget";
  public static final String SUBQUERY_GET = "subQueryGet";

  // RegPublish..Actions (some constants from the RegFind...Actions will be reused.
  public static final String QUERY_INPUT_SIMPLE_BUSINESS_DESCRIPTION = "simpleBusinessDescription";

  public static final String QUERY_INPUT_ADVANCED_DESCRIPTION_LANGUAGE = "advancedDescriptionLanguage";
  public static final String QUERY_INPUT_ADVANCED_DESCRIPTION = "advancedDescription";
  public static final String QUERY_INPUT_ADVANCED_BUSINESS_DESCRIPTIONS = "advancedBusinessDescriptions";

  public static final String QUERY_INPUT_WSDL_URL = "wsdlURL";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_WSDL_URL = "simpleServiceInterfaceWSDLURL";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_INTERFACE_DESCRIPTION = "simpleServiceInterfaceDescription";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_WSDL_URL = "advancedServiceInterfaceWSDLURL";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_INTERFACE_DESCRIPTIONS = "advancedServiceInterfaceDescriptions";

  public static final String QUERY_INPUT_SIMPLE_SERVICE_BUSINESS = "simpleServiceBusiness";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_BUSINESS_COPY = "simpleServiceBusinessCopy";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_SERVICE_PROVIDER = "simpleServiceServiceProvider";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_WSDL_URL = "simpleServiceWSDLURL";
  public static final String QUERY_INPUT_SIMPLE_SERVICE_DESCRIPTION = "simpleServiceDescription";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_WSDL_URL = "advancedServiceWSDLURL";
  public static final String QUERY_INPUT_ADVANCED_SERVICE_DESCRIPTIONS = "advancedServiceDescriptions";

  // WSDL Browser
  public static final String PROJECT = "project";
  public static final String WSDL_TYPE = "wsdlType";
  public static final int WSDL_TYPE_SERVICE_INTERFACE = 0;
  public static final int WSDL_TYPE_SERVICE = 1;
  public static final String QUERY_INPUT_WEBPROJECT_WSDL_URL = "webProjectWSDLURL";
  public static final String QUERY_INPUT_FAVORITE_WSDL_URL = "favoriteWSDLURL";

  // Details
  public static final String LATEST_OBJECT = "latestObject";
  public static final String DISCOVERYURL_MODIFIED = "discoveryURLModified";
  public static final String DISCOVERYURL_VIEWID = "discoveryURLViewId";
  public static final String NAME_MODIFIED = "nameModified";
  public static final String NAME_VIEWID = "businessViewId";
  public static final String DESCRIPTION_MODIFIED = "descriptionModified";
  public static final String DESCRIPTION_VIEWID = "descriptionViewId";
  public static final String IDENTIFIER_MODIFIED = "identifierModified";
  public static final String IDENTIFIER_VIEWID = "identifierViewId";
  public static final String CATEGORY_MODIFIED = "categoryModified";
  public static final String CATEGORY_VIEWID = "categoryViewId";
  public static final String WSDL_URL_MODIFIED = "wsdlURLModified";
  public static final String PUBLISH_ACTION = "publishAction";
  public static final String LATEST_BUSINESS = "latestBusiness";

  // Refresh action
  public static final String REFRESH_NODE = "refreshNode";

  // Manage PublisherAssertions
  // "To" means from another business to my owning business
  public static final int DIRECTION_TO = 0;
  // "From" means from my owning business to another business
  public static final int DIRECTION_FROM = 1;
  public static final String PUBLISHER_ASSERTIONS_VIEWID = "publisherAssertionsViewId";
  public static final String PUBLISHER_ASSERTIONS_SELECTED_BUS_ID = "publisherAssertionsSelectedBusID";
  public static final String PUBLISHER_ASSERTIONS_DIRECTION = "publisherAssertionsDirection";
  public static final String QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS = "queryInputAddPublisherAssertions";
  public static final String QUERY_INPUT_ADD_PUBLISHER_ASSERTIONS_COPY = "queryInputAddPublisherAssertionsCopy";
  public static final String QUERY_INPUT_EXISTING_PUBLISHER_ASSERTIONS = "queryInputExistingPublisherAssertions";
  public static final String PUBLISHER_ASSERTIONS_TYPE = "publisherAssertionsType";
  public static final String PUBLISHER_ASSERTIONS_TYPE_PARENT_CHILD = "parent-child";
  public static final String PUBLISHER_ASSERTIONS_TYPE_PEER_TO_PEER = "peer-peer";
  public static final String PUBLISHER_ASSERTIONS_TYPE_IDENTITY = "identity";

  // Manage Referenced Services
  public static final String QUERY_INPUT_REFERENCED_SERVICES = "referencedServices";
  public static final String QUERY_INPUT_SERVICES = "services";
  public static final String MANAGE_REFERENCED_SERVICES_OPERATION = "managedReferencedServicesOperation";
  public static final int MANAGE_REFERENCED_SERVICES_OPERATION_ADD = 0;
  public static final int MANAGE_REFERENCED_SERVICES_OPERATION_REMOVE = 1;
  public static final String REFERENCED_SERVICE_SELECT_STATE = "referencedServiceSelectState";
  
  // ResizeUDDIFrameAction
  public static final String FRAMESET_COLS_PERSPECTIVE_CONTENT = "framesetColsPerspectiveContent";
  public static final String FRAMESET_ROWS_ACTIONS_CONTAINER = "framesetRowsActionsContainer";
}
