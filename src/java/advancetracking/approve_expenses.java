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
public class approve_expenses extends HttpServlet {

    HttpSession session;
    String credit_id,status;
    String message;
    int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
            credit_id = request.getParameter("credit_id");
            status="1";
            if(session.getAttribute("approve_expenses")!=null){
            if(session.getAttribute("approve_expenses").toString().equals("1")){
            String approve = "UPDATE credit SET approved=? WHERE credit_id=?";
            conn.pst = conn.conn.prepareStatement(approve);
            conn.pst.setString(1, status);
            conn.pst.setString(2, credit_id);
            
            int num = conn.pst.executeUpdate();
            if(num>0){
                         message="Expenses approved successfully."; 
                         code=1;
                         if(session.getAttribute("pending_approval")!=null){
                             int pending = Integer.parseInt(session.getAttribute("pending_approval").toString())-1;
                             session.setAttribute("pending_approval", pending);
                         }
                         
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
        }
            else{
             message="You are not allowed to do this operation."; 
             code=0;    
            }
            
            }
            else{
             message="Login to do this operation."; 
             code=0;    
            }
                   JSONObject obj = new JSONObject();
                   obj.put("message", message);
                   obj.put("code", code);
                   
                   JSONObject final_obj = new JSONObject();
                   final_obj.put("data", obj);
                    
            out.println(final_obj);
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
            Logger.getLogger(approve_expenses.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(approve_expenses.class.getName()).log(Level.SEVERE, null, ex);
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
