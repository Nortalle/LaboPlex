package controllers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public abstract class Constants {

	public final static SimpleDateFormat[] DATE_FORMATS = new SimpleDateFormat[]{
		new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH),
		new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH),
		new SimpleDateFormat("yyyy", Locale.ENGLISH),
		new SimpleDateFormat("'c.' yyyy", Locale.ENGLISH),
	};
	
	public final static List<Integer> MOVIES_IDS_1ST = new LinkedList<Integer>();
	public final static List<Integer> MOVIES_IDS_2ND = new LinkedList<Integer>();
	public final static List<Integer> MOVIES_IDS_3RD = new LinkedList<Integer>();
	
	public final static int KIND_MOVIE 			=   1;
	
	public final static int INFO_RUNTIMES 		=   1;
	public final static int INFO_GENRES 		=   3;
	public final static int INFO_LANGUAGES 		=   4;
	public final static int INFO_MINI_BIOGRAPHY =  19;
	public final static int INFO_BIRTHDATE 		=  21;
	public final static int INFO_DEATHDATE 		=  23;
	public final static int INFO_BIRTHNAME 		=  26;
	public final static int INFO_PLOT 			=  98;
	public final static int INFO_RATING 		= 101;
	public final static int INFO_BUDGET 		= 105;
	public final static int INFO_ADMISSIONS 	= 110;
	
	public final static int ROLE_ACTOR 			= 1;
	public final static int ROLE_ACTRESS 		= 2;
	public final static int ROLE_PRODUCER 		= 3;
	public final static int ROLE_WRITER 		= 4;
	public final static int ROLE_DIRECTOR 		= 8;
	public final static int ROLE_EDITOR 		= 9;
	
	static {
		//movies for first update
		MOVIES_IDS_1ST.add(2235127); //GoldenEye
		MOVIES_IDS_1ST.add(2822603); //Tomorrow Never Dies
		MOVIES_IDS_1ST.add(2806844); //The World Is Not Enough
		MOVIES_IDS_1ST.add(2053403); //Casino Royale
		MOVIES_IDS_1ST.add(2576878); //Quantum of Solace
		MOVIES_IDS_1ST.add(2652003); //SkyFall
		
		MOVIES_IDS_1ST.add(2304289); //Inglourious Basterds
		MOVIES_IDS_1ST.add(2136717); //Django Unchained
		
		MOVIES_IDS_1ST.add(2510706); //Ocean's Eleven
		MOVIES_IDS_1ST.add(2510709); //Ocean's Twelve
		MOVIES_IDS_1ST.add(2510708); //Ocean's Thirteen

		//movies for second update
		MOVIES_IDS_2ND.addAll(MOVIES_IDS_1ST);
		
		MOVIES_IDS_2ND.add(2768695); //Monuments Men
		MOVIES_IDS_2ND.add(2239795); //Gravity
		
		MOVIES_IDS_2ND.add(2625070); //Se7en
		MOVIES_IDS_2ND.add(2896170); //World War Z
		
		MOVIES_IDS_2ND.add(2619877); //Saving Private Ryan
		MOVIES_IDS_2ND.add(2054453); //Catch Me If You Can

		//movies for third update
		MOVIES_IDS_3RD.addAll(MOVIES_IDS_2ND);
		
		MOVIES_IDS_3RD.add(2719473); //The Bourne Identity
		MOVIES_IDS_3RD.add(2719478); //The Bourne Supremacy
		MOVIES_IDS_3RD.add(2719479); //The Bourne Ultimatum
		MOVIES_IDS_3RD.add(2207917); //Forrest Gump
		MOVIES_IDS_3RD.add(2728991); //The Da Vinci Code
		MOVIES_IDS_3RD.add(2433059); //Man of Steel
		
	}
	
	public static final String getImageUrlForMovie(long movieId) {
		return getImageUrl(movieId, EXISTING_MOVIES_IMAGES);
	}
	
	public static final String getImageUrlForPerson(long personId) {
		return getImageUrl(personId, EXISTING_PERSONS_IMAGES);
	}
	
	private static final String BASE_URL = "http://docr.iict.ch/imdb/";
	private static final String IMAGE_EXTENSION = ".jpg";

	private static final String getImageUrl(long id, long[] existingIds) {
		if(existingIds == null) return null;
		for(long existing : existingIds) {
			if(existing == id)
				return BASE_URL + id + IMAGE_EXTENSION;
		}
		return null;
	}
	
	private static final long[] EXISTING_MOVIES_IMAGES = {
			2053403,
			2054453,
			2136717,
			2207917,
			2235127,
			2239795,
			2304289,
			2433059,
			2510706,
			2510708,
			2510709,
			2576878,
			2619877,
			2625070,
			2652003,
			2719473,
			2719478,
			2719479,
			2728991,
			2768695,
			2806844,
			2822603,
			2896170,
	};
	
	private static final long[] EXISTING_PERSONS_IMAGES = {
			1160485,
			1251489,
			126963,
			1335895,
			1339097,
			1404087,
			1431310,
			1475419,
			1527519,
			1595357,
			1622417,
			1712112,
			1870485,
			1877030,
			1987280,
			2051251,
			2076841,
			2111197,
			220515,
			2220301,
			2284083,
			2348923,
			2377699,
			2381659,
			2437457,
			2504308,
			2508360,
			2518528,
			2525050,
			2587934,
			2593257,
			2720413,
			273673,
			2758943,
			2793880,
			2801646,
			2854968,
			291079,
			2913008,
			2938340,
			3010290,
			3047006,
			3056455,
			334121,
			346345,
			355325,
			370864,
			378358,
			397344,
			40988,
			449474,
			482267,
			538605,
			579122,
			586866,
			659071,
			719186,
			833480,
	};
	
}
