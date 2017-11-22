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
public class update_credit extends HttpServlet {
HttpSession session;
String credit_id,amount,date,debit_id,health_type_id,health_id,healths;
String fco,gl_code;
int effect,balance;
String message,prev_date;
int code,prev_amount;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           fco = request.getParameter("fco");
           gl_code = request.getParameter("gl_code");
           credit_id=request.getParameter("credit_id");
           amount=request.getParameter("amount");
           date=request.getParameter("date");
           healths=request.getParameter("health_id");
           
           if(healths.contains("-") && gl_code.equals("523")){
           String[] health_details = healths.split("-");
           health_type_id=health_details[0];
           health_id = health_details[1];
           }
           else if(!gl_code.equals("523")){
           health_type_id = null;
           health_id = null;  
           }
           else{
           gl_code="";
           }
           System.out.println("credit:"+credit_id+", amount : "+amount+", date : "+date);
           effect=balance=code=prev_amount=0;
           debit_id=message=prev_date="";
           
           if(!gl_code.equals("")){
           String checkEffect="Select "+amount+"-amount AS effect,amount AS prev_amount,debit_id,date AS prev_date FROM credit WHERE credit_id=?";
           conn.pst=conn.conn.prepareStatement(checkEffect);
           conn.pst.setString(1, credit_id);
           conn.rs=conn.pst.executeQuery();
           if(conn.rs.next()){
               effect=conn.rs.getInt(1);
               debit_id = conn.rs.getString(3);
               prev_amount=conn.rs.getInt(2);
               prev_date=conn.rs.getString(4);
           }
           
           if(session.getAttribute("staff_status")!=null){
           if(session.getAttribute("staff_status").toString().equals("1")){   
           if(!amount.equals("0")){
           //level 2 checking
           balance=0;
            String get_current_balance="SELECT debit.amount-(IFNULL(SUM(credit.amount),0)+"+effect+") AS balance FROM debit LEFT JOIN credit ON debit.debit_id=credit.debit_id WHERE debit.debit_id=?";
            conn.pst=conn.conn.prepareStatement(get_current_balance);
            conn.pst.setString(1, debit_id);
            conn.rs=conn.pst.executeQuery();
            if(conn.rs.next()){
                balance=conn.rs.getInt(1);
            }
            System.out.println(get_current_balance);
            if(balance>=0){
                String updator="UPDATE credit SET amount=?,date=?,health_id=?,health_type_id=?,fco=?,gl_code=? WHERE credit_id=?";
                conn.pst=conn.conn.prepareStatement(updator);
                conn.pst.setString(1, amount);
                conn.pst.setString(2, date);
                conn.pst.setString(3, health_id);
                conn.pst.setString(4, health_type_id);
                conn.pst.setString(5, fco);
                conn.pst.setString(6, gl_code);
                conn.pst.setString(7, credit_id);
                
                conn.pst.executeUpdate();
                message = "Details updated successfully.";
                code = 1;
            }
            else{
             message = "Error encountered while updating. New amount entered is more than the current balance.";
                code = 0;   
            }
           }
           else{
               //delete this entry
               String deleter="DELETE FROM credit WHERE credit_id=?";
               conn.pst=conn.conn.prepareStatement(deleter);
               conn.pst.setString(1, credit_id);
               conn.pst.executeUpdate();
               message="Item removed successfully.";
               code=1;
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
           
           }
           else{
            code=0;
            message="Not updated. For GL Code 523, a health facility or Health management team must be selected.";       
           }
            JSONObject obj = new JSONObject();
            obj.put("message", message);
            obj.put("code", code);
            obj.put("prev_amount", prev_amount);
            obj.put("prev_date", prev_date);
            
            JSONObject obj_final = new JSONObject();
            obj_final.put("data", obj);
            
            System.out.println("data : "+obj_final);
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
        Logger.getLogger(update_credit.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(update_credit.class.getName()).log(Level.SEVERE, null, ex);
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
