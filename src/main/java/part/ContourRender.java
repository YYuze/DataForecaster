package part;

import model.LineModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ContourRender {

    private Graphics2D g;

    public ContourRender(BufferedImage target) {
        this.g = target.createGraphics();
    }

    public void drawLine(LineModel[] target) {
        for (LineModel i : target) {
            g.setColor(new Color(i.getLineRGB()));
            g.draw(i.getLine());
        }
    }

    public void drawLine(LineModel target) {
        g.setColor(new Color(target.getLineRGB()));
        g.draw(target.getLine());
    }
}
