import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Game logic
 * @author sergey gordiyevich
 * @version 1.0
 */
public class GamePlay {

  /**
   * tiles ratings
   */
  public int[][] cellValue;

  /**
   * current and best score
   */
  public int currentScore, bestScore;

  /**
   * constant keys
   */
  public final int up = 16777217, down = 16777218, right = 16777220, left = 16777219;

  /**
   * constructor
   */
  public GamePlay() {

    cellValue = new int[4][4];
    currentScore = 0;
    getBestScoreValue();
  }

  /**
   * Shift up
   * @return true, if shift occurred, and false,if not
   */
  public boolean moveUp() {

    boolean canMove = false;

    for (int col = 0; col < 4; col++) {

      int pivot = 0, row = 1;

      while (row < 4) {

        if (cellValue[row][col] == 0)
          row++;
        else if (cellValue[pivot][col] == 0) {
          cellValue[pivot][col] = cellValue[row][col];
          cellValue[row++][col] = 0;
          canMove = true;
        } else if (cellValue[pivot][col] == cellValue[row][col]) {
          currentScore += cellValue[pivot][col] * 2;
          cellValue[pivot++][col] += cellValue[row][col];
          cellValue[row++][col] = 0;
          canMove = true;
        } else if (++pivot == row)
          row++;
      }
    }

    if (canMove) {
      setNumberInCell();
    }

    return canMove;
  }

  /**
   * Shift down
   * @return true, if shift occurred, and false,if not
   */
  public boolean moveDown() {

    boolean canMove = false;

    for (int col = 0; col < 4; col++) {

      int pivot = 3, row = 2;

      while (row >= 0) {
        if (cellValue[row][col] == 0)
          row--;
        else if (cellValue[pivot][col] == 0) {
          cellValue[pivot][col] = cellValue[row][col];
          cellValue[row--][col] = 0;
          canMove = true;
        } else if (cellValue[pivot][col] == cellValue[row][col]) {
          currentScore += cellValue[pivot][col] * 2;
          cellValue[pivot--][col] += cellValue[row][col];
          cellValue[row--][col] = 0;
          canMove = true;
        } else if (--pivot == row)
          row--;
      }
    }

    if (canMove) {
      setNumberInCell();
    }

    return canMove;
  }

  /**
   * Shift left
   * @return true, if shift occurred, and false,if not
   */
  public boolean moveLeft() {

    boolean canMove = false;

    for (int row = 0; row < 4; row++) {

      int pivot = 0, col = 1;

      while (col < 4) {

        if (cellValue[row][col] == 0)
          col++;
        else if (cellValue[row][pivot] == 0) {
          cellValue[row][pivot] = cellValue[row][col];
          cellValue[row][col++] = 0;
          canMove = true;
        } else if (cellValue[row][pivot] == cellValue[row][col]) {
          currentScore += cellValue[row][pivot] * 2;
          cellValue[row][pivot++] += cellValue[row][col];
          cellValue[row][col++] = 0;
          canMove = true;
        } else if (++pivot == col)
          col++;
      }
    }

    if (canMove) {
      setNumberInCell();
    }

    return canMove;
  }

  /**
   * Shift right
   * @return true, if shift occurred, and false,if not
   */
  public boolean moveRight() {

    boolean canMove = false;

    for (int row = 0; row < 4; row++) {

      int pivot = 3, col = 2;

      while (col >= 0) {

        if (cellValue[row][col] == 0)
          col--;
        else if (cellValue[row][pivot] == 0) {
          cellValue[row][pivot] = cellValue[row][col];
          cellValue[row][col--] = 0;
          canMove = true;
        } else if (cellValue[row][pivot] == cellValue[row][col]) {
          currentScore += cellValue[row][pivot] * 2;
          cellValue[row][pivot--] += cellValue[row][col];
          cellValue[row][col--] = 0;
          canMove = true;
        } else if (--pivot == col)
          col--;
      }
    }

    if (canMove) {
      setNumberInCell();
    }

    return canMove;
  }

  /**
   * Sets a new number into an empty cell
   */
  public void setNumberInCell() {

    int posI, posJ, posF;

    posF = getPosition();

    posI = posF / 4;
    posJ = posF % 4;

    cellValue[posI][posJ] = getNumber();
  }

  /**
   * Random number to the empty cell after the shift
   * @return new number
   */
  public int getNumber() {

    int temp = 0;

    if ((int) (Math.random() * 100) >= 90) {
      temp = 4;
    } else
      temp = 2;

    return temp;
  }

  /**
   * Random position for the new number
   * @return position
   */
  public int getPosition() {

    ArrayList<Integer> emptyCell = new ArrayList<Integer>();

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (cellValue[i][j] == 0) {
          emptyCell.add(i * 4 + j);
        }
      }
    }

    return emptyCell.get((int) (Math.random() * emptyCell.size()));
  }

  /**
   * Saving the best result
   */
  public void saveBestScore() {

    try (FileChannel fBestScoreChannel =
        (FileChannel) Files.newByteChannel(Paths.get("BestScore"), StandardOpenOption.WRITE,
            StandardOpenOption.CREATE)) {

      ByteBuffer buffer = ByteBuffer.allocate(4);
      buffer.putInt(bestScore);
      buffer.flip();
      fBestScoreChannel.write(buffer);
      fBestScoreChannel.close();
    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода: " + e);
      System.exit(1);
    }
  }

  /**
   * Get the best result
   */
  public void getBestScoreValue() {

    try (SeekableByteChannel fBestScoreChannel = Files.newByteChannel(Paths.get("BestScore"))) {
      long fileSize = fBestScoreChannel.size();
      ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
      fBestScoreChannel.read(buffer);
      buffer.flip();

      bestScore = buffer.getInt();

      fBestScoreChannel.close();

    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода " + e);
    }
  }
}