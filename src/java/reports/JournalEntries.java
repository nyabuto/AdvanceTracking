/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import Db.dbConn;
import advancetracking.Manager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author GNyabuto
 */
public class JournalEntries extends HttpServlet {
HttpSession session;
String debit_id,staffname,cheque_no,fco,id_code,gl_code,gl_account,gl_account_name,id_code_speed_key_reqd,external_doc_no,award_reqd,
        restriction_reqd,id_code_reqd,sub_award,debit,credit,posting_description,je_description;
String debit_date,facility_id,cleared;
int row_pos=0;
String report_id;
String date_between;
String passed_year;
int year;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        dbConn conn = new dbConn();
        session = request.getSession();
        Manager manager = new Manager();
       
        String headers[] = {"FCO","ID CODE","GL Acct/Det Code (Old or New)","GL Acct/Detail Code (New)","GL Account Name","ID Code/Speedkey (Reqd)","External Doc. No.","Award (Reqd)","Restriction (Reqd)","ID Code (Reqd)","Subaward (Option.)","Debit","Credit","Posting Description","JE Description (Optional Not Posted)"};
        
        row_pos=0;
       date_between=" WHERE "; 
      report_id= request.getParameter("reports");
      if(report_id.equals("5")){
          String start_date=request.getParameter("start_date");
          String end_date=request.getParameter("end_date");
          date_between +=" debit.date BETWEEN '"+start_date+"' AND '"+end_date+"' ";
      }
      else{
          passed_year=request.getParameter("years");
          year=Integer.parseInt(passed_year);
            switch (report_id) {
                case "1":
                    date_between +=" debit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-09-31' ";
                    break;
//        end of period
                case "2":
                    String semiannual[] = request.getParameterValues("semi_annual");
                    for (String sannual : semiannual){
                        if(sannual.equals("1")){
                            date_between +=" debit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-03-31' ";
                        }
                        else if(sannual.equals("2")){
                            date_between +=" OR debit.date BETWEEN '"+year+"-04-01' AND '"+year+"-09-31' ";
                        }
                    }          break;
                case "3":
                    String quarters[] = request.getParameterValues("quarters");
                    String qtrs[] ={(year-1)+"-10-01",(year-1)+"-12-31",year+"01-01",year+"03-31",year+"04-01",year+"06-31",year+"07-01",year+"09-31",};
                    for (String quarter : quarters){
                        if(!quarter.equals("")){
                            int qr=Integer.parseInt(quarter);
                            date_between +=" debit.date BETWEEN '"+qtrs[(qr*2)-2]+"' AND '"+qtrs[(qr*2)-1]+"' OR ";
                        }
                    }          date_between=removeLastChars(date_between, 3);
                    break;
                case "4":
                    String months[] = request.getParameterValues("months");
                    for (String month : months){
                        if(!month.equals("")){
                            int mn=Integer.parseInt(month);
                            if(mn<10){
                                date_between +=" debit.date BETWEEN '"+year+"-0"+month+"-01' AND '"+year+"-0"+month+"-31' OR ";
                            }
                            else{
                                date_between +=" debit.date BETWEEN '"+(year-1)+"-"+month+"-01' AND '"+(year-1)+"-"+month+"-31' OR ";
                            }
                            
                        }
                    }          date_between=removeLastChars(date_between, 3);
                    break;
                default:
                    break;
            }
      }  
        
        
        
        
    XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet1=wb.createSheet("Comprehensive Report");
    XSSFFont font=wb.createFont();
    font.setFontHeightInPoints((short)18);
    font.setFontName("Cambria");
    font.setColor((short)0000);
    
    CellStyle style=wb.createCellStyle();
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFCellStyle styleHeader = wb.createCellStyle();
    styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
    styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleHeader.setBorderTop(BorderStyle.THIN);
    styleHeader.setBorderBottom(BorderStyle.THIN);
    styleHeader.setBorderLeft(BorderStyle.THIN);
    styleHeader.setBorderRight(BorderStyle.THIN);
    styleHeader.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFFont fontHeader = wb.createFont();
    fontHeader.setColor(HSSFColor.BLACK.index);
    fontHeader.setBold(true);
    fontHeader.setFamily(FontFamily.MODERN);
    fontHeader.setFontName("Cambria");
    fontHeader.setFontHeight(15);
    styleHeader.setFont(fontHeader);
    styleHeader.setWrapText(true);
    
