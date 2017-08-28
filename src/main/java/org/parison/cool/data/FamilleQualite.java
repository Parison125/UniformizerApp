package org.parison.cool.data;

public class FamilleQualite {

	private String code;
	private String name;
	
	public FamilleQualite(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCodeNumber() {
		String [] splitedCode = code.split("-");
		return splitedCode[2];
	}
	
	public String getFullName() {
		return code+"- "+name;
	}

}