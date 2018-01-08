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
public class all_glcodes extends HttpServlet {
HttpSession session;
String code,account,account_name,status,gl_type;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            String getglcodes = "SELECT code,account,account_name,status,gl_code_types.name AS gl_type FROM gl_code LEFT JOIN gl_code_types ON gl_code.type_id=gl_code_types.id ORDER by code ASC";
            conn.rs=conn.st.executeQuery(getglcodes);
            while(conn.rs.next()){
           code = conn.rs.getString(1);
           account = conn.rs.getString(2);
           account_name = conn.rs.getString(3);
           status = conn.rs.getString(4);
           gl_type = conn.rs.getString(5);
           
           JSONObject obj = new JSONObject();
           obj.put("code", code);
           obj.put("account", account);
           obj.put("account_name", account_name);
           obj.put("status", status);
           obj.put("gl_type", gl_type);
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
        Logger.getLogger(all_glcodes.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(all_glcodes.class.getName()).log(Level.SEVERE, null, ex);
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
