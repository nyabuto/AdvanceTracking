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
public class update_gl_code extends HttpServlet {

    HttpSession session;
    String gl_code,account,account_name,gl_code_type,message;
    int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           gl_code = request.getParameter("code");
           account = request.getParameter("account");
           account_name = request.getParameter("account_name");
           gl_code_type = request.getParameter("gl_code_type");
           
           String checker="SELECT code FROM gl_code WHERE account=? && code!=?";
           conn.pst = conn.conn.prepareStatement(checker);
           conn.pst.setString(1, account);
           conn.pst.setString(2, gl_code);
           conn.rs=conn.pst.executeQuery();
           if(conn.rs.next()){
               message = "Details already associated with another GL Code";
               code = 1;
           }
           else{
               String updater = "UPDATE gl_code SET account=?, account_name=?, type_id=? WHERE code=?";
               conn.pst=conn.conn.prepareStatement(updater);
               conn.pst.setString(1, account);
               conn.pst.setString(2, account_name);
               conn.pst.setString(3, gl_code_type);
               conn.pst.setString(4, gl_code);
               
               int rows = conn.pst.executeUpdate();
               if(rows>0){
                       message="GL Code updated successfully.";
                       code=1;
                   }
                   else{
                     message = "No changes detected";
                     code=0;
                   }
           }
           
            JSONObject obj = new JSONObject();
            obj.put("message", message);
            obj.put("code", code);
            
            JSONObject obj_final = new JSONObject();
            obj_final.put("data", obj);
            
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
            Logger.getLogger(update_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(update_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
