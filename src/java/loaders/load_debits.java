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
public class load_debits extends HttpServlet {
HttpSession session;
String staff_no;
String debit_id,cheque_no,fco,gl_code,amount,date,purpose,timestamp,status;
int    status_id;
int total_debit,total_credit,balance;
String account_advance,edit_advance,rebanking;
String currency;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           dbConn conn = new dbConn();
           session = request.getSession();
           
             total_debit=total_credit=balance=status_id=0;
            JSONObject obj_final = new JSONObject();
            JSONArray arr = new JSONArray();
            currency="";
            
            account_advance=edit_advance="0";
           String getcurrency="SELECT name FROM currency";
           conn.rs=conn.st.executeQuery(getcurrency);
           if(conn.rs.next()){
               currency = conn.rs.getString(1);
           }
            if(session.getAttribute("staff_no")!=null){
             staff_no = session.getAttribute("staff_no").toString();
            String getadvances = "SELECT debit_id,cheque_no,fco,gl_code,amount,date,purpose,timestamp FROM debit WHERE staff_no=? ORDER BY timestamp DESC";
            conn.pst=conn.conn.prepareStatement(getadvances);
            conn.pst.setString(1, staff_no);
            conn.rs=conn.pst.executeQuery();
            while(conn.rs.next()){
            //loop through adding the items to array
             status="";
             status_id=0;
             debit_id=cheque_no=fco=gl_code=amount=date=purpose=timestamp="";
             int pos=0;
                           
              debit_id = conn.rs.getString(1);
              cheque_no = conn.rs.getString(2);
              fco = conn.rs.getString(3);
              gl_code = conn.rs.getString(4);
              amount = conn.rs.getString(5);
              total_debit = conn.rs.getInt(5);
              date = conn.rs.getString(6);
              purpose = conn.rs.getString(7);
              timestamp = conn.rs.getString(8);
              
              if(!conn.rs.getString(7).equals("")){
                   pos++;
              purpose = pos+". "+conn.rs.getString(7)+"\n";   
              
              }
//              purpose="";
              JSONObject obj = new JSONObject();
              
                  String get_data = "SELECT id,code,CONCAT(description,others) AS description,amount,CONCAT(CHMT,SCHMT,facility_name) AS mou,  " +
                        "CONCAT(unique_code,sc_unique_code,fac_unique_code) AS unique_code,status FROM( " +
                        "SELECT advanced_activities.id AS id," +
                        "IFNULL(code,\"\") AS code," +
                        "IFNULL(description,\"\") AS description," +
                        "IFNULL(amount,\"\") AS amount, " +
                        "ifnull(others,\"\") AS others," +
                        "ifnull(CASE WHEN mous.type_id=1 THEN county.CHMT END,\"\") AS CHMT,  " +
                        "ifnull(CASE WHEN mous.type_id=1 THEN county.unique_code END,\"\") AS unique_code,   " +
                        "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.SCHMT END,\"\") AS SCHMT,  " +
                        "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.unique_code END,\"\") AS sc_unique_code, " +
                        "ifnull(CASE WHEN mous.type_id=3 THEN facilities.facility_name END,\"\") AS facility_name,  " +
                        "ifnull(CASE WHEN mous.type_id=3 THEN facilities.unique_code END,\"\") AS fac_unique_code,"+
                        "advanced_activities.status AS status   " +
                        "FROM advanced_activities  " +
                        "LEFT JOIN activities ON advanced_activities.activity_id  = activities.id " +
                        "LEFT JOIN mous ON activities.mou_id = mous.id  " +
                        "LEFT JOIN county ON mous.health_id=county.id  " +
                        "LEFT JOIN sub_county ON mous.health_id=sub_county.id  " +
                        "LEFT JOIN facilities ON mous.health_id=facilities.id  " +
                        "WHERE advanced_activities.debit_id=? ORDER BY advanced_activities.id) AS activiti_data";
                  
               conn.pst1 = conn.conn.prepareStatement(get_data);
               conn.pst1.setString(1, debit_id);
            
               conn.rs1 = conn.pst1.executeQuery();
               while(conn.rs1.next()){
                   pos++;
                purpose+=pos+"."+conn.rs1.getString("unique_code")+" "+conn.rs1.getString(3)+"<br>";   
               }
            //get paid
            String getpaid="SELECT SUM(amount) FROM credit WHERE debit_id='"+debit_id+"'";
            conn.rs1=conn.st1.executeQuery(getpaid);
            if(conn.rs1.next()){
             total_credit=conn.rs1.getInt(1);
            }
            
            balance = total_debit-total_credit;
            
            if(balance>0 && balance!=total_debit){
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px; class=\"label label-warning\">Accounted : "+currency+". "+total_credit+"<br>"
                        + "Not Accounted : "+currency+". "+balance+"</span> ";
                status_id=2;
            }
            else if(total_credit==0){
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px;  class=\"label label-danger\">None Accounted</span> ";
                status_id=3;
            }
            else{
                status="<span style=\"width:170px; height: 40px; text-align:center;\" font-size:10px; class=\"label label-success\">Fully Accounted</span> ";
                status_id=1;
            }
            account_advance=edit_advance="0";
            if(session.getAttribute("advance")!=null){
            edit_advance =  session.getAttribute("advance").toString();
            }
            if(session.getAttribute("expenses")!=null){
             account_advance=  session.getAttribute("expenses").toString();
            }
            
            if(session.getAttribute("rebanking")!=null){
             rebanking=  session.getAttribute("rebanking").toString();
            }
              obj.put("debit_id",debit_id );
              obj.put("cheque_no", cheque_no);
              obj.put("fco", fco);
              obj.put("gl_code", gl_code);
              obj.put("amount", amount);
              obj.put("date", date);
              obj.put("purpose", purpose);
              obj.put("timestamp", timestamp);
//              obj.put("facility_name", facility_name);
              obj.put("status", status);
              obj.put("status_id", status_id);
              obj.put("balance", balance);
              obj.put("currency", currency);
              
              obj.put("account_advance", account_advance);
              obj.put("edit_advance", edit_advance);
              obj.put("rebanking", rebanking);
              
              
            arr.add(obj);
            }
            }
            
            
            obj_final.put("data", arr);
            out.println(obj_final);
            
            System.out.println("advances object : "+obj_final);
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
        Logger.getLogger(load_debits.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_debits.class.getName()).log(Level.SEVERE, null, ex);
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
