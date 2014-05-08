package managerStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Gives a history of all reservations, dictated either by a customer ID, or by
 * a flight number.
 *
 * @author yv <yvonne@yvds.net>
 */
@ManagedBean(name = "reshistory")
@RequestScoped
public class ReservationHistory {

    private static final ArrayList<Reservation> reservations = new ArrayList<Reservation>();
    private static int flightNum;
    private static String flightID;
    private static int customerID;
    private static String firstName;
    private static String lastName;
    private static String textInput;

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setFlightNum(int flightNum) {
        this.flightNum = flightNum;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }

    public int getFlightNum() {
        return flightNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCustomerID() {
        return customerID;
    }

    /**
     * Creates a new instance of ReservationHistory
     */
    public ReservationHistory() {

    }

    public void flightSeatRecord() {
        reservations.removeAll(reservations);
        StringTokenizer st = new StringTokenizer(textInput);
        try {
            flightID = st.nextToken();
            flightNum = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            /* uh oh bad input! */ }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "    SELECT DISTINCT P.Id, P.FirstName, P.LastName FROM Reservation R, Includes I, ReservationPassenger RP, Person P WHERE I.AirlineID= ? AND I.FlightNo = ? AND I.ResrNo = R.ResrNo AND R.ResrNo = RP.ResrNo AND RP.Id = P.Id";
                ps = con.prepareStatement(sql);
                ps.setString(1, flightID);
                ps.setInt(2, flightNum);
                System.out.println(ps);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    reservations.add(new Reservation(rs.getString("FirstName"), rs.getString("LastName"),rs.getInt("Id")));
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 

    public void flightRecord() {
        reservations.removeAll(reservations);
        StringTokenizer st = new StringTokenizer(textInput);
        try {
            flightID = st.nextToken();
            flightNum = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
            /* uh oh bad input! */ }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT DISTINCT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, \n"
                        + "R.RepSSN, P.FirstName, P.LastName \n"
                        + " FROM Reservation R, Customer C, Person P, Includes I \n"
                        + " WHERE R.AccountNo = C.AccountNo AND C.Id = P.Id \n"
                        + " AND I.ResrNo = R.ResrNo AND I.AirlineID = ? AND I.FlightNo = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, flightID);
                ps.setInt(2, flightNum);
                System.out.println(ps);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    reservations.add(new Reservation(rs.getInt("ResrNo"), rs.getDate("ResrDate"), rs.getDouble("BookingFee"),
                            rs.getDouble("TotalFare"), rs.getInt("RepSSN"), rs.getString("FirstName"), rs.getString("LastName")));
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void customerRecord() {
        reservations.removeAll(reservations);
        StringTokenizer st = new StringTokenizer(textInput);
        try {
            firstName = st.nextToken();
            lastName = st.nextToken();
        } catch (Exception e) {
            /* uh oh bad input! */ }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT DISTINCT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, \n"
                        + "R.RepSSN, P.FirstName, P.LastName \n"
                        + " FROM Reservation R, Customer C, Person P \n"
                        + " WHERE R.AccountNo = C.AccountNo AND C.Id = P.Id \n"
                        + " AND P.FirstName = ? AND P.LastName = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, firstName);
                ps.setString(2, lastName);

                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    reservations.add(new Reservation(rs.getInt("ResrNo"), rs.getDate("ResrDate"), rs.getDouble("BookingFee"),
                            rs.getDouble("TotalFare"), rs.getInt("RepSSN"), rs.getString("FirstName"), rs.getString("LastName")));
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class Reservation {

        int resNum;
        Date resDate;
        double bookingFee;
        double totalFare;
        int repSSN;
        String firstName;
        String lastName;
        int acctNo;

        public Reservation(int resNum, Date resDate, double bookingFee, double totalFare, int repSSN, String firstName, String lastName) {
            this.resNum = resNum;
            this.resDate = resDate;
            this.bookingFee = bookingFee;
            this.totalFare = totalFare;
            this.repSSN = repSSN;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public Reservation(String firstName, String lastName, int acctNo) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.acctNo = acctNo;
        }

        
        public int getAcctNo() {
            return acctNo;
        }

        public void setAcctNo(int acctNo) {
            this.acctNo = acctNo;
        }

        
        public int getResNum() {
            return resNum;
        }

        public void setResNum(int resrNo) {
            this.resNum = resrNo;
        }

        public Date getResDate() {
            return resDate;
        }

        public void setResDate(Date resrDate) {
            this.resDate = resrDate;
        }

        public double getBookingFee() {
            return bookingFee;
        }

        public void setBookingFee(double bookingFee) {
            this.bookingFee = bookingFee;
        }

        public double getTotalFare() {
            return totalFare;
        }

        public void setTotalFare(double totalFare) {
            this.totalFare = totalFare;
        }

        public int getRepSSN() {
            return repSSN;
        }

        public void setRepSSN(int repSSN) {
            this.repSSN = repSSN;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName + " " + resNum + " " + resDate + " $" + totalFare + " $" + bookingFee + " " + repSSN;

        }

    }

}
