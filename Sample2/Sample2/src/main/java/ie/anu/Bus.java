package ie.anu;

class Bus {

    private String destination;
    private int capacity;
    private String features;
    private String accessible;

    Bus(String pDestination, int pCapacity, String pFeatures, String pAccessible) {
        setDestination(pDestination);
        setCapacity(pCapacity);
        setFeatures(pFeatures);
        setAccessible(pAccessible);
    }

    /**
     * @return the accessible
     */
    public String getAccessible() {
        return accessible;
    }

    /**
     * @param accessible the accessible to set
     */
    public void setAccessible(String accessible) {
        this.accessible = accessible;
    }

    /**
     * @return the features
     */
    public String getFeatures() {
        return features;
    }

    /**
     * @param features the features to set
     */
    public void setFeatures(String features) {
        this.features = features;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

}