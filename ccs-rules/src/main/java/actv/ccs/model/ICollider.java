package actv.ccs.model;

public interface ICollider {
	public boolean collidesWith(ICollider o); // detection
	public void handleCollision(ICollider o); // implementation
	
}
