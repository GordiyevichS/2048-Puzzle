import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

/**
 * Used to create a playing field. It contains methods for saving / loading the game. AI
 * 
 * @author sergey gordiyevich
 * @version 1.0
 * 
 */
public class Game {

  public GamePlay gamePlay;

  public Shell shellGame;

  /**
   * cells game field
   */
  public CLabel labelCell[][];

  /**
   * current scores,best result and logo
   */
  private Label labelCurScoreValue, labelBestScoreValue, labelField, labelBestScoreT, labelName;

  private Listener listenerKeyboard;

  /**
   * buttons
   */
  private Button buttonMainMenu, buttonRestart, buttonAI;

  /**
   * colors cells and buttons
   */
  public Color yellow, gold, orange, orangeRed, red, oliveDrab, seaGreen, white, dimGray, gray,
      dark, dark_red;

  /**
   * fonts
   */
  private Font fontArial24, fontArial10, fontTNR18;

  public Shell[] shells;

  private boolean dialogOpened = false, aiPlays = false, clearFile = true, replayMode = false,
      stopReplay = false;

  private static final int TIMER_INTERVAL = 500;

  public Runnable runnableAI, runnableReplay;

  /**
   * constructor
   * 
   * @param mode new game,load game or replay
   */
  public Game(int mode) {

    shellGame = new Shell(Display.getCurrent());
    shells = Display.getCurrent().getShells();

    final Device device = Display.getCurrent();
    yellow = new Color(device, 255, 255, 0);
    gold = new Color(device, 255, 215, 0);
    orange = new Color(device, 255, 165, 0);
    orangeRed = new Color(device, 255, 69, 0);
    red = new Color(device, 255, 0, 0);
    oliveDrab = new Color(device, 192, 255, 62);
    seaGreen = new Color(device, 67, 205, 128);
    dimGray = new Color(device, 105, 105, 105);
    gray = new Color(device, 190, 190, 190);
    white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
    dark = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    dark_red = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);

    shellGame.setText("2048 Puzzle");
    shellGame.setBackground(white);
    shellGame.setSize(295, 400);

    FormLayout formLayout = new FormLayout();
    shellGame.setLayout(formLayout);

    gamePlay = new GamePlay();

    createWidgets();

    createListeners();

    labelBestScoreValue.setText(String.valueOf(gamePlay.bestScore));

