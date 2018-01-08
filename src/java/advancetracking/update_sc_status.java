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
public class update_sc_status extends HttpServlet {
   HttpSession session;
   String sc_id,status,message;
   int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
        session = request.getSession();
          dbConn conn = new dbConn();
          
          
          sc_id = request.getParameter("sc_id");
          status = request.getParameter("status");
          
          String updator = "UPDATE sub_county SET is_active=? WHERE id=?";
          conn.pst = conn.conn.prepareStatement(updator);
          conn.pst.setString(1, status);
          conn.pst.setString(2, sc_id);
          
         int res = conn.pst.executeUpdate();
            
            if(res>0){
                if(status.equals("1")){
                 message="Sub County activated successfully.";   
                }
                else{
                 message="Sub County de-activated successfully.";   
                }
              code=1;
            }
            else{
              message="No change detected."; 
              code=0;  
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
           Logger.getLogger(update_sc_status.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(update_sc_status.class.getName()).log(Level.SEVERE, null, ex);
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
