package edu.usfca.cs272.tests.utils;

import static edu.usfca.cs272.tests.utils.ProjectPath.ACTUAL;
import static edu.usfca.cs272.tests.utils.ProjectPath.EXPECTED;
import static edu.usfca.cs272.tests.utils.ProjectPath.QUERY;
import static edu.usfca.cs272.tests.utils.ProjectPath.TEXT;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import org.opentest4j.MultipleFailuresError;

import edu.usfca.cs272.Driver;

/**
 * Utility methods used by other JUnit test classes.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2024
 */
public class ProjectTests {
	/** Amount of time to wait for long-running tests to finish. */
	public static final Duration LONG_TIMEOUT = Duration.ofMinutes(5);

	/** Amount of time to wait for short-running tests to finish. */
	public static final Duration SHORT_TIMEOUT = Duration.ofSeconds(30);

	/**
	 * Generates header for debugging output when an error occurs.
	 *
	 * @param args the arguments
	 * @param message the error message
	 * @return the error header
	 */
	public static StringBuilder errorHeader(String[] args, String message) {
		StringBuilder header = new StringBuilder("\n");

		header.append("Error Message:\n");
		header.append(message);
		header.append("\n\n");

		// add arguments
		header.append("Arguments (");
		header.append(args.length);
		header.append("):\n");
		header.append(args.length > 0 ? String.join(" ", args) : "(none)");
		header.append("\n\n");

		// add working directory information
		header.append("Working Directory:\n");
		header.append(Path.of(".").toAbsolutePath().normalize().getFileName());
		header.append("\n\n");

		return header;
	}

	/**
	 * Only adds the most relevant parts of the stack trace to the debug output.
	 *
	 * @param debug the debug output
	 * @param thrown the thrown error
	 */
	public static void addFilteredTrace(StringBuilder debug, Throwable thrown) {
		// get full stack trace
		StackTraceElement[] trace = thrown.getStackTrace();

		// filter to only include relevant source code
		List<StackTraceElement> filtered = Arrays.stream(trace)
				.filter(line -> line.getClassName().startsWith("edu.usfca.cs272"))
				.filter(line -> !line.getClassName().startsWith("edu.usfca.cs272.tests"))
				.toList();

		// make sure at least one line is displayed
		if (filtered.isEmpty() && trace.length > 0) {
			filtered = List.of(trace[0]);
		}

		// add exception name and message
		debug.append(thrown.toString());
		debug.append("\n");

		// add each filtered line to output
		for (StackTraceElement element : filtered) {
			debug.append("\tat ").append(element.toString()).append("\n");
		}
	}

	/**
	 * Makes multiple assertions with customized error output if there are any
	 * failures.
	 *
	 * @param tests the assertions
	 * @param args the original args
	 * @param header the header
	 */
	public static void assertMultiple(List<Executable> tests, String[] args, String header) {
		// Test the output of all files
		try {
			Assertions.assertAll(tests);
		}
		catch (MultipleFailuresError e) {
			// Customize multiple failure output
			int errors = e.getFailures().size();
			StringBuilder debug = errorHeader(args, header);

			debug.append("Error Details (" + errors + "):");

			for (Throwable t : e.getFailures()) {
				debug.append("\n");
				if (t instanceof AssertionError) {
					debug.append(t.getMessage());
				}
				else {
					addFilteredTrace(debug, t);
				}
				debug.append("\n");
			}

			debug.append("\nFull Failure Trace:");
			Assertions.fail(debug.toString());
		}
	}

