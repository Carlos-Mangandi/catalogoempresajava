
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="catalogoempresas.entidadesdenegocio.Empresa"%>
<%@page import="catalogoempresas.entidadesdenegocio.Contacto"%>
<%@page import="java.util.ArrayList"%>

<% ArrayList<Empresa> empresas = (ArrayList<Empresa>) request.getAttribute("empresas");
    int numPage = 1;
    int numReg = 10;
    int countReg = 0;
    if (empresas == null) {
        empresas = new ArrayList();
    } else if (empresas.size() > numReg) {
        double divNumPage = (double) empresas.size() / (double) numReg;
        numPage = (int) Math.ceil(divNumPage);
    }
    String strTop_aux = request.getParameter("top_aux");
    int top_aux = 10;
    if (strTop_aux != null && strTop_aux.trim().length() > 0) {
        top_aux = Integer.parseInt(strTop_aux);
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Lista de Empresas</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Buscar Empresa</h5>
            <form action="Empresa" method="post">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>"> 
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input  id="txtNombre" type="text" name="nombre">
                        <label for="txtNombre">Nombre</label>
                    </div>  
                    <div class="input-field col l4 s12">
                        <input  id="txtRubro" type="text" name="rubro">
                        <label for="txtRubro">Rubro</label>
                    </div> 
                    <div class="input-field col l4 s12">
                        <input  id="txtCategoria" type="text" name="categoria">
                        <label for="txtCategoria">Categoria</label>
                    </div>                    
                    <div class="input-field col l4 s12">
                        <input  id="txtDepartamento" type="text" name="departamento">
                        <label for="txtDepartamento">Departamento</label>
                    </div>
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Contacto/select.jsp">                           
                            <jsp:param name="id" value="0" />  
                        </jsp:include>                        
                    </div>
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Shared/selectTop.jsp">
                            <jsp:param name="top_aux" value="<%=top_aux%>" />                        
                        </jsp:include>                        
                    </div> 
                </div>
                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">search</i>Buscar</button>
                        <a href="Empresa?accion=create" class="waves-effect waves-light btn blue"><i class="material-icons right">add</i>Crear</a>                          
                    </div>
                </div>
            </form>

            <div class="row">
                <div class="col l12 s12">
                    <div style="overflow: auto">
                        <table class="paginationjs">
                            <thead>
                                <tr>                                     
                                    <th>Nombre</th>  
                                    <th>Rubro</th> 
                                    <th>Categoría</th>  
                                    <th>Departamento</th>  
                                    <th>Contacto</th>     
                                    <th>Acciones</th>
                                </tr>
                            </thead>                       
                            <tbody>                           
                                <% for (Empresa empresa : empresas) {
                                        int tempNumPage = numPage;
                                        if (numPage > 1) {
                                            countReg++;
                                            double divTempNumPage = (double) countReg / (double) numReg;
                                            tempNumPage = (int) Math.ceil(divTempNumPage);
                                        }
                                %>
                                <tr data-page="<%= tempNumPage%>">                                    
                                    <td><%=empresa.getNombre()%></td>  
                                    <td><%=empresa.getRubro()%></td>
                                    <td><%=empresa.getCategoria()%></td>  
                                    <td><%=empresa.getDepartamento()%></td>
                                    <td><%=empresa.getContacto().getNombre()%></td>  
                                    <td>
                                        <div style="display:flex">
                                             <a href="Empresa?accion=edit&id=<%=empresa.getId()%>" title="Modificar" class="waves-effect waves-light btn green">
                                            <i class="material-icons">edit</i>
                                        </a>
                                        <a href="Empresa?accion=details&id=<%=empresa.getId()%>" title="Ver" class="waves-effect waves-light btn blue">
                                            <i class="material-icons">description</i>
                                        </a>
                                        <a href="Empresa?accion=delete&id=<%=empresa.getId()%>" title="Eliminar" class="waves-effect waves-light btn red">
                                            <i class="material-icons">delete</i>
                                        </a>    
                                        </div>                                                                    
                                    </td>                                   
                                </tr>
                                <%}%>                                                       
                            </tbody>
                        </table>
                    </div>                  
                </div>
            </div>             
            <div class="row">
                <div class="col l12 s12">
                    <jsp:include page="/Views/Shared/paginacion.jsp">
                        <jsp:param name="numPage" value="<%= numPage%>" />                        
                    </jsp:include>
                </div>
            </div>
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>