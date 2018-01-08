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
public class update_sub_county extends HttpServlet {
    HttpSession session;
    String county_id,sc_name,schmt,unique_code,finance_name,id;
    String message;
    int code,is_active; 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
            
           id = request.getParameter("id");
           county_id = request.getParameter("county_id");
          sc_name = request.getParameter("sc_name");
          schmt = request.getParameter("schmt");
          unique_code = request.getParameter("unique_code");
          finance_name = request.getParameter("sc_name");

          String checker="SELECT id FROM sub_county WHERE (sub_county=? OR SCHMT=? OR unique_code=?) AND id!=?";
          conn.pst = conn.conn.prepareStatement(checker);
          conn.pst.setString(1, sc_name);
          conn.pst.setString(2, schmt);
          conn.pst.setString(3, unique_code);
          conn.pst.setString(4, id);
          
          conn.rs = conn.pst.executeQuery();
          if(conn.rs.next()){
              
          }
          else{
              //update 
              String updator = "UPDATE sub_county SET county_id=?, sub_county=?, SCHMT=?,unique_code=?, finance_name=? WHERE id=?";
              conn.pst=conn.conn.prepareStatement(updator);
              conn.pst.setString(1, county_id);
              conn.pst.setString(2, sc_name);
              conn.pst.setString(3, schmt);
              conn.pst.setString(4, unique_code);
              conn.pst.setString(5, finance_name);
              conn.pst.setString(6, id);
              
            int num = conn.pst.executeUpdate();
            if(num>0){
                         message="Sub County updated successfully."; 
                         code=1;
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
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
            Logger.getLogger(update_sub_county.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(update_sub_county.class.getName()).log(Level.SEVERE, null, ex);
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
