
package catalogoempresas.appweb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import catalogoempresas.accesoadatos.ContactoDAL;
import catalogoempresas.accesoadatos.EmpresaDAL;
import catalogoempresas.appweb.utils.*;
import catalogoempresas.entidadesdenegocio.Contacto;
import catalogoempresas.entidadesdenegocio.Empresa;

@WebServlet(name = "EmpresaServlet", urlPatterns = {"/Empresa"})
public class EmpresaServlet extends HttpServlet {

    private Empresa obtenerEmpresa(HttpServletRequest request) {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Empresa empresa = new Empresa();
        if (accion.equals("create") == false) {
            empresa.setId(Integer.parseInt(Utilidad.getParameter(request, "id", "0")));
        }
        empresa.setNombre(Utilidad.getParameter(request, "nombre", ""));
        empresa.setRubro(Utilidad.getParameter(request, "rubro", ""));
        empresa.setCategoria(Utilidad.getParameter(request, "categoria", ""));
        empresa.setDepartamento(Utilidad.getParameter(request, "departamento", ""));
        empresa.setIdContacto(Integer.parseInt(Utilidad.getParameter(request, "idContacto", "0")));
        
        if (accion.equals("index")) {
            empresa.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, "top_aux", "10")));
            empresa.setTop_aux(empresa.getTop_aux() == 0 ? Integer.MAX_VALUE : empresa.getTop_aux());
        }
        
        return empresa;
    }

    private void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = new Empresa();
            empresa.setTop_aux(10);
            ArrayList<Empresa> empresas = EmpresaDAL.buscarIncluirContacto(empresa);
            request.setAttribute("empresas", empresas);
            request.setAttribute("top_aux", empresa.getTop_aux());
            request.getRequestDispatcher("Views/Empresa/index.jsp").forward(request, response);
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

    private void doPostRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = obtenerEmpresa(request);
            ArrayList<Empresa> empresas = EmpresaDAL.buscarIncluirContacto(empresa);
            request.setAttribute("empresas", empresas);
            request.setAttribute("top_aux", empresa.getTop_aux());
            request.getRequestDispatcher("Views/Empresa/index.jsp").forward(request, response);
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

    private void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("Views/Empresa/create.jsp").forward(request, response);
    }

    private void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = obtenerEmpresa(request);
            int result = EmpresaDAL.crear(empresa);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro guardar el nuevo registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }

    }

    private void requestObtenerPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = obtenerEmpresa(request);
            Empresa empresa_result = EmpresaDAL.obtenerPorId(empresa);
            if (empresa_result.getId() > 0) {
                Contacto contacto = new Contacto();
                contacto.setId(empresa_result.getIdContacto());
                empresa_result.setContacto(ContactoDAL.obtenerPorId(contacto));
                request.setAttribute("empresa", empresa_result);
            } else {
                Utilidad.enviarError("El Id:" + empresa_result.getId() + " no existe en la tabla de Empresas", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

    private void doGetRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Empresa/edit.jsp").forward(request, response);
    }

    private void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = obtenerEmpresa(request);
            int result = EmpresaDAL.modificar(empresa);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro actualizar el registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

    private void doGetRequestDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Empresa/details.jsp").forward(request, response);
    }

    private void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/Empresa/delete.jsp").forward(request, response);
    }

    private void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Empresa empresa = obtenerEmpresa(request);
            int result = EmpresaDAL.eliminar(empresa);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro eliminar el registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
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
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
            }
        });
    }
    
// </editor-fold>
}
