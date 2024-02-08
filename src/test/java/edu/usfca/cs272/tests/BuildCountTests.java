package edu.usfca.cs272.tests;

import static edu.usfca.cs272.tests.utils.ProjectFlag.COUNTS;
import static edu.usfca.cs272.tests.utils.ProjectFlag.TEXT;
import static edu.usfca.cs272.tests.utils.ProjectPath.HELLO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;

import edu.usfca.cs272.tests.utils.ProjectPath;
import edu.usfca.cs272.tests.utils.ProjectTests;

/**
 * A test suite for project v1.0.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class BuildCountTests extends ProjectTests {
	/**
	 * Tests the output of this project.
	 */
	@Nested
	@Order(1)
	@Tag("test-v1.0")
	@TestMethodOrder(OrderAnnotation.class)
	public class FileTests {
		/**
		 * See the JUnit output for test details.
		 */
		@Order(1)
		@Test
		public void testHello() {
			testOutput(ProjectPath.HELLO);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(2)
		@Test
		@Tag("test-v1.1")
		@Tag("test-v1.2")
		@Tag("test-v1.3")
		@Tag("test-v1.4")
		public void testSentences() {
			Path input = ProjectPath.SIMPLE.resolve("sentences.md");
			String id = ProjectPath.id(input);
			testOutput(input, id);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(3)
		@Test
		public void testStemIn() {
			testOutput(ProjectPath.STEMS_IN);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(4)
		@Test
		public void testRfcHttp() {
			testOutput(ProjectPath.RFCS_HTTP);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(4)
		@Test
		public void testGutenGreat() {
			testOutput(ProjectPath.GUTEN_GREAT);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(5)
		@Test
		@Tag("test-v1.1")
		@Tag("test-v1.2")
		@Tag("test-v1.3")
		@Tag("test-v1.4")
		public void testEmpty() {
			testOutput(ProjectPath.EMPTY);
		}
	}

	/**
	 * Tests the output of this project.
	 */
	@Nested
	@Order(2)
	@Tag("test-v1.0")
	@TestMethodOrder(OrderAnnotation.class)
	public class DirectoryTests {
		/**
		 * See the JUnit output for test details.
		 */
		@Order(1)
		@Test
		public void testSimple() {
			testOutput(ProjectPath.SIMPLE);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(2)
		@Test
		public void testRfcs() {
			testOutput(ProjectPath.RFCS);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(3)
		@Test
		public void testGuten() {
			testOutput(ProjectPath.GUTEN);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Order(4)
		@Test
		@Tag("test-v1.1")
		@Tag("test-v1.2")
		@Tag("test-v1.3")
		@Tag("test-v1.4")
		public void testText() {
			testOutput(ProjectPath.TEXT);
		}
	}

	/**
	 * Tests the exception handling of this project.
	 */
	@Nested
	@Order(3)
	@Tag("test-v1.0")
	@Tag("test-v1.1")
	@Tag("test-v1.2")
	@Tag("test-v1.3")
	@Tag("test-v1.4")
	@Tag("past-v2")
	@Tag("past-v3")
	@Tag("past-v4")
	@Tag("past-v5")
	@TestMethodOrder(OrderAnnotation.class)
	public class ExceptionTests {
		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 */
		@Test
		@Order(1)
		public void testNoArguments() {
			String[] args = {};
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 */
		@Test
		@Order(2)
		public void testInvalidFlag() {
			String[] args = { "-hello", "world" };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 */
		@Test
		@Order(3)
		public void testOnlyValues() {
			String[] args = { "hello", "world" };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 */
		@Test
		@Order(4)
		public void testOnlyText() {
			String[] args = { "-text" };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 */
		@Test
		@Order(5)
		public void testInvalidPath() {
			// generates a random path name
			String path = Long.toHexString(Double.doubleToLongBits(Math.random()));
			String[] args = { TEXT.flag, path };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		@Order(6)
		public void testNoCounts() throws IOException {
			Path output = COUNTS.path;
			String[] args = { TEXT.flag, HELLO.text };
			Files.deleteIfExists(output);
			testNoExceptions(args, SHORT_TIMEOUT);

			Executable test = testFileExists(COUNTS.path, COUNTS.flag, false);
			assertMultiple(List.of(test), args, "Found unexpected output file.");
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		@Order(7)
		public void testDefaultCounts() throws IOException {
			String[] args = { TEXT.flag, HELLO.text, COUNTS.flag };
			Files.deleteIfExists(COUNTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);
			Executable test = testFileExists(COUNTS.path, COUNTS.flag, true);
			assertMultiple(List.of(test), args, "Missing expected output file.");
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		@Order(8)
		public void testOnlyCounts() throws IOException {
			String[] args = { COUNTS.flag };
			Files.deleteIfExists(COUNTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);
			Executable test = testFileExists(COUNTS.path, COUNTS.flag, true);
			assertMultiple(List.of(test), args, "Missing expected output file.");
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		@Order(9)
		public void testSwitched() throws IOException {
			String[] args = { COUNTS.flag, TEXT.flag, HELLO.text };
			Files.deleteIfExists(COUNTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);
			Executable test = testFileExists(COUNTS.path, COUNTS.flag, true);
			assertMultiple(List.of(test), args, "Missing expected output file.");
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		@Order(10)
		public void testSwitchedComplex() throws IOException {
			String[] args = { COUNTS.flag, "-ignored", TEXT.flag, HELLO.text };
			Files.deleteIfExists(COUNTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);
			Executable test = testFileExists(COUNTS.path, COUNTS.flag, true);
			assertMultiple(List.of(test), args, "Missing expected output file.");
		}
	}

	/**
	 * Generates the arguments to use for the output test cases. Designed to be used
	 * inside a JUnit test.
	 *
	 * @param input the input
	 * @param id the id
	 */
	public static void testOutput(Path input, String id) {
		String filename = String.format("counts-%s.json", id);
		Path actual = ProjectPath.ACTUAL.resolve(filename).normalize();
		Path expected = ProjectPath.EXPECTED.resolve("counts").resolve(filename).normalize();
		String[] args = { TEXT.flag, input.toString(), COUNTS.flag, actual.toString() };
		Executable debug = () -> checkOutput(args, actual, expected);
		Assertions.assertTimeoutPreemptively(LONG_TIMEOUT, debug);
	}

	/**
	 * Calls {@link BuildCountTests#testOutput(Path, String)} using the enum.
	 *
	 * @param path the path
	 * @see BuildCountTests#testOutput(Path, String)
	 */
	public static void testOutput(ProjectPath path) {
		testOutput(path.path, path.id);
	}
}
