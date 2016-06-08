import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

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
  private Button buttonNewGame, buttonLoadGame, buttonReplay,buttonReplayPlay, buttonSeq, buttonExit,buttonSortS,
  buttonSortJ,buttonStat;

  int index,numberOfSaves;
  String[] fileName;
  int[] score,gameStat;
  String[][] sequencing;
  boolean openReplay;

  public static void main(String[] args) {

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

    buttonSeq = new Button(shellMenu, SWT.PUSH);
    buttonSeq.setFont(fontArial12);
    buttonSeq.setText("Best &sequence");
    buttonSeq.setForeground(dark_red);
    buttonSeq.setLayoutData(formDataHelp);

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
    

    buttonSeq.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        openDialogSeq();
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
  public void openDialogSeq() {

    final Shell dialogSeq = new Shell(shellMenu, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    dialogSeq.setText("Best sequence");
    dialogSeq.setSize(200, 140);

    try(FileReader reader = new FileReader("Replays/replaysList")) {
      getNumberOfSaves();

      fileName = new String[numberOfSaves];
      score = new int[numberOfSaves];

      for(int i = 0; i < numberOfSaves-1; i++) {
        int c,pos = 0;
        char[] buf = new char[11];
        while((c=reader.read()) != ' ') {
          buf[pos] = (char)c;
          pos++;
        }

        fileName[i] = new String(buf,0,pos);

        pos = 0;
        while((c=reader.read()) != '\n') {
          buf[pos] = (char)c;
          pos++;
        }

        score[i] = Integer.parseInt(new String(buf,0,pos));
      }
    }
    catch(IOException ex){   
      System.out.println(ex.getMessage());
    }
    
    int[] ind = getBestSeq();
    String[] moves = {"Up", "Down", "Left", "Right"};

    CLabel textPopMove = new CLabel(dialogSeq,SWT.CENTER);
    textPopMove.setText("Best sequence\n"+moves[ind[0]/16] + " " + 
                                          moves[(ind[0]%16)/4] + " " + 
                                          moves[(ind[0]%16)%4]);
    textPopMove.setBounds(10,10,180,40);
    
    CLabel textBestVal = new CLabel(dialogSeq,SWT.CENTER);
    textBestVal.setText("Number\n"+ind[1]);
    textBestVal.setBounds(10, 60, 180, 40);
    
    dialogSeq.open();
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
    dialogStatistics.setSize(200,240);

    String[] moves = {"Up","Down","Left","Right"};
    getStatistics();
    
    CLabel textPopMove = new CLabel(dialogStatistics,SWT.CENTER);
    textPopMove.setText("Popular movement\n"+moves[gameStat[0]]);
    textPopMove.setBounds(10,10,180,40);
    
    CLabel textBestVal = new CLabel(dialogStatistics,SWT.CENTER);
    textBestVal.setText("Number of pop moves\n"+Integer.toString(gameStat[1]));
    textBestVal.setBounds(10, 60, 180, 40);
    
    CLabel textTurns = new CLabel(dialogStatistics,SWT.CENTER);
    textTurns.setText("Number of turns\n"+Integer.toString(gameStat[2]));
    textTurns.setBounds(10, 110, 180, 40);
    
    CLabel textTurnsPerGame = new CLabel(dialogStatistics,SWT.CENTER);
    textTurnsPerGame.setText("Turns per game\n"+Integer.toString(gameStat[3]));
    textTurnsPerGame.setBounds(10, 160, 180, 40);
    
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
        sortReplays.qSort(score,fileName,0,score.length-1);
        table.removeAll();

        for(int i = 0; i < numberOfSaves; i++) {
          updateTable(table,fileName[i],score[i]);
        }
      }
    });
  }

  public void readFiles() {
    sequencing = new String[numberOfSaves][];
    for(int i = 0; i < numberOfSaves-1; i++) {
      int lineNumber = 0;
      try{  
        File myFile =new File("Replays/"+fileName[i]);
        FileReader fileReader = new FileReader(myFile);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        
        while (lineNumberReader.readLine() != null){
          lineNumber++;
        }
        lineNumberReader.close();
      } catch(IOException e) {
        e.printStackTrace();
      }

      sequencing[i] = new String[lineNumber];

      try(FileReader reader = new FileReader("Replays/"+fileName[i])) {
        for(int j = 0; j < lineNumber; j++) {
          int c,pos = 0;
          char[] buf = new char[6];
          while((c = reader.read()) != ' '){
            buf[pos] = (char)c;
            pos++;
          }
          sequencing[i][j] = new String(buf,0,pos);
          while((c = reader.read()) != '\n') {}
        }
      }
      catch(IOException ex){
          System.out.println(ex.getMessage());
      }
    }
  }

  public void getStatistics() {

    readFiles();

    Statistics stat = new Statistics();
    gameStat = new int[4];
    gameStat = stat.getStatistics(sequencing);
  }

  public void getReplaysList(Table table) {
    try(FileReader reader = new FileReader("Replays/replaysList")) {
      getNumberOfSaves();

      fileName = new String[numberOfSaves];
      score = new int[numberOfSaves];

      for(int i = 0; i < numberOfSaves-1; i++) {
        int c,pos = 0;
        char[] buf = new char[11];
        while((c=reader.read()) != ' ') {
          buf[pos] = (char)c;
          pos++;
        }

        fileName[i] = new String(buf,0,pos);

        pos = 0;
        while((c=reader.read()) != '\n') {
          buf[pos] = (char)c;
          pos++;
        }

        score[i] = Integer.parseInt(new String(buf,0,pos));

        updateTable(table,fileName[i],score[i]);
      }
    }
    catch(IOException ex){   
      System.out.println(ex.getMessage());
    }
  }
  
  
  public int[] getBestSeq() {
    readFiles();

    Sequencing seq = new Sequencing();

    seq.createArrBuf();

    for(int i = 0; i < numberOfSaves-1; i++) {
      for(int j = 0; j < sequencing[i].length/3; j+=3) {
        String[] str = {sequencing[i][j],sequencing[i][j+1],sequencing[i][j+2]};
        seq.check(str);
      }
    }

    return seq.getBestSeq();
  }

  public void getNumberOfSaves() {
    File listFile = new File("Replays");
    File exportFiles[] = listFile.listFiles();
    numberOfSaves = exportFiles.length-1;
  }

  
  public void updateTable(Table table,String fileName,int score) {
    TableItem item = new TableItem(table,SWT.CENTER);
    item.setText(new String[] {fileName,Integer.toString(score)});
  }
}