package dashboard;

import java.sql.SQLException;

import database.Connect;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Users;

public class Register{

	BorderPane borderContainer;
	GridPane gridContainer;
	VBox titleContainer, regisContainer;
	CheckBox check;
	Label regisLbl, nJuiceLbl, usernameLbl, passLbl, tcLbl, errorEmptyLbl, errorTakenLbl;
	Button regisBtn;
	FlowPane tcContainer;
	MenuBar dashMenu;
	Menu dash;
	MenuItem selectLogin, selectRegis;
	TextField usernameInput;
	PasswordField passInput;
	
	Connect connect = Connect.getConnection();
	
	public void initialize() {
		borderContainer = new BorderPane();
		gridContainer = new GridPane();
		titleContainer = new VBox();
		regisContainer = new VBox();
		check = new CheckBox();
		regisLbl = new Label("Register");
		nJuiceLbl = new Label("NJuice");
		usernameLbl = new Label("Username");
		passLbl = new Label("Password");
		tcLbl = new Label("I agree to the terms and conditions of NJuice!");
		errorEmptyLbl = new Label("Please input all the field");
		errorTakenLbl = new Label("Username is already taken");
		regisBtn = new Button("Register");
		tcContainer = new FlowPane();
		selectLogin = new MenuItem("Login");
		selectRegis = new MenuItem("Register");
		usernameInput = new TextField();
		passInput = new PasswordField();
		dashMenu = new MenuBar();
		dash = new Menu("Dashboard");
		selectLogin = new MenuItem("Login");
		selectRegis = new MenuItem("Register");
	}
	
	public void setLayout() {
		borderContainer.setCenter(gridContainer);
		dash.getItems().addAll(selectLogin, selectRegis);
		dashMenu.getMenus().add(dash);
		borderContainer.setTop(dashMenu);
		gridContainer.setAlignment(Pos.CENTER);
		regisContainer.setAlignment(Pos.CENTER);
//		regisBtn.setAlignment(Pos.CENTER);
		regisLbl.setFont(Font.font(60));
		titleContainer.setAlignment(Pos.CENTER);
		usernameInput.setMinWidth(300);
		passInput.setMinWidth(300);
		gridContainer.setVgap(10);
		tcContainer.setHgap(10);
		errorEmptyLbl.setTextFill(Color.RED);
		errorTakenLbl.setTextFill(Color.RED);
		errorEmptyLbl.setVisible(false);
		errorTakenLbl.setVisible(false);
	}
	
	public void addComponent() {
		tcContainer.getChildren().add(check);
		tcContainer.getChildren().add(tcLbl);
		titleContainer.getChildren().add(regisLbl);
		titleContainer.getChildren().add(nJuiceLbl);
		regisContainer.getChildren().add(regisBtn);
		gridContainer.add(titleContainer, 0, 0);
		gridContainer.add(usernameLbl, 0, 1);
		gridContainer.add(usernameInput, 0, 2);
		gridContainer.add(passLbl, 0, 3);
		gridContainer.add(passInput, 0, 4);
		gridContainer.add(tcContainer, 0, 5);
		gridContainer.add(errorEmptyLbl, 0, 6);
//		gridContainer.add(errorTakenLbl, 0, 6);
		gridContainer.add(regisContainer, 0, 9);
	}
	
	public Boolean uniqueChecker(String usernameInput) {
		try {
			while (connect.rs.next()) {
				Users user = new Users(null, null, null);
				user.setUsername(connect.rs.getString("username"));
				user.setPassword(connect.rs.getString("password"));
				user.setRole(connect.rs.getString("role"));
				
				if (user.getUsername().equals(usernameInput)) {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void regisListener(Stage primaryStage) {
		regisBtn.setOnAction(e->{
			if (usernameInput.getText().toString().equals("") || passInput.getText().toString().equals("")) {
				errorEmptyLbl.setVisible(true);
//			} else if(!(uniqueChecker(usernameInput.getText().toString()))){
//				errorEmptyLbl.setText("Username is already taken");
//				errorEmptyLbl.setVisible(true);
			} else {
				String update_query = String.format("INSERT INTO msuser (Username, Password, Role) VALUES "
						+ "('%s', '%s', 'Customer')", usernameInput.getText().toString(), passInput.getText().toString());
				connect.executeUpdate(update_query);
				Login login = new Login();
				try {
					login.start(primaryStage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
	}
	
	public void toLogin(Stage primaryStage) {
		selectLogin.setOnAction(e -> {
			Login login = new Login();
			try {
				login.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	
	public Register(Stage primaryStage) {
		initialize();
		setLayout();
		addComponent();
		toLogin(primaryStage);
		regisListener(primaryStage);
		primaryStage.setScene(new Scene(borderContainer, 800, 600));
	}

}
