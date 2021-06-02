/**
 * @autor: Mohammad AbuBader
 * ID: 1190478
 * At: 1-6-2021  12:00 AM
 */

package Controllers;

import DataBaseClasses.SupplierBill;
import Utilities.ConnectionToSbitanyDatabase;
import Utilities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SupplierBillController implements Initializable {

    @FXML // fx:id="tableSupplier"
    private TableView<SupplierBill> tableSupplier; // Value injected by FXMLLoader

    @FXML // fx:id="cmSupplierID"
    private TableColumn<SupplierBill, String> cmSupplierID; // Value injected by FXMLLoader

    @FXML // fx:id="cmSupplierBillID"
    private TableColumn<SupplierBill, String> cmSupplierBillID; // Value injected by FXMLLoader

    @FXML // fx:id="cmOrderAt"
    private TableColumn<SupplierBill, String> cmOrderAt; // Value injected by FXMLLoader

    @FXML // fx:id="cmValueOfBill"
    private TableColumn<SupplierBill, String> cmValueOfBill; // Value injected by FXMLLoader

    @FXML // fx:id="cmDeposit"
    private TableColumn<SupplierBill, String> cmDeposit; // Value injected by FXMLLoader

    @FXML // fx:id="cmPatches"
    private TableColumn<SupplierBill, String> cmPatches; // Value injected by FXMLLoader
    @FXML // fx:id="txtSearch"
    private TextField txtSearch; // Value injected by FXMLLoader

    @FXML // fx:id="btSearchSupplierBill"
    private Button btSearchSupplierBill; // Value injected by FXMLLoader

    @FXML // fx:id="lblNumberOfBills"
    private Label lblNumberOfBills; // Value injected by FXMLLoader

    @FXML // fx:id="txNumberOfBill"
    private TextField txNumberOfBill; // Value injected by FXMLLoader

    @FXML // fx:id="lblValues"
    private Label lblValues; // Value injected by FXMLLoader

    @FXML // fx:id="txtValueOfBills"
    private TextField txtValueOfBills; // Value injected by FXMLLoader

    @FXML // fx:id="combBranchName"
    private ComboBox<String > combBranchName; // Value injected by FXMLLoader

    @FXML // fx:id="rbBillNumber"
    private RadioButton rbBillNumber; // Value injected by FXMLLoader

    @FXML // fx:id="tgSearch"
    private ToggleGroup tgSearch; // Value injected by FXMLLoader

    @FXML // fx:id="rbSupplierID"
    private RadioButton rbSupplierID; // Value injected by FXMLLoader

    @FXML // fx:id="rbDetailsOf"
    private RadioButton rbDetailsOf; // Value injected by FXMLLoader

    @FXML // fx:id="btRefresh"
    private Button btRefresh; // Value injected by FXMLLoader

    @FXML // fx:id="combShow"
    private ComboBox<String> combShow; // Value injected by FXMLLoader




    @FXML
    void handleBtRefresh(ActionEvent event) {

    }

    @FXML
    void handleBtSearch(ActionEvent event) {

    }

    @FXML
    void handleCombBranchName(ActionEvent event) {

    }

    @FXML
    void handleCombShow(ActionEvent event) {

    }

    private Connection con;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ConnectionToSbitanyDatabase connection = new ConnectionToSbitanyDatabase();
        con = connection.connectSbitanyDB();

        this.tableSupplier.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width:2; -fx-font-family:" +
                "'Times New Roman'; -fx-font-size:15; -fx-text-fill: #000000; -fx-font-weight: BOLd; ");

        cmSupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        cmSupplierBillID.setCellValueFactory(new PropertyValueFactory<>("supplierBillID"));
        cmOrderAt.setCellValueFactory(new PropertyValueFactory<>("orderAt"));
        cmValueOfBill.setCellValueFactory(new PropertyValueFactory<>("valueOfBill"));
        cmDeposit.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        cmPatches.setCellValueFactory(new PropertyValueFactory<>("patches"));
        this.refresh(" ");
        this.fillCombBranchName();
        this.combShow.getItems().add("The paid bills");
        this.combShow.getItems().add("The unpaid bill");

    }

    private void refresh(String str) {
        this.tableSupplier.getItems().clear();
        this.txtSearch.clear();
        this.rbBillNumber.setSelected(false);
        this.rbSupplierID.setSelected(false);
        this.rbDetailsOf.setSelected(false);
        try {
            String getSupplierBill = "SELECT * from SupplierBill S " + str;
            Statement statNumberOfBill = con.createStatement();
            ResultSet resultNumberOfBill = statNumberOfBill.executeQuery("SELECT COUNT(*) FROM SupplierBill C " + str);
            resultNumberOfBill.next();
            if (resultNumberOfBill.getString(1) != null)
                txNumberOfBill.setText(resultNumberOfBill.getString(1));
            else txNumberOfBill.setText("0");

            Statement stmtValueOfBill = con.createStatement();
            ResultSet resultValueOfBill = stmtValueOfBill.executeQuery("SELECT SUM(S.valueOfBill) FROM SupplierBill S " + str);
            resultValueOfBill.next();
            this.lblValues.setText("Value Of Bills:");
            if (resultValueOfBill.getString(1) != null)
                txtValueOfBills.setText(resultValueOfBill.getString(1));
            else txtValueOfBills.setText("0");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getSupplierBill);

            boolean flag = true;
            while (rs.next()) {
                SupplierBill supplierBill = new SupplierBill();
                supplierBill.setSupplierBillID(rs.getString(1));
                supplierBill.setOrderAt(rs.getString(2));
                supplierBill.setSupplierID(rs.getString(3));
                supplierBill.setValueOfBill(rs.getString(4));
                supplierBill.setDeposit(rs.getString(5));
                supplierBill.setPatches(rs.getString(6));


                this.tableSupplier.getItems().add(supplierBill);
                flag = false;
            }
            if (flag) {
                Message.displayMassage("Warning", "There are no bills");
                this.txtSearch.clear();
            }
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }
    }
    @FXML
    void handleBtRefresh() {
        refresh(" ");
    }

    private void searchByBillID() {
        this.refresh("  where S.supplierBillID=" + Integer.parseInt(this.txtSearch.getText().trim()));
    }
    private void searchBySupplierID() {
        try {
            Statement customerID = con.createStatement();
            ResultSet resultSupplierID = customerID.executeQuery("select S.SupplierID From Supplier S where S.SupplierID =" + Integer.parseInt((txtSearch.getText().trim())));
            boolean isExist = resultSupplierID.next();
            if (isExist)
                this.refresh(" Where S.SupplierID=" + Integer.parseInt(resultSupplierID.getString(1).trim()));
            else {
                Message.displayMassage("Warning", this.txtSearch.getText().trim() + " Does not have bills");
                this.txtSearch.clear();
            }
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }
    }

    private void detailsOf() {

    }
    @FXML
    void handleBtSearch() {
        if (!this.txtSearch.getText().trim().isEmpty()) {
            if (!isNumber(this.txtSearch.getText().trim())) {
                Message.displayMassage("Warning", " The ID is invalid ");
                this.txtSearch.clear();
                return;
            }
        }
        if (this.rbBillNumber.isSelected()) {
            this.searchByBillID();
        } else if (this.rbSupplierID.isSelected()) {
            this.searchBySupplierID();
        } else if (this.rbDetailsOf.isSelected()) {
            this.detailsOf();
        } else {
            Message.displayMassage("Warning", " Please choose how to search ");
        }

    }
    private void fillCombBranchName() {
        try {
            String sqlBranchesName = "SELECT B.branchName from branch B";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlBranchesName);
            while (rs.next()) {
                if (rs.getString(1).trim().equals("Main Company")) continue;
                this.combBranchName.getItems().add(rs.getString(1).trim());
            }
            stmt.close();
            rs.close();
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }

    }
    @FXML
    void handleCombBranchName() {
        this.tableSupplier.getItems().clear();
        try {
            String bName = this.combBranchName.getValue().trim();
            String getBranchID = "SELECT B.branchID from branch B where B.branchName= '" + bName + "'";
            Statement bID = con.createStatement();
            ResultSet resultBId = bID.executeQuery(getBranchID);
            resultBId.next();
            int branchID = Integer.parseInt(resultBId.getString(1).trim());
            this.refresh(" where S.branchID=" + branchID);
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }
    }


    private void getPaidBills() {
        try {
            this.refresh(" where C.patches=0");
            Statement stmtValueOfBill = con.createStatement();
            ResultSet resultValueOfBill = stmtValueOfBill.executeQuery("SELECT SUM(S.deposit) FROM SupplierBillID S where S.patches=0");
            resultValueOfBill.next();
            this.lblValues.setText("Total paid bills:");
            this.txtValueOfBills.setText(resultValueOfBill.getString(1));
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }
    }

    private void getUnpaidBills() {
        try {
            this.refresh(" where C.patches>0");
            Statement stmtValueOfBill = con.createStatement();
            ResultSet resultValueOfBill = stmtValueOfBill.executeQuery("SELECT SUM(S.patches) FROM SupplierBillID S where S.patches>0");
            resultValueOfBill.next();
            this.lblValues.setText("Total unpaid bills:");
            this.txtValueOfBills.setText(resultValueOfBill.getString(1));
        } catch (SQLException sqlException) {
            Message.displayMassage("Warning", sqlException.getMessage());
        }
    }
    @FXML
    void handleCombShow() {
        if (this.combShow.getValue().equals("The paid bills")) getPaidBills();
        else getUnpaidBills();
    }



    /**
     * To check the value of the entered numberOfShares if contain only digits or not
     */
    public static boolean isNumber(String number) {
        /* To check the entered number of shares, that it consists of
           only digits
         */
        try {
            int temp = Integer.parseInt(number);
            return number.matches("\\d+") && temp > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}