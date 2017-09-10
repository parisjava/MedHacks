package therapist.nlp.process;

import therapist.nlp.util.Init;

import edu.stanford.nlp.pipeline.*;
public class Main {
    public static void main(String args[]) {
	System.out.println(new Processor().process("i hate my job"));
    }
}
