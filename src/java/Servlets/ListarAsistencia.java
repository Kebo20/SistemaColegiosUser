/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.AsistenciaJpaController;
import Entidades.Asistencia;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class ListarAsistencia extends HttpServlet {

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
            
            AsistenciaJpaController ac = new AsistenciaJpaController();
//            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            
            List<Asistencia> list = ac.listarxclasefecha(request.getParameter("fecha"),Integer.parseInt(request.getParameter("clase")));
            
            for (int i = 0; i < list.size(); i++) {
                JsonObject j = new JsonObject();
                j.addProperty("alumno", list.get(i).getIdalumno().getNombre()+" "+list.get(i).getIdalumno().getApPaterno()+" "+list.get(i).getIdalumno().getApMaterno());
                j.addProperty("fecha", (list.get(i).getFecha()));
                
                switch(list.get(i).getTipo()){
                    case "P":
                        j.addProperty("tipo", "Presente");
                        break;
                    case "A":
                        j.addProperty("tipo", "Ausente");
                        break;
                    case "T":
                        j.addProperty("tipo", "Tardanza");
                        break;
                    case "TJ":
                        j.addProperty("tipo", "Tardanza Justificada");
                        break;
                   

                }
                
                j.addProperty("descripcion", list.get(i).getDescripcion());
                j.addProperty("editar", "<a class=\"btn \" onclick=\"editarAsistencia('" + list.get(i).getId()+ "','" + list.get(i).getFecha()+ "','" + list.get(i).getTipo()+ "','" + list.get(i).getDescripcion()+ "','" + list.get(i).getIdalumno().getId()+ "')\"><icon class='fa fa-pencil text-inverse m-r-10'></icon></a>");
                j.addProperty("eliminar", "<a class=\"btn btn-danger\" onclick=\"eliminarAsistencia('" + list.get(i).getId()+ "')\">Eliminar</a>");
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
