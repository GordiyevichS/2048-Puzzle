
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Game {
	
	public Shell shellGame;
	
	public CLabel labelCell[][];
	
	private Label labelCurScoreValue,labelBestScoreValue,labelField,labelBestScoreT,labelName;
	
	private Button buttonMainMenu, buttonRestart, buttonAI;

	private Color yellow, gold, orange, orangeRed, red, oliveDrab, seaGreen, white, dimGray, gray, dark, dark_red;
	
	public int cellValue[][];
	
	public int currentScore, bestScore;
	
	private Listener listenerKeyboard;
	
	private SelectionAdapter listenerMainMenu, listenerRestart, listenerAI;
			
	private Font fontArial24, fontArial10, fontTNR18;
	
	public Shell [] shells;
	
	private boolean dialogOpened = false;
	
	public static final int up = 16777217, down = 16777218, left = 16777219, right = 16777220;
	
	public Game(){
		
		shellGame = new Shell(Display.getCurrent());
		
		shells = Display.getCurrent().getShells();
		
		final Device device = Display.getCurrent();
		yellow = new Color(device,255,255,0);
		gold = new Color(device,255,215,0);
		orange = new Color(device,255,165,0);
		orangeRed = new Color(device,255,69,0);
		red = new Color(device,255,0,0);
		oliveDrab = new Color(device,192,255,62);
		seaGreen = new Color(device,67,205,128);
		dimGray = new Color(device,105,105,105);
		gray = new Color(device,190,190,190);		
		white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		dark = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		dark_red = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
		
		shellGame.setText("2048 Puzzle");
		shellGame.setBackground(white);
		shellGame.setSize(295,400);
		
		FormLayout formLayout = new FormLayout(); //раскладка компонентов
		shellGame.setLayout(formLayout);
		
		createWidgets();
		
		cellValue = new int [4][4];
		
		currentScore = 0;
		
		getBestScoreValue();
		
		createListeners();		
	}

	public void open(){
		shells[1].open();
		shells[0].setVisible(false);
	}
	
	public void createWidgets(){   				 //создание виджетов 
		
		createNameLabel();
		createScoreLabels();
		createButtons();
		createField();
	}
	
	public void createListeners(){
		
		createCloseListener();
		createButtonsListener();
		createBotListener();
	}
	
	public void play(){                          //обработчик нажатия клавиш
		
		listenerKeyboard = new Listener(){

			@Override
			public void handleEvent(Event e) {
				
				if((e.keyCode == right)){     //вправо
                    moveRight();
                    checkEndGame();
                }
                
                if(e.keyCode == left){		 //влево
        			moveLeft();
        			checkEndGame();
				}
                
				if(e.keyCode == down){		 //вниз
        			moveDown();
        			checkEndGame();
				}
				
				if(e.keyCode == up){ 	     //вверх
        			moveUp();
        			checkEndGame();
				}
			}
			
		};
		
		Display.getCurrent().addFilter(SWT.KeyUp, listenerKeyboard);
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

	public void updateField(){                   //обновляем клетки(после сдвигов)
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(cellValue[i][j] != 0){
					if(cellValue[i][j]<8){
						labelCell[i][j].setBackground(white);
					}
					else if(cellValue[i][j]>=8 && cellValue[i][j]<16){
						labelCell[i][j].setBackground(gold);
					}					
					else if(cellValue[i][j]>=16 && cellValue[i][j]<32){
						labelCell[i][j].setBackground(orange);
					}
					else if(cellValue[i][j]>=32 && cellValue[i][j]<64){
						labelCell[i][j].setBackground(orangeRed);
					}
					else if(cellValue[i][j]>=64 && cellValue[i][j]<128){
						labelCell[i][j].setBackground(red);
					}
					else if(cellValue[i][j]>=128 && cellValue[i][j]<1024){
						labelCell[i][j].setBackground(yellow);
					}
					else if(cellValue[i][j]>=1024 && cellValue[i][j]<2048){
						labelCell[i][j].setBackground(oliveDrab);
					}
					else if(cellValue[i][j] >=2048){
						labelCell[i][j].setBackground(seaGreen);
					}
					
					labelCell[i][j].setText(Integer.toString(cellValue[i][j]));
				}
				else{
					labelCell[i][j].setText("");
					labelCell[i][j].setBackground(gray);
				}
			}
		}
		
		if(currentScore > bestScore){
			bestScore = currentScore;
			labelBestScoreValue.setText(Integer.toString(bestScore));
		}
		
		labelCurScoreValue.setText(Integer.toString(currentScore));
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
		
		updateField();
		
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
		
		updateField();
		
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
		
		updateField();
		
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
		
		updateField();
		
		return canMove;
	}

	public void getBestScoreValue() {

        try (SeekableByteChannel fBestScoreChannel = Files.newByteChannel(Paths.get("BestScore")) )
        {
        	
		    long fileSize = fBestScoreChannel.size();
		    ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
		    fBestScoreChannel.read(buffer);
		    buffer.flip();
		    
		    bestScore = buffer.getInt();
		    labelBestScoreValue.setText(String.valueOf(bestScore));
		    
		    fBestScoreChannel.close();

        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода " + e);
        }
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
	
	public boolean checkEndGame(){
	
		boolean canOpen = true;
		int count = 0;
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(cellValue[i][j] != 0){
					count++;
				}
			}
		}
		
		
		if(count == 16){	
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 4; j++){
					if(i < 3 && j < 3){
						if(cellValue[i][j] == cellValue[i+1][j] 
								|| cellValue[i][j] == cellValue[i][j+1]){
							canOpen = false;
						}
					}
					else if(i == 3 && j < 3){
						if(cellValue[i][j] == cellValue[i][j+1]){
							canOpen = false;
						}
					}
					else if(i < 3 && j == 3){
						if(cellValue[i][j] == cellValue[i+1][j]){
							canOpen = false;
						}
					}
				}
			}
		}
		else
			canOpen = false;
		
		if(canOpen == true && dialogOpened == false){
			dialogOpened = true;
			openDialogDefeat();
			return false;
		}
		
		return true;
}
	
	public void openDialogDefeat(){
		final Shell dialogDefeat = new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL 
				| SWT.DIALOG_TRIM);
	    dialogDefeat.setText(":(");
	    dialogDefeat.setSize(220, 120);
		    
		final Label labelDialogDefeat = new Label(dialogDefeat,SWT.CENTER);
		labelDialogDefeat.setBounds(10, 10, 200, 100);
		
		labelDialogDefeat.setText("Вы проиграли.\n"
									+"Вы набрали:"
									+Integer.toString(currentScore)+" балла(ов)");
		
		dialogDefeat.open();
		
		while (!dialogDefeat.isDisposed()) {
		    if (!Display.getCurrent().readAndDispatch()) {
		        Display.getCurrent().sleep();
		    }
		}
		
		dialogOpened = false;
	}
	
	public void saveGame(){
		
		try ( FileChannel fSaveChannel = (FileChannel)Files.newByteChannel(Paths.get("Save"),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE) )
		{			
			ByteBuffer buffer = ByteBuffer.allocate(68);
			for(int i = 0; i < 4; i++)
				for(int j = 0; j < 4; j++)
					buffer.putInt(cellValue[i][j]);
			buffer.putInt(currentScore);
			buffer.flip();
			
			fSaveChannel.write(buffer);
			
			fSaveChannel.close();
		} catch(InvalidPathException e) {
			System.out.println("Ошибка указания пути " + e);
		} catch (IOException e) {
			System.out.println("Ошибка ввода-вывода: " + e);
			System.exit(1);
		}
	}
	
	public void loadGame(){
		
		try (SeekableByteChannel fLoadChannel = Files.newByteChannel(Paths.get("Save")) )
        {
        	
		    long fileSize = fLoadChannel.size();
		    ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
		    fLoadChannel.read(buffer);
		    buffer.flip();
		    
		    for(int i = 0; i < 4; i++){
		    	for(int j = 0; j < 4; j++){
		    		cellValue[i][j] = buffer.getInt();
		    		labelCell[i][j].setText(String.valueOf(cellValue[i][j]));
		    	}
		    }
		    
		    currentScore = buffer.getInt();
		    labelCurScoreValue.setText(String.valueOf(currentScore));
		    
		    fLoadChannel.close();

        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода " + e);
        }
	}

	public void sleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		shellGame.update();
	}
	
	public void createNameLabel(){
		
		fontArial24 = new Font(Display.getCurrent(),"Arial",24,SWT.NORMAL);
		fontArial10 = new Font(Display.getCurrent(),"Arial",10,SWT.NORMAL);
		fontTNR18 = new Font(Display.getCurrent(),"TimesNewRoman",18,SWT.BOLD);
		
		FormData formDataName = new FormData(); //расположение названия игры
		formDataName.top = new FormAttachment(3,0);
		formDataName.left = new FormAttachment(6,0);
		formDataName.right = new FormAttachment(34,0);
		formDataName.bottom = new FormAttachment(12,0);
		
		labelName = new Label(shellGame,SWT.CENTER);//label названия игры
		labelName.setFont(fontArial24);                         //шрифт
		labelName.setText("2048");						  //помещаем текст
		labelName.setForeground(dark);					  //цвет заднего фона
		labelName.setLayoutData(formDataName);			  //размещаем label
	}
	
	public void createScoreLabels(){
		
		FormData formDataCurScoreT = new FormData();      //расположение label Score 
		formDataCurScoreT.left = new FormAttachment(44,0);
		formDataCurScoreT.top = new FormAttachment(labelName,0,SWT.TOP);
		formDataCurScoreT.right = new FormAttachment(64,0);
		formDataCurScoreT.bottom = new FormAttachment(8,0);
		
		Label labelCurScoreT = new Label(shellGame,SWT.CENTER);//label Score
		labelCurScoreT.setText("SCORE");
		labelCurScoreT.setFont(fontArial10);
		labelCurScoreT.setBackground(gray);
		labelCurScoreT.setForeground(dark);				 //цвет текста
		labelCurScoreT.setLayoutData(formDataCurScoreT);
		
		FormData formDataCurScoreValue = new FormData(); //расположение значения набранных очков
		formDataCurScoreValue.left = new FormAttachment(44,0);
		formDataCurScoreValue.top = new FormAttachment(labelCurScoreT,0,SWT.BOTTOM);
		formDataCurScoreValue.right = new FormAttachment(64,0);
		formDataCurScoreValue.bottom = new FormAttachment(labelName,0,SWT.BOTTOM);
		
		labelCurScoreValue = new Label(shellGame,SWT.CENTER); //label набранные очки
		labelCurScoreValue.setBackground(gray);
		labelCurScoreValue.setForeground(dark);
		labelCurScoreValue.setLayoutData(formDataCurScoreValue);
		labelCurScoreValue.setText(Integer.toString(currentScore));
		
		FormData formDataBestScoreT = new FormData(); //расположение надписи Best
		formDataBestScoreT.left = new FormAttachment(73,0);
		formDataBestScoreT.top = new FormAttachment(labelCurScoreT,0,SWT.TOP);
		formDataBestScoreT.right = new FormAttachment(94,0);
		formDataBestScoreT.bottom = new FormAttachment(8,0);
		
		labelBestScoreT = new Label(shellGame,SWT.CENTER);//надпись Best
		labelBestScoreT.setText("BEST");
		labelBestScoreT.setFont(fontArial10);
		labelBestScoreT.setBackground(gray);
		labelBestScoreT.setForeground(dark);
		labelBestScoreT.setLayoutData(formDataBestScoreT);
		
		FormData formDataBestScoreValue = new FormData();//расположение значения лучшего результата
		formDataBestScoreValue.left = new FormAttachment(73,0);
		formDataBestScoreValue.top = new FormAttachment(labelBestScoreT,0,SWT.BOTTOM);
		formDataBestScoreValue.right = new FormAttachment(94,0);
		formDataBestScoreValue.bottom = new FormAttachment(labelName,0,SWT.BOTTOM);
		
		labelBestScoreValue = new Label(shellGame,SWT.CENTER);//label лучший результат
		labelBestScoreValue.setBackground(gray);
		labelBestScoreValue.setForeground(dark);
		labelBestScoreValue.setLayoutData(formDataBestScoreValue);
		labelBestScoreValue.setText(Integer.toString(bestScore));
	}
	
	public void createButtons(){
		
		FormData formDataMainMenu = new FormData(); //расположение кнопки MainMenu
		formDataMainMenu.left = new FormAttachment(6,0);
		formDataMainMenu.top = new FormAttachment(16,0);
		formDataMainMenu.right = new FormAttachment(34,0);
		formDataMainMenu.bottom = new FormAttachment(24,0);

		buttonMainMenu = new Button(shellGame,SWT.PUSH);//кнопка MainMeny
		buttonMainMenu.setFont(fontArial10);
		buttonMainMenu.setText("&Main menu");
		buttonMainMenu.setForeground(dark_red);
		buttonMainMenu.setLayoutData(formDataMainMenu);
		
		FormData formDataAI = new FormData();
		formDataAI.left = new FormAttachment(68,0);
		formDataAI.top = new FormAttachment(buttonMainMenu,0,SWT.TOP);
		formDataAI.right = new FormAttachment(96,0);
		formDataAI.bottom = new FormAttachment(buttonMainMenu,0,SWT.BOTTOM);
		
		buttonAI = new Button(shellGame,SWT.PUSH);
		buttonAI.setFont(fontArial10);
		buttonAI.setText("&AI");
		buttonAI.setForeground(dark_red);
		buttonAI.setLayoutData(formDataAI);
		
		FormData formDataRestart = new FormData();// расположение кнопки Restart
		formDataRestart.left = new FormAttachment(37,0);
		formDataRestart.top = new FormAttachment(buttonMainMenu,0,SWT.TOP);
		formDataRestart.right = new FormAttachment(65,0);
		formDataRestart.bottom = new FormAttachment(buttonMainMenu,0,SWT.BOTTOM);
		
		buttonRestart = new Button(shellGame,SWT.PUSH);//кнопка Restart
		buttonRestart.setFont(fontArial10);
		buttonRestart.setText("&Restart");
		buttonRestart.setForeground(dark_red);
		buttonRestart.setLayoutData(formDataRestart);
	}
	
	public void createField(){
		FormData [][] formDataCell= new FormData[4][4];//расположение игрового поля
		labelCell = new CLabel[4][4];				   
		
		for (int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				formDataCell[i][j] = new FormData();
				formDataCell[i][j].left = new FormAttachment(10 + 21*j,0);
				formDataCell[i][j].top = new FormAttachment(29 + 17*i,0);
				formDataCell[i][j].right = new FormAttachment(27 + 21*j,0);
				formDataCell[i][j].bottom = new FormAttachment(42 + 17*i,0);
				
				labelCell[i][j] = new CLabel(shellGame,SWT.CENTER);
				labelCell[i][j].setFont(fontTNR18);
				labelCell[i][j].setForeground(dark);
				labelCell[i][j].setBackground(gray);
				labelCell[i][j].setLayoutData(formDataCell[i][j]);
			}
		}
		
		FormData formDataField = new FormData();
		formDataField.left = new FormAttachment(6,0);
		formDataField.top = new FormAttachment(26,0);
		formDataField.right = new FormAttachment(labelBestScoreT,0,SWT.RIGHT);
		formDataField.bottom = new FormAttachment(96,0);
		
		labelField = new Label(shellGame,SWT.NONE);
		labelField.setBackground(dimGray);
		labelField.setLayoutData(formDataField);
	}
	
	public void createCloseListener(){
		shellGame.addListener(SWT.Close, new Listener()
        {
           @Override
           public void handleEvent(Event event)
           {
        	   saveGame();
        	   
        	   if(currentScore == bestScore)
        		   saveBestScore();
        	   
	    	   shells[0].setVisible(true);
	     	   Display.getCurrent().removeFilter(SWT.KeyUp, listenerKeyboard);
	     	   buttonMainMenu.removeSelectionListener(listenerMainMenu);
	     	   buttonRestart.removeSelectionListener(listenerRestart);
	     	   
	     	   yellow.dispose();
	     	   gold.dispose();
	     	   orange.dispose();
	     	   orangeRed.dispose();
	     	   red.dispose();
	     	   oliveDrab.dispose();
	     	   seaGreen.dispose();
	     	   
	     	   fontArial24.dispose();
	     	   fontArial10.dispose();
	     	   fontTNR18.dispose();
	         	  
	           shellGame.dispose();
           }
        }); 
	}
	
	public void createButtonsListener(){
		
		listenerMainMenu = new SelectionAdapter(){ // действие при нажатии на MainMenu
            
			@Override public void widgetSelected(final SelectionEvent e)
	            {            		
	            		shellGame.close();           		
	            }
		};
		
		buttonMainMenu.addSelectionListener(listenerMainMenu);
		
		listenerRestart = new SelectionAdapter(){
			@Override public void widgetSelected(final SelectionEvent e)
            {
				if(currentScore == bestScore)
					saveBestScore();
				
        		for(int i = 0; i < 4; i++){  //обнуляем элементы массива
        			for(int j = 0 ; j < 4; j++){
        				cellValue[i][j] = 0;
        			}
        		}       
        		
        		currentScore = 0; //значение набранных очков = 0
        		
        		setNumberInCell(0); // размещаем два числа на игровом поле
        		
        		updateField();// обновляем поле
        		
            }
		};
		
		buttonRestart.addSelectionListener(listenerRestart);//действия при нажатии на Restart;
		
	}

	public void createBotListener(){
		
		listenerAI = new SelectionAdapter(){
			@Override public void widgetSelected(final SelectionEvent e)
            {
				while(true){
					
					if(checkEndGame() == false){
						break;
					}
					
					if(moveRight() == true){
						sleep(10);
						if(moveUp() == true){
							sleep(10);
						}
						continue;
					}
					else if(moveUp() == true){
						sleep(10);
						continue;
					}
					else if(moveLeft() == true){
						sleep(10);
						continue;
					}
					else if(moveDown() == true){
						sleep(10);
						continue;
					}
				} 
            }
		};
		
		buttonAI.addSelectionListener(listenerAI);
	}
}