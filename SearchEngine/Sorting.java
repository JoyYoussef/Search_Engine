package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		ArrayList<K> unsorted = new ArrayList<>(results.keySet());
		ArrayList<K> sorted = mergeSort(unsorted,results);
		return sorted;


	}
	private static <K, V extends Comparable<V>> ArrayList<K> mergeSort(ArrayList<K> unsorted, HashMap<K,V> results){

		if(unsorted.size()<=1)
			return unsorted;

		else{
			int mid=unsorted.size()/2;

			ArrayList<K> list1 = new ArrayList<>();
			for(int i = 0; i < mid; i++)
				list1.add(unsorted.get(i));

			ArrayList<K> list2 = new ArrayList<>();
			for(int j = mid; j <unsorted.size(); j++)
				list2.add(unsorted.get(j));

			list1 =mergeSort(list1,results);
			list2 =mergeSort(list2,results);
			return merge (list1,list2,results);
		}

    }

	private static <K, V extends Comparable<V>> ArrayList<K> merge(ArrayList<K> list1, ArrayList<K> list2, HashMap<K, V> results) {
		ArrayList<K> list = new ArrayList<>();

		int index1 = 0;
		int index2 = 0;


		while (index1 < list1.size() && index2 < list2.size()) {

			V value1= results.get(list1.get(index1));
			V value2= results.get(list2.get(index2));

			if (value1.compareTo(value2) < 0){
				list.add(list2.get(index2));
				index2++;
			} else {
				list.add(list1.get(index1));
				index1++;
			}
		}

		while (index1 < list1.size()) {
			list.add(list1.get(index1));
			index1++;
		}

		while (index2 < list2.size()) {
			list.add(list2.get(index2));
			index2++;
		}

		return list;
	}

}