package memory.cache.cacheReplacementStrategy;

import memory.cache.Cache;

/**
 * TODO 最近最少用算法
 */
public class LRUReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        Cache.getCache().setTimeStamp(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        Cache temcache=Cache.getCache();
        Long temstamp=temcache.getTimeStamp(start);
        int toBeReplaced=start;
        for(int i=start;i<end;i++){
            if(temcache.getTimeStamp(i)<temstamp){
                temstamp=temcache.getTimeStamp(i);
                toBeReplaced=i;
            }
        }
        temcache.update(toBeReplaced,addrTag,input);
        return toBeReplaced;
    }
}





























