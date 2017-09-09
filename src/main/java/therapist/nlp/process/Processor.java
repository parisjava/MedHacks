package therapist.nlp.processor;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
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
	int sentiment = 0;
	String cause = "";
	for (CoreMap sentence: document.get(
	    CoreAnnotations.SentencesAnnotation.class)) {

	    Tree tree = sentence.get(
	        SentimentCoreAnnotations.SentimentAnnotatedTree.class);

	    sentiment = RNNCoreAnnotations.getPredictedClass(tree);

	    for (CoreLabel token: sentence.get(
	        CoreAnnotations.TokensAnnotation.class)) {

		String word = token.get(
		    CoreAnnotations.TextAnnotation.class);
		String ner = token.get(
		    CoreAnnotations.NamedEntityTagAnnotation.class);

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
