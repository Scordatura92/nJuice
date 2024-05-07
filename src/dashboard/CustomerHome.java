package dashboard;

import java.sql.SQLException;

import database.Connect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CustomerHome {

	BorderPane borderContainer;
	VBox vb;
	FlowPane fp;
	//label ini buat tes doang jadi kalau mau bikin CustomerHome diapus aja
//	Label lblTest = new Label("CustHome");
	
	//Toolbar
	ToolBar toolbar;
	Button logout, addItem, deleteItem, checkOut;
	Label custGreet, emptyCart, title, lblTotalPrice;
	
	
	//Listview need to show (qty)x name - [Rp.Price]
	ListView<String> listview;
	
	
	//Connect database
	Connect connect = Connect.getConnection();
	
//	Login login = new Login();
	String username, userDB, juiceNameStripped, juiceId;
	int totalPrice = 0;
	
	public void initialize() {
		borderContainer = new BorderPane();
		vb = new VBox();
		fp = new FlowPane();
		
		//Component init
		title = new Label("Your Cart");
	    checkOut = new Button("Checkout");
	    logout = new Button("Logout");
	    addItem = new Button("Add new Item");
	    deleteItem = new Button("Delete Item");
	    emptyCart = new Label("Your cart is empty, try adding items");
		
		//Toolbar init
		toolbar = new ToolBar();
		logout = new Button("Logout");
		custGreet = new Label("Hi, "+username);
		
		//Listview
		listview = new ListView<String>();
		
	}
	
	public void setLayout() {
		//Set styling
		title.setFont(new Font(50));
		listview.setMaxWidth(500);
		listview.setPrefHeight(200);
		vb.setSpacing(10);
		fp.setHgap(5);
		
		borderContainer.setTop(toolbar);
		borderContainer.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
		fp.setAlignment(Pos.CENTER);
		custGreet.setPadding(new Insets(0, 0, 0, 620));
	}
	
	public void addComponent() {
//		String query = "SELECT * FROM cartdetail";
//		connect.rs = connect.executeQuery(query);
		vb.getChildren().addAll(title, listview, lblTotalPrice, fp);
		fp.getChildren().addAll(addItem, deleteItem, checkOut);	
		
		
		//Toolbar
		toolbar.getItems().addAll(logout, custGreet);
		
	}
	
//	public void showList(Stage primaryStage) {
//		listview.getItems().clear();
//		/*SELECT cd.Username, mj.JuiceName, mj.Price FROM cartdetail cd JOIN msjuice mj ON 
//		cd.JuiceId = mj.JuiceId WHERE cd.Username = 'shanoble'*/
//		String query = String.format("SELECT cd.Username, cd.Quantity, mj.JuiceName, mj.Price FROM cartdetail cd "
//				+ "JOIN msjuice mj ON cd.JuiceId = mj.JuiceId WHERE cd.Username = '%s'", username);
//		
//		connect.rs = connect.executeQuery(query);
//		try {
//			if (!connect.rs.next()) {
////				listview.setPlaceholder(emptyCart);
////				vb.getChildren().addAll(title, emptyCart, fp);
////				fp.getChildren().addAll(addItem, deleteItem, checkOut);	
//				new CustomerHomeEmpty(primaryStage, username);
//			} else {
//				do {
//					int quantity = connect.rs.getInt("Quantity");
//					String juiceName = connect.rs.getString("JuiceName");
//					int price = connect.rs.getInt("Price")*quantity;
//					totalPrice += price;
//					String listItems = String.format("%dx %s - [Rp. %d]", quantity, juiceName, price);
//					
//					listview.getItems().add(listItems);
//					lblTotalPrice = new Label("Total Price: "+totalPrice);
//					
//				} while (connect.rs.next());
//				
//			}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}

	public void refreshList() {
		listview.getItems().clear();
		/*SELECT cd.Username, mj.JuiceName, mj.Price FROM cartdetail cd JOIN msjuice mj ON 
		cd.JuiceId = mj.JuiceId WHERE cd.Username = 'shanoble'*/
		String query = String.format("SELECT cd.Username, cd.Quantity, mj.JuiceName, mj.Price FROM cartdetail cd "
				+ "JOIN msjuice mj ON cd.JuiceId = mj.JuiceId WHERE cd.Username = '%s'", username);
		
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				int quantity = connect.rs.getInt("Quantity");
				String juiceName = connect.rs.getString("JuiceName");
				int price = connect.rs.getInt("Price")*quantity;
				totalPrice += price;
				String listItems = String.format("%dx %s - [Rp. %d]", quantity, juiceName, price);
				
				listview.getItems().add(listItems);
				lblTotalPrice = new Label("Total Price: "+totalPrice);
				lblTotalPrice.setFont(new Font(15));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void getName() {
		int rowValue = listview.getSelectionModel().getSelectedIndex();
		String container = listview.getItems().get(rowValue).toString();
//		System.out.println(container);
		int indexFirstSpace = container.indexOf(" ", 0);
		int indexSecondSpace = container.indexOf("-");
		String juiceName = container.substring(indexFirstSpace, indexSecondSpace);
		juiceNameStripped = juiceName.stripLeading();
		juiceNameStripped = juiceNameStripped.stripTrailing();
		System.out.println(juiceNameStripped);
		
//		String query = String.format("DELETE FROM cartdetail", args)
		
//		String selectedItem = listview.getSelectionModel().getSelectedItem();
//		System.out.println(selectedItem);
	}
	
	public void juiceNameToId(String juiceNameStripped) {
		String query = String.format("SELECT JuiceId FROM msjuice WHERE JuiceName = '%s'", juiceNameStripped);
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				juiceId = connect.rs.getString("JuiceId");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFromList(String juiceId) {
		String delete_query = String.format("DELETE FROM cartdetail WHERE JuiceId = '%s'", juiceId);
		connect.executeUpdate(delete_query);
	}
	
	public void addEventHandler(Stage primaryStage) {
		logout.setOnAction(e->{
			Login login = new Login();
			
			try {
				login.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		deleteItem.setOnAction(e->{
			if (listview.getSelectionModel().getSelectedIndex() == 0) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setTitle("Error");
				alert.setContentText("Please choose which juice to delete");
				alert.show();
			} else {
				deleteFromList(juiceId);
				refreshList();
			}
//			deleteFromList(juiceId);
//			refreshList();
		});
		
		listview.setOnMouseClicked(e->{
			getName();
			juiceNameToId(juiceNameStripped);
		});
		
		addItem.setOnAction(e->{
			PopupAddItem popup = new PopupAddItem();
			popup.display(username, primaryStage);
			
			refreshList();
		});
		
		checkOut.setOnAction(e->{
			new CustomerCheckout(primaryStage, username);
		});
	}
	
	public CustomerHome(Stage primaryStage, String username) {
//		Login login = new Login();
//		System.out.println(login.usernameFromLogin());
		this.username = username;
		System.out.println(username);
		initialize();
		setLayout();
		refreshList();
		addComponent();
		addEventHandler(primaryStage);
//		System.out.println(usernameHere);
		primaryStage.setScene(new Scene(borderContainer, 800, 600));
//		System.out.println(username);
	}

}
