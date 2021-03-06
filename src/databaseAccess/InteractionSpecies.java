package databaseAccess;

// Generated 1/Jul/2015 12:11:11 by Hibernate Tools 4.0.0

/**
 * InteractionSpecies generated by hbm2java
 */
public class InteractionSpecies implements java.io.Serializable {

	private Integer id;
	private Interaction interaction;
	private String species;

	public InteractionSpecies() {
	}

	public InteractionSpecies(Interaction interaction, String species) {
		this.interaction = interaction;
		this.species = species;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Interaction getInteraction() {
		return this.interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public String getSpecies() {
		return this.species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

}
