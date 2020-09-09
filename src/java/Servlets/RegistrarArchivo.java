/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Controladores.ArchivoJpaController;
import Entidades.Archivo;
import Entidades.Clase;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author kevin
 */
public class RegistrarArchivo extends HttpServlet {

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
           RecibirFormulario(request);
            ArchivoJpaController ac = new ArchivoJpaController(Persistence.createEntityManagerFactory("colegiosPU") );
            Archivo a= new Archivo();
            a.setIdclase(new Clase(Integer.parseInt(request.getAttribute("clase").toString())));
            a.setDescripcion(request.getAttribute("descripcion").toString());
            a.setNombre(request.getAttribute("archivo").toString());
            a.setTamaño(request.getAttribute("tamaño").toString());
            
             try {
                ac.create(a);
                out.print(":)");
            } catch (Exception ex) {
                out.print(ex.toString());

            }
            
        }
    }

    
     private void RecibirFormulario(HttpServletRequest request) {
        try {
            FileItemFactory fileFactory = new DiskFileItemFactory();
            ServletFileUpload servletUpload = new ServletFileUpload(fileFactory);
            List items = servletUpload.parseRequest(request);
            String nombre = "";

            for (int i = 0; i < items.size(); i++) {
                FileItem item = (FileItem) items.get(i);

                if (!item.isFormField()) {
                    String ruta = request.getServletContext().getRealPath("/") + "archivos/";
////                    String ruta="/opt/tomcat/webapps/AulaVirtual/archivos/";
//                    String ruta = "/opt/tomcat/webapps/archivos-colegios/archivos-clases/";

                    SimpleDateFormat sdf = new SimpleDateFormat("ddMyyyyhhmmss");
                    String fecha = sdf.format(new Date());
                    nombre = fecha + item.getName();
                    String nuevonombre = ruta + nombre;
                    File folder = new File(ruta);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    File archivo = new File(nuevonombre);
//                    if (item.getContentType().contains("image")) {

                    item.write(archivo);

                    request.setAttribute(item.getFieldName(), nombre);//nombre del campo(imagen) del formulario que se está analizando y su valor
//                    request.setAttribute("tamaño", (double) Math.round(Double.valueOf(item.getSize()) / 1048576 * 10000) / 10000);
                    request.setAttribute("tamaño", (double) Math.round(Double.valueOf(item.getSize()) / 1024000 * 10000) / 10000);
//                    request.setAttribute("tamaño", (item.getSize()) );
//                    } else {

//                    }
                } else {
                    request.setAttribute(item.getFieldName(), new String(item.getString().getBytes("ISO-8859-1"), "UTF-8"));//nombre del campo(de texto) del formulario que se está analizando y su valor
//                    request.setAttribute(item.getFieldName(), item.getString());//nombre del campo(de texto) del formulario que se está analizando y su valor

                }

            }

        } catch (FileUploadException ex) {

        } catch (Exception ex) {

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
