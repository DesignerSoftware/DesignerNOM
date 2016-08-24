/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Validaciones;

import Entidades.Aficiones;
import InterfacePersistencia.PersistenciaAficionesInterface;
import Persistencia.PersistenciaAficiones;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "PruebaValida", urlPatterns = {"/PruebaValida"})
public class PruebaValida extends HttpServlet {

    @EJB
    PersistenciaAficionesInterface persistenciaAficiones;
    
    private Aficiones aficion;
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            short codigo =126;
            String descaficion = "Tejo";
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Prueba Valida</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PruebaValida at " + request.getContextPath() + "</h1>");
            out.println("Codigo: " + codigo);
            out.println("Descripción: " + descaficion);
            out.println("</body>");
            out.println("</html>");
            aficion = new Aficiones();
            aficion.setSecuencia(BigInteger.valueOf(0));
            aficion.setCodigo(codigo);
            aficion.setDescripcion(descaficion);
            //persistenciaAficiones.crear(aficion);
            
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
