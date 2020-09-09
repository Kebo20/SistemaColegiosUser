/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.NotasJpaController;
import Entidades.Matriculaclase;
import Entidades.Notas;
import Entidades.Periodo;
import Entidades.Subcalificacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class RegistrarNotas extends HttpServlet {

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
           
            
            NotasJpaController nc= new NotasJpaController(Persistence.createEntityManagerFactory("colegiosPU"));
            Notas n = new Notas();
            
           n.setDescripcion(request.getParameter("descripcion"));
           n.setIdmatriculaclase(new Matriculaclase(Integer.parseInt(request.getParameter("matriculaclase"))));
           n.setIdperiodo(new Periodo(Integer.parseInt(request.getParameter("periodo"))));
           n.setIdsubcalificacion(new Subcalificacion(Integer.parseInt(request.getParameter("subcal"))));
           n.setNota(new Double(request.getParameter("nota")));
           n.setId(Integer.parseInt(request.getParameter("matriculaclase")+request.getParameter("subcal")+request.getParameter("periodo")));
           
           
            try {
                nc.create(n);
            } catch (Exception ex) {
                Logger.getLogger(RegistrarNotas.class.getName()).log(Level.SEVERE, null, ex);
            }
                   
            
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
