package edu.usfca.cs272.tests;

import static edu.usfca.cs272.tests.utils.ProjectFlag.PARTIAL;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import edu.usfca.cs272.tests.utils.ProjectPath;

/**
 * A test suite for project v2.1.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class SearchPartialTests extends SearchExactTests {
	/**
	 * Sets up the tests before running.
	 */
	@Override
	@BeforeEach
	public void setup() {
		super.partial = true;
		super.searchFlag = partial ? PARTIAL.flag : "";
	}

	/** A nested test suite. */
	@Nested
	@Order(1)
	@Tag("test-v2.1")
	@TestMethodOrder(OrderAnnotation.class)
	public class InitialTests extends SearchExactTests.InitialTests {
		@Test
		@Order(1)
		@Override
		public void testSimpleSimple() {
			super.testSimpleSimple();
		}

		@Test
		@Order(2)
		@Override
		public void testStemsWords() {
			super.testStemsWords();
		}

		@Test
		@Order(3)
		@Override
		public void testStemsRespect() {
			super.testStemsRespect();
		}

		@Test
		@Order(4)
		@Override
		public void testStemsLetters() {
			super.testStemsLetters();
		}

		@Test
		@Order(5)
		@Override
		public void testRfcsLetters() {
			super.testRfcsLetters();
		}
	}

	/** A nested test suite. */
	@Nested
	@Order(2)
	@Tag("test-v2.1")
	@TestMethodOrder(OrderAnnotation.class)
	public class ComplexTests extends SearchExactTests.ComplexTests {
		@ParameterizedTest
		@Order(1)
		@EnumSource(mode = MATCH_ALL, names = "^GUTEN_.+")
		@Override
		public void testGutenFiles(ProjectPath path) {
			super.testGutenFiles(path);
		}

		@Test
		@Order(2)
		@Override
		public void testGutenComplex() {
			super.testGutenComplex();
		}

		@Test
		@Order(3)
		@Override
		public void testTextWords() {
			super.testTextWords();
		}

		@Override
		@Test
		@Order(4)
		@Tag("test-v2.2")
		@Tag("test-v2.3")
		@Tag("test-v2.4")
		public void testTextRespect() {
			super.testTextRespect();
		}

		@Override@Test
		@Order(5)
		@Tag("test-v2.2")
		@Tag("test-v2.3")
		@Tag("test-v2.4")
		public void testTextComplex() {
			super.testTextComplex();
		}
	}

	/** A nested test suite. */
	@Nested
	@Order(3)
	@Tag("test-v2.1")
	@Tag("test-v2.2")
	@Tag("test-v2.3")
	@Tag("test-v2.4")
	@Tag("past-v3")
	@Tag("past-v4")
	@Tag("past-v5")
	public class ExceptionTests extends SearchExactTests.ExceptionTests {
		@Test
		@Order(1)
		@Override
		public void testMissingQueryPath() throws Exception {
			super.testMissingQueryPath();
		}

		@Test
		@Order(1)
		@Override
		public void testInvalidQueryPath() throws Exception {
			super.testInvalidQueryPath();
		}

		@Test
		@Order(1)
		@Override
		public void testNoOutput() throws Exception {
			super.testNoOutput();
		}

		@Test
		@Order(1)
		@Override
		public void testDefaultResults() throws Exception {
			super.testDefaultResults();
		}

		@Test
		@Order(1)
		@Override
		public void testNoText() throws Exception {
			super.testNoText();
		}

		@Test
		@Order(1)
		@Override
		public void testOnlyResults() throws Exception {
			super.testOnlyResults();
		}

		@Test
		@Order(1)
		@Override
		public void testSwitched() throws Exception {
			super.testSwitched();
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
	public class ComboTests extends SearchExactTests.ComboTests {

	}
}
