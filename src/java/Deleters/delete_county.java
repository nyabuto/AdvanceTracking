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
public class delete_county extends HttpServlet {
HttpSession session;
String county_id;
String message;
int code,count_found;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
          session = request.getSession();
          dbConn conn = new dbConn();
          
          
          county_id = request.getParameter("county_id");
          
          count_found = 0;
          String checker = "SELECT COUNT(id) FROM sub_county WHERE county_id=?";
          conn.pst = conn.conn.prepareStatement(checker);
          conn.pst.setString(1, county_id);
          conn.rs = conn.pst.executeQuery();
          if(conn.rs.next()){
          count_found+=conn.rs.getInt(1);
          }
          
          String checker2 = "SELECT COUNT(credit_id) FROM credit WHERE health_type_id=? AND health_id=?";
          conn.pst = conn.conn.prepareStatement(checker2);
          conn.pst.setString(1, "1");
          conn.pst.setString(2, county_id);
          conn.rs = conn.pst.executeQuery();
          if(conn.rs.next()){
           count_found+=conn.rs.getInt(1);   
          }
          
          if(count_found==0){
              //delete this county. it has no associativity.
              String deleter = "DELETE FROM county WHERE id=?";
              conn.pst = conn.conn.prepareStatement(deleter);
              conn.pst.setString(1, county_id);
              int num = conn.pst.executeUpdate();
              if(num>0){
                         message="County has been deleted successfully."; 
                         code=1;
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
                    
          }
          else{
              message = "County cannot be deleted. it has already been linked to districts or used in accounting.";
              code = 0;
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
        Logger.getLogger(delete_county.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(delete_county.class.getName()).log(Level.SEVERE, null, ex);
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
