package Layer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ContourRender {

    private Graphics2D g;

    public ContourRender(BufferedImage target) {
        this.g = target.createGraphics();
    }

    public void drawLine(Line[] target) {
        for (Line i : target) {
            g.setColor(new Color(i.getLineRGB()));
            g.draw(i.getLine());
        }
    }

    public void drawLine(Line target) {
        g.setColor(new Color(target.getLineRGB()));
        g.draw(target.getLine());
    }
}
