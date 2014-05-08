/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Michael
 */
@Named(value = "flightList")
@Dependent
public class FlightList {

    private static final ArrayList<Flight> flights = new ArrayList<Flight>();
    private static String airlineID;
    private static int flightNo;
    private static int numSeats;
    private static String daysOperating;
    private static int minLength;
    private static int maxLength;

    /**
     * Creates a new instance of FlightList
     */
    public FlightList() {
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public int getFlightNo() {
        return flightNo;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public String getDaysOperating() {
        return daysOperating;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void delayedFlights() {
        flights.removeAll(flights);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {

            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                con.setAutoCommit(false);
                try {
                    String sql = "SELECT * FROM Flight F WHERE EXISTS ( SELECT * FROM Leg L WHERE F.AirlineID = L.AirlineID AND F.FlightNo = L.FlightNo AND (ActualArrTime > ArrTime OR ActualDepTime > DepTime))";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        flights.add(new Flight(rs.getString("AirlineID"), rs.getInt("FlightNo"), rs.getInt("NoOfSeats"), rs.getString("DaysOperating"), rs.getInt("MinLengthOfStay"), rs.getInt("MaxLengthOfStay")));
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
     public void onTimeFlights() {
        flights.removeAll(flights);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {

            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                con.setAutoCommit(false);
                try {
                    String sql = "SELECT * FROM Flight F WHERE NOT EXISTS ( SELECT * FROM Leg L WHERE F.AirlineID = L.AirlineID AND F.FlightNo = L.FlightNo AND (ActualArrTime > ArrTime OR ActualDepTime > DepTime))";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        flights.add(new Flight(rs.getString("AirlineID"), rs.getInt("FlightNo"), rs.getInt("NoOfSeats"), rs.getString("DaysOperating"), rs.getInt("MinLengthOfStay"), rs.getInt("MaxLengthOfStay")));
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
     
    public void makeFlights() {
        flights.removeAll(flights);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {

            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                con.setAutoCommit(false);
                try {
                    String sql = "SELECT * FROM Flight";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        flights.add(new Flight(rs.getString("AirlineID"), rs.getInt("FlightNo"), rs.getInt("NoOfSeats"), rs.getString("DaysOperating"), rs.getInt("MinLengthOfStay"), rs.getInt("MaxLengthOfStay")));
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void makeActiveFlights() {
        flights.removeAll(flights);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {

            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                con.setAutoCommit(false);
                String sql;
                try {
                    sql = "CREATE VIEW FlightReservation(AirlineID, FlightNo, ResrCount) AS SELECT I.AirlineID, I.FlightNo, COUNT(DISTINCT I.ResrNo) FROM Includes I GROUP BY I.AirlineID, I.FlightNo";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }

                try {
                    sql = "SELECT * FROM FlightReservation\n"
                            + "WHERE ResrCount >= (SELECT MAX(ResrCount) FROM FlightReservation)";
                    ps = con.prepareStatement(sql);
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        flights.add(new Flight(rs.getString("AirlineID"), rs.getInt("FlightNo"), rs.getInt("NoOfSeats"), rs.getString("DaysOperating"), rs.getInt("MinLengthOfStay"), rs.getInt("MaxLengthOfStay")));
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                con.setAutoCommit(true);
                con.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class Flight {

        String airlineID;
        int flightNo;
        int numSeats;
        String daysOperating;
        int minLength;
        int maxLength;

        public String getAirlineID() {
            return airlineID;
        }

        public void setAirlineID(String airlineID) {
            this.airlineID = airlineID;
        }

        public int getFlightNo() {
            return flightNo;
        }

        public void setFlightNo(int flightNo) {
            this.flightNo = flightNo;
        }

        public int getNumSeats() {
            return numSeats;
        }

        public void setNumSeats(int numSeats) {
            this.numSeats = numSeats;
        }

        public String getDaysOperating() {
            return daysOperating;
        }

        public void setDaysOperating(String daysOperating) {
            this.daysOperating = daysOperating;
        }

        public int getMinLength() {
            return minLength;
        }

        public void setMinLength(int minLength) {
            this.minLength = minLength;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }

        public Flight(String airLineID, int flightNo, int numSeats, String daysOperating, int minLength, int maxLength) {
            this.airlineID = airLineID;
            this.flightNo = flightNo;
            this.numSeats = numSeats;
            this.daysOperating = daysOperating;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }

        @Override
        public String toString() {
            return "Flight{" + "airlineID=" + airlineID + ", flightNo=" + flightNo + ", numSeats=" + numSeats + ", daysOperating=" + daysOperating + ", minLength=" + minLength + ", maxLength=" + maxLength + '}';
        }
        
    }
}
