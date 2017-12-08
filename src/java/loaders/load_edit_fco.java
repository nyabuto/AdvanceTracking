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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class load_edit_fco extends HttpServlet {
     String fco,fcos,types;
     HttpSession session;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          session = request.getSession();
          dbConn conn = new dbConn();
          
//          fco = "1";
          fco = request.getParameter("fco");
          session.setAttribute("fco", fco);
          types = "";
          String gettype="SELECT type_id FROM fco WHERE fco=?";
          conn.pst = conn.conn.prepareStatement(gettype);
          conn.pst.setString(1, fco);
          conn.rs=conn.pst.executeQuery();
          if(conn.rs.next()){
              // get all types
              String gettrtypes = "SELECT id,name FROM fco_types";
              conn.rs1=conn.st.executeQuery(gettrtypes);
              while(conn.rs1.next()){
               if(conn.rs.getInt(1)==conn.rs1.getInt(1)){
               types+="<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(2)+"</option>";   
               }  
               else{
              types+="<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(2)+"</option>";        
               }
              }
          }
          
        //add to json obj
            JSONObject obj_final = new JSONObject();
            JSONObject obj = new JSONObject();
            
            obj.put("fco", fco);
            obj.put("fco_type", types);
            
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
             Logger.getLogger(load_edit_fco.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(load_edit_fco.class.getName()).log(Level.SEVERE, null, ex);
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
