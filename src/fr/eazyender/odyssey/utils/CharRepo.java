package fr.eazyender.odyssey.utils;

import java.lang.reflect.Field;

//Class by Lickymoo
//https://gist.github.com/Lickymoo/691482611b6dcc5c623d786dab13252b#file-charrepo-java-L15
//Negative space font by AmberWat
//https://github.com/AmberWat/NegativeSpaceFont

public class CharRepo {
	
	public static final String NEG1 ="\uF801";
	public static final String NEG2 ="\uF802";
	public static final String NEG3 ="\uF803";
	public static final String NEG4 ="\uF804";
	public static final String NEG5 ="\uF805";
	public static final String NEG6 ="\uF806";
	public static final String NEG7 ="\uF807";
	public static final String NEG8 ="\uF808";
	public static final String NEG16 ="\uF809";
	public static final String NEG32 ="\uF80A";
	public static final String NEG64 ="\uF80B";
	public static final String NEG128="\uF80C";
	public static final String NEG256="\uF80D";
	public static final String NEG512="\uF80E";
	public static final String NEG1024="\uF80F";
	
	public static final String POS1 ="\uF821";
	public static final String POS2 ="\uF822";
	public static final String POS3 ="\uF823";
	public static final String POS4 ="\uF824";
	public static final String POS5 ="\uF825";
	public static final String POS6 ="\uF826";
	public static final String POS7 ="\uF827";
	public static final String POS8 ="\uF828";
	public static final String POS16 ="\uF829";
	public static final String POS32 ="\uF82A";
	public static final String POS64 ="\uF82B";
	public static final String POS128="\uF82C";
	public static final String POS256="\uF82D";
	public static final String POS512="\uF82E";
	public static final String POS1024="\uF82F";
	
	//UI
	public static final String BUILD_STOCKAGE_54 = NEG8 +"\uF001";
	public static final String BUILD_FURNACE_54 = NEG8 +"\uF002";
	public static final String BUILD_WORKBENCH_54 = NEG8 + "UN TRUC";
	
	public static final String BUILD_MENU = NEG8 +"\uF003";
	public static final String BUILD_MENU_MILS = NEG8 +"\uF004";
	public static final String BUILD_MENU_METS = NEG8 +"\uF005";
	public static final String BUILD_MENU_HABS = NEG8 +"\uF006";
	public static final String BUILD_MENU_INSTS = NEG8 +"\uF007";
	public static final String BUILD_MENU_DIVS = NEG8 +"\uF008";
	
	public static String getNeg(int pixel) {
		String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(char c : binary.toCharArray()){
			if(c != '0')
			{
				sb.append(NegativeChar.getCharByWeight((int)Math.pow(2, index)).s );
			}
			index++;
		}
		
		return sb.toString();
	}
	
	public static String getPos(int pixel) {
		String binary = new StringBuilder(Integer.toBinaryString(pixel)).reverse().toString();
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(char c : binary.toCharArray()){
			if(c != '0')
			{
				sb.append(PositiveChar.getCharByWeight((int)Math.pow(2, index)).s );
			}
			index++;
		}
		
		return sb.toString();
	}
	
	public static String fromName(String name) {
		try {
			Field f = CharRepo.class.getField(name);
			return(String.valueOf(f.get(null)));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private enum NegativeChar{
		NEG1(1, CharRepo.NEG1),
		NEG2(2, CharRepo.NEG2),
		NEG4(4, CharRepo.NEG4),
		NEG8(8, CharRepo.NEG8),
		NEG16(16, CharRepo.NEG16),
		NEG32(32, CharRepo.NEG32),
		NEG64(64, CharRepo.NEG64),
		NEG128(128, CharRepo.NEG128),
		NEG256(256, CharRepo.NEG256),
		NEG512(512, CharRepo.NEG512),
		NEG1024(1024, CharRepo.NEG1024);
		
		private int weight;
		private String s;
		NegativeChar(int weight, String s){
			this.weight = weight;
			this.s = s;
		}
		
		static NegativeChar getCharByWeight(int weight)
		{
			for(NegativeChar c : NegativeChar.values())
				if(c.weight==weight)
					return c;
			return null;
		}
	}
	
	private enum PositiveChar{
		POS1(1, CharRepo.POS1),
		POS2(2, CharRepo.POS2),
		POS4(4, CharRepo.POS4),
		POS8(8, CharRepo.POS8),
		POS16(16, CharRepo.POS16),
		POS32(32, CharRepo.POS32),
		POS64(64, CharRepo.POS64),
		POS128(128, CharRepo.POS128),
		POS256(256, CharRepo.POS256),
		POS512(512, CharRepo.POS512),
		POS1024(1024, CharRepo.POS1024);
		
		private int weight;
		private String s;
		PositiveChar(int weight, String s){
			this.weight = weight;
			this.s = s;
		}
		
		static PositiveChar getCharByWeight(int weight)
		{
			for(PositiveChar c : PositiveChar.values())
				if(c.weight==weight)
					return c;
			return null;
		}
	}

}
