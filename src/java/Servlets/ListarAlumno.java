/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.AlumnoJpaController;
import Controladores.ProfesorJpaController;
import Entidades.Alumno;
import Entidades.Profesor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class ListarAlumno extends HttpServlet {

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

            AlumnoJpaController ac = new AlumnoJpaController();
            LocalDate ahora = LocalDate.now();
            DateTimeFormatter s = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Alumno p : ac.findAlumnoEntities()) {
                JsonObject j = new JsonObject();
                j.addProperty("paterno", p.getApPaterno());
                j.addProperty("materno", p.getApMaterno());
                j.addProperty("codminedu", p.getCodminedu());
                j.addProperty("colegio", p.getColegioanterior());

                j.addProperty("contraseña", p.getContraseña());
                j.addProperty("correo", p.getCorreo());
                j.addProperty("departamento", p.getDepartamento());
                j.addProperty("direccion", p.getDireccion());
                j.addProperty("discapacidad", p.getDiscapacidad());
                j.addProperty("distrito", p.getDistrito());
                j.addProperty("dni", p.getDni());
                j.addProperty("estado", p.getEstado());

                LocalDate fechaNac = LocalDate.parse((p.getFechaNacimiento()), s);

                Period periodo = Period.between(fechaNac, ahora);

                j.addProperty("edad", periodo.getYears() + " años");
                j.addProperty("nacimiento", (p.getFechaNacimiento()));

                j.addProperty("lenguas", p.getLenguas());

                j.addProperty("nombre", p.getNombre());
                j.addProperty("provincia", p.getProvincia());
                j.addProperty("sexo", p.getSexo());
                j.addProperty("telefono", p.getTelefono());
                j.addProperty("usuario", p.getUsuario());
                j.addProperty("apoderados", "<a  class='btn' href='apoderados.jsp?alumno='"+p.getId()+" \"><i class='fa fa-users text-inverse m-r-10'></i></a>");
                j.addProperty("foto", "<a  class='btn' onclick=\"fotoProfesor('" + p.getImagen() + "','" + p.getNombre() + " " + p.getApPaterno() + " " + p.getApMaterno() + "')\"><i class='fa fa-picture-o text-inverse m-r-10'></i></a>");
                j.addProperty("editar", "<a  class='btn' onclick=\"editarProfesor('" + p.getId() + "','" + p.getNombre() + "','" + p.getApPaterno() + "','" + p.getApMaterno() + "','"+ p.getDni() + "','"+ p.getFechaNacimiento() + "','"+ p.getLenguas() + "','"+ p.getDiscapacidad() + "','"+ p.getCodminedu() + "','"+ p.getDireccion() + "','"+ p.getDistrito() + "','"+ p.getProvincia() + "','"+ p.getDepartamento() + "','" + p.getTelefono() + "','"+ p.getCorreo() + "','"+ p.getColegioanterior() + "','" + p.getEstado()+ "','"+ p.getSexo() + "','" + p.getUsuario() + "','"+ p.getContraseña() + "','" + p.getImagen() + "')\"><i class='fa fa-pencil text-inverse m-r-10'></i></a>");

                j.addProperty("eliminar", "<a class='btn' onclick=\"eliminarProfesor(" + p.getId() + ")\"><i class='fa fa-close text-danger'></i></a>");

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
