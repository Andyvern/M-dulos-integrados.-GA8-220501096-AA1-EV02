package modelo;

import controlador.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    ConexionBD conexion = new ConexionBD(); // Instancia de la conección a la base de datos
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List listar() {
        String sql = "select * from productos";
        List<Producto> lista = new ArrayList<>();

        try {
            con = conexion.ConectarBaseDeDAtos();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigo(rs.getInt(1));
                producto.setNombre(rs.getString(2));
                producto.setPrecio(rs.getDouble(3));
                producto.setInventario(rs.getInt(4));

                lista.add(producto);
            }
        }catch(SQLException e){
            System.out.println("Error al Listar: " + e);
        }
        
        return lista;
    }//Fin de la clase Listar
    
   //Metodo agregar
    public void Agregar(Producto producto){
         String sql = "INSERT INTO productos (nombre, precio, inventario) VALUES  (?,?,?)";
        try{
           con = conexion.ConectarBaseDeDAtos(); 
            ps = con.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getInventario());
            ps.executeUpdate(); 
        }catch(SQLException e){
            System.err.println("Error en el método Agregar clase ProductoDAO ");
        }
    }//Fin método agregar

}
