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
public class update_mou extends HttpServlet {
HttpSession session;
String id,mou_no,approved_budget;
String message;
int  code,found;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           id = request.getParameter("id");
           mou_no = request.getParameter("mou_no");
           approved_budget = request.getParameter("approved_budget");
           
           found = 0;
                   
           String fetch="SELECT * FROM mous WHERE id=?";
           conn.pst = conn.conn.prepareStatement(fetch);
           conn.pst.setString(1, id);
           conn.rs = conn.pst.executeQuery();
           if(conn.rs.next()){
               String checker="SELECT COUNT(id) FROM MOUS where mou_no=? AND start_date=? AND end_date=? AND id!=?";
               conn.pst1 = conn.conn.prepareStatement(checker);
               conn.pst1.setString(1, mou_no);
               conn.pst1.setString(2,  conn.rs.getString("start_date"));
               conn.pst1.setString(3,  conn.rs.getString("end_date"));
               conn.pst1.setString(4,  id);
               
               System.out.println("pst1"+conn.pst1);
               conn.rs1 = conn.pst1.executeQuery();
               if(conn.rs1.next()){
                 found+=conn.rs1.getInt(1);  
               }
           }
           if(found==0){
           String updator = "UPDATE mous SET mou_no=?,approved_budget=? WHERE id=?";
           conn.pst = conn.conn.prepareStatement(updator);
           conn.pst.setString(1, mou_no);
           conn.pst.setString(2, approved_budget);
           conn.pst.setString(3, id);
            System.out.println("qr:"+conn.pst);
           int num = conn.pst.executeUpdate();
           if(num>0){
                         message="MOU has been updated successfully."; 
                         code=1;
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
           }
           else{
           message="The MOU No has already been assigned to another MoU CHMT,SCHMT,Facility HMTs"; 
           code=0;      
           }
            
             JSONObject obj = new JSONObject();
                   obj.put("message", message);
                   obj.put("code", code);
                    
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
        Logger.getLogger(update_mou.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(update_mou.class.getName()).log(Level.SEVERE, null, ex);
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
