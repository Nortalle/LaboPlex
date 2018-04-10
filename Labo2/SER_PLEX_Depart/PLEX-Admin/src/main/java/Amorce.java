
import controllers.*;

class Amorce {

	public static void main(String [] args) {
		//we start client
		Amorce amorce = new Amorce();
		amorce.run();
	}
	
	private ORMAccess 			ormAccess 			= null;
	private ControleurGeneral	controleurGeneral	= null;

	private Amorce() {
		this.ormAccess = new ORMAccess();
		this.controleurGeneral = new ControleurGeneral(ormAccess);
	}
	
	private void run() {

		while (controleurGeneral.isAlive()) {
			try {Thread.sleep(1000);} 
			catch(InterruptedException e) {}
		}
	}

}