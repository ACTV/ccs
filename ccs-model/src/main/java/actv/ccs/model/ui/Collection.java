package actv.ccs.model.ui;

import java.util.Vector;

import actv.ccs.model.ConvictCichlid;

public interface Collection {
	public void add(ConvictCichlid obj);
	public Vector<ConvictCichlid> getObjects();
}
