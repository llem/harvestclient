package com.enonic.harvest.harvestclient.models;

import com.enonic.harvest.harvestclient.exceptions.HarvestClientException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.Marshaller;

@XmlRootElement(name = "payment")
@XmlAccessorType(XmlAccessType.NONE)
public class InvoicePayment
{
    // Class fields
    static private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZ");
    
    // Class initializer
    /* static {
        sdf = new SimpleDateFormat("YYYY-MM-ddHH:mm:ssZ");
    } */
    
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "amount")
    private BigDecimal amount;

    @XmlElement(name = "invoice-id")
    private Integer invoiceId;

    @XmlElement(name = "notes")
    private String notes;

    // Not annotated as @XmlElement, as we need to marshal this field to a String, but unmarshal into a Date
    private Date paidAt;

    @XmlElement(name = "recorded-by")
    private String recordedBy;

    @XmlElement(name = "recorded-by-email")
    private String recordedByEmail;

    @XmlElement(name = "updated-at")
    private Date updatedAt;

    @XmlElement(name = "created-at")
    private Date createdAt;

    public InvoicePayment() {
    }
    
    public static InvoicePayment fromInputStream(final InputStream xml)
            throws HarvestClientException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(InvoicePayment.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (InvoicePayment) unmarshaller.unmarshal(xml);
        }
        catch (Exception e)
        {
            throw new HarvestClientException("Unable to parse XML into InvoicePayment.", e);
        }

    }
    
    public static String marshal(InvoicePayment payment) throws HarvestClientException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(InvoicePayment.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(payment, sw);
            return sw.toString();
        }
        catch (Exception e)
        {
            throw new HarvestClientException("Unable to marshal InvoicePayment to String.", e);
        }

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Integer getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Date getPaidAtDate()
    {
        return paidAt;
    }
    
    /**
     * Formats the <tt>paidAt</tt> field in the format required by Harvest.
     * 
     * @return the date formatted as YYYY-MM-DDTHH:MM:SS
     */
    @XmlElement(name = "paid-at")
    public String getPaidAt() {
        return paidAt != null ? sdf.format(paidAt) : null;
    }

    @XmlElement(name = "paid-at")
    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public String getRecordedBy()
    {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy)
    {
        this.recordedBy = recordedBy;
    }

    public String getRecordedByEmail()
    {
        return recordedByEmail;
    }

    public void setRecordedByEmail(String recordedByEmail)
    {
        this.recordedByEmail = recordedByEmail;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }
}
