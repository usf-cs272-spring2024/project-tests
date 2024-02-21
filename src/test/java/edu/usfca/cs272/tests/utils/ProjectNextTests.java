package edu.usfca.cs272.tests.utils;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import edu.usfca.cs272.tests.BuildIndexTests;
import edu.usfca.cs272.tests.SearchExactTests;

/**
 * Tests that next project code is not in the current project. This class should
 * not be run directly; it is run by GitHub Actions only.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Fall 2023
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
		var parent = new BuildIndexTests();
		var child = parent.new FileTests();

		// expect test to fail
		Assertions.assertThrows(
				AssertionError.class,
				() -> child.testSimple("hello.txt"));
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
		var parent = new SearchExactTests();
		var child = parent.new InitialTests();

		// expect test to fail
		Assertions.assertThrows(
				AssertionError.class,
				child::testSimpleSimple);
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws Exception if an error occurs
	 */
	@Test
	@Tag("past-v1")
	@Tag("next-v2.0")
	@Tag("next-v2.1")
	@Tag("next-v2.2")
	@Tag("next-v2.3")
	@Tag("next-v2.4")
	@Tag("next-v3.0")
	@Tag("next-v3.1")
	@Tag("next-v3.2")
	@Tag("next-v3.3")
	@Tag("next-v3.4")
	@Tag("next-v4.1")
	@Tag("next-v4.x")
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
	@Tag("test-v2.1")
	@Tag("test-v2.2")
	@Tag("test-v2.3")
	@Tag("test-v2.4")
	@Tag("test-v3.0")
	@Tag("test-v3.1")
	@Tag("test-v3.2")
	@Tag("test-v3.3")
	@Tag("test-v3.4")
	@Tag("test-v4.1")
	@Tag("test-v4.x")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	public void fail() {
		Assertions.fail();
	}
}
