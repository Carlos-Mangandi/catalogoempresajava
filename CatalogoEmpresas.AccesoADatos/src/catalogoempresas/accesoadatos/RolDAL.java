
package catalogoempresas.accesoadatos;

import java.util.*;
import java.sql.*;
import catalogoempresas.entidadesdenegocio.*;


public class RolDAL {
    static String obtenerCampos() { //los static no necesitan metodos ;  aqui nos muestra una lista de objetos
        return "r.Id, r.Nombre"; // alias r. " es para abreviar el nombre de una tabla "
    }
    
    private static String obtenerSelect(Rol pRol) {
        String sql;
        sql = "SELECT ";
        if (pRol.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {            
            sql += "TOP " + pRol.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Rol r");
        return sql;
    }
    
    private static String agregarOrderBy(Rol pRol) {
        String sql = " ORDER BY r.Id DESC";
        if (pRol.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            sql += " LIMIT " + pRol.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Rol pRol) throws Exception { //vamos hacer un INSERT
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO Rol(Nombre) VALUES(?)"; // el ? es para expresar que ahi hay un parametro
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pRol.getNombre());// 1 es de adonde vamos a sacar la consulta
                result = ps.executeUpdate();
                ps.close();
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return result; // nosotros capturamos lo que nos devuelve la BD; si fue correcta nos mandará 1 de lo contrario será 0
    }
    
    public static int modificar(Rol pRol) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "UPDATE Rol SET Nombre=? WHERE Id=?"; // aca hay 2 parámetros 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, pRol.getNombre());
                ps.setInt(2, pRol.getId());
                result = ps.executeUpdate();
                ps.close();
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    public static int eliminar(Rol pRol) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "DELETE FROM Rol WHERE Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pRol.getId());
                result = ps.executeUpdate();
                ps.close();
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return result;
    } 
    
    static int asignarDatosResultSet(Rol pRol, ResultSet pResultSet, int pIndex) throws Exception { // que es un result set = proporciona varios métodos para obtener  
                                  // una especie de copia que queda almacenada en la memoria       //los datos de columna correspondientes a un fila
        pIndex++;
        pRol.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pRol.setNombre(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Rol> pRoles) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            while (resultSet.next()) { // mientras en el result set siguiente
                Rol rol = new Rol(); 
                asignarDatosResultSet(rol, resultSet, 0);
                pRoles.add(rol);
            }
            resultSet.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
    }
    
    public static Rol obtenerPorId(Rol pRol) throws Exception {
        Rol rol = new Rol();
        ArrayList<Rol> roles = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pRol);
            sql += " WHERE r.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pRol.getId());
                obtenerDatos(ps, roles);
                ps.close(); // cierra la conexion
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        
        if (roles.size() > 0) {
            rol = roles.get(0);
        }
        
        return rol;
    }
    
    public static ArrayList<Rol> obtenerTodos() throws Exception {
        ArrayList<Rol> roles = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new Rol());
            sql += agregarOrderBy(new Rol());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                obtenerDatos(ps, roles);
                ps.close();
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        
        return roles;
    }
    
    static void querySelect(Rol pRol, ComunDB.utilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pRol.getId() > 0) {
            pUtilQuery.AgregarNumWhere(" r.Id=? ");
            if (statement != null) { 
                statement.setInt(pUtilQuery.getNumWhere(), pRol.getId()); 
            }
        }

        if (pRol.getNombre() != null && pRol.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" r.Nombre LIKE ? "); // en una cadena no se puede poner " = " sino que el operadors " LIKE"    
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pRol.getNombre() + "%"); // el " % " puede llevar cualquier cosa 
            }
        }
    }
    
    public static ArrayList<Rol> buscar(Rol pRol) throws Exception {
        ArrayList<Rol> roles = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pRol);
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0); 
            querySelect(pRol, utilQuery);
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pRol);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pRol, utilQuery);
                obtenerDatos(ps, roles);
                ps.close();
            } 
            catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return roles;
    }
}