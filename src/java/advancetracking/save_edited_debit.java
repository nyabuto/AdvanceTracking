/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advancetracking;

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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class save_edited_debit extends HttpServlet {
HttpSession session;
String debit_id,amount,date,fco,gl_code,cheque_no,facility,purpose;
int accounted,code;
String message;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
            accounted=0;
            
            cheque_no = request.getParameter("cheque_no");
            fco = request.getParameter("fco");
            gl_code = request.getParameter("gl_code");
            amount = request.getParameter("amount");
            date = request.getParameter("date");
            purpose = request.getParameter("purpose");
            facility = request.getParameter("facility");
            debit_id = request.getParameter("debit_id");
            
             if(session.getAttribute("staff_status")!=null){
             if(session.getAttribute("staff_status").toString().equals("1")){   
              
            String getpaid="SELECT SUM(amount) FROM credit WHERE debit_id=?";
            conn.pst = conn.conn.prepareStatement(getpaid);
            conn.pst.setString(1, debit_id);
            conn.rs=conn.pst.executeQuery();
            if(conn.rs.next()){
            accounted=conn.rs.getInt(1);
            }
            
            if(Integer.parseInt(amount)==0 && accounted==0){
             String deleter = "DELETE FROM debit WHERE debit_id=?";
             conn.pst=conn.conn.prepareStatement(deleter);
             conn.pst.setString(1, debit_id);
             conn.pst.executeUpdate();
             
             message = "Advance deleted successfully.";
             code=1;
            }
                    
               
            else{     
                System.out.println("data : "+Integer.parseInt(amount)+" accounted : "+accounted);     
            if(Integer.parseInt(amount)>=accounted){
            String updator = "UPDATE debit SET amount=?,date=?,fco=?,gl_code=?,cheque_no=?, facility_id=?, purpose=? WHERE debit_id=?";
            conn.pst=conn.conn.prepareStatement(updator);
            conn.pst.setString(1, amount);
            conn.pst.setString(2, date);
            conn.pst.setString(3, fco);
            conn.pst.setString(4, gl_code);
            conn.pst.setString(5, cheque_no);
            conn.pst.setString(6, facility);
            conn.pst.setString(7, purpose);
            conn.pst.setString(8, debit_id);
            
            conn.pst.executeUpdate();
              message = "Advance details updated successfully."; 
              code = 1;
            }
            else{
              message="Failed. Amount entered is less that amount already accounted for.";
              code=0; 
            }
            }
            }
             else{
            code=0;
             message="Account for this staff has been disabled, no operation is allowed.";         
             }
            }
            else{
             code=0;
             message="Staff cannot be determined, Go back and reselect the staff who was given this advance.";    
            }
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("message", message);
            
            JSONObject obj_final = new JSONObject();
            obj_final.put("data", obj);
            
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
        Logger.getLogger(save_edited_debit.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_edited_debit.class.getName()).log(Level.SEVERE, null, ex);
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
