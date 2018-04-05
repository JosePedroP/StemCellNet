package databaseAccess.accessers;

import databaseAccess.Pathway;

public class PathwaydistributionDTO {

	private String pathName;
	private String pathDes;
	
	public PathwaydistributionDTO(Pathway p) {
		this.pathName = p.getPathName();
		this.pathDes = p.getPathDes();
	}
	
	public String getPathName() {
		return pathName;
	}
	
	public String getPathDes() {
		return pathDes;
	}
	
}
