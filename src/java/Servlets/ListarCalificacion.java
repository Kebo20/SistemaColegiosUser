/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.CalificacionJpaController;
import Controladores.ClaseJpaController;
import Entidades.Calificacion;
import Entidades.Clase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class ListarCalificacion extends HttpServlet {

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
             JsonObject json = new JsonObject();
            
            JsonArray ja = new JsonArray();
            
            CalificacionJpaController cc = new CalificacionJpaController();
            
            List<Calificacion> list = cc.listarxclase(Integer.parseInt(request.getParameter("clase")));
            
            for (int i = 0; i < list.size(); i++) {
                JsonObject j = new JsonObject();
                j.addProperty("descripcion", list.get(i).getDescripcion());
                j.addProperty("subcalificacion", "<a class=\"btn \"href='subcalificaciones.jsp?cal="+list.get(i).getId()+"&clase="+list.get(i).getIdclase().getId()+"' \">Subcalificación</a>");
                j.addProperty("editar", "<a class=\"btn \" onclick=\"editarCalificacion('" + list.get(i).getId()+ "','" +list.get(i).getDescripcion()+ "')\"><icon class='fa fa-pencil'></icon></a>");
                j.addProperty("eliminar", "<a class=\"btn \" onclick=\"eliminarCalificacion('" + list.get(i).getId()+ "')\"><icon class='fa fa-trash'></icon></a>");
                ja.add(j);
            }
            json.add("datos", ja);
            out.print(json.toString());
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
