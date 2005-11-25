package org.eclipse.wst.ws.tests.unittest;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.model.v10.registry.Name;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.registry.RegistryService;
import org.eclipse.wst.ws.internal.registry.UDDIRegistryService;

public class RegistryTests extends TestCase
{
	public static Test suite ()
	{
		return new TestSuite(RegistryTests.class);
	}

	public void testTaxonomySave ()
	{
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

			//TODO: Need a better output filename.
			System.out.println("Saving the Taxonomy model");
			registryService.saveTaxonomy(new URL("file:/c:/temp/taxonomy.xml"),taxonomy);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	public void testRegistrySave ()
	{
		try
		{
			System.out.println("Creating RegistryService, UDDIRegistryService and Registry");
			RegistryService registryService = RegistryService.instance();
			UDDIRegistryService uddiRegistryService = UDDIRegistryService.instance();			
			UDDIRegistry uddiRegistry = uddiRegistryService.newUDDIRegistry();

			System.out.println("Building a simple Registry model");
			uddiRegistry.setId("my.registry");
			uddiRegistry.setVersion("2.0");
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

			//TODO: Need a better output filename.
			System.out.println("Saving the Registry model");
			registryService.saveRegistry(new URL("file:/c:/temp/registry.xml"),uddiRegistry);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}
