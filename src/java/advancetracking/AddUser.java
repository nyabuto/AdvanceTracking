/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advancetracking;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;
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
public class AddUser extends HttpServlet {
HttpSession session;
String username,pass,pass2,password,fullname,email,phone,message;
MessageDigest m;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
        
            dbConn conn = new dbConn();
            session = request.getSession();
            
            message="";
            username = request.getParameter("username");
            pass = request.getParameter("pass");
            pass2 = request.getParameter("pass2");
            fullname = request.getParameter("fullname");
            email = request.getParameter("email");
            phone = request.getParameter("phone");
            
            if(pass.equals(pass2)){
            String checker="SELECT id FROM user WHERE username=? OR (phone=? AND phone!=?) or (email=? AND email!=?)";
            conn.pst=conn.conn.prepareStatement(checker);
            conn.pst.setString(1, username);
            conn.pst.setString(2, phone);
            conn.pst.setString(3, "");
            conn.pst.setString(4, email);
            conn.pst.setString(5, "");
            
            conn.rs=conn.pst.executeQuery();
            if(conn.rs.next()){
//                user already exist in the system
             message="<font color=\"red\">User details already exist.</font>";
            }
            else{
                Random rand = new Random();
                int id=rand.nextInt(8)+1;
                
       m = MessageDigest.getInstance("MD5");
       m.update(pass.getBytes(), 0, pass.length());
       password = new BigInteger(1, m.digest()).toString(16);

//                add as a new user
            String inserter="INSERT INTO user (id,username,password,fullname,phone,email) VALUES (?,?,?,?,?,?)";
            conn.pst=conn.conn.prepareStatement(inserter);
            conn.pst.setInt(1, id);
            conn.pst.setString(2, username);
            conn.pst.setString(3, password);
            conn.pst.setString(4, fullname);
            conn.pst.setString(5, phone);
            conn.pst.setString(6, email);
            
            conn.pst.executeUpdate();

           message="<font color=\"green\">User added successfully.</font>";
            }
            
            }
            else{
//                passwords do not match
            message="<font color=\"green\">Passwords do not match.</font>";
            }
            session.setAttribute("adduser",message);
            System.out.println("message: "+message);
            
        response.sendRedirect("AddUser.jsp");
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
        Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(AddUser.class.getName()).log(Level.SEVERE, null, ex);
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
