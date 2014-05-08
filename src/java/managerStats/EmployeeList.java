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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author yv
 */
@Named(value = "employeeList")
@Dependent
public class EmployeeList {

    private static final ArrayList<Employee> employees = new ArrayList<Employee>();
    private static Employee selected;
    private int selection;
    private static int SSN;
    private static int manager;
    private static Date startDate;
    private static double hourlyRate;
    private static String firstName;
    private static String lastName;
    private static String address;
    private static String city;
    private static String state;
    private static int zip;
    private static int dBName;
    private static int dBPassword;
    private static int name;
    private static int password;
    private String loggedIn;
    private int isManager;

    public String getLoggedIn() {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("loggedIn");
    }

    public int getdBName() {
        return dBName;
    }

    public void setdBName(int dBName) {
        EmployeeList.dBName = dBName;
    }

    public int getdBPassword() {
        return dBPassword;
    }

    public void setdBPassword(int dBPassword) {
        EmployeeList.dBPassword = dBPassword;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        EmployeeList.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        EmployeeList.password = password;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
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

    public ArrayList<Employee> getEmployees() {
        return employees;
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

    public void dbData(int uName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }
        if (name >= 0) {
            PreparedStatement ps = null;
            Connection con = null;
            ResultSet rs = null;

            try {
                con = DriverManager.getConnection("jdbc:mysql://mysql2.cs.stonybrook.edu:3306/mlavina", "mlavina", "108262940");
                if (con != null) {
                    con.setAutoCommit(false);
                    try {
                        String sql = "select id,ssn,ismanager from employee where ismanager=1 and id = '"
                                + uName + "'";
                        ps = con.prepareStatement(sql);
                        rs = ps.executeQuery();
                        rs.next();
                        dBName = rs.getInt("id");
                        dBPassword = rs.getInt("ssn");
                        isManager = rs.getInt("ismanager");
                        con.commit();
                    } catch (Exception e) {
                        con.rollback();
                    }
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
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
    }

    public String login() {
        dbData(name);
        if (name == dBName && password == dBPassword && isManager==1) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("loggedIn", "valid");
            loggedIn = "valid";
            return "valid";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedIn","invalid");
            return "invalid";
        }
    }

    public void addEmployee() {
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
                con.setAutoCommit(false);
                String sql = "SELECT max(P.Id), max(E.SSN) FROM employee E, person P";
                ps = con.prepareStatement(sql);
                int id = 0;
                int newSSN = 0;
                System.out.println(ps);
                try {
                    ps.execute();
                    rs = ps.getResultSet();
                    if (rs.next()) {
                        id = rs.getInt(1) + 1;
                        newSSN = rs.getInt(2) + 1;
                    }
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }

                sql = "INSERT INTO Person VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, address);
                ps.setString(5, city);
                ps.setString(6, state);
                ps.setInt(7, zip);
                System.out.println(ps);
                try {
                    ps.execute();
                    con.commit();
                } catch (Exception e) {
                    con.rollback();
                }

                sql = "INSERT INTO Employee VALUES (?, ?, ?, ?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, newSSN);
                ps.setInt(3, manager);
                ps.setDate(4, new java.sql.Date(startDate.getTime()));
                ps.setDouble(5, hourlyRate);
                System.out.println(ps);
                try {
                    ps.execute();
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

    public void editEmployee() {
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
                con.setAutoCommit(false);
                String sql = "UPDATE Employee SET IsManager = ?,StartDate = ? ,HourlyRate = ?  WHERE SSN = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, selected.manager);
                ps.setDate(2, new java.sql.Date(selected.startDate.getTime()));
                ps.setDouble(3, selected.hourlyRate);
                ps.setInt(4, selected.SSN);
                try {
                    ps.execute();
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

    public void deleteEmployee() {
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
                con.setAutoCommit(false);
                String sql = "DELETE FROM Employee WHERE SSN = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, selected.SSN);
                try {
                    ps.execute();
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

    public void populateEmployees() {

        employees.removeAll(employees);
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
                String sql = "SELECT E.SSN, E.isManager, E.StartDate, E.HourlyRate, \n"
                        + "P.FirstName, P.LastName, P.Address, P.City, P.State, P.ZipCode \n"
                        + "FROM Person P, Employee E \n"
                        + "WHERE P.Id = E.Id AND E.SSN <> 0";
                ps = con.prepareStatement(sql);
                ps.execute();

                rs = ps.getResultSet();
                while (rs.next()) {
                    employees.add(new Employee(
                            rs.getInt("E.SSN"), rs.getInt("E.isManager"), new Date(rs.getDate("E.StartDate").getTime()),
                            rs.getDouble("E.HourlyRate"), rs.getString("P.FirstName"), rs.getString("P.LastName"),
                            rs.getString("P.Address"), rs.getString("P.City"), rs.getString("P.State"),
                            rs.getInt("P.Zipcode")
                    ));
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

    public class Employee {

        int SSN;
        int manager;
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

        public int getManager() {
            return manager;
        }

        public void setManager(int manager) {
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

        public Employee(int SSN, int manager, Date startDate, double hourlyRate, String firstName, String lastName, String address, String city, String state, int zip) {
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
