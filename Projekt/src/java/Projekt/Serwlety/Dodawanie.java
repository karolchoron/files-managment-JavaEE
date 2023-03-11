package Projekt.Serwlety;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Hiber.PptQuery;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;


@WebServlet(name = "Dodawanie", urlPatterns = {"/Dodawanie"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50     
        )
        

public class Dodawanie extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><meta><link rel='stylesheet' href='Style/css/components.css'>");
            out.println("<link rel='stylesheet' href='Style/css/icons.css'>");
            out.println("<link rel='stylesheet' href='Style/css/responsee.css'>");
            out.println("<title>Adding PPT files </title>");                       
            out.println("</head>");
            out.println("</body>");
            
            
            
            Part filePart = request.getPart("file_upload");
            
            String nazwaPliku = filePart.getSubmittedFileName();
            
            int najwiekszeAktualneId = new Hiber.PptQuery().najwiekszeId();
            int idNowegoPliku = najwiekszeAktualneId+1;
            
            String opis = request.getParameter("opis_upload");
            
            int dlugoscOpisu = opis.length();
            
            
            
            PptQuery wgrywaniePliku = new PptQuery();
            
            if(dlugoscOpisu==0){
                out.println("<h3> YOU CAN'T LEAVE EMPTY DESCRIPTION! </h3><br><br>");
            }else if(dlugoscOpisu>300){
                out.println("<h3> MAX SIZE OF DESCRIPTION IS 300 CHARACTERS! </h3><br><br>");  
            }else{
                int x = wgrywaniePliku.uploadFile(filePart, nazwaPliku, idNowegoPliku, opis);
                if(x==0){
                    out.println("<h3> FILE: "+nazwaPliku+" UPLOADED ON THE SERVER </h3><br><br>");
                }else{
                    out.println("<h3> ERROR! YOU CAN UPLOAD ONLY .PPT OR .PPTX FILES!</h3><br><br>");
                }   
            }
            
            
            out.println("<a class=\'button rounded-full-btn reload-btn s-2 margin-bottom\' href=");
            out.println(request.getHeader("referer"));
            out.println("><i class='icon-sli-arrow-left'>Back...</i></a>");        
            out.println("</body>");
            out.println("</html>");
        } 
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
