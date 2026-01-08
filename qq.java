
package db1;
 
 
import java.io.*;

import java.net.*;

import java.sql.*;
 
class EmpServer implements Runnable {
 
    public void run() {

        try {

            // JDBC connection

            String url = "jdbc:derby://localhost:1527/db";



            String user = "db";     // change

            String pass = "db";     // change
 
            Connection con = DriverManager.getConnection(url, user, pass);
 
            ServerSocket ss = new ServerSocket(7777);

            System.out.println("Server started... waiting for client");
 
            Socket s = ss.accept();
 
            // Receive EMPID

            DataInputStream dis = new DataInputStream(s.getInputStream());

            int empid = dis.readInt();
 
            System.out.println("Received EMPID: " + empid);
 
            // Query DB

            String query = "SELECT * FROM EMP WHERE EMPID = " + empid;

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(query);
 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
 
            if (rs.next()) {

                String name = rs.getString("NAME");

                int salary = rs.getInt("SALARY");
 
                // Send data to client

                dos.writeUTF(name);

                dos.writeInt(salary);

            } else {

                dos.writeUTF("NOT FOUND");

                dos.writeInt(0);

            }
 
            dos.close();

            dis.close();

            s.close();

            ss.close();

            con.close();
 
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
 
public class SR {

    public static void main(String[] args) {

        Thread t = new Thread(new EmpServer());

        t.start();

    }

}
