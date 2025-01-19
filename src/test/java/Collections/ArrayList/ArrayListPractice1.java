package Collections.ArrayList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayListPractice1 {


    // Common ArrayList interview questions and answers
    /**
     * Q: What is an ArrayList in Java?
     * A: ArrayList is a resizable array implementation of the List interface.
     * It allows for dynamic resizing and provides methods to manipulate the size of the array that is used internally to store the list.
     *
     * Q: How does ArrayList differ from an array?
     * A: Unlike arrays, ArrayList can dynamically grow and shrink in size.
     * Arrays have a fixed size, while ArrayList can adjust its size as elements are added or removed.
     *
     * Q: How do you add elements to an ArrayList?
     * A: You can add elements to an ArrayList using the add() method. For example: arrayList.add("element");
     *
     * Q: How do you remove elements from an ArrayList?
     * A: You can remove elements from an ArrayList using the remove() method. For example: arrayList.remove("element") or arrayList.remove(index);
     *
     * Q: How do you iterate over an ArrayList?
     * A: You can iterate over an ArrayList using a for loop, enhanced for loop, Iterator, or streams.
     *
     * Q: Is ArrayList synchronized?
     * A: No, ArrayList is not synchronized. If multiple threads access an ArrayList concurrently, it must be synchronized externally.
     *
     * Q: How do you convert an ArrayList to an array?
     * A: You can convert an ArrayList to an array using the toArray() method. For example: String[] array = arrayList.toArray(new String[0]);
     *
     * Q: What is the default initial capacity of an ArrayList?
     * A: The default initial capacity of an ArrayList is 10.
     *
     * Q: How does ArrayList handle resizing?
     * A: When the ArrayList exceeds its current capacity, it increases its capacity by 50% of the current size.
     */

    public static void main(String[] args) {

        ArrayList<String> stockslist = arrayListAndLoop();

        for(int i = 0; i<stockslist.size();i++){

            if(stockslist.get(i).toString().equalsIgnoreCase("AAPL")){
                stockslist.remove(i);
            }

        }

        if(stockslist.contains("AAPL")){
            System.out.println("true, AAPL is not in the list");
        }else{
            System.out.println("false, AAPL is not in the list");

            if(stockslist.contains("SPOT")){
                System.out.println("true, SPY is in the list");
            }
        }

        stockslist.add("CRWD");

        System.out.println(stockslist.get(0));

        System.out.println(stockslist.get(9));

        Iterator itr = stockslist.iterator();

        while (itr.hasNext()){
            System.out.println("stock: "+itr.next());
        }

        //provide another way to iterate oover the array
        for(int i = 0; i<stockslist.size();i++){
            System.out.println("stock: "+stockslist.get(i));
        }

        //iterate over the array using streams
        stockslist.stream().forEach(stock -> System.out.println("stock: "+stock));

        //use streams on the array and filter for a specific stock
        stockslist.stream().filter(stock -> stock.equalsIgnoreCase("AMZN")).forEach(stock -> System.out.println("AMZN stock: "+stock));


        ArrayList<String> stockListMyFavorites = new ArrayList<>();

        stockListMyFavorites.addAll(stockslist);

        for(String s : stockListMyFavorites){
            System.out.println("new stock favorite list "+ s);
        }


       int arrayListSize = stockListMyFavorites.size();

        System.out.println("arraylist size is :"+ arrayListSize);

       boolean isArrayListEmpty = stockListMyFavorites.isEmpty();

        System.out.println("is my list empty? : "+ isArrayListEmpty);

        stockListMyFavorites.clear();

        isArrayListEmpty = stockListMyFavorites.isEmpty();


        System.out.println("is my list empty after clearing? : "+ isArrayListEmpty);

        int indexOfMyFavoriteStock = stockslist.indexOf("TSLA");

        System.out.println("index of my favorite stock, TSLA :" +indexOfMyFavoriteStock);



    }



    public static ArrayList<String> arrayListAndLoop(){

        ArrayList<String> topTenTechStocksList = new ArrayList<>();

        topTenTechStocksList.add("TSLA");
        topTenTechStocksList.add("AAPL");
        topTenTechStocksList.add("NVDA");
        topTenTechStocksList.add("META");
        topTenTechStocksList.add("AMD");
        topTenTechStocksList.add("GOOGL");
        topTenTechStocksList.add("SPOT");
        topTenTechStocksList.add("NFLX");
        topTenTechStocksList.add("AMZN");
        topTenTechStocksList.add("MSFT");


        for(String s : topTenTechStocksList){
            System.out.println(s);
        }

        return topTenTechStocksList;

    }
}
