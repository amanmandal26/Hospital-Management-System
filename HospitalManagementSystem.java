package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String url="jdbc:mysql://localhost:3306/Hospital";
    private static final String username="root";
    private static final String password="Aman@123";

    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url,username,password);
        Scanner scanner = new Scanner(System.in);
        Patient patient = new Patient(connection, scanner);
        Doctor doctor = new Doctor(connection);
        while(true){
            System.out.println("Hospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. View Doctors");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.println("Enter your Choice");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    //Add Patient
                    patient.addPatient();
                    System.out.println();
                    break;
                case 2:
                    //View Patient
                    patient.viewPatients();
                    System.out.println();
                    break;
                case 3:
                    //View Doctor
                    doctor.viewDoctors();
                    System.out.println();
                    break;
                case 4:
                    //Book Appointment
                   bookAppointment(patient,doctor,connection,scanner);
                    System.out.println();
                    break;
                case 5:
                    return;

                default:
            }

        }

    }
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter patient id");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date(yyyy-mm-dd):");
        String appointmentDate = scanner.next();
        if(patient.getPatientbyId(patientId) && doctor.getDoctorsbyId(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id,appointment_date) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if(rowAffected>0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("Fail to book appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor is not available on this date");
            }
        }else{
            System.out.println("Either doctor or patient doesnt  exist");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments where doctor_id =? and appointment_date=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
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
