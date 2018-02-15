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
public class load_expenses extends HttpServlet {
HttpSession session;
String staff_no,staff_name,email_address,phone,credit_id,fco_gl_code,amount_date,health_type,health;
String approval_status;
String debit_id,fco,gl_code,amount,date,health_type_id,health_id,fullname,email,cheque_no,receipt_no;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           session = request.getSession();
           dbConn conn = new dbConn();
           
         approval_status = request.getParameter("status");
         
//         approval_status="0";
            JSONArray jarray = new JSONArray();
            JSONObject obj_final = new JSONObject();
            
            if(session.getAttribute("approve_expenses")!=null){
            if(session.getAttribute("approve_expenses").toString().equals("1")){
         String get_details = "SELECT credit.credit_id AS credit_id,credit.debit_id AS debit_id,"
                 + "credit.fco AS fco,credit.gl_code AS gl_code,credit.amount AS amount,"
                 + "credit.date AS date,credit.health_type_id AS health_type_id,"
                 + "credit.health_id AS health_id,"
                 + "staff.staff_no AS staff_no,fullname,email,phone,cheque_no,receipt_no "
                 + "FROM credit "
                 + "LEFT JOIN debit ON credit.debit_id=debit.debit_id "
                 + "LEFT JOIN staff ON debit.staff_no=staff.staff_no "
                 + "WHERE credit.approved=? ORDER BY credit.timestamp DESC";
         conn.pst = conn.conn.prepareStatement(get_details);
         conn.pst.setString(1, approval_status);
         conn.rs = conn.pst.executeQuery();
         while(conn.rs.next()){
         health=receipt_no="";
         credit_id = conn.rs.getString(1);
         debit_id = conn.rs.getString(2);
         fco = conn.rs.getString(3);
         gl_code = conn.rs.getString(4);
         amount = conn.rs.getString(5);
         date = conn.rs.getString(6);
         health_type_id = conn.rs.getString(7);
         health_id = conn.rs.getString(8);
         staff_no =   conn.rs.getString(9);
         fullname = conn.rs.getString(10);
         email =    conn.rs.getString(11);    
         phone =  conn.rs.getString(12);
         cheque_no = conn.rs.getString(13);
         
         if(conn.rs.getString(14)!=null && !conn.rs.getString(14).equals("")){
         receipt_no = conn.rs.getString(14);
         health="Receipt no: "+receipt_no;
         }
         
         // get health details
         String gethealth="";
         if(health_type_id!=null){
          if(!health_type_id.equals("")){   
         if(health_type_id.equals("1")){
         gethealth="SELECT CHMT FROM county WHERE id='"+health_id+"'";
         }
         else if(health_type_id.equals("2")){
         gethealth="SELECT SCHMT FROM sub_county WHERE id='"+health_id+"'";
         }
         else if(health_type_id.equals("3")){
         gethealth="SELECT facility_name FROM facilities WHERE id='"+health_id+"'";
         }
         else if(health_type_id.equals("4")){
         gethealth="SELECT name FROM facility_accounting_types WHERE id='"+health_type_id+"'";
         }
             System.out.println("query : "+gethealth);
         conn.rs1 = conn.st1.executeQuery(gethealth);
         if(conn.rs1.next()){
             health = conn.rs1.getString(1);
         }
          }
         }
         else{ 
         }
             JSONObject obj = new JSONObject();
             obj.put("credit_id",credit_id );
             obj.put("fco", fco);
             obj.put("gl_code",gl_code );
             obj.put("amount",amount );
             obj.put("date", date);
             obj.put("staff_no", staff_no);
             obj.put("fullname", fullname);
             obj.put("email", email);
             obj.put("phone", phone);
             obj.put("cheque_no", cheque_no);
             obj.put("other_details", health);
        jarray.add(obj);
         }
            }
            }
            
         obj_final.put("data", jarray);
            out.println(obj_final);
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
        Logger.getLogger(load_expenses.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_expenses.class.getName()).log(Level.SEVERE, null, ex);
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
