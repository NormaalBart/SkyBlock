package me.BartVV.SK.Enums;

public enum Rank {
	
	OWNER("owner"), 
	MEMBER("member"),
	NO_SKYBLOCK("no skyblock")
	;
	
	
	private String str;
	Rank(String str){
		this.str = str;
	}
	
	public String getString(){
		return str;
	}
	
	public Rank getRank(String rank){
		rank = rank.toLowerCase();
		if(rank == OWNER.getString()){
			return OWNER;
		}
		else if (rank == MEMBER.getString()){
			return MEMBER;
		}else{
			return NO_SKYBLOCK;
		}
	}

}
