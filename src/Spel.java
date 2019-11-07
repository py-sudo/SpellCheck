import java.io.IOException;
import java.util.*;
/*
 * Peiyi Guan
 * 215328917
 * spell check & correction
 */

public class Spel implements AbsSpel {
    // constructor
    public Set<String> spellCheckingDictionary;
    public SpinDictinary sd;

    public Spel(){
        this.spellCheckingDictionary = new HashSet<String>();
        this.sd = new SpinDictinary();
    }
    public void checker(String textFile){
        try{
            ParseWord input = new ParseWord(textFile);
            String currentWord = input.nextWord();
            while(currentWord!=""){
                if(!this.spellCheckingDictionary.contains(currentWord)) {
                    this.corrector(currentWord);
                }
                currentWord = input.nextWord();
            }
        } catch(IOException ex){
            System.out.println (ex.toString());
            System.out.println("Could not find file " + textFile);
        }

    }
    public void buildDictionary(String dictFile){
        try{
            ParseWord parsedWords = new ParseWord(dictFile);
            String currentWord = parsedWords.nextWord();
            while (currentWord!=""){
                int len = currentWord.length();
                // spining word
                SpinWord sw = new SpinWord(currentWord);
                while(sw.hasNext()){
                    this.sd.add(len,sw.next());
                }
                // store the current word for spell checking
                this.spellCheckingDictionary.add(currentWord);
                currentWord = parsedWords.nextWord();
            }

        } catch(IOException ex){
            System.out.println (ex.toString());
            System.out.println("Could not find file " + dictFile);
        }


    }

    public void corrector(String word) {
        List<String> omssionSuggestion = this.checkOmission(word);
        List <String> additionSuggestion = this.checkAddition(word);
        List <String> ajacentSwapSuggestion = this.checkAjacentSwap(word);
        List <String> typoSuggestion = this.checkTypo(word);
        Collections.sort(omssionSuggestion);
        Collections.sort(additionSuggestion);
        Collections.sort(ajacentSwapSuggestion);
        Collections.sort(typoSuggestion);
        Iterator it1 = omssionSuggestion.iterator();
        Iterator it2 = additionSuggestion.iterator();
        Iterator it3 = ajacentSwapSuggestion.iterator();
        Iterator it4 = typoSuggestion.iterator();
        System.out.print(word +"?");
        while(it1.hasNext()){
            System.out.print("  "+it1.next());
        }
        while(it2.hasNext()){
            System.out.print("  "+it2.next());
        }
        while(it3.hasNext()){
            System.out.print("  "+it3.next());
        }
        while(it4.hasNext()){
            System.out.print("  "+it4.next());
        }
        System.out.println();
    }

    public ArrayList<String> checkOmission(String word){
        ArrayList <String> omissionCorrection = new ArrayList<>();
        if(word.length()==1){
            String [] arr = new String[2];
            arr[0] = word+".";
            arr[1] = "."+word;
            for(int i=0;i<arr.length;i++){
                SortedSet set = this.sd.subSet(word.length()+1,arr[i]+"@",arr[i]+"~");
                Iterator itSet = set.iterator();
                while (itSet.hasNext()){
                    String currentWord = (String)itSet.next();
                    String container = SpinWord.unSpin(currentWord);
                    if(!omissionCorrection.contains(container))
                        omissionCorrection.add(container);
                }
            }
        }else{

            SpinWord sw = new SpinWord(word);
            while (sw.hasNext()){
                String currentSpinedWord = sw.next();
                SortedSet set = this.sd.subSet(word.length()+1,currentSpinedWord+"@",currentSpinedWord+"~");
                if(set!=null && !set.isEmpty()){
                    Iterator itSet = set.iterator();
                    while (itSet.hasNext()){
                        String suggestWord = (String) itSet.next();
                        String container = SpinWord.unSpin(suggestWord);
                        if(!omissionCorrection.contains(container))
                            omissionCorrection.add(container);
                    }
                }

            }

        }

        return omissionCorrection;
    }
    public ArrayList<String> checkTypo(String word){
        ArrayList <String> typoCorrection = new ArrayList<>();
        if(word.length()>1){
            SpinWord sw = new SpinWord(word);
            while(sw.hasNext()){
                String currentSpinedWord = sw.next();
                String typoWord = currentSpinedWord.substring(0,currentSpinedWord.length()-1);
                SortedSet set = this.sd.subSet(word.length(),typoWord+"@",typoWord+"~");
                if(set!=null && !set.isEmpty()){
                    Iterator itSet = set.iterator();
                    while (itSet.hasNext()){
                        String suggestWord = (String) itSet.next();
                        String container = SpinWord.unSpin(suggestWord);
                        if(!typoCorrection.contains(container))
                            typoCorrection.add(container);
                    }
                }
            }
        }

        return typoCorrection;
    }

    public ArrayList<String>checkAddition(String word){
        ArrayList <String> additionCorrection = new ArrayList<>();
        int len = word.length();
        for(int i=0;i<len;i++){
            char letter = word.charAt(len-i-1);
            int index = word.indexOf(letter);
            String front = word.substring(0,index);
            String back = word.substring(index+1);
            String result = front+back;
            if(this.spellCheckingDictionary.contains(result) && !additionCorrection.contains(result))
                    additionCorrection.add(result);
        }
        return additionCorrection;
    }
    public ArrayList<String> checkAjacentSwap(String word){
        ArrayList <String> ajacentSwapCorrection = new ArrayList<>();
        String str = new String(word);
        for(int i=0;i<str.length()-1;i++){
            StringBuilder temp = new StringBuilder(str);
            char a = temp.charAt(i);
            char b = temp.charAt(i+1);
            temp.setCharAt(i,b);
            temp.setCharAt(i+1,a);
            //check if temp is in the dictionary
            String tempStr = temp.toString();
            if(this.spellCheckingDictionary.contains(tempStr) && !ajacentSwapCorrection.contains(tempStr)){
                ajacentSwapCorrection.add(tempStr);
            }
        }
        return ajacentSwapCorrection;
    }

    public static void main(String args[]){

        if (args.length < 2) {
            System.out.println("Usage : Spel : dictionary_file file_to_spellcheck+");
            System.exit(-1);
        }

        int len = args.length;
        Spel s = new Spel();
        s.buildDictionary(args[0]);

        for (int i = 1; i < len; i++) {
            System.out.println();
            System.out.println("Checking " + args[i] +" for spell corrections...");
            System.out.println();
            s.checker( args[i]);
            System.out.println();
        }


    }

}
