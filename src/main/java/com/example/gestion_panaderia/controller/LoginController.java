package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import com.example.gestion_panaderia.model.Usuario;
import com.example.gestion_panaderia.service.AuthServiceImpl;
import com.example.gestion_panaderia.service.IAuthService;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controlador de la vista de Login en la aplicación Dulce Tentación.
 * Se encarga de manejar la autenticación de usuarios, validando credenciales
 * y mostrando mensajes de error o éxito en la interfaz.
 *
 * Implementa {@link IController} para estandarizar el ciclo de vida de los controladores.
 *
 * @author ¿?
 * @version ¿?
 */
public class LoginController implements IController {

    /** Campo de texto para ingresar el nombre de usuario */
    @FXML
    private TextField txtUsuario;

    /** Campo de texto para ingresar la contraseña */
    @FXML
    private PasswordField txtPassword;

    /** Botón para aceptar e intentar iniciar sesión */
    @FXML
    private Button btnAceptar;

    /** Botón para cancelar y salir de la aplicación */
    @FXML
    private Button btnCancelar;

    /** Contenedor para mostrar mensajes de error o éxito */
    @FXML
    private VBox boxMensaje;

    /** Etiqueta para mostrar el mensaje al usuario */
    @FXML
    private Label lblMensaje;

    /** Servicio de autenticación utilizado para validar credenciales */
    private IAuthService authService;

    /**
     * Método de inicialización automática de JavaFX.
     * Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        inicializar();
        configurarEventos();
    }

    /**
     * Inicializa el controlador configurando el repositorio de usuarios
     * y ocultando el contenedor de mensajes.
     */
    @Override
    public void inicializar() {
        JsonRepository<Usuario> userRepo = new JsonRepository<>("usuarios.json", Usuario.class);
        authService = new AuthServiceImpl(userRepo);
        boxMensaje.setVisible(false);
    }

    /**
     * Configura los eventos de los botones y campos de texto.
     * Incluye la acción de presionar Enter en el campo de contraseña.
     */
    private void configurarEventos() {
        btnAceptar.setOnAction(event -> handleLogin());
        btnCancelar.setOnAction(event -> handleCancelar());

        // Enter en password ejecuta login
        txtPassword.setOnAction(event -> handleLogin());
    }

    /**
     * Maneja el proceso de login.
     * Valida los campos, autentica al usuario y cambia la vista si es correcto.
     */
    private void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor complete todos los campos", false);
            return;
        }

        Usuario usuarioAutenticado = authService.autenticar(usuario, password);

        if (usuarioAutenticado != null) {
            try {
                Main.cambiarVista("/fxml/ventas/ventas.fxml");
            } catch (Exception e) {
                mostrarMensaje("Error al cargar la vista de ventas", false);
                e.printStackTrace();
            }
        } else {
            mostrarMensaje("Usuario o contraseña incorrectos", false);
        }
    }

    /**
     * Maneja la acción de cancelar el login.
     * Finaliza la aplicación.
     */
    private void handleCancelar() {
        System.exit(0);
    }

    /**
     * Muestra un mensaje en la interfaz.
     * @param mensaje Texto del mensaje
     * @param esExito true si es un mensaje de éxito, false si es error
     */
    private void mostrarMensaje(String mensaje, boolean esExito) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esExito ?
                "-fx-text-fill: #228B22; -fx-font-weight: bold;" :
                "-fx-text-fill: #FF4500; -fx-font-weight: bold;");
        boxMensaje.setVisible(true);
    }
}
