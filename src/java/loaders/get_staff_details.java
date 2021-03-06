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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class get_staff_details extends HttpServlet {
HttpSession session;
String fullname,email,phone,is_finance;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          session = request.getSession();
          dbConn conn = new dbConn();
            JSONObject obj = new JSONObject();
            
            String staff_no = request.getParameter("staff_no");
            
            is_finance="";
            fullname=email=phone="";
            String get_info = "SELECT fullname,IFNULL(email,''),phone,is_finance FROM staff WHERE staff_no=?";
            conn.pst = conn.conn.prepareStatement(get_info);
            conn.pst.setString(1, staff_no);
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
              fullname = conn.rs.getString(1);
              email = conn.rs.getString(2);
              phone = conn.rs.getString(3);
              if(conn.rs.getInt("is_finance")==1){
               is_finance="<option value=\"1\" selected>Yes</option>" ;  
               is_finance+="<option value=\"0\">No</option>" ;  
              }
              else{
               is_finance="<option value=\"1\">Yes</option>" ;  
               is_finance+="<option value=\"0\" selected>No</option>" ;   
              }
              
            }
            
            obj.put("fullname", fullname);
            obj.put("email", email);
            obj.put("phone", phone);
            obj.put("is_finance", is_finance);
            out.println(obj);
            System.out.println(obj);
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
        Logger.getLogger(get_staff_details.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(get_staff_details.class.getName()).log(Level.SEVERE, null, ex);
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
