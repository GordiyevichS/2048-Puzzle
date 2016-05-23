public class SortReplaysJava {
  public void qSort(int[] score,String[] fileName, int l, int r) {
    int i = l;
    int j = r;
    int x = score[l + (r - l)/2];
    while (i <= j) {
      while (score[i] > x) {
        i++;
      }
      while (score[j] < x) {
        j--;
      }
      if (i <= j) {
        int temp = score[i];
        score[i] = score[j];
        score[j] = temp;
        
        String temp1 = fileName[i];
        fileName[i] = fileName[j];
        fileName[j] = temp1;
        
        i++;
        j--;
      }
    }
    if (l < j) {
      qSort(score, fileName, l, j);
    }
    if (i < r) {
      qSort(score, fileName, i, r);
    }
  }
}
