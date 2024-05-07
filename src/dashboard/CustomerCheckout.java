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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.TransactionDetail;

public class CustomerCheckout {

	BorderPane bp;
	VBox vb;
	GridPane gp;
	FlowPane fp, fpButtonContainer;
	
	//Toolbar
	ToolBar toolbar;
	Button logout;
	Label custGreet;
	
	//Components
	Label lblTitle, lblItems, lblTotalPrice, lblPaymentType;
	Button btnCancel, btnCheckout;
	RadioButton radioCash, radioDebit, radioCredit;
	ToggleGroup paymentGroup;
	
	//Temp fix
	ListView<String> tempList;
	int totalPrice = 0;
	String username;
	
	Connect connect = Connect.getConnection();
	public void initialize() {
		//Temporary
		tempList = new ListView<String>();
		
		bp = new BorderPane();
		vb = new VBox();
		gp = new GridPane();
		fp = new FlowPane();
		fpButtonContainer = new FlowPane();
		
		//Components(lblItems later)
		lblTitle = new Label("Checkout");
		btnCancel = new Button("Cancel");
		btnCheckout = new Button("Checkout");
		lblPaymentType = new Label("Payment Type:");
		
		//Toolbar init
		toolbar = new ToolBar();
		logout = new Button("Logout");
		custGreet = new Label("Hi, " +username);
		
		//Radio Button
		radioCash = new RadioButton("Cash");
		radioDebit = new RadioButton("Debit");
		radioCredit = new RadioButton("Credit");
		paymentGroup = new ToggleGroup();
		radioCash.setToggleGroup(paymentGroup);
		radioDebit.setToggleGroup(paymentGroup);
		radioCredit.setToggleGroup(paymentGroup);
	}
	
	public void setLayout() {
		//set styling
		vb.setSpacing(15);
		fp.setHgap(15);
		fpButtonContainer.setHgap(5);
		tempList.setPrefSize(500, 200);
		custGreet.setPadding(new Insets(0, 0, 0, 620));
		lblTitle.setFont(new Font(50));
		
		toolbar.getItems().addAll(logout, custGreet);
		bp.setTop(toolbar);
		bp.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		fpButtonContainer.setAlignment(Pos.CENTER);
//		bp.setBottom(fpButtonContainer);
	}
	
	public void initLabelItems() {
		String query = String.format("SELECT cd.Username, cd.Quantity, mj.JuiceName, mj.Price FROM cartdetail cd "
				+ "JOIN msjuice mj ON cd.JuiceId = mj.JuiceId WHERE cd.Username = '%s'", username);
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				int quantity = connect.rs.getInt("Quantity");
				String juiceName = connect.rs.getString("JuiceName");
				int price = connect.rs.getInt("Price");
				int priceQty = price*quantity;
				
				String container = String.format("%dx %s [%d x Rp. %d,- = Rp. %d,-]", quantity, juiceName,
						quantity, price, priceQty);
				
				tempList.getItems().add(container);
				totalPrice += priceQty;
				lblTotalPrice = new Label("Total Price: "+totalPrice);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addComponent() {
		fpButtonContainer.getChildren().addAll(btnCancel, btnCheckout);
		fp.getChildren().addAll(radioCash, radioDebit, radioCredit);
		vb.getChildren().addAll(lblTitle, gp, fpButtonContainer);
//		VBox.setMargin(fpButtonContainer, new Insets(20, 0, 0, 60));
//		gp.add(lblItems, 0, 0);
		gp.add(tempList, 0, 0);
		gp.add(lblTotalPrice, 0, 1);
		gp.add(lblPaymentType, 0, 3);
		gp.add(fp, 0, 4);
	}
	
	public String idGenerator() {
		String query = "SELECT TransactionId FROM transactionheader";
		String id = null;
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String transactionId = connect.rs.getString("TransactionId");
				id = transactionId;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Integer numberID = Integer.valueOf(id.substring(2));
		numberID += 1;
		
		String addedZero = String.format("%03d", numberID);
		
		String finalId = "TR" +addedZero;
		return finalId;
	}
	
	public String radioToString() {
		String selection = null;
		
		if (paymentGroup.getSelectedToggle() == radioCash) {
			selection = "Cash";
		} else if (paymentGroup.getSelectedToggle() == radioDebit){
			selection = "Debit";
		} else if (paymentGroup.getSelectedToggle() == radioCredit) {
			selection = "Credit";
		} else {
			return null;
		}
		
		return selection;
	}
	
	public void addEventHandler(Stage primaryStage) {
		btnCancel.setOnAction(e->{
			new CustomerHome(primaryStage, username);
		});
		
		btnCheckout.setOnAction(e->{
			if (radioCash.isSelected() || radioCredit.isSelected() || radioDebit.isSelected()) {
				String newId = idGenerator();
				String insert_query = String.format("INSERT INTO transactionheader (TransactionId, Username, PaymentType) "
						+ "VALUES('%s', '%s', '%s')", newId, username, radioToString());
//				System.out.println(insert_query);
				connect.executeUpdate(insert_query);
				
				TransactionDetail td = new TransactionDetail(null, null, null, 0);
				String query = String.format("SELECT * FROM cartdetail WHERE Username = '%s'", username);
				connect.rs = connect.executeQuery(query);
				
				try {
					while (connect.rs.next()) {
						td.setTransactionId(newId);
						td.setJuiceId(connect.rs.getString("JuiceId"));
						td.setQuantity(connect.rs.getInt("Quantity"));
						
						String insert_query2 = String.format("INSERT INTO transactiondetail (TransactionId,"
								+ "JuiceId, Quantity) VALUES('%s', '%s', %d)", td.getTransactionId(), td.getJuiceId(),
								td.getQuantity());
						
//						System.out.println(insert_query2);
						connect.executeUpdate(insert_query2);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Message");
				alert.setHeaderText("Message");
				alert.setContentText("All items Checked out successfully, please process your payment");
				alert.showAndWait();
				System.out.println("Transaction processed");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please select a payment type!");
                alert.showAndWait();
			}
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
	}
	
	public CustomerCheckout(Stage primaryStage, String username) {
		this.username = username;
		initialize();
		initLabelItems();
		setLayout();
		addEventHandler(primaryStage);
		addComponent();
		primaryStage.setScene(new Scene(bp, 800, 600));
	}

}
