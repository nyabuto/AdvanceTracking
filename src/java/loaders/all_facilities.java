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
public class all_facilities extends HttpServlet {

HttpSession session;
String facility_id,unique_code,facility_name,county,sub_county,mfl_code,status;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          session = request.getSession();
          dbConn conn = new dbConn();
          
        JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            
           String getFacilities = "SELECT facilities.id AS facility_id,facilities.unique_code AS unique_code,facility_name,sub_county.sub_county as sub_county, county.name AS county,"
                   + "mfl_code,active AS status "
                   + "FROM facilities "
                   + "LEFT JOIN sub_county ON facilities.sub_county_id=sub_county.id "
                   + "LEFT JOIN county ON sub_county.county_id=county.id ORDER BY active DESC,county ASC,sub_county ASC, facility_name ASC";
           conn.rs = conn.st.executeQuery(getFacilities);
           while(conn.rs.next()){
            
               facility_id = conn.rs.getString(1);
               unique_code = conn.rs.getString(2);
               facility_name = conn.rs.getString(3);
               sub_county = conn.rs.getString(4);
               county = conn.rs.getString(5);
               mfl_code = conn.rs.getString(6);
               status = conn.rs.getString(7);
               
               JSONObject obj = new JSONObject();
               obj.put("facility_id", facility_id);
               obj.put("unique_code", unique_code);
               obj.put("facility_name", facility_name);
               obj.put("sub_county", sub_county);
               obj.put("county", county);
               obj.put("mfl_code", mfl_code);
               obj.put("status", status);
               
               jarray.add(obj);
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
        Logger.getLogger(all_facilities.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(all_facilities.class.getName()).log(Level.SEVERE, null, ex);
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
