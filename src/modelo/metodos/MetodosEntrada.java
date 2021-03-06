package modelo.metodos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import controlador.ConexionManager;
import modelo.POJOs.Entrada;

public class MetodosEntrada {

	/**
	 * Declaramos map en el que se van a cargar las entradas vendidas
	 */
	HashMap <Integer, Entrada> mapEntradasVendidas=new HashMap <Integer, Entrada>();
	
	public void cargarEntradas() throws SQLException {
		
		// 1.Declaramos map y variables
		HashMap <Integer, Entrada> mapSoldTickets=new HashMap<Integer, Entrada>();
		int idEntrada;
		String idSala;
		double precioEntrada;
		int idPelicula;
		String idCine;
		String idCompania;
		String idUsuario;
		Entrada e;
		
		// 2. Creamos la conexion: Instanciamos objeto de ConexionManager e invocamos el metodo crear()
		ConexionManager conexionManager = new ConexionManager();
		Connection conexion = conexionManager.crear();
		
		//3. Creamos el statement
		try {
			PreparedStatement consulta = conexion
					.prepareStatement("SELECT IDENTRADA, ENTRADA_IDSALA, PRECIOENTRADA, ENTRADA_IDPELICULA, CINE_NOMBRECINE, CINE_COMPANIA_NOMBRECOMPAGNIA, CUENTA_USUARIO_NOMBREACCESO FROM ENTRADA");
			// 2.2.Preparamos el ResultSet
			ResultSet resultado = consulta.executeQuery();
			// 2.3.Iteramos sobre las tuplas de la base de datos
			while (resultado.next()) {
				idEntrada = resultado.getInt("IDENTRADA");
				idSala=resultado.getString("ENTRADA_IDSALA");
				precioEntrada=resultado.getDouble("PRECIOENTRADA");
				idPelicula=resultado.getInt("ENTRADA_IDPELICULA");
				idCine=resultado.getString("CINE_NOMBRECINE");
				idCompania=resultado.getString("CINE_COMPANIA_NOMBRECOMPAGNIA");
				idUsuario=resultado.getString("CUENTA_USUARIO_NOMBREACCESO");
				e=new Entrada(idEntrada, idSala, precioEntrada, idPelicula, idCine, idUsuario);
				mapSoldTickets.put(idEntrada,e);
			}
		} catch (Exception e1) {
			System.err.println("Excepcion no controlada en cargarEntradas");
			e1.printStackTrace();
		}
		
		// 3.Actualizamos map
		mapEntradasVendidas = mapSoldTickets;
		// 4.Cerramos la conexion
		conexionManager.cerrar();
	}
	
	public void crearEntrada(int idTicket, String idHall, double ticketPrice, int idMovie, String idCin, String idUser) throws SQLException {
		cargarEntradas();
		
		if (idHall==null || ticketPrice==0 || idMovie==0 || idCin==null || idUser==null) {
			JOptionPane.showMessageDialog(null, "Introduzca datos validos");
		}else if(mapEntradasVendidas.containsKey(idTicket)) {
			JOptionPane.showMessageDialog(null, "Ya existe una entrada con ese identificador");
		}
		
		
	}
}
