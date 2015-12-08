/*
 * HarvestPostRequest.java
 *
 * Created on Dec 8, 2015, 2:17:35 AM
 *
 */

package com.enonic.harvest.harvestclient;

import com.enonic.harvest.harvestclient.exceptions.HarvestClientException;
import com.enonic.harvest.harvestclient.exceptions.ThrottleLimitReachedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Aleem Sunderji
 */
public class HarvestPostRequest extends HarvestRequest {
    
    // Instance variables
    HttpPost request;

    public void executePostRequest(String requestBody) throws HarvestClientException
    {
        HttpClient client;
        HttpResponse response;

        try
        {
            client = HttpClientBuilder.create().build();
            request = new HttpPost(this.getUrl());
            request.addHeader("Authorization", this.getAuthenticationHeader());
            request.addHeader("Accept", "application/xml");
            request.addHeader("Content-Type", "application/xml");
            request.addHeader("User-Agent", "HarvestClient");
            
            StringEntity reqEntity = new StringEntity(requestBody);
            request.setEntity(reqEntity);
            
            response = client.execute(request);
        }
        catch (Exception e)
        {
            throw new HarvestClientException("Could not perform request.", e);
        }
        
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 503)
            throw new ThrottleLimitReachedException();
        else if (statusCode != 200 && statusCode != 201)
            throw new HarvestClientException(String.format("Returned status code %s: %s", response.getStatusLine().getStatusCode(), this.getUrl()));
    }
}
