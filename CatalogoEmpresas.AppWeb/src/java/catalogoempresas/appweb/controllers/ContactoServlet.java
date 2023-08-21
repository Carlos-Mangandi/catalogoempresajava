
package catalogoempresas.appweb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import catalogoempresas.accesoadatos.ContactoDAL;
import catalogoempresas.entidadesdenegocio.Contacto;
import catalogoempresas.appweb.utils.*;

@WebServlet(name = "ContactoServlet", urlPatterns = {"/Contacto"})
public class ContactoServlet extends HttpServlet {
    
    private Contacto obtenerContacto(HttpServletRequest request) {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Contacto contacto = new Contacto();
        if (accion.equals("create") == false) {
            contacto.setId(Integer.parseInt(Utilidad.getParameter(request, "id", "0")));
        }

        contacto.setNombre(Utilidad.getParameter(request, "nombre", ""));
        contacto.setEmail(Utilidad.getParameter(request, "email", ""));
        contacto.setTelefono(Utilidad.getParameter(request, "telefono", ""));
        contacto.setCelular(Utilidad.getParameter(request, "celular", ""));
        
        if (accion.equals("index")) {
            contacto.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, "top_aux", "10")));
            contacto.setTop_aux(contacto.getTop_aux() == 0 ? Integer.MAX_VALUE : contacto.getTop_aux());
        }
        
        return contacto;
    }
    
    // accion 
    private void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = new Contacto();
            contacto.setTop_aux(10);
            ArrayList<Contacto> contactos = ContactoDAL.buscar(contacto);
            request.setAttribute("contactos", contactos);
            request.setAttribute("top_aux", contacto.getTop_aux());             
            request.getRequestDispatcher("Views/Contacto/index.jsp").forward(request, response);
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    // devuelve una consulta
    private void doPostRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = obtenerContacto(request);
            ArrayList<Contacto> contactos = ContactoDAL.buscar(contacto);
            request.setAttribute("contactos", contactos);
            request.setAttribute("top_aux", contacto.getTop_aux());
            request.getRequestDispatcher("Views/Contacto/index.jsp").forward(request, response);
        } catch (Exception ex) { 
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("Views/Contacto/create.jsp").forward(request, response);
    }
    
    private void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = obtenerContacto(request);
            int result = ContactoDAL.crear(contacto);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro registrar un nuevo registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void requestObtenerPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = obtenerContacto(request);
            Contacto contacto_result = ContactoDAL.obtenerPorId(contacto);
            if (contacto_result.getId() > 0) {
                request.setAttribute("contacto", contacto_result);
            } else {
                Utilidad.enviarError("El Id:" + contacto.getId() + " no existe en la tabla de Contactos", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Contacto/edit.jsp").forward(request, response);
    }
    
    private void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = obtenerContacto(request);
            int result = ContactoDAL.modificar(contacto);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response); // este metodo hace llenarlos la tabla con diez registro
            } else {
                Utilidad.enviarError("No se logro actualizar el registro", request, response);
            }
        } catch (Exception ex) {
            // Enviar al jsp de error si hay un Exception
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
    private void doGetRequestDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Contacto/details.jsp").forward(request, response);
    }
    
    private void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Contacto/delete.jsp").forward(request, response);
    }
    
    private void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Contacto contacto = obtenerContacto(request);
            int result = ContactoDAL.eliminar(contacto);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logró eliminar el registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    @Override // el servlet solo tiene dos metodos y son doGet y doPost
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doGetRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doGetRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doGetRequestDelete(request, response);
                    break;
                case "details":
                    request.setAttribute("accion", accion);
                    doGetRequestDetails(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion); 
                    doGetRequestIndex(request, response);
            }
        });
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doPostRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doPostRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doPostRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doPostRequestDelete(request, response);
                    break;
                default: //encualquier otro caso
                    request.setAttribute("accion", accion); // el accion es index
                    doGetRequestIndex(request, response);
            }
        });
    }
// </editor-fold>
}
