/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managerStats;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 *
 * @author yv
 */
@Named(value = "salesReport")
@Dependent
public class SalesReport {

    private static final ArrayList<Revenue> revenue = new ArrayList<Revenue>();
    private static Date d1;
    private static Date d2;

    public ArrayList<Revenue> getRevenue() {
        return revenue;
    }

    

    public Date getD2() {
        return d2;
    }

    public void setD2(Date d2) {
        this.d2 = d2;
    }
    
    public Date getD1() {
        return d1;
    }

    public void setD1(Date d) {
        this.d1 = d;
    }
    /**
     * Creates a new instance of SalesReport
     */
    public SalesReport() {
    }
    
    public void makeReport(){

        revenue.removeAll(revenue);
        
        try { 
            Class.forName("com.mysql.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            //return "ISSUE WITH DATABASE DRIVER!";
        }
            PreparedStatement ps = null;
            Connection con = null;
            ResultSet rs;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "SELECT R.ResrNo, R.ResrDate, R.TotalFare, R.BookingFee, \n"+
                    "R.RepSSN, P.FirstName, P.LastName \n"+
                    "FROM Reservation R, Customer C, Person P \n"+
                    "WHERE R.ResrDate >= ? AND R.ResrDate <= ? \n"+
                    "AND R.AccountNo = C.AccountNo AND C.Id = P.Id ";
               ps = con.prepareStatement(sql);
               ps.setDate(1, new java.sql.Date(d1.getTime()));
               ps.setDate(2, new java.sql.Date(d2.getTime()));
               ps.execute();
               rs = ps.getResultSet();
               while(rs.next()){
                   revenue.add( new Revenue(
                      rs.getInt("ResrNo"), new java.util.Date(rs.getDate("ResrDate").getTime()), rs.getDouble("TotalFare"),
                      rs.getDouble("BookingFee"), rs.getInt("RepSSN"),
                      rs.getString("FirstName"), rs.getString("LastName")
                   ));
               }

            }
            
            
        }
        catch(SQLException sqle){
            //return "ISSUE WITH DATABASE TRANSACTION!";
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
    
    public static class Revenue{
        
        int resNum;
        Date bookingDate;
        double totalFare;
        double bookingFee;
        int SSN;
        String firstName;
        String lastName;

        public Revenue(int resNum, Date bookingDate, double totalFare, double bookingFee, int SSN, String firstName, String lastName) {
            this.resNum = resNum;
            this.bookingDate = bookingDate;
            this.totalFare = totalFare;
            this.SSN = SSN;
            this.firstName = firstName;
            this.lastName = lastName;
            this.bookingFee = bookingFee;
        }

        public double getBookingFee() {
            return bookingFee;
        }

        public void setBookingFee(double bookingFee) {
            this.bookingFee = bookingFee;
        }
        

        public int getResNum() {
            return resNum;
        }

        public void setResNum(int resNum) {
            this.resNum = resNum;
        }

        public Date getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(Date bookingDate) {
            this.bookingDate = bookingDate;
        }

        public double getTotalFare() {
            return totalFare;
        }

        public void setTotalFare(double totalFare) {
            this.totalFare = totalFare;
        }

        public int getSSN() {
            return SSN;
        }

        public void setSSN(int SSN) {
            this.SSN = SSN;
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
        
    }
    
}
