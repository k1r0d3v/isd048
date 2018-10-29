package es.udc.ws.app.test.model.service;


import es.udc.ws.app.model.service.TicketSellerService;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.*;

import javax.sql.DataSource;

public class TicketSellerServiceTest
{
    private static TicketSellerService ticketService;

    @BeforeClass
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.movies.model.movieservice.MovieService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource("ws-javaexamples-ds", dataSource);

        ticketService = TicketSellerServiceFactory.getService();
    }
}