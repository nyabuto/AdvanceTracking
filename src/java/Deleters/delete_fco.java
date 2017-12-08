/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deleters;

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
public class delete_fco extends HttpServlet {
HttpSession session;
String message,fco;
int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           fco = request.getParameter("fco");
           
           //check fco
           String checker="SELECT count(*) FROM debit LEFT JOIN credit ON debit.debit_id=credit.debit_id "
                   + "WHERE debit.fco=? OR credit.fco=?";
           conn.pst=conn.conn.prepareStatement(checker);
           conn.pst.setString(1, fco);
           conn.pst.setString(2, fco);
           conn.rs=conn.pst.executeQuery();
           
           if(conn.rs.next()){
           if(conn.rs.getInt(1)>0){
            message="Failed to delete, Advances and Expenses have been associated with FCO "+fco+" "; 
            code=0;
           }
           else{
           //delete record
           String delete = "DELETE FROM fco WHERE fco=?";
           conn.pst1=conn.conn.prepareStatement(delete);
           conn.pst1.setString(1, fco);
           conn.pst1.executeUpdate();
           message="FCO "+fco+" deleted successfully.";
           code=1;
           }
           }
           
            JSONObject obj_final = new JSONObject();
            JSONObject obj = new JSONObject();
            
            obj.put("message", message);
            obj.put("code", code);
            
            obj_final.put("data", obj);
            out.println(obj_final);
            System.out.println(obj_final);
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
        Logger.getLogger(delete_fco.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(delete_fco.class.getName()).log(Level.SEVERE, null, ex);
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
