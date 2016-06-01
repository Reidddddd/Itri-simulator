package hello.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Solution {
  class Interval {
    int start;
    int end;
    Interval() { start = 0; end = 0; }
    Interval(int s, int e) { start = s; end = e; }
  }
  
  public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
    int index = 0;
    while(index <intervals.size() && intervals.get(index).end<newInterval.start) {
      index++;
    }
    while (index < intervals.size() && intervals.get(index).start <= newInterval.end) {
      newInterval.start = Math.min(intervals.get(index).start, newInterval.start);
      newInterval.end = Math.max(intervals.get(index).end, newInterval.end);
      intervals.remove(index);
    }
    return intervals;
  }
  
  public List<Interval> insert2(List<Interval> intervals, Interval newInterval) {
    int i=0;
    while(i<intervals.size() && intervals.get(i).end<newInterval.start) i++;
    while(i<intervals.size() && intervals.get(i).start<=newInterval.end){
        newInterval = new Interval(Math.min(intervals.get(i).start, newInterval.start), Math.max(intervals.get(i).end, newInterval.end));
        intervals.remove(i);
    }
    intervals.add(i,newInterval);
    return intervals;
}
  
  public List<Interval> merge(List<Interval> intervals) {
    if (intervals.size() <= 1) return intervals;
    
    List<Interval> merged = new LinkedList<>();
    Collections.sort(intervals, new Comparator<Interval>() {
      @Override
      public int compare(Interval o1, Interval o2) {
        return Integer.compare(o1.start, o2.start);
      }
    });
    
    int start = 0, end = 0;
    boolean first = true;
    for (Interval itv : intervals) {
      if (first) {
        start = itv.start;
        end = itv.end;
        first = false;
      } else {
        if (itv.start > end) {
          merged.add(new Interval(start, end));
          start = itv.start;
          end = itv.end;
        } else {
          end = Math.max(end, itv.end);
        }
      }
    }
    merged.add(new Interval(start, end));
    return merged;
  }
  
  public String largestNumber(int[] nums) {
    if (nums == null || nums.length == 0) return "";
    
    String[] strs = new String [nums.length];
    for (int i = 0; i < nums.length; i++) strs[i] = nums[i] + "";
    
    Comparator<String> cmp = new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        String s1 = o1 + o2;
        String s2 = o2 + o2;
        return s1.compareTo(s2);
      }
    };
    
    Arrays.sort(strs, cmp);
    if (strs[0] == "0") return "0";
    StringBuilder builder = new StringBuilder();
    for (int i = strs.length - 1; i >= 0; i--) builder.append(strs[i]);
    return builder.toString();
  }

  public int reverseBits(int n) {
    int res = 0x00000000;
    int shift = 31;
    int bit;
    while (shift >= 0) {
      bit = n & 1;
      n >>= 1;
      if (bit == 1) res |= (1 << shift);
      shift--;
    }
    return res;
  }
  
  public int maxProduct(String[] words) {
    int[] wordsBit = new int [words.length];
    for (int i = 0; i < wordsBit.length; i++) {
      for (char w : words[i].toCharArray()) {
        wordsBit[i] |= 1 << (w - 'a');
      }
    }
    
    int maxProduct = 0;
    for (int i = 0; i < words.length; i++) {
      for (int j = i + 1; j < words.length; j++) {
        if ((wordsBit[i] & wordsBit[j]) == 0) {
          maxProduct = Math.max(maxProduct, words[i].length() * words[j].length());
        }
      }
    }
    return maxProduct;
  }
  
  public int[] singleNumber3(int[] nums) {
    int[] result = new int [2];
    int xor = 0;
    for (int num : nums) {
      xor ^= num;
    }
    xor &= -xor;
    for (int num : nums) {
      if ((num & xor) == 0) result[0] ^= num;
      else result[1] ^= num;
    }
    return result;
  }
  
  public int singleNumber(int[] nums) {
    int one = 0x000, two = 0x000;
    for (int i = 0; i < nums.length; i++) {
      one = one ^ nums[i] & ~two;
      two = two ^ nums[i] & ~one;
    }
    return one;
  }
  
  public int maxProduct(int[] nums) {
    int[] dp = new int [nums.length + 1];
    dp[0] = 1;
    int max = Integer.MIN_VALUE;
    int tmp = 1;
    for (int i = 1; i <= nums.length; i++) {
      tmp *= nums[i - 1];
      dp[i] = Math.max(dp[i - 1] * nums[i - 1], nums[i - 1]);
      max = Math.max(Math.max(max, dp[i]), tmp);
    }
    return max;
  }
  
  public int nthUglyNumber(int n) {
    int[] ugly = new int [n];
    ugly[0] = 1;
    int i2 = 0, i3 = 0, i5 = 0;
    int u2 = 2, u3 = 3, u5 = 5;
    int minU;
    for (int i = 1; i < n; i++) {
      minU = Math.min(Math.min(u2, u3), u5);
      ugly[i] = minU;
      if (minU == u2) u2 = 2 * ugly[++i2];
      if (minU == u3) u3 = 3 * ugly[++i3];
      if (minU == u5) u5 = 5 * ugly[++i5];
    }
    return ugly[n - 1];
  }
  
  public int coinChange(int[] coins, int amount) {
    if (amount == 0) return 0;
    int[] dp = new int [amount + 1];
    Arrays.fill(dp, -1);
    dp[0] = 0;
    
    for (int i = 1; i <= amount; i++) {
      for (int coin : coins) {
        if (i == coin) {
          dp[i] = 1;
          break;
        }
        if (i > coin && dp[i - coin] != -1) {
          if (dp[i] == -1) dp[i] = dp[i - coin] + 1;
          else dp[i] = Math.min(dp[i - coin] + 1, dp[i]);
        }
      }
    }
    return dp[amount];
  }
  
  public int minimumTotal(List<List<Integer>> triangle) {
    int row = triangle.size();
    if (row == 0) return 0;
    
    int[] dp = new int [row];
    int index = 0;
    for (int num : triangle.get(row - 1)) {
      dp[index++] = num;
    }
    
    List<Integer> layer;
    for (int i = row - 2; i >= 0; i--) {
      layer = triangle.get(i);
      for (int j = 0; j < layer.size(); j++) {
        dp[j] = Math.min(dp[j], dp[j + 1]) + layer.get(j);
      }
    }
    return dp[0];
  }
  
  public int rob(int[] nums) {
    if (nums.length == 1) return nums[0];
    return Math.max(rob(nums, 0, nums.length - 2), rob(nums, 1, nums.length - 1));
  }

  private int rob(int[] nums, int start, int end) {
    int[] dp = new int [2];
    int tmp = 0;
    for (int i = start; i <= end; i++) {
      tmp = dp[1];
      dp[1] = nums[i] + dp[0];
      dp[0] = Math.max(dp[0], tmp);
    }
    return Math.max(dp[0], dp[1]);
  }
  
  public int numSquares(int n) {
    int[] dp = new int [n + 1];
    for (int i = 1; i <= n; i++) dp[i] = Integer.MAX_VALUE;
    int sqrt, diff;
    
    for (int i = 1; i <= n; i++) {
      sqrt = (int) Math.sqrt(i);
      if (sqrt * sqrt == i) {
        dp[i] = 1;
        continue;
      }
      for (int j = 1; j <= sqrt; j++) {
        diff = i - j * j;
        dp[i] = Math.min(dp[i], dp[diff] + 1);
      }
    }
    return dp[n];
  }
  
  public static void main(String[] args) {
    int[] coins = {1,2,4,8,16,32,64,128,256,512};
    Solution solution = new Solution();
    System.out.println(solution.largestNumber(coins));
  }
}
