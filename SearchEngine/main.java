package finalproject;
import java.util.ArrayList;


public class main {

    public static void main(String[] args) {
        try {
            // Instantiate SearchEngine with the XML file path
            SearchEngine searchEngine = new SearchEngine( "test.xml");

            // Perform crawling, indexing, and page rank assignment
            searchEngine.crawlAndIndex("www.cs.mcgill.ca");
            searchEngine.assignPageRanks(0.001);

            // Get search results for the query "and"
            ArrayList<String> results = searchEngine.getResults("and");

            // Print out the search results
            printSearchResults(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printSearchResults(ArrayList<String> results) {
        // Print out the search results
        for (int i = 0; i < results.size(); i++) {
            System.out.println("Rank " + (i+1) + ": " + results.get(i));
        }
    }
}