package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;

public interface Iterator {

public boolean hasNext();
public ConvictCichlid getNext();
public void remove();
}
