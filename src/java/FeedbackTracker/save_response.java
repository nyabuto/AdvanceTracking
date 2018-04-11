/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeedbackTracker;

import Db.dbConn;
import advancetracking.Manager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
public class save_response extends HttpServlet {
HttpSession session;
String id,user_id,res,solvedon;
int status;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
            dbConn conn = new dbConn();
            Manager mg = new Manager();
            
            
            String today = mg.today();
            
            JSONObject obj = new JSONObject();
            
            JSONObject ob = new JSONObject();
            
            id = request.getParameter("id");
            res = request.getParameter("response");
            
            System.out.println("id is : "+id+" response is : "+res);
            status = 1;
            if(session.getAttribute("userid")!=null){
                user_id = session.getAttribute("userid").toString();
              
                if(session.getAttribute("is_dev")!=null){
                String dev = session.getAttribute("is_dev").toString();
               if(dev.equals("1")){
              String update_query = "UPDATE feedback SET response=?,status=?,solved_on=? WHERE id=?";
              conn.pst = conn.conn.prepareStatement(update_query);
              conn.pst.setString(1, res);
              conn.pst.setInt(2, status);
              conn.pst.setString(3, today);
              conn.pst.setString(4, id);
              int updated = conn.pst.executeUpdate();
              if(updated>0){
               ob.put("name", "Response saved successfully.");
                ob.put("code", 1);     
              }
              else{
              ob.put("name", "No changes detected.");
              ob.put("code", 0);      
              }
               }
               else{
                ob.put("name", "You are not allowed to resolve issues raised by users. Only developers can do.");
                ob.put("code", 0);   
               }
               
                }
                else{
                  ob.put("name", "We cant determine who you are.");
                  ob.put("code", 0);  
                }
            }
            else{
                ob.put("name", "Kindly login in and try to approve user feedback.");
                ob.put("code", 0);
            }
            
            obj.put("data", ob);
            
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
        Logger.getLogger(save_response.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_response.class.getName()).log(Level.SEVERE, null, ex);
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
