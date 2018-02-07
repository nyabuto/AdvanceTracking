/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uploads;

import Db.dbConn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class upload_activities extends HttpServlet {
    String output;
    HttpSession session;
    String full_path="";
    String fileName="";
    File file_source;
    private static final long serialVersionUID = 205242440643911308L;
    private static final String UPLOAD_DIR = "uploads";
    
    String activity_code,activity_description,amount;
    String id,type_id,health_id;
    String message,mou_id,userid;
    int counter,added,updated,missing,code;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           added=updated=missing=0;
           
           mou_id=userid="";
            if(session.getAttribute("mou_id")!=null){
                System.out.println("mou id upload file : "+session.getAttribute("mou_id").toString());
          mou_id = session.getAttribute("mou_id").toString();
            }
            if(session.getAttribute("userid")!=null){
          userid = session.getAttribute("userid").toString();
            }
           if(!mou_id.equals("")){
           String applicationPath = request.getServletContext().getRealPath("");
         String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
         session=request.getSession();
          File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
        
        for (Part part : request.getParts()) {
            if(!getFileName(part).equals("")){
           fileName = getFileName(part);
            part.write(uploadFilePath + File.separator + fileName);
            }
        }
        if(!fileName.endsWith(".xlsx")){
          message="Failed to load the excel file. Please choose a .xlsx excel file"; 
          code=0;
        }
        else{
          full_path=fileSaveDir.getAbsolutePath()+"\\"+fileName;
  
// GET DATA FROM THE EXCEL AND AND OUTPUT IT ON THE CONSOLE..................................
 
  FileInputStream fileInputStream = new FileInputStream(full_path);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        int j=0;
        int number_sheets = workbook.getNumberOfSheets();
        while(j<number_sheets){
        XSSFSheet worksheet;
        
        worksheet = workbook.getSheetAt(j);
        Iterator rowIterator = worksheet.iterator();

        int i=1,y=0;
        while(rowIterator.hasNext()){
        activity_code=activity_description=amount="";
        type_id=health_id="";
        XSSFRow rowi = worksheet.getRow(i);
        if( rowi==null){
         break;}
//        3_______________________Mou No__________________________
            XSSFCell cellserialno = rowi.getCell((short) 0);
            if(cellserialno==null){
                break;
            }
            
            if(cellserialno.getCellType()==0){
                //numeric
           activity_code =""+(int)cellserialno.getNumericCellValue();
            } 
            else if(cellserialno.getCellType()==1){
           activity_code =cellserialno.getStringCellValue();
            } 
            
        
//        4_______________________MOU__________________________
            XSSFCell cellBatch_No = rowi.getCell((short) 1);
            if(cellBatch_No.getCellType()==0){
                //numeric
           activity_description =""+(int)cellBatch_No.getNumericCellValue();
            } 
            else if(cellBatch_No.getCellType()==1){
           activity_description =cellBatch_No.getStringCellValue();
            } 
            
            
            
        
//        5_______________________UNIQUE CODE__________________________
            XSSFCell cellPatient_CCC_No = rowi.getCell((short) 2);
            if(cellPatient_CCC_No.getCellType()==0){
                //numeric
           amount = ""+(int)cellPatient_CCC_No.getNumericCellValue();
            } 
            else if(cellPatient_CCC_No.getCellType()==1){
           amount = cellPatient_CCC_No.getStringCellValue();
            } 

         String checker = "SELECT id FROM activities WHERE mou_id=? && code=?" ;
         conn.pst=conn.conn.prepareStatement(checker);
         conn.pst.setString(1, mou_id);
         conn.pst.setString(2, activity_code);
         
           
         conn.rs=conn.pst.executeQuery();
         if(conn.rs.next()){
            id=conn.rs.getString(1);
            
           //update the record
           String updater = "UPDATE activities SET code=?,description=?, amount=?,user_id=? WHERE id=?";
           
           conn.pst1=conn.conn.prepareStatement(updater);
           conn.pst1.setString(1, activity_code);
           conn.pst1.setString(2, activity_description);
           conn.pst1.setString(3, amount);
           conn.pst1.setString(4, userid);
           conn.pst1.setString(5, id);
           
           updated+=conn.pst1.executeUpdate();
           
             System.out.println("Updated Activity code : "+activity_code+"   activity description :"+activity_description+" mou id:"+mou_id );
              System.out.println(conn.pst1);
         }
         else{
          String inserter = "INSERT INTO activities (mou_id,code,description,amount,user_id,is_active) VALUES(?,?,?,?,?,?)"; 
           
           conn.pst1 = conn.conn.prepareStatement(inserter);
           conn.pst1.setString(1, mou_id);
           conn.pst1.setString(2, activity_code);
           conn.pst1.setString(3, activity_description);
           conn.pst1.setString(4, amount);
           conn.pst1.setString(5, userid);
           conn.pst1.setString(6, "1");
           
           added+=conn.pst1.executeUpdate();
           
           
         }
              
            i++;
        }
        
        j++;
        }
        
        message="Activities: Excel data has been uploaded and <b>"+added+"</b> New entries were loaded, <b>"+updated+"</b> records were updated and <b>"+missing+"</b> records were skipped." ;
        if(added>missing){
            code=1;
        }
        else{
            code=2;
        }
        }
           }
           else{
               message="Error while uploading. No Mou was found. Kindly select MOU again to load activities.";
               code=0;
           }
        JSONObject obj = new JSONObject();
        obj.put("message", message);
        obj.put("code", code);
        
            out.println(obj);
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
            Logger.getLogger(upload_activities.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(upload_activities.class.getName()).log(Level.SEVERE, null, ex);
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

    private String getFileName(Part part) {
            String file_name="";
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
      
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                file_name = token.substring(token.indexOf("=") + 2, token.length()-1);
              break;  
            }
            
        }
         System.out.println("content-disposition final : "+file_name);
        return file_name;
    }
         
}
