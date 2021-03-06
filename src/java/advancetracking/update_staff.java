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
public class update_staff extends HttpServlet {
HttpSession session;
String staff_name,email,phone,staff_no,is_finance;
String user_id,level;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
            JSONObject obj = new JSONObject();
            
            if(session.getAttribute("level")!=null){
                level=session.getAttribute("level").toString();
            }
           
            staff_name = request.getParameter("staff_name");
            email = request.getParameter("email");
            phone = request.getParameter("phone");
            staff_no = request.getParameter("staff_no");
            is_finance = request.getParameter("is_finance");
            
            if(level.equals("1")){
                
              String checker="SELECT staff_no FROM staff WHERE (fullname=? OR phone=?) AND staff_no!=?";
                conn.pst=conn.conn.prepareStatement(checker);
                conn.pst.setString(1, staff_name);
                conn.pst.setString(2, phone);
                conn.pst.setString(3, staff_no);
                conn.rs=conn.pst.executeQuery();
                if(conn.rs.next()){
                  obj.put("message", "Staff details [name or phone] already registered with another staff.");
                obj.put("code", 0);   
                }
                
                else{
                    String inserter="UPDATE staff SET fullname=?,email=?,phone=?,is_finance=? WHERE staff_no=?";
                    conn.pst=conn.conn.prepareStatement(inserter);
                    conn.pst.setString(1, staff_name);
                    conn.pst.setString(2, email);
                    conn.pst.setString(3, phone);
                    conn.pst.setString(4, is_finance);
                    conn.pst.setString(5, staff_no);
                    
                    conn.pst.executeUpdate();
                    
                     obj.put("message", "Staff details updated successfully.");
                     obj.put("code", 1);
                }
                
            }
            else{
                obj.put("message", "Error, You do not have enough priviledges to add staffs.");
                obj.put("code", 0);
            }
           
            System.out.println(obj);
            out.println(obj);
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
        Logger.getLogger(update_staff.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(update_staff.class.getName()).log(Level.SEVERE, null, ex);
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
