
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="catalogoempresas.entidadesdenegocio.Empresa"%>
<% Empresa empresa = (Empresa) request.getAttribute("empresa");%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Editar Empresa</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Editar Empresa</h5>
            <form action="Empresa" method="post" onsubmit="return  validarFormulario()">
                <input type="hidden" name="accion" value="<%=request.getAttribute("accion")%>"> 
                <input type="hidden" name="id" value="<%=empresa.getId()%>">  
                <div class="row">
                    <div class="input-field col l4 s12">
                        <input  id="txtNombre" type="text" name="nombre" value="<%=empresa.getNombre()%>" required class="validate" maxlength="50">
                        <label for="txtNombre">Nombre</label>
                    </div>                       
                    <div class="input-field col l4 s12">
                        <input  id="txtRubro" type="text" name="rubro" value="<%=empresa.getRubro()%>" required class="validate" maxlength="30">
                        <label for="txtRubro">Rubro</label>
                    </div> 
                    <div class="input-field col l4 s12">
                        <input  id="txtCategoria" type="text" name="categoria" value="<%=empresa.getCategoria()%>" required  class="validate" maxlength="25">
                        <label for="txtCategoria">Categoria</label>
                    </div>
                    
                    <div class="input-field col l4 s12">
                        <input  id="txtDepartamento" type="text" name="departamento" value="<%=empresa.getDepartamento()%>" required  class="validate" maxlength="25">
                        <label for="txtDepartamento">Departamento</label>
                    </div>
                    
                    <div class="input-field col l4 s12">   
                        <jsp:include page="/Views/Contacto/select.jsp">                           
                            <jsp:param name="id" value="<%=empresa.getIdContacto() %>" />  
                        </jsp:include>  
                        <span id="slContacto_error" style="color:red" class="helper-text"></span>
                    </div>
                </div>

                <div class="row">
                    <div class="col l12 s12">
                        <button type="sutmit" class="waves-effect waves-light btn blue"><i class="material-icons right">save</i>Guardar</button>
                        <a href="Empresa" class="waves-effect waves-light btn blue"><i class="material-icons right">cancel</i>Cancelar</a>                          
                    </div>
                </div>
            </form>          
        </main>
                        
        <jsp:include page="/Views/Shared/footerBody.jsp" />   
        <script>
            function validarFormulario() {
                var result = true;                
                var slContacto = document.getElementById("slContacto");
                var slContacto_error = document.getElementById("slContacto_error");
                if (slContacto.value == 0) {
                    slContacto_error.innerHTML = "El Contacto es obligatorio";
                    result = false;
                } else {
                    slContacto_error.innerHTML = "";
                }

                return result;
            }
        </script>
    </body>
</html>