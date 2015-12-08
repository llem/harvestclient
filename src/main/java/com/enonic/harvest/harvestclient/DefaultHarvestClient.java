package com.enonic.harvest.harvestclient;

import com.enonic.harvest.harvestclient.exceptions.HarvestClientException;
import com.enonic.harvest.harvestclient.exceptions.MissingParameterException;
import com.enonic.harvest.harvestclient.parameters.GetDayEntriesByUserParameters;
import com.enonic.harvest.harvestclient.parameters.GetRecentInvoicesParameters;
import com.enonic.harvest.harvestclient.parameters.GetDayEntriesByProjectParameters;
import com.enonic.harvest.harvestclient.models.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultHarvestClient
        implements HarvestClient
{
    private String username;
    private String password;
    private String subDomain;

    private SimpleDateFormat dateFormatter     = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    
    // Cache of recent invoices used for mapping invoice number to invoice ID (not included in Harvest CSV export).
    private Map<String, Integer> invoiceCache = new HashMap<String, Integer>();

    public DefaultHarvestClient(String username, String password, String subDomain)
    {
        this.username = username;
        this.password = password;
        this.subDomain = subDomain;
    }

    @Override
    public UserCollection getUsers()
            throws HarvestClientException
    {
        return UserCollection.fromInputStream(this.getInputStream("/people"));
    }

    @Override
    public UserCollection getUsers(Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return UserCollection.fromInputStream(this.getInputStream("/people?updated_since=%s", updatedSinceUrlString));
    }

    @Override
    public User getUser(int id)
            throws HarvestClientException
    {
        return User.fromInputStream(this.getInputStream("/people/%s", id));
    }

    @Override
    public ClientCollection getClients()
            throws HarvestClientException
    {
        return ClientCollection.fromInputStream(this.getInputStream("/clients"));
    }

    @Override
    public ClientCollection getClients(Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return ClientCollection.fromInputStream(this.getInputStream("/clients?updated_since=%s", updatedSinceUrlString));
    }

    @Override
    public Client getClient(int id)
            throws HarvestClientException
    {
        return Client.fromInputStream(this.getInputStream("/clients/%s", id));
    }

    @Override
    public DayEntryCollection getDayEntriesByUser(GetDayEntriesByUserParameters params)
            throws HarvestClientException
    {
        if (params.fromDate == null)
            throw new MissingParameterException("fromDate");
        if (params.toDate == null)
            throw new MissingParameterException("toDate");

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        urlParams.add(new BasicNameValuePair("from", this.dateFormatter.format(params.fromDate)));
        urlParams.add(new BasicNameValuePair("to", this.dateFormatter.format(params.toDate)));

        if (params.updatedSince != null)
        {
            urlParams.add(new BasicNameValuePair("updated_since", this.dateTimeFormatter.format(params.updatedSince)));
        }

        String urlParamString = URLEncodedUtils.format(urlParams, "utf-8");

        return DayEntryCollection.fromInputStream(this.getInputStream("/people/%s/entries?%s", params.userId, urlParamString));
    }

    @Override
    public DayEntryCollection getDayEntriesByProject(GetDayEntriesByProjectParameters params)
            throws HarvestClientException
    {
        if (params.fromDate == null)
            throw new MissingParameterException("fromDate");
        if (params.toDate == null)
            throw new MissingParameterException("toDate");

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        urlParams.add(new BasicNameValuePair("from", this.dateFormatter.format(params.fromDate)));
        urlParams.add(new BasicNameValuePair("to", this.dateFormatter.format(params.toDate)));

        if (params.updatedSince != null)
        {
            urlParams.add(new BasicNameValuePair("updated_since", this.dateTimeFormatter.format(params.updatedSince)));
        }

        String urlParamString = URLEncodedUtils.format(urlParams, "utf-8");

        return DayEntryCollection.fromInputStream(this.getInputStream("/projects/%s/entries?%s", params.projectId, urlParamString));
    }

    @Override
    public ProjectCollection getProjects()
            throws HarvestClientException
    {
        return ProjectCollection.fromInputStream(this.getInputStream("/projects"));
    }

    @Override
    public ProjectCollection getProjects(Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return ProjectCollection.fromInputStream(this.getInputStream("/projects?updated_since=%s", updatedSinceUrlString));
    }

    @Override
    public ProjectCollection getProjects(int clientId)
            throws HarvestClientException
    {
        return ProjectCollection.fromInputStream(this.getInputStream("/projects?client=%s", clientId));
    }

    @Override
    public ProjectCollection getProjects(int clientId, Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return ProjectCollection.fromInputStream(this.getInputStream("/projects?client=%s&updated_since=%s", clientId, updatedSinceUrlString));
    }

    @Override
    public Project getProject(int id)
            throws HarvestClientException
    {
        return Project.fromInputStream(this.getInputStream("/projects/%s", id));
    }

    @Override
    public UserAssignmentCollection getUserAssignments(int projectId)
            throws HarvestClientException
    {
        return UserAssignmentCollection.fromInputStream(this.getInputStream("/projects/%s/user_assignments", projectId));
    }

    @Override
    public UserAssignmentCollection getUserAssignments(int projectId, Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return UserAssignmentCollection.fromInputStream(this.getInputStream("/projects/%s/user_assignments?updated_since=%s", projectId, updatedSinceUrlString));
    }

    @Override
    public TaskCollection getTasks()
            throws HarvestClientException
    {
        return TaskCollection.fromInputStream(this.getInputStream("/tasks"));
    }

    @Override
    public TaskCollection getTasks(Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return TaskCollection.fromInputStream(this.getInputStream("/tasks?updated_since=%s", updatedSinceUrlString));
    }

    @Override
    public Task getTask(int id)
            throws HarvestClientException
    {
        return Task.fromInputStream(this.getInputStream("/tasks/%s", id));
    }

    @Override
    public TaskAssignmentCollection getTaskAssignments(int projectId)
            throws HarvestClientException
    {
        return TaskAssignmentCollection.fromInputStream(this.getInputStream("/projects/%s/task_assignments", projectId));
    }

    @Override
    public TaskAssignmentCollection getTaskAssignments(int projectId, Date updatedSince)
            throws HarvestClientException
    {
        String updatedSinceUrlString;
        try
        {
            updatedSinceUrlString = URLEncoder.encode(this.dateTimeFormatter.format(updatedSince), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new HarvestClientException("Unsupported encoding.", e);
        }
        return TaskAssignmentCollection.fromInputStream(this.getInputStream("/projects/%s/task_assignments?updated_since=%s", projectId, updatedSinceUrlString));
    }

    @Override
    public InvoiceCollection getRecentInvoices(GetRecentInvoicesParameters params)
            throws HarvestClientException
    {
        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

        if (params.getPage() != null)
            urlParams.add(new BasicNameValuePair("page", params.getPage().toString()));
        if (params.getFromDate() != null)
            urlParams.add(new BasicNameValuePair("from", this.dateFormatter.format(params.getFromDate())));
        if (params.getToDate() != null)
            urlParams.add(new BasicNameValuePair("to", this.dateFormatter.format(params.getToDate())));
        if (params.getUpdatedSince() != null)
            urlParams.add(new BasicNameValuePair("updated_since", this.dateFormatter.format(params.getUpdatedSince())));
        if (params.getStatus() != null)
            urlParams.add(new BasicNameValuePair("status", params.getStatus()));
        if (params.getClient() != null)
            urlParams.add(new BasicNameValuePair("client", params.getClient().toString()));

        String urlParamString = URLEncodedUtils.format(urlParams, "utf-8");

        return InvoiceCollection.fromInputStream(this.getInputStream("/invoices?%s", urlParamString));
    }

    @Override
    public Invoice getInvoice(int id)
            throws HarvestClientException
    {
        return Invoice.fromInputStream(this.getInputStream("/invoices/%s", id));
    }

    @Override
    public InvoiceItemCategoryCollection getInvoiceItemCategories()
            throws HarvestClientException
    {
        return InvoiceItemCategoryCollection.fromInputStream(this.getInputStream("/invoice_item_categories"));
    }

    @Override
    public InvoiceMessageCollection getInvoiceMessages(int invoiceId)
            throws HarvestClientException
    {
        return InvoiceMessageCollection.fromInputStream(this.getInputStream("/invoices/%s/messages", invoiceId));
    }

    @Override
    public InvoiceMessage getInvoiceMessage(int invoiceId, int id)
            throws  HarvestClientException
    {
        return InvoiceMessage.fromInputStream(this.getInputStream("/invoices/%s/messages/%s", invoiceId, id));
    }

    @Override
    public InvoicePaymentCollection getInvoicePayments(int invoiceId)
            throws HarvestClientException
    {
        return InvoicePaymentCollection.fromInputStream(this.getInputStream("/invoices/%s/payments", invoiceId));
    }

    @Override
    public InvoicePayment getInvoicePayment(int invoiceId, int id)
            throws HarvestClientException
    {
        return InvoicePayment.fromInputStream(this.getInputStream("/invoices/%s/payments/%s", invoiceId, id));
    }

    private InputStream getInputStream(String url, Object... args)
            throws HarvestClientException
    {
        HarvestGetRequest request = new HarvestGetRequest();
        request.setUrl(String.format(url, args));
        request.setSubdomain(this.subDomain);
        request.setUsername(this.username);
        request.setPassword(this.password);
        return request.getInputStream();
    }
    
    public int populateInvoiceCache(Date from, Date to) {
        // Clear the existing cache
        invoiceCache.clear();
        
        int currentPage = 1;
        GetRecentInvoicesParameters params = new GetRecentInvoicesParameters();
        params.setFromDate(from);
        params.setToDate(to);
        params.setPage(currentPage);
        boolean resultsExhausted = false;
        List<Invoice> recentInvoices = new ArrayList<Invoice>();
        
        // Iterate through the result pages until no further invoices are found
        while (!resultsExhausted) {
            
            System.out.println("Retrieving recent invoices, page " + currentPage);
            InvoiceCollection invoiceCollection = getRecentInvoices(params);
            List<Invoice> invoiceList = invoiceCollection.getList();

            if (invoiceList.size() > 0)
                recentInvoices.addAll(invoiceList);
            else
                resultsExhausted = true;
            
            currentPage++;
            params.setPage(currentPage);
        }
            
        // Populate the cache with the Number --> Id mapping
        for (Invoice invoice:recentInvoices)
            invoiceCache.put(invoice.getNumber(), invoice.getId());
        
        return recentInvoices.size();
    }
    
    public void postInvoicePayment(int invoiceId, String paymentReference, BigDecimal paymentAmount) throws HarvestClientException {
        // Create and populate the payment object
        InvoicePayment payment = new InvoicePayment();
        payment.setPaidAt(new Date());
        payment.setAmount(paymentAmount);
        payment.setNotes(paymentReference);

        // DIAGNOSTIC
        System.out.println(InvoicePayment.marshal(payment));
        
        HarvestPostRequest request = new HarvestPostRequest();
        request.setUrl(String.format("/invoices/%s/payments", invoiceId));
        request.setSubdomain(this.subDomain);
        request.setUsername(this.username);
        request.setPassword(this.password);

        // Execute the POST request
        request.executePostRequest(InvoicePayment.marshal(payment));
    }

    public void postInvoicePayment(String invoiceNumber, String paymentReference, BigDecimal paymentAmount) throws HarvestClientException {
        // Lookup the invoice Id from the cache
        Integer invoiceId = invoiceCache.get(invoiceNumber);
        
        if (invoiceId != null)
            postInvoicePayment(invoiceId, paymentReference, paymentAmount);
        else
            throw new HarvestClientException("Unable to find invoice number " + invoiceNumber + " in cache.");
    }
}
