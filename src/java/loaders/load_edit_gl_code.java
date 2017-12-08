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
public class load_edit_gl_code extends HttpServlet {
   HttpSession session;
   String gl_code;
   String code,account,account_name,types;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           session = request.getSession();
            dbConn conn = new dbConn();
            
            gl_code = "523";
//            gl_code = request.getParameter("gl_code");
            code=account=account_name=types="";
            String get_gl_data="SELECT code,account,account_name,type_id FROM gl_code WHERE code=?";
            conn.pst = conn.conn.prepareStatement(get_gl_data);
            conn.pst.setString(1, gl_code);
            conn.rs = conn.pst.executeQuery();
            if(conn.rs.next()){
              // get types
               code = conn.rs.getString(1);
                 account = conn.rs.getString(2);
                 account_name = conn.rs.getString(3);
              String gettypes="SELECT id,name FROM gl_code_types ORDER BY name";
              conn.rs1=conn.st.executeQuery(gettypes);
              while(conn.rs1.next()){
                  if(conn.rs.getInt(4)==conn.rs1.getInt(1)){
                types+="<option value=\""+conn.rs1.getString(1)+"\" selected>"+conn.rs1.getString(2)+"</option>";
              }
                  else{
                 types+="<option value=\""+conn.rs1.getString(1)+"\">"+conn.rs1.getString(2)+"</option>";      
                  }
              }
            }
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("account", account);
            obj.put("account_name", account_name);
            obj.put("types", types);
            
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
           Logger.getLogger(load_edit_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(load_edit_gl_code.class.getName()).log(Level.SEVERE, null, ex);
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
