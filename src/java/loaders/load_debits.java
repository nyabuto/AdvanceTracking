/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loaders;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class load_debits extends HttpServlet {
HttpSession session;
String staff_no,facility_name;
String debit_id,cheque_no,fco,gl_code,amount,date,purpose,timestamp,status;
int    status_id;
int total_debit,total_credit,balance;
String currency;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           dbConn conn = new dbConn();
           session = request.getSession();
           
             total_debit=total_credit=balance=status_id=0;
            JSONObject obj_final = new JSONObject();
            JSONArray arr = new JSONArray();
            currency="";
            
           String getcurrency="SELECT name FROM currency";
           conn.rs=conn.st.executeQuery(getcurrency);
           if(conn.rs.next()){
               currency = conn.rs.getString(1);
           }
            if(session.getAttribute("staff_no")!=null){
             staff_no = session.getAttribute("staff_no").toString();
            String getadvances = "SELECT debit_id,cheque_no,fco,gl_code,amount,date,purpose,timestamp,facility_id FROM debit WHERE staff_no=? ORDER BY timestamp DESC";
            conn.pst=conn.conn.prepareStatement(getadvances);
            conn.pst.setString(1, staff_no);
            conn.rs=conn.pst.executeQuery();
            while(conn.rs.next()){
            //loop through adding the items to array
             facility_name=status="";
             status_id=0;
             debit_id=cheque_no=fco=gl_code=amount=date=purpose=timestamp="";
              debit_id = conn.rs.getString(1);
              cheque_no = conn.rs.getString(2);
              fco = conn.rs.getString(3);
              gl_code = conn.rs.getString(4);
              amount = conn.rs.getString(5);
              total_debit = conn.rs.getInt(5);
              date = conn.rs.getString(6);
              purpose = conn.rs.getString(7);
              timestamp = conn.rs.getString(8);
              
              JSONObject obj = new JSONObject();
              
            //get facility supported
            if(conn.rs.getString("facility_id")!=null){
                String getfacilityname="SELECT facility_name,unique_code FROM facilities WHERE id='"+conn.rs.getString("facility_id")+"'";
                conn.rs1=conn.st1.executeQuery(getfacilityname);
                if(conn.rs1.next()){
                    facility_name=conn.rs1.getString(1);
                }
            }
            
            //get paid
            String getpaid="SELECT SUM(amount) FROM credit WHERE debit_id='"+debit_id+"'";
            conn.rs1=conn.st1.executeQuery(getpaid);
            if(conn.rs1.next()){
             total_credit=conn.rs1.getInt(1);
            }
            
            balance = total_debit-total_credit;
            
            if(balance>0 && balance!=total_debit){
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px; class=\"label label-warning\">Accounted : "+currency+". "+total_credit+"<br>"
                        + "Not Accounted : "+currency+". "+balance+"</span> ";
                status_id=2;
            }
            else if(total_credit==0){
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px;  class=\"label label-danger\">None Accounted</span> ";
                status_id=3;
            }
            else{
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px; class=\"label label-success\">Fully Accounted</span> ";
                status_id=1;
            }
              obj.put("debit_id",debit_id );
              obj.put("cheque_no", cheque_no);
              obj.put("fco", fco);
              obj.put("gl_code", gl_code);
              obj.put("amount", amount);
              obj.put("date", date);
              obj.put("purpose", purpose);
              obj.put("timestamp", timestamp);
              obj.put("facility_name", facility_name);
              obj.put("status", status);
              obj.put("status_id", status_id);
              obj.put("balance", balance);
              obj.put("currency", currency);
              
              
            arr.add(obj);
            }
            }
            
            
            obj_final.put("data", arr);
            out.println(obj_final);
            
            System.out.println("advances object : "+obj_final);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    try {
        processRequest(request, response);
    } catch (SQLException ex) {
        Logger.getLogger(load_debits.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    try {
        processRequest(request, response);
    } catch (SQLException ex) {
        Logger.getLogger(load_debits.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
