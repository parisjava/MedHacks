package therapist.nlp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonReader;
import javax.json.JsonObject;

import java.io.PrintWriter;
import java.io.IOException;

import therapist.nlp.process.Processor;

public class AlexaServlet extends HttpServlet {

    Processor processor;
    public void init() throws ServletException {
	processor = new Processor();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
	response.setContentType("text/html");

	try (PrintWriter out = response.getWriter()) {
	    out.println("<h1>This shit works</h1>");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
	try (JsonReader reader = Json.createReader(request.getInputStream())) {
	    JsonObject object = reader.readObject();

	    String version = object.getString("version");
	    
	    String intent = object.getJsonObject("request").getJsonObject("intent").getString("name");

	    JsonObjectBuilder output = null;
	    
	    if (intent.equals("Database")) {
		String input = object.getJsonObject("request")
		    .getJsonObject("intent").getJsonObject("slots")
                        .getJsonObject("TEXT").getString("value");
		String result = processor.process(input);
		output = setResponse(result, version, true);
	    } else if (intent.equals("AMAZON.StopIntent")) {
		output = setResponse("Bye", version, false);
	    } else {
		output = setResponse("Hi", version, true);
	    }

	    try (PrintWriter out = response.getWriter()) {
		Json.createWriter(out).write(output.build());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	

	
	
    }

    private JsonObjectBuilder setResponse(String outputSpeech, String version, boolean reprompt) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("version", version);

        JsonObjectBuilder outputSpeechBuilder = Json.createObjectBuilder();
        outputSpeechBuilder.add("type", "SSML");
        outputSpeechBuilder.add("ssml", "<speak>" + outputSpeech + "</speak>");
        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
        responseBuilder.add("outputSpeech", outputSpeechBuilder);
        responseBuilder.add("shouldEndSession", !reprompt);
        /*
         * if (reprompt) { JsonObjectBuilder repromptBuilder =
         * Json.createObjectBuilder(); repromptBuilder.add("outputSpeech",
         * outputSpeechBuilder); responseBuilder.add("reprompt",
         * repromptBuilder); }
         */
        builder.add("response", responseBuilder);
        builder.add("sessionAttributes", Json.createObjectBuilder().build());

        return builder;
    }
}
