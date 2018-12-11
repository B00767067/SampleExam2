package ie.anu;

class Bus
{
    
    private String destination;
    private int capacity;
	private String features;
	private String accesible;
	public Object getAccesible;
    
    Bus(String pDestination, int pCapacity, String pFeatures, String pAccessible) {
        destination = pDestination;
        capacity = pCapacity;
        features = pFeatures;
        accesible = pAccessible;
   }
   public void setDestination (String pDestination ){
       destination = pDestination;

   }
   public void setCapacity (int pCapacity){
    capacity = pCapacity;

}
public void setFeatures (String pFeatures){
    features = pFeatures;

}
public void setAccessible (String pAccesssible){
    accesible = pAccesssible;

}
public String getDestination(){
    return destination;
}
public int getCapacity(){
    return capacity;
}
public String getFeatures(){
    return features;
}
public String getAccessible(){
    return accesible;
}

}