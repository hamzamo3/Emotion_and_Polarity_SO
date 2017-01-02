package tokenizer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
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
                    "ptb3Escaping=false,untokenizable=allKeep,tokenizeNLs=true"
                    );
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                String s =  String.valueOf(PTBTokenizer.getNewlineToken());
                if (String.valueOf(label).equals(s)) {
                    fw.append(System.lineSeparator());
                }
                else {
                    //controllo dei doppi apici,'\n','\r'
                    switch(String.valueOf(label).charAt(0)){
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
                            fw.append(label + " ");
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
    public  void tokenizerBySentence(String pathIn,String pathOut) throws IOException {
        String line = new String("");
        String outfile = new String(pathOut);
        FileWriter out = new FileWriter(outfile);

            // option #1: By sentence.
            DocumentPreprocessor dp = new DocumentPreprocessor(pathIn);
            for (List<HasWord> sentence : dp) {
                for (HasWord word : sentence) {
                    line = line.concat(word + " ");
                }

                System.out.println(sentence);
                out.append(line);
                out.append(System.lineSeparator());
                line = "";

            }

            out.flush();
            out.close();

    }

}