package com.lee.progresspie;

public interface IProgress {
    void setMax(int max);
    void setMin(int min);
    void setProgress(int progress);
    int getMax();
    int getMin();
    int getProgress();
}
