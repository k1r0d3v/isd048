package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientTicketSellerService;
import es.udc.ws.app.client.service.ClientTicketSellerServiceFactory;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;

import java.util.List;

public class UserTicketSellerServiceClient {

    public static void main(String[] args) {

        if(args.length == 0)
            printUsageAndExit();

        ClientTicketSellerService service = ClientTicketSellerServiceFactory.getService();


        if("-f".equalsIgnoreCase(args[0]))
            // [find] UserTicketSellerServiceClient -f <keywords>
        {
            validateArgs(args, 2, new int[] {});

            try {
                List<ClientShowDto> shows = service.findShows(args[1]);
                System.out.println("Found " + shows.size() +
                        " show(s) with keywords '" + args[1] + "'");

                for (ClientShowDto show : shows)
                {
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
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
        else if("-b".equalsIgnoreCase(args[0]))
            // [buy] UserTicketSellerServiceClient -b <showId> <email> <creditCardNumber> <count>
        {
            validateArgs(args, 5, new int[] {1, 4});

            ClientReservationDto reservation;
            try {
                reservation = service.buyTickets(Long.parseLong(args[1]), args[2], args[3], Integer.parseInt(args[4]));

                System.out.println("Purchased  " + reservation.getTickets() + " tickets successfully");
                System.out.println(
                        "Reservation {" +
                        "id=" + reservation.getId() +
                        ", showId=" + reservation.getShowId() +
                        ", email='" + reservation.getEmail() + '\'' +
                        ", creditCard='" + reservation.getCreditCard() + '\'' +
                        ", tickets=" + reservation.getTickets() +
                        ", isValid=" + reservation.isValid() +
                        ", code='" + reservation.getCode() + '\'' +
                        ", reservationDate=" + reservation.getReservationDate().toInstant() +
                        ", price=" + reservation.getPrice() +
                        "}");

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        else if("-g".equalsIgnoreCase(args[0]))
            // [get] UserTicketSellerServiceClient -g <email>
        {
            validateArgs(args, 2, new int[] {});

            try {
                List<ClientReservationDto> reservations = service.getUserReservations(args[1]);

                System.out.println("Found " + reservations.size() +
                        " reservation(s) for email '" + args[1] + "'");

                for (ClientReservationDto reservation : reservations)
                {
                    System.out.println(
                            "{" +
                            "id=" + reservation.getId() +
                            ", showId=" + reservation.getShowId() +
                            ", email='" + reservation.getEmail() + '\'' +
                            ", creditCard='" + reservation.getCreditCard() + '\'' +
                            ", tickets=" + reservation.getTickets() +
                            ", isValid=" + reservation.isValid() +
                            ", code='" + reservation.getCode() + '\'' +
                            ", reservationDate=" + reservation.getReservationDate().toInstant() +
                            ", price=" + reservation.getPrice() +
                            "}");
                }
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

    private static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    private static void printUsage() {
        System.err.println("Usage:\n" +
                "    [find]   UserTicketSellerServiceClient -f <keywords>\n" +
                "    [buy]    UserTicketSellerServiceClient -b <showId> <email> <creditCardNumber> <count>\n" +
                "    [get]    UserTicketSellerServiceClient -g <email>\n");
    }
}
