package team.three.usedstroller.api.security.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;

public class RandomNickNameGenerator {
	private static final String POST_POSITION_1 = "과";
	private static final String POST_POSITION_2 = "와";
	private static final String POST_POSITION_3 = "으로";
	private static final String WHITE_SPACE = " ";
	private static final String ADJECTIVE_TXT = "adjective.txt";
	private static final String NOUN_TXT = "noun.txt";

	public static String getCharterNickname(String noun) {
		return addAdjective(noun);
	}

	public static String addAdjective(String noun) {
		String[] adjectives = getResource(ADJECTIVE_TXT).split(",");
		int index = (int) (Math.random() * (adjectives.length));
		return adjectives[index] + " " + noun;
	}

	public static List<String> getRandomNickname(int count) {
		List<String> nickName = new ArrayList<>();
		String[] nounWords = getResource(NOUN_TXT).split(",");
		String[] adjWords = getResource(ADJECTIVE_TXT).split(",");
		if (count > 10) count = 10;
		return loopWord(count, nickName, nounWords, adjWords);
	}

	public static List<String> loopWord(int count, List<String> nickName, String[] Noun, String[] Adj) {
		int NounLen = Noun.length;
		int AdjLen = Adj.length;
		for (int i = 0; i < count; i++) {
			int firstWord = (int) (Math.random() * (NounLen));
			int adjective = (int) (Math.random() * (AdjLen));
			int adjective2 = (int) (Math.random() * (AdjLen));
			nickName.add(getPostWord(Adj[adjective]+Adj[adjective2] + Noun[firstWord]));
		}
		return nickName;
	}

	public static String getResource(String filename) {
		String streamToString = "";
		try {
			var Path = new ClassPathResource(filename);
			InputStream inputStream = new BufferedInputStream(Path.getInputStream());
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
			streamToString = streamOfString.collect(Collectors.joining());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return streamToString;
	}

	public static String getPostWord(String str) {
		try {
			char laststr = str.charAt(str.length() - 1);
			if (laststr < 0xAC00 || laststr > 0xD7A3) {
				return str;
			}
		} catch (Exception e) {
		}
		return str;
	}
}
