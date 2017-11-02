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
public class staff_session extends HttpServlet {
HttpSession session;
String staff_no,src;
String staff_fullname,staff_email,staff_phone,staff_status;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        session = request.getSession();
        dbConn conn = new dbConn();
        if(request.getParameter("axn")!=null && request.getParameter("src")!=null){
         src=request.getParameter("src")+".jsp";
         staff_no = request.getParameter("axn");
         
         session.setAttribute("staff_no", staff_no);
         
         //load staff session
         String getStaff = "SELECT fullname,email,phone,status_id FROM staff WHERE staff_no=?";
         conn.pst = conn.conn.prepareStatement(getStaff);
         conn.pst.setString(1, staff_no);
         
         conn.rs = conn.pst.executeQuery();
         if(conn.rs.next()){
           staff_fullname = conn.rs.getString(1);
           staff_email = conn.rs.getString(2);
           staff_phone = conn.rs.getString(3);
           staff_status = conn.rs.getString(4);  
           
           session.setAttribute("staff_fullname", staff_fullname);
           session.setAttribute("staff_email", staff_email);
           session.setAttribute("staff_phone", staff_phone);
           session.setAttribute("staff_status", staff_status);
          
             System.out.println("staff status : "+staff_status);
         }
         
         
        }
        else{
        src="Staffs.jsp";   
        }
        
        
        response.sendRedirect(src);
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
        Logger.getLogger(staff_session.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(staff_session.class.getName()).log(Level.SEVERE, null, ex);
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
