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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class advance_credits extends HttpServlet {
HttpSession session;
String debit_id;
String credit_id,amount, date, timestamp;
String currency;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            debit_id = request.getParameter("id");
//            debit_id="1";
//           fetch credits under that debit;
           
            currency="";
            
           String getcurrency="SELECT name FROM currency";
           conn.rs=conn.st.executeQuery(getcurrency);
           if(conn.rs.next()){
               currency = conn.rs.getString(1);
           }
           
           String fetchCredits="SELECT credit_id,amount,date,timestamp FROM credit WHERE debit_id=? ORDER BY timestamp DESC";
           conn.pst=conn.conn.prepareStatement(fetchCredits);
           conn.pst.setString(1, debit_id);
           conn.rs=conn.pst.executeQuery();
           while(conn.rs.next()){
            credit_id = conn.rs.getString(1);
            amount = conn.rs.getString(2); 
            date = conn.rs.getString(3);
            timestamp = conn.rs.getString(4);
            
            JSONObject obj = new JSONObject();
            obj.put("id", credit_id);
            obj.put("amount", amount);
            obj.put("date", date);
            obj.put("timestamp", timestamp);
            obj.put("currency", currency);
            
            jarray.add(obj);
           }
           
           obj_final.put("data", jarray);
           System.out.println("data:"+obj_final);
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
        Logger.getLogger(advance_credits.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(advance_credits.class.getName()).log(Level.SEVERE, null, ex);
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
