package it.polimi.ingsw2022.eriantys.view.cli.components;

public class AnimatedCLIComponent extends CLIComponent {

    private float stateTime, deltaTime;
    private long lastUpdateTime;
    protected float speedX, speedY;

    public AnimatedCLIComponent(int width, String[] rows) {

        this(width, rows.length);
        for (int n = 0; n < rows.length; n++) setRow(n, rows[n]);
    }

    public AnimatedCLIComponent(int width, int height) {

        super(width, height);
        resetAnimation();
        speedX = 0;
        speedY = 0;
    }

    @Override
    public void printToFrame(String[][] frame) {

        update();
        super.printToFrame(frame);
    }

    protected void update() {

        if (lastUpdateTime >= 0) deltaTime = (System.nanoTime() - lastUpdateTime) / 1000000000f;
        stateTime += deltaTime;
        lastUpdateTime = System.nanoTime();
        setX(getX() + speedX * deltaTime);
        setY(getY() + speedY * deltaTime);
    }

    public float getStateTime() {

        return stateTime;
    }

    public float getDeltaTime() {

        return deltaTime;
    }

    public float getSpeedX() {

        return speedX;
    }

    public void setSpeedX(float speedX) {

        this.speedX = speedX;
    }

    public float getSpeedY() {

        return speedY;
    }

    public void setSpeedY(float speedY) {

        this.speedY = speedY;
    }

    public void resetAnimation() {

        stateTime = 0;
        deltaTime = 0;
        lastUpdateTime = -1;
    }
}
