package managerStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author yv
 */
@Named(value = "revenueList")
@Dependent
public class RevenueList {

    private static final ArrayList<Revenue> revenue = new ArrayList<Revenue>();
    private static int flightNum;
    private static String flightID;
    private static String city;
    private static String firstName;
    private static String lastName;
    private static int custNum;
    private static String input;

    public ArrayList<Revenue> getRevenue() {
        return revenue;
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
    

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }
    
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
    
    public int getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(int flightNum) {
        this.flightNum = flightNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCustNum() {
        return custNum;
    }

    public void setCustNum(int custNum) {
        this.custNum = custNum;
    }
    
    public void flightRecord(){
        revenue.removeAll(revenue);
        StringTokenizer st = new StringTokenizer(input);

        try {
        flightID = st.nextToken();
        flightNum = Integer.parseInt(st.nextToken()); }
        catch(Exception e) {
            /* uh oh bad input! */ }
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
 try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue \n"
                        + "FROM Reservation R, Includes I \n"
                        + "WHERE I.ResrNo = R.ResrNo AND I.AirlineID = ? AND I.FlightNo = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, flightID);
                ps.setInt(2, flightNum);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    revenue.add(new Revenue(rs.getInt("ResrNo"), rs.getDouble("Revenue")));
                }

            }
        } 
        catch (Exception e) {
            System.out.println(e);
        } 
        finally {
            try {
                con.close();
                ps.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        } 
    }
    
    public void customerNumber(){
        revenue.removeAll(revenue);
        custNum = Integer.parseInt(input);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue \n"+
                        "FROM Reservation R \n"+
                        "WHERE R.AccountNo = ?";
               ps = con.prepareStatement(sql);
               ps.setInt(1, custNum);
               ps.executeQuery();
               rs = ps.getResultSet();
               while(rs.next()){
                   revenue.add( new Revenue(
                      rs.getInt("ResrNo"), rs.getDouble("Revenue") ));
               }
            }
            
        }
        catch (Exception e) {
            System.out.println(e);
        } 
        finally {
            try {
                con.close();
                ps.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        } 
    }
    
    public void cityName(){
        revenue.removeAll(revenue);
        city = input;
         try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
                try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "CREATE OR REPLACE VIEW ResrFlightLastLeg(ResrNo, AirlineID, FlightNo, LegNo) \n"
                        + "AS \n"
                        + "SELECT I.ResrNo, I.AirlineID, I.FlightNo, MAX(I.LegNo) \n"
                        + "FROM Includes I \n"
                        + "GROUP BY I.ResrNo, I.AirlineID, I.FlightNo";
                ps = con.prepareStatement(sql);
                ps.execute();
                sql = "SELECT DISTINCT R.ResrNo, R.TotalFare * 0.1 AS Revenue \n"
                        + "FROM Reservation R, Leg L, ResrFlightLastLeg LL, Airport A \n"
                        + "WHERE R.ResrNo = LL.ResrNo AND L.AirlineID = LL.AirlineID \n"
                        + "AND L.FlightNo = LL.FlightNo AND L.LegNo = LL.LegNo \n"
                        + "AND L.ArrAirportID = A.ID AND A.City = ?"; 
                ps = con.prepareStatement(sql);
                ps.setString(1, city);
                System.out.println(ps);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    revenue.add(new Revenue(rs.getInt("ResrNo"), rs.getDouble("Revenue")));
                }

            }
        } 
        catch (Exception e) {
            System.out.println(e);
        } 
        finally {
            try {
                con.close();
                ps.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        } 
        
    }
    
    public void airlineName(){
        revenue.removeAll(revenue);
        String airlineName = input;
         try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            /* uh oh no driver! */ }
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs;
                try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT DISTINCT F.* FROM Flight F, Leg L, Airport A WHERE F.AirlineID = L.AirlineID AND F.FlightNo = L.FlightNo AND (L.DepAirportId = A.Id OR L.ArrAirportId = A.Id) AND A.Name = ?"; 
                ps = con.prepareStatement(sql);
                ps.setString(1, airlineName);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    revenue.add(new Revenue(rs.getString("AirlineID"), rs.getInt("FlightNo"), rs.getInt("NoOfSeats")
                    , rs.getString("DaysOperating"), rs.getInt("MinLengthOfStay"), rs.getInt("MaxLengthofStay")));
                }

            }
        } 
        catch (Exception e) {
            System.out.println(e);
        } 
        finally {
            try {
                con.close();
                ps.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        } 
        
    }
    
    /**
     * Creates a new instance of RevenueList
     */
    public RevenueList() {
    }
    
     public static class Revenue {
         int resNum;
         double revenue;
         String airlineId;
         int flightNo;
         int noSeats;
         String daysOperating;
         int minLength;
         int maxLength;
         
         
         
         public Revenue(int resNum, double revenue){
             this.resNum = resNum;
             this.revenue = revenue;
         }

        public Revenue(String airlineId, int flightNo, int noSeats, String daysOperating, int minLength, int maxLength) {
            this.airlineId = airlineId;
            this.flightNo = flightNo;
            this.noSeats = noSeats;
            this.daysOperating = daysOperating;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }

        public String getAirlineId() {
            return airlineId;
        }

        public void setAirlineId(String airlineId) {
            this.airlineId = airlineId;
        }

        public int getFlightNo() {
            return flightNo;
        }

        public void setFlightNo(int flightNo) {
            this.flightNo = flightNo;
        }

        public int getNoSeats() {
            return noSeats;
        }

        public void setNoSeats(int noSeats) {
            this.noSeats = noSeats;
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
         
         

        public int getResNum() {
            return resNum;
        }

        public void setResNum(int resNum) {
            this.resNum = resNum;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }
         
        @Override
        public String toString() {
            return resNum+" $"+revenue;
        }
         
     }
    
}
