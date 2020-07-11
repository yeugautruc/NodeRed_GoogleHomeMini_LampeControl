package homedash;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

@WebServlet(urlPatterns = { "dashboard" })
public class HomeDashboard extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();			
		HttpGet requestCalls = new HttpGet("http://pi-8f5fe075.localhost.run/actioncalls");
		String calls="";
		try {
			HttpResponse responseCalls = client.execute(requestCalls);
			calls = EntityUtils.toString(responseCalls.getEntity());
		}catch (Exception e) {
		}
		
		String responseString = "<!DOCTYPE html> " + 
				 "    <html> " + 
				 "<html lang='de'>"+
				 "    <head> " +  
				 "    <meta charset='utf-8'> " +  
				 "    <title>Dashboard</title> " +
				 //jquery import
				 "<script src='https://code.jquery.com/jquery-3.4.1.min.js'></script>"+
				 
				 //style(CSS) for html elements
				"<style>"+
				"body {text-align:center}"+
				//style for button
				"#btnlamp {background-color:#b3b3b3;"+
				"font-size:3vh;"+
				"border-style:dashed;"+
				"border-color:black;"+
				"height:8vh; width:20vw;"+
				"margin-bottom:2vh;"+
				"}"+
				//style for number input
				"#bright_input {background-color:#b3b3b3;"+
				"font-size:2vh;"+
				"border-color:black;"+
				"height:5vh; width:10vw;"+
				"margin-right:1vw;"+
				"}"+
				//style for color input
				"#colors {background-color:#b3b3b3;"+
				"font-size:2vh;"+
				"border-color:black;"+
				"}"+
				"</style>"+
				
				//html begins
				 "</head> " +  
				 "<body onload=\"setLampStatus()\">" +
				 "<p style='font-size:7vh;text-decoration:underline;'>My Home</p>"+
				 "<div style='text-align:center;background-color:#8c8c8c;padding:10vh;margin-bottom:2vh;'>"+
				 "<p style='font-size:5vh;margin-bottom:10vh;'>Sleeping Room Lamp</p>"+
				 //on/off button
				" <button id='btnlamp' type='button'>Off</button>"+
				 "<form action=''>"+
				//textfield for brightness input
				 "<input id='bright_input' type='number' placeholder = 'Put the brightnes here' min='2' max='100'>"+
				 //color selector
				 "<select name='colors' id='colors'>"+
				 "<option value='white'>White</option>"+
				 "<option value='green'>Green</option>"+
				 "<option value='blue'>Blue</option>"+
				 "<option value='red'>Red</option>"+
				 "<option value='yellow'>Yellow</option>"+
				 "</select>"+
				 "</form>"+
				 //div element google action calls
				"</div>"+
				"<div style='text-align:center;background-color:#8c8c8c;padding:10vh';>"+
				"<p style='font-size:5vh'>Google Actioncalls </p>"+
				//number of action calls
				"<p style='font-size:25vh'>"+calls+ " </p>"+
				"</div>"+
				
				//javascript begins
				"<script>"+
				//set lamp status always at website reload
				"function setLampStatus() {"+
				"$.get(\"http://pi-8f5fe075.localhost.run/huelampstatus\", function(data, status) {"+				
				"data=JSON.parse(data);"+
				"if(data.on===true) {"+
				//set brightness if some has already specified
				"$(\"#bright_input\").val(parseInt(data.brightness));"+
				//set color if some has already specified
				"$(\"#colors\").val(data.color);"+
				"$(\"#btnlamp\").click();"+
				"}"+
				"else {"+
				"$(\"#btnlamp\").css('background-color','#b3b3b3');"+
				//hover attribute
				"$(\"#btnlamp\").hover(function(){$(\"#btnlamp\").css('background-color','#e6e6e6');} , function(){$(\"#btnlamp\").css('background-color','#b3b3b3');});"+
				"}"+
				"}"+
				");"+
				"}"+
				"$(\"#btnlamp\").click(clicked);"+
				//function for button click event
				"function clicked(){"+
				//set button text
				"if($(\"#btnlamp\").text()==='Off') {"+
				"$(\"#btnlamp\").text(\"On\");"+
				//set button color
				"$(\"#btnlamp\").css('background-color',$(\"#colors\").val());"+
				//hover attribute
				"$(\"#btnlamp\").hover(function(){$(\"#btnlamp\").css('background-color',$(\"#colors\").val());} , function(){$(\"#btnlamp\").css('background-color',$(\"#colors\").val());});"+
				//brightness
				"var bright=1;"+
				"if($(\"#bright_input\").val()>1) {"+
				"bright=$(\"#bright_input\").val();"+
				"}"+
				//send request to Node red for switching on the lamp (parameter are lamp configuration as json)
				"$.get(\"http://pi-8f5fe075.localhost.run/huelampcontrol\",{\"on\":bright,\"color\":$(\"#colors\").val()});"+
				"}"+
				"else {"+
				//set button text
				"$(\"#btnlamp\").text('Off');"+
				//set button color
				"$(\"#btnlamp\").css('background-color','#cccccc');"+
				//hover attribute
				"$(\"#btnlamp\").hover(function(){$(\"#btnlamp\").css('background-color','#e6e6e6');} , function(){$(\"#btnlamp\").css('background-color','#cccccc');});"+
				//send request to Node red for switching off the lamp
				"$.get(\"http://pi-8f5fe075.localhost.run/huelampcontrol\",{\"on\":0});"+
				"}"+
				"}"+
				"</script>"+
		 		"</body> " +  
				"</html>"; 
		response.getWriter().print(responseString );
	}
}
