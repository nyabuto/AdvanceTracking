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
public class save_fco extends HttpServlet {
    HttpSession session;
    String message, fco, fco_types;
    int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           fco = request.getParameter("fco");
           fco_types = request.getParameter("fco_type");
           
           if(!fco.equals("") && !fco_types.equals("")){
           //check existence 
           String checker = "SELECT fco FROM fco WHERE fco=?";
           conn.pst = conn.conn.prepareStatement(checker);
           conn.pst.setString(1, fco);
           conn.rs = conn.pst.executeQuery();
           if(conn.rs.next()){
            message = "Similar record exist.";
            code = 0;   
           }
           else{
               String inserter = "INSERT INTO fco (fco,type_id) VALUES (?,?)";
               conn.pst = conn.conn.prepareStatement(inserter);
               conn.pst.setString(1, fco);
               conn.pst.setString(2, fco_types);
               conn.pst.executeUpdate();
               
               message = "FCO added successfully.";
               code = 1;
           }
           }
           else{
             message = "Enter FCO and FCO Type.";
             code = 0;  
           }
            JSONObject obj = new JSONObject();
            obj.put("message", message);
            obj.put("code", code);
            
            JSONObject obj_final = new JSONObject();
            obj_final.put("data",obj);
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
            Logger.getLogger(save_fco.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(save_fco.class.getName()).log(Level.SEVERE, null, ex);
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
