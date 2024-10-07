package memory.cache.cacheReplacementStrategy;

import memory.Memory;
import memory.cache.Cache;
import util.Transformer;

/**
 * TODO 先进先出算法
 */
public class FIFOReplacement implements ReplacementStrategy {
    boolean dirtyjud=false;

    @Override
    public void hit(int rowNO) {

    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        Cache temcache=Cache.getCache();
        Long temstamp=temcache.getTimeStamp(start);
        boolean validjud=false;
        int toBeReplaced=start;
        if(temcache.getWriteBack()){
            for (int i=start;i<end;i++){
                if (!temcache.getValid(i)){
                    validjud=true;
                    toBeReplaced=i;
                }
            }
        }
        if(!validjud){
            for(int i=start;i<end;i++){
                if(temcache.getTimeStamp(i)<temstamp){
                    temstamp=temcache.getTimeStamp(i);
                    toBeReplaced=i;
                }
            }
        }
        char[] originTag=temcache.getTag(toBeReplaced);
        char[] toFlush=temcache.getData(toBeReplaced);
        String pAddr=pAddrrev(originTag,start);
        if(Cache.isWriteBack){
            if (temcache.getDirty(toBeReplaced)) {
                Memory.getMemory().write(pAddr,toFlush.length,toFlush);
                temcache.setDirty(toBeReplaced);
            }
        }
        temcache.update(toBeReplaced,addrTag,input);
        Cache.getCache().setTimeStamp(toBeReplaced);
        return toBeReplaced;
    }

    String pAddrrev(char[] addrTag,int start){
        String tag=new String(addrTag);
        int n=Cache.getCache().Log(Cache.getCache().getSetSize());
        tag=tag.substring(0,12+n);
        Transformer tf=new Transformer();
        String Sgroupnum=tf.intToBinary(String.valueOf(start/Cache.getCache().getSetSize()));
        Sgroupnum=Sgroupnum.substring(22+n,32);
        return tag+Sgroupnum+"0000000000";
    }
}
