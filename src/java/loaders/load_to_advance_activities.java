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
public class load_to_advance_activities extends HttpServlet {
HttpSession session;
String id,code,description,amount,type_id,status,mou,unique_code;   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
            dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            type_id = request.getParameter("type_id");
            
            
    String get_data = "SELECT id,code,description,amount,advanced,CONCAT(CHMT,SCHMT,facility_name) AS mou, " +
                "CONCAT(unique_code,sc_unique_code,fac_unique_code) AS unique_code FROM(" +
                "SELECT activities.id AS id,code,description,amount,ifnull(advanced_activities.id,0) AS advanced," +
                "ifnull(CASE WHEN mous.type_id=1 THEN county.CHMT END,\"\") AS CHMT, " +
                "ifnull(CASE WHEN mous.type_id=1 THEN county.unique_code END,\"\") AS unique_code,  " +
                "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.SCHMT END,\"\") AS SCHMT, " +
                "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.unique_code END,\"\") AS sc_unique_code," +
                "ifnull(CASE WHEN mous.type_id=3 THEN facilities.facility_name END,\"\") AS facility_name, " +
                "ifnull(CASE WHEN mous.type_id=3 THEN facilities.unique_code END,\"\") AS fac_unique_code  " +
                "FROM activities " +
                "LEFT JOIN advanced_activities ON activities.id = advanced_activities.activity_id " +
                "LEFT JOIN mous ON activities.mou_id = mous.id " +
                "LEFT JOIN county ON mous.health_id=county.id " +
                "LEFT JOIN sub_county ON mous.health_id=sub_county.id " +
                "LEFT JOIN facilities ON mous.health_id=facilities.id " +
                "WHERE mous.is_active=? ORDER BY activities.id) AS activiti_data";
            conn.pst = conn.conn.prepareStatement(get_data);
            conn.pst.setString(1, "1");
            conn.rs = conn.pst.executeQuery();
            while(conn.rs.next()){
            id = conn.rs.getString(1);
            code = conn.rs.getString(2);
            description = conn.rs.getString(3);
            amount = conn.rs.getString(4);
            if(conn.rs.getInt(5)>0){
            status  = "1";
            }
            else{
            status="0" ;   
            }
            mou = conn.rs.getString(6);
            unique_code = conn.rs.getString(7);
            
                JSONObject obj = new JSONObject();
                obj.put("id", id);
                obj.put("code", code);
                obj.put("description", description);
                obj.put("amount", amount);
                obj.put("status", status);
                obj.put("mou", mou);
                obj.put("unique_code", unique_code);
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
        Logger.getLogger(load_to_advance_activities.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_to_advance_activities.class.getName()).log(Level.SEVERE, null, ex);
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
