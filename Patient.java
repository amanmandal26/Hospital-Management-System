package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("Enter patient name ");
        String name = scanner.next();
        System.out.print("Enter patient age ");
        int  age  = scanner.nextInt();
        System.out.print("Enter patient gender ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO Patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added Successully");
            }else{
                System.out.println("failed to  add");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void viewPatients(){
        String query ="select * from Patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+-------------+----------------+-----------+-----------+");
            System.out.println("| Patient Id  | Name           | Age       | Gender    |");
            System.out.println("+-------------+----------------+-----------+-----------+");
            while(resultSet.next()){
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s | %-14s | %-9s | %-9s |\n",id,name,age,gender);
                System.out.println("+-------------+----------------+-----------+-----------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientbyId(int id){
        String query = "select * from Patients where id =?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
