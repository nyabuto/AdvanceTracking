/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FeedbackTracker;

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
public class loadfeedbacks extends HttpServlet {
HttpSession session;
String user_level,user_id;
String id,description,res,reported_on,solved_on,timestamp,fullname,phone,email,is_dev;
int status;
String turnaroundtime,duration_pending;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
            JSONObject data = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            user_level=user_id=is_dev="";
           
            if(session.getAttribute("level")!=null){
             user_level = session.getAttribute("level").toString();
            }
            if(session.getAttribute("userid")!=null){
             user_id = session.getAttribute("userid").toString();
            }
           
           String query="";
           
            if(session.getAttribute("is_dev")!=null){
                is_dev = session.getAttribute("is_dev").toString();
            }
                
        if(user_level.equals("1") || is_dev.equals("1")){
           query = "SELECT feedback.id AS id,IFNULL(description,'') AS description,IFNULL(response,'') AS response,status,IFNULL(reported_on,'N/A') AS reported_on,IFNULL(solved_on,'N/A') AS solved_on,feedback.timestamp AS timestamp,fullname,phone,email, IFNULL(DATEDIFF (solved_on,reported_on),'N/A') AS turnaroundtime, IFNULL(DATEDIFF (NOW(),reported_on),0) AS duration_pending FROM feedback LEFT JOIN user ON feedback.user_id=user.id ORDER BY feedback.timestamp ASC";
        }
        else if(user_level.equals("2")){
           query = "SELECT feedback.id AS id,IFNULL(description,'') AS description,IFNULL(response,'') AS response,status,IFNULL(reported_on,'N/A') AS reported_on,IFNULL(solved_on,'N/A') AS solved_on,feedback.timestamp AS timestamp,fullname,phone,email, IFNULL(DATEDIFF (solved_on,reported_on),'N/A') AS turnaroundtime, IFNULL(DATEDIFF (NOW(),reported_on),0) AS duration_pending FROM feedback LEFT JOIN user ON feedback.user_id=user.id WHERE user_id='"+user_id+"' ORDER BY feedback.timestamp ASC";
        }
            System.out.println("query: "+query);
        conn.rs = conn.st.executeQuery(query);
        while(conn.rs.next()){
          turnaroundtime=duration_pending="N/A";
            
          id = conn.rs.getString("id");
          description = conn.rs.getString("description");
          res = conn.rs.getString("response");
          reported_on = conn.rs.getString("reported_on");
          solved_on = conn.rs.getString("solved_on");
          timestamp = conn.rs.getString("timestamp");
          fullname = conn.rs.getString("fullname");
          phone = conn.rs.getString("phone");
          email = conn.rs.getString("email");
          status = conn.rs.getInt("status");
          turnaroundtime = conn.rs.getString("turnaroundtime");
          
          if(turnaroundtime.equals("N/A")){
          duration_pending = conn.rs.getString("duration_pending");
          if(duration_pending.equals("1")){
            duration_pending = duration_pending+" Day";    
          }
          else {
           duration_pending = duration_pending+" Days";     
          }
          }
          else{
              if(turnaroundtime.equals("1")){
                  turnaroundtime = turnaroundtime+" Day";  
              }
              else{
           turnaroundtime = turnaroundtime+" Days"; 
              }
          }
          
          JSONObject obj = new JSONObject();
          obj.put("id", id);
          obj.put("description", description);
          obj.put("response", res);
          obj.put("reported_on", reported_on);
          obj.put("solved_on", solved_on);
          obj.put("timestamp", timestamp);
          obj.put("fullname", fullname);
          obj.put("phone", phone);
          obj.put("email", email);
          obj.put("status", status);
          obj.put("is_dev", is_dev);
          obj.put("turnaroundtime", turnaroundtime);
          obj.put("duration_pending", duration_pending);
          
          jarray.add(obj);
        }
        
        data.put("data", jarray);
        out.println(data);
        
            System.out.println(data);
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
        Logger.getLogger(loadfeedbacks.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(loadfeedbacks.class.getName()).log(Level.SEVERE, null, ex);
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
