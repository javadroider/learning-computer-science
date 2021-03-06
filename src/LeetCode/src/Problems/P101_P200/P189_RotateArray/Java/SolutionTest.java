package LeetCode.src.Problems.P101_P200.P189_RotateArray.Java;

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
        int[] nums = {1,2,3,4,5,6,7};
        int k = 3;
        assertTimeout(Duration.ofMillis(500), () -> {
            int[] expected = {5, 6, 7, 1, 2, 3, 4};
            Solution.rotate(nums, k);
            int[] actual = nums;
            assertArrayEquals(expected, actual);
        });
    }
}