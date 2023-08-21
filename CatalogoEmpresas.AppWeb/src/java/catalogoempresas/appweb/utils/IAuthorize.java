
package catalogoempresas.appweb.utils;

import jakarta.servlet.ServletException;
import java.io.IOException;

public interface IAuthorize {
    void authorize() throws ServletException, IOException; // La interfaz solo define los metodos pero no los implementa
}
