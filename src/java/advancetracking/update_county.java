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
public class update_county extends HttpServlet {
HttpSession session;
String county_id,county_name,CHMT,unique_code;
String message;
int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            session = request.getSession();
            dbConn conn = new dbConn();
            
            county_id = request.getParameter("county_id");
            county_name = request.getParameter("county_name");
            CHMT = county_name+" CHMT";
            unique_code = request.getParameter("unique_code");
            
            String checker = "SELECT id FROM county WHERE (name LIKE ? OR unique_code LIKE ?) AND id!=?";
            conn.pst = conn. conn.prepareStatement(checker);
            conn.pst.setString(1, "%"+county_name+"%");
            conn.pst.setString(2, "%"+unique_code+"%");
            conn.pst.setString(3, county_id);
            System.out.println("query update county : "+conn.pst);
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
                message = "Details already exist with another entry.";
                code = 0;
            }
            else{
             String inserter = "UPDATE county SET name=?, CHMT=?, unique_code=? WHERE id=?";
             conn.pst = conn.conn.prepareStatement(inserter);
             conn.pst.setString(1, county_name);
             conn.pst.setString(2, CHMT);
             conn.pst.setString(3, unique_code);
             conn.pst.setString(4, county_id);
             
             int num = conn.pst.executeUpdate();
             if(num>0){
                         message="County details updated successfully."; 
                         code=1;
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
                    }
                   JSONObject obj = new JSONObject();
                   obj.put("message", message);
                   obj.put("code", code);
                   
                   JSONObject final_obj = new JSONObject();
                   final_obj.put("data", obj);
                    
            out.println(final_obj);
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
        Logger.getLogger(update_county.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(update_county.class.getName()).log(Level.SEVERE, null, ex);
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
