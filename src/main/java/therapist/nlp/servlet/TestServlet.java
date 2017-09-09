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

public class TestServlet extends HttpServlet {

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
	    String question = object.getString("request");
	    String result  = processor.process(question);

	    try (PrintWriter out = response.getWriter()) {
		Json.createWriter(out).write(createObject(result));
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	

	
	
    }

    private JsonObject createObject(String response) {
	JsonObjectBuilder builder = Json.createObjectBuilder();
	builder.add("response" , response);
	return builder.build();
    }
}
