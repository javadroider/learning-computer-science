package InterviewBit.src.Courses.Programming.Level7_DynamicProgramming.DpTricky.ShortestCommonSuperString.Java;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<String> A = new ArrayList<String>(Arrays.asList("abcd", "cdef", "fgh", "de"));
        assertTimeout(Duration.ofMillis(500), () -> {
            int expected = 10;
            int actual = Solution.solve(A);
            assertEquals(expected, actual);
        });
    }
}