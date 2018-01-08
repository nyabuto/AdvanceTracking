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
public class save_facility extends HttpServlet {
HttpSession session;
String message;
int code;
String id,facility_name,finance_name,unique_code,sub_county_id,mfl_code,Type,latitude,longitude,ward,constituency,active;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
            dbConn conn = new dbConn();
            facility_name=finance_name=unique_code=sub_county_id=mfl_code=Type=latitude=longitude=ward=constituency=active="";
            
            facility_name=finance_name=request.getParameter("facility_name");
                    unique_code=request.getParameter("unique_code");
                    sub_county_id=request.getParameter("sub_county_id");
                    mfl_code=request.getParameter("mfl_code");
                    Type=latitude=longitude=ward=constituency="";
                    active="1";
           
                    String checker="SELECT id FROM facilities WHERE mfl_code=? OR facility_name=? OR unique_code=? OR (finance_name=? AND finance_name!=?)";
                    conn.pst = conn.conn.prepareStatement(checker);
                    conn.pst.setString(1, mfl_code);
                    conn.pst.setString(2, facility_name);
                    conn.pst.setString(3, unique_code);
                    conn.pst.setString(4, finance_name);
                    conn.pst.setString(5, "");
                    
                    conn.rs = conn.pst.executeQuery();
                    if(conn.rs.next()){
                    message="Similar Record exists";
                    code=0;
                    }
                    else{
                     String inserter="INSERT INTO facilities (facility_name,finance_name,unique_code,sub_county_id,mfl_code,Type,latitude,longitude,ward,constituency,active) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                       conn.pst = conn.conn.prepareStatement(inserter);
                       conn.pst.setString(1, facility_name);
                       conn.pst.setString(2, finance_name);
                       conn.pst.setString(3, unique_code);
                       conn.pst.setString(4, sub_county_id);
                       conn.pst.setString(5, mfl_code);
                       conn.pst.setString(6, Type);
                       conn.pst.setString(7, latitude);
                       conn.pst.setString(8, longitude);
                       conn.pst.setString(9, ward);
                       conn.pst.setString(10, constituency);
                       conn.pst.setString(11, active);
                       
                       int res = conn.pst.executeUpdate();
                       if(res>0){
                         message="Facility added successfully."; 
                         code=1;
                       }
                       else{
                         message="No change detected."; 
                         code=0;  
                       }
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
        Logger.getLogger(save_facility.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_facility.class.getName()).log(Level.SEVERE, null, ex);
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
