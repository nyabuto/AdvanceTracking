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
public class upload_consolidated extends HttpServlet {
    String output;
    HttpSession session;
    String full_path="";
    String fileName="";
    File file_source;
    private static final long serialVersionUID = 205242440643911308L;
    private static final String UPLOAD_DIR = "uploads";
    
    String mou_no,mou,unique_code,approved_budget,start_date,end_date;
    String id,type_id,health_id;
    String message;
    int counter,added,updated,missing,code;
    String start_key,end_key;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
           added=updated=missing=0;
           start_date = request.getParameter("start_date");
           end_date = request.getParameter("end_date");
//           start_date = "2017-01-01";
//           end_date = "2018-01-01";

            System.out.println("start_date : "+start_date+" end date : "+end_date+" file name : "+fileName);
           
           start_key = start_date.replace("-", "");
           end_key = end_date.replace("-", "");
           
           
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
  System.out.println("start_date : "+start_date+" end date : "+end_date+" file name : "+full_path);
  
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
        mou_no=mou=unique_code=approved_budget="";
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
           mou_no =""+(int)cellserialno.getNumericCellValue();
            } 
            else if(cellserialno.getCellType()==1){
           mou_no =cellserialno.getStringCellValue();
            } 
            
        
//        4_______________________MOU__________________________
            XSSFCell cellBatch_No = rowi.getCell((short) 1);
            if(cellBatch_No.getCellType()==0){
                //numeric
           mou =""+(int)cellBatch_No.getNumericCellValue();
            } 
            else if(cellBatch_No.getCellType()==1){
           mou =cellBatch_No.getStringCellValue();
            } 
            
            
            
        
//        5_______________________UNIQUE CODE__________________________
            XSSFCell cellPatient_CCC_No = rowi.getCell((short) 2);
            if(cellPatient_CCC_No.getCellType()==0){
                //numeric
           unique_code = ""+(int)cellPatient_CCC_No.getNumericCellValue();
            } 
            else if(cellPatient_CCC_No.getCellType()==1){
           unique_code = cellPatient_CCC_No.getStringCellValue();
            } 
            
            
//        6_______________________APPROVED BUDGET__________________________
            XSSFCell cellTesting_Lab = rowi.getCell((short) 3);
            if(cellTesting_Lab.getCellType()==0){
                //numeric
           approved_budget =""+(int)cellTesting_Lab.getNumericCellValue();
            } 
            else if(cellTesting_Lab.getCellType()==1){
           approved_budget =cellTesting_Lab.getStringCellValue();
            } 
            
           JSONObject health_res = gethealthinfo(conn,mou,unique_code);
           if(health_res.containsKey("type_id") && health_res.containsKey("health_id")){
            type_id = health_res.get("type_id").toString();
            health_id = health_res.get("health_id").toString();
            //HMT found
           }
           else{
             
//               missing HMT
           }
         String checker = "SELECT id FROM mous WHERE start_date=? && end_date=? && health_id=? && type_id=?" ;
         conn.pst=conn.conn.prepareStatement(checker);
         conn.pst.setString(1, start_date);
         conn.pst.setString(2, end_date);
         conn.pst.setString(3, health_id);
         conn.pst.setString(4, type_id);
         
           
         conn.rs=conn.pst.executeQuery();
         if(conn.rs.next()){
            id=conn.rs.getString(1);
            
           //update the record
           String updater = "UPDATE mous SET mou_no=?,type_id=?, health_id=?,start_date=?, "
            + "end_date=?,start_key=?, end_key=?, approved_budget=? WHERE id=?";
           
           conn.pst1=conn.conn.prepareStatement(updater);
           conn.pst1.setString(1, mou_no);
           conn.pst1.setString(2, type_id);
           conn.pst1.setString(3, health_id);
           conn.pst1.setString(4, start_date);
           conn.pst1.setString(5, end_date);
           conn.pst1.setString(6, start_key);
           conn.pst1.setString(7, end_key);
           conn.pst1.setString(8, approved_budget);
           conn.pst1.setString(9, id);
           
           updated+=conn.pst1.executeUpdate();
           
             System.out.println("Updated Mou No : "+mou_no+"   approved budget :"+approved_budget+" mou:"+mou );
              System.out.println(conn.pst1);
         }
         else{
          String inserter = "INSERT INTO mous (mou_no,type_id,health_id,approved_budget,start_date,end_date,start_key,end_key,is_active) VALUES(?,?,?,?,?,?,?,?,?)"; 
           
           conn.pst1 = conn.conn.prepareStatement(inserter);
           conn.pst1.setString(1, mou_no);
           conn.pst1.setString(2, type_id);
           conn.pst1.setString(3, health_id);
           conn.pst1.setString(4, approved_budget);
           conn.pst1.setString(5, start_date);
           conn.pst1.setString(6, end_date);
           conn.pst1.setString(7, start_key);
           conn.pst1.setString(8, end_key);
           conn.pst1.setString(9, "1");
           
           added+=conn.pst1.executeUpdate();
           
           
         }
              
            i++;
        }
        
        j++;
        }
        
        message="Excel data has been uploaded and <b>"+added+"</b> New entries were loaded, <b>"+updated+"</b> records were updated and <b>"+missing+"</b> records were skipped." ;
        if(added>missing){
            code=1;
        }
        else{
            code=2;
        }
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
            Logger.getLogger(upload_consolidated.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(upload_consolidated.class.getName()).log(Level.SEVERE, null, ex);
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
        
    private JSONObject gethealthinfo(dbConn conn,String mou, String unique_code) throws SQLException{
     JSONObject obj = new JSONObject();
     String getcounty="SELECT id FROM county WHERE CHMT=? OR unique_code=?";
     obj = getonehealthinfo(conn,"1",getcounty,mou,unique_code);
     
     if(!obj.containsKey("type_id")){
     String getsubcounty="SELECT id FROM sub_county WHERE SCHMT=? OR unique_code=?";
     obj = getonehealthinfo(conn,"2",getsubcounty,mou,unique_code);
     }
     if(!obj.containsKey("type_id")){
     String getfacility="SELECT id FROM facilities WHERE facility_name=? OR unique_code=?";
     obj = getonehealthinfo(conn,"3",getfacility,mou,unique_code);
     }
     return obj;
    }    
    
public JSONObject getonehealthinfo(dbConn conn,String type_id,String query,String mou, String unique_code) throws SQLException{
  JSONObject obj = new JSONObject();
   conn.pst = conn.conn.prepareStatement(query);
     conn.pst.setString(1, mou);
     conn.pst.setString(2, unique_code);
     
     conn.rs = conn.pst.executeQuery();
     if(conn.rs.next()){
         obj.put("type_id", type_id);
         obj.put("health_id", conn.rs.getString(1));
     } 
 
  return obj;
}   
}
