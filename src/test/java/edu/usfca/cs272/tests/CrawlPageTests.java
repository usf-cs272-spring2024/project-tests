package edu.usfca.cs272.tests;

import static edu.usfca.cs272.tests.utils.ProjectPath.ACTUAL;
import static edu.usfca.cs272.tests.utils.ProjectPath.EXPECTED;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.usfca.cs272.tests.utils.ProjectFlag;
import edu.usfca.cs272.tests.utils.ProjectPath;
import edu.usfca.cs272.tests.utils.ProjectTests;

/**
 * A test suite for project v4.0.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
@ExtendWith(ProjectTests.TestCounter.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class CrawlPageTests extends ProjectTests {
	// ███████╗████████╗ ██████╗ ██████╗
	// ██╔════╝╚══██╔══╝██╔═══██╗██╔══██╗
	// ███████╗   ██║   ██║   ██║██████╔╝
	// ╚════██║   ██║   ██║   ██║██╔═══╝
	// ███████║   ██║   ╚██████╔╝██║
	// ╚══════╝   ╚═╝    ╚═════╝ ╚═╝

	/*
	 * ...and read this! Please do not spam web servers by rapidly re-running all of
	 * these tests over and over again. You risk being blocked by the web server if
	 * you make making too many requests in too short of a time period!
	 *
	 * Focus on one test or one group of tests at a time instead. If you do that,
	 * you will not have anything to worry about!
	 */

	/**
	 * Tests the output of this project on smaller web pages.
	 */
	@Nested
	@Order(1)
	@Tag("test-v4.0")
	@TestMethodOrder(OrderAnnotation.class)
	public class InitialTests {
		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(1)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"simple, simple,   input/simple/",
			"simple, subdir,   input/simple/a/b/c/subdir.html",
			"simple, capital,  input/simple/capital_extension.HTML",
			"simple, hello,    input/simple/hello.html",
			"simple, mixed,    input/simple/mixed_case.htm",
			"simple, position, input/simple/position.html",
			"simple, stems,    input/simple/stems.html",
			"simple, symbols,  input/simple/symbols.html",
			"simple, dir,      input/simple/dir.txt",
			"simple, wrong,    input/simple/wrong_extension.html"
		})
		public void testSimple(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(3)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"birds, birds,  input/birds/",
			"birds, raven,  input/birds/raven.html",
			"birds, falcon, input/birds/falcon.html#file=hello.jpg"
		})
		public void testBirds(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Free up memory before running.
		 */
		@BeforeAll
		public static void freeMemory() {
			ProjectTests.freeMemory();
		}
	}

	/**
	 * Tests the output of this project on complex web pages.
	 */
	@Nested
	@Order(2)
	@TestMethodOrder(OrderAnnotation.class)
	public class ComplexTests {
		/**
		 * Only run if other tests had 0 failures.
		 *
		 * @param info test information
		 */
		@BeforeAll
		public static void checkStatus(TestInfo info) {
			ProjectTests.TestCounter.assumeNoFailures(info);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(1)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"rfcs, rfcs,    input/rfcs/",
			"rfcs, rfc3629, input/rfcs/rfc3629.html",
			"rfcs, rfc475,  input/rfcs/rfc475.html",
			"rfcs, rfc5646, input/rfcs/rfc5646.html",
			"rfcs, rfc6797, input/rfcs/rfc6797.html",
			"rfcs, rfc6805, input/rfcs/rfc6805.html",
			"rfcs, rfc6838, input/rfcs/rfc6838.html"
		})
		public void testRFCs(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(2)
		@ParameterizedTest(name = "{index} {2}")
		@Tag("test-v4.0")
		@Tag("test-v4.1")
		@Tag("test-v4.2")
		@CsvSource({
			"rfcs, rfc7231, input/rfcs/rfc7231.html"
		})
		public void testRFC7231(String subdir, String id, String seed) throws MalformedURLException {
			testPartial(seed, subdir, id, ProjectPath.QUERY_LETTERS);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(3)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"guten, guten, input/guten/",
			"guten, guten-1322, input/guten/1322-h/1322-h.htm",
			"guten, guten-1400, input/guten/1400-h/1400-h.htm",
			"guten, guten-1661, input/guten/1661-h/1661-h.htm",
			"guten, guten-22577, input/guten/22577-h/22577-h.htm",
			"guten, guten-50468, input/guten/50468-h/50468-h.htm"
		})
		public void testGuten(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(4)
		@ParameterizedTest(name = "{index} {2}")
		@Tag("test-v4.0")
		@Tag("test-v4.1")
		@Tag("test-v4.2")
		@CsvSource({
			"guten, guten-2701, input/guten/2701-h/2701-h.htm"
		})
		public void testGuten2701(String subdir, String id, String seed) throws MalformedURLException {
			testPartial(seed, subdir, id, ProjectPath.QUERY_COMPLEX);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(5)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"java, AbstractCollection, docs/api/java.base/java/util/AbstractCollection.html",
			"java, SourceVersion, docs/api/java.compiler/javax/lang/model/SourceVersion.html",
			"java, AboutHandler, docs/api/java.desktop/java/awt/desktop/AboutHandler.html",
			"java, AbstractPreferences, docs/api/java.prefs/java/util/prefs/AbstractPreferences.html",
			"java, overview, docs/api/overview-tree.html"
		})
		public void testJava(String subdir, String id, String seed) throws MalformedURLException {
			testCountsIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(6)
		@ParameterizedTest(name = "{index} {2}")
		@Tag("test-v4.0")
		@Tag("test-v4.1")
		@Tag("test-v4.2")
		@Tag("past-v5")
		@CsvSource({
			"java, allclasses, docs/api/allclasses-index.html"
		})
		public void testJavaIndex(String subdir, String id, String seed) throws MalformedURLException {
			testPartial(seed, subdir, id, ProjectPath.QUERY_LETTERS);
		}

		/**
		 * Free up memory before running.
		 */
		@BeforeAll
		public static void freeMemory() {
			ProjectTests.freeMemory();
		}
	}

	/**
	 * Tests the output of this project on special web pages.
	 */
	@Nested
	@Order(3)
	@Tag("test-v4.0")
	@Tag("test-v4.1")
	@Tag("test-v4.2")
	@TestMethodOrder(OrderAnnotation.class)
	public class SpecialTests {
		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(1)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"special, empty, input/simple/empty.html"
		})
		public void testEmptyIndex(String subdir, String id, String seed) throws MalformedURLException {
			Map<ProjectFlag, String> input = Map.of(ProjectFlag.QUERY, ProjectPath.QUERY_SIMPLE.text);
			List<ProjectFlag> output = List.of(ProjectFlag.INDEX, ProjectFlag.COUNTS, ProjectFlag.RESULTS);
			testCrawl(seed, subdir, id, input, output);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(2)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"special, type-double,  input/simple/double_extension.html.txt",
			"special, type-noext,   input/simple/no_extension",
			"special, type-nowhere, input/simple/no_extension#nowhere.html",
			"special, type-cover,   input/guten/1661-h/images/cover.jpg",
		})
		public void testNotHtml(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(3)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"special, status-404, https://www.cs.usfca.edu/~cs272/redirect/nowhere",
			"special, status-410, https://www.cs.usfca.edu/~cs272/redirect/gone"
		})
		public void testNotOkay(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(4)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"special, redirect-1, https://www.cs.usfca.edu/~cs272/redirect/one",
			"special, redirect-2, https://www.cs.usfca.edu/~cs272/redirect/two",
			"special, redirect-3, https://www.cs.usfca.edu/~cs272/redirect/three",
			"special, http-usfcs, http://www.cs.usfca.edu/~cs272/simple/hello.html",
			"special, http-hello, input/simple/hello.html"
		})
		public void testRedirect(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl output.
		 *
		 * @param subdir the expected output subdirectory
		 * @param id the unique test and file id
		 * @param seed the seed url
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(5)
		@ParameterizedTest(name = "{index} {2}")
		@CsvSource({
			"special, loop-1, https://www.cs.usfca.edu/~cs272/redirect/loop1",
			"special, loop-2, https://www.cs.usfca.edu/~cs272/redirect/loop2"
		})
		public void testFailedRedirect(String subdir, String id, String seed) throws MalformedURLException {
			testIndex(seed, subdir, id);
		}

		/**
		 * Tests crawl AND text output.
		 *
		 * @throws MalformedURLException if unable to create seed url
		 */
		@Order(6)
		@Test
		public void testMixedInput() throws MalformedURLException {
			URI uri = GITHUB.resolve("input/simple/hello.html");

			List<ProjectFlag> output = List.of(ProjectFlag.COUNTS, ProjectFlag.INDEX, ProjectFlag.RESULTS);
			Map<ProjectFlag, String> input = new LinkedHashMap<>();

			input.put(ProjectFlag.HTML, uri.toString());
			input.put(ProjectFlag.TEXT, ProjectPath.HELLO.text);
			input.put(ProjectFlag.QUERY, ProjectPath.QUERY_SIMPLE.text);
			input.put(ProjectFlag.PARTIAL, null);

			testCrawl(uri.toString(), "special", "mixed", input, output);
		}

		/**
		 * Free up memory before running.
		 */
		@BeforeAll
		public static void freeMemory() {
			ProjectTests.freeMemory();
		}
	}

	/**
	 * Tests the exception handling of this project.
	 */
	@Nested
	@Order(4)
	@Tag("test-v4.0")
	@Tag("test-v4.1")
	@Tag("test-v4.2")
	@Tag("past-v5")
	@TestMethodOrder(OrderAnnotation.class)
	public class ExceptionTests {
		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(1)
		public void testMissingSeed() throws Exception {
			Map<ProjectFlag, String> config = new LinkedHashMap<>();
			config.put(ProjectFlag.HTML, null);
			config.put(ProjectFlag.COUNTS, null);

			testNoExceptions(args(config), SHORT_TIMEOUT);
			Assertions.assertTrue(Files.exists(ProjectFlag.COUNTS.path), ProjectFlag.COUNTS.value);
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @param seed the seed URL to use
		 * @throws Exception if an error occurs
		 */
		@ParameterizedTest
		@Order(2)
		@ValueSource(strings = {
				"mailto:sjengle@cs.usfca.edu",
				"javascript:alert('Hello!')"
		})
		public void testInvalidURL(String seed) throws Exception {
			Map<ProjectFlag, String> config = new LinkedHashMap<>();
			config.put(ProjectFlag.HTML, seed);
			config.put(ProjectFlag.COUNTS, null);

			testNoExceptions(args(config), SHORT_TIMEOUT);
			Assertions.assertTrue(Files.exists(ProjectFlag.COUNTS.path), ProjectFlag.COUNTS.value);
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(3)
		public void testThreads() throws Exception {
			List<ProjectFlag> output = List.of(ProjectFlag.INDEX);
			Map<ProjectFlag, String> input = new LinkedHashMap<>();
			input.put(ProjectFlag.THREADS, Integer.toString(2));
			testCrawl("input/simple/hello.html", "simple", "hello", input, output);
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(4)
		public void testInvalidThreads() throws Exception {
			List<ProjectFlag> output = List.of(ProjectFlag.INDEX);
			Map<ProjectFlag, String> input = new LinkedHashMap<>();
			input.put(ProjectFlag.THREADS, "hello");
			testCrawl("input/simple/hello.html", "simple", "hello", input, output);
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(5)
		public void testNoOutput() throws Exception {
			URI uri = GITHUB.resolve("input/simple/hello.html");
			Map<ProjectFlag, String> input = new LinkedHashMap<>();
			input.put(ProjectFlag.HTML, uri.toString());
			testNoExceptions(args(input), SHORT_TIMEOUT);
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(6)
		public void testOkTextNoSeed() throws Exception {
			Map<ProjectFlag, String> input = new LinkedHashMap<>();
			input.put(ProjectFlag.HTML, null);
			input.put(ProjectFlag.TEXT, ProjectPath.HELLO.text);
			input.put(ProjectFlag.QUERY, ProjectPath.QUERY_SIMPLE.text);
			input.put(ProjectFlag.PARTIAL, null);
			input.put(ProjectFlag.COUNTS, null);

			Path actual = ProjectFlag.COUNTS.path;
			Path expected = ProjectPath.EXPECTED.resolve("counts").resolve("counts-simple-hello.json");
			checkOutput(args(input), Map.of(actual, expected));
		}

		/**
		 * Tests that exceptions are not thrown.
		 *
		 * @throws Exception if an error occurs
		 */
		@Test
		@Order(7)
		public void testNoTextOKSeed() throws Exception {
			URI uri = GITHUB.resolve("input/simple/hello.html");

			Map<ProjectFlag, String> input = new LinkedHashMap<>();
			input.put(ProjectFlag.HTML, uri.toString());
			input.put(ProjectFlag.TEXT, null);
			input.put(ProjectFlag.QUERY, ProjectPath.QUERY_SIMPLE.text);
			input.put(ProjectFlag.PARTIAL, null);
			input.put(ProjectFlag.INDEX, null);

			Path actual = ProjectFlag.INDEX.path;
			Path expected = ProjectPath.EXPECTED.resolve("crawl").resolve("simple").resolve("hello-index.json");
			checkOutput(args(input), Map.of(actual, expected));
		}

		/**
		 * Free up memory before running.
		 */
		@BeforeAll
		public static void freeMemory() {
			ProjectTests.freeMemory();
		}
	}

	/** Base for the GitHub test website. */
	public static final URI GITHUB = URI.create("https://usf-cs272-spring2024.github.io/project-web/");

	/** Base directory for crawl output. */
	public static final Path CRAWL = EXPECTED.resolve("crawl");

	/**
	 * Tests the output of crawl.
	 *
	 * @param seed the seed link
	 * @param subdir the expected output subdir
	 * @param id the test id
	 * @param input the input flags to use
	 * @param output the output flags to use
	 * @throws MalformedURLException if unable to convert seed to URL
	 */
	public static void testCrawl(String seed, String subdir, String id, Map<ProjectFlag, String> input, List<ProjectFlag> output) throws MalformedURLException {
		URI uri = GITHUB.resolve(seed);

		Map<ProjectFlag, String> config = new LinkedHashMap<>();
		Map<Path, Path> files = new LinkedHashMap<>();

		// configure input parameters
		config.put(ProjectFlag.HTML, uri.toString());
		config.putAll(input);

		// configure output parameters
		for (ProjectFlag flag : output) {
			String name = String.format("%s%s.json", id, flag.flag);
			Path actual = ACTUAL.resolve(name);
			Path expected = CRAWL.resolve(subdir).resolve(name);

			config.put(flag, actual.toString());
			files.put(actual, expected);
		}

		checkOutput(args(config), files);
	}


	/**
	 * Tests the inverted index output of crawl.
	 *
	 * @param seed the seed link
	 * @param subdir the expected output subdir
	 * @param id the test id
	 * @throws MalformedURLException if unable to convert seed to URL
	 */
	public static void testIndex(String seed, String subdir, String id) throws MalformedURLException {
		Map<ProjectFlag, String> input = Collections.emptyMap();
		List<ProjectFlag> output = List.of(ProjectFlag.INDEX);
		testCrawl(seed, subdir, id, input, output);
	}

	/**
	 * Tests the inverted index and counts output of crawl.
	 *
	 * @param seed the seed link
	 * @param subdir the expected output subdir
	 * @param id the test id
	 * @throws MalformedURLException if unable to convert seed to URL
	 */
	public static void testCountsIndex(String seed, String subdir, String id) throws MalformedURLException {
		Map<ProjectFlag, String> input = Collections.emptyMap();
		List<ProjectFlag> output = List.of(ProjectFlag.COUNTS, ProjectFlag.INDEX);
		testCrawl(seed, subdir, id, input, output);
	}

	/**
	 * Tests the inverted index and partial search output output of crawl.
	 *
	 * @param seed the seed link
	 * @param subdir the expected output subdir
	 * @param id the test id
	 * @param query the query file
	 * @throws MalformedURLException if unable to convert seed to URL
	 */
	public static void testPartial(String seed, String subdir, String id, ProjectPath query) throws MalformedURLException {
		Map<ProjectFlag, String> input = Map.of(ProjectFlag.QUERY, query.text, ProjectFlag.PARTIAL, "");
		List<ProjectFlag> output = List.of(ProjectFlag.COUNTS, ProjectFlag.INDEX, ProjectFlag.RESULTS);
		testCrawl(seed, subdir, id, input, output);
	}
}
