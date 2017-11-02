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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class all_users extends HttpServlet {
HttpSession session;
String id,username,fullname,phone,email,level,status,level_label,status_name;
int code;
String message;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          dbConn conn = new dbConn();
          session = request.getSession();
          
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            if(session.getAttribute("level")!=null){
                System.out.println("level : "+session.getAttribute("level").toString());
                if(session.getAttribute("level").toString().equals("1")){
            String getUsers="SELECT user.id AS id,username,fullname,phone,email,user_levels.name AS level_label,status.name AS status_name,status_id,level  FROM user LEFT JOIN user_levels ON user.level=user_levels.id LEFT JOIN status ON user.status_id=status.id ORDER BY fullname ASC";
            conn.rs=conn.st.executeQuery(getUsers);
            while(conn.rs.next()){
            id = conn.rs.getString(1);
            username = conn.rs.getString(2);
            fullname = conn.rs.getString(3);
            phone = conn.rs.getString(4);
            email = conn.rs.getString(5);
            level_label = conn.rs.getString(6);   
            status_name = conn.rs.getString(7);   
            status = conn.rs.getString(8);   
            level = conn.rs.getString(9);   
            JSONObject obj = new JSONObject();
            
            obj.put("id", id);
            obj.put("username", username);
            obj.put("fullname", fullname);
            obj.put("phone", phone);
            obj.put("email", email);
            obj.put("level_label", level_label);
            obj.put("status_label",status_name );
            obj.put("status",status );
            obj.put("level",level );
            
            jarray.add(obj);
            }
               code =1;   
               message="Users loaded successfully.";      
                    }
                
                else{
               code =0;   
               message="Not allowed to access this module.";
                }
                    }
            else{
            code =0;   
            message="Kindly login afresh to determine your access permissions.";     
            }
            
         obj_final.put("data", jarray);
         obj_final.put("code", code);
         obj_final.put("message", message);
           
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
        Logger.getLogger(all_users.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(all_users.class.getName()).log(Level.SEVERE, null, ex);
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
