package edu.usfca.cs272.tests.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The commonly-used paths for testing.
 */
public enum ProjectPath {
	/** Path to the actual output files */
	ACTUAL("actual"),

	/** Path to the expected output files (based on type of slash) */
	EXPECTED(File.separator.equals("/") ? "expected-nix" : "expected-win"),

	/** Path to the input files */
	INPUT("input"),

	/** Path to the input text files */
	TEXT("input", "text"),

	/** Path to the input simple text files */
	SIMPLE("input", "text", "simple"),

	/** hello.txt */
	HELLO("input", "text", "simple", "hello.txt"),

	/** empty.txt */
	EMPTY("input", "text", "simple", "empty.txt"),

	/** Path to the input stems text files */
	STEMS("input", "text", "stems"),

	/** stems-in.txt */
	STEMS_IN("input", "text", "stems", "stem-in.txt"),

	/** stems-out.txt */
	STEMS_OUT("input", "text", "stems", "stem-out.txt"),

	/** Path to the input RFC text files */
	RFCS("input", "text", "rfcs"),

	/** UTF-8, a transformation format of ISO 10646 */
	RFCS_UTF8("input", "text", "rfcs", "rfc3629.txt"),

	/** FTP AND NETWORK MAIL SYSTEM */
	RFCS_FTP("input", "text", "rfcs", "rfc475.txt"),

	/** Tags for Identifying Languages */
	RFCS_TAGS("input", "text", "rfcs", "rfc5646.txt"),

	/** PCE Hierarchy Framework */
	RFCS_PCE("input", "text", "rfcs", "rfc6805.txt"),

	/** Media Type Specifications and Registration Procedures */
	RFCS_MEDIA("input", "text", "rfcs", "rfc6838.txt"),

	/** Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content */
	RFCS_HTTP("input", "text", "rfcs", "rfc7231.txt"),

	/** Path to the input guten text files */
	GUTEN("input", "text", "guten"),

	/** The Elements of Style by William Strunk */
	GUTEN_STYLE("input", "text", "guten", "pg37134.txt"),

	/** Practical Grammar and Composition by Thomas Wood */
	GUTEN_GRAMMAR("input", "text", "guten", "pg22577.txt"),

	/** Adventures of Sherlock Holmes by Arthur Conan Doyle */
	GUTEN_HOLMES("input", "text", "guten", "pg1661.txt"),

	/** Leaves of Grass by Walt Whitman */
	GUTEN_GRASS("input", "text", "guten", "pg1322.txt"),

	/** ALGOL Compiler by L. L. Bumgarner */
	GUTEN_ALGOL("input", "text", "guten", "50468-0.txt"),

	/** Moby Dick by Herman Melville */
	GUTEN_MOBY("input", "text", "guten", "2701-0.txt"),

	/** Great Expectations by Charles Dickens */
	GUTEN_GREAT("input", "text", "guten", "1400-0.txt"),

	/** Path to the input query files */
	QUERY("input", "query"),

	/** Path to the input query simple file */
	QUERY_SIMPLE("input", "query", "simple.txt"),

	/** Path to the input query simple file */
	QUERY_WORDS("input", "query", "words.txt"),

	/** Path to the input query simple file */
	QUERY_RESPECT("input", "query", "respect.txt"),

	/** Path to the input query simple file */
	QUERY_LETTERS("input", "query", "letters.txt"),

	/** Path to the input query simple file */
	QUERY_COMPLEX("input", "query", "complex.txt");

	/** The normalized relative path */
	public final Path path;

	/** The normalized relative path as text */
	public final String text;

	/** The name to use in output files */
	public final String id;

	/**
	 * Initializes one of the paths used by the search engine project.
	 *
	 * @param paths the subpaths to use to create a path object
	 */
	private ProjectPath(String... paths) {
		this.path = Path.of(".", paths).normalize();
		this.text = this.path.toString();
		this.id = id(this.path);
	}

	/**
	 * Wrapper for {@link Path#resolve(String)}.
	 *
	 * @param other the path string to resolve against this path
	 * @return the resulting path
	 */
	public Path resolve(String other) {
		return this.path.resolve(other);
	}

	/**
	 * Returns the json file suffix for the provided path. If the path is a
	 * directory, the suffix will be the directory name (e.g. "simple.json"). If the
	 * path is not a directory, then the suffix will be the directory and file name
	 * without extension (e.g. "simple-hello.json").
	 *
	 * @param path the path
	 * @return the json file suffix for this path
	 */
	public static String id(Path path) {
		if (Files.isDirectory(path)) {
			return path.getFileName().toString();
		}

		String[] parts = path.getFileName().toString().split("\\.");
		Path parent = path.getParent();

		if (parent == null) {
			return parts[0];
		}

		String subdir = parent.getFileName().toString();
		return subdir + "-" + parts[0];
	}
}