    if (mode != 2) {
      if (mode == 0) {
        gamePlay.setNumberInCell();
        gamePlay.setNumberInCell();
      }
      else
        clearFile = false;

      updateField();
      play();
    } else {
      replayMode = true;
      open();
      buttonRestart.setEnabled(false);
      buttonAI.setEnabled(false);
      replay();
    }
  }

  public void open() {

    shells[1].open();
    shells[0].setVisible(false);
  }

  /**
   * Used to create widgets
   */
  public void createWidgets() {

    createNameLabel();
    createScoreLabels();
    createButtons();
    createField();
  }

  /**
   * Used to create listeners
   */
  public void createListeners() {

    createCloseListener();
    createButtonsListeners();
    createAIListener();
  }

  /**
   * Used to create keyboard listeners
   * 
   */
  public void play() {

    listenerKeyboard = new Listener() {

      @Override
      public void handleEvent(Event e) {

        if (aiPlays == false) {
          if ((e.keyCode == gamePlay.right)) {

            MovesThread movesThread = new MovesThread("right");
            movesThread.join();
            updateField();

            if (checkEndGame() == true) {
              SaveThread saveThread = new SaveThread();
              saveThread.join();
            }
          }

          if (e.keyCode == gamePlay.left) {

            MovesThread movesThread = new MovesThread("left");
            movesThread.join();
            updateField();

            if (checkEndGame() == true) {
              SaveThread saveThread = new SaveThread();
              saveThread.join();
            }
          }

          if (e.keyCode == gamePlay.down) {

            MovesThread movesThread = new MovesThread("down");
            movesThread.join();
            updateField();

            if (checkEndGame() == true) {
              SaveThread saveThread = new SaveThread();
              saveThread.join();
            }
          }

          if (e.keyCode == gamePlay.up) {

            MovesThread movesThread = new MovesThread("up");
            movesThread.join();
            updateField();

            if (checkEndGame() == true) {
              SaveThread saveThread = new SaveThread();
              saveThread.join();
            }
          }
        }
      }
    };

    Display.getCurrent().addFilter(SWT.KeyUp, listenerKeyboard);
  }

  /**
   * Updating the game field after shifts
   */
  public void updateField() {

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (gamePlay.cellValue[i][j] != 0) {

          if (gamePlay.cellValue[i][j] < 8) {
            labelCell[i][j].setBackground(white);
          } else if (gamePlay.cellValue[i][j] >= 8 && gamePlay.cellValue[i][j] < 16) {
            labelCell[i][j].setBackground(gold);
          } else if (gamePlay.cellValue[i][j] >= 16 && gamePlay.cellValue[i][j] < 32) {
            labelCell[i][j].setBackground(orange);
          } else if (gamePlay.cellValue[i][j] >= 32 && gamePlay.cellValue[i][j] < 64) {
            labelCell[i][j].setBackground(orangeRed);
          } else if (gamePlay.cellValue[i][j] >= 64 && gamePlay.cellValue[i][j] < 128) {
            labelCell[i][j].setBackground(red);
          } else if (gamePlay.cellValue[i][j] >= 128 && gamePlay.cellValue[i][j] < 1024) {
            labelCell[i][j].setBackground(yellow);
          } else if (gamePlay.cellValue[i][j] >= 1024 && gamePlay.cellValue[i][j] < 2048) {
            labelCell[i][j].setBackground(oliveDrab);
          } else if (gamePlay.cellValue[i][j] >= 2048) {
            labelCell[i][j].setBackground(seaGreen);
          }

          labelCell[i][j].setText(Integer.toString(gamePlay.cellValue[i][j]));
        } else {
          labelCell[i][j].setText("");
          labelCell[i][j].setBackground(gray);
        }
      }
    }

    if (gamePlay.currentScore > gamePlay.bestScore) {
      gamePlay.bestScore = gamePlay.currentScore;
      labelBestScoreValue.setText(Integer.toString(gamePlay.bestScore));
    }

    labelCurScoreValue.setText(Integer.toString(gamePlay.currentScore));
  }

  /**
   * Check the end of the game
   * 
   * @return true, if end, and false, if not
   */
  public boolean checkEndGame() {

    boolean canOpen = true;
    int count = 0;

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (gamePlay.cellValue[i][j] != 0) {
          count++;
        }
      }
    }

    if (count == 16) {

      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (i < 3 && j < 3) {
            if (gamePlay.cellValue[i][j] == gamePlay.cellValue[i + 1][j]
                || gamePlay.cellValue[i][j] == gamePlay.cellValue[i][j + 1]) {
              canOpen = false;
            }
          } else if (i == 3 && j < 3) {
            if (gamePlay.cellValue[i][j] == gamePlay.cellValue[i][j + 1]) {
              canOpen = false;
            }
          } else if (i < 3 && j == 3) {
            if (gamePlay.cellValue[i][j] == gamePlay.cellValue[i + 1][j]) {
              canOpen = false;
            }
          }
        }
      }
    } else
      canOpen = false;

    if (canOpen == true && dialogOpened == false) {
      dialogOpened = true;
      openDialogDefeat();
      return false;
    }

    return true;
  }

  /**
   * Opening window defeat
   */
  public void openDialogDefeat() {

    final Shell dialogDefeat =
        new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    dialogDefeat.setText("Game over");
    dialogDefeat.setSize(220, 120);

    final Label labelDialogDefeat = new Label(dialogDefeat, SWT.CENTER);
    labelDialogDefeat.setBounds(10, 10, 200, 100);

    labelDialogDefeat.setText("Вы проиграли.\n" + "Вы набрали:"
        + Integer.toString(gamePlay.currentScore) + " балла(ов)");

    dialogDefeat.open();

    while (!dialogDefeat.isDisposed()) {
      if (!Display.getCurrent().readAndDispatch()) {
        Display.getCurrent().sleep();
      }
    }

    dialogOpened = false;
  }

  /**
   * Game saving
   */
  public void saveGame() {

    try (FileChannel fSaveChannel =
        (FileChannel) Files.newByteChannel(Paths.get("Save"), StandardOpenOption.WRITE,
            StandardOpenOption.CREATE)) {

      ByteBuffer buffer = ByteBuffer.allocate(68);
      for (int i = 0; i < 4; i++)
        for (int j = 0; j < 4; j++)
          buffer.putInt(gamePlay.cellValue[i][j]);
      buffer.putInt(gamePlay.currentScore);
      buffer.flip();

      fSaveChannel.write(buffer);

      fSaveChannel.close();
    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода: " + e);
      System.exit(1);
    }
  }

  /**
   * Game loading
   */
  public void loadGame() {

    try (SeekableByteChannel fLoadChannel = Files.newByteChannel(Paths.get("Save"))) {
      long fileSize = fLoadChannel.size();
      ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
      fLoadChannel.read(buffer);
      buffer.flip();

      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          gamePlay.cellValue[i][j] = buffer.getInt();
          labelCell[i][j].setText(String.valueOf(gamePlay.cellValue[i][j]));
        }
      }

      gamePlay.currentScore = buffer.getInt();
      labelCurScoreValue.setText(String.valueOf(gamePlay.currentScore));

      fLoadChannel.close();
    } catch (InvalidPathException e) {
      System.out.println("Ошибка указания пути " + e);
    } catch (IOException e) {
      System.out.println("Ошибка ввода-вывода " + e);
    }

    updateField();
  }

  /**
   * Creating the game logo
   */
  public void createNameLabel() {

    fontArial24 = new Font(Display.getCurrent(), "Arial", 24, SWT.NORMAL);
    fontArial10 = new Font(Display.getCurrent(), "Arial", 10, SWT.NORMAL);
    fontTNR18 = new Font(Display.getCurrent(), "TimesNewRoman", 18, SWT.BOLD);

    FormData formDataName = new FormData();
    formDataName.top = new FormAttachment(3, 0);
    formDataName.left = new FormAttachment(6, 0);
    formDataName.right = new FormAttachment(34, 0);
    formDataName.bottom = new FormAttachment(12, 0);

    labelName = new Label(shellGame, SWT.CENTER);
    labelName.setFont(fontArial24);
    labelName.setText("2048");
    labelName.setForeground(dark);
    labelName.setLayoutData(formDataName);
  }

  /**
   * Current points and the best result
   */
  public void createScoreLabels() {

    FormData formDataCurScoreT = new FormData();
    formDataCurScoreT.left = new FormAttachment(44, 0);
    formDataCurScoreT.top = new FormAttachment(labelName, 0, SWT.TOP);
    formDataCurScoreT.right = new FormAttachment(64, 0);
    formDataCurScoreT.bottom = new FormAttachment(8, 0);

    Label labelCurScoreT = new Label(shellGame, SWT.CENTER);
    labelCurScoreT.setText("SCORE");
    labelCurScoreT.setFont(fontArial10);
    labelCurScoreT.setBackground(gray);
    labelCurScoreT.setForeground(dark);
    labelCurScoreT.setLayoutData(formDataCurScoreT);

    FormData formDataCurScoreValue = new FormData();
    formDataCurScoreValue.left = new FormAttachment(44, 0);
    formDataCurScoreValue.top = new FormAttachment(labelCurScoreT, 0, SWT.BOTTOM);
    formDataCurScoreValue.right = new FormAttachment(64, 0);
    formDataCurScoreValue.bottom = new FormAttachment(labelName, 0, SWT.BOTTOM);

    labelCurScoreValue = new Label(shellGame, SWT.CENTER);
    labelCurScoreValue.setBackground(gray);
    labelCurScoreValue.setForeground(dark);
    labelCurScoreValue.setLayoutData(formDataCurScoreValue);
    labelCurScoreValue.setText(Integer.toString(gamePlay.currentScore));

    FormData formDataBestScoreT = new FormData();
    formDataBestScoreT.left = new FormAttachment(73, 0);
    formDataBestScoreT.top = new FormAttachment(labelCurScoreT, 0, SWT.TOP);
    formDataBestScoreT.right = new FormAttachment(94, 0);
    formDataBestScoreT.bottom = new FormAttachment(8, 0);

    labelBestScoreT = new Label(shellGame, SWT.CENTER);
    labelBestScoreT.setText("BEST");
    labelBestScoreT.setFont(fontArial10);
    labelBestScoreT.setBackground(gray);
    labelBestScoreT.setForeground(dark);
    labelBestScoreT.setLayoutData(formDataBestScoreT);

    FormData formDataBestScoreValue = new FormData();
    formDataBestScoreValue.left = new FormAttachment(73, 0);
    formDataBestScoreValue.top = new FormAttachment(labelBestScoreT, 0, SWT.BOTTOM);
    formDataBestScoreValue.right = new FormAttachment(94, 0);
    formDataBestScoreValue.bottom = new FormAttachment(labelName, 0, SWT.BOTTOM);

    labelBestScoreValue = new Label(shellGame, SWT.CENTER);
    labelBestScoreValue.setBackground(gray);
    labelBestScoreValue.setForeground(dark);
    labelBestScoreValue.setLayoutData(formDataBestScoreValue);
    labelBestScoreValue.setText(Integer.toString(gamePlay.bestScore));
  }

  /**
   * Used to create buttons(main menu, restart)
   */
  public void createButtons() {

    FormData formDataMainMenu = new FormData();
    formDataMainMenu.left = new FormAttachment(6, 0);
    formDataMainMenu.top = new FormAttachment(16, 0);
    formDataMainMenu.right = new FormAttachment(34, 0);
    formDataMainMenu.bottom = new FormAttachment(24, 0);

    buttonMainMenu = new Button(shellGame, SWT.PUSH);
    buttonMainMenu.setFont(fontArial10);
    buttonMainMenu.setText("&Main menu");
    buttonMainMenu.setForeground(dark_red);
    buttonMainMenu.setLayoutData(formDataMainMenu);

    FormData formDataAI = new FormData();
    formDataAI.left = new FormAttachment(68, 0);
    formDataAI.top = new FormAttachment(buttonMainMenu, 0, SWT.TOP);
    formDataAI.right = new FormAttachment(94, 0);
    formDataAI.bottom = new FormAttachment(buttonMainMenu, 0, SWT.BOTTOM);

    buttonAI = new Button(shellGame, SWT.CHECK);
    buttonAI.setFont(fontArial10);
    buttonAI.setText("&AI");
    buttonAI.setForeground(dark_red);
    buttonAI.setLayoutData(formDataAI);

    FormData formDataRestart = new FormData();
    formDataRestart.left = new FormAttachment(37, 0);
    formDataRestart.top = new FormAttachment(buttonMainMenu, 0, SWT.TOP);
    formDataRestart.right = new FormAttachment(65, 0);
    formDataRestart.bottom = new FormAttachment(buttonMainMenu, 0, SWT.BOTTOM);

    buttonRestart = new Button(shellGame, SWT.PUSH);
    buttonRestart.setFont(fontArial10);
    buttonRestart.setText("&Restart");
    buttonRestart.setForeground(dark_red);
    buttonRestart.setLayoutData(formDataRestart);
  }

  /**
   * Used to create game field cells
   */
  public void createField() {

    FormData[][] formDataCell = new FormData[4][4];
    labelCell = new CLabel[4][4];

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        formDataCell[i][j] = new FormData();
        formDataCell[i][j].left = new FormAttachment(10 + 21 * j, 0);
        formDataCell[i][j].top = new FormAttachment(29 + 17 * i, 0);
        formDataCell[i][j].right = new FormAttachment(27 + 21 * j, 0);
        formDataCell[i][j].bottom = new FormAttachment(42 + 17 * i, 0);

        labelCell[i][j] = new CLabel(shellGame, SWT.CENTER);
        labelCell[i][j].setFont(fontTNR18);
        labelCell[i][j].setForeground(dark);
        labelCell[i][j].setBackground(gray);
        labelCell[i][j].setLayoutData(formDataCell[i][j]);
      }
    }

    FormData formDataField = new FormData();
    formDataField.left = new FormAttachment(6, 0);
    formDataField.top = new FormAttachment(26, 0);
    formDataField.right = new FormAttachment(labelBestScoreT, 0, SWT.RIGHT);
    formDataField.bottom = new FormAttachment(96, 0);

    labelField = new Label(shellGame, SWT.NONE);
    labelField.setBackground(dimGray);
    labelField.setLayoutData(formDataField);
  }

  /**
   * Used to create close listener(Return to main menu)
   */
  public void createCloseListener() {

    shellGame.addListener(SWT.Close, new Listener() {
      @Override
      public void handleEvent(Event event) {
        if (aiPlays == true)
          aiPlays = false;

        if (replayMode == false) {
          saveGame();

          if (gamePlay.currentScore == gamePlay.bestScore)
            gamePlay.saveBestScore();

          Display.getCurrent().removeFilter(SWT.KeyUp, listenerKeyboard);
        } else {

          stopReplay = true;
        }

        shells[0].setVisible(true);

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

  /**
   * Used to create buttons listeners
   */
  public void createButtonsListeners() {

    SelectionAdapter listenerMainMenu = new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        shellGame.close();
      }
    };

    buttonMainMenu.addSelectionListener(listenerMainMenu);

    SelectionAdapter listenerRestart = new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {

        buttonAI.setSelection(false);
        aiPlays = false;

        if (gamePlay.currentScore == gamePlay.bestScore)
          gamePlay.saveBestScore();

        for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 4; j++) {
            gamePlay.cellValue[i][j] = 0;
          }
        }

        gamePlay.currentScore = 0;

        gamePlay.setNumberInCell();
        gamePlay.setNumberInCell();

        updateField();
      }
    };

    buttonRestart.addSelectionListener(listenerRestart);

    buttonAI.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (buttonAI.getSelection())
          aiPlays = true;
        else
          aiPlays = false;
      }
    });
  }

  /**
   * Used to create AI listener
   */
  public void createAIListener() {

    SelectionAdapter listenerAI = new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        runnableAI = new Runnable() {
          public void run() {
            if (aiPlays == true) {
              if (checkEndGame() == true) {
                playAI();
                Display.getCurrent().timerExec(TIMER_INTERVAL, this);
              }
            }
          }
        };

        Display.getCurrent().timerExec(TIMER_INTERVAL, runnableAI);
      }
    };

    buttonAI.addSelectionListener(listenerAI);
  }

  /**
   * AI moves
   */
  public void playAI() {

    if (gamePlay.moveRight() == true) {
      gamePlay.moveUp();
    } else if (gamePlay.moveUp() == true) {
      gamePlay.moveRight();
    } else if (gamePlay.moveLeft() == true) {
      gamePlay.moveRight();
    } else if (gamePlay.moveDown() == true) {
      gamePlay.moveUp();
    }

    updateField();
    shellGame.update();
  }

  /**
   * replay mode
   */
  public void replay() {

    try {
      final SeekableByteChannel fLoadChannel = Files.newByteChannel(Paths.get("Replay"));
      final long fileSize = fLoadChannel.size();
      final ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
      fLoadChannel.read(buffer);
      buffer.flip();

      runnableReplay = new Runnable() {

        long curSize = 0;

        public void run() {

          curSize += 68;
          if (curSize > fileSize || stopReplay == true) {
            try {
              fLoadChannel.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
            return;
          }

          for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
              gamePlay.cellValue[i][j] = buffer.getInt();
            }
          }

          gamePlay.currentScore = buffer.getInt();

          updateField();

          Display.getCurrent().timerExec(TIMER_INTERVAL, this);
        }
      };
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    Display.getCurrent().timerExec(TIMER_INTERVAL, runnableReplay);
  }

  /**
   * thread for moves
   */
  class MovesThread implements Runnable {

    Thread thread;
    String move;

    MovesThread(String move) {

      this.move = move;
      thread = new Thread(this);
      thread.start();
    }

    public void join() {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void run() {
      switch (move) {

        case "up":
          gamePlay.moveUp();
          break;
        case "down":
          gamePlay.moveDown();
          break;
        case "left":
          gamePlay.moveLeft();
          break;
        case "right":
          gamePlay.moveRight();
          break;
        default:
          break;
      }
    }
  }

  /**
   * thread for moves saving
   */
  class SaveThread implements Runnable {

    Thread thread;

    SaveThread() {

      thread = new Thread(this);
      thread.start();
    }

    public void join() {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void run() {

      if (clearFile == true) {
        try (FileChannel fSaveChannel =
            (FileChannel) Files.newByteChannel(Paths.get("Replay"), StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

          ByteBuffer buffer = ByteBuffer.allocate(68);
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
              buffer.putInt(gamePlay.cellValue[i][j]);
          buffer.putInt(gamePlay.currentScore);
          buffer.rewind();

          fSaveChannel.write(buffer);

          fSaveChannel.close();
          clearFile = false;
        } catch (InvalidPathException e) {
          System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
          System.out.println("Ошибка ввода-вывода: " + e);
          System.exit(1);
        }
      } else {
        try (FileChannel fSaveChannel =
            (FileChannel) Files.newByteChannel(Paths.get("Replay"), StandardOpenOption.WRITE,
                StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {

          ByteBuffer buffer = ByteBuffer.allocate(68);
          for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
              buffer.putInt(gamePlay.cellValue[i][j]);
          buffer.putInt(gamePlay.currentScore);
          buffer.rewind();

          fSaveChannel.write(buffer);

          fSaveChannel.close();
        } catch (InvalidPathException e) {
          System.out.println("Ошибка указания пути " + e);
        } catch (IOException e) {
          System.out.println("Ошибка ввода-вывода: " + e);
          System.exit(1);
        }
      }
    }
  }
}
