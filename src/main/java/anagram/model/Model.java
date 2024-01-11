package anagram.model;

import java.util.ArrayList;

public class Model {

    private static ArrayList<Object> allpermutations = new ArrayList<>();
    private static ArrayList<Object> anagrams = new ArrayList<>();
    private static ArrayList<Object> anagramdata = new ArrayList<>();

    public static ArrayList<Object> getAllpermutations() {
        return allpermutations;
    }

    public static void setAllpermutations(ArrayList<Object> allpermutations) {
        Model.allpermutations = allpermutations;
    }

    public static ArrayList<Object> getAnagrams() {
        return anagrams;
    }

    public static void setAnagrams(ArrayList<Object> anagrams) {
        Model.anagrams = anagrams;
    }

    public static ArrayList<Object> getAnagramdata() {
        return anagramdata;
    }

    public static void setAnagramdata(ArrayList<Object> anagramdata) {
        Model.anagramdata = anagramdata;
    }


}
