import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Menu
 * It allows you to start a new game or load game
 * @author sergey gordiyevich
 * @version 1.0
 */
public class Menu {

  public final Display display;

  static public Game newGame;

  public Shell shellMenu,dialogReplays,dialogStatistics;

  private Color white, dark_red, dark;

  /**
   * fonts 
   */
  private Font fontArial30, fontArial12;

  /**
   * buttons
   */
  private Button buttonNewGame, buttonLoadGame, buttonReplay,buttonReplayPlay, buttonHelp, buttonExit,buttonSortS,
  buttonSortJ,buttonStat;

  int index,numberOfSaves;
  String[] fileName;
  int[] score,gameStat;
  int[][] sequencing;
  boolean openReplay;

  public static void main(String[] argameStat) {

    new Menu();
  }

  /**
   * constructor
   */
  public Menu() {

    display = Display.getDefault();

    fontArial30 = new Font(display, "Arial", 30, SWT.NORMAL);
    fontArial12 = new Font(display, "Arial", 12, SWT.NORMAL);

    white = display.getSystemColor(SWT.COLOR_WHITE);
    dark = display.getSystemColor(SWT.COLOR_BLACK);
    dark_red = display.getSystemColor(SWT.COLOR_DARK_RED);

    shellMenu = new Shell(display,SWT.DIALOG_TRIM);
    shellMenu.setText("2048 Puzzle");
    shellMenu.setBackground(white);
    shellMenu.setSize(300, 400);

    openReplay = false;
    
    createWidgets(shellMenu);

    createButtonsListeners();

    shellMenu.open();
    while (!shellMenu.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

  /**
   * Used to create widgets
   * @param shellMenu current application window
   */
  public void createWidgets(final Shell shellMenu) {

    FormLayout formLayout = new FormLayout();
    shellMenu.setLayout(formLayout);

    FormData formDataName = new FormData();
    formDataName.top = new FormAttachment(3, 0);
    formDataName.bottom = new FormAttachment(15, 0);
    formDataName.left = new FormAttachment(35, 0);
    formDataName.right = new FormAttachment(65, 0);

    Label labelName = new Label(shellMenu, SWT.CENTER);
    labelName.setFont(fontArial30);
    labelName.setText("2048");
    labelName.setForeground(dark);
    labelName.setLayoutData(formDataName);

    createButtons();
  }

  /**
   * Used to create buttons(new game, load game, help, quit)
   */
  public void createButtons() {

    FormData formDataNewGame = new FormData();
    formDataNewGame.top = new FormAttachment(18, 0);
    formDataNewGame.bottom = new FormAttachment(30, 0);
    formDataNewGame.left = new FormAttachment(25, 0);
    formDataNewGame.right = new FormAttachment(75, 0);

    buttonNewGame = new Button(shellMenu, SWT.PUSH);
    buttonNewGame.setFont(fontArial12);
    buttonNewGame.setText("&New game");
    buttonNewGame.setForeground(dark_red);
    buttonNewGame.setLayoutData(formDataNewGame);

    FormData formDataLoadGame = new FormData();
    formDataLoadGame.top = new FormAttachment(33, 0);
    formDataLoadGame.bottom = new FormAttachment(45, 0);
    formDataLoadGame.left = new FormAttachment(25, 0);
    formDataLoadGame.right = new FormAttachment(75, 0);

    buttonLoadGame = new Button(shellMenu, SWT.PUSH);
    buttonLoadGame.setFont(fontArial12);
    buttonLoadGame.setText("&Load game");
    buttonLoadGame.setForeground(dark_red);
    buttonLoadGame.setLayoutData(formDataLoadGame);

    FormData formDataRecords = new FormData();
    formDataRecords.top = new FormAttachment(48, 0);
    formDataRecords.bottom = new FormAttachment(60, 0);
    formDataRecords.left = new FormAttachment(25, 0);
    formDataRecords.right = new FormAttachment(75, 0);

    buttonReplay = new Button(shellMenu, SWT.PUSH);
    buttonReplay.setFont(fontArial12);
    buttonReplay.setText("&Replay");
    buttonReplay.setForeground(dark_red);
    buttonReplay.setLayoutData(formDataRecords);

    FormData formDataHelp = new FormData();
    formDataHelp.top = new FormAttachment(63, 0);
    formDataHelp.bottom = new FormAttachment(75, 0);
    formDataHelp.left = new FormAttachment(25, 0);
    formDataHelp.right = new FormAttachment(75, 0);

    buttonHelp = new Button(shellMenu, SWT.PUSH);
    buttonHelp.setFont(fontArial12);
    buttonHelp.setText("&Help");
    buttonHelp.setForeground(dark_red);
    buttonHelp.setLayoutData(formDataHelp);

    FormData formDataExit = new FormData();
    formDataExit.top = new FormAttachment(78, 0);
    formDataExit.bottom = new FormAttachment(90, 0);
    formDataExit.left = new FormAttachment(25, 0);
    formDataExit.right = new FormAttachment(75, 0);

    buttonExit = new Button(shellMenu, SWT.PUSH);
    buttonExit.setFont(fontArial12);
    buttonExit.setText("E&xit");
    buttonExit.setForeground(dark_red);
    buttonExit.setLayoutData(formDataExit);
  }

  /**
   * Used to create buttons listeners 
   */
  public void createButtonsListeners() {

    buttonNewGame.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {

        newGame = new Game(0,null);
        newGame.open();
      }
    });

    buttonLoadGame.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {

        newGame = new Game(1,null);
        newGame.open();
        newGame.loadGame();
      }
    });

    buttonReplay.addSelectionListener(new SelectionAdapter(){

      @Override
      public void widgetSelected(final SelectionEvent e){
        openDialogReplays();
      }
    });
    

    buttonHelp.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        openDialogHelp();
      }
    });

    buttonExit.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        shellMenu.close();
      }
    });
  }

  /**
   * Opens the help window
   * @param shellMenu current application window
   */
  public void openDialogHelp() {

    final Shell dialogHelp = new Shell(shellMenu, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    dialogHelp.setText("Help");
    dialogHelp.setSize(430, 340);

    final Label labelDialogHelp = new Label(dialogHelp, SWT.NONE);
    labelDialogHelp.setBounds(10, 10, 410, 285);

    try (SeekableByteChannel fHelpChannel = Files.newByteChannel(Paths.get("Help"))) {

      int fileSize = (int) fHelpChannel.size();
      ByteBuffer buffer = ByteBuffer.allocate(fileSize);
      fHelpChannel.read(buffer);
      buffer.flip();

      byte[] str;
      str = new byte[fileSize];

      for (int i = 0; i < fileSize - 1; i++) {
        str[i] = buffer.get();
      }

      String text = new String(str);
      labelDialogHelp.setText(text);;

      fHelpChannel.close();
    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода " + e);
    }

    dialogHelp.open();
  }

  public void openDialogReplays() {
    dialogReplays = new Shell(shellMenu, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    dialogReplays.setText("Replays");
    dialogReplays.setSize(300, 360);
    
    final Table table = new Table(dialogReplays,SWT.BORDER);

    TableColumn tc1 = new TableColumn(table,SWT.CENTER);
    TableColumn tc2 = new TableColumn(table,SWT.CENTER);
    tc1.setWidth(140);
    tc1.setText("Save");
    tc2.setWidth(140);
    tc2.setText("Score");
    table.setHeaderVisible(true);
    
    table.setBounds(10,10,280,280);
    getReplaysList(table);
    createDialogReplaysButtons(dialogReplays,table);
    
    dialogReplays.open();
  }

  public void openDialogStatistic(Shell dialogReplays) {
    dialogStatistics = new Shell(dialogReplays, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    
    dialogStatistics.setText("Statistics");
    dialogStatistics.setSize(200,250);

    getStatistics();
    
    CLabel textPopCell = new CLabel(dialogStatistics,SWT.CENTER);
    textPopCell.setText("Popular cell");
    textPopCell.setBounds(10,10,180,20);
    
    CLabel[][] cells = new CLabel[4][4];
    for(int i = 0; i < 4; i++) {
      for(int j = 0; j < 4; j++) {
        cells[i][j] = new CLabel(dialogStatistics,SWT.CENTER);
        cells[i][j].setBackground(white);
        cells[i][j].setBounds(43+30*j, 40+30*i, 20, 20);
      }
    }
    
    cells[gameStat[0]/4][gameStat[0]%4].setBackground(dark);
    
    CLabel textBestVal = new CLabel(dialogStatistics,SWT.CENTER);
    textBestVal.setText("Best value\n"+Integer.toString(gameStat[1]));
    textBestVal.setBounds(10, 160, 180, 40);
    
    dialogStatistics.open();
  }

  public void createDialogReplaysButtons(Shell diealogReplays,Table table) {
    buttonReplayPlay = new Button(dialogReplays,SWT.PUSH);
    buttonReplayPlay.setText("Start");    
    buttonReplayPlay.setBounds(10, 290, 70, 30);
    
    if(table.getItemCount() == 0) {
      buttonReplayPlay.setEnabled(false);
    }
 
    buttonSortS = new Button(dialogReplays,SWT.PUSH);
    buttonSortS.setText("SortS");
    buttonSortS.setBounds(80, 290, 70, 30);
    
    buttonSortJ = new Button(dialogReplays,SWT.PUSH);
    buttonSortJ.setText("SortJ");
    buttonSortJ.setBounds(150, 290, 70, 30);

    buttonStat = new Button(dialogReplays,SWT.PUSH);
    buttonStat.setText("Stat");
    buttonStat.setBounds(220,290,70,30);
    
    createDRButtonsListeners(table);
  }

  public void createDRButtonsListeners(final Table table) {
    buttonReplayPlay.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        index = table.getSelectionIndex();
        dialogReplays.close();
        newGame = new Game(2,fileName[index]);
      }
    });

    buttonStat.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        index = table.getSelectionIndex();
        openDialogStatistic(dialogReplays);
      }
    });

    buttonSortS.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        SortReplays sortReplays = new SortReplays();
        sortReplays.sort(fileName,score);
        table.removeAll();

        for(int i = 0; i < numberOfSaves; i++) {
          updateTable(table,fileName[i],score[i]);
        }
      }
    });

    buttonSortJ.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        SortReplaysJava sortReplays = new SortReplaysJava();
        long start = System.nanoTime();
        sortReplays.qSort(score,fileName,0,score.length-1);
        long end = System.nanoTime();
        System.out.println(end-start);
        table.removeAll();

        for(int i = 0; i < numberOfSaves; i++) {
          updateTable(table,fileName[i],score[i]);
        }
      }
    });
  }

  public void getStatistics() {

    sequencing = new int[numberOfSaves][];
    for(int i = 0; i < numberOfSaves; i++) {
      try (SeekableByteChannel fLoadChannel = Files.newByteChannel(Paths.get("Replays/"+fileName[i]))) {
        int fileSize = (int) fLoadChannel.size();
        ByteBuffer buffer = ByteBuffer.allocate(fileSize);
        fLoadChannel.read(buffer);
        buffer.flip();

        sequencing[i] = new int[fileSize/68*16];

        for(int j = 0; j < fileSize/68; j++) {
          for (int k = 16*j; k < 16*j+16; k++) {
            sequencing[i][k] = buffer.getInt();
          }

          buffer.getInt();
        }

        fLoadChannel.close();
      } catch (InvalidPathException e) {
        System.out.println("Ошибка указания пути " + e);
      } catch (IOException e) {
        System.out.println("Ошибка ввода-вывода " + e);
      }
    }

    Statistics stat = new Statistics();
    gameStat = new int[2];
    gameStat = stat.getStatistics(sequencing);
  }

  public void getReplaysList(Table table) {
    try (SeekableByteChannel fHelpChannel = Files.newByteChannel(Paths.get("Replays/replaysList"))) {

      File listFile = new File("Replays");
      File exportFiles[] = listFile.listFiles();
      numberOfSaves = exportFiles.length-1;
      int fileSize = (int) fHelpChannel.size();
      ByteBuffer buffer = ByteBuffer.allocate(fileSize);
      fHelpChannel.read(buffer);
      buffer.flip();

      fileName = new String[numberOfSaves];
      score = new int[numberOfSaves];

      for(int i = 0; i < numberOfSaves; i++) {
        char[] _fileName = new char[11];
        char[] symb = new char[2];
        char temp;
        int index = 0;

        while(true) {
          temp = buffer.getChar();
          if(temp == ' ')
            break;
          _fileName[index] = temp;
          index++;
        }
        symb[0] = temp;
        score[i] = buffer.getInt();
        symb[1] = buffer.getChar();

        fileName[i] = new String(_fileName,0,index);

        updateTable(table,fileName[i],score[i]);
      }

      fHelpChannel.close();
    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода " + e);
    }
  }
  
  public void updateTable(Table table,String fileName,int score) {
    TableItem item = new TableItem(table,SWT.CENTER);
    item.setText(new String[] {fileName,Integer.toString(score)});
  }
}