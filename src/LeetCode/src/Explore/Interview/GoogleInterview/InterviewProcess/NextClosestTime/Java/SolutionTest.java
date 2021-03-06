package LeetCode.src.Explore.Interview.GoogleInterview.InterviewProcess.NextClosestTime.Java;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SolutionTest {
    
    Solution solution;
    
    @BeforeEach
    public void setUp() throws Exception {
        solution = new Solution();
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        solution = null;
    }
    
    @Test
    public void MainFunction() {
        assertTimeout(Duration.ofMillis(500), () -> {
             String[] args = new String[0];
             assertAll(() -> Solution.main(args));
        });
    }
    
    @Test
    public void TrivialCase1() {
        String time = "19:34";
        assertTimeout(Duration.ofMillis(500), () -> {
            String expected = "19:39";
            String actual = Solution.nextClosestTime(time);
            assertEquals(expected, actual);
        });
    }

    @Test
    public void TrivialCase2() {
        String time = "23:59";
        assertTimeout(Duration.ofMillis(500), () -> {
            String expected = "22:22";
            String actual = Solution.nextClosestTime(time);
            assertEquals(expected, actual);
        });
    }
}