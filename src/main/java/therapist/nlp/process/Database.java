package therapist.nlp.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;


public class Database implements AutoCloseable {

    Connection connection = null;
    
    public Database(String url, Properties properties) throws SQLException {
	this.connect(url, properties);
    }

    public void connect(String url, Properties properties) throws SQLException {
	this.connection = DriverManager.getConnection(url, properties);
    }

    public String query(String cause) {
	String sqlQuery = "Select Question From " + cause +
	    " ORDER BY Random() LIMIT 1";

	try (Statement statement = connection.createStatement()) {
	    return  getQuestion(statement.executeQuery(sqlQuery));
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return fallBack(cause);
	}
    }

    public String fallBack(String cause) {
	try {
	    return queryFallback(cause);
	} catch (SQLException e) {
	    return "Sorry something went wrong";
	}
    }

    public String queryFallback(String cause) throws SQLException  {
	String sqlQuery = "Select Question From Generic Order By Random() LIMIT 1";
	try (Statement statement = connection.createStatement()) {
	    String answer = "";
	    if (cause.length() != 0) {
		answer = "I am sorry to hear " + cause
		    + " bothered you today! "; 
	    }
	    return answer + getQuestion(statement.executeQuery(sqlQuery));
	}
    }

    public String getQuestion(ResultSet rs) {
	try {
	    if (rs.next()) {
		return rs.getString("question");
	    }
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}

	return "Something went wrong";
    }

    public String getAlias(String word) {
	String sqlQuery = "Select alias from alias where word=\'" + word + "\'";
	
	try (Statement statement = connection.createStatement()) {
	    ResultSet rs = statement.executeQuery(sqlQuery);
	    if (rs.next()) {
		return rs.getString("alias");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return word;
    }

    public void close() throws SQLException {
	connection.close();
    }
    
}
