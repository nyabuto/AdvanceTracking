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
public class load_advanced_activities extends HttpServlet {
HttpSession session;
String debit_id;
String id,code,description,amount,mou,unique_code,status;
String type_id,health_id,gl_code;
String where="",special="";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          dbConn conn = new dbConn();
           session = request.getSession();
            
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            debit_id = request.getParameter("debit_id");
            type_id= request.getParameter("type_id");
            health_id =request.getParameter("health_id");
            gl_code = request.getParameter("gl_code");
            where=special="";
            where="AND ((1=1 ";
            if(!type_id.equals("")){where += " AND type_id='"+type_id+"' ";}
            if(!health_id.equals("")){where += " AND health_id='"+health_id+"' ";}
            if(!gl_code.equals("523")){
                where+=" AND activity_id=0 )";
            }
            else if(gl_code.equals("523") && type_id.equals("4")){
                special+=" OR activity_id=0)";
            }
            else if(gl_code.equals("523") && !type_id.equals("4")){
                special+=")";
            }
            else{
                special+=")";
            } 
            
            if(gl_code.equals("608")){
             where="AND ((1=1 ";
             special=")";
            }
            System.out.println("gl_code : "+gl_code+"  type_id:"+type_id+" health_id : "+health_id);
            
    String get_data = "SELECT id,code,CONCAT(description,others) AS description,amount,CONCAT(CHMT,SCHMT,facility_name) AS mou,  " +
   "CONCAT(unique_code,sc_unique_code,fac_unique_code) AS unique_code,status FROM( " +
   "SELECT advanced_activities.id AS id," +
   "IFNULL(code,\"\") AS code," +
   "IFNULL(description,\"\") AS description," +
   "IFNULL(amount,\"\") AS amount, " +
   "ifnull(others,\"\") AS others," +
   "ifnull(CASE WHEN mous.type_id=1 THEN county.CHMT END,\"\") AS CHMT,  " +
   "ifnull(CASE WHEN mous.type_id=1 THEN county.unique_code END,\"\") AS unique_code,   " +
   "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.SCHMT END,\"\") AS SCHMT,  " +
   "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.unique_code END,\"\") AS sc_unique_code, " +
   "ifnull(CASE WHEN mous.type_id=3 THEN facilities.facility_name END,\"\") AS facility_name,  " +
   "ifnull(CASE WHEN mous.type_id=3 THEN facilities.unique_code END,\"\") AS fac_unique_code,"+
   "advanced_activities.status AS status   " +
   "FROM advanced_activities  " +
   "LEFT JOIN activities ON advanced_activities.activity_id  = activities.id " +
   "LEFT JOIN mous ON activities.mou_id = mous.id  " +
   "LEFT JOIN county ON mous.health_id=county.id  " +
   "LEFT JOIN sub_county ON mous.health_id=sub_county.id  " +
   "LEFT JOIN facilities ON mous.health_id=facilities.id  " +
   "WHERE advanced_activities.debit_id=? "+where+") "+special+" ORDER BY advanced_activities.id) AS activiti_data";
                             
    conn.pst = conn.conn.prepareStatement(get_data);
    conn.pst.setString(1, debit_id);
     System.out.println("loaded_act:"+conn.pst);
    conn.rs = conn.pst.executeQuery();
    
   
    while(conn.rs.next()){
    id = conn.rs.getString(1);
    code = conn.rs.getString(2);
    description = conn.rs.getString(3);
    amount = conn.rs.getString(4);
    mou = conn.rs.getString(5);
    unique_code = conn.rs.getString(6);
    status = conn.rs.getString(7);

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
        Logger.getLogger(load_advanced_activities.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_advanced_activities.class.getName()).log(Level.SEVERE, null, ex);
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
