package edu.usfca.cs272.tests.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import edu.usfca.cs272.tests.BuildIndexTests;
import edu.usfca.cs272.tests.CrawlPageTests;
import edu.usfca.cs272.tests.SearchExactTests;
import edu.usfca.cs272.tests.SearchPartialTests;
import edu.usfca.cs272.tests.ThreadBuildTests;

/**
 * Tests that next project code is not in the current project. This class should
 * not be run directly; it is run by GitHub Actions only.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class ProjectNextTests {
	/** Message output when a test fails. */
	public static final String debug = "You must place functionality for the next project in a separate branch. It should not be in the current version of your project in the main branch!";

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws IOException if an IO error occurs
	 */
	@Test
	@Tag("next-v1.0")
	public void testIndexOutput() throws IOException {
		runJUnitTest(BuildIndexTests.DirectoryTests.class, "testSimple");
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws IOException if an IO error occurs
	 */
	@Test
	@Tag("next-v1.0")
	@Tag("next-v1.1")
	@Tag("next-v1.2")
	@Tag("next-v1.3")
	@Tag("next-v1.4")
	public void testExactResultOutput() throws IOException {
		runJUnitTest(SearchExactTests.InitialTests.class, "testSimpleSimple");
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws IOException if an IO error occurs
	 */
	@Test
	@Tag("next-v2.0")
	public void testPartialResultOutput() throws IOException {
		runJUnitTest(SearchPartialTests.InitialTests.class, "testSimpleSimple");
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws IOException if an IO error occurs
	 */
	@Test
	@Tag("next-v2.1")
	@Tag("next-v2.2")
	@Tag("next-v2.3")
	@Tag("next-v2.4")
	public void testThreadIndexOutput() throws IOException {
		runJUnitTest(ThreadBuildTests.ApproachTests.class, "testIndex");
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws IOException if an IO error occurs
	 */
	@Test
	@Tag("next-v3.0")
	@Tag("next-v3.1")
	@Tag("next-v3.2")
	@Tag("next-v3.3")
	@Tag("next-v3.4")
	public void testCrawlPageIndexOutput() throws IOException {
		runJUnitTest(CrawlPageTests.InitialTests.class, "testHello");
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws Exception if an error occurs
	 */
	@Test
	@Tag("past-v1")
	@Tag("time-v1.0")
	@Tag("time-v1.1")
	@Tag("time-v1.2")
	@Tag("time-v1.3")
	@Tag("time-v1.4")
	@Tag("time-v2.0")
	@Tag("time-v2.1")
	@Tag("time-v2.2")
	@Tag("time-v2.3")
	@Tag("time-v2.4")
	@Tag("time-v4.0")
	@Tag("next-v4.0")
	@Tag("next-v5.0")
	@Tag("next-v5.1")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	public void pass() throws Exception {
		// No next tests for these releases!
	}

	/**
	 * Makes sure tests fail for future projects that are not yet supported.
	 */
	@Test
	@Tag("time-v4.1")
	@Tag("time-v4.2")
	@Tag("next-v4.1")
	@Tag("next-v4.2")
	@Tag("test-v4.1")
	@Tag("test-v4.2")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	public void fail() {
		Assertions.fail();
	}

	/**
	 * Runs a JUnit test. Invokes the test through JUnit so that all setup and
	 * teardown steps are still run (rather than calling the method directly).
	 *
	 * @param testClass the test class with the test method to run
	 * @param testMethod the test method to run (including parameter types)
	 */
	public static void runJUnitTest(Class<?> testClass, String testMethod) {
		var request = LauncherDiscoveryRequestBuilder.request()
				.selectors(DiscoverySelectors.selectMethod(testClass, testMethod))
				.build();

		var launcher = LauncherFactory.create();
		var listener = new SummaryGeneratingListener();

		Logger logger = Logger.getLogger("org.junit.platform.launcher");
		logger.setLevel(Level.SEVERE);

		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);

		listener.getSummary().printTo(new PrintWriter(System.out));

		long testCount = listener.getSummary().getTestsFoundCount();
		String testText = "Unable to find JUnit test. Reach out to instructor for assistance.";

		long failCount = listener.getSummary().getTotalFailureCount();
		String failText = "You should NOT pass the tests for future projects in this release!";

		assert 1 == testCount : testText;
		Assertions.assertEquals(1, failCount, failText);
	}
}
