public class Main {
    public static void main(String[] args) {
        int[] nums = {1,5,4,32,4,2,4,5,62,34,4,2,5,2,56,2,5,6,2,114514};
        sleepSort(nums);
    }

    static void sleepSort(int[] nums){
        for (int i = 0; i < nums.length; i++) {
            int finalI = i;
            new Thread(()->{
                int num = nums[finalI];
                try {
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(num);
            }).start();
        }
    }
}
