
package catalogoempresas.accesoadatos;

import java.util.*;
import java.sql.*;
import catalogoempresas.entidadesdenegocio.*;

public class EmpresaDAL {
    static String obtenerCampos() {
        return "e.Id, e.IdContacto, e.Nombre, e.Rubro, e.Categoria, e.Departamento";
    }
    
    private static String obtenerSelect(Empresa pEmpresa) {
        String sql;
        sql = "SELECT ";
        if (pEmpresa.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
             sql += "TOP " + pEmpresa.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Empresa e");
        return sql;
    }
    
    private static String agregarOrderBy(Empresa pEmpresa) {
        String sql = " ORDER BY e.Id DESC";
        if (pEmpresa.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            sql += " LIMIT " + pEmpresa.getTop_aux() + " ";
        }
        return sql;
    }
    
    public static int crear(Empresa pEmpresa) throws Exception {
        int result;
        String sql;
        
        try ( Connection conn = ComunDB.obtenerConexion();) {
            sql = "INSERT INTO Empresa(IdContacto,Nombre,Rubro,Categoria,Departamento) VALUES(?,?,?,?,?)";
            try ( PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pEmpresa.getIdContacto());
                ps.setString(2, pEmpresa.getNombre());
                ps.setString(3, pEmpresa.getRubro());
                ps.setString(4, pEmpresa.getCategoria());
                ps.setString(5, pEmpresa.getDepartamento());
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
    
    public static int modificar(Empresa pEmpresa) throws Exception {
        int result;
        String sql;
        try ( Connection conn = ComunDB.obtenerConexion();) {
            sql = "UPDATE Empresa SET IdContacto=?, Nombre=?, Rubro=?, Categoria=?, Departamento=? WHERE Id=?";
            try ( PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pEmpresa.getIdContacto());
                ps.setString(2, pEmpresa.getNombre());
                ps.setString(3, pEmpresa.getRubro());
                ps.setString(4, pEmpresa.getCategoria());
                ps.setString(5, pEmpresa.getDepartamento());
                ps.setInt(6, pEmpresa.getId());
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
    
    public static int eliminar(Empresa pEmpresa) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM Empresa WHERE Id=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pEmpresa.getId());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return result;
    }
    
    static int asignarDatosResultSet(Empresa pEmpresa, ResultSet pResultSet, int pIndex) throws Exception {
        pIndex++;
        pEmpresa.setId(pResultSet.getInt(pIndex)); 
        pIndex++;
        pEmpresa.setIdContacto(pResultSet.getInt(pIndex)); 
        pIndex++;
        pEmpresa.setNombre(pResultSet.getString(pIndex)); 
        pIndex++;
        pEmpresa.setRubro(pResultSet.getString(pIndex)); 
        pIndex++;
        pEmpresa.setCategoria(pResultSet.getString(pIndex)); 
        pIndex++;
        pEmpresa.setDepartamento(pResultSet.getString(pIndex)); 
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Empresa> pEmpresas) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) {
                Empresa empresa = new Empresa();
                asignarDatosResultSet(empresa, resultSet, 0);
                pEmpresas.add(empresa);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    private static void obtenerDatosIncluirContacto(PreparedStatement pPS, ArrayList<Empresa> pEmpresas) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            HashMap<Integer, Contacto> contactoMap = new HashMap(); 
            while (resultSet.next()) {
                Empresa empresa = new Empresa();
                int index = asignarDatosResultSet(empresa, resultSet, 0);
                if (contactoMap.containsKey(empresa.getIdContacto()) == false) {
                    Contacto contacto = new Contacto();
                    ContactoDAL.asignarDatosResultSet(contacto, resultSet, index);
                    contactoMap.put(contacto.getId(), contacto); 
                    empresa.setContacto(contacto); 
                } else {
                    empresa.setContacto(contactoMap.get(empresa.getIdContacto())); 
                }
                pEmpresas.add(empresa); 
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex; 
        }
    }
    
    public static Empresa obtenerPorId(Empresa pEmpresa) throws Exception {
        Empresa empresa = new Empresa();
        ArrayList<Empresa> empresas = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pEmpresa);
            sql += " WHERE e.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, pEmpresa.getId());
                obtenerDatos(ps, empresas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        if (empresas.size() > 0) {
            empresa = empresas.get(0);
        }
        return empresa;
    }
    
    public static ArrayList<Empresa> obtenerTodos() throws Exception {
        ArrayList<Empresa> empresas = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new Empresa()); 
            sql += agregarOrderBy(new Empresa());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                obtenerDatos(ps, empresas);
                ps.close();
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return empresas;
    }
    
    static void querySelect(Empresa pEmpresa, ComunDB.utilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (pEmpresa.getId() > 0) {
            pUtilQuery.AgregarNumWhere(" e.Id=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), pEmpresa.getId());
            }
        }

        if (pEmpresa.getIdContacto()> 0) {
            pUtilQuery.AgregarNumWhere(" e.IdContacto=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), pEmpresa.getIdContacto());
            }
        }
        
        if (pEmpresa.getNombre() != null && pEmpresa.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" e.Nombre LIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pEmpresa.getNombre() + "%");
            }
        }

        if (pEmpresa.getRubro()!= null && pEmpresa.getRubro().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" e.Rubro LIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + pEmpresa.getRubro()+ "%");
            }
        }

        if (pEmpresa.getCategoria()!= null && pEmpresa.getCategoria().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" e.Categoria=? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), pEmpresa.getCategoria());
            }
        }

        if (pEmpresa.getDepartamento()!= null && pEmpresa.getDepartamento().trim().isEmpty() == false) {
            pUtilQuery.AgregarNumWhere(" e.Departamento=? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), pEmpresa.getDepartamento());
            }
        }
    }
    
    public static ArrayList<Empresa> buscar(Empresa pEmpresa) throws Exception {
        ArrayList<Empresa> empresas = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(pEmpresa);
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0);
            querySelect(pEmpresa, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pEmpresa);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pEmpresa, utilQuery);
                obtenerDatos(ps, empresas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return empresas;
    }
    
    public static ArrayList<Empresa> buscarIncluirContacto(Empresa pEmpresa) throws Exception {
        ArrayList<Empresa> empresas = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = "SELECT ";
            if (pEmpresa.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pEmpresa.getTop_aux() + " "; 
            }
            sql += obtenerCampos();
            sql += ",";
            sql += ContactoDAL.obtenerCampos();
            sql += " FROM Empresa e";
            sql += " JOIN Contacto c on (e.IdContacto=c.Id)";
            ComunDB comundb = new ComunDB();
            ComunDB.utilQuery utilQuery = comundb.new utilQuery(sql, null, 0);
            querySelect(pEmpresa, utilQuery);
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pEmpresa);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pEmpresa, utilQuery);
                obtenerDatosIncluirContacto(ps, empresas);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return empresas;
    }
}
