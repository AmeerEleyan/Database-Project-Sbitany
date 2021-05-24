/**
 * @autor: Ameer Eleyan
 * ID: 1191076
 * At: 24-5-2021  7:15 PM
 */

package Controllers;

import DataBaseClasses.Branch;
import DataBaseClasses.Employee;
import Utilities.ConnectionToSbitanyDatabase;
import Utilities.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class BranchesController implements Initializable {

    @FXML // fx:id="lblNumBranches"
    private Label lblNumBranches; // Value injected by FXMLLoader

    @FXML // fx:id="txtSearch"
    private TextField txtSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tableBranches"
    private TableView<Branch> tableBranches; // Value injected by FXMLLoader

    @FXML // fx:id="cmBranchId"
    private TableColumn<Branch, String> cmBranchId; // Value injected by FXMLLoader

    @FXML // fx:id="cmBranchName"
    private TableColumn<Branch, String> cmBranchName; // Value injected by FXMLLoader

    @FXML // fx:id="cmBranchPhone"
    private TableColumn<Branch, String> cmBranchPhone; // Value injected by FXMLLoader

    @FXML // fx:id="cmAddress"
    private TableColumn<Branch, String> cmAddress; // Value injected by FXMLLoader

    @FXML // fx:id="txtBranchID"
    private TextField txtBranchID; // Value injected by FXMLLoader

    @FXML // fx:id="txtBranchName"
    private TextField txtBranchName; // Value injected by FXMLLoader

    @FXML // fx:id="txtBranchPhone"
    private TextField txtBranchPhone; // Value injected by FXMLLoader

    @FXML // fx:id="cbxCityName"
    private ComboBox<String> cbxCityName; // Value injected by FXMLLoader

    @FXML // fx:id="txtStrretName"
    private TextField txtStrretName; // Value injected by FXMLLoader

    @FXML // fx:id="txtRegionName"
    private TextField txtRegionName; // Value injected by FXMLLoader

    @FXML // fx:id="txtBulldingNumber"
    private TextField txtBulldingNumber; // Value injected by FXMLLoader

    private Message message;

    private Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ConnectionToSbitanyDatabase connection = new ConnectionToSbitanyDatabase();
        con = connection.connectSbitanyDB();
        this.tableBranches.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width:2; -fx-font-family:" +
                "'Times New Roman'; -fx-font-size:17; -fx-text-fill: #000000; -fx-font-weight: BOLd; ");
        this.cmBranchId.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        this.cmBranchName.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        this.cmBranchPhone.setCellValueFactory(new PropertyValueFactory<>("branchPhone"));
        this.cmAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        try {
            assert con != null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT C.cityName From city C");
            while (rs.next()) {
                this.cbxCityName.getItems().add(rs.getString(1).trim());
            }
            stmt.close();
            rs.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }


        this.refresh("");
    }

    public void handleBtSearch() {
        if (!this.txtSearch.getText().trim().isEmpty()) {
            if (!isNumber(this.txtSearch.getText().trim())) {
                this.message = new Message();
                message.displayMassage("Warning", " Branch ID is invalid ");
                this.txtSearch.clear();
                return;
            }
            String search = "SELECT * from branch B where B.branchID=" + Integer.parseInt(this.txtSearch.getText().trim());

            try {
                assert con != null;
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(search);
                boolean empty = rs.next();
                if (!empty) {
                    this.message = new Message();
                    message.displayMassage("Warning", this.txtSearch.getText() + " Does not exist ");
                    this.txtSearch.clear();
                    return;
                }
                this.refresh(" where B.branchID=" + Integer.parseInt(this.txtSearch.getText().trim()));

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }

    public void handleBtRefresh() {
        this.refresh(" ");
    }

    public void handleBtAdd() {

    }

    public void handleBtUpdate() {

    }

    public void handleBtDelete() {

    }


    private void refresh(String str) {
        this.txtSearch.clear();
        this.tableBranches.getItems().clear();
        this.txtBranchName.clear();
        this.txtBranchPhone.clear();
        this.txtBranchPhone.clear();
        this.txtBulldingNumber.clear();
        this.txtRegionName.clear();
        this.txtStrretName.clear();
        this.cbxCityName.setValue("");
        try {
            String getBranch = "SELECT * from branch B " + str;
            String getNumOfBranch = "SELECT COUNT(*) from branch B " + str;

            assert con != null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getBranch);

            while (rs.next()) {
                Branch branch = new Branch();
                String id = rs.getString(1);
                branch.setBranchId(id);
                branch.setBranchName(rs.getString(2).trim());
                branch.setBranchPhone(rs.getString(3).trim());

                int cityID = Integer.parseInt(rs.getString(4).trim());
                Statement stmt5 = con.createStatement();
                ResultSet rs5 = stmt5.executeQuery("SELECT C.cityName" +
                        "  From city C where C.cityID =" + cityID);
                rs5.next();

                String address = rs5.getString(1).trim() + ", " + rs.getString(5).trim()
                        + ", " + rs.getString(6).trim() + ", " + rs.getString(7).trim();
                branch.setAddress(address);
                this.tableBranches.getItems().add(branch);
            }

            Statement total = con.createStatement();
            ResultSet resultSetTotal = total.executeQuery(getNumOfBranch);
            resultSetTotal.next();
            this.lblNumBranches.setText(resultSetTotal.getString(1).trim());
            rs.close();
            stmt.close();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public void getSelected() {
        int index = this.tableBranches.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;
        this.txtStrretName.setText(cmBranchName.getCellData(index));
        this.txtBranchPhone.setText(cmBranchPhone.getCellData(index));
        try {

            String getBranchID = "SELECT * from branch B where B.branchID=" + Integer.parseInt(cmBranchId.getCellData(index));
            assert con != null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getBranchID);
            rs.next();

            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT C.cityName From city C where C.cityID=" + Integer.parseInt(rs.getString(4)));
            rs2.next();

            this.txtBranchName.setText(rs.getString(2).trim());
            this.cbxCityName.setValue(rs2.getString(1).trim());
            this.txtStrretName.setText(rs.getString(5).trim());
            this.txtRegionName.setText(rs.getString(6).trim());
            this.txtBulldingNumber.setText(rs.getString(7).trim());

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
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
            if (number.matches("\\d+") && temp > 0) return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
