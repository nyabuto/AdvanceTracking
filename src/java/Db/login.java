/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class login extends HttpServlet {
HttpSession session;
String id,username,password,fullname,email,phone,pass,level,nextPage,message;
MessageDigest m;
int status;
int advance,expenses,approve_expenses,rebanking,is_dev;
String new_notifications;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        session=request.getSession();
          dbConn conn = new dbConn();
         conn.st.executeUpdate("Set GLOBAL  max_connections=6000");
          
          username=password=fullname=email=phone=message="";
          status=0;
          username=request.getParameter("username").trim();
          pass=request.getParameter("password").trim();
         
          
          new_notifications = ",";
          m = MessageDigest.getInstance("MD5");
       m.update(pass.getBytes(), 0, pass.length());
       password = new BigInteger(1, m.digest()).toString(16);
        System.out.println("username : "+username+" password : "+password);  
        String logger="SELECT id,fullname,phone,email,level,status_id,advance,expenses,approve_expenses,rebanking,is_dev FROM user WHERE username=? && password=? " ;
        conn.pst=conn.conn.prepareStatement(logger);
        conn.pst.setString(1, username);
        conn.pst.setString(2, password);
         conn.rs=conn.pst.executeQuery();
         if(conn.rs.next()==true){
             id=conn.rs.getString(1);
             fullname=conn.rs.getString(2);
             phone=conn.rs.getString(3);
             email=conn.rs.getString(4);
             level=conn.rs.getString(5);
             status = conn.rs.getInt(6);
             
             advance = conn.rs.getInt(7);
             expenses = conn.rs.getInt(8);
             approve_expenses = conn.rs.getInt(9);
             rebanking = conn.rs.getInt(10);
             is_dev = conn.rs.getInt(11);
             System.out.println("is dev : "+is_dev);
             if(status==1){
             session.setAttribute("userid", id);
             session.setAttribute("fullname", fullname);
             session.setAttribute("level", level);
             session.setAttribute("phone", phone);
             session.setAttribute("email", email);
             session.setAttribute("username", username);
             
             session.setAttribute("advance", advance);
             session.setAttribute("expenses", expenses);
             session.setAttribute("approve_expenses", approve_expenses);
             session.setAttribute("rebanking", rebanking);
             session.setAttribute("is_dev", is_dev);
              
          nextPage="Staffs.jsp";
          
          
          //get new updates on the system
          String getupdates = "SELECT DISTINCT(notifications.id) AS id FROM notifications LEFT JOIN viewed_notifications on notifications.id=viewed_notifications.notification_id WHERE notifications.id NOT IN(SELECT notifications.id AS id FROM notifications LEFT JOIN viewed_notifications on notifications.id=viewed_notifications.notification_id WHERE user_id=?)";
          conn.pst1 = conn.conn.prepareStatement(getupdates);
          conn.pst1.setString(1, id);
          conn.rs1 = conn.pst1.executeQuery();
          while(conn.rs1.next()){
            new_notifications+=conn.rs1.getString(1)+",";  
          }
                 System.out.println("query : "+conn.pst1);
          //end of getting new updates
          session.setAttribute("new_notifications", new_notifications);
                 System.out.println("new notifications:"+session.getAttribute("new_notifications"));
         }
             else{
             message="Your account is blocked. Contact System Administrator for help.";
             nextPage="../AdvanceTracking";
             session.setAttribute("login", message);   
             }
             
         }
         else{
             message="Wrong username and password combination.";
             nextPage="../AdvanceTracking";
             session.setAttribute("login", message);
         }
         
         if(conn.conn!=null){
 conn.conn.close();
         }
         
         if(conn.rs!=null){
         conn.rs.close();
         }
         
         if(conn.pst!=null){
         conn.pst.close();
         }
         
         response.sendRedirect(nextPage);
        
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
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
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
