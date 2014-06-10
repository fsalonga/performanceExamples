/*
 * Copyright 2012 MarkLogic Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.performanceExamples;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;

/**
 * KeyValueSearch illustrates searching for a key/value pair among JSON documents.
 */
public class TutorialUtil {

	public static void displayResultURIs(SearchHandle resultsHandle) {
		// Get the list of matching documents in this page of results
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		
		System.out.println("Listing " + results.length + " documents:");
		
		// List the URI of each matching document
		for (MatchDocumentSummary result : results) {
			System.out.println(result.getUri());
		}
	}
	
	public static void displayJSONResultDocs(SearchHandle resultsHandle, DatabaseClient client) {
		JSONDocumentManager mgr = client.newJSONDocumentManager();
		
		// iterate over the result documents
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		System.out.println("Listing "+results.length+" documents:\n");
		for (MatchDocumentSummary result: results) {
			StringHandle content = new StringHandle();
			mgr.read(result.getUri(), content);
			System.out.println(content.get());
		}
	}
	
	public static void displayResults(SearchHandle resultsHandle) {
		System.out.println("Matched "+resultsHandle.getTotalResults()+" documents\n");

		// Get the list of matching documents in this page of results
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		System.out.println("Listing "+results.length+" documents:\n");
		
		// Iterate over the results
		for (MatchDocumentSummary result: results) {

			// get the list of match locations for this result
			MatchLocation[] locations = result.getMatchLocations();
			System.out.println("Matched "+locations.length+" locations in "+result.getUri()+":");

			// iterate over the match locations
			for (MatchLocation location: locations) {

				// iterate over the snippets at a match location
				for (MatchSnippet snippet : location.getSnippets()) {
					boolean isHighlighted = snippet.isHighlighted();

					if (isHighlighted)
						System.out.print("[");
					System.out.print(snippet.getText());
					if (isHighlighted)
						System.out.print("]");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}
