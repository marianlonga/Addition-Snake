package com.marianlonga.additionsnake;

public class Translations {
	
	static String language = "English";
	
	public static final String[][] words = {
		{"New Game", "Nová hra"},
		{"High Score", "Sieň slávy"},
		{"Exit", "Skončiť"},
		{"Welcome to Addition Snake!", "Vitaj v sčítavacom hadovi!"},
		{"Collect food with the value:", "Zbieraj jedlo s hodnotou:"},
		{"Score:", "Body:"},
		{"Speed:", "Rýchlosť:"},
		{"Game Over!", "Koniec hry!"},
		{"Enter your username", "Zadaj svoju prezývku"},
		{"Username may contain only alphanumeric characters and must be at least 2 letters long!", "Prezývka môže obsahovať iba alfanumerické znaky a musí mať aspoň 2 znaky!"},
		{"User", "Používateľ"},
		{"has earned", "získal"},
		{"points", "bodov"},
		{"Press [ESC] to exit", "Stlač [ESC] pre skončenie"},
		{"Press [ESC] to return to menu", "Stlač [ESC] pre návrat do menu"},
		{"by Marian Longa", "vyrobil Marian Longa"},
		{"No.", "Č."},
		{"Username", "Prezývka"},
		{"Score", "Body"}
	};
	
	public static void setLanguage(String lang) {
		language = lang;
	}
	
	public static String translate(String whatToTranslate) {
		for(String[] s : words) {
			if(s[0].equals(whatToTranslate)) {
				if(language.equals("English")) return s[0];
				if(language.equals("Slovencina")) return s[1];
			}
		}
		return null;
	}
}
