package ch.heigvd.iict.ser.imdb.models;

import java.io.Serializable;

public class Role implements Serializable, Comparable<Role> {

	private static final long serialVersionUID = 5730300911874431228L;
	
	private long 	actorId		= -1L;
	private long 	order		= -1L;
	private String 	character 	= null;
	
	public Role() { }
	
	public Role(long actorId, String character) {
		this.actorId = actorId;
		this.character = character;
	}
	
	public long getActorId() { return actorId; }
	public void setActorId(long actorId) { this.actorId = actorId; }
	
	public void setOrder(long order) {
		if(order == 0) order = Long.MAX_VALUE;
		this.order = order;	
	}
	public long getOrder() { return order; }
	
	public String getCharacter() { return character; }
	public void setCharacter(String character) { this.character = character; }
	
	@Override
	public boolean equals(Object that) {
		if(that != null && that instanceof Role) {
			Role rThat = (Role) that;
			return this.actorId == rThat.actorId && (
					(this.character == null && rThat.character == null) ||
					(rThat.character.equals(this.character))
					);
		}
		return false;
	}

	@Override
	public int compareTo(Role that) {
		return Double.compare(this.order, that.order);
	}
	
}
