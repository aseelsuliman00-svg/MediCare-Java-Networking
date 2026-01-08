package db1;
 
 
import java.io.*;

import java.net.*;

import java.util.*;
 
class EmpClient implements Runnable {
 
    public void run() {

        try {

            Socket s = new Socket("localhost", 7777);
 
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Employee ID: ");

            int empid = sc.nextInt();
 
            // Send EMPID to server

            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            dos.writeInt(empid);

            dos.flush();
 
            // Receive data from server

            DataInputStream dis = new DataInputStream(s.getInputStream());
 
            String name = dis.readUTF();

            int salary = dis.readInt();
 
            System.out.println("\n--- Employee Details ---");

            System.out.println("Employee ID : " + empid);

            System.out.println("Employee Name : " + name);

            System.out.println("Salary : " + salary);
 
            dis.close();

            dos.close();

            s.close();
 
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
 
public class CL{

    public static void main(String[] args) {

        Thread t = new Thread(new EmpClient());

        t.start();

    }

}
