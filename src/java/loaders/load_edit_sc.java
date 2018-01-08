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
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class load_edit_sc extends HttpServlet {
HttpSession session;
String id,county,sc_name,SCHMT,unique_id;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
           id = request.getParameter("sc_id");
//           id="30";
            county=sc_name=SCHMT=unique_id="";
            String getSC="SELECT SCHMT,unique_code,sub_county,county_id FROM sub_county WHERE id=?";
            conn.pst = conn.conn.prepareStatement(getSC);
            conn.pst.setString(1, id);
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
             
                //get all counties
                String getCounties="SELECT id,name FROM county";
                conn.rs1=conn.st.executeQuery(getCounties);
                while(conn.rs1.next()){
                    if(conn.rs.getString("county_id").equals(conn.rs1.getString("id"))){
                    county+="<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(2)+"</option>";    
                    }
                    else{
                   county+="<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(2)+"</option>";         
                    }
                }
                
                sc_name = conn.rs.getString(3);
                SCHMT = conn.rs.getString(1);
                unique_id = conn.rs.getString(2);
                
                
            }
            JSONObject obj_final = new JSONObject();
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("sc_name", sc_name);
            obj.put("SCHMT", SCHMT);
            obj.put("unique_code", unique_id);
            obj.put("county", county);
           
            obj_final.put("data", obj);
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
        Logger.getLogger(load_edit_sc.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_edit_sc.class.getName()).log(Level.SEVERE, null, ex);
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
