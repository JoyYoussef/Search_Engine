package finalproject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		internet.setVisited(url, true);
		internet.addVertex(url);

		ArrayList<String> links = parser.getLinks(url);
		for (String link : links) {
			if (!internet.getVisited(link)) {
				crawlAndIndex(link);
			}
			internet.addEdge(url, link);
		}
		ArrayList<String> content = parser.getContent(url);
		for (String word : content) {
			String lowerCase = word.toLowerCase();
			if (wordIndex.containsKey(lowerCase)) {
				if (!wordIndex.get(lowerCase).contains(url))
					wordIndex.get(lowerCase).add(url);
			}
			else {
				ArrayList<String> values = new ArrayList<>();
				values.add(url);
				wordIndex.put(lowerCase, values);
			}

		}

	}


	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {

		ArrayList<String> vertices = internet.getVertices();
		ArrayList<Double> prevRank = new ArrayList<>();

		for (String vertex : vertices) {
			internet.setPageRank(vertex, 1.0);
			prevRank.add(1.0);
		}

		boolean done = false;
		while (!done) {
			done = true;
			ArrayList<Double> currRank = computeRanks(vertices);

			for (int i = 0; i < vertices.size(); i++) {
				String vertex = vertices.get(i);
				double rank = currRank.get(i);
				internet.setPageRank(vertex, rank);
				if (Math.abs(prevRank.get(i) - rank) >= epsilon)
					done = false;
			}
			prevRank = currRank;
		}


	}


	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 *
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList<Double> ranks = new ArrayList<>();

		for (String vertex : vertices) {
			ArrayList<String> ws = internet.getEdgesInto(vertex);
			double v = 0.5;
			for (String w : ws) {
				if (internet.getPageRank(w) == 0)
					internet.setPageRank(w, 1);
				v += 0.5 * (internet.getPageRank(w)) / (internet.getOutDegree(w));
			}
			ranks.add(v);
		}
		return ranks;

	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
		String finalQuery = query.toLowerCase();

		if (wordIndex.containsKey(finalQuery) && !wordIndex.get(finalQuery).isEmpty()) {
			ArrayList<String> urls = wordIndex.get(finalQuery);
			HashMap<String, Double> pageToRank = new HashMap<>();
			for (String url : urls) {
				double rank = internet.getPageRank(url);
				pageToRank.put(url, rank);
			}
			return Sorting.fastSort(pageToRank);
		} else {
			 return new ArrayList<>();
		}
	}

}
