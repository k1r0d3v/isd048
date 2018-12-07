package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.ClientAdminTicketSellerServiceFactory;
import es.udc.ws.app.client.service.ClientTicketSellerService;
import es.udc.ws.app.client.service.ClientTicketSellerServiceFactory;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;

import java.util.List;

public class TicketSellerServiceAdminClient
{
    public static void main(String[] args) {

        if(args.length == 0)
            printUsageAndExit();

        ClientAdminTicketSellerService clientTicketSellerService = ClientAdminTicketSellerServiceFactory.getService();
        
    }

    private static void validateArgs(String[] args, int expectedArgs, int[] numericArguments)
    {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }

        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    private static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    private static void printUsage() {
        System.err.println("Usage:\n" +
                "    [find]   TicketSellerServiceClient -f <keywords>\n" +
                "    [buy]    TicketSellerServiceClient -b <showId> <email> <creditCardNumber> <count>\n" +
                "    [get]    TicketSellerServiceClient -g <email>\n");
    }
}
