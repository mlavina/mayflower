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
import java.sql.SQLException;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author yv
 */
@Named(value = "highestRevenue")
@Dependent
public class HighestRevenue {

    private static String custFirstName;
    private static String custLastName;
    private static int customerID;
    private static int employeeID;
    private static double customerRevenue;
    private static double employeeRevenue;

    /**
     * Creates a new instance of HighestRevenue
     */
    public HighestRevenue() {
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public double getCustomerRevenue() {
        return customerRevenue;
    }

    public void setCustomerRevenue(double customerRevenue) {
        this.customerRevenue = customerRevenue;
    }

    public double getEmployeeRevenue() {
        return employeeRevenue;
    }

    public void setEmployeeRevenue(double employeeRevenue) {
        this.employeeRevenue = employeeRevenue;
    }

    public void highestEmployee() {
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
                String sql = "CREATE OR REPLACE VIEW CRRevenue(SSN, TotalRevenue) \n"
                        + "AS \n"
                        + "SELECT RepSSN, SUM(TotalFare * 0.1) \n" 
                        +" FROM Reservation WHERE RepSSN <> 0 GROUP BY RepSSN ;";

                ps = con.prepareStatement(sql);
                ps.execute();
                
                sql = "SELECT SSN, TotalRevenue FROM CRRevenue \n"
                        + "WHERE TotalRevenue >= (SELECT MAX(TotalRevenue) FROM CRRevenue)";
                ps = con.prepareStatement(sql);
                ps.execute();
                
                
                rs = ps.getResultSet();
                if (rs.next()) {
                    employeeID = rs.getInt("SSN");
                    employeeRevenue = rs.getDouble("TotalRevenue");
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

    public void highestCustomer() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //return "ISSUE WITH DATABASE DRIVER!";
        }

        try {
            PreparedStatement ps;
            Connection con;
            ResultSet rs;
            con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
            if (con != null) {
                String sql = "CREATE OR REPLACE VIEW CustomerRevenue(AccountNo, TotalRevenue) \n"
                        + "AS \n"
                        + "SELECT AccountNo, SUM(TotalFare * 0.1) \n"
                        + "FROM Reservation \n"
                        + "GROUP BY AccountNo";
                ps = con.prepareStatement(sql);
                ps.execute();
         
                
                sql = "SELECT CR.TotalRevenue, CR.AccountNo, P.FirstName, P.LastName \n"
                        + "FROM CustomerRevenue CR, Customer C, Person P \n"
                        + "WHERE CR.AccountNo = C.AccountNo AND C.Id = P.Id \n"
                        + "AND CR.TotalRevenue >= (SELECT MAX(TotalRevenue) FROM CustomerRevenue)";
                ps = con.prepareStatement(sql);
                ps.execute();

                
                rs = ps.getResultSet();
                if (rs.next()) {
                    this.customerRevenue = rs.getDouble("TotalRevenue");
                    this.customerID = rs.getInt("AccountNo");
                    this.custLastName = rs.getString("LastName");
                    this.custFirstName = rs.getString("FirstName");
                }
            }

        } catch (SQLException sqle) {
            //return "ISSUE WITH DATABASE TRANSACTION!";
            System.out.println("ISSUE WITH DATABASE TRANSACTION");
            System.out.println(sqle);
        }
    }

}
