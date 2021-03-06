package databaseAccess;

// Generated 1/Jul/2015 12:11:11 by Hibernate Tools 4.0.0

/**
 * Expression generated by hbm2java
 */
public class Expression implements java.io.Serializable {

	private Integer id;
	private Proteins proteins;
	private String type;
	private String logfoldchange;

	public Expression() {
	}

	public Expression(Proteins proteins, String type, String logfoldchange) {
		this.proteins = proteins;
		this.type = type;
		this.logfoldchange = logfoldchange;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Proteins getProteins() {
		return this.proteins;
	}

	public void setProteins(Proteins proteins) {
		this.proteins = proteins;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogfoldchange() {
		return this.logfoldchange;
	}

	public void setLogfoldchange(String logfoldchange) {
		this.logfoldchange = logfoldchange;
	}

}
