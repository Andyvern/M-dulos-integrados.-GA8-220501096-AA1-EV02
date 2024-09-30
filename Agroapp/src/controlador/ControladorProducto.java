package controlador;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;
import modelo.ProductoDAO;
import vista.Interfaz;

public class ControladorProducto implements ActionListener {
//Variables globales

    private int codigo;
    private String nombre;
    private double precio;
    private int inventario;

    //Instancias
    Producto producto = new Producto();
    ProductoDAO productoDAO = new ProductoDAO();
    Interfaz vista = new Interfaz();
    DefaultTableModel modeloTabla = new DefaultTableModel();

    public ControladorProducto(Interfaz _vista) {
        this.vista = _vista;
        vista.setVisible(true);
        AgregarEventos();
        listarTabla();
    }

    private void listarTabla() {
        String[] titulos = new String[]{"Codigo", "Nombre", "Precio", "Inventario"};
        modeloTabla = new DefaultTableModel(titulos, 0);
        List<Producto> listaProductos = productoDAO.listar();
        for (Producto producto : listaProductos) {
            modeloTabla.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getInventario()
            });
        }
        vista.getTblTablaProductos().setModel(modeloTabla);
        vista.getTblTablaProductos().setPreferredSize(new Dimension(350, modeloTabla.getRowCount() * 16));

    }

    private boolean cargarDatos() {
        try {
            nombre = vista.getTxtNombre().getText();
            precio = Double.parseDouble(vista.getTxtPrecio().getText());
            inventario = Integer.parseInt(vista.getTxtInventario().getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Los campos precio e inventario debe ser númericos",
                    "error", JOptionPane.ERROR);
            System.out.println("Error al cargar los Datos " + e);
            return false;
        }
    }

    private void AgregarEventos() {
        //Acciones cuando el botón sea oprimido
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getTblTablaProductos().addMouseListener(new MouseAdapter() {
            //Creamos el método
            public void mouseClicked(MouseEvent e) {
                llenarCampos(e);
            }

        });
    }

    private void llenarCampos(MouseEvent e) {
        JTable target = (JTable) e.getSource();
        vista.getTxtNombre()
                .setText(vista.getTblTablaProductos().getModel()
                        .getValueAt(target.getSelectedRow(), 1)
                        .toString());  // para el nombre
        vista.getTxtPrecio()
                .setText(vista.getTblTablaProductos().getModel()
                        .getValueAt(target.getSelectedRow(), 2)
                        .toString());  // para el precio
        vista.getTxtInventario()
                .setText(vista.getTblTablaProductos().getModel()
                        .getValueAt(target.getSelectedRow(), 3)
                        .toString());  // para el inventario
    }

    private boolean validarDatos() {
        if ("".equals(vista.getTxtNombre().getText())
                || "".equals(vista.getTxtPrecio().getText())
                || "".equals(vista.getTxtInventario().getText())) {
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtInventario().setText("");
        codigo = 0;
        nombre = "";
        precio = 0;
        inventario = 0;
    }

    
    private void AgregarProducto(){
        try{
            if(validarDatos()){
                if(cargarDatos()){
                    Producto producto = new Producto(nombre, precio, inventario);
                    productoDAO.Agregar(producto);
                    JOptionPane.showMessageDialog(null, "Registro agregado con éxito");
                    limpiarCampos();
                }
            }            
        }catch(Exception e){
            System.err.println("Error al agregar(C)");
        }finally{
            listarTabla();
        }
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == vista.getBtnAgregar()) {
            AgregarProducto();
        }

    }
}
