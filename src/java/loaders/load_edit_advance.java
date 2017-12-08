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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class load_edit_advance extends HttpServlet {
HttpSession session;
String id,cheque_no,fco_id,gl_id,facility_id,fco,gl_code,amount,date,purpose;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            id=cheque_no=fco_id=gl_id=facility_id=fco=gl_code=amount=date=purpose="";
            id = request.getParameter("id");
            
//            id="20171002130534436";
            String getadvance = "SELECT cheque_no,fco,gl_code,amount,date,purpose FROM debit WHERE debit_id=?";
            conn.pst=conn.conn.prepareStatement(getadvance);
            conn.pst.setString(1, id);
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
            cheque_no = conn.rs.getString(1);
            fco_id = conn.rs.getString(2);
            gl_id = conn.rs.getString(3);
            amount = conn.rs.getString(4);
            date = conn.rs.getString(5);
            purpose = conn.rs.getString(6);
//            facility_id = conn.rs.getString(7);
            
            String getfco="SELECT fco FROM fco WHERE type_id=1 && status=1";
            conn.rs1 = conn.st1.executeQuery(getfco);
            while(conn.rs1.next()){
                if(conn.rs1.getString(1).equals(fco_id)){
             fco += "<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(1)+"</option>"; 
            }
                else{
             fco += "<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(1)+"</option>"; 
            }
            }
            
            String getglcode="SELECT code FROM gl_code WHERE type_id=1";
            conn.rs1 = conn.st1.executeQuery(getglcode);
            while(conn.rs1.next()){
             if(conn.rs1.getString(1).equals(gl_id)){
             gl_code += "<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(1)+"</option>"; 
                }
             else{
             gl_code += "<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(1)+"</option>";
             }
            }
            
//            String getfacilities="SELECT id,facility_name FROM facilities";
//            conn.rs1 = conn.st1.executeQuery(getfacilities);
//            while(conn.rs1.next()){
//              if(conn.rs1.getString(1).equals(facility_id)){   
//             facility += "<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(2)+"</option>";
//            }
//            else{
//             facility += "<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(2)+"</option>"; 
//            }
//            }
             
            JSONObject obj = new JSONObject();
            obj.put("cheque_no", cheque_no);
            obj.put("fco", fco);
            obj.put("gl_code", gl_code);
//            obj.put("facility", facility);
            obj.put("amount", amount);
            obj.put("date", date);
            obj.put("purpose", purpose);
            obj_final.put("data", obj);
            }
            
           
            
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
        Logger.getLogger(load_edit_advance.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_edit_advance.class.getName()).log(Level.SEVERE, null, ex);
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