    XSSFCellStyle stylex = wb.createCellStyle();
    stylex.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    stylex.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    stylex.setBorderTop(BorderStyle.THIN);
    stylex.setBorderBottom(BorderStyle.THIN);
    stylex.setBorderLeft(BorderStyle.THIN);
    stylex.setBorderRight(BorderStyle.THIN);
    stylex.setAlignment(HorizontalAlignment.LEFT);
    
    XSSFFont fontx = wb.createFont();
    fontx.setColor(HSSFColor.BLACK.index);
    fontx.setBold(true);
    fontx.setFamily(FontFamily.MODERN);
    fontx.setFontName("Cambria");
    stylex.setFont(fontx);
    stylex.setWrapText(true);
    
    XSSFCellStyle stborder = wb.createCellStyle();
    stborder.setBorderTop(BorderStyle.THIN);
    stborder.setBorderBottom(BorderStyle.THIN);
    stborder.setBorderLeft(BorderStyle.THIN);
    stborder.setBorderRight(BorderStyle.THIN);
    stborder.setAlignment(HorizontalAlignment.LEFT);
    
    XSSFFont font_cell=wb.createFont();
    font_cell.setColor(HSSFColor.BLACK.index);
    font_cell.setFamily(FontFamily.MODERN);
    font_cell.setFontName("Cambria");
    stborder.setFont(font_cell);
    stborder.setWrapText(true);
    
    

    for (int i=0;i<=15;i++){
   shet1.setColumnWidth(i, 3000);
   if(i==4){
  shet1.setColumnWidth(i, 8000);     
   }
   if(i==13){
  shet1.setColumnWidth(i, 15000);     
   }
  }
    
     //Create headers

     XSSFRow rw1=shet1.createRow(row_pos);  
     
     int cell_pos=0;
        for (String header_name : headers) {
            // headers
        XSSFCell  S1cell=rw1.createCell(cell_pos);
        S1cell.setCellValue(header_name);
        S1cell.setCellStyle(stylex); 
        cell_pos++;
        }
        
     //        end of currency
        row_pos++;
        int num=0;
        String getReport="SELECT debit.debit_id AS debit_id,staff.fullname AS staff_name,cheque_no,fco,code,account,account_name,debit.amount AS debit_amount," +
                "debit.date AS debit_date,purpose,facility_id,IFNULL(SUM(credit.amount),0) AS credit_amount,IF(debit.amount-IFNULL(SUM(credit.amount),0)=0,'Yes','No') AS cleared " +
                "FROM debit " +
                "LEFT JOIN gl_code ON debit.gl_code=gl_code.code " +
                "LEFT JOIN credit ON debit.debit_id=credit.debit_id " +
                "LEFT JOIN staff ON debit.staff_no=staff.staff_no " +
                ""+date_between+"" +
                "GROUP BY debit.debit_id " +
                "ORDER BY debit.date ";
        System.out.println(getReport);
        conn.rs = conn.st.executeQuery(getReport);
        
        while(conn.rs.next()){
         num++;
         fco=id_code=gl_code=gl_account=gl_account_name=id_code_speed_key_reqd=external_doc_no=award_reqd=restriction_reqd=id_code_reqd=sub_award=debit=credit=posting_description=je_description;
         XSSFRow rw2=shet1.createRow(row_pos); 
         debit_id = conn.rs.getString(1);
         staffname = conn.rs.getString(2);
         cheque_no = conn.rs.getString(3);
         fco = conn.rs.getString(4);
         id_code = conn.rs.getString(5);
         gl_account = conn.rs.getString(6);
         gl_account_name = conn.rs.getString(7);
         debit = conn.rs.getString(8);
         debit_date = conn.rs.getString(9);
         posting_description = conn.rs.getString(10);
         facility_id = conn.rs.getString(11);
         credit = conn.rs.getString(12);
         cleared = conn.rs.getString(13);
         //push to excel
         String [] data = {fco,id_code,gl_code,gl_account,gl_account_name,id_code_speed_key_reqd,external_doc_no,award_reqd,restriction_reqd,id_code_reqd,sub_award,debit,credit,posting_description,je_description};
         cell_pos=0;
         for(String value :data){
            XSSFCell  S1cell=rw2.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Integer.parseInt(value));
            }
            else{
                S1cell.setCellValue(value);
            }
            S1cell.setCellStyle(stborder); 
            cell_pos++;   
         }
         row_pos++;
        }
        
        
       
        
        
        
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=Journal Entries_"+manager.getdatekey()+".xlsx");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();   
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
        Logger.getLogger(JournalEntries.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(JournalEntries.class.getName()).log(Level.SEVERE, null, ex);
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

       public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
       
       private static String removeLastChars(String str, int num) {
    return str.substring(0, str.length() - num);
}
}
