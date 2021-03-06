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

/**
 *
 * @author GNyabuto
 */
public class load_glcodes extends HttpServlet {

   HttpSession session;
   String output,type_id,rebanking,expenses;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            output=rebanking=expenses="";
           dbConn conn = new dbConn();
           session = request.getSession();
           
           type_id = request.getParameter("type_id");
           String get_glcodes="";
           
           if(session.getAttribute("rebanking")!=null){
             rebanking=session.getAttribute("rebanking").toString();
           }
           if(session.getAttribute("expenses")!=null){
             expenses=session.getAttribute("expenses").toString();
           }
           
           if(type_id.equals("1")){
           get_glcodes="SELECT code FROM gl_code WHERE type_id='"+type_id+"' AND status=1";
           }
           
           else{
              if(expenses.equals("1") && rebanking.equals("1")){
                get_glcodes="SELECT code FROM gl_code WHERE type_id='"+type_id+"' AND status=1";  
              }
              else if(expenses.equals("1") && !rebanking.equals("1")){
                  get_glcodes="SELECT code FROM gl_code WHERE type_id='"+type_id+"' AND code!=608 AND status=1";
              }
              else if(!expenses.equals("1") && rebanking.equals("1")){
                  get_glcodes="SELECT code FROM gl_code WHERE type_id='"+type_id+"' AND code=608 AND status=1";
              }
           }
           
           conn.rs=conn.st.executeQuery(get_glcodes);
           while(conn.rs.next()){
               output+="<option value=\""+conn.rs.getString(1)+"\">"+conn.rs.getString(1)+"</option>";
           }
            out.println(output);
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
           Logger.getLogger(load_glcodes.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(load_glcodes.class.getName()).log(Level.SEVERE, null, ex);
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
