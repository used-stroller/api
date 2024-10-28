package team.three.usedstroller.api.security.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RandomNickNameGeneratorTest {
	
	
	@Test
	void getRandomNickName() {
		List<String> randomNickname = RandomNickNameGenerator.getRandomNickname(1);
		System.out.println("randomNickname = " + randomNickname);
		
	}
}