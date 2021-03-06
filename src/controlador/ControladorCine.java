package controlador;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ConcurrentModificationException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import modelo.POJOs.CalificacionEdades;
import modelo.POJOs.Compagnia;
import modelo.POJOs.GeneroPelicula;
import modelo.POJOs.IdiomaOriginal;
import modelo.POJOs.Pais;
import modelo.POJOs.Pelicula;
import modelo.POJOs.ValoracionPeli;
import modelo.metodos.MetodosArtistas;
import modelo.metodos.MetodosCine;
import modelo.metodos.MetodosFuncion;
import modelo.metodos.MetodosPelicula;
import modelo.metodos.MetodosPromocion;
import modelo.metodos.MetodosSala;
import vista.PantallaAdministrador;

public class ControladorCine implements ActionListener, MouseListener {

	// Instanciamos vista PantallaAdministrador
	public PantallaAdministrador pantallaAdministrador = new PantallaAdministrador();
	// Instanciamos modelos
	public MetodosFuncion metodosFuncion = new MetodosFuncion();
	public MetodosCine metodosCine = new MetodosCine();
	public MetodosPromocion metodosPromocion = new MetodosPromocion();
	public MetodosSala metodosSala = new MetodosSala();
	public MetodosPelicula metodosPelicula = new MetodosPelicula();
	public MetodosArtistas metodosArtistas = new MetodosArtistas();
	// Declaracion de variables
	private String now = "2016-11-09 10:30";
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	private LocalDateTime formatDateTime;
	private String nombreCineA;
	private String oldPromoDiscount;// Fijate Antonio
	private String oldNombreSal;
	private String idPelicula;
	// Declaramos en un enum las acciones relacionadas con el Administrador

	public enum accionesAdministrador {// Fijate Antonio
		ELIMINAR_CINE, ANIADIR_CINE, MODIFICAR_CINE, ELEGIR_CINE, ELIMINAR_FUNCION, ELIMINAR_FUNCION_CINE, ANIADIR_FUNCION, ANIADIR_FUNCION_CINE, MODIFICAR_FUNCION, // Este
		ANIADIR_ARTISTA, ELIMINAR_ARTISTA, MODIFICAR_ARTISTA, CREAR_PROMOCION, MODIFICAR_PROMOCION, ELIMINAR_PROMOCION, ANIADIR_SALA, MODIFICAR_SALA, ELIMINAR_SALAS_CINE, INSERTAR_DIRECTOR_MODIFICAR, INSERTAR_ACTOR_MODIFICAR, INSERTAR_ACTOR_ANIADIR, INSERTAR_DIRECTOR_ANIADIR, CREAR_PELICULA, MODIFICAR_PELICULA, ELIMINAR_PELICULA, ANIADIR_ACTOR, ANIADIR_DIRECTOR,
	}

	// CONSTRUCTOR DE CLASE
	public ControladorCine(PantallaAdministrador pantallaAdministrador) {
		super();
		this.pantallaAdministrador = pantallaAdministrador;
	}

