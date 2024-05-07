package dashboard;

import java.sql.SQLException;

import database.Connect;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.CartDetail;

public class PopupAddItem {
	Stage popup;
	BorderPane bp;
	VBox vb;
	FlowPane fp;
	//Components
	Label lblJuice, lblJuicePrice, lblDesc, lblQty, lblTotalPrice;
	ComboBox<String> comboJuiceName;
	Spinner<Integer> spinnerQty;
	Button btnAddItem;
	
	String username;
	int quantityFromDB;
	
	Connect connect = Connect.getConnection();
	public void initialize() {
		popup = new Stage();
		bp = new BorderPane();
		vb = new VBox();
		fp = new FlowPane();
		
		//Components init
		lblJuice = new Label("Juice:");
		lblJuicePrice = new Label("Juice Price:");
		lblDesc = new Label("Description:");
		lblQty = new Label("Quantity:");
		lblTotalPrice = new Label("Total Price:");
		btnAddItem = new Button("Add Item");
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.setTitle("Add new item");
		comboJuiceName = new ComboBox<>();
		spinnerQty = new Spinner<>(1, 999999999, 1);
		spinnerQty.setEditable(true);
	}
	
	public void setLayout() {
		bp.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
		fp.setAlignment(Pos.CENTER);
	}
	
	public void addComponent() {
		fp.getChildren().addAll(comboJuiceName, lblJuicePrice);
		vb.getChildren().addAll(lblJuice, fp, lblDesc, lblQty, spinnerQty, lblTotalPrice, btnAddItem);
		
	}
	
	public void fillCombo() {
		String query = "SELECT JuiceName FROM msjuice";
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String juiceName = connect.rs.getString("JuiceName");
				comboJuiceName.getItems().add(juiceName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Boolean validateItemInCart(String newJuiceId, String username) {
		String query = String.format("SELECT * FROM cartdetail WHERE Username = '%s'", username);
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				CartDetail cartDetail = new CartDetail(null, null, 0);
				cartDetail.setUsername(connect.rs.getString("Username"));
				cartDetail.setJuiceId(connect.rs.getString("JuiceId"));
				cartDetail.setQuantity(connect.rs.getInt("Quantity"));
				
				if (newJuiceId.equals(cartDetail.getJuiceId())) {
					return true;
				} else {
					quantityFromDB = cartDetail.getQuantity();
//					System.out.println(quantityFromDB);
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void addEventHandler(Stage primaryStage) {
		btnAddItem.setOnAction(e->{
			String newJuiceId = "";
			String newJuice = comboJuiceName.getValue().toString();
			int addedQty = spinnerQty.getValue();
			String query = String.format("SELECT JuiceId FROM msjuice WHERE JuiceName = '%s'", newJuice);
			System.out.println(query);
			connect.rs = connect.executeQuery(query);
			try {
				while (connect.rs.next()) {
					newJuiceId = connect.rs.getString("JuiceId");
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			if (validateItemInCart(newJuiceId, username) == false) {
				String insert_query = String.format("INSERT INTO cartdetail (Username, JuiceId, Quantity)"
						+ "VALUES ('%s', '%s', '%d')", username, newJuiceId, addedQty);
				connect.executeUpdate(insert_query);
			} else {
				addedQty = addedQty + quantityFromDB;
				String update_query = String.format("UPDATE cartdetail SET Quantity = %d WHERE "
						+ "Username = '%s' AND JuiceID = '%s'", addedQty, username, newJuiceId);
				connect.executeUpdate(update_query);
				
			}
//			new CustomerHome(primaryStage, username);
			
		});
	}
	
	public void display(String username, Stage primaryStage) {
		this.username = username;
		initialize();
		setLayout();
		fillCombo();
		addComponent();
		addEventHandler(primaryStage);
		popup.setScene(new Scene(bp, 300, 250));
		popup.showAndWait();
		
	}
	
}
