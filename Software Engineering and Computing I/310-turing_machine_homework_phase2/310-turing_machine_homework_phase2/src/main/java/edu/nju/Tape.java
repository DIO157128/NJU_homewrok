package edu.nju;

import java.util.ArrayList;

/**
 * @Author: pkun
 * @CreateTime: 2021-05-23 19:37
 */
public class Tape {

    ArrayList<StringBuilder> tracks;
    public char B;
    public int head;
    public int start;
    public int end;


    public Tape(ArrayList<StringBuilder> tracks, int head, char B) {
        this.tracks = tracks;
        this.head = head;
        this.B = B;
        updatestart();
        updateend();
    }
    public void updateend() {
        char[] tem = tracks.get(0).toString().toCharArray();
        for (int i = tem.length - 1; i >= 0; i--) {
            if(i!=head) {
                if (tem[i] != '_') {
                    this.end = i;
                    break;
                }
            }
            else{
                end=i;
                break;
            }
        }
    }
    public void updatestart() {
        char[] tem = tracks.get(0).toString().toCharArray();
        for (int i = 0; i <tem.length; i++) {
            if(i!=head) {
                if (tem[i] != '_') {
                    this.start = i;
                    break;
                }
            }
            else{
                start=i;
                break;
            }
        }
    }
}


