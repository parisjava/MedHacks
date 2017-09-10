package therapist.nlp.process;

import therapist.nlp.util.Init;

import edu.stanford.nlp.pipeline.*;
public class Main {
    public static void main(String args[]) {
	StanfordCoreNLP pipeline = new StanfordCoreNLP(Init.pipeline);
	System.out.println(new Processor().process("i hate work", pipeline));
    }
}
