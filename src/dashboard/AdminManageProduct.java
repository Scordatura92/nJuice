package dashboard;

import java.sql.SQLException;

import database.Connect;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Juice;

public class AdminManageProduct {

	//Containers
	BorderPane borderContainer;
	VBox vb;
	GridPane gp;
	
	//Components
	Label lblTitle, lblProductId, lblProductName, lblPrice, lblDesc;
	ComboBox<String> comboProductId;
	TextField nameInput;
	Spinner<Integer> priceSpinner;
	TextArea descInput;
	Button btnInsert, btnUpdate, btnRemove;
	
	//Menubar
	MenuBar menubar;
	Menu adminDash, logout;
	MenuItem viewTransaction, manageProducts, logoutAdmin;
	
	//Table
	TableView<Juice> tblJuice;
	TableColumn<Juice, String> idCol;
	TableColumn<Juice, String> nameCol;
	TableColumn<Juice, Integer> priceCol;
	TableColumn<Juice, String> descCol;
	
	//Buat milih row di table
	TableSelectionModel<Juice> tblSelection;
	ObservableList<Juice> selectedRow;
	
	//Buat connect database
	Connect connect = Connect.getConnection();
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		//Containers
		borderContainer = new BorderPane();
		vb = new VBox();
		gp = new GridPane();
		
		//Init components
		lblTitle = new Label("Manage Products");
		lblProductId = new Label("Product ID:");
		lblProductName = new Label("Product Name:");
		lblPrice = new Label("Price:");
		lblDesc = new Label("Product Description:");
		comboProductId = new ComboBox<>();
		nameInput = new TextField();
		priceSpinner = new Spinner<>(10000, 999999999, 1);
		priceSpinner.setEditable(true);
		descInput = new TextArea();
		btnInsert = new Button("Insert Juice");
		btnUpdate = new Button("Update Price");
		btnRemove = new Button("Remove Juice");
		
		//Init menubar
		menubar = new MenuBar();
		adminDash = new Menu("Admin's Dashboard");
		logout = new Menu("Logout");
		viewTransaction = new MenuItem("View Transaction");
		manageProducts = new MenuItem("Manage Products");
		logoutAdmin = new MenuItem("Logout from admin");
		
		//Init table
		tblJuice = new TableView<>();
		idCol = new TableColumn<>("Juice Id");
		nameCol = new TableColumn<>("Juice Name");
		priceCol = new TableColumn<>("Price");
		descCol = new TableColumn<>("Juice Description");
		
		//Cell value factory table
		idCol.setCellValueFactory(new PropertyValueFactory<Juice, String>("JuiceId"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Juice, String>("JuiceName"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Juice, Integer>("Price"));
		descCol.setCellValueFactory(new PropertyValueFactory<Juice, String>("JuiceDescription"));
		
		//Add to table
		tblJuice.getColumns().addAll(idCol, nameCol, priceCol, descCol);
	}
	
	public void setLayout() {
		//Set style
		lblTitle.setFont(new Font(20));
		tblJuice.setMaxSize(500, 300);
		descCol.setMaxWidth(250);
		vb.setSpacing(15);
		gp.setVgap(10);
		gp.setHgap(5);
		
		borderContainer.setTop(menubar);
		borderContainer.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
	}
	
	public void addComponent() {
		menubar.getMenus().addAll(adminDash, logout);
		adminDash.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutAdmin);
		gp.add(lblProductId, 0, 0);
		gp.add(comboProductId, 1, 0);
		gp.add(btnInsert, 2, 0);
		gp.add(lblPrice, 0, 1);
		gp.add(priceSpinner, 1, 1);
		gp.add(btnUpdate, 2, 1);
		gp.add(lblProductName, 0, 2);
		gp.add(nameInput, 1, 2);
		gp.add(btnRemove, 2, 2);
		gp.add(lblDesc, 0, 3);
		gp.add(descInput, 1, 3);
		vb.getChildren().addAll(lblTitle, tblJuice, gp);
	}
	
	public void viewJuice() {
		tblJuice.getItems().clear();
		String query = "SELECT * FROM msjuice";
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String juiceId = connect.rs.getString("JuiceId");
				String juiceName = connect.rs.getString("JuiceName");
				String juiceDescription = connect.rs.getString("JuiceDescription");
				int price = connect.rs.getInt("Price");
				
				tblJuice.getItems().add(new Juice(juiceId, juiceName, juiceDescription, price));
				comboProductId.getItems().add(juiceId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String idGenerator() {
		String query = "SELECT JuiceId FROM msjuice";
		String id = null;
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String juiceId = connect.rs.getString("JuiceId");
				System.out.println(juiceId);
				id = juiceId;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("This is id: " +id);
		
//		System.out.println(id.substring(2));
		Integer numberID = Integer.valueOf(id.substring(2));
//		System.out.println(numberID);
		numberID += 1;
		
		String addedZero = String.format("%03d", numberID);
//		System.out.println(addedZero);
		
		String finalId = "JU" +addedZero;
		return finalId;
//		System.out.println(finalId);
	}
	
	public void addEventHandler(Stage primaryStage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Error");
		alert.setContentText("Please fill all the field");
		
		logoutAdmin.setOnAction(e->{
			Login login = new Login();
			
			try {
				login.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		//Insert
		btnInsert.setOnAction(e->{
			if (nameInput.getText().toString().isBlank() || descInput.getText().toString().isBlank() || descInput.getText().toString().length() < 10 || descInput.getText().toString().length() > 100) {
				alert.show();
			} else {
				String insert_query = String.format("INSERT INTO msjuice (JuiceId, JuiceName, Price, JuiceDescription)"
						+ "VALUES ('%s', '%s', '%d', '%s')", idGenerator(), nameInput.getText().toString(), 
						priceSpinner.getValue(), descInput.getText().toString());
				connect.executeUpdate(insert_query);
				viewJuice();
			}
		});
		
		btnUpdate.setOnAction(e->{
			if (comboProductId.getSelectionModel().getSelectedIndex() == 0) {
				alert.show();
			} else {
				String update_query = String.format("UPDATE msjuice SET Price = '%d' WHERE JuiceId = '%s'", 
						priceSpinner.getValue(),comboProductId.getValue().toString());
				connect.executeUpdate(update_query);
				viewJuice();
			}
		});
		
		btnRemove.setOnAction(e->{
			if (comboProductId.getSelectionModel().getSelectedIndex() == 0) {
				alert.show();
			} else {
				String delete_query = String.format("DELETE FROM msjuice WHERE JuiceId = '%s'", comboProductId.getValue().toString());
				connect.executeUpdate(delete_query);
				viewJuice();
			}
		});
		
		viewTransaction.setOnAction(e->{
			new AdminViewTransaction(primaryStage);
		});
	}
	
	public AdminManageProduct(Stage primaryStage) {
		idGenerator();
		initialize();
		setLayout();
		addComponent();
		viewJuice();
		addEventHandler(primaryStage);
		primaryStage.setScene(new Scene(borderContainer, 800, 600));
	}

}
