package databaseAccess.accessers;

import databaseAccess.Method;

public class InteractionPropertiesDTO {

//	private Integer ipid;
//	private Integer intdisidFk;
//	private Integer source;
//	private String status;
//	private String dbid;
	
	private String name;
	
	public InteractionPropertiesDTO(Method ip) {
		this.name = ip.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
