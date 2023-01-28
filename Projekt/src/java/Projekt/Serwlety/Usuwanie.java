/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Projekt.Serwlety;

import Hiber.PptQuery;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author student
 */
@WebServlet(name = "Usuwanie", urlPatterns = {"/Usuwanie"})
public class Usuwanie extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><meta><link rel='stylesheet' href='Style/css/components.css'>");
            out.println("<link rel='stylesheet' href='Style/css/icons.css'>");
            out.println("<link rel='stylesheet' href='Style/css/responsee.css'>");
            out.println("<title>Removing files</title>");                        
            out.println("</head>");
            out.println("<body>");
            
            
            String pobranyElement = request.getParameter("del_file_id");
            int file_id = Integer.parseInt(pobranyElement);
            
            if(pobranyElement.isEmpty()){
                out.println("<h3> PLEASE WRITE ID OF FILE YOU WANT TO DELETE  </h3><br><br>");
            }else{
                int czyIstnieje = new Hiber.PptQuery().walidacjaCzyIstaniejeId(file_id);
                if(czyIstnieje==0){
                    PptQuery del = new PptQuery();
                    del.deletePptFile(file_id);
                    out.println("<h3> FILE REMOVED FROM SERVER </h3><br><br>");

                }else{
                    out.println("<h3> ERROR! WE DONT HAVE FILE WITH THIS ID! TRY AGAIN  </h3><br><br>");  
                }
            }            
            
            out.println("<a class=\'button rounded-full-btn reload-btn s-2 margin-bottom\' href=");
            out.println(request.getHeader("referer"));
            out.println("><i class='icon-sli-arrow-left'>Back...</i></a>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
