import java.io.*
import java.net.*
import java.sql.*

public class MediCareServer {
public static void main(String[] args) {
try(ServerSocket serverSocket = new ServerSocket(1234)) {
System.out.println("MediCare Server is running and waiting for clients.");
while (true) {
Socket socket = serverSocket.accept();
System.out.println("New client
new ClientHandler(socket).start(); // Handling Each Client in a separate Thread
}
} catch (IOException e) {
e.printStackTrace
}
}

}

class ClientHandler extends Thread {
private Socket socket;

public ClientHandler(Socket socket) {
this.socket = socket;

}
public void run() {
try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
// RECEIVE DATA FROM CLIENT: PatientID, ServiceType, PatientType

String request = in.readLine();
String[] data = request.split(",");
int patientId = Integer.parseInt(data[

String serviceType = data[1].

String patientType = data[2].

double basePrice = getServicePrice(serviceType
String insurance = getInsurancePlanFromDB
// Calculating Bill according to Rules

double finalBill = calculateLogic(basePrice, insurance, patient
saveBillToDB
out.println("Calculation Complete. Final Bill for " + serviceType + " is: OMR " + finalBill);
}
catch (Exception e) {
System.out.println("Error: " +
}
}

private double getServicePrice(String service) {
switch (service) {
case "MRI": return 180.
case "CT Scan": return 120.0
case "Blood Test": return 20.0
case "X-RAY": return
default: return 0.
}
}

private String getInsurancePlanFromDB(int id)
String plan = "Basic";
try (
        Connection conn = DriverManager.getConnection("jdbc:derby://
PreparedStatement ps = conn.prepareStatement("SELECT InsurancePlan FROM Patient WHERE PatientID = ?")) {
ps.setInt(1

ResultSet rs = ps.executeQuery();
if (rs.next()) plan = rs.getString("
} catch (SQLException e) { e.printStackTrace(); }
return plan;

}

private double calculateLogic(double price, String insurance, String type) {
double insDiscount = 0;

if (insurance.equals("Premium")) insDiscount =

} else if (insurance.equals("Standard")) insDiscount =
double amountAfterIns = price - (price * insDiscount);
// 2. Deducting the
double visitDiscount = 0;
if (type.equals("Inpatient")) visitDiscount
else if (type.equals("Outpatient")) visitDiscount =
}else if(type.equals("Emergency")) visitDiscount = 10
double finalAmount = amountAfterIns – visitDiscount; // 3. Emergency service fees }else if (type.equals finalAmount += finalAmount * 0.15 } return finalAmount; } private void saveBillToDB(int id, double amount) { try {
      Connection conn = DriverManager.getConnection("jdbc:derby:// &amp; PreparedStatement ps = conn.prepareStatement("INSERT INTO PatientBill (PatientID, VisitDate, TotalAmount) VALUES (?, CURRENT_DATE, ? ps.setInt(1 ps.setDouble( ps.executeUpdate } catch (SQLException e) {
    e.printStackTrace();
} } }
