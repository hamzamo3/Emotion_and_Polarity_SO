package tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import reading.ReadingFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static edu.stanford.nlp.scoref.BestFirstCorefSystem.i;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nicole
 */
public class TokenizeCorpus {

    public void tokenizerByToken(String pathIn,String pathOu) throws FileNotFoundException {
        //by token
        File inputTokenized = new File(pathOu);
        FileWriter fw = null;
        List<String> tk = new ArrayList<>();
        try {
            fw = new FileWriter(inputTokenized);
            System.out.println("Tokenizing input corpus ...");
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(pathIn), new CoreLabelTokenFactory(),
                    //se metti untokenizable = noneDelete
                    "ptb3Escaping=false,untokenizable=noneDelete,tokenizeNLs=true"
                    );
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                String labelLowCase= String.valueOf(label).toLowerCase();
                String s =  String.valueOf(PTBTokenizer.getNewlineToken());
                if (String.valueOf(label).equals(s)) {
                    fw.append(System.lineSeparator());
                }
                else {
                    //controllo dei doppi apici,'\n','\r'
                    switch(labelLowCase.charAt(0)){
                        case '"':
                            label.setValue("");
                            fw.append(label + " ");
                            continue;
                        case '\n':
                            label.setValue("");
                            fw.append(label + " ");
                            continue;
                        case '\r':
                            label.setValue("");
                            fw.append(label + " ");
                            continue;
                        default :
                            fw.append(labelLowCase + " ");
                    }

                }
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //by sentence
    public List<String> tokenizerBySentence(String doc){
        List<String> sentences = new ArrayList<>();
        String line ="";
        Reader reader = new StringReader(doc);
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        for (List<HasWord> sentence : dp) {
            for (HasWord word : sentence) {
                line = line.concat(word + " ");
            }
            sentences.add(Sentence.listToString(sentence));
            System.out.println(Sentence.listToString(sentence));
            line = "";
        }
        return sentences;
    }

}
