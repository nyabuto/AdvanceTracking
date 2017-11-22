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
String credit_id,amount, date, timestamp,fco,gl_code,health_type_id,health_id;
String currency,health,fcos,gl_codes;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           
            JSONObject obj_final = new JSONObject();
            JSONArray jarray = new JSONArray();
            
            debit_id = request.getParameter("id");
//            debit_id="20171120091710011";
//           fetch credits under that debit;
           
            currency="";
            
           String getcurrency="SELECT name FROM currency";
           conn.rs=conn.st.executeQuery(getcurrency);
           if(conn.rs.next()){
               currency = conn.rs.getString(1);
           }
           
           String fetchCredits="SELECT credit_id,amount,date,timestamp,fco,gl_code,health_type_id,health_id FROM credit WHERE debit_id=? ORDER BY timestamp DESC";
           conn.pst=conn.conn.prepareStatement(fetchCredits);
           conn.pst.setString(1, debit_id);
           conn.rs=conn.pst.executeQuery();
           while(conn.rs.next()){
               health=gl_codes=fcos="";
            credit_id = conn.rs.getString(1);
            amount = conn.rs.getString(2); 
            date = conn.rs.getString(3);
            timestamp = conn.rs.getString(4);
            fco = conn.rs.getString(5);
            gl_code = conn.rs.getString(6);
            health_type_id = conn.rs.getString(7);
            health_id = conn.rs.getString(8);
            
            
            String getfcos = "SELECT fco FROM fco WHERE type_id=2";
            conn.rs1=conn.st1.executeQuery(getfcos);
            while(conn.rs1.next()){
                if(conn.rs1.getString(1).equals(fco)){
                fcos+="<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(1)+"</option>";   
                }
                else{
                fcos+="<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(1)+"</option>";       
                }
            }
            
            String getglcodes = "SELECT code FROM gl_code WHERE type_id=2";
            conn.rs1=conn.st1.executeQuery(getglcodes);
            while(conn.rs1.next()){
                if(conn.rs1.getString(1).equals(gl_code)){
                gl_codes+="<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(1)+"</option>";   
                }
                else{
                gl_codes+="<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(1)+"</option>";       
                }
            }
            
            
//           if(health_type_id!=null && health_id!=null){
               
                int i;
                i=0;
                 if(!gl_code.equals("523")){
                    health="<option value=\"\" selected></option>";  
                }
                else{
               health="<option value=\"\" disabled>County HMTs</option>";
                    }
                String get_county="SELECT id,CHMT FROM county ORDER BY CHMT ASC";
                conn.rs1=conn.st1.executeQuery(get_county);
                while(conn.rs1.next()){
                    i++;
                 if(conn.rs1.getString(1).equals(health_id) && health_type_id.equals("1")){
                health+="<option value=\"1-"+conn.rs1.getString(1)+"\" selected>"+i+". "+conn.rs1.getString(2)+"</option>";   
                }
                else{
                health+="<option value=\"1-"+conn.rs1.getString(1)+"\">"+i+". "+conn.rs1.getString(2)+"</option>";       
                }   
                }
                 // for sub_counties
                 i=0;
                 health+="<option value=\"\" disabled>Sub-County HMTs</option>";
                String get_subcounty="SELECT id,SCHMT FROM sub_county  ORDER BY SCHMT ASC";
                conn.rs1=conn.st1.executeQuery(get_subcounty);
                while(conn.rs1.next()){
                    i++;
                 if(conn.rs1.getString(1).equals(health_id) && health_type_id.equals("2")){
                health+="<option value=\"2-"+conn.rs1.getString(1)+"\" selected>"+i+". "+conn.rs1.getString(2)+"</option>";   
                }
                else{
                health+="<option value=\"2-"+conn.rs1.getString(1)+"\">"+i+". "+conn.rs1.getString(2)+"</option>";       
                }   
                }    
                 // for health facilities
                 i=0;
                health+="<option value=\"\" disabled>Health Facilities</option>";    
                String get_facility="SELECT id,facility_name FROM facilities  ORDER BY facility_name ASC";
                conn.rs1=conn.st1.executeQuery(get_facility);
                while(conn.rs1.next()){
                    i++;
                 if(conn.rs1.getString(1).equals(health_id) && health_type_id.equals("3")){
                health+="<option value=\"3-"+conn.rs1.getString(1)+"\" selected>"+i+". "+conn.rs1.getString(2)+"</option>";   
                }
                else{
                health+="<option value=\"3-"+conn.rs1.getString(1)+"\">"+i+". "+conn.rs1.getString(2)+"</option>";       
                }
            }
            //end of htting facility details
//           }
            
            
            JSONObject obj = new JSONObject();
            obj.put("id", credit_id);
            obj.put("amount", amount);
            obj.put("date", date);
            obj.put("timestamp", timestamp);
            obj.put("fco", fcos);
            obj.put("gl_code", gl_codes);
            obj.put("health", health);
            
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
