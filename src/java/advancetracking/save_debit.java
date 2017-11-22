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
public class save_debit extends HttpServlet {
HttpSession session;
String debit_id,staff_no,cheque_no,fco,gl_code,amount,date,purpose;
String message;
int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            dbConn conn = new dbConn();
            session = request.getSession();
            
            JSONObject obj = new JSONObject();
            JSONObject obj_final = new JSONObject();
            Manager manager = new Manager();
            
            if(session.getAttribute("staff_no")!=null){
            staff_no = session.getAttribute("staff_no").toString();
                System.out.println("staff no : "+staff_no+" staff status : "+session.getAttribute("staff_status"));
            if(session.getAttribute("staff_status").toString().equals("1")){
                System.out.println("Entered here---staff session is 1");
            cheque_no = request.getParameter("cheque_no");
            fco = request.getParameter("fco");
            gl_code = request.getParameter("gl_code");
            amount = request.getParameter("amount");
            date = request.getParameter("date");
            purpose = request.getParameter("purpose");
//            facility_id = request.getParameter("facility");
            debit_id = manager.getdatekey();
            
            String checkExistence = "SELECT debit_id FROM debit WHERE cheque_no=? AND staff_no=? AND amount=? AND gl_code=? AND fco=?";
            conn.pst=conn.conn.prepareStatement(checkExistence);
            conn.pst.setString(1, cheque_no);
            conn.pst.setString(2, staff_no);
            conn.pst.setString(3, amount);
            conn.pst.setString(4, gl_code);
            conn.pst.setString(5, fco);
            
            conn.rs=conn.pst.executeQuery();
            if(conn.rs.next()){
                message = "Advance already captured in the system.";
                code=0;
            }
            else{
              //add advance
              
              String inserter = "INSERT INTO debit (debit_id,staff_no,cheque_no,fco,gl_code,amount,date,purpose) VALUES(?,?,?,?,?,?,?,?)";
              conn.pst=conn.conn.prepareStatement(inserter);
              conn.pst.setString(1, debit_id);
              conn.pst.setString(2, staff_no);
              conn.pst.setString(3, cheque_no);
              conn.pst.setString(4, fco);
              conn.pst.setString(5, gl_code);
              conn.pst.setString(6, amount);
              conn.pst.setString(7, date);
              conn.pst.setString(8, purpose);
//              conn.pst.setString(9, facility_id);
              
              conn.pst.executeUpdate();
              message = "Advance saved successfully.";
              code=1;   
            }
            }
            else {
             message = "Staffs whose accounts are disabled cannot be given any advance.";
             code=0;       
            }
            }
            else{
              message = "We cannot determine the staff who is given this advance, log out and login in back.";
              code=0;   
            }
            obj.put("message", message);
            obj.put("code", code);
            
            obj_final.put("data", obj);
            System.out.println("save_debit:"+obj_final);
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
        Logger.getLogger(save_debit.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(save_debit.class.getName()).log(Level.SEVERE, null, ex);
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
