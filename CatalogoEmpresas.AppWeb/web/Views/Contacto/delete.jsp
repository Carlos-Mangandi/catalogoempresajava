
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="catalogoempresas.entidadesdenegocio.Contacto"%>
<% Contacto contacto = (Contacto) request.getAttribute("contacto");%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Eliminar Contacto</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Eliminar Contacto</h5>          
            <form action="Contacto" method="post">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>">   
                <input type="hidden" name="id" value="<%=contacto.getId()%>">   
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input disabled  id="txtNombre" type="text" value="<%=contacto.getNombre()%>">
                        <label for="txtNombre">Nombre</label>
                    </div>       
                    <div class="input-field col l4 s12">
                        <input disabled  id="txtEmail" type="text" value="<%=contacto.getEmail()%>">
                        <label for="txtEmail">Email</label>
                    </div>
                    <div class="input-field col l4 s12">
                        <input disabled  id="txtTelefono" type="text" value="<%=contacto.getTelefono()%>">
                        <label for="txtTelefono">Teléfono</label>
                    </div>
                    <div class="input-field col l4 s12">
                        <input disabled  id="txtCelular" type="text" value="<%=contacto.getCelular()%>">
                        <label for="txtCelular">Celular</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">delete</i>Eliminar</button>
                        <a href="Contacto" class="waves-effect waves-light btn blue"><i class="material-icons right">cancel</i>Cancelar</a>                          
                    </div>
                </div>
            </form>          
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>