package models;

import java.io.Serializable;
import java.util.*;

public class GlobalData implements Serializable {

	private static final long serialVersionUID = 1649290909965800077L;
	
	private List<Projection> projections;

	public GlobalData() {}

	public void setProjections(List<Projection> projections){
		this.projections=projections ;
	}
	public List<Projection> getProjections(){return projections;}


}
