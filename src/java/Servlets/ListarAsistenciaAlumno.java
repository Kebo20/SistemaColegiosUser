/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.AsistenciaJpaController;
import Entidades.Alumno;
import Entidades.Asistencia;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
public class ListarAsistenciaAlumno extends HttpServlet {

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
             HttpSession sesion = request.getSession();
//            Alumno alumno = (Alumno) sesion.getAttribute("alumno");
            List<Asistencia> list = ac.listarxalumno(Integer.parseInt(sesion.getAttribute("idalumno").toString()));
            
            for (int i = 0; i < list.size(); i++) {
                JsonObject j = new JsonObject();
                //j.addProperty("alumno", list.get(i).getIdalumno().getNombre()+" "+list.get(i).getIdalumno().getApPaterno()+" "+list.get(i).getIdalumno().getApMaterno());
                j.addProperty("start", (list.get(i).getFecha()));
                j.addProperty("end", (list.get(i).getFecha()));
                
                switch(list.get(i).getTipo()){
                    case "P":
                        j.addProperty("title", "Presente");
                        j.addProperty("color", "green");
                        break;
                    case "A":
                        j.addProperty("title", "Ausente");
                       j.addProperty("color", "red");

                        break;
                    case "T":
                        j.addProperty("title", "Tardanza");
                        j.addProperty("color", "blue");

                        break;
                    
                   

                }
                
              j.addProperty("descripcion", list.get(i).getDescripcion());
                ja.add(j);
            }
            out.print(ja.toString());
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
