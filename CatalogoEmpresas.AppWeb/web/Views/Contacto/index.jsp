
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="catalogoempresas.entidadesdenegocio.Contacto"%>
<%@page import="java.util.ArrayList"%>

<% ArrayList<Contacto> contactos = (ArrayList<Contacto>) request.getAttribute("contactos");
    int numPage = 1;
    int numReg = 10;
    int countReg = 0;
    if (contactos == null) {
        contactos = new ArrayList();
    } else if (contactos.size() > numReg) {
        double divNumPage = (double) contactos.size() / (double) numReg;
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
        <title>Lista de Contactos</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Buscar Contacto</h5>
            <form action="Contacto" method="post">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>"> 
                <div class="row">
                    <div class="input-field col l6 s12">
                        <input  id="txtNombre" type="text" name="nombre">
                        <label for="txtNombre">Nombre</label>
                    </div>
                    <div class="input-field col l6 s12">
                        <input  id="txtEmail" type="text" name="email">
                        <label for="txtEmail">Email</label>
                    </div>
                    <div class="input-field col l6 s12">
                        <input  id="txtTelefono" type="text" name="telefono">
                        <label for="txtTelefono">Teléfono</label>
                    </div>
                    <div class="input-field col l6 s12">
                        <input  id="txtCelular" type="text" name="celular">
                        <label for="txtCelular">Celular</label>
                    </div>                    
                    <div class="input-field col l3 s12">   
                        <jsp:include page="/Views/Shared/selectTop.jsp">
                            <jsp:param name="top_aux" value="<%=top_aux%>" />                        
                        </jsp:include>                        
                    </div> 
                </div>
                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">search</i>Buscar</button>
                        <a href="Contacto?accion=create" class="waves-effect waves-light btn blue"><i class="material-icons right">add</i>Crear</a>                          
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
                                    <th>Email</th> 
                                    <th>Teléfono</th> 
                                    <th>Celular</th> 
                                    <th>Acciones</th>
                                </tr>
                            </thead>                       
                            <tbody>                           
                                <% for (Contacto contacto : contactos) {
                                        int tempNumPage = numPage;
                                        if (numPage > 1) {
                                            countReg++;
                                            double divTempNumPage = (double) countReg / (double) numReg;
                                            tempNumPage = (int) Math.ceil(divTempNumPage);
                                        }
                                %>
                                <tr data-page="<%= tempNumPage%>">
                                    <td><%=contacto.getNombre()%></td> 
                                    <td><%=contacto.getEmail()%></td>
                                    <td><%=contacto.getTelefono()%></td>
                                    <td><%=contacto.getCelular()%></td>
                                    <td>
                                        <div style="display:flex">
                                            <a href="Contacto?accion=edit&id=<%=contacto.getId()%>" title="Modificar" class="waves-effect waves-light btn green">
                                                <i class="material-icons">edit</i>
                                            </a>
                                            <a href="Contacto?accion=details&id=<%=contacto.getId()%>" title="Ver" class="waves-effect waves-light btn blue">
                                                <i class="material-icons">description</i>
                                            </a>
                                            <a href="Contacto?accion=delete&id=<%=contacto.getId()%>" title="Eliminar" class="waves-effect waves-light btn red">
                                                <i class="material-icons">delete</i>
                                            </a>     
                                        </div>
                                    </td>                                   
                                </tr>
                                <% } %>                                                       
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
