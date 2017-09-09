package therapist.nlp.processor;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.pipeline.*;
import static edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.AbstractCoreLabel;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;

public class Processor {

    public Processor() {
    }

    public String process(String text, StanfordCoreNLP pipeline) {
	Annotation document = new Annotation(text);
	pipeline.annotate(document);
	int sentiment;
	String cause = "";
	for (CoreMap sentence: document.get(SentencesAnnotation.class)) {
	    Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
	    sentiment = RNNCoreAnotations.getPredictedClass(tree);
	    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		String word = token.get(TextAnnotation.class);
		String ner = token.get(NamedEntityTagAnnotation.class);
		if (ner.equalsIgnoreCase("CAUSE")) {
		    cause = word;
		}	      
	    }
	}

	if (sentiment < 2) {
	    return "I am sorry to hear that you are having " + cause + " issues. Please contact a therapist!";
	}

        return "Glad to see you are having your a nice day";

	
    }
}
