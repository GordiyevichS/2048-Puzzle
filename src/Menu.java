
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	
	final Display display;
	
	static Game newGame;
	
	Shell shellMenu;
	
	Color white,dark_red,dark;
	
	Font font1,font2;
	
	public static void main(String[] args) {
		
		new Menu();
	}
	
	Menu(){
		
		display = Display.getDefault();
		
		font1 = new Font(display,"Arial",30,SWT.NORMAL);
		font2 = new Font(display,"Arial",12,SWT.NORMAL);
		
		white = display.getSystemColor(SWT.COLOR_WHITE);
		dark = display.getSystemColor(SWT.COLOR_BLACK);
		dark_red = display.getSystemColor(SWT.COLOR_DARK_RED);
		
		shellMenu = new Shell(display);
		shellMenu.setText("2048 Puzzle");
		shellMenu.setBackground(white);
		shellMenu.setSize(300,400);
		
		createWidgets(shellMenu);
		
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
		formDataName.top = new FormAttachment(3,0);
		formDataName.bottom = new FormAttachment(15,0);
		formDataName.left = new FormAttachment(35,0);
		formDataName.right = new FormAttachment(65,0);
		
		Label labelName = new Label(shellMenu,SWT.CENTER);//label название игры
		labelName.setFont(font1);//установка шрифта
		labelName.setText("2048");//установка текста
		labelName.setForeground(dark);//установка цвета текста
		labelName.setLayoutData(formDataName);
		
		FormData formDataNewGame = new FormData();//расположение кнопки New game
		formDataNewGame.top = new FormAttachment(18,0);
		formDataNewGame.bottom = new FormAttachment(30,0);
		formDataNewGame.left = new FormAttachment(25,0);
		formDataNewGame.right = new FormAttachment(75,0);
		
		Button buttonNewGame = new Button(shellMenu,SWT.PUSH);//кнопка New game
		buttonNewGame.setFont(font2);
		buttonNewGame.setText("&New game");
		buttonNewGame.setForeground(dark_red);
		buttonNewGame.setLayoutData(formDataNewGame);
		
		buttonNewGame.addSelectionListener(new SelectionAdapter() // действия при нажатии на кнопку New game
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {

            	newGame = new Game();
            	
            	newGame.open();//открываем окно с игровым полем
            	
            	newGame.setNumberInCell(0);//два числа в пустые клетки
            	
            	newGame.updateField();//обновляем игровое поле 
                
                newGame.play();
                
            }
        });
		
		FormData formDataLoadGame = new FormData();//расположение кнопки Load game
		formDataLoadGame.top = new FormAttachment(33,0);
		formDataLoadGame.bottom = new FormAttachment(45,0);
		formDataLoadGame.left = new FormAttachment(25,0);
		formDataLoadGame.right = new FormAttachment(75,0);
		
		Button buttonLoadGame = new Button(shellMenu,SWT.PUSH);//Кнопка Load game
		buttonLoadGame.setFont(font2);
		buttonLoadGame.setText("&Load game");
		buttonLoadGame.setForeground(dark_red);
		buttonLoadGame.setLayoutData(formDataLoadGame);
		
		FormData formDataRecords = new FormData();//Расположение кнопки Records
		formDataRecords.top = new FormAttachment(48,0);
		formDataRecords.bottom = new FormAttachment(60,0);
		formDataRecords.left = new FormAttachment(25,0);
		formDataRecords.right = new FormAttachment(75,0);
		
		Button buttonRecords = new Button(shellMenu,SWT.PUSH);//кнопка Records
		buttonRecords.setFont(font2);
		buttonRecords.setText("&Records");
		buttonRecords.setForeground(dark_red);
		buttonRecords.setLayoutData(formDataRecords);
		
		FormData formDataHelp = new FormData();//расположение кнопки Help
		formDataHelp.top = new FormAttachment(63,0);
		formDataHelp.bottom = new FormAttachment(75,0);
		formDataHelp.left = new FormAttachment(25,0);
		formDataHelp.right = new FormAttachment(75,0);
		
		Button buttonHelp = new Button(shellMenu,SWT.PUSH);//кнопка Help
		buttonHelp.setFont(font2);
		buttonHelp.setText("&Help");
		buttonHelp.setForeground(dark_red);
		buttonHelp.setLayoutData(formDataHelp);
	
		buttonHelp.addSelectionListener(new SelectionAdapter()//действия при нажатии на кнопку Help
        {
            @Override public void widgetSelected(final SelectionEvent e)
            {
            	openDialogHelp(shellMenu);//открываем диалоговое окно с текстом
            }
        });
		
		FormData formDataExit = new FormData();// расположение кнопки Exit
		formDataExit.top = new FormAttachment(78,0);
		formDataExit.bottom = new FormAttachment(90,0);
		formDataExit.left = new FormAttachment(25,0);
		formDataExit.right = new FormAttachment(75,0);
		
		Button buttonExit = new Button(shellMenu,SWT.PUSH);//кнопка ExiT
		buttonExit.setFont(font2);
		buttonExit.setText("E&xit");
		buttonExit.setForeground(dark_red);
		buttonExit.setLayoutData(formDataExit);
		
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
		    
		final Label labelDialogHelp = new Label(dialogHelp,SWT.NONE);
		labelDialogHelp.setBounds(10, 10, 410, 320);
    	
		FileInputStream f = null;		//информация хранится в файле
		try {
			f = new FileInputStream("Help");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	byte[] str = null;
		try {
			str = new byte[f.available()];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			f.read(str);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String text = new String(str);
		
		labelDialogHelp.setText(text);
		
		try {
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dialogHelp.open();          	
    }

}

	