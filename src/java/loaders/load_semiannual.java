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
import java.util.Calendar;
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
public class load_semiannual extends HttpServlet {
    HttpSession session;
    String name,passed_year;
    int year,id;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            session = request.getSession();
            dbConn conn = new dbConn();
            
            JSONObject obj_final = new JSONObject();
            JSONArray  array_final = new JSONArray();
            
            if(request.getParameter("year")!=null){
            passed_year=request.getParameter("year");
            year=Integer.parseInt(passed_year);
            }
            else{
                year = Calendar.getInstance().get(Calendar.YEAR);
            }
            System.out.println("year : "+year);
            String getSemiAnnuals = "SELECT id,name FROM semi_annual";
            conn.rs=conn.st.executeQuery(getSemiAnnuals);
            while(conn.rs.next()){
                id = conn.rs.getInt(1);
                name = conn.rs.getString(2);
                if(id==1){
                 name = name.replace(" -", " "+(year-1)+" - ")+" "+year;
                }
                else{
                name = name.replace(" -", " "+year+" - ")+" "+year;    
                }
                
             JSONObject obj = new JSONObject();
             obj.put("id", id);
             obj.put("name", name);
            array_final.add(obj);
            }
            
            obj_final.put("data", array_final);
            out.println(obj_final);
            System.out.println(obj_final);
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
            Logger.getLogger(load_semiannual.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(load_semiannual.class.getName()).log(Level.SEVERE, null, ex);
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
