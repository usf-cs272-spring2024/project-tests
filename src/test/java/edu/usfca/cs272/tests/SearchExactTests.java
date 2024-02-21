package edu.usfca.cs272.tests;

import static edu.usfca.cs272.tests.utils.ProjectFlag.COUNTS;
import static edu.usfca.cs272.tests.utils.ProjectFlag.INDEX;
import static edu.usfca.cs272.tests.utils.ProjectFlag.PARTIAL;
import static edu.usfca.cs272.tests.utils.ProjectFlag.QUERY;
import static edu.usfca.cs272.tests.utils.ProjectFlag.RESULTS;
import static edu.usfca.cs272.tests.utils.ProjectFlag.TEXT;
import static edu.usfca.cs272.tests.utils.ProjectPath.ACTUAL;
import static edu.usfca.cs272.tests.utils.ProjectPath.EXPECTED;
import static edu.usfca.cs272.tests.utils.ProjectPath.HELLO;
import static edu.usfca.cs272.tests.utils.ProjectPath.QUERY_SIMPLE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import edu.usfca.cs272.tests.utils.ProjectPath;
import edu.usfca.cs272.tests.utils.ProjectTests;

/**
 * A test suite for project v2.0.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SearchExactTests extends ProjectTests {
	/** The default search mode for this nested class. */
	public boolean partial;

	/** The search flag to add to the command-line arguments. */
	public String searchFlag;

	/**
	 * Sets up the tests before running.
	 */
	@BeforeEach
	public void setup() {
		partial = false;
		searchFlag = partial ? PARTIAL.flag : "";
	}

	/**
	 * Tests the output of this project.
	 */
	@Nested
	@Order(1)
	@Tag("test-v2.0")
	@TestMethodOrder(OrderAnnotation.class)
	public class InitialTests {
		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(1)
		public void testSimpleSimple() {
			testOutput(partial, "simple", ProjectPath.SIMPLE);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(2)
		public void testStemsWords() {
			testOutput(partial, "words", ProjectPath.STEMS);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(3)
		public void testStemsRespect() {
			testOutput(partial, "respect", ProjectPath.STEMS);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(4)
		public void testStemsLetters() {
			testOutput(partial, "letters", ProjectPath.STEMS);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(5)
		public void testRfcsLetters() {
			testOutput(partial, "letters", ProjectPath.RFCS);
		}
	}

	/**
	 * Tests the output of this project.
	 */
	@Nested
	@Order(2)
	@Tag("test-v2.0")
	@TestMethodOrder(OrderAnnotation.class)
	public class ComplexTests {
		/**
		 * See the JUnit output for test details.
		 *
		 * @param path the path
		 */
		@ParameterizedTest
		@Order(1)
		@EnumSource(mode = MATCH_ALL, names = "^GUTEN_.+")
		public void testGutenFiles(ProjectPath path) {
			testOutput(partial, "complex", path);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(2)
		public void testGutenComplex() {
			testOutput(partial, "complex", ProjectPath.GUTEN);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(3)
		public void testTextWords() {
			testOutput(partial, "words", ProjectPath.TEXT);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(4)
		@Tag("test-v2.1")
		@Tag("test-v2.2")
		@Tag("test-v2.3")
		@Tag("test-v2.4")
		public void testTextRespect() {
			testOutput(partial, "respect", ProjectPath.TEXT);
		}

		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(5)
		@Tag("test-v2.1")
		@Tag("test-v2.2")
		@Tag("test-v2.3")
		@Tag("test-v2.4")
		public void testTextComplex() {
			testOutput(partial, "complex", ProjectPath.TEXT);
		}
	}

	/**
	 * Tests the index exception handling of this project.
	 */
	@Nested
	@Order(3)
	@Tag("test-v2.0")
	@Tag("test-v2.1")
	@Tag("test-v2.2")
	@Tag("test-v2.3")
	@Tag("test-v2.4")
	@Tag("past-v3")
	@Tag("past-v4")
	@Tag("past-v5")
	@TestMethodOrder(OrderAnnotation.class)
	public class ExceptionTests {
		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(1)
		public void testMissingQueryPath() throws Exception {
			String[] args = { TEXT.flag, HELLO.text, QUERY.flag, searchFlag };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(2)
		public void testInvalidQueryPath() throws Exception {
			String query = Long.toHexString(Double.doubleToLongBits(Math.random()));
			String[] args = { TEXT.flag, HELLO.text, QUERY.flag, query, searchFlag };
			testNoExceptions(args, SHORT_TIMEOUT);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(3)
		public void testNoOutput() throws Exception {
			String[] args = { TEXT.flag, HELLO.text, QUERY.flag, QUERY_SIMPLE.text, searchFlag };

			Files.deleteIfExists(COUNTS.path);
			Files.deleteIfExists(INDEX.path);
			Files.deleteIfExists(RESULTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);

			Assertions.assertAll(
					() -> Assertions.assertFalse(Files.exists(INDEX.path), INDEX.value),
					() -> Assertions.assertFalse(Files.exists(COUNTS.path), COUNTS.value),
					() -> Assertions.assertFalse(Files.exists(RESULTS.path), RESULTS.value)
			);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(4)
		public void testDefaultResults() throws Exception {
			String[] args = { TEXT.flag, HELLO.text, QUERY.flag, QUERY_SIMPLE.text, searchFlag, RESULTS.flag };

			Files.deleteIfExists(COUNTS.path);
			Files.deleteIfExists(INDEX.path);
			Files.deleteIfExists(RESULTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);

			Assertions.assertAll(
					() -> Assertions.assertFalse(Files.exists(INDEX.path), INDEX.value),
					() -> Assertions.assertFalse(Files.exists(COUNTS.path), COUNTS.value),
					() -> Assertions.assertTrue(Files.exists(RESULTS.path), RESULTS.value)
			);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(5)
		public void testNoText() throws Exception {
			String[] args = { QUERY.flag, QUERY_SIMPLE.text, searchFlag, RESULTS.flag };

			Files.deleteIfExists(COUNTS.path);
			Files.deleteIfExists(INDEX.path);
			Files.deleteIfExists(RESULTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);

			Assertions.assertAll(
					() -> Assertions.assertFalse(Files.exists(INDEX.path), INDEX.value),
					() -> Assertions.assertFalse(Files.exists(COUNTS.path), COUNTS.value),
					() -> Assertions.assertTrue(Files.exists(RESULTS.path), RESULTS.value)
			);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(6)
		public void testOnlyResults() throws Exception {
			String[] args = { searchFlag, RESULTS.flag };

			Files.deleteIfExists(COUNTS.path);
			Files.deleteIfExists(INDEX.path);
			Files.deleteIfExists(RESULTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);

			Assertions.assertAll(
					() -> Assertions.assertFalse(Files.exists(INDEX.path), INDEX.value),
					() -> Assertions.assertFalse(Files.exists(COUNTS.path), COUNTS.value),
					() -> Assertions.assertTrue(Files.exists(RESULTS.path), RESULTS.value)
			);
		}

		/**
		 * Tests no exceptions are thrown with the provided arguments.
		 *
		 * @throws Exception if exception occurs
		 */
		@Test
		@Order(7)
		public void testSwitched() throws Exception {
			String[] args = { searchFlag, QUERY.flag, QUERY_SIMPLE.text, TEXT.flag, HELLO.text, RESULTS.flag };

			Files.deleteIfExists(COUNTS.path);
			Files.deleteIfExists(INDEX.path);
			Files.deleteIfExists(RESULTS.path);
			testNoExceptions(args, SHORT_TIMEOUT);

			Assertions.assertAll(
					() -> Assertions.assertFalse(Files.exists(INDEX.path), INDEX.value),
					() -> Assertions.assertFalse(Files.exists(COUNTS.path), COUNTS.value),
					() -> Assertions.assertTrue(Files.exists(RESULTS.path), RESULTS.value)
			);
		}
	}

	/**
	 * All-in-one tests of this project functionality.
	 */
	@Nested
	@Order(4)
	@Tag("past-v3")
	@Tag("past-v4")
	@Tag("past-v5")
	@TestMethodOrder(OrderAnnotation.class)
	public class ComboTests {
		/**
		 * See the JUnit output for test details.
		 */
		@Test
		@Order(1)
		public void testCountsIndexResults() {
			ProjectPath input = ProjectPath.TEXT;
			String query = "complex";
			String type = partial ? "partial" : "exact";

			String indexName = String.format("index-%s.json", input.id);
			String countsName = String.format("counts-%s.json", input.id);
			String resultsName = String.format("%s-%s-%s.json", type, query, input.id);

			Path indexActual = ACTUAL.resolve(indexName);
			Path countsActual = ACTUAL.resolve(countsName);
			Path resultsActual = ACTUAL.resolve(resultsName);

			String[] args = {
					TEXT.flag, input.text,
					INDEX.flag, indexActual.toString(),
					COUNTS.flag, countsActual.toString(),
					QUERY.flag, ProjectPath.QUERY.resolve(query + ".txt").toString(),
					RESULTS.flag, resultsActual.toString(),
					partial ? PARTIAL.flag : ""
			};

			Map<Path, Path> files = Map.of(
					countsActual, EXPECTED.resolve("counts").resolve(countsName),
					indexActual, EXPECTED.resolve("index").resolve(indexName),
					resultsActual, EXPECTED.resolve(type).resolve(resultsName)
			);

			Executable test = () -> ProjectTests.checkOutput(args, files);
			Assertions.assertTimeoutPreemptively(LONG_TIMEOUT, test);
		}
	}

	/**
	 * Generates the arguments to use for this test case. Designed to be used
	 * inside a JUnit test.
	 * @param partial whether to perform exact or partial search
	 * @param query the query file to use for search
	 * @param input the input path to use
	 */
	public static void testOutput(boolean partial, String query, ProjectPath input) {
		String type = partial ? "partial" : "exact";
		String filename = String.format("%s-%s-%s.json", type, query, input.id);

		Path actual = ACTUAL.resolve(filename).normalize();
		Path expected = EXPECTED.resolve(type).resolve(filename).normalize();
		Path queries = ProjectPath.QUERY.resolve(query + ".txt").normalize();

		String[] args = {
				TEXT.flag, input.text, QUERY.flag, queries.toString(),
				RESULTS.flag, actual.toString(), partial ? PARTIAL.flag : ""
		};

		Executable debug = () -> checkOutput(args, actual, expected);
		Assertions.assertTimeoutPreemptively(LONG_TIMEOUT, debug);
	}
}
