package memory.disk;

import java.util.ArrayList;

public class Scheduler {

    /**
     * 先来先服务算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double FCFS(int start, int[] request) {
        double totalnum=0.0;
        int currentptr=start;
        for(int i:request){
            totalnum+=Math.abs(currentptr-i);
            currentptr=i;
        }
        return totalnum/(double) request.length;
    }

    /**
     * 最短寻道时间优先算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double SSTF(int start, int[] request) {
        double totalnum=0.0;
        int currentptr=start;
        ArrayList<Integer> remainToDo=new ArrayList<Integer>();
        for(int i:request){
            remainToDo.add(i);
        }
        while (!remainToDo.isEmpty()){
            int temmin=Math.abs(currentptr-remainToDo.get(0));
            int minnum=remainToDo.get(0);
            for(int i:remainToDo){
                if(Math.abs(currentptr-i)<temmin){
                    temmin=Math.abs(currentptr-i);
                    minnum=i;
                }
            }
            totalnum+=temmin;
            Integer Minum= minnum;
            remainToDo.remove(Minum);
            currentptr=minnum;
        }
        return totalnum/(double) request.length;
    }
    /**
     * 扫描算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @param direction 磁头初始移动方向，true表示磁道号增大的方向，false表示磁道号减小的方向
     * @return 平均寻道长度
     */
    public double SCAN(int start, int[] request, boolean direction) {
        int maxnum=getMax(request);
        int minnum=getMin(request);
        double totalnum=0.0;
        if(!(start==maxnum&&start==minnum)){
            if(start<maxnum&&start>minnum){
                if(direction){
                    totalnum=Disk.TRACK_NUM-1-start+Disk.TRACK_NUM-1-minnum;
                }
                else totalnum=start+maxnum;
            }
            else if(start>=maxnum){
                if(direction){
                    totalnum=Disk.TRACK_NUM-1-start+Disk.TRACK_NUM-1-minnum;
                }
                else totalnum=start-minnum;
            }
            else if(start<=minnum){
                if(direction){
                    totalnum=maxnum-start;
                }
                else totalnum=start+maxnum;
            }
        }
        return totalnum/(double) request.length;
    }
    public  int getMax(int[] arr) {
        int max = arr[0];
        for(int i = 1;i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public  int getMin(int[] arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }
}
