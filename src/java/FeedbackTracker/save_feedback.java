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
public class save_feedback extends HttpServlet {
String description,user_id;
HttpSession session;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
            dbConn conn = new dbConn();
            JSONObject obj = new JSONObject();
            Manager mg = new Manager();
            
            
            String today = mg.today();
            
            
            description = request.getParameter("description");
            user_id="";
            
            JSONObject ob = new JSONObject();
            
            if(session.getAttribute("userid")!=null){
                user_id = session.getAttribute("userid").toString();
                
                if(session.getAttribute("is_dev")!=null){
                String dev = session.getAttribute("is_dev").toString();
               if(!dev.equals("1")){
                String checker = "SELECT id FROM feedback WHERE description=? AND user_id=?";
            conn.pst = conn.conn.prepareStatement(checker);
            conn.pst.setString(1, description);
            conn.pst.setString(2, user_id);
            
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
            ob.put("name", "Feedback already exist.");
            ob.put("code", 0);
            }
            else{
                String add_description = "INSERT INTO feedback (user_id,description,reported_on) VALUES (?,?,?)";
                conn.pst = conn.conn.prepareStatement(add_description);
                conn.pst.setString(1, user_id);
                conn.pst.setString(2, description);
                conn.pst.setString(3, today);
                
                conn.pst.executeUpdate();
            
                ob.put("name", "Feedback added successfully.");
                ob.put("code", 1);
                
                //send email to the reporter
                
                
                
                //end of sending email
            }}
               else{
               ob.put("name", "You are not allowed to add feedback.");
               ob.put("code", 0); 
               }
               
                }
                else{
                 ob.put("name", "We cannot determine who you are.");
                 ob.put("code", 0);    
                }
            }
            else{
              ob.put("name", "Unknown user. Feedback not added.");
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
        Logger.getLogger(save_feedback.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_feedback.class.getName()).log(Level.SEVERE, null, ex);
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
