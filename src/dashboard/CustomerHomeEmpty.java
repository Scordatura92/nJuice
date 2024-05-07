package dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CustomerHomeEmpty {

	BorderPane borderContainer;
	VBox vb;
	FlowPane fp;
	//label ini buat tes doang jadi kalau mau bikin CustomerHome diapus aja
//	Label lblTest = new Label("CustHome");
	
	//Toolbar
	ToolBar toolbar;
	Button logout, addItem, deleteItem, checkOut;
	Label custGreet, emptyCart, title, lblTotalPrice;
	
	String username;
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
	}
	
	public void setLayout() {
		//Set styling
		title.setFont(new Font(50));
		vb.setSpacing(15);
		fp.setHgap(5);
		
		borderContainer.setTop(toolbar);
		borderContainer.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
		fp.setAlignment(Pos.CENTER);
		custGreet.setPadding(new Insets(0, 0, 0, 620));
	}
	
	public void addComponent() {
	    toolbar.getItems().addAll(logout, custGreet);
	    fp.getChildren().addAll(addItem, deleteItem, checkOut);
	    vb.getChildren().addAll(title, emptyCart, fp);
	}

	
	public void addEventHandler(Stage primaryStage) {
		addItem.setOnAction(e->{
			PopupAddItem popup = new PopupAddItem();
			popup.display(username, primaryStage);
		});
		
		logout.setOnAction(e->{
			Login login = new Login();
			try {
				login.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		checkOut.setOnAction(e->{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Your cart is empty");
			alert.showAndWait();
		});
	}
	
	public CustomerHomeEmpty(Stage primaryStage, String username) {
		this.username = username;
		initialize();
		setLayout();
		addComponent();
		addEventHandler(primaryStage);
		primaryStage.setScene(new Scene(borderContainer, 800, 600));
	}

}
