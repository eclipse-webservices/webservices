package org.eclipse.jst.ws.jaxws.dom.integration.tests.util;

import java.util.Collection;

import org.eclipse.jst.ws.jaxws.dom.integration.internal.util.LoadingWsProjectNodesCollector;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LoadingWsProjectNodesCollectorTest extends MockObjectTestCase
{
	private TreeItem parentItem;
	private TreeItem childItem;
	private Mock<ILoadingWsProject> loadingProject;
	private Shell treeShell;
	private LoadingWsProjectNodesCollector collector;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		collector = new LoadingWsProjectNodesCollector();
		loadingProject = mock(ILoadingWsProject.class);

		treeShell = new Shell();
		final Tree parentTree = new Tree(treeShell, SWT.NONE);
		parentItem = new TreeItem(parentTree, SWT.NONE);
		childItem = new TreeItem(parentItem, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		treeShell.dispose();
	}

	public void testLoadingProjectAsParent()
	{
		parentItem.setData(loadingProject.proxy());
		childItem.setData(new Object());
		verifyFoundItem(parentItem);
	}

	public void testLoadingProjectAsChild()
	{
		parentItem.setData(new Object());
		childItem.setData(loadingProject.proxy());
		verifyFoundItem(childItem);
	}

	public void testStopCollectingOnWebServiceProject()
	{
		parentItem.setData(mock(IWebServiceProject.class).proxy());
		childItem.setData(loadingProject.proxy());
		assertEquals("Collector did not stop when IWebServiceProject encoutered", 0, getLoadingProjectItems().size());
	}

	private void verifyFoundItem(final TreeItem expectedItem)
	{
		final Collection<TreeItem> items = getLoadingProjectItems();
		assertEquals("One item expected", 1, items.size());
		assertSame("Unexpected item", expectedItem, items.iterator().next());
	}

	private Collection<TreeItem> getLoadingProjectItems()
	{
		return collector.getLoadingWsProjects(new TreeItem[] { parentItem });
	}
}
