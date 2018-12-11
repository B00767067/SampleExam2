package ie.anu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private static final long serialVersionUID = 8854189939381258536L;
    private static final Grid<Bus> myGrid = null;
    
    

    @Override
    protected void init(VaadinRequest vaadinRequest) {
       final VerticalLayout vLayout = new VerticalLayout();
       // layout.setSizeFull();
        final HorizontalLayout hLayout1 = new HorizontalLayout();
        hLayout1.setSizeUndefined();
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSizeUndefined();
        final GridLayout glayout = new GridLayout();
        glayout.setSizeUndefined();
        Button button1 = new Button("Book it now");
                //Label errorMessage = new Label("");
    
        
        
        Label logo = new Label("<H1>Fun Bus Bookings</H1> <p/> <h3>Please enter the details below and click Book</h3>", ContentMode.HTML);
        Label studentNo = new Label("B00767067");
        final TextField name = new TextField();
        name.setCaption("Name of the group");

        
        Slider noOfPeople = new Slider("How many people are travelling", 20, 150);
        noOfPeople.setWidth(500, Unit.PIXELS);


        ComboBox<String> comboBox = new ComboBox<>("Accesiblity need ?");
        comboBox.setItems("yes","no"); 
        comboBox.setValue("");
        
        comboBox.addSelectionListener(event ->{
              comboBox.setValue(event.getValue());
              
        });

        

        
   

    Connection connection = null;
    

    String connectionString = "jdbc:sqlserver://sampleexam2.database.windows.net:1433;"+
    "database=SampleExam2;"+
    "user=anu@sampleexam2;" +
    "password= Cloud12**;" +
    "encrypt=true;" +
    "trustServerCertificate=false;" +
    "hostNameInCertificate=*.database.windows.net;" +
    "loginTimeout=30;";
    try {
        // Connect with JDBC driver to a database
        connection = DriverManager.getConnection(connectionString);
        //Add a label to the web app with the message and name of the database we connected to
        //layout2.addComponent(new Label("Connected to database: " + connection.getCatalog()));
       ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM BusType;");
                  // Convert the resultset that comes back into a List - we need a Java class to represent the data (Customer.java in this case)
                  List<Bus> busType = new ArrayList<Bus>();
                  // While there are more records in the resultset
                  //int selectedRoomCapacity;
                  while(rs.next())
                  {   
                      // Add a new Room instantiated with the fields from the record (that we want, we might not want all the fields, note how I skip the id)
                      busType.add(new Bus(rs.getString("destination"),rs.getInt("capacity"),rs.getString("features"),rs.getString("accessible")));
                                   
                  }
                  // Add my component, grid is templated with Customer
                 Grid <Bus> myGrid = new Grid<>();
                 // Set the items (List)
                myGrid.setItems(busType);
                // Configure the order and the caption of the grid
                myGrid.addColumn(Bus::getDestination).setCaption("Destination");
                myGrid.addColumn(Bus::getCapacity).setCaption("Capacity");
                myGrid.addColumn(Bus::getFeatures).setCaption("Features");
                myGrid.addColumn(Bus::getAccessible).setCaption("Accesible");

                //Adding selection box
                // Add the grid to the list
                glayout.addComponent(myGrid);
                // preselect value
    } 
    catch (Exception e){
	    // This will show an error message if something went wrong
        glayout.addComponent(new Label(e.getMessage()));
    }

    myGrid.setSelectionMode(SelectionMode.MULTI);
    //myGrid.select(Default);
    MultiSelect<Bus>select = myGrid.asMultiSelect();
    myGrid.addSelectionListener( selection-> {               
     select.getValue(); 
                     
    }); 

    
    Label errorMessage = new Label();
    errorMessage.setContentMode(ContentMode.HTML); 
    errorMessage.setValue("Your room is not booked yet.");               
    
             
button1.addClickListener(e -> {
int noOfSelectedBus = (int) select.getValue().stream().count();
int selectedCapacity = select.getValue().stream().mapToInt(Bus::getCapacity).sum();
int accessiblityNeed = (int) select.getValue().stream()
            .filter(Bus -> Bus.getAccesible.equals("yes")).map(Bus::getAccessible).count();
if(noOfSelectedBus == 0){
    errorMessage.setValue("<strong>Please select at least one bus!</strong>");
}
if(name.getValue().isEmpty()){
    errorMessage.setValue("<strong>Please enter group name.</strong>");
}
if(comboBox.getValue().isEmpty()){
    errorMessage.setValue("<strong>Please confirm if you need an accessible bus</strong>");
}
if((comboBox.getValue().equalsIgnoreCase("yes")&& (accessiblityNeed > 0))){
    errorMessage.setValue("<strong>You cannot select a non-accessible bus.</strong>");
}
if (noOfPeople.getValue()> selectedCapacity){
    errorMessage.setValue("<strong>You have selected buses with a max capacity of " 
    + selectedCapacity +" which is not enough to hold " 
    +  noOfPeople.getValue() + "</strong>");
}else{
    errorMessage.setValue("<h3>Success! The group is booked now</h3>");
}

});          

      
       
        hLayout1.addComponents(name, noOfPeople,comboBox);
        
        vLayout.addComponents(logo,hLayout1,errorMessage,button1,hLayout,glayout,studentNo);
        
        setContent(vLayout);
        
    }
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        private static final long serialVersionUID = 4240798457088128024L;
    }
}
