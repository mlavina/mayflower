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
import javax.inject.Named;

/**
 *
 * @author yv
 */
@Named(value = "employeeList")
@Dependent
public class EmployeeList {
    
    private ArrayList<Employee> employees = new ArrayList<Employee>();
    private Employee selected;
    private int selection;

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public Employee getSelected() {
        return selected;
    }

    public void setSelected(Employee selected) {
        this.selected = selected;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    
    /**
     * Creates a new instance of EmployeeList
     */
    public EmployeeList() {

    }
    
    public void addEmployee(){
        
    }
    
    public void editEmployee(){
        
    }
    
    public void deleteEmployee(){
        
    }
    
    public void populateEmployees(){
        
        employees = new ArrayList<Employee>();
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
                String sql = "SELECT E.SSN, E.isManager, E.StartDate, E.HourlyRate, \n"+
                              "P.FirstName, P.LastName, P.Address, P.City, P.State, P.ZipCode \n"+
                              "FROM Person P, Employee E \n" +
                              "WHERE P.Id = E.Id";
                ps = con.prepareStatement(sql);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    employees.add(new Employee( 
                            rs.getInt("E.SSN"), rs.getBoolean("E.isManager"), new Date(rs.getDate("E.StartDate").getTime()),
                            rs.getDouble("E.HourlyRate"), rs.getString("P.FirstName"), rs.getString("P.LastName"),
                            rs.getString("P.Address"), rs.getString("P.City"), rs.getString("P.State"),
                            rs.getInt("P.Zipcode")
                    ));
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
    
    public class Employee {
        
        int SSN;
        boolean manager;
        Date startDate;
        double hourlyRate;
        String firstName;
        String lastName;
        String address;
        String city;
        String state;
        int zip;

        public int getSSN() {
            return SSN;
        }

        public void setSSN(int SSN) {
            this.SSN = SSN;
        }

        public boolean isManager() {
            return manager;
        }

        public void setManager(boolean manager) {
            this.manager = manager;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public double getHourlyRate() {
            return hourlyRate;
        }

        public void setHourlyRate(double hourlyRate) {
            this.hourlyRate = hourlyRate;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getZip() {
            return zip;
        }

        public void setZip(int zip) {
            this.zip = zip;
        }

        public Employee(int SSN, boolean manager, Date startDate, double hourlyRate, String firstName, String lastName, String address, String city, String state, int zip) {
            this.SSN = SSN;
            this.manager = manager;
            this.startDate = startDate;
            this.hourlyRate = hourlyRate;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.city = city;
            this.state = state;
            this.zip = zip;
        }
        
        
        
    }
    
}
