package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.ClientAdminTicketSellerServiceFactory;
import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;

import javax.xml.bind.DatatypeConverter;


public class AdminTicketSellerServiceClient
{
    public static void main(String[] args) {

        if(args.length == 0)
            printUsageAndExit();

        ClientAdminTicketSellerService service = ClientAdminTicketSellerServiceFactory.getService();

        if("-c".equalsIgnoreCase(args[0]))
        // [check] UserTicketSellerServiceClient -c <reservationCode> <creditCardNumber>
        {
            validateArgs(args, 3, new int[] {});

            try
            {
                service.checkReservation(args[1], args[2]);
                System.out.println("Reservation " + args[1] + " checked");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-a".equalsIgnoreCase(args[0]))
        // [add]      AdminTicketSellerServiceClient -a <name> <description> <start> <duration> <limit> <tickets> <price> <discountedPrice> <commission>
        {
            validateArgs(args, 10, new int[] {4, 6, 7, 8, 9});

            try
            {
                ClientShowDto show = service.createShow(new ClientAdminShowDto(
                        null,
                        args[1],
                        args[2],
                        DatatypeConverter.parseDateTime(args[3]),
                        Long.parseLong(args[4]),
                        DatatypeConverter.parseDateTime(args[5]),
                        Long.parseLong(args[6]),
                        Long.parseLong(args[6]),
                        Float.parseFloat(args[7]),
                        Float.parseFloat(args[8]),
                        Float.parseFloat(args[9])
                ));

                System.out.println("{" +
                        "id=" + show.getId() +
                        ", name='" + show.getName() + '\'' +
                        ", description='" + show.getDescription() + '\'' +
                        ", startDate=" + show.getStartDate().toInstant() +
                        ", endDate=" + show.getEndDate().toInstant() +
                        ", limitDate=" + show.getLimitDate().toInstant() +
                        ", tickets=" + show.getTickets() +
                        ", price=" + show.getPrice() +
                        ", discountedPrice=" + show.getDiscountedPrice() +
                        "}");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-u".equalsIgnoreCase(args[0]))
        // [update]   AdminTicketSellerServiceClient -u <id> <name> <description> <start> <duration> <limit> <tickets> <price> <discountedPrice> <commission>
        {
            validateArgs(args, 11, new int[] {1, 5, 7, 8, 9, 10});

            try
            {
                service.updateShow(new ClientAdminShowDto(
                        Long.parseLong(args[1]),
                        args[2],
                        args[3],
                        DatatypeConverter.parseDateTime(args[4]),
                        Long.parseLong(args[5]),
                        DatatypeConverter.parseDateTime(args[6]),
                        Long.parseLong(args[7]),
                        Long.parseLong(args[7]),
                        Float.parseFloat(args[8]),
                        Float.parseFloat(args[9]),
                        Float.parseFloat(args[10])
                ));

                ClientShowDto show = service.findShow(Long.parseLong(args[1]));
                System.out.println("{" +
                        "id=" + show.getId() +
                        ", name='" + show.getName() + '\'' +
                        ", description='" + show.getDescription() + '\'' +
                        ", startDate=" + show.getStartDate().toInstant() +
                        ", endDate=" + show.getEndDate().toInstant() +
                        ", limitDate=" + show.getLimitDate().toInstant() +
                        ", tickets=" + show.getTickets() +
                        ", price=" + show.getPrice() +
                        ", discountedPrice=" + show.getDiscountedPrice() +
                        "}");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-g".equalsIgnoreCase(args[0]))
        // [get]      AdminTicketSellerServiceClient -g <showId>
        {
            validateArgs(args, 2, new int[] {1});

            try
            {
                ClientShowDto show = service.findShow(Long.parseLong(args[1]));
                System.out.println("{" +
                        "id=" + show.getId() +
                        ", name='" + show.getName() + '\'' +
                        ", description='" + show.getDescription() + '\'' +
                        ", startDate=" + show.getStartDate().toInstant() +
                        ", endDate=" + show.getEndDate().toInstant() +
                        ", limitDate=" + show.getLimitDate().toInstant() +
                        ", tickets=" + show.getTickets() +
                        ", price=" + show.getPrice() +
                        ", discountedPrice=" + show.getDiscountedPrice() +
                        "}");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else printUsageAndExit();
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
                "    [add]      AdminTicketSellerServiceClient -a <name> <description> <start> <duration> <limit> <tickets> <price> <discountedPrice> <commission>\n" +
                "    [update]   AdminTicketSellerServiceClient -u <id> <name> <description> <start> <duration> <limit> <tickets> <price> <discountedPrice> <commission> \n" +
                "    [get]      AdminTicketSellerServiceClient -g <showId>\n" +
                "    [check]    AdminTicketSellerServiceClient -c <reservationCode> <creditCardNumber>\n");
    }
}
