package memory.cache.cacheReplacementStrategy;

import memory.cache.Cache;

/**
 * TODO 最近不经常使用算法
 */
public class LFUReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        Cache.getCache().addvisited(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        Cache temcache=Cache.getCache();
        int temvisited=temcache.getvisited(start);
        int toBeReplaced=start;
        for(int i=start;i<end;i++){
            if(temcache.getvisited(i)<temvisited){
                temvisited=temcache.getvisited(i);
                toBeReplaced=i;
            }
        }
        temcache.update(toBeReplaced,addrTag,input);
        return toBeReplaced;
    }

}
