package it.polimi.ingsw2022.eriantys.client.view.cli.scenes.components;

import static it.polimi.ingsw2022.eriantys.client.view.cli.AnsiCodes.*;

public class BlinkingCLIComponent extends AnimatedCLIComponent {

    private enum BlinkState {

        FIRST, SECOND
    }

    private float blinkTime;
    private BlinkState state;
    private String firstColor, secondColor;

    public BlinkingCLIComponent(int width, String[] rows) {

        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }

    public BlinkingCLIComponent(int width, int height) {

        super(width, height);
        firstColor = WHITE_BRIGHT;
        secondColor = BLACK;
        blinkTime = 0.5f;

        setColor(firstColor);
        state = BlinkState.FIRST;
    }

    public void setBlinkTime(float blinkTime) {

        this.blinkTime = blinkTime;
    }

    public void setFirstColor(String firstColor) {

        this.firstColor = firstColor;
        if(state == BlinkState.FIRST) setColor(firstColor);
    }

    public void setSecondColor(String secondColor) {

        this.secondColor = secondColor;
        if(state == BlinkState.SECOND) setColor(secondColor);
    }

    @Override
    protected void update() {

        super.update();
        if(getStateTime() < blinkTime && state == BlinkState.SECOND) {

            setColor(firstColor);
            state = BlinkState.FIRST;
        }
        else if(getStateTime() >= blinkTime && state == BlinkState.FIRST) {

            setColor(secondColor);
            state = BlinkState.SECOND;
        }
        if(getStateTime() > blinkTime * 2) reset();
    }
}
