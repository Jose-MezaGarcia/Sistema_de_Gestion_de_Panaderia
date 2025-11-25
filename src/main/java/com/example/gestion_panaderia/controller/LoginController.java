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
 * Controlador de login
 * Aquí se maneja la vista de inicio de sesión y la autenticación de usuarios
 */
public class LoginController implements IController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private VBox boxMensaje;
    @FXML private Label lblMensaje;

    private IAuthService authService;

    /**
     * Se inicializa el controlador al cargar la vista
     * Se configuran los eventos de los botones
     */
    @FXML
    public void initialize() {
        inicializar();
        configurarEventos();
    }

    /**
     * Se inicializan los servicios y se oculta el mensaje
     */
    @Override
    public void inicializar() {
        JsonRepository<Usuario> userRepo = new JsonRepository<>("usuarios.json", Usuario.class);
        authService = new AuthServiceImpl(userRepo);
        boxMensaje.setVisible(false);
    }

    /**
     * Se configuran los eventos de los botones
     * Aceptar ejecuta login, cancelar cierra la aplicación
     * Enter en el campo password también ejecuta login
     */
    private void configurarEventos() {
        btnAceptar.setOnAction(event -> handleLogin());
        btnCancelar.setOnAction(event -> handleCancelar());
        txtPassword.setOnAction(event -> handleLogin());
    }

    /**
     * Se maneja el proceso de login
     * Se valida que los campos no estén vacíos
     * Se autentica el usuario y se cambia la vista si es correcto
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
     * Se maneja la acción de cancelar
     * Aquí se cierra la aplicación
     */
    private void handleCancelar() {
        System.exit(0);
    }

    /**
     * Se muestra un mensaje en pantalla
     * Se cambia el color según si es éxito o error
     */
    private void mostrarMensaje(String mensaje, boolean esExito) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle(esExito ?
                "-fx-text-fill: #228B22; -fx-font-weight: bold;" :
                "-fx-text-fill: #FF4500; -fx-font-weight: bold;");
        boxMensaje.setVisible(true);
    }
}
