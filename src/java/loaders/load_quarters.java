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
public class load_quarters extends HttpServlet {
    HttpSession session;
    String name,passed_year;
    int id,year;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray json_array = new JSONArray();
            
            if(request.getParameter("year")!=null){
            passed_year=request.getParameter("year");
            year=Integer.parseInt(passed_year);
            }
            else{
               year = Calendar.getInstance().get(Calendar.YEAR);
            }
            
           String getQuarters = "SELECT id,name FROM quarters";
           conn.rs=conn.st.executeQuery(getQuarters);
           while(conn.rs.next()){
               id = conn.rs.getInt(1);
               name = conn.rs.getString(2);
              if(id==1){
                 name = name.replace(" -", " "+(year-1)+" - ")+" "+(year-1);
                }
                else{
                name = name.replace(" -", " "+year+" - ")+" "+year;    
                }
              JSONObject jobj = new JSONObject();
              jobj.put("id", id);
              jobj.put("name", name);
              
              json_array.add(jobj);
           }
            
          obj_final.put("data", json_array);
            
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
            Logger.getLogger(load_quarters.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(load_quarters.class.getName()).log(Level.SEVERE, null, ex);
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
