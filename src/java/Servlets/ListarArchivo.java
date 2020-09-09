/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.ArchivoJpaController;
import Controladores.AsistenciaJpaController;
import Entidades.Archivo;
import Entidades.Asistencia;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
public class ListarArchivo extends HttpServlet {

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

            ArchivoJpaController ac = new ArchivoJpaController(Persistence.createEntityManagerFactory("colegiosPU"));

            List<Archivo> list = ac.listarxclase(Integer.parseInt(request.getParameter("clase")));

            for (int i = 0; i < list.size(); i++) {
                JsonObject j = new JsonObject();
                j.addProperty("nombre", (list.get(i).getNombre()));
               
              j.addProperty("archivo",VerificarExtension(list.get(i).getNombre()));
                      

                
                j.addProperty("tamaño", list.get(i).getTamaño() + " MB");
                j.addProperty("descripcion", list.get(i).getDescripcion());
                j.addProperty("eliminar", "<a class=\"btn \" onclick=\"eliminarArchivo('" + list.get(i).getId() + "')\"><icon class='fa fa-trash'></icon></a>");
                ja.add(j);
            }
            json.add("datos", ja);
            out.print(json.toString());
        }
    }

    public static String VerificarExtension(String url) {
        String extension;
        String extension2;
        String resultado = "";

        extension = url.substring(url.length() - 3, url.length());
        extension2 = url.substring(url.length() - 4, url.length());
        if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg")) {

            resultado = "<a class='btn btn-inverse ' href='../archivos/" + url + "' ><icon class='fa fa-file-image-o'></icon></a>";
        } else if (extension.equalsIgnoreCase("mkv") || extension.equalsIgnoreCase("mp4") || extension.equalsIgnoreCase("mpeg") || extension.equalsIgnoreCase("avi")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-video-o'></icon></a>";
        } else if (extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("wav")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-audio-o'></icon></a>";
        } else if (extension.equalsIgnoreCase("doc") || extension2.equalsIgnoreCase("docx")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-word-o'></icon></a>";
        } else if (extension.equalsIgnoreCase("ppt") || extension2.equalsIgnoreCase("pptx")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-powerpoint-o'></icon></a>";
        } else if (extension.equalsIgnoreCase("pdf")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-pdf-o'></icon></a>";

        } else if (extension.equalsIgnoreCase("xls") || extension2.equalsIgnoreCase("xlsx")) {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-excel-o'></icon></a>";
        } else {
            resultado = "<a class='btn btn-inverse' href='../archivos/" + url + "' ><icon class='fa fa-file-archive-o'></icon></a>";
        }

        return resultado;
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
