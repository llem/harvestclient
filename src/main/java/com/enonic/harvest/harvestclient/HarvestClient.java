package com.enonic.harvest.harvestclient;

import com.enonic.harvest.harvestclient.exceptions.HarvestClientException;
import com.enonic.harvest.harvestclient.parameters.GetDayEntriesByProjectParameters;
import com.enonic.harvest.harvestclient.parameters.GetDayEntriesByUserParameters;
import com.enonic.harvest.harvestclient.parameters.GetRecentInvoicesParameters;
import com.enonic.harvest.harvestclient.models.*;

import java.math.BigDecimal;
import java.util.Date;

public interface HarvestClient
{
    UserCollection getUsers()
            throws HarvestClientException;

    UserCollection getUsers(Date updatedSince)
            throws HarvestClientException;

    User getUser(int id)
            throws HarvestClientException;

    ClientCollection getClients()
            throws HarvestClientException;

    ClientCollection getClients(Date updatedSince)
            throws HarvestClientException;

    Client getClient(int id)
            throws HarvestClientException;

    DayEntryCollection getDayEntriesByUser(GetDayEntriesByUserParameters params)
            throws HarvestClientException;

    DayEntryCollection getDayEntriesByProject(GetDayEntriesByProjectParameters params)
            throws HarvestClientException;

    ProjectCollection getProjects()
            throws HarvestClientException;

    ProjectCollection getProjects(Date updatedSince)
            throws HarvestClientException;

    ProjectCollection getProjects(int clientId)
            throws HarvestClientException;

    ProjectCollection getProjects(int clientId, Date updatedSince)
            throws HarvestClientException;

    Project getProject(int id)
            throws HarvestClientException;

    TaskCollection getTasks()
            throws HarvestClientException;

    TaskCollection getTasks(Date updatedSince)
            throws HarvestClientException;

    Task getTask(int id)
            throws HarvestClientException;

    UserAssignmentCollection getUserAssignments(int projectId)
            throws HarvestClientException;

    UserAssignmentCollection getUserAssignments(int projectId, Date updatedSince)
            throws HarvestClientException;

    TaskAssignmentCollection getTaskAssignments(int projectId)
            throws HarvestClientException;

    TaskAssignmentCollection getTaskAssignments(int projectId, Date updatedSince)
            throws HarvestClientException;

    InvoiceCollection getRecentInvoices(GetRecentInvoicesParameters params)
            throws HarvestClientException;

    Invoice getInvoice(int id)
            throws HarvestClientException;

    InvoiceItemCategoryCollection getInvoiceItemCategories()
            throws  HarvestClientException;

    InvoiceMessageCollection getInvoiceMessages(int invoiceId)
            throws HarvestClientException;

    InvoiceMessage getInvoiceMessage(int invoiceId, int id)
            throws  HarvestClientException;

    InvoicePaymentCollection getInvoicePayments(int invoiceId)
            throws HarvestClientException;

    InvoicePayment getInvoicePayment(int invoiceId, int id)
            throws HarvestClientException;
    
    /**
     * Populates a cache which maps invoice numbers to Harvest invoice Ids.
     * 
     * @param from start date of range from which invoices are to be retrieved
     * @param to end date of range from which invoices are to be retrieved
     * @return the number of invoices retrieved
     */
    int populateInvoiceCache(Date from, Date to);
    
    /**
     * Posts a payment against an invoice in Harvest.
     * 
     * @param invoiceId The Harvest unique identifier for the invoice
     * @param paymentReference An identifier or reference number for the payment
     * @param paymentAmount The amount of the payment to be applied
     * @throws HarvestClientException
     */
    void postInvoicePayment(int invoiceId, String paymentReference, BigDecimal paymentAmount) throws HarvestClientException;
    
    /**
     * Posts a payment against an invoice in Harvest.
     * 
     * @param invoiceNumber The invoice number in Harvest
     * @param paymentReference An identifier or reference number for the payment
     * @param paymentAmount The amount of the payment to be applied
     * @throws HarvestClientException
     */
    void postInvoicePayment(String invoiceNumber, String paymentReference, BigDecimal paymentAmount) throws HarvestClientException;
}
