package anagram.resource;

import anagram.model.Model;

public class Logic {

    static void printDistinctPermutations(String str, String ans) {

        //If the word's character string is empty
        if (str.length() == 0) {

            //print permutations as of right now
            System.out.print(ans + " ");
            Model.getAllpermutations().add(ans);
            return;
        }

        boolean alpha[] = new boolean[26];

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            //Rest of the string after excluding the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            //If the character has not been used -> recursive call
            if (alpha[ch - 'a'] == false) {
                printDistinctPermutations(ros, ans + ch);
                alpha[ch - 'a'] = true;
            }
        }

    }

}
