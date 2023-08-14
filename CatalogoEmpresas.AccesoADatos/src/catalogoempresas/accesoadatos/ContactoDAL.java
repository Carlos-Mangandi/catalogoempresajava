
package catalogoempresas.accesoadatos;

import java.util.*;
import java.sql.*;
import catalogoempresas.entidadesdenegocio.*;

public class ContactoDAL {
    static String obtenerCampos() {
        return "c.Id, c.Nombre, c.Email, c.Telefono, c.Celular";
    }
    
    private static String obtenerSelect(Contacto pContacto) {
        String sql;
        sql = "SELECT ";
        if (pContacto.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {            
            sql += "TOP " + pContacto.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Contacto c");
        return sql;
    }
    
    private static String agregarOrderBy(Contacto pContacto) {
        String sql = " ORDER BY c.Id DESC";
        if (pContacto.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            sql += " LIMIT " + pContacto.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Contacto pContacto) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO Contacto(Nombre, Email, Telefono, Celular) VALUES(?, ?, ?, ?)";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, pContacto.getNombre());
                ps.setString(2, pContacto.getEmail());
                ps.setString(3, pContacto.getTelefono());
                ps.setString(4, pContacto.getCelular());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    public static int modificar(Contacto pContacto) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "UPDATE Contacto SET Nombre=?, Email = ?, Telefono = ?, Celular = ? WHERE Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, pContacto.getNombre());
                ps.setString(2, pContacto.getEmail());
                ps.setString(3, pContacto.getTelefono());
                ps.setString(4, pContacto.getCelular());
                ps.setInt(5, pContacto.getId());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    public static int eliminar(Contacto pContacto) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "DELETE FROM Contacto WHERE Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pContacto.getId());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    static int asignarDatosResultSet(Contacto pContacto, ResultSet pResultSet, int pIndex) throws Exception {
        pIndex++;
        pContacto.setId(pResultSet.getInt(pIndex));
        pIndex++;
        pContacto.setNombre(pResultSet.getString(pIndex));
        pIndex++;
        pContacto.setEmail(pResultSet.getString(pIndex));
        pIndex++;
        pContacto.setTelefono(pResultSet.getString(pIndex));
        pIndex++;
        pContacto.setCelular(pResultSet.getString(pIndex));
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Contacto> pContactos) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            while (resultSet.next()) {
                Contacto contacto = new Contacto(); 
                asignarDatosResultSet(contacto, resultSet, 0);
                pContactos.add(contacto);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public static Contacto obtenerPorId(Contacto pContacto) throws Exception {
        Contacto contacto = new Contacto();
        ArrayList<Contacto> contactos = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pContacto);
            sql += " WHERE c.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pContacto.getId());
                obtenerDatos(ps, contactos);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        if (contactos.size() > 0) {
            contacto = contactos.get(0);
        }
        return contacto;
    }
    
    public static ArrayList<Contacto> obtenerTodos() throws Exception {
        ArrayList<Contacto> contactos = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new Contacto());
            sql += agregarOrderBy(new Contacto());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                obtenerDatos(ps, contactos);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return contactos;
    }
    
    static void querySelect(Contacto pContacto, ComunDB.utilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pContacto.getId() > 0) {
            pUtilQuery.AgregarNumWhere(" c.Id=? ");
            if (statement != null) { 
                statement.setInt(pUtilQuery.getNumWhere(), pContacto.getId()); 
            }
        }

        if (pContacto.getNombre() != null && pContacto.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" c.Nombre LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pContacto.getNombre() + "%"); 
            }
        }
        
        if (pContacto.getEmail()!= null && pContacto.getEmail().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" c.Email LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pContacto.getEmail()+ "%"); 
            }
        }
        
        if (pContacto.getTelefono()!= null && pContacto.getTelefono().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" c.Telefono LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pContacto.getTelefono()+ "%"); 
            }
        }
        
        if (pContacto.getCelular()!= null && pContacto.getCelular().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" c.Celular LIKE ? "); 
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pContacto.getCelular()+ "%"); 
            }
        }
    }
    
    public static ArrayList<Contacto> buscar(Contacto pContacto) throws Exception {
        ArrayList<Contacto> contactos = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pContacto);
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0); 
            querySelect(pContacto, utilQuery);
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pContacto);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pContacto, utilQuery);
                obtenerDatos(ps, contactos);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return contactos;
    }
}
