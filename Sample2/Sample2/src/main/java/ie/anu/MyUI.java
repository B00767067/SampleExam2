package ie.anu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Declaring the types of layout to be used in the app
        final VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout1 = new HorizontalLayout();


        //Declaring connection and connection string for connecting to the database
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://sampleexam2.database.windows.net:1433;" + "database=SampleExam2;"
                + "user=anu@sampleexam2;" + "password= Cloud12**;" + "encrypt=true;" + "trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;" + "loginTimeout=30;";
        
        //Adding and setting the required UI components as per instruction
        Label logo = new Label("<H1>Fun Bus Bookings</H1> <p/> <h3>Please enter the details below and click Book</h3>",
                ContentMode.HTML);
        Label studentNo = new Label("B00767067");

        final TextField name = new TextField();
        name.setCaption("Name of the group");

        final Slider noOfPeople = new Slider("How many people are travelling", 20, 150);
        noOfPeople.setWidth("500px");

        final ComboBox<String> comboBox = new ComboBox<>("Accesiblity need ?");
        comboBox.setItems("yes", "no");
        comboBox.setValue("");

        Button button1 = new Button("Book it now");

        hLayout1.addComponents(name, noOfPeople, comboBox);

        Label errorMessage = new Label();
        errorMessage.setContentMode(ContentMode.HTML);
        errorMessage.setValue("Your bus is not booked yet");


        //Connecting with database 

        try {
            // Connect with JDBC driver to a database
            connection = DriverManager.getConnection(connectionString);
           
            //Sending sql query to the database to get data 
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM BusType;");

            // Convert the resultset that comes back into a List 
            List<Bus> busType = new ArrayList<Bus>();
            
            while (rs.next()) {
                busType.add(new Bus(rs.getString("destination"), rs.getInt("capacity"), rs.getString("features"),
                        rs.getString("accessible")));

            }

            //Setting up the Grid and its items
            Grid<Bus> myGrid = new Grid<>();
            myGrid.setItems(busType);


            // Configure the order and the caption of the grid
            myGrid.addColumn(Bus::getDestination).setCaption("Destination");
            myGrid.addColumn(Bus::getCapacity).setCaption("Capacity");
            myGrid.addColumn(Bus::getFeatures).setCaption("Features");
            myGrid.addColumn(Bus::getAccessible).setCaption("Accesible");
            myGrid.setSizeFull();
            myGrid.setSelectionMode(SelectionMode.MULTI);



            //Adding click listener 
            button1.addClickListener(event -> {

                //Declaring and assigning the grid selection to the set
                Set<Bus> select = myGrid.getSelectedItems();

                //Updating errormessage if no bus selected
                if (select.size() == 0) {
                    errorMessage.setValue("<strong>Please select at least one bus!</strong>");
                    return;
                }

                //Updating errormessage if name of the group is not given
                if (name.getValue().isEmpty()) {
                    errorMessage.setValue("<strong>Please enter group name.</strong>");
                    return;
                }

                //Updating errormessage if Accessibility is not selected
                if (comboBox.getValue().isEmpty()) {
                    errorMessage.setValue("<strong>Please confirm if you need an accessible bus</strong>");
                    return;
                }

                //Updating errormessage if selected bus are non accesible and the group needs accessible bus
                for (Bus b : select) {
                    if (b.getAccessible().equalsIgnoreCase("False") && (comboBox.getValue().equalsIgnoreCase("Yes"))) {
                        errorMessage.setValue("<strong>You cannot select a non-accessible bus.</strong>");
                        return;
                    }
                }

                //Calculating total capacity of selected buses 
                //and updating the error message if total capacity is less than the number of people in the group.
                int totalCapacity = 0;
                for (Bus b : select) {
                    totalCapacity = totalCapacity + b.getCapacity();
                }
                if (noOfPeople.getValue().intValue() > totalCapacity) {
                    errorMessage.setValue("<strong>You have selected buses with a max capacity of " + totalCapacity
                            + " which is not enough to hold " + noOfPeople.getValue().intValue() + " .</strong>");
                    return;

                }

                //Updating the errormessage if all the above condition are met

                errorMessage.setValue("<h3>Success! The group is booked now</h3>");
                return;

            });

            //Adding all the UI components into desired layout
            vLayout.addComponents(logo, hLayout1, errorMessage, button1, myGrid, studentNo);
            
        } catch (Exception e) {
            // This will show an error message if something went wrong
            vLayout.addComponent(new Label(e.getMessage()));
        }

        setContent(vLayout);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        private static final long serialVersionUID = 1L;
    }
}
