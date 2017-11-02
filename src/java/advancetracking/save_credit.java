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
public class save_credit extends HttpServlet {
HttpSession session;
String output;
String amount,date,debit_id,credit_id,message;
int balance,code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
           amount = request.getParameter("amount");
           date = request.getParameter("date");
           debit_id = request.getParameter("debit_id");
           
            JSONObject obj_final = new JSONObject();
            
            if(session.getAttribute("staff_status")!=null){
            if(session.getAttribute("staff_status").toString().equals("1")){   
            balance=0;
            String get_current_balance="SELECT debit.amount-IFNULL(SUM(credit.amount),0) AS balance FROM debit LEFT JOIN credit ON debit.debit_id=credit.debit_id WHERE debit.debit_id=?";
            conn.pst=conn.conn.prepareStatement(get_current_balance);
            conn.pst.setString(1, debit_id);
            conn.rs=conn.pst.executeQuery();
            if(conn.rs.next()){
                balance=conn.rs.getInt(1);
            }
            if(balance>=0){
                Manager manager = new Manager();
               credit_id=manager.getdatekey();
                String insertCredit = "INSERT INTO credit (credit_id,debit_id,amount,date) VALUES (?,?,?,?)";
                conn.pst=conn.conn.prepareStatement(insertCredit);
                conn.pst.setString(1, credit_id);
                conn.pst.setString(2, debit_id);
                conn.pst.setString(3, amount);
                conn.pst.setString(4, date);
                
                conn.pst.executeUpdate();
                code=1;
                message="Accounting data added successfuly.";
            }
            else{
             code=0;
             message="You are trying to account for more than your current balance.";
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
            
           JSONObject  obj = new JSONObject();
            obj.put("code", code);
            obj.put("message", message);
            
            obj_final.put("data", obj);
            System.out.println("output : "+obj_final);
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
        Logger.getLogger(save_credit.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_credit.class.getName()).log(Level.SEVERE, null, ex);
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
