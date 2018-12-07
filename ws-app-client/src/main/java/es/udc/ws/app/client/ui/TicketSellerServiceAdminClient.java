package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.ClientAdminTicketSellerServiceFactory;


public class TicketSellerServiceAdminClient
{
    public static void main(String[] args) {

        if(args.length == 0)
            printUsageAndExit();

        ClientAdminTicketSellerService service = ClientAdminTicketSellerServiceFactory.getService();

        if("-c".equalsIgnoreCase(args[0]))
        // [check] TicketSellerServiceClient -c <reservationCode> <creditCardNumber>
        {
            validateArgs(args, 3, new int[] {});

            try
            {
                service.checkReservation(args[1], args[2]);
                System.out.println("Reservation " + args[1] + " checked");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-c".equalsIgnoreCase(args[0]))
        // [add]      TicketSellerServiceAdminClient -a <name> <description> <start> <end> <limit> <tickets> <price> <discount>
        {
            validateArgs(args, 9, new int[] {});

            try
            {

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
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

    private static void printUsageAndExit()
    {
        printUsage();
        System.exit(-1);
    }

    private static void printUsage()
    {
        System.err.println("Usage:\n" +
                "    [add]      TicketSellerServiceAdminClient -a <name> <description> <start> <end> <limit> <maxTickets> <tickets> <price> <discount> <commission>\n" +
                "    [update]   TicketSellerServiceAdminClient -u <id> [-name <name>] [-description <description>] " +
                "                                                      [-start <start>] [-limit <limit>] [-maxTickets <maxTickets>] " +
                "                                                      [-tickets <tickets>] [-price <price>] " +
                "                                                      [-discount <discount>] [-commission <commission>] \n" +
                "    [get]      TicketSellerServiceAdminClient -g <showId>\n" +
                "    [check]    TicketSellerServiceAdminClient -c <reservationCode> <creditCardNumber>\n");
    }
}
