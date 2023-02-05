package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{
    private Trie dict = new Trie();

    private SortedSet<String> candidate = new TreeSet<>();
    private String answer = null;

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            dict.add(scanner.next());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        candidate.clear();
        if(inputWord.isEmpty()) {
            return null;
        }
        if(dict.find(inputWord.toLowerCase()) != null) {
            return inputWord.toLowerCase();
        }
        int frequency = 0;
        HashSet<String> distOne = distOne_Helper(inputWord.toLowerCase());

        for(String one : distOne) {
            if (dict.find(one) != null) {
                if(dict.find(one).getValue() > frequency) {
                    candidate.clear();
                    frequency = dict.find(one).getValue();
                    candidate.add(one);
                }
                if(dict.find(one).getValue() == frequency) {
                    candidate.add(one);
                }
            }
        }
        if(candidate.size() > 0) {
            return candidate.stream().findFirst().get();
        }

        HashSet<String> distTwo = new HashSet<>();
        for(String one : distOne) {
            distTwo.addAll(distOne_Helper(one));
        }

        for(String two : distTwo) {
            if (dict.find(two) != null) {
                if(dict.find(two).getValue() > frequency) {
                    candidate.clear();
                    frequency = dict.find(two).getValue();
                    candidate.add(two);
                }
                if(dict.find(two).getValue() == frequency) {
                    candidate.add(two);
                }
            }
        }
        if(candidate.size() > 0) {
            return candidate.stream().findFirst().get();
        }
        return null;
    }

    private HashSet<String> distOne_Helper(String word) {
        HashSet<String> distWord = new HashSet<>();
        // add delete
        for(int i = 0; i < word.length(); i++) {
            StringBuilder delete = new StringBuilder(word.toLowerCase());
            delete.deleteCharAt(i);
            if(!delete.toString().isEmpty()) {
                distWord.add(delete.toString());
            }
        }
        // add trans
        for(int i = 0; i < word.length(); i++) {
            if(i != word.length() - 1) {
                StringBuilder trans = new StringBuilder(word.toLowerCase());
                char temp1 = trans.charAt(i);
                char temp2 = trans.charAt(i + 1);
                trans.setCharAt(i, temp2);
                trans.setCharAt(i+1, temp1);
                distWord.add(trans.toString());
            }
        }
        // add alter
        for(int i = 0; i < word.length(); i++) {
            StringBuilder alter = new StringBuilder(word.toLowerCase());
            for(char c = 'a'; c <= 'z'; c++) {
               if(c != alter.charAt(i)) {
                   alter.setCharAt(i, c);
                   distWord.add(alter.toString());
               }
            }
        }
        // add insert
        for(int i = 0; i < word.length() + 1; i++) {
            for(char c = 'a'; c <= 'z'; c++) {
                StringBuilder ins = new StringBuilder(word.toLowerCase());
                ins.insert(i, c);
                distWord.add(ins.toString());
            }
        }
        return distWord;
    }
}
