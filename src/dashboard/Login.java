package dashboard;

import java.sql.SQLException;
import java.util.Vector;

import database.Connect;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Users;

public class Login extends Application{

	BorderPane borderContainer;
	Scene scene;
	Label lblUsername, lblPass, lblError, lblLogin, lblNJuice;
	TextField usernameInput;
	PasswordField passInput;
	Button login;
	GridPane gridContainer;
	MenuBar dashMenu;
	Menu dash;
	MenuItem selectLogin, selectRegis;
	VBox titleContainer, loginContainer;
	
	//Untuk connect database
	Connect connect = Connect.getConnection();
	
	
	String usernameHere;
	public void initialize() {
		borderContainer = new BorderPane();
//		scene = new Scene(borderContainer, 800, 600);
		dash = new Menu("Dashboard");
		dashMenu = new MenuBar();
		selectLogin = new MenuItem("Login");
		selectRegis = new MenuItem("Register");
		dash.getItems().addAll(selectLogin, selectRegis);
		dashMenu.getMenus().add(dash);
		gridContainer = new GridPane();
		lblLogin = new Label("Login");
		lblUsername = new Label("Username");
		lblPass = new Label("Password");
		lblNJuice = new Label("NJuice");
		lblError = new Label("Credentials failed!");
		usernameInput = new TextField();
		passInput = new PasswordField();
		login = new Button("Login");
		titleContainer = new VBox();
		loginContainer = new VBox();
		scene = new Scene(borderContainer, 800, 600);
	}
	
	
	public void setLayout() {
		borderContainer.setCenter(gridContainer);
		borderContainer.setTop(dashMenu);
		gridContainer.setAlignment(Pos.CENTER);
		loginContainer.setAlignment(Pos.CENTER);
		lblLogin.setFont(Font.font(60));
		titleContainer.setAlignment(Pos.CENTER);
		usernameInput.setMinWidth(300);
		passInput.setMinWidth(300);
		gridContainer.setVgap(10);
		lblError.setVisible(false);
		lblError.setTextFill(Color.RED);
//		borderContainer.setBottom(loginContainer);
//		borderContainer.setPadding(new Insets(0, 0, 300, 0));
	}
	
	public void addComponent() {
		titleContainer.getChildren().add(lblLogin);
		titleContainer.getChildren().add(lblNJuice);
		loginContainer.getChildren().add(login);
		gridContainer.add(titleContainer, 0, 0);
		gridContainer.add(lblUsername, 0, 1);
		gridContainer.add(usernameInput, 0, 2);
		gridContainer.add(lblPass, 0, 3);
		gridContainer.add(passInput, 0, 4);
		gridContainer.add(lblError, 0, 5);
		gridContainer.add(loginContainer, 0, 7);
		
	}
	
	public void loginListener(Stage primaryStage) {
		login.setOnMouseClicked(e -> {
			String query = "SELECT * FROM msuser";
			connect.rs = connect.executeQuery(query);
//			connect.executeQuery(query);
			String usernameInputted = usernameInput.getText().toString();
			String passwordInputted = passInput.getText().toString();

			try {
				while (connect.rs.next()) {
					
					Users user = new Users(null, null, null);
					user.setUsername(connect.rs.getString("username"));
					user.setPassword(connect.rs.getString("password"));
					user.setRole(connect.rs.getString("role"));
					
					if (user.getUsername().equals(usernameInputted) && user.getPassword().equals(passwordInputted)) {
						if (user.getRole().equalsIgnoreCase("admin")) {
							new AdminViewTransaction(primaryStage);
						} else if(user.getRole().equalsIgnoreCase("customer")) {
//							return usernameInputted
//							conn
							String getUsername = connect.rs.getString("Username");
//							String getUsername = connect.rs.getString(columnIndex)
							if (userHasCart(usernameInputted) == true) {
								new CustomerHome(primaryStage, getUsername);
								
							} else {
								new CustomerHomeEmpty(primaryStage, getUsername);
							}
							
							
//							new Test(primaryStage);
						}
					} else {
						lblError.setVisible(true);
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			usernameHere = usernameInputted;
			
//			System.out.println(usernameHere);
		});
	}
	
	public Boolean userHasCart(String username) {
		String query = "SELECT Username FROM cartdetail";
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String usernameDB = connect.rs.getString("Username");
				if (username.equals(usernameDB)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void toRegis(Stage primaryStage) {
		selectRegis.setOnAction(e -> {
			new Register(primaryStage);
		});
			
	}
	
//	public String usernameFromLogin() {
//		return usernameHere;
//	}
//	public Scene getScene() {
//		int width = 800, height = 600;
//		return new Scene(borderContainer, width, height);
//	}
	
	//yg refresh table while loop itu buat masukin data db ke object di drive lec biar
	//lebih gampang nantinya
	
	
	
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setLayout();
		addComponent();
		toRegis(primaryStage);
		loginListener(primaryStage);
//		usernameFromLogin();
//		System.out.println(usernameHere);
//		Register regis = new Regi+ster();
//		regis.initialize();
//		regis.setLayout();
//		regis.addComponent();
//		primaryStage.setScene(regis.getScene());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	
	}

}
