package com.mindex.challenge.data;

public class ReportingStructure {
    private Employee employee;
    private int numberofReports;

    public ReportingStructure() {

    }

    public ReportingStructure(Employee employee, int numberofReports) {
        this.employee = employee;
        this.numberofReports = numberofReports;
    }
  
    @Override
    public String toString() {
        return "ReportingStructure [employee=" + employee + ", numberofReports=" + numberofReports + "]";
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberofReports() {
        return numberofReports;
    }

    public void setNumberofReports(int numberofReports) {
        this.numberofReports = numberofReports;
    }
}
