
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class Menu {
	
	final public Display display;
	
	static public Game newGame;
	
	public Shell shellMenu;
	
	private Color white,dark_red,dark;
	
	private Font fontArial30,fontArial12;
	
	private Button buttonNewGame,buttonLoadGame,buttonRecords,buttonHelp,buttonExit;
	
	public static void main(String[] args) {
		
		new Menu();
	}
	
	Menu(){
		
		display = Display.getDefault();
		
		fontArial30 = new Font(display,"Arial", 30, SWT.NORMAL);
		fontArial12 = new Font(display,"Arial", 12, SWT.NORMAL);
		
		white = display.getSystemColor(SWT.COLOR_WHITE);
		dark = display.getSystemColor(SWT.COLOR_BLACK);
		dark_red = display.getSystemColor(SWT.COLOR_DARK_RED);
		
		shellMenu = new Shell(display);
		shellMenu.setText("2048 Puzzle");
		shellMenu.setBackground(white);
		shellMenu.setSize(300,400);
		
		createWidgets(shellMenu);
		
		buttonListeners();
		
		shellMenu.open();	
		while (!shellMenu.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public void createWidgets(final Shell shellMenu){        //создание виджетов
		
		FormLayout formLayout = new FormLayout();//раскладка компнентов
		shellMenu.setLayout(formLayout);
		
		FormData formDataName = new FormData();  //расположение названия игры		
		formDataName.top = new FormAttachment(3, 0);
		formDataName.bottom = new FormAttachment(15, 0);
		formDataName.left = new FormAttachment(35, 0);
		formDataName.right = new FormAttachment(65, 0);
		
		Label labelName = new Label(shellMenu, SWT.CENTER);//label название игры
		labelName.setFont(fontArial30);//установка шрифта
		labelName.setText("2048");//установка текста
		labelName.setForeground(dark);//установка цвета текста
		labelName.setLayoutData(formDataName);
		
		createButtons();
	}
	
	public void createButtons(){
		
		FormData formDataNewGame = new FormData();//расположение кнопки New game
		formDataNewGame.top = new FormAttachment(18, 0);
		formDataNewGame.bottom = new FormAttachment(30, 0);
		formDataNewGame.left = new FormAttachment(25, 0);
		formDataNewGame.right = new FormAttachment(75, 0);
		
		buttonNewGame = new Button(shellMenu, SWT.PUSH);//кнопка New game
		buttonNewGame.setFont(fontArial12);
		buttonNewGame.setText("&New game");
		buttonNewGame.setForeground(dark_red);
		buttonNewGame.setLayoutData(formDataNewGame);
		
		FormData formDataLoadGame = new FormData();//расположение кнопки Load game
		formDataLoadGame.top = new FormAttachment(33, 0);
		formDataLoadGame.bottom = new FormAttachment(45, 0);
		formDataLoadGame.left = new FormAttachment(25, 0);
		formDataLoadGame.right = new FormAttachment(75, 0);
		
		buttonLoadGame = new Button(shellMenu, SWT.PUSH);//Кнопка Load game
		buttonLoadGame.setFont(fontArial12);
		buttonLoadGame.setText("&Load game");
		buttonLoadGame.setForeground(dark_red);
		buttonLoadGame.setLayoutData(formDataLoadGame);
		
		FormData formDataRecords = new FormData();//Расположение кнопки Records
		formDataRecords.top = new FormAttachment(48, 0);
		formDataRecords.bottom = new FormAttachment(60, 0);
		formDataRecords.left = new FormAttachment(25, 0);
		formDataRecords.right = new FormAttachment(75, 0);
		
		buttonRecords = new Button(shellMenu, SWT.PUSH);//кнопка Records
		buttonRecords.setFont(fontArial12);
		buttonRecords.setText("&Records");
		buttonRecords.setForeground(dark_red);
		buttonRecords.setLayoutData(formDataRecords);
		
		FormData formDataHelp = new FormData();//расположение кнопки Help
		formDataHelp.top = new FormAttachment(63, 0);
		formDataHelp.bottom = new FormAttachment(75, 0);
		formDataHelp.left = new FormAttachment(25, 0);
		formDataHelp.right = new FormAttachment(75, 0);
		
		buttonHelp = new Button(shellMenu, SWT.PUSH);//кнопка Help
		buttonHelp.setFont(fontArial12);
		buttonHelp.setText("&Help");
		buttonHelp.setForeground(dark_red);
		buttonHelp.setLayoutData(formDataHelp);
		
		FormData formDataExit = new FormData();// расположение кнопки Exit
		formDataExit.top = new FormAttachment(78, 0);
		formDataExit.bottom = new FormAttachment(90, 0);
		formDataExit.left = new FormAttachment(25, 0);
		formDataExit.right = new FormAttachment(75, 0);
		
		buttonExit = new Button(shellMenu, SWT.PUSH);//кнопка ExiT
		buttonExit.setFont(fontArial12);
		buttonExit.setText("E&xit");
		buttonExit.setForeground(dark_red);
		buttonExit.setLayoutData(formDataExit);		
	}
	
	public void buttonListeners(){
		
		buttonNewGame.addSelectionListener(new SelectionAdapter() // действия при нажатии на кнопку New game
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {

            	newGame = new Game(0);
            	
            	newGame.open();//открываем окно с игровым полем              
            }
        });
		
		buttonLoadGame.addSelectionListener(new SelectionAdapter() // действия при нажатии на кнопку New game
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {

            	newGame = new Game(1);
            	
            	newGame.open();//открываем окно с игровым полем
            	
            	newGame.loadGame();//два числа в пустые клетки         
            }
        });

		buttonHelp.addSelectionListener(new SelectionAdapter()//действия при нажатии на кнопку Help
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {
            	openDialogHelp(shellMenu);//открываем диалоговое окно с текстом
            }
        });

		buttonExit.addSelectionListener(new SelectionAdapter()
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {
            	shellMenu.close();//закрываем игру
            }
        });
	}
	
	public void openDialogHelp(Shell shellMenu){              //открытие диалового окна с информацией об игре
															 
		final Shell dialogHelp = new Shell(shellMenu, SWT.APPLICATION_MODAL 
				| SWT.DIALOG_TRIM);
	    dialogHelp.setText("Help");
	    dialogHelp.setSize(430, 340);
		    
		final Label labelDialogHelp = new Label(dialogHelp, SWT.NONE);
		labelDialogHelp.setBounds(10, 10, 410, 320);
    	
		try (SeekableByteChannel fHelpChannel = Files.newByteChannel(Paths.get("Help")) )
        {
        	
		    int fileSize = (int) fHelpChannel.size();
		    ByteBuffer buffer = ByteBuffer.allocate(fileSize);
		    fHelpChannel.read(buffer);
		    buffer.flip();
		    
		    byte str[];
		    str = new byte[fileSize];
		    
		    for(int i = 0; i < fileSize-1; i++){
		    	str[i] = buffer.get();
		    }
		    
		    String text = new String(str);
			
			labelDialogHelp.setText(text);;
					    
		    fHelpChannel.close();

        } catch(InvalidPathException e) {
            System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода " + e);
        }
		
		dialogHelp.open();          	
    }

}

	