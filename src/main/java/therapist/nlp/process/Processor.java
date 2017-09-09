package therapist.nlp.process;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.AbstractCoreLabel;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;

import therapist.nlp.util.Init;

import java.sql.SQLException;

public class Processor {

    StanfordCoreNLP pipeline;
    public Processor() {
	pipeline = new StanfordCoreNLP(Init.pipeline);
    }

    public String process(String text) {
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
	    return databaseAccess(cause);
	}

        return "Glad to see everything is going well with the " + cause;

	
    }

    private String databaseAccess(String cause) {
	String url = Init.database.getProperty("url");
	try (Database db = new Database(url, Init.database)) {
	    return db.query(cause); 
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return "Sorry, im having some trouble now";
	}
    }
}
