package me.BartVV.SK.Enums;

public enum Rank {
	
	OWNER("owner"), 
	CO_OWNER("co-owner"),
	MEMBER("member"),
	NO_SKYBLOCK("no_skyblock")
	;
	
	
	private String str;
	Rank(String str){
		this.str = str;
	}
	
	public String getString(){
		return str;
	}

}
