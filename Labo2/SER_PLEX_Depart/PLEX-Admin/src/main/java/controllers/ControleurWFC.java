package controllers;

import views.*;

public class ControleurWFC {

	private ControleurGeneral ctrGeneral;
	private static MainGUI mainGUI;

	public ControleurWFC(ControleurGeneral ctrGeneral, MainGUI mainGUI){
		this.ctrGeneral=ctrGeneral;
		ControleurWFC.mainGUI=mainGUI;
	}


}