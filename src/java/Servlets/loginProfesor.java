/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.ProfesorJpaController;
import Entidades.Calificacion;
import Entidades.Clase;
import Entidades.Notas;
import Entidades.Profesor;
import Entidades.Subcalificacion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kevin
 */
public class loginProfesor extends HttpServlet {

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
            Profesor a = new Profesor();
            a.setContraseña(request.getParameter("contraseña"));
            a.setUsuario(request.getParameter("usuario"));

            ProfesorJpaController ac = new ProfesorJpaController();

            Profesor profesor = ac.Login(a);

            if (profesor == null) {
                out.print(":(");
            } else {
                HttpSession nueva_sesion = request.getSession(true);
                nueva_sesion.setAttribute("profesor", profesor);
                nueva_sesion.setAttribute("clases", profesor.getClaseList());

////                List<Calificacion> lcalificacion = null;
////                List<Subcalificacion> lsubcalificacion = null;
////                List<Notas> lnotas = null;
////               try{
////                   for (Clase clase : profesor.getClaseList()) {
////
////                    lcalificacion.addAll(clase.getCalificacionList());
////                }
////                nueva_sesion.setAttribute("calificaciones", lcalificacion);
////               } catch(Exception e){
////                   
////               }
//                
//                for (Calificacion calificacion : lcalificacion) {
//                    lsubcalificacion.addAll(calificacion.getSubcalificacionList());
//                }
//
//                nueva_sesion.setAttribute("subcalificaciones", lcalificacion);
//
//                for (Subcalificacion subcalificacion : lsubcalificacion) {
//                    lnotas.addAll(subcalificacion.getNotasList());
//                }
//
//                nueva_sesion.setAttribute("notas", lnotas);

                nueva_sesion.setAttribute("rol", "profesor");
                nueva_sesion.setMaxInactiveInterval(24 * 60 * 60);

                out.print(":)");

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
