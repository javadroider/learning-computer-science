package InterviewBit.src.Courses.Programming.Level7_DynamicProgramming.TwoDStringDp.EditDistance.Java;

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
        String A = "Anshuman";
        String B = "Antihuman";
        assertTimeout(Duration.ofMillis(500), () -> {
            int expected = 2;
            int actual = Solution.minDistance(A, B);
            assertEquals(expected, actual);
        });
    }
}