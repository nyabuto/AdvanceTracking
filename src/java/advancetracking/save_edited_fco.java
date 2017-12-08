/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advancetracking;

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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class save_edited_fco extends HttpServlet {
HttpSession session;
String fco,fco_type;
int code;
String message;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
            if(session.getAttribute("fco")!=null){
            fco = session.getAttribute("fco").toString();
            
            fco_type = request.getParameter("fco_type");
            code=0;
            message="";
            String update="UPDATE fco SET type_id=? WHERE fco=?";
            conn.pst=conn.conn.prepareStatement(update);
            conn.pst.setString(1, fco_type);
            conn.pst.setString(2, fco);
            
           int rows = conn.pst.executeUpdate();
            if(rows>0){
              message = "FCO details updated successfully."; 
              code = 1;   
            }
            else{
               message = "No changes were detected."; 
              code = 0;     
            }
            }
            else{
            message = "No such FCO."; 
            code = 0;       
            }
            JSONObject obj_final = new JSONObject();
            JSONObject obj = new JSONObject();
            
            obj.put("message", message);
            obj.put("code", code);
            
            obj_final.put("data", obj);
            out.println(obj_final);
            System.out.println(obj_final);
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
        Logger.getLogger(save_edited_fco.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_edited_fco.class.getName()).log(Level.SEVERE, null, ex);
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

    private int getRowCount(ResultSet resultSet) {
    if (resultSet == null) {
        return 0;
    }
    try {
        resultSet.last();
        return resultSet.getRow();
    } catch (SQLException exp) {
        exp.printStackTrace();
    } finally {
        try {
            resultSet.beforeFirst();
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }
    return 0;
}
}
