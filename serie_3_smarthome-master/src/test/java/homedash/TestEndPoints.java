package homedash;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hamcrest.beans.HasProperty;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestEndPoints {

	@Test
	public void validateCounterActioncalls() {
		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			//request number befor call for validating action call counter
			HttpGet actioncallsRequest = new HttpGet("http://pi-a0e09eb2.localhost.run/actioncalls");
			HttpResponse actioncallsResponse = client.execute(actioncallsRequest);
			int beforeCallNumber = Integer.parseInt(EntityUtils.toString(actioncallsResponse.getEntity()));
			//simulating actioncall
			HttpPost actioncall = new HttpPost("http://pi-a0e09eb2.localhost.run/webhookraum");
			client.execute(actioncall);
			//number after call
			actioncallsResponse = client.execute(actioncallsRequest);
			//number for validating actioncall counter (number before action call)
			int afterCallNumber = Integer.parseInt(EntityUtils.toString(actioncallsResponse.getEntity()));
			assertThat(beforeCallNumber+1,equalTo(afterCallNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void validateActioncallResponse() {
		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			
			//Action call object
			HttpPost actioncall = new HttpPost("http://pi-a0e09eb2.localhost.run/webhookraum");
			HttpResponse callResponse = client.execute(actioncall);
			String temp = EntityUtils.toString(callResponse.getEntity());
			JsonObject callResponseJson = JsonParser.parseString(temp).getAsJsonObject();
			String googleAssistantResponse = callResponseJson.get("payload").getAsJsonObject()
					.get("google").getAsJsonObject().get("richResponse").getAsJsonObject()
					.get("items").getAsJsonArray().get(0)
					.getAsJsonObject().get("simpleResponse").getAsJsonObject()
					.get("textToSpeech").toString();
			//room temperature request for comparing the temperature with action response
			HttpGet temperatureRequest = new HttpGet("http://pi-a0e09eb2.localhost.run/raumalarm");
			HttpResponse temperatureResponse = client.execute(temperatureRequest);
			temp = EntityUtils.toString(temperatureResponse.getEntity());
			JsonObject temperatureJson = JsonParser.parseString(temp).getAsJsonObject();
			String temperature=temperatureJson.get("temp").toString();
			assertThat(googleAssistantResponse, containsString(temperature));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
