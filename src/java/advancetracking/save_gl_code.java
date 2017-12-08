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
public class save_gl_code extends HttpServlet {

   HttpSession session;
   String gl_code,account,account_name,gl_code_type,message;
   int code;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            session = request.getSession();
            dbConn conn = new dbConn();
            
            gl_code = request.getParameter("code");
            account = request.getParameter("account");
            account_name = request.getParameter("account_name");
            gl_code_type = request.getParameter("gl_code_type");
            
            if(!gl_code.equals("") && !account.equals("") &&! account_name.equals("") &&! gl_code_type.equals("")){
                // check for existence
                
                String checker="SELECT code FROM gl_code WHERE code=? OR account=?";
                conn.pst = conn.conn.prepareStatement(checker);
                conn.pst.setString(1, gl_code);
                conn.pst.setString(2, account);
                conn.rs = conn.pst.executeQuery();
                if(conn.rs.next()){
                    message = "Similar details already exist.";
                    code=0;
                }
                else{
                    String inserter="INSERT INTO gl_code(code,account,account_name,type_id) VALUES(?,?,?,?)";
                    conn.pst=conn.conn.prepareStatement(inserter);
                    conn.pst.setString(1, gl_code);
                    conn.pst.setString(2, account);
                    conn.pst.setString(3, account_name);
                    conn.pst.setString(4, gl_code_type);
                    
                   int rows = conn.pst.executeUpdate();
                   if(rows>0){
                       message="GL Code added successfully.";
                       code=1;
                   }
                   else{
                     message = "No changes detected";
                     code=0;
                   }
                }
                
            }
            else{
                message="No data saved, enter all information.";
                code=0;
            }
            
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("message", message);
            
            JSONObject obj_final = new JSONObject();
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
           Logger.getLogger(save_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(save_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
