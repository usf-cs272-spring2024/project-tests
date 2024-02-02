package edu.usfca.cs272.tests.utils;

import java.nio.file.Path;

/**
 * The command-line flag/value arguments supported by the search engine project.
 */
public enum ProjectFlag {
	/** Flag to indicate the path to index as text */
	TEXT("-text", null),

	/** Flag to indicate the queries to search for in the index */
	QUERY("-query", null),

	/** Flag to indicate whether to conduct an exact or partial search */
	PARTIAL("-partial", null),

	/** Flag to indicate the URL to index as html */
	HTML("-html", null),

	/** Flag to enable threading with the specified number of threads */
	THREADS("-threads", "5"),

	/** Flag to set the maximum number of URLs to process as HTML */
	MAX("-crawl", "1"),

	/** Flag to indicate whether to launch a web server on the specified port */
	SERVER("-server", "8080"),

	/** Flag to indicate whether to output the word counts to JSON */
	COUNTS("-counts", "counts.json"),

	/** Flag to indicate whether to output the inverted index to JSON */
	INDEX("-index", "index.json"),

	/** Flag to indicate whether to output the search results to JSON */
	RESULTS("-results", "results.json");

	/** The flag with the leading dash */
	public final String flag;

	/** The default value if missing or null if no default value */
	public final String value;

	/** The default value if missing or null as a path */
	public final Path path;

	/**
	 * Initializes this command-line argument flag and optionally its default value.
	 *
	 * @param flag the flag with the leading dash
	 * @param value the default value if missing or null if no default value
	 */
	private ProjectFlag(String flag, String value) {
		this.flag = flag;
		this.value = value;
		this.path = value == null ? null : Path.of(value);
	}
}
