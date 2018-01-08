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
public class all_subcounties extends HttpServlet {
HttpSession session;
String id,county,sub_county,unique_code,is_active,SCHMT;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject final_obj = new JSONObject();
            JSONArray jarray = new JSONArray();
          String getSubcounties = "SELECT county.name AS county_name, sub_county.id AS sub_county_id,SCHMT, sub_county.unique_code AS unique_code, sub_county, sub_county.is_active "
                  + "FROM sub_county "
                  + "LEFT JOIN county ON sub_county.county_id=county.id "
                  + "ORDER BY county_name,sub_county ";
          conn.rs=conn.st.executeQuery(getSubcounties);
          while(conn.rs.next()){
             id = conn.rs.getString(2);
             county = conn.rs.getString(1);
             SCHMT = conn.rs.getString(3);
             sub_county = conn.rs.getString(5);
             unique_code = conn.rs.getString(4);
             is_active = conn.rs.getString(6);
            
             JSONObject obj = new JSONObject();
             obj.put("id", id);
             obj.put("county", county);
             obj.put("SCHMT", SCHMT);
             obj.put("sub_county", sub_county);
             obj.put("unique_code", unique_code);
             obj.put("status", is_active);
             
             jarray.add(obj);
          }
          
          final_obj.put("data", jarray);
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
        Logger.getLogger(all_subcounties.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(all_subcounties.class.getName()).log(Level.SEVERE, null, ex);
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
