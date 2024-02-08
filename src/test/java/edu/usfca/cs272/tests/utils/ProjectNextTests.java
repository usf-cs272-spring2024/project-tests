package edu.usfca.cs272.tests.utils;

import static edu.usfca.cs272.tests.utils.ProjectFlag.INDEX;
import static edu.usfca.cs272.tests.utils.ProjectFlag.TEXT;
import static edu.usfca.cs272.tests.utils.ProjectPath.HELLO;
import static edu.usfca.cs272.tests.utils.ProjectTests.SHORT_TIMEOUT;
import static edu.usfca.cs272.tests.utils.ProjectTests.testNoExceptions;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
		String[] args = { TEXT.flag, HELLO.text, INDEX.flag };
		Files.deleteIfExists(INDEX.path);
		testNoExceptions(args, SHORT_TIMEOUT);
		Assertions.assertFalse(Files.exists(INDEX.path), debug);
	}

	/**
	 * Tests that next project functionality is not present.
	 *
	 * @throws Exception if an error occurs
	 */
	@Test
	@Tag("past-v1")
	@Tag("next-v4.1")
	@Tag("next-v4.x")
	@Tag("next-v5.0")
	@Tag("next-v5.1")
	@Tag("test-v5.0")
	@Tag("test-v5.1")
	public void pass() throws Exception {
		// No next tests for these releases!
	}
}
