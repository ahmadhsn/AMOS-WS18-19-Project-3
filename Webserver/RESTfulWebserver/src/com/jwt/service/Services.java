package com.jwt.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.format.DateTimeFormatter;
import com.jwt.service.mail.Mailer;
import java.util.*;
import java.text.*;

/**
 * This class provides all web services.
 */
@Path("/services")
public class Services {
	
	@Context	
	private ServletContext context;
	
	/**
	 * Gives greetings to an Request. Testable with
	 * http://localhost:8080/RESTfulWebserver/services/Tester
	 * 
	 * @param name
	 *            path ending of tester
	 * @return Response with status 200 and a JSONObject with greetings.
	 * @throws JSONException
	 */
	@GET
	@Path("/{name}")
	public Response getMsg(@PathParam("name") String name) throws JSONException {
		JSONObject output = new JSONObject("{Welcome : " + name + "}");
		System.out.println("...TestRequest from " + name);
		return Response.status(200).entity(output.toString()).build();
	}

	/**TODO
	 * Checks users authentication.
	 * 
	 * @param urlReq
	 *            with user name and password
	 * @return Response with status 200 and massage for valid user or status 400
	 *         with InvalidRequestBody-message.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/checkUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkUser(String urlReq)
			throws ClassNotFoundException, SQLException, JSONException, UnsupportedEncodingException {
		JSONObject JSONreq = new JSONObject(urlReq);

		if (JSONreq.has("username") && JSONreq.has("password")) {
			String username = JSONreq.getString("username"); //or email
			String password = JSONreq.getString("password");

			// check users info in DB
			
			// TODO: response for client
			JSONObject response = new JSONObject();

			return Response.status(200).entity(response.toString()).build();
		} else {
			return Response.status(400).entity("InvalidRequestBody").build();
		}
	}

	/**TODO
	 * Adds new user to database.
	 * 
	 * @param urlReq
	 *            with user name, password, email, ...
	 * @return Response with status 200 and a message or status 400 with
	 *         InvalidRequestBody-message.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/userRegistration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userRegistration(String urlReq)
			throws ClassNotFoundException, SQLException, JSONException, UnsupportedEncodingException {
		JSONObject JSONreq = new JSONObject(urlReq);

		if (JSONreq.has("name") && JSONreq.has("password") && JSONreq.has("email")) {
			try {
				// TODO: check if email already exists

				// get data
				String username = JSONreq.getString("name");
				String password = JSONreq.getString("password");
				String email = JSONreq.getString("email");
				// TODO: more data... city, birth?

				System.out.println("...userRegistrationRequest from " + username);


				// TODO: addUserToDB
				
				//send registration mail 
				Mailer mailer = new Mailer(context);
				boolean messageSent = mailer.sendRegistrationMail(email, username);
		
				// TODO: response for client
				JSONObject response = new JSONObject();
				
				response.put("userRegistration", "successfullRegistration");

				return Response.status(200).entity(response.toString()).build();

			} catch (Exception e) {
				System.out.println("Wrong JSONFormat:" + e.toString());
			}
		}
		System.out.println("InvalidRequestbody");
		return Response.status(400).entity("InvalidRequestBody").build();
	}
	
	/**
	 * TODO
	 * @param urlReq
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("/createEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createEvent(String urlReq)
			throws ClassNotFoundException, SQLException, JSONException, UnsupportedEncodingException {
		JSONObject JSONreq = new JSONObject(urlReq);
		System.out.println("...createEventRequest");
		
		if (JSONreq.has("name")) {
			try {
		
				
				String eventname = JSONreq.getString("name");
				String eventdescription = JSONreq.getString("description");
				String eventdate = JSONreq.getString("date");
				String eventtime = JSONreq.getString("time");
				
				
				System.out.println("...newEventCreated:" + eventname);
				
		        try {
		            PostgreSQLExample postgreSQLExample = new PostgreSQLExample();
		            Connection conn = postgreSQLExample.getPostgreSQLConnection();

	            PreparedStatement st = conn.prepareStatement("INSERT INTO EVENT (name,description) VALUES (?,?)");
	            st.setString(1, eventname);
	            st.setString(2, eventdescription);
	            st.executeUpdate();
	            st.closeOnCompletion();

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
				
				
				
				JSONObject response = new JSONObject();
				
				response.put("eventCreation", "successfullCreation");
		
				return Response.status(200).entity(response.toString()).build();

			} catch (Exception e) {
				System.out.println("Wrong JSONFormat:" + e.toString());
			}
		}
		System.out.println("InvalidRequestbody");
		return Response.status(400).entity("InvalidRequestBody").build();
	}
	
	/**
	 * Sets options for headers.
	 */
	@OPTIONS
	public Response optionsOptions() {
		return Response.ok().header("Allow-Control-Allow-Methods", "POST,GET,OPTIONS")
				.header("Access-Control-Allow-Origin", "*").build();
	}


	@GET
	@Path("/testMail")
	public Response sendTestMail() throws JSONException {
		JSONObject output = new JSONObject("{Test: send Mail}");
		System.out.println("...sendTestMail Request " );
		
		Mailer mailer = new Mailer(context);
		//send test registration mail 
		
		//mailer.sendRegistrationMail("anika.apel@gmx.de", "test");
		
		return Response.status(200).entity(output.toString()).build();
	}

}


