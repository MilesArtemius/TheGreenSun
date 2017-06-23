package classes;

import classes.NoControllers.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by User on 18.03.2017.
 */
public class ResizableCanvas extends Canvas {

    GameRulez gr = GameRulez.get(true);
    static HashMap<String, Double> gm;
    double x = 0;
    double y = 0;
    double t = 1;
    static double AT = 0;
    static AnimationTimer at;
    private static GraphicsContext gc;
    static boolean AntiJumper = false;
    static int MOVEMENTER = 0;
    static int MOVEMENTER2 = 0;
    Level level;
    static Double param;
    static WritableImage [] wim = new WritableImage [9];
    Canvas structure;
    static double currentTranslationX = 0;
    static double currentTranslationY;
    int multiplier = 0;

    public double Standartify(int parameter, char definitor) {
        if (definitor == 'w') {
            return (double) parameter/602*getWidth();
        } else {
            return (double) parameter/500*getHeight();
        }
    }

    public ResizableCanvas() {
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());
    }

    private void redraw() {
        if ((getWidth() > 0) && (getHeight() > 0)) {
            {
                param = ((getHeight() > getWidth()) ? (getWidth()) : (getHeight()));
                gm = gr.getRulez(getWidth(), getHeight(), param);
                System.out.println(gm.toString());

                x = gm.get("BASIC_STATE_X");
                y = gm.get("BASIC_STATE_Y");
            }

            level = Depacker.getStartedLevel(getClass());

            gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            gc.setFill(Color.WHEAT);
            gc.fillRect(0, 0, getWidth(), getHeight());



            structure = new Canvas(level.Width * gm.get("BLOCK_SIZE"), level.Height * gm.get("BLOCK_SIZE"));
            GraphicsContext str_gc = structure.getGraphicsContext2D();
            str_gc.clearRect(0, 0, getWidth(), getHeight());



            for (int i = 0; i < level.level.length; i++) {
                for (int j = 0; j < level.level[i].length; j++) {
                    try {
                        str_gc.drawImage(OuterFunctions.scale(level.level[i][j].texture, gm.get("BLOCK_SIZE").intValue(), gm.get("BLOCK_SIZE").intValue(), (!gm.get("IMG_QUALITY").equals(0.0))), gm.get("BLOCK_SIZE") * i, gm.get("BLOCK_SIZE") * j);
                    } catch (NullPointerException npe) {
                        npe.getMessage();
                    }
                }
            }

            //str_gc.translate(3*getWidth(), 0);


            //System.out.println(wim[0].getWidth() / getWidth() + "\n" + wim[0].getHeight() / getHeight());
            currentTranslationX = -getWidth();
            //gc.drawImage(wim [0], 0, 0);
        }

        if (at == null) {
            at = new AnimationTimer() {
                @Override
                public void handle(long now) {

                    System.out.println();
                    System.out.println(AT);
                    System.out.println(getWidth());


                    // Секция объявления фона: на канвах рисуется прямоугольник определённого фона.
                    gc.setFill(Color.WHEAT);
                    gc.fillRect(AT, 0, getWidth() + AT, getHeight());

                    ScreenDrawer(gc, getWidth(), getHeight(), AT, 0);

                    //
                    gc.drawImage(OuterFunctions.scale(gr.getBlockz().get("sample").texture, gm.get("BLOCK_SIZE").intValue(), gm.get("BLOCK_SIZE").intValue(), (!gm.get("IMG_QUALITY").equals(0.0))), x, y); //0 - bad, 1 - good;

                    //

                    //gc.drawImage(OuterFunctions.scale(gr.getBlockz().get("floor").texture, gm.get("BLOCK_SIZE").intValue(), gm.get("BLOCK_SIZE").intValue(), (!gm.get("IMG_QUALITY").equals(0.0))), Standartify(1000, 'w'), Standartify(200, 'h'));
                    //gc.drawImage(OuterFunctions.scale(gr.getBlockz().get("floor").texture, gm.get("BLOCK_SIZE").intValue(), gm.get("BLOCK_SIZE").intValue(), (!gm.get("IMG_QUALITY").equals(0.0))), Standartify(299, 'w'), Standartify(200, 'h'));
                    //gc.drawImage(OuterFunctions.scale(gr.getBlockz().get("floor").texture, gm.get("BLOCK_SIZE").intValue(), gm.get("BLOCK_SIZE").intValue(), (!gm.get("IMG_QUALITY").equals(0.0))), Standartify(200, 'w'), Standartify(200, 'h'));

                    //
                    if (MOVEMENTER == 1) {
                        y = y - (gm.get("SPEED") * t - gm.get("GRAVITY") * t * t / 2) / gm.get("MULTIPLIER");
                        t += 0.3;
                        if (y >= gm.get("BASIC_STATE_Y")) {
                            t = 1;
                            y = gm.get("BASIC_STATE_Y");
                            if (AntiJumper) {
                                MOVEMENTER = 0;
                                AntiJumper = false;
                            }
                        }
                    }
                    //
                    switch (MOVEMENTER2) {
                        case 1:
                            x += gm.get("MOVEMENT");
                            //if (x >= (sSize.width/3*2+AT)) {
                            gc.translate(-gm.get("MOVEMENT"), 0);
                            AT += gm.get("MOVEMENT");
                            //}

                            break;
                        case 2:
                            x -= gm.get("MOVEMENT");
                            gc.translate(+gm.get("MOVEMENT"), 0);
                            AT -= gm.get("MOVEMENT");
                            break;
                    }
                }
            };
        }
    }

    public void jump(int moves) {
        if (!(moves == 0)) {
            MOVEMENTER = moves;
        } else {
            AntiJumper = true;
        }
        at.start();
    }

    public void move(int moves) {
        MOVEMENTER2 = moves;
        at.start();
    }

    public void start() {
        at.start();
    }

    public void getLevel(String pathTo) {
        try(ZipInputStream zin = new ZipInputStream(new FileInputStream(pathTo)))
        {
            ZipEntry entry;
            String name;
            long size;
            while((entry = zin.getNextEntry())!=null){

                name = entry.getName(); // получим название файла
                size=entry.getSize();  // получим его размер в байтах
                System.out.printf("Название: %s \t размер: %d \n", name, size);
            }
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    public void ScreenDrawer(GraphicsContext gc, double screenwidth, double screenheight, double ATX, double ATY) {
        System.out.println(currentTranslationX);
        System.out.println(currentTranslationX + screenwidth);
        System.out.println((ATX >= (currentTranslationX + screenwidth)));
        if (ATX >= (currentTranslationX + screenwidth)) {
            System.out.println("redrawed");
            if (ATX != 0) {
                multiplier++;
            }
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.AQUA);
            for (int i = 0; i < 9; i++) {
                params.setViewport(new Rectangle2D(screenwidth * ((i % 3) + multiplier - 1), 0, screenwidth, screenheight));
                wim[i] = structure.snapshot(params, null);
            }
            currentTranslationX += screenwidth;
        } else {
            /*gc.drawImage(OuterFunctions.scale(wim[4], 100, 100, true), 0, 0);
            gc.drawImage(OuterFunctions.scale(wim[0], 100, 100, true), 100, 0);
            gc.drawImage(OuterFunctions.scale(wim[1], 100, 100, true), 200, 0);
            gc.drawImage(OuterFunctions.scale(wim[2], 100, 100, true), 300, 0);
            gc.drawImage(OuterFunctions.scale(wim[3], 100, 100, true), 400, 0);
            gc.drawImage(OuterFunctions.scale(wim[5], 100, 100, true), 500, 0);
            gc.drawImage(OuterFunctions.scale(wim[6], 100, 100, true), 600, 0);
            gc.drawImage(OuterFunctions.scale(wim[7], 100, 100, true), 700, 0);
            gc.drawImage(OuterFunctions.scale(wim[8], 100, 100, true), 800, 0);*/
            gc.drawImage(wim[4], 0 + (screenwidth * multiplier), 0);
            gc.drawImage(wim[0], -screenwidth + (screenwidth * multiplier), -screenheight);
            gc.drawImage(wim[1], 0 + (screenwidth * multiplier), -screenheight);
            gc.drawImage(wim[2], +screenwidth + (screenwidth * multiplier), -screenheight);
            gc.drawImage(wim[3], -screenwidth + (screenwidth * multiplier), 0);
            gc.drawImage(wim[5], +screenwidth + (screenwidth * multiplier), 0);
            gc.drawImage(wim[6], -screenwidth + (screenwidth * multiplier), +screenheight);
            gc.drawImage(wim[7], 0 + (screenwidth * multiplier), +screenheight);
            gc.drawImage(wim[8], +screenwidth + (screenwidth * multiplier), +screenheight);
        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
