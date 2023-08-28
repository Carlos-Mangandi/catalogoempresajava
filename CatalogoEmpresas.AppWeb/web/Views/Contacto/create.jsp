
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Crear Contacto</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Crear Contacto</h5>
            <form action="Contacto" method="post">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>">                
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input  id="txtNombre" type="text" name="nombre" required class="validate" maxlength="30">
                        <label for="txtNombre">Nombre</label>
                    </div>  
                    <div class="input-field col l4 s12">
                        <input  id="txtEmail" type="text" name="email" required class="validate" maxlength="30">
                        <label for="txtEmail">Email</label>
                    </div>
                    <div class="input-field col l4 s12">
                        <input  id="txtTelefono" type="text" name="telefono" required class="validate" maxlength="30">
                        <label for="txtTelefono">Teléfono</label>
                    </div>
                    <div class="input-field col l4 s12">
                        <input  id="txtCelular" type="text" name="celular" required class="validate" maxlength="30">
                        <label for="txtCelular">Celular</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">save</i>Guardar</button>
                        <a href="Contacto" class="waves-effect waves-light btn blue"><i class="material-icons right">cancel</i>Cancelar</a>                          
                    </div>
                </div>
            </form>          
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>