	/**
	 * Checks line-by-line if two files are equal. If one file contains extra blank
	 * lines at the end of the file, the two are still considered equal. Works even
	 * if the path separators in each file are different.
	 *
	 * @param path1 path to first file to compare with
	 * @param path2 path to second file to compare with
	 * @return positive value if two files are equal, negative value if not
	 *
	 * @throws IOException if IO error occurs
	 */
	public static int compareFiles(Path path1, Path path2) throws IOException {
		// used to output line mismatch
		int count = 0;

		try (
				BufferedReader reader1 = Files.newBufferedReader(path1, UTF_8);
				BufferedReader reader2 = Files.newBufferedReader(path2, UTF_8);
		) {
			String line1 = reader1.readLine();
			String line2 = reader2.readLine();

			while (true) {
				count++;

				// compare lines until we hit a null (i.e. end of file)
				if (line1 != null && line2 != null) {
					// remove trailing spaces
					line1 = line1.stripTrailing();
					line2 = line2.stripTrailing();

					// check if lines are equal
					if (!line1.equals(line2)) {
						return -count;
					}

					// read next lines if we get this far
					line1 = reader1.readLine();
					line2 = reader2.readLine();
				}
				else {
					// discard extra blank lines at end of reader1
					while (line1 != null && line1.isBlank()) {
						line1 = reader1.readLine();
					}

					// discard extra blank lines at end of reader2
					while (line2 != null && line2.isBlank()) {
						line2 = reader2.readLine();
					}

					if (line1 == line2) {
						// only true if both are null, otherwise one file had extra non-empty lines
						return count;
					}

					// extra blank lines found in one file
					return -count;
				}
			}
		}
	}

	/**
	 * Checks whether {@link Driver} generates the expected output without any
	 * exceptions. Will print the stack trace if an exception occurs. Designed to be
	 * used within an unit test. If the test was successful, deletes the actual
	 * file. Otherwise, keeps the file for debugging purposes.
	 *
	 * @param args arguments to pass to {@link Driver}
	 * @param actual path to actual output
	 * @param expected path to expected output
	 */
	public static void checkOutput(String[] args, Path actual, Path expected) {
		checkOutput(args, Map.of(actual, expected));
	}

	/**
	 * Checks whether {@link Driver} generates the expected output without any
	 * exceptions. Will print the stack trace if an exception occurs. Designed to be
	 * used within an unit test. If the test was successful, deletes the actual
	 * files. Otherwise, keeps the files for debugging purposes.
	 *
	 * @param args arguments to pass to {@link Driver}
	 * @param files map of actual to expected files to test
	 */
	public static void checkOutput(String[] args, Map<Path, Path> files) {
		try {
			ArrayList<Executable> tests = new ArrayList<>();

			for (var entry : files.entrySet()) {
				Path actual = entry.getKey();
				Path expected = entry.getValue();

				// Remove old actual files (if exists), setup directories if needed
				Files.deleteIfExists(actual);
				Files.createDirectories(actual.toAbsolutePath().getParent());

				// Generate (but do not run) the output test
				Executable test = () -> {
					// Double-check we can read the expected output file
					if (!Files.isReadable(expected)) {
						String message = """
								\tUnable to read expected output file
								\t\tat %s""";

						Assertions.fail(() -> message.formatted(expected));
					}

					// Double-check we can read the actual output file
					if (!Files.isReadable(actual)) {
						String message = """
								\tUnable to read actual output file
								\t\tat %s""";

						Assertions.fail(() -> message.formatted(actual));
					}

					// Compare the two files
					int count = compareFiles(actual, expected);

					if (count <= 0) {
						String message = """
								\tUnexpected output on line %d
								\t\tat %s and
								\t\tat %s""";
						Assertions.fail(() -> message.formatted(-count, actual, expected));
					}

					// Clean up file if get this far
					Files.delete(actual);
				};

				// Add to the output tests
				tests.add(test);
			}

			// Generate actual output files
			testNoExceptions(args, LONG_TIMEOUT);

			// Compare all output files
			assertMultiple(tests, args, "Found error(s) while comparing file output.");
		}
		catch (Exception e) {
			StringBuilder debug = errorHeader(args, "Unexpected exception while comparing files.");
			Assertions.fail(debug.toString(), e);
		}
	}

	/**
	 * Checks whether {@link Driver} will run without generating any exceptions.
	 * Will print the stack trace if an exception occurs. Designed to be used within
	 * an unit test.
	 *
	 * @param args arguments to pass to {@link Driver}
	 * @param timeout the duration to run before timing out
	 */
	public static void testNoExceptions(String[] args, Duration timeout) {
		Assertions.assertTimeoutPreemptively(timeout, () -> {
			try {
				System.out.printf("%nRunning Driver %s...%n", String.join(" ", args));
				Driver.main(args);
			}
			catch (Exception e) {
				String summary = "Unexpected exception while running Driver.";
				StringBuilder debug = errorHeader(args, summary);

				debug.append("Error Details:\n");
				addFilteredTrace(debug, e);

				debug.append("\nFull Failure Trace:");
				Assertions.fail(debug.toString());
			}
		});
	}

	/**
	 * Attempts to test that multiple threads are being used in this code.
	 *
	 * @param action the action to run
	 */
	public static void testMultithreaded(Runnable action) {
		// time running Driver without any file output
		long[] times = new long[2];
		String[] args = new String[] { ProjectFlag.TEXT.flag, ProjectPath.TEXT.text,
				ProjectFlag.THREADS.flag, "2" };

		Assertions.assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			Instant start = Instant.now();
			Driver.main(args);

			times[1] = Duration.between(start, Instant.now()).toMillis();
			times[0] = start.toEpochMilli();
		});

		// get how long to pause when checking for multithreading
		long elapsed = times[1];
		long pause = Math.max(100, elapsed / 3);

		Assertions.assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			// get the non-worker threads that are running this test code
			List<String> before = activeThreads();

			// start up the Driver thread
			Thread driver = new Thread(action);
			driver.setPriority(Thread.MAX_PRIORITY);
			driver.start();

			// pause this thread for a bit (this is where things can go wrong)
			// this gives Driver a chance to start up its worker threads
			Thread.sleep(pause);

			// get the threads (ideally Driver should be up and running by this point)
			List<String> finish = activeThreads();

			// check that driver is still alive
			String error = "Something went wrong with the test code; see instructor. Elapsed: %d, Pause: %d";
			Assertions.assertTrue(driver.isAlive(), error.formatted(elapsed, pause));

			// wait for Driver to finish up
			driver.join();

			// try to figure out which ones are worker threads
			List<String> workers = new ArrayList<>(finish);
			workers.removeAll(before); // remove threads running this code
			workers.remove(driver.getName()); // remove the driver thread
			workers.removeIf(name -> name.startsWith("junit")); // remove junit timeout threads
			workers.removeIf(name -> name.startsWith("ForkJoinPool")); // remove other junit threads

			System.out.println("Workers: " + workers);

			String message = """
					Unable to detect any worker threads. Are you 100% positive threads
					are being created and used in your code? You can debug this by
					producing log output inside the run method of your thread objects.
					This is an imperfect test; if you are able to verify threads are
					being created and used, make a private post on the course forum.
					The instructor will look into the problem.
					""";
			String debug = "\nThreads Before: %s\nThreads After: %s\nWorker Threads: %s\nPaused: %d milliseconds\n\n%s\n";
			Assertions.assertTrue(workers.size() > 0, () -> debug.formatted(before, finish, workers, pause, message));
		});
	}

	/**
	 * Crafts an executable that tests whether a file exists.
	 *
	 * @param path the output file to test if exists
	 * @param flag the related flag that should trigger the file output
	 * @param exists whether to test if the file does or does not exist
	 * @return an executable
	 */
	public static Executable testFileExists(Path path, String flag, boolean exists) {
		String format = exists ? "Always create %s if the %s flag is present.\n" :
			"Never create %s if the %s flag is missing.\n";
		Supplier<String> debug = () -> format.formatted(path.toString(), flag);
		BiConsumer<Boolean, Supplier<String>> assertion = exists ? Assertions::assertTrue : Assertions::assertFalse;
		Executable test = () -> assertion.accept(Files.exists(path), debug);
		return test;
	}

	/**
	 * Returns a list of the active thread names (approximate).
	 *
	 * @return list of active thread names
	 */
	public static List<String> activeThreads() {
		int active = Thread.activeCount(); // only an estimate
		Thread[] threads = new Thread[active * 2]; // make sure large enough
		Thread.enumerate(threads); // fill in active threads
		return Arrays.stream(threads).filter(t -> t != null).map(Thread::getName).toList();
	}

	/**
	 * Generates the output file name to use given the prefix and path.
	 *
	 * @param prefix the prefix to use, like "index" or "results"
	 * @param path the input path being tested
	 * @return the output file name to use
	 */
	public static String outputFileName(String prefix, Path path) {
		// determine filename to use for actual/expected output
		if (Files.isDirectory(path)) {
			// e.g. index-simple.json
			return String.format("%s-%s.json", prefix, path.getFileName().toString());
		}

		// e.g. index-simple-hello.json
		String[] parts = path.getFileName().toString().split("\\.");
		String subdir = path.getParent().getFileName().toString();
		return String.format("%s-%s-%s.json", prefix, subdir, parts[0]);
	}

	/**
	 * Generates the output file name to use given the prefix, path, and number of
	 * threads.
	 *
	 * @param prefix the prefix to use, like "index" or "results"
	 * @param path the input path being tested
	 * @param threads the number of threads
	 * @return the output file name to use
	 */
	public static String outputFileName(String prefix, Path path, int threads) {
		// determine filename to use for actual/expected output
		if (Files.isDirectory(path)) {
			// e.g. index-simple-1.json
			return String.format("%s-%s-%d.json", prefix, path.getFileName().toString(), threads);
		}

		// e.g. index-simple-hello-1.json
		String[] parts = path.getFileName().toString().split("\\.");
		String subdir = path.getParent().getFileName().toString();
		return String.format("%s-%s-%s-%d.json", prefix, subdir, parts[0], threads);
	}

	/**
	 * Converts a map of flags and values to an array of command-line arguments.
	 *
	 * @param config the flag and value pairs
	 * @return command-line arguments
	 */
	public static String[] args(Map<ProjectFlag, String> config) {
		return config.entrySet()
				.stream()
				.flatMap(entry -> Stream.of(entry.getKey().flag, entry.getValue()))
				.filter(Objects::nonNull)
				.filter(Predicate.not(String::isBlank))
				.toArray(String[]::new);
	}

	/**
	 * Makes sure the expected environment is setup before running any tests.
	 */
	@BeforeAll
	public static void testEnvironment() {
		System.out.println("Using: " + EXPECTED.text);

		try {
			Files.createDirectories(ACTUAL.path);

			// check system environment
			Map<String, String> env = System.getenv();
			
			if (!env.containsKey("SKIP_ACTUAL_CLEANUP")) {
				// delete any old files located in actual directory
				System.out.println("Cleaning up old actual files...");
				Files.walk(ACTUAL.path).filter(Files::isRegularFile).forEach(path -> {
					try {
						Files.delete(path);
					}
					catch (IOException e) {
						System.out.println("Warning: Unable to delete actual file " + path.toString());
					}
				});
			}
			else {
				System.out.println("Skipping actual files cleanup...");
			}
		}
		catch (IOException e) {
			Assertions.fail("Unable to create actual output directory.");
		}

		try {
			// only make Windows files if necessary
			if (!File.separator.equals("/")) {
				System.out.println("Windows detected; generating expected files.");
				List<Path> copied = copyExpected();
				System.out.println("Copied: " + copied.size() + " files.");
				copied.forEach(System.out::println);
				System.out.println();
			}
		}
		catch (IOException e) {
			Assertions.fail("Unable to copy expected files for Windows systems.", e);
		}

		Assertions.assertAll(() -> Assertions.assertTrue(Files.isReadable(EXPECTED.path), EXPECTED.text),
				() -> Assertions.assertTrue(Files.isWritable(ACTUAL.path), ACTUAL.text),
				() -> Assertions.assertTrue(Files.isDirectory(TEXT.path), TEXT.text),
				() -> Assertions.assertTrue(Files.isDirectory(QUERY.path), QUERY.text));
	}

	/**
	 * Copies the expected files for Unix-like operating systems to expected files
	 * for Windows operating systems.
	 *
	 * @return all of the files copied
	 * @throws IOException if an IO error occurs
	 */
	public static List<Path> copyExpected() throws IOException {
		List<Path> copied = new ArrayList<>();
		Path nix = Path.of("expected-nix");
		Path win = Path.of("expected-win");

		// mixed file is a special case (think about a better way to handle in future)
		Path mixed = nix.resolve("crawl").resolve("special");

		// loop through each expected file
		try (Stream<Path> stream = Files.walk(nix, FileVisitOption.FOLLOW_LINKS)) {
			for (Path path : stream.toList()) {
				// path for windows version of expected file
				Path other = win.resolve(nix.relativize(path));

				// check if we need to re-copy the expected file
				if (Files.isRegularFile(path)) {
					if (!Files.isReadable(other) || Files.size(path) != Files.size(other)) {
						String original = Files.readString(path, StandardCharsets.UTF_8);

						if (path.startsWith(mixed) && path.getFileName().toString().startsWith("mixed-")) {
							String modified = original.replace("input/text/simple/hello.txt", "input\\text\\simple\\hello.txt");
							Files.writeString(other, modified, StandardCharsets.UTF_8);
						}
						else if (path.startsWith(nix.resolve("crawl"))) {
							Files.writeString(other, original, StandardCharsets.UTF_8);
						}
						else {
							String modified = original.replace('/', '\\');
							Files.writeString(other, modified, StandardCharsets.UTF_8);
						}

						copied.add(other);
					}
				}
				else if (Files.isDirectory(path) && !Files.exists(other)) {
					Files.createDirectories(other);
				}
			}
		}

		return copied;
	}

	/**
	 * Tries to extract a human-readable test name from the failure.
	 *
	 * @param failure the test failure
	 * @return readable test name of the failure
	 */
	public static String parseTestId(Failure failure) {
		// [engine:junit-jupiter]/[class:Project3aTest]/[nested-class:A_ThreadBuildTest]/[test-template:testText(int)]/[test-template-invocation:#1]
		TestIdentifier test = failure.getTestIdentifier();
		String id = test.getUniqueId();

		String regex = ".+?\\[nested-class:(.+?)\\].+?\\[(?:test-template|method):(.+?)\\](?:.*?\\[test-template-invocation:(.+?)\\])?";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(id);

		if (matcher.matches()) {
			String className = matcher.group(1);
			String methodName = matcher.group(2);
			String repeatName = matcher.group(3);

			repeatName = repeatName == null ? "" : repeatName;
			return className + "." + methodName + repeatName;
		}

		return id;
	}

	/**
	 * Encourages the garbage collector to run; useful in between intensive groups
	 * of tests or before benchmarking.
	 */
	public static void freeMemory() {
		Runtime runtime = Runtime.getRuntime();
		long bytes = 1048576;
		double before = (double) (runtime.totalMemory() - runtime.freeMemory()) / bytes;

		// try to free up memory before another run of intensive tests
		runtime.gc();

		// collect rest of system information
		int processors = runtime.availableProcessors();
		double maximum = (double) runtime.maxMemory() / bytes;
		double after = (double) (runtime.totalMemory() - runtime.freeMemory()) / bytes;

		String format = """

				```
				%8.2f Processors
				%8.2f MB Memory Maximum
				%8.2f MB Memory Used (Before GC)
				%8.2f MB Memory Used (After GC)
				```

				""";

		System.out.printf(format, (double) processors, maximum, before, after);
	}
}
