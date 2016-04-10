import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;


public class GamePlay {
	
	public int cellValue[][];
	
	public int currentScore, bestScore;
	
	public static final int up = 16777217, down = 16777218, left = 16777219, right = 16777220;
	
	public GamePlay(){
		
		cellValue = new int [4][4];
		
		currentScore = 0;
		
	}
	
	public boolean moveUp(){						 //сдвиг снизу свверх
		
		boolean canMove = false;
		
		for (int j = 0; j < 4; j++)
		{
		    for (int i = 0; i < 4; i++)
		    {
		        for (int k = i + 1; k < 4; k++)
		        {
		            if (cellValue[k][j] != 0)
		            {
		                if (cellValue[i][j] == 0)
		                {
		                    cellValue[i][j] = cellValue[k][j];
		                    cellValue[k][j] = 0;
		                    canMove = true;
		                }
		                else
		                {
		                    if (cellValue[i][j] == cellValue[k][j])
		                    {
		                        cellValue[i][j] += cellValue[k][j];
		                        cellValue[k][j] = 0;
		                        currentScore += cellValue[i][j];
		                        canMove = true;
		                    }
		                    break;
		                }
		            }
		        }
		    }
		}

		if(canMove){
			setNumberInCell(1);
		}
		
		return canMove;
	}
	
    public boolean moveDown(){                       //сдвиг сверху вниз
		
		boolean canMove = false;
		
		for (int j = 0; j < 4; j++)
		{
		    for (int i = 3; i >= 0; i--)
		    {
		        for (int k = i - 1; k >= 0; k--)
		        {
		            if (cellValue[k][j] != 0)
		            {
		                if (cellValue[i][j] == 0)
		                {
		                    cellValue[i][j] = cellValue[k][j];
		                    cellValue[k][j] = 0;
		                    canMove = true;
		                }
		                else
		                {
		                    if (cellValue[i][j] == cellValue[k][j])
		                    {
		                        cellValue[i][j] += cellValue[k][j];
		                        cellValue[k][j] = 0;
		                        currentScore += cellValue[i][j];
		                        canMove = true;
		                    }
		                    break;
		                }
		            }
		        }
		    }
		}
		
		if(canMove){
			setNumberInCell(1);
		}
		
		return canMove;
	}

	public boolean moveLeft(){                      //сдвиг справа налево
		
		boolean canMove = false;
		
		for (int row = 0; row < 4; row++)
		{
			
		    int pivot = 0, col = 1;
		 
		    while (col < 4)
		    {
		        
		        if (cellValue[row][col] == 0)
		            col++;
		        
		        else if (cellValue[row][pivot] == 0)
		        {
		            cellValue[row][pivot] = cellValue[row][col];
		            cellValue[row][col++] = 0;
		            canMove = true;
		        }
		        
		        else if (cellValue[row][pivot] == cellValue[row][col])
		        {
		        	currentScore += cellValue[row][pivot]*2;
		            cellValue[row][pivot++] += cellValue[row][col];
		            cellValue[row][col++] = 0;		            
		            canMove = true;
		        }
		        
		        else if (++pivot == col)
		            col++;
		    }
		}
		
		if(canMove){
			setNumberInCell(1);
		}
		
		return canMove;
	}

	public boolean moveRight(){                     //сдвиг слева направо
		
		boolean canMove = false;
		
		for (int row = 0; row < 4; row++)
		{
			
		    int pivot = 3, col = 2;
		 
		    while (col >= 0)
		    {
		        
		        if (cellValue[row][col] == 0)
		            col--;
		        
		        else if (cellValue[row][pivot] == 0)
		        {
		            cellValue[row][pivot] = cellValue[row][col];
		            cellValue[row][col--] = 0;
		            canMove = true;
		        }
		        
		        else if (cellValue[row][pivot] == cellValue[row][col])
		        {
		        	currentScore += cellValue[row][pivot]*2;
		            cellValue[row][pivot--] += cellValue[row][col];
		            cellValue[row][col--] = 0;
		            canMove = true;
		        }
				
		    	
		        
		        else if (--pivot == col)
		            col--;
		    }
		}

		if(canMove){
			setNumberInCell(1);
		}
		
		return canMove;
	}

	public void setNumberInCell(int turn){		 //заносим новое число в случайную пустую клетку
		 
		int posI,posJ,posF;
		
		if(turn == 0){
			
			posF = getPosition();
			posI = posF/4;
			posJ = posF%4;
			
			cellValue[posI][posJ] = getNumber();
		}
		
		posF = getPosition();
		
		posI = posF/4;
		posJ = posF%4;
		
		cellValue[posI][posJ] = getNumber();;
		
	}
	
	public int getNumber(){                      //новое число(90% - 2, 10% - 4)
		
		int temp = 0;
	
		if((int)(Math.random()*100) >= 90){
			temp = 4;
		}
		else
			temp = 2;
		
		return temp;
	}

	public int getPosition(){				     //позиция(пустая клетка) для нового числа
		 
		ArrayList<Integer> emptyCell = new ArrayList<Integer>();
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(cellValue[i][j] == 0){
					emptyCell.add(i*4+j);
				}
			}
		}

		return  emptyCell.get((int) (Math.random()*emptyCell.size()));
	}
	
	public void saveBestScore(){
		
        try ( FileChannel fBestScoreChannel = (FileChannel)Files.newByteChannel(Paths.get("BestScore"),
                                     StandardOpenOption.WRITE, StandardOpenOption.CREATE) )
        {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(bestScore);
            buffer.flip();
            fBestScoreChannel.write(buffer);
            fBestScoreChannel.close();
        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода: " + e);
            System.exit(1);
        }                                   
	}

	public void getBestScoreValue() {

        try (SeekableByteChannel fBestScoreChannel = Files.newByteChannel(Paths.get("BestScore")) )
        {
        	
		    long fileSize = fBestScoreChannel.size();
		    ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
		    fBestScoreChannel.read(buffer);
		    buffer.flip();
		    
		    bestScore = buffer.getInt();
		    
		    fBestScoreChannel.close();

        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода " + e);
        }
	}
}
