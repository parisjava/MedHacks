package therapist.nlp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.PrintWriter;
import java.io.IOException;

public class TestServlet extends HttpServlet {

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
	response.setContentType("text/html");

	try (PrintWriter out = response.getWriter()) {
	    out.println("<h1>This shit works</h1>");
	} catch (IOException e) {

	}
    }
}
