import javax.swing.text.View;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;



public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/Hotel_db";
    private static final String username = "root";
    private static final String password = "Harshal@6321";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Choose An Option :-");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice.Try Again");

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } catch (InterruptedException e) {
            throw new RuntimeException();

        }


    }





    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter guest name:- ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room Number:- ");
            int rooNumber = scanner.nextInt();
            System.out.println("Enter Contact Number:- ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number)" +
                    "VALUES ('" + guestName + "', " + rooNumber + ", '" + contactNumber + "')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);
                if (affectedRows > 0) {
                    System.out.println("----------Reservation Sucessfully----------");
                } else {
                    System.out.println("Reservation Failed.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    private static void viewReservation(Connection connection)throws SQLException{
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql))
        {
            System.out.println("Current reservations:");





            while(resultSet.next())
            {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

            }
        }
    }
    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try
        {
            System.out.println("Enter Reservation ID:- ");
            int reservationId = scanner.nextInt();
            System.out.println("Enter Guest Name:- ");
            String guestname = scanner.next();
            String sql="SELECT room_number from RESERVATIONS WHERE RESERVATION_ID = " + reservationId + " AND guest_name = '" + guestname + "'";
            try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("----------Room number for Reservation ID " + reservationId +
                            " and Guest " + guestname + " is:---------- " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter reservation ID to update: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine();

            if (!reservationIdExists(connection, reservationId)){
                System.out.println("reservation not found for the given ID.");
                return;
            }
            System.out.println("Enter new guest name: ");
            String newGuestName=scanner.nextLine();
            System.out.println("Enter The New Room Number");
            int newRoomNumber = scanner.nextInt();
            System.out.println("Enter The New Contact Number: ");
            String newContactNumber =scanner.next();

            String sql ="UPDATE reservations SET guest_name = '" + newGuestName + "'," +
                    "room_number = " + newRoomNumber +"," +
                    "contact_number ='" + newContactNumber +"'" +
                    "WHERE reservation_id = "+ reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRoes = statement.executeUpdate(sql);

                if (affectedRoes > 0)
                {
                    System.out.println("Reservation update sucessfully");
                }else
                {
                    System.out.println("Reservations update failed");
                }

            }
        }   catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
                private static void deleteReservation(Connection connection, Scanner scanner) {
                    try {
                        System.out.println("Enter reservation Id to Delete:; ");
                        int reservationId =scanner.nextInt();


                        if (!reservationIdExists(connection, reservationId)){
                            System.out.println("Reservation not found for the given Id.");
                            return;
                        }
                              String sql ="DELETE FROM reservations WHERE reservation_id = " + reservationId;

                        try(Statement statement =connection.createStatement()) {
                            int affectedRows = statement.executeUpdate(sql);

                            if (affectedRows > 0)
                            {
                                System.out.println("Reservation deleted succesfully");
                            } else
                            {
                                System.out.println("Reservation deletion Failed.");
                        }

                          }
                    }   catch (SQLException e){
                        e.printStackTrace();
                    }
}

    private static boolean reservationIdExists(Connection connection, int reservationId) {
        try
        {
            String sql ="SELECT reservation_id FROM reservations WHERE reservation_id =" +reservationId;

            try(Statement statement= connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql))
            {
                return  resultSet.next();
            }

        }   catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
        private static void exit()throws InterruptedException {
            System.out.println("Exiting System");
            int i = 5;
            while (i != 0) {
                System.out.print(".");
                Thread.sleep(450);
                i--;
                }
            System.out.println();
            System.out.println("-----------Thank you For Using Hotel Reservation System!!!----------");
            }
        }



