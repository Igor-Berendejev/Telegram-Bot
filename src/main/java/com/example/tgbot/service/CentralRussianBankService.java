package com.example.tgbot.service;

import com.example.tgbot.DTO.GetCursOnDateXML;
import com.example.tgbot.DTO.GetCursOnDateXMLResponse;
import com.example.tgbot.DTO.ValuteCursOnDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CentralRussianBankService extends WebServiceTemplate {

    @Value(value = "${cbr.api.url}")
    private String cbrApiUrl;

    public List<ValuteCursOnDate> getCurrenciesFromCbr() throws DatatypeConfigurationException {
        final GetCursOnDateXML getCursOnDateXml = new GetCursOnDateXML();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        getCursOnDateXml.setOnDate(xmlGregorianCalendar);

        GetCursOnDateXMLResponse response = (GetCursOnDateXMLResponse) marshalSendAndReceive(cbrApiUrl, getCursOnDateXml);

        if (response == null){
            throw new IllegalStateException("Could not get response from CBR Service");
        }
        final List<ValuteCursOnDate> courses = response.getGetCursOnDateXmlResult().getValuteData();
        courses.forEach(course -> course.setName(course.getName().trim()));

        return courses;
    }

    public ValuteCursOnDate getCurrencyRate(String currencyCode) throws DatatypeConfigurationException {
        return getCurrenciesFromCbr()
                .stream()
                .filter(currency -> currency.getChCode().equals(currencyCode))
                .findFirst()
                .get();
    }
}
