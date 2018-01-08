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
public class load_sub_counties extends HttpServlet {
HttpSession session;
String sub_county_id,county_id,county_name,CHMT,county_unique_code,sub_county,SCHMT,sub_county_unique_code;
String where="";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            where="";
            if(request.getParameter("county_id")!=null){
                String county = request.getParameter("county_id");
            where=" AND county_id='"+county+"'";    
            }
            String getsubcounties = "SELECT sub_county.id AS sub_county_id,county_id,county.name AS county_name,CHMT,sub_county.unique_code AS county_unique_code,"
                    + "sub_county,SCHMT,sub_county.unique_code AS sub_county_unique_code FROM sub_county LEFT JOIN county ON sub_county.county_id=county.id "
                    + "WHERE is_active=1 "+where+"";
            conn.rs = conn.st.executeQuery(getsubcounties);
            while(conn.rs.next()){
             sub_county_id = conn.rs.getString(1);
             county_id = conn.rs.getString(2);
             county_name = conn.rs.getString(3);
             CHMT = conn.rs.getString(4);
             county_unique_code = conn.rs.getString(5);
             sub_county = conn.rs.getString(6);
             SCHMT = conn.rs.getString(7);
             sub_county_unique_code = conn.rs.getString(8);   
             
             JSONObject obj = new JSONObject();
             obj.put("sub_county_id", sub_county_id);
             obj.put("county_id", county_id);
             obj.put("county_name", county_name);
             obj.put("CHMT", CHMT);
             obj.put("county_unique_code", county_unique_code);
             obj.put("sub_county", sub_county);
             obj.put("SCHMT", SCHMT);
             obj.put("sub_county_unique_code", sub_county_unique_code);
             
             jarray.add(obj);
            }
            obj_final.put("data",jarray); 
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
        Logger.getLogger(load_sub_counties.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_sub_counties.class.getName()).log(Level.SEVERE, null, ex);
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