	// INICIAMOS
	public void Iniciar() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(pantallaAdministrador);
			pantallaAdministrador.setVisible(true);
			pantallaAdministrador.setLocationRelativeTo(null);
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		}

		// Declaramos las acciones y aniadimos las escuchas al evento producido por el
		// componente
		// Artistas
		this.pantallaAdministrador.jTable1.addMouseListener(this);
		this.pantallaAdministrador.jTable1.setModel(new DefaultTableModel());

		pantallaAdministrador.jComboBox13.removeAllItems();

		this.pantallaAdministrador.jTable4.addMouseListener(this);
		this.pantallaAdministrador.jTable4.setModel(new DefaultTableModel());

		// Funcion
		metodosCine.cogerTodosLosCineBBDD();
		metodosFuncion.cogerTodasLasFuncionesBBDD();

		metodosArtistas.devolverArtistasArray();

		this.pantallaAdministrador.jTable12.addMouseListener(this);
		this.pantallaAdministrador.jTable12.setModel(new DefaultTableModel());

		this.pantallaAdministrador.jTable2.addMouseListener(this);
		this.pantallaAdministrador.jTable2.setModel(new DefaultTableModel());

		this.pantallaAdministrador.jTable8.addMouseListener(this);
		this.pantallaAdministrador.jTable8.setModel(new DefaultTableModel());

		this.pantallaAdministrador.jTable6.addMouseListener(this);
		this.pantallaAdministrador.jTable6.setModel(new DefaultTableModel());

		this.pantallaAdministrador.jTable11.addMouseListener(this);
		this.pantallaAdministrador.jTable11.setModel(new DefaultTableModel());
		// Cine
		this.pantallaAdministrador.jTable7.addMouseListener(this);
		this.pantallaAdministrador.jTable7.setModel(new DefaultTableModel());

		this.pantallaAdministrador.jTable9.addMouseListener(this);
		this.pantallaAdministrador.jTable9.setModel(new DefaultTableModel());
		// INSERTAR ACTOR
		this.pantallaAdministrador.jButton24.setActionCommand("INSERTAR_ACTOR");
		this.pantallaAdministrador.jButton24.addActionListener(this);

		// INSERTAR DIRECTOR
		this.pantallaAdministrador.jButton23.setActionCommand("INSERTAR_DIRECTOR");
		this.pantallaAdministrador.jButton23.addActionListener(this);

		// MDOFICAR ARTISTA
		this.pantallaAdministrador.jButton16.setActionCommand("MODIFICAR_ARTISTA");
		this.pantallaAdministrador.jButton16.addActionListener(this);

		// Eliminar Artista
		this.pantallaAdministrador.jButton19.setActionCommand("ELIMINAR_ARTISTA");
		this.pantallaAdministrador.jButton19.addActionListener(this);
		// ANIADIR ARTISTA
		this.pantallaAdministrador.jButton26.setActionCommand("ANIADIR_ARTISTA");
		this.pantallaAdministrador.jButton26.addActionListener(this);
		// INSERTAR ARTISTA MODIFICAR
		this.pantallaAdministrador.jButton28.setActionCommand("INSERTAR_ACTOR_MODIFICAR");
		this.pantallaAdministrador.jButton28.addActionListener(this);

		this.pantallaAdministrador.jButton27.setActionCommand("INSERTAR_DIRECTOR_MODIFICAR");
		this.pantallaAdministrador.jButton27.addActionListener(this);

		// INSERTAR ARTISTA ANIADIR
		this.pantallaAdministrador.jButton24.setActionCommand("INSERTAR_ACTOR_ANIADIR");
		this.pantallaAdministrador.jButton24.addActionListener(this);

		this.pantallaAdministrador.jButton23.setActionCommand("INSERTAR_DIRECTOR_ANIADIR");
		this.pantallaAdministrador.jButton23.addActionListener(this);

		// Eliminar cine
		this.pantallaAdministrador.jButton4.setActionCommand("ELIMINAR_CINE");
		this.pantallaAdministrador.jButton4.addActionListener(this);

		// Aniadir Cine
		this.pantallaAdministrador.jButton3.setActionCommand("ANIADIR_CINE");
		this.pantallaAdministrador.jButton3.addActionListener(this);

		// Elegir Cine
		this.pantallaAdministrador.jButton2.setActionCommand("ELEGIR_CINE");
		this.pantallaAdministrador.jButton2.addActionListener(this);

		// Modificar Cine
		this.pantallaAdministrador.jButton6.setActionCommand("MODIFICAR_CINE");
		this.pantallaAdministrador.jButton6.addActionListener(this);
		// ELIMINAR FUNCION CINE
		this.pantallaAdministrador.jButton10.setActionCommand("ELIMINAR_FUNCION_CINE");
		this.pantallaAdministrador.jButton10.addActionListener(this);
		// ANIADIR FUNCION CINE
		this.pantallaAdministrador.jButton5.setActionCommand("ANIADIR_FUNCION_CINE");
		this.pantallaAdministrador.jButton5.addActionListener(this);
		// ELIMINAR FUNCION
		this.pantallaAdministrador.jButton22.setActionCommand("ELIMINAR_FUNCION");
		this.pantallaAdministrador.jButton22.addActionListener(this);
		// Aniadir Funcion
		this.pantallaAdministrador.jButton30.setActionCommand("ANIADIR_FUNCION");
		this.pantallaAdministrador.jButton30.addActionListener(this);
		// modificar funcion
		this.pantallaAdministrador.jButton21.setActionCommand("MODIFICAR_FUNCION");
		this.pantallaAdministrador.jButton21.addActionListener(this);
		// Crear promocion
		pantallaAdministrador.botonAniadirPromocion.setActionCommand("CREAR_PROMOCION");
		pantallaAdministrador.botonAniadirPromocion.addActionListener(this);
		pantallaAdministrador.botonAniadirPromocion.addMouseListener(this);

		// Modificar promocion
		pantallaAdministrador.tablaModificarPromocion.addMouseListener(this);// Fijate Antonio
		pantallaAdministrador.tablaModificarPromocion.setModel(metodosPromocion.generarTablaPromociones());
		pantallaAdministrador.botonModificarPromocion.setActionCommand("MODIFICAR_PROMOCION");
		pantallaAdministrador.botonModificarPromocion.addActionListener(this);
		pantallaAdministrador.botonModificarPromocion.addMouseListener(this);

		// Eliminar promocion
		pantallaAdministrador.tablaEliminarPromocion.addMouseListener(this);
		pantallaAdministrador.tablaEliminarPromocion.setModel(metodosPromocion.generarTablaPromociones());
		pantallaAdministrador.botonEliminarPromocion.setActionCommand("ELIMINAR_PROMOCION");
		pantallaAdministrador.botonEliminarPromocion.addActionListener(this);
		pantallaAdministrador.botonEliminarPromocion.addMouseListener(this);

		// Aniadir sala
		pantallaAdministrador.botonAniadirSala.setActionCommand("ANIADIR_SALA");
		pantallaAdministrador.botonAniadirSala.addActionListener(this);
		pantallaAdministrador.botonAniadirSala.addMouseListener(this);

		// Modificar sala
		pantallaAdministrador.tablaSalasmodificarCine.addMouseListener(this);
		pantallaAdministrador.tablaSalasmodificarCine.setModel(metodosSala.generarTablaSalas());
		pantallaAdministrador.botonModificarSala.setActionCommand("MODIFICAR_SALA");
		pantallaAdministrador.botonModificarSala.addActionListener(this);
		pantallaAdministrador.botonModificarSala.addMouseListener(this);

		// Eliminar salas cine (por eliminacion de cine)

		// TABLAS ALE
		//
		pantallaAdministrador.jTable7.setModel(metodosCine.cogerCineBBDDTodo());
		pantallaAdministrador.jTable9.setModel(metodosCine.cogerCineBBDDTodo());
		// coger las funciones del cine no todas
		pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
		pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
		pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
		pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

		// Artistas
		pantallaAdministrador.jTable1.setModel(metodosArtistas.cogerArtistaBBDDTodo());
		pantallaAdministrador.jTable4.setModel(metodosArtistas.cogerArtistaBBDDTodo());

		// Crear pelicula
		pantallaAdministrador.jButton25.setActionCommand("CREAR_PELICULA");
		pantallaAdministrador.jButton25.addActionListener(this);
		pantallaAdministrador.jButton25.addMouseListener(this);

		// Modificar pelicula
		pantallaAdministrador.jTable13.addMouseListener(this);
		pantallaAdministrador.jTable13.setModel(metodosPelicula.generarTablaPeliculas());
		pantallaAdministrador.jButton29.setActionCommand("MODIFICAR_PELICULA");
		pantallaAdministrador.jButton29.addActionListener(this);
		pantallaAdministrador.jButton29.addMouseListener(this);

		// Eliminar pelicula
		pantallaAdministrador.jTable3.addMouseListener(this);
		pantallaAdministrador.jTable3.setModel(metodosPelicula.generarTablaPeliculas());
		pantallaAdministrador.jButton31.setActionCommand("ELIMINAR_PELICULA");
		pantallaAdministrador.jButton31.addActionListener(this);
		pantallaAdministrador.jButton31.addMouseListener(this);
		System.out.println("dwaaaaaaa" + MetodosFuncion.Funciones.size());
		// para introducir items en los jcombobox
		for (GeneroPelicula a : GeneroPelicula.values()) {
			pantallaAdministrador.jComboBox1.addItem(a.name());
			pantallaAdministrador.jComboBox6.addItem(a.name());
		}

		for (CalificacionEdades a : CalificacionEdades.values()) {
			pantallaAdministrador.jComboBox3.addItem(a.name());
			pantallaAdministrador.jComboBox8.addItem(a.name());
		}
		for (IdiomaOriginal a : IdiomaOriginal.values()) {
			pantallaAdministrador.jComboBox2.addItem(a.name());
			pantallaAdministrador.jComboBox7.addItem(a.name());
		}
		for (Pais a : Pais.values()) {
			pantallaAdministrador.jComboBox11.addItem(a.name());
			pantallaAdministrador.jComboBox4.addItem(a.name());
			pantallaAdministrador.jComboBox5.addItem(a.name());
			pantallaAdministrador.jComboBox12.addItem(a.name());
			// Artistas
			pantallaAdministrador.jComboBox13.addItem(a.name());
			pantallaAdministrador.jComboBox4.addItem(a.name());
			pantallaAdministrador.jComboBox5.addItem(a.name());
			pantallaAdministrador.jComboBox9.addItem(a.name());
			pantallaAdministrador.jComboBox10.addItem(a.name());
			pantallaAdministrador.jComboBox14.addItem(a.name());

		}
		for (ValoracionPeli a : ValoracionPeli.values()) {

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (accionesAdministrador.valueOf(e.getActionCommand())) {
		case ANIADIR_CINE:
			int numero = 0;
			double precioBase = 0;
			String nombreCine = pantallaAdministrador.jTextField1.getText();
			System.out.println(nombreCine);
			String direccion = pantallaAdministrador.jTextField2.getText();
			if (pantallaAdministrador.jTextField3.getText().length() == 0) {
				numero = 0;
			} else {
				numero = Integer.parseInt(pantallaAdministrador.jTextField3.getText());
			}
			if (pantallaAdministrador.jTextField4.getText().length() == 0) {
				precioBase = 0;
			} else {
				precioBase = Double.parseDouble(pantallaAdministrador.jTextField4.getText());
			}

			metodosCine.crearCine(nombreCine, direccion, numero, precioBase);
			pantallaAdministrador.jTable7.setModel(metodosCine.cogerCineBBDDTodo());
			pantallaAdministrador.jTable9.setModel(metodosCine.cogerCineBBDDTodo());
			break;
		case ELIMINAR_CINE:
			metodosCine.eliminarCine(nombreCineA);
			pantallaAdministrador.jTable7.setModel(metodosCine.cogerCineBBDDTodo());
			pantallaAdministrador.jTable9.setModel(metodosCine.cogerCineBBDDTodo());
			break;
		case INSERTAR_ACTOR_MODIFICAR:

			metodosArtistas.insertarArtistaActorBBDD(pantallaAdministrador.jTextField47.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField50.getText()));

			break;
		case INSERTAR_DIRECTOR_MODIFICAR:
			metodosArtistas.insertarArtistaDirectorBBDD(pantallaAdministrador.jTextField46.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField50.getText()));

			break;
		case INSERTAR_DIRECTOR_ANIADIR:
			metodosArtistas.insertarArtistaDirectorBBDD(pantallaAdministrador.jTextField36.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField48.getText()));

			break;
		case INSERTAR_ACTOR_ANIADIR:
			metodosArtistas.insertarArtistaActorBBDD(pantallaAdministrador.jTextField37.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField48.getText()));

			break;
		case ANIADIR_ARTISTA:
			metodosArtistas.CrearArtista(pantallaAdministrador.jTextField41.getText(),
					pantallaAdministrador.jComboBox14.getSelectedItem().toString());
			pantallaAdministrador.jTable1.setModel(metodosArtistas.cogerArtistaBBDDTodo());
			pantallaAdministrador.jTable4.setModel(metodosArtistas.cogerArtistaBBDDTodo());

			break;
		case MODIFICAR_ARTISTA:
			metodosArtistas.modificarArtista(pantallaAdministrador.jLabel107.getText(),
					pantallaAdministrador.jTextField17.getText(), pantallaAdministrador.jLabel109.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField18.getText()));
			pantallaAdministrador.jTable1.setModel(metodosArtistas.cogerArtistaBBDDTodo());
			pantallaAdministrador.jTable4.setModel(metodosArtistas.cogerArtistaBBDDTodo());

			break;
		case ELIMINAR_ARTISTA:
			metodosArtistas.eliminarArtista(pantallaAdministrador.jLabel106.getText());
			pantallaAdministrador.jTable1.setModel(metodosArtistas.cogerArtistaBBDDTodo());
			pantallaAdministrador.jTable4.setModel(metodosArtistas.cogerArtistaBBDDTodo());

			break;
		case MODIFICAR_CINE:

			metodosCine.actualizarCineBBDD(pantallaAdministrador.jLabel91.getText(),
					pantallaAdministrador.jTextField11.getText(), pantallaAdministrador.jTextField12.getText(),
					Integer.parseInt(pantallaAdministrador.jTextField5.getText()),
					Double.parseDouble(pantallaAdministrador.jTextField8.getText()));
			pantallaAdministrador.jTable7.setModel(metodosCine.cogerCineBBDDTodo());
			pantallaAdministrador.jTable9.setModel(metodosCine.cogerCineBBDDTodo());
			break;
		case ANIADIR_FUNCION:
			// "2016-11-09 10:30" tengo que poner este formato
			try {
				metodosSala.cargarSalas();
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				metodosPromocion.cargarPromociones();
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			try {
				metodosPelicula.cargarPeliculas();
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			now = pantallaAdministrador.jTextField19.getText();
			formatDateTime = LocalDateTime.parse(now, formatter);
			// pantallaAdministrador.jTextField20.getText() Sala
			// pantallaAdministrador.jTextField21.getText() Pelicula
			// pantallaAdministrador.jTextField22.getText() Promocion
			metodosFuncion.crearFuncion(formatDateTime,
					MetodosSala.salas.get(pantallaAdministrador.jTextField20.getText()),
					MetodosPelicula.peliculas.get(Integer.parseInt(pantallaAdministrador.jTextField21.getText())), null,
					MetodosPromocion.mapPromocionesCreadas
							.get(Integer.parseInt(pantallaAdministrador.jTextField22.getText())));
			pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
			pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

			break;
		case ELIMINAR_FUNCION:
			now = pantallaAdministrador.jTextField27.getText();

			formatDateTime = LocalDateTime.parse(now, formatter);
			metodosFuncion.eliminarFuncion(formatDateTime);
			pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
			pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

			break;
		case MODIFICAR_FUNCION:
			now = pantallaAdministrador.jTextField23.getText();

			formatDateTime = LocalDateTime.parse(now, formatter);

			String antiguo = pantallaAdministrador.jLabel92.getText();

			LocalDateTime formatDateTimeAntiguo = LocalDateTime.parse(antiguo, formatter);
			metodosFuncion.modificarFuncion(formatDateTimeAntiguo, formatDateTime,
					metodosSala.salas.get(pantallaAdministrador.jTextField24.getText()),
					metodosPelicula.peliculas.get(Integer.parseInt(pantallaAdministrador.jTextField25.getText())),
					MetodosPromocion.mapPromocionesCreadas
							.get(Integer.parseInt(pantallaAdministrador.jTextField26.getText())),
					Compagnia.listaCines.get(pantallaAdministrador.jLabel92.getText()));
			pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
			pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

			break;
		case ELIMINAR_FUNCION_CINE:
			now = pantallaAdministrador.jLabel102.getText();
			System.out.println(now);
			formatDateTime = LocalDateTime.parse(now, formatter);
			metodosFuncion.eliminarFuncionCine(formatDateTime);
			pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
			pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

			break;
		case ANIADIR_FUNCION_CINE:
			now = pantallaAdministrador.jLabel104.getText();
			System.out.println(now);
			formatDateTime = LocalDateTime.parse(now, formatter);
			metodosFuncion.actualizarFuncionCineBBDD(formatDateTime,
					Compagnia.listaCines.get(pantallaAdministrador.jLabel91.getText()));
			pantallaAdministrador.jTable6.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable8.setModel(metodosFuncion.cogerFuncionBBDDTodo());
			pantallaAdministrador.jTable11.setModel(metodosFuncion.cogerFuncionBBDDNombre());
			pantallaAdministrador.jTable12.setModel(metodosFuncion.cogerFuncionBBDDNombre());

			break;
		case CREAR_PROMOCION:
			try {
				String descripcionPromo = pantallaAdministrador.textoDescripcionPromocionAniadir.getText();
				int descuentoPromo = Integer.parseInt(pantallaAdministrador.textoDescuentoPromocionAniadir.getText());
				try {
					metodosPromocion.crearPromocion(descuentoPromo, descripcionPromo);
				} catch (SQLException e1) {
					System.err.println("Error SQL");
					e1.printStackTrace();
				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Debe introducir valores correctos");
			}
			try {
				pantallaAdministrador.textoDescripcionPromocionAniadir.setText("");
				pantallaAdministrador.textoDescuentoPromocionAniadir.setText("");
			} catch (Exception e1) {
				System.err.println("Excepcion no controlada");
				e1.printStackTrace();
			}
			break;
		case MODIFICAR_PROMOCION:// Fijate Antonio
			try {
				String descripcionPromo = pantallaAdministrador.textoDescripcionPromocionModificar.getText();
				int descuentoPromo = Integer.parseInt(pantallaAdministrador.textoDescuentoPromocionModificar.getText());
				int antiguoDescuentoPromo = Integer.parseInt(oldPromoDiscount);
				if (descripcionPromo != null && descuentoPromo != 0 && antiguoDescuentoPromo != 0) {
					metodosPromocion.modificarPromocion(antiguoDescuentoPromo, descripcionPromo, descuentoPromo);
				} else {
					JOptionPane.showMessageDialog(null, "Debe introducir datos validos");
				}
			} catch (NumberFormatException | SQLException e1) {
				System.err.println("Excepcion SQL no controlada");
				System.err.println("Excepcion NumberFormatException no controlada");
				e1.printStackTrace();
			}
			break;
		case ELIMINAR_PROMOCION:
			try {
				int promoDiscount = Integer.parseInt(oldPromoDiscount);
				if (promoDiscount != 0) {
					metodosPromocion.eliminarPromocion(promoDiscount);
				} else {
					JOptionPane.showMessageDialog(null, "La promocion ya no existe");
				}
			} catch (NumberFormatException | SQLException e1) {
				System.err.println("Excepcion SQL no controlada");
				System.err.println("Excepcion NumberFormatException no controlada en eliminar promocion");
				e1.printStackTrace();
			}
			break;
		case ANIADIR_SALA:

			try {
				String nombreCin = pantallaAdministrador.jTextField1.getText();
				String nombreSala = pantallaAdministrador.textoIdSalaCineAniadir.getText();
				int numeroButacas = Integer.parseInt(pantallaAdministrador.textoNumeroButacasAniadir.getText());
				if (nombreCin == null || nombreSala == null || numeroButacas == 0) {
					JOptionPane.showMessageDialog(null,
							"Debe introducir datos validos. Recuerde escribir el nombre del cine al que desea aniadir la sala.");
				} else {

					numeroButacas = Integer.parseInt(pantallaAdministrador.textoNumeroButacasAniadir.getText());
					metodosSala.aniadirSala(nombreCin, nombreSala, numeroButacas);
					pantallaAdministrador.textoIdSalaCineAniadir.setText("");
					pantallaAdministrador.textoNumeroButacasAniadir.setText("0");
				}
			} catch (NumberFormatException | HeadlessException | SQLException e3) {

			}
			break;
		case MODIFICAR_SALA:

			try {
				String oldNombreSala = oldNombreSal;
				System.out.println(oldNombreSala);
				String nombreCinema = pantallaAdministrador.jTextField11.getText();
				System.out.println(nombreCinema);
				String nombreSal = pantallaAdministrador.textoIdSalaCine.getText();
				System.out.println(nombreSal);
				int seatsNumber = Integer.parseInt(pantallaAdministrador.textoNumeroButacas.getText());
				if (oldNombreSala == null || nombreCinema == null || nombreSal == null || seatsNumber == 0) {
					JOptionPane.showMessageDialog(null, "Debe introducir datos validos");
				} else {

					metodosSala.modificarSala(oldNombreSala, nombreCinema, nombreSal, seatsNumber);
				}
			} catch (HeadlessException | NumberFormatException | SQLException e2) {

			}
			break;
		case CREAR_PELICULA:
			try {
				int agnoProduccion = Integer.parseInt(pantallaAdministrador.jTextField28.getText());
				String tituloDistribucion = pantallaAdministrador.jTextField29.getText();
				String tituloOriginal = pantallaAdministrador.jTextField30.getText();
				String genero = pantallaAdministrador.jComboBox1.getSelectedItem().toString();
				String idioma = pantallaAdministrador.jComboBox2.getSelectedItem().toString();
				boolean subtitulosEs = pantallaAdministrador.jCheckBox1.isSelected();
				String paisOrigen = pantallaAdministrador.jComboBox11.getSelectedItem().toString();
				String sitioWeb = pantallaAdministrador.jTextField32.getText();
				Duration duracionPelicula = Duration
						.ofMinutes(Integer.parseInt(pantallaAdministrador.jTextField33.getText()));
				String calificacionEdades = pantallaAdministrador.jComboBox3.getSelectedItem().toString();
				LocalDate fechaEstrenoEs = LocalDate.parse(pantallaAdministrador.jTextField49.getText(),
						DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String resumen = pantallaAdministrador.jTextField35.getText();
				int idPelicula = Integer.parseInt(pantallaAdministrador.jTextField34.getText());

				metodosPelicula.agnadirPelicula(agnoProduccion, tituloDistribucion, tituloOriginal, genero, idioma,
						subtitulosEs, paisOrigen, sitioWeb, duracionPelicula, calificacionEdades, fechaEstrenoEs,
						resumen, idPelicula);

				pantallaAdministrador.jTextField48.setText(String.valueOf(idPelicula));
				pantallaAdministrador.jTextField28.setText("");
				pantallaAdministrador.jTextField29.setText("");
				pantallaAdministrador.jTextField30.setText("");
				pantallaAdministrador.jTextField32.setText("");
				pantallaAdministrador.jTextField33.setText("");
				pantallaAdministrador.jTextField49.setText("");
				pantallaAdministrador.jTextField35.setText("");
				pantallaAdministrador.jTextField34.setText("");

				pantallaAdministrador.jTable3.setModel(metodosPelicula.generarTablaPeliculas());
				pantallaAdministrador.jTable13.setModel(metodosPelicula.generarTablaPeliculas());

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Debe introducir valores correctos");
			} catch (SQLException e1) {
				System.err.println("Error SQL");
				e1.printStackTrace();
			}
			break;
		case MODIFICAR_PELICULA:
			try {

				int agnoProduccion = Integer.parseInt(pantallaAdministrador.jTextField38.getText());
				String tituloDistribucion = pantallaAdministrador.jTextField39.getText();
				String tituloOriginal = pantallaAdministrador.jTextField40.getText();
				String genero = pantallaAdministrador.jComboBox6.getSelectedItem().toString();
				String idioma = pantallaAdministrador.jComboBox7.getSelectedItem().toString();
				boolean subtitulosEs = pantallaAdministrador.jCheckBox2.isSelected();
				String paisOrigen = pantallaAdministrador.jComboBox12.getSelectedItem().toString();
				String sitioWeb = pantallaAdministrador.jTextField42.getText();
				Duration duracionPelicula = Duration
						.ofMinutes(Integer.parseInt(pantallaAdministrador.jTextField43.getText()));
				String calificacionEdades = pantallaAdministrador.jComboBox8.getSelectedItem().toString();
				LocalDate fechaEstrenoEs = LocalDate.parse(pantallaAdministrador.jTextField44.getText(),
						DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String resumen = pantallaAdministrador.jTextField45.getText();
				int id = Integer.parseInt(idPelicula);

				metodosPelicula.modificarPelicula(id, agnoProduccion, tituloDistribucion, tituloOriginal, genero,
						idioma, subtitulosEs, paisOrigen, sitioWeb, duracionPelicula, calificacionEdades,
						fechaEstrenoEs, resumen);

				pantallaAdministrador.jTextField38.setText("");
				pantallaAdministrador.jTextField39.setText("");
				pantallaAdministrador.jTextField40.setText("");
				pantallaAdministrador.jTextField42.setText("");
				pantallaAdministrador.jTextField43.setText("");
				pantallaAdministrador.jTextField44.setText("");
				pantallaAdministrador.jTextField45.setText("");
				pantallaAdministrador.jTextField50.setText("");

				pantallaAdministrador.jTable13.setModel(metodosPelicula.generarTablaPeliculas());
				pantallaAdministrador.jTable3.setModel(metodosPelicula.generarTablaPeliculas());

			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Debe introducir valores correctos");
			} catch (SQLException e1) {
				System.err.println("Error SQL");
				e1.printStackTrace();
			}
			break;
		case ELIMINAR_PELICULA:
			try {
				int id = Integer.parseInt(idPelicula);
				if (id != 0) {
					metodosPelicula.eliminarPelicula(id);
					pantallaAdministrador.jTable3.setModel(metodosPelicula.generarTablaPeliculas());
					pantallaAdministrador.jTable13.setModel(metodosPelicula.generarTablaPeliculas());
				} else {
					JOptionPane.showMessageDialog(null, "La pelicula no existe");
				}
			} catch (NumberFormatException | SQLException e1) {
				System.err.println("Excepcion SQL no controlada");
				System.err.println("Excepcion NumberFormatException no controlada en eliminar pelicula");
				e1.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	// METODOS
	private void presionarJTable7(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable7.rowAtPoint(e.getPoint());
			if (fila > -1) {
				nombreCineA = String.valueOf(this.pantallaAdministrador.jTable7.getValueAt(fila, 0));

			}
		}
	}

	private void presionarJTable4(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable4.rowAtPoint(e.getPoint());
			if (fila > -1) {
				pantallaAdministrador.jLabel107
						.setText(String.valueOf(this.pantallaAdministrador.jTable4.getValueAt(fila, 0)));
				pantallaAdministrador.jTextField17
						.setText(String.valueOf(this.pantallaAdministrador.jTable4.getValueAt(fila, 0)));
				pantallaAdministrador.jTextField18
						.setText(String.valueOf(this.pantallaAdministrador.jTable4.getValueAt(fila, 2)));
				pantallaAdministrador.jLabel109
						.setText(String.valueOf(this.pantallaAdministrador.jTable4.getValueAt(fila, 1)));
				pantallaAdministrador.jComboBox13.setSelectedItem(
						String.valueOf(this.pantallaAdministrador.jTable4.getValueAt(fila, 1)).toString());
			}
		}
	}

	private void presionarJTable12(java.awt.event.MouseEvent e) {
		System.out.println("wdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		if (e.getButton() == 1)// boton izquierdo
		{

			int fila = this.pantallaAdministrador.jTable12.rowAtPoint(e.getPoint());
			if (fila > -1) {
				pantallaAdministrador.jLabel104
						.setText(String.valueOf(this.pantallaAdministrador.jTable12.getValueAt(fila, 0)));
			}
		}
	}

	private void presionarJTable1(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable1.rowAtPoint(e.getPoint());
			if (fila > -1) {
				try {
					pantallaAdministrador.jLabel106
							.setText(String.valueOf(this.pantallaAdministrador.jTable1.getValueAt(fila, 0)));
				} catch (ArrayIndexOutOfBoundsException ee) {
					System.out.println("Error en las funciones");
				}
			}
		}
	}

	private void presionarJTable2(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable2.rowAtPoint(e.getPoint());
			if (fila > -1) {
				pantallaAdministrador.jLabel102
						.setText(String.valueOf(this.pantallaAdministrador.jTable2.getValueAt(fila, 0)));

			}
		}
	}

	private void presionarJTable11(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable11.rowAtPoint(e.getPoint());
			if (fila > -1) {
				this.pantallaAdministrador.jTextField27
						.setText(String.valueOf(this.pantallaAdministrador.jTable11.getValueAt(fila, 0)));

			}
		}
	}

	private void presionarJTable6(java.awt.event.MouseEvent e) {
		System.out.println("Noseeeee");
		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable6.rowAtPoint(e.getPoint());
			if (fila > -1) {
				this.pantallaAdministrador.jLabel92
						.setText(String.valueOf(this.pantallaAdministrador.jTable6.getValueAt(fila, 0)));
				this.pantallaAdministrador.jTextField23
						.setText(String.valueOf(this.pantallaAdministrador.jTable6.getValueAt(fila, 0)));
				this.pantallaAdministrador.jTextField24
						.setText(String.valueOf(this.pantallaAdministrador.jTable6.getValueAt(fila, 1)));
				this.pantallaAdministrador.jTextField25
						.setText(String.valueOf(this.pantallaAdministrador.jTable6.getValueAt(fila, 2)));
				this.pantallaAdministrador.jTextField26
						.setText(String.valueOf(this.pantallaAdministrador.jTable6.getValueAt(fila, 4)));

			}
		}
	}

	private void presionarJTable9(java.awt.event.MouseEvent e) {
		if (e.getButton() == 1)// boton izquierdo
		{
			int fila = this.pantallaAdministrador.jTable9.rowAtPoint(e.getPoint());
			if (fila > -1) {
				this.pantallaAdministrador.jLabel91
						.setText(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 0)));
				try {

					// metodosCine.cogerTodosLosCineBBDD();
					// El error esta aqui

					this.pantallaAdministrador.jTextField11
							.setText(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 0)));
					this.pantallaAdministrador.jTextField12
							.setText(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 1)));
					this.pantallaAdministrador.jTextField5
							.setText(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 2)));
					this.pantallaAdministrador.jTextField8
							.setText(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 3)));

					pantallaAdministrador.jTable2.setModel(metodosFuncion.cogerFuncionBBDDCine(Compagnia.listaCines
							.get(String.valueOf(this.pantallaAdministrador.jTable9.getValueAt(fila, 0)))));

				} catch (IllegalArgumentException ew) {
					System.out.print("no tiene funciones");
				}

			}
		}
	}

	private void presionarTablaModificarPromocion(java.awt.event.MouseEvent e) {// Fijate Antonio
		if (e.getButton() == 1) {
			int fila = this.pantallaAdministrador.tablaModificarPromocion.rowAtPoint(e.getPoint());
			if (fila > -1) {
				oldPromoDiscount = String
						.valueOf(this.pantallaAdministrador.tablaModificarPromocion.getValueAt(fila, 1));
				this.pantallaAdministrador.textoDescuentoPromocionModificar.setText(
						String.valueOf(this.pantallaAdministrador.tablaModificarPromocion.getValueAt(fila, 1)));
				this.pantallaAdministrador.textoDescripcionPromocionModificar.setText(
						String.valueOf(this.pantallaAdministrador.tablaModificarPromocion.getValueAt(fila, 0)));
				// Recarga la tabla cada vez que se hace click sobre la misma
				pantallaAdministrador.tablaModificarPromocion.setModel(metodosPromocion.generarTablaPromociones());
			}
		}
	}

	private void presionarTablaEliminarPromocion(java.awt.event.MouseEvent e) {
		if (e.getButton() == 1) {
			int fila = this.pantallaAdministrador.tablaEliminarPromocion.rowAtPoint(e.getPoint());
			if (fila > -1) {
				oldPromoDiscount = String
						.valueOf(this.pantallaAdministrador.tablaEliminarPromocion.getValueAt(fila, 1));
				// Recarga la tabla cada vez que se hace click sobre la misma
				pantallaAdministrador.tablaEliminarPromocion.setModel(metodosPromocion.generarTablaPromociones());
			}
		}
	}

	private void presionarTablaModificarSala(java.awt.event.MouseEvent e) {
		if (e.getButton() == 1) {
			int fila = this.pantallaAdministrador.tablaSalasmodificarCine.rowAtPoint(e.getPoint());
			if (fila > -1) {
				oldNombreSal = String.valueOf(this.pantallaAdministrador.tablaSalasmodificarCine.getValueAt(fila, 1));
				this.pantallaAdministrador.textoIdSalaCine.setText(
						String.valueOf(this.pantallaAdministrador.tablaSalasmodificarCine.getValueAt(fila, 1)));
				this.pantallaAdministrador.textoNumeroButacas.setText(
						String.valueOf(this.pantallaAdministrador.tablaSalasmodificarCine.getValueAt(fila, 2)));
				// Recarga la tabla cada vez que se hace click sobre la misma
				pantallaAdministrador.tablaSalasmodificarCine.setModel(metodosSala.generarTablaSalas());
			}
		}
	}

	private void clickTablaModificarPelicula(java.awt.event.MouseEvent e) throws SQLException {

		metodosPelicula.cargarPeliculas();

		if (e.getButton() == 1) {
			int fila = this.pantallaAdministrador.jTable13.rowAtPoint(e.getPoint());
			if (fila > -1) {
				idPelicula = String.valueOf(this.pantallaAdministrador.jTable13.getValueAt(fila, 1));
				pantallaAdministrador.jTextField39
						.setText(String.valueOf(pantallaAdministrador.jTable13.getValueAt(fila, 0)));
				pantallaAdministrador.jTextField50
						.setText(String.valueOf(pantallaAdministrador.jTable13.getValueAt(fila, 1)));
				pantallaAdministrador.jTable13.setModel(metodosPelicula.generarTablaPeliculas());

				Pelicula peli = modelo.metodos.MetodosPelicula.peliculas.get(Integer.parseInt(idPelicula));
				pantallaAdministrador.jTextField38.setText(String.valueOf(peli.getAgnoProduccion()));
				pantallaAdministrador.jTextField40.setText(peli.getTituloOriginal());
				pantallaAdministrador.jComboBox6.setSelectedItem(peli.getGenero().name());
				pantallaAdministrador.jComboBox7.setSelectedItem(peli.getIdioma().name());
				pantallaAdministrador.jCheckBox2.setSelected(peli.isSubtitulosEs());
				pantallaAdministrador.jComboBox12.setSelectedItem(peli.getPaisOrigen().name());
				pantallaAdministrador.jTextField42.setText(peli.getSitioWeb());
				pantallaAdministrador.jTextField43.setText(String.valueOf(peli.getDuracionPelicula().toMinutes()));
				pantallaAdministrador.jComboBox8.setSelectedItem(peli.getCalificacionEdades().name());
				pantallaAdministrador.jTextField44
						.setText(peli.getFechaEstrenoEs().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				pantallaAdministrador.jTextField45.setText(peli.getResumen());
			}
		}
	}

	private void clickTablaEliminarPelicula(java.awt.event.MouseEvent e) {

		if (e.getButton() == 1) {
			int fila = this.pantallaAdministrador.jTable13.rowAtPoint(e.getPoint());
			if (fila > -1) {
				idPelicula = String.valueOf(this.pantallaAdministrador.jTable3.getValueAt(fila, 1));
				pantallaAdministrador.jTable3.setModel(metodosPelicula.generarTablaPeliculas());
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			presionarJTable2(e);
			presionarJTable9(e);
		} catch (ArrayIndexOutOfBoundsException we) {
			System.out.println("No tiene funciones");
		}

		presionarJTable4(e);
		presionarJTable1(e);
		presionarJTable12(e);
		presionarJTable7(e);
		presionarJTable11(e);
		presionarJTable6(e);
		presionarTablaModificarPromocion(e);// Fijate Antonio
		presionarTablaEliminarPromocion(e);
		presionarTablaModificarSala(e);
		try {
			clickTablaModificarPelicula(e);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		clickTablaEliminarPelicula(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
