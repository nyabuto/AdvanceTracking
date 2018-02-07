/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loaders;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
public class loadMOUs extends HttpServlet {
HttpSession session;
String id,mou_no,mou,unique_code,approved_budget,start_date,end_date;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           session = request.getSession();
            dbConn conn = new dbConn();
            
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
           String  getmous_county = "SELECT CHMT AS mou,unique_code AS unique_code,mous.id AS id,mou_no,approved_budget,start_date,end_date "
                   + "FROM mous "
                   + "LEFT JOIN county ON mous.health_id=county.id "
                   + "WHERE mous.type_id=1 && mous.is_active=1";
            
           String  getmous_sub_county = "SELECT SCHMT AS mou,unique_code AS unique_code,mous.id AS id,mou_no,approved_budget,start_date,end_date "
                   + "FROM mous "
                   + "LEFT JOIN sub_county ON mous.health_id=sub_county.id "
                   + "WHERE mous.type_id=2 && mous.is_active=1";
           
           String  getmous_facility = "SELECT facility_name AS mou,unique_code AS unique_code,mous.id AS id,mou_no,approved_budget,start_date,end_date "
                   + "FROM mous "
                   + "LEFT JOIN facilities ON mous.health_id=facilities.id "
                   + "WHERE mous.type_id=3 && mous.is_active=1";
            
           conn.rs = conn.st.executeQuery(getmous_county);
           jarray = get_data(conn.rs,jarray);
           
           conn.rs = conn.st.executeQuery(getmous_sub_county);
           jarray = get_data(conn.rs,jarray);
           
           conn.rs = conn.st.executeQuery(getmous_facility);
           jarray = get_data(conn.rs,jarray);
           
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
        Logger.getLogger(loadMOUs.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(loadMOUs.class.getName()).log(Level.SEVERE, null, ex);
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

    private JSONArray get_data(ResultSet rs, JSONArray jarray) throws SQLException{
        
        while(rs.next()){
            id = rs.getString(3);
            mou_no = rs.getString(4);
            mou = rs.getString(1);
            unique_code = rs.getString(2);
            approved_budget = rs.getString(5);
            start_date = rs.getString(6);
            end_date = rs.getString(7);
            
               JSONObject obj = new JSONObject();
               obj.put("id", id);
               obj.put("mou_no", mou_no);
               obj.put("mou", mou);
               obj.put("unique_code", unique_code);
               obj.put("approved_budget", approved_budget);
               obj.put("start_date", start_date);
               obj.put("end_date", end_date);
              jarray.add(obj);
           }
        
        return jarray;
    }
    
}
