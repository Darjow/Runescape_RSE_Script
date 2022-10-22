package nodes;

public abstract class Node {

    public abstract boolean validate();
    public abstract void execute();

    public String getNodeStatus(String s){
        return String.format("[Node: %s] - %s -", this.getClass().getSimpleName(), s);
    }
}
