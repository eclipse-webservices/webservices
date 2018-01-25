/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.unittest;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.model.v10.registry.Name;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.registry.IRegistryManager;
import org.eclipse.wst.ws.internal.registry.RegistryService;
import org.eclipse.wst.ws.internal.registry.UDDIRegistryService;

public class RegistryTests extends TestCase
{
	public static Test suite ()
	{
		return new TestSuite(RegistryTests.class);
	}

	/**
	 * Creates a new Taxonomy model and passes it to
	 * <code>RegistryService.saveTaxonomy()</code>
	 * where the model is persisted to a platform
	 * specified temporary filesystem location.
	 */
	public void test_RegistryService_saveTaxonomy ()
	{
		System.out.println("STARTING test_RegistryService_saveTaxonomy");
		try
		{
			System.out.println("Creating RegistryService and Taxonomy");
			RegistryService registryService = RegistryService.instance();			
			Taxonomy taxonomy = registryService.newTaxonomy();

			System.out.println("Building a simple Taxonomy model");
			taxonomy.setId("my.taxonomy");
			taxonomy.setName("My Taxonomy");
			EList list = taxonomy.getCategory();
			Category category1 = TaxonomyFactory.eINSTANCE.createCategory();
			category1.setName("My first category");
			category1.setCode("1");
			Category category2 = TaxonomyFactory.eINSTANCE.createCategory();
			category2.setName("My second category");
			category2.setCode("2");
			Category category3 = TaxonomyFactory.eINSTANCE.createCategory();
			category3.setName("My third category");
			category3.setCode("2.1");
			category2.getCategory().add(category3);
			list.add(category1);
			list.add(category2);

			File file = File.createTempFile("taxonomy.",".xml");
			System.out.println("Saving the Taxonomy model to "+file.getCanonicalPath());
			registryService.saveTaxonomy(file.toURL(),taxonomy);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			fail("Unexpected Throwable ["+exc.getMessage()+"]");
		}
		finally
		{
			System.out.println("FINISHED test_RegistryService_saveTaxonomy\n");
		}
	}

	/**
	 * Creates a new UDDI Registry model and passes it to
	 * <code>RegistryService.saveRegistry()</code>
	 * where the model is persisted to a platform
	 * specified temporary filesystem location.
	 */
	public void test_RegistryService_saveRegistry ()
	{
		System.out.println("STARTING test_RegistryService_saveRegistry");
		try
		{
			System.out.println("Creating RegistryService, UDDIRegistryService and Registry");
			RegistryService registryService = RegistryService.instance();
			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();			
			UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();

			System.out.println("Building a simple Registry model");
			uddiRegistry.setId("my.registry");
			uddiRegistry.setVersion("2.0");
			uddiRegistry.setDiscoveryURL("http://some.discovery.url");
			uddiRegistry.setPublicationURL("http://some.publication.url");
			uddiRegistry.setDefaultLogin("hockeyfanatic");
			Name name = RegistryFactory.eINSTANCE.createName();
			name.setValue("My Registry");
			uddiRegistry.getName().add(name);
			Taxonomy taxonomyRef = registryService.newTaxonomy();
			taxonomyRef.setRef("my.taxonomy");
			Taxonomies t = uddiRegistry.getTaxonomies();
			if (t == null)
			{
				t = UDDIRegistryFactory.eINSTANCE.createTaxonomies();
				uddiRegistry.setTaxonomies(t);
			}
			EList e = t.getTaxonomy();
			e.add(taxonomyRef);

			File file = File.createTempFile("registry.",".xml");
			System.out.println("Saving the Registry model to "+file.getCanonicalPath());
			registryService.saveRegistry(file.toURL(),uddiRegistry);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			fail("Unexpected Throwable ["+exc.getMessage()+"]");
		}
		finally
		{
			System.out.println("FINISHED test_RegistryService_saveRegistry\n");
		}
	}

	/**
	 * Creates a new UDDI Registry model and a new Taxonomy
	 * model, associates the latter with the former, and
	 * drives <code>IRegistryManager.saveRegistry()</code>
	 * to save the models and the index document to the
	 * preferred location of the default registry manager.
	 */
	public void test_RegistryManager_saveRegistry()
	{
		System.out.println("STARTING test_RegistryManager_saveRegistry");
		try {
			System.out.println("Creating RegistryService and Getting RegistryManager");	
			RegistryService registryService = RegistryService.instance();	
			IRegistryManager regManager = registryService.getDefaultRegistryManager();
			System.out.println("The location of the default index is " + regManager.getURL());

			//build the hockey taxonomy
			System.out.println("Building the hockey Taxonomy model");
			Taxonomy hockeyTaxonomy = registryService.newTaxonomy();
			hockeyTaxonomy.setId("hockey.taxonomy");
			hockeyTaxonomy.setName("hockey Taxonomy");
			EList list = hockeyTaxonomy.getCategory();
			Category category1 = TaxonomyFactory.eINSTANCE.createCategory();
			Category category4 = TaxonomyFactory.eINSTANCE.createCategory();
			category4.setName("Pro");
			category4.setCode("4");
			Category category5 = TaxonomyFactory.eINSTANCE.createCategory();
			category5.setName("AHL");
			category5.setCode("4.1");
			Category category6 = TaxonomyFactory.eINSTANCE.createCategory();
			category6.setName("NHL");
			category6.setCode("4.2");
			category1.setName("Junior");
			category1.setCode("1");
			Category category2 = TaxonomyFactory.eINSTANCE.createCategory();
			category2.setName("Midget");
			category2.setCode("2");
			Category category3 = TaxonomyFactory.eINSTANCE.createCategory();
			category3.setName("Bantam");
			category3.setCode("3");
			category4.getCategory().add(category5);
			category4.getCategory().add(category6);
			list.add(category1);
			list.add(category2);
			list.add(category3);
			list.add(category4);
			
			//save the hockey taxonomy
			Taxonomy hockeyTaxonomyRef = regManager.saveTaxonomy(hockeyTaxonomy);

			//make the hockey registry
			System.out.println("Building the hockey Registry model");
			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();			
			UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();
			uddiRegistry.setId("hockey.registry");
			uddiRegistry.setVersion("2.0");
			uddiRegistry.setDefaultLogin("hockeyfanatic");
			uddiRegistry.setDiscoveryURL("http://some.discovery.url");
			uddiRegistry.setPublicationURL("http://some.publication.url");
			Name name = RegistryFactory.eINSTANCE.createName();
			name.setValue("Hockey Registry");
			uddiRegistry.getName().add(name);

			// Add the reference to the taxonomy (saved above) to the registry.  
			uddiRegistryService.addTaxonomy(uddiRegistry,hockeyTaxonomyRef);

			/*
			UDDITaxonomyFinder UITFinder = new UDDITaxonomyFinder();
			regManager.addTaxonomyFinder(uddiRegistry.getClass().getName(),UITFinder);
			*/
			// Save the thing.
			System.out.println("Saving the hockey Registry model");
			regManager.saveRegistry(uddiRegistry);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			fail("Unexpected Throwable ["+exc.getMessage()+"]");
		}
		finally
		{
			System.out.println("FINISHED test_RegistryManager_saveRegistry\n");
		}
	}

	/**
	 * Loads the registry and taxonomy in the index created in
	 * test_RegistryManager_saveRegistry, then asserts that the
	 * content of the model is what we expect.
	 */
	public void test_RegistryManager_loadRegistry()
	{
		System.out.println("STARTING test_RegistryManager_loadRegistry");
		try {
			// Setup
			System.out.println("Creating RegistryService and Getting RegistryManager");	
			RegistryService registryService = RegistryService.instance();	
			IRegistryManager regManager = registryService.getDefaultRegistryManager();
			
			// Get the one registry URI we expect, and load the registry.
			String[] registries = regManager.getRegistryURIs();
			assertEquals("Unexpected number of registries "+registries.length,registries.length,1);
			String uri = registries[0];
			System.out.println("Loading registry " + uri);
			Registry registry = regManager.loadRegistry(uri);
			System.out.println("Found registry " + registry.getId());
			assertEquals("Did not find hockey registry","hockey.registry",registry.getId());
			
			// It's supposed to be a UDDI registry. Verify it and check its values.
			assertTrue("Registry is not a UDDIRegistry",(registry instanceof UDDIRegistry));
			UDDIRegistry uddiRegistry = (UDDIRegistry)registry;
			assertEquals(uddiRegistry.getVersion(),"2.0");
			assertEquals(uddiRegistry.getDefaultLogin(),"hockeyfanatic");
			assertEquals(uddiRegistry.getDiscoveryURL(),"http://some.discovery.url");
			assertEquals(uddiRegistry.getPublicationURL(),"http://some.publication.url");
			
			// Load the taxonomies used the UDDI registry.
			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();
			Taxonomy[] taxonomies = regManager.loadTaxonomies(uddiRegistryService.getTaxonomyURIs(uddiRegistry));
			assertEquals("Unexpected number of taxonomies "+taxonomies.length,taxonomies.length,1);
			Taxonomy taxonomy = taxonomies[0];
			System.out.println("Found taxonomy " + taxonomy.getId());
			Assert.assertEquals("Did not find hockey taxonomy","hockey.taxonomy",taxonomy.getId());
			
			// Dump out the values (no assertions here).
			EList cList = taxonomy.getCategory();
			for(int i = 0;i<cList.size();i++){
				Category category = (Category)cList.get(i);
				System.out.println("Found category " + category.getName());
				EList c2List = category.getCategory();
				for(int j = 0;j<c2List.size();j++){
					Category iCategory = (Category)c2List.get(j);
					System.out.println("Found subcategory " + iCategory.getName());
				}
			}					
		}
		catch(Exception t)
		{
			t.printStackTrace();
			fail("Unexpected Throwable ["+t.getMessage()+"]");
		}
		finally
		{
			System.out.println("FINISHED test_RegistryManager_loadRegistry\n");
		}
	}
}
