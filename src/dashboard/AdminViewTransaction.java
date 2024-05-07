package dashboard;

import java.sql.SQLException;

import database.Connect;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.TransactionDetail;
import model.TransactionHeader;

public class AdminViewTransaction {

	BorderPane borderContainer;
//	GridPane gp;
	VBox vb;
	Label title;
	MenuBar menubar;
	Menu adminDash, logout;
	MenuItem viewTransaction, manageProducts, logoutAdmin;
	
	//Setup table 1
	TableView<TransactionHeader> tblTransHeader;
	TableColumn<TransactionHeader, String> headerIdCol;
	TableColumn<TransactionHeader, String> headerTypeCol;
	TableColumn<TransactionHeader, String> headerUsernameCol;
	
	//Buat milih row di table 1
	TableSelectionModel<TransactionHeader> tblHeaderSelection;
	ObservableList<TransactionHeader> selectedHeader;
	
	//Setup table 2
	TableView<TransactionDetail> tblTransDetail;
	TableColumn<TransactionDetail, String> detailIdCol;
	TableColumn<TransactionDetail, String> detailJuiceIdCol;
	TableColumn<TransactionDetail, String> detailJuiceNameCol;
	TableColumn<TransactionDetail, Integer> detailQuantity;
	
	//Buat connect database
	Connect connect = Connect.getConnection();
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		borderContainer = new BorderPane();
//		gp = new GridPane();
		vb = new VBox();
		title = new Label("View Transaction");
		menubar = new MenuBar();
		adminDash = new Menu("Admin's Dashboard");
		logout = new Menu("Logout");
		viewTransaction = new MenuItem("View Transaction");
		manageProducts = new MenuItem("Manage Products");
		logoutAdmin = new MenuItem("Logout from admin");
		
		//Init table 1
		tblTransHeader = new TableView<>();
		headerIdCol = new TableColumn<>("Transaction Id");
		headerTypeCol = new TableColumn<>("Payment Type");
		headerUsernameCol = new TableColumn<>("Username");
		
		//Cell value factory table 1
		headerIdCol.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("transactionId"));
		headerTypeCol.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("paymentType"));
		headerUsernameCol.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("username"));
		
		//Add to table 1
		tblTransHeader.getColumns().addAll(headerIdCol, headerTypeCol, headerUsernameCol);
		
		//Init table 2
		tblTransDetail = new TableView<>();
		detailIdCol = new TableColumn<>("Transaction Id");
		detailJuiceIdCol = new TableColumn<>("Juice Id");
		detailJuiceNameCol = new TableColumn<>("Juice Name");
		detailQuantity = new TableColumn<>("Quantity");
		
		//Cell value factory table 2
		detailIdCol.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("transactionId"));
		detailJuiceIdCol.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("juiceId"));
		detailJuiceNameCol.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("juiceName"));
		detailQuantity.setCellValueFactory(new PropertyValueFactory<TransactionDetail, Integer>("quantity"));
	
		//Add to table 2
		tblTransDetail.getColumns().add(detailIdCol);
		tblTransDetail.getColumns().add(detailJuiceIdCol);
		tblTransDetail.getColumns().add(detailJuiceNameCol);
		tblTransDetail.getColumns().add(detailQuantity);
	}
	
	
	public void tableDetailFill() {
		int rowIndex = tblTransHeader.getSelectionModel().getSelectedIndex();
		TransactionHeader transHeader = tblTransHeader.getItems().get(rowIndex);
		String transactionIdHeader = transHeader.getTransactionId();
//		System.out.println(transactionIdHeader);
		
		String query = "SELECT td.TransactionId, td.JuiceId, mj.JuiceName, td.Quantity "
				+ "FROM transactiondetail td JOIN msjuice mj ON td.JuiceId = mj.JuiceId "
				+ "WHERE td.TransactionId = '" +transactionIdHeader +"'";
//		System.out.println(query);
		connect.rs = connect.executeQuery(query);
		
		
		try {
			while (connect.rs.next()) {
				String transactionId = connect.rs.getString("TransactionId");
				String juiceId = connect.rs.getString("JuiceId");
				String juiceName = connect.rs.getString("JuiceName");
				int quantity = connect.rs.getInt("Quantity");
				
				tblTransDetail.getItems().add(new TransactionDetail(transactionId, juiceId, juiceName, quantity));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void menubarSetup() {
		menubar.getMenus().addAll(adminDash, logout);
		adminDash.getItems().addAll(viewTransaction, manageProducts);
		logout.getItems().add(logoutAdmin);
	}
	
	public void setLayout() {
		//Set style
		title.setFont(new Font(20));
		tblTransHeader.setMaxSize(300, 200);
		headerIdCol.setMinWidth(100);
		headerTypeCol.setMinWidth(100);
		headerUsernameCol.setMinWidth(100);
		tblTransDetail.setMaxSize(400, 180);
		vb.setSpacing(18);
		detailIdCol.setMinWidth(90);
		detailJuiceIdCol.setMinWidth(90);
		detailJuiceNameCol.setMinWidth(140);
		detailQuantity.setMinWidth(80);
		
		borderContainer.setTop(menubar);
		borderContainer.setCenter(vb);
		vb.setAlignment(Pos.CENTER);
//		borderContainer.setCenter(gp);
//		gp.setAlignment(Pos.CENTER);
	}
	
	public void addComponent() {
		vb.getChildren().addAll(title, tblTransHeader, tblTransDetail);
	}
	
	public void viewTransaction() {
		String query = "SELECT * FROM transactionheader";
		connect.rs = connect.executeQuery(query);
		
		try {
			while (connect.rs.next()) {
				String transactionId = connect.rs.getString("TransactionId");
				String username = connect.rs.getString("Username");
				String paymentType = connect.rs.getString("PaymentType");
				
				tblTransHeader.getItems().add(new TransactionHeader(transactionId, username, paymentType));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addEventHandler(Stage primaryStage) {
		logoutAdmin.setOnAction(e->{
			Login login = new Login();
			
			try {
				login.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		tblTransHeader.setOnMouseClicked(e->{
			tblTransDetail.getItems().clear();
			tableDetailFill();
		});
		
		manageProducts.setOnAction(e->{
			new AdminManageProduct(primaryStage);
		});
	}
	
	public AdminViewTransaction(Stage primaryStage) {
		initialize();
		menubarSetup();
		viewTransaction();
		setLayout();
		addComponent();
		addEventHandler(primaryStage);
		primaryStage.setScene(new Scene(borderContainer, 800, 600));
	}

}
