package nodes;

import util.Constants;

public abstract class Node {

    public abstract boolean validate();
    public abstract void execute();

    public void setNodeStatus(String s){
        Constants.STATUS =  String.format("[Node: %s] - %s -", this.getClass().getSimpleName(), s);
    }
}
