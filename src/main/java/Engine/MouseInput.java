package Engine;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private Vector2f currentPos;
    private Vector2f displVec;
    private Vector2f scroll;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean leftButtonReleased;
    private boolean rightButtonPressed;
    private Vector2f previousPos;
    private long windowHandle;

    public MouseInput(long windowHandle) {
        this.windowHandle = windowHandle;
        currentPos = new Vector2f();
        previousPos = new Vector2f();
        scroll = new Vector2f();
        displVec = new Vector2f();
        leftButtonPressed = false;
        rightButtonPressed = false;
        leftButtonReleased = true;
        inWindow = false;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;
            previousPos.x = (float) xpos;
            previousPos.y = (float) ypos;
        });
        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> inWindow = entered);
        glfwSetScrollCallback(windowHandle, (handle, xoffset, yoffset) -> {
            scroll.x = (float) xoffset;
            scroll.y = (float) yoffset;
        });
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            leftButtonReleased = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE;
        });
    }

    public Vector2f getCurrentPos() {
        return currentPos;
    }

    public Vector2f getScroll() {
        return scroll;
    }

    public void setScroll(Vector2f scroll) {
        this.scroll = scroll;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (inWindow) {
            glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
                currentPos.x = (float) xpos;
                currentPos.y = (float) ypos;
            });

            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;

            displVec.y = (float) deltax;
            displVec.x = (float) deltay;

        }


    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isLeftButtonReleased() {
        return leftButtonReleased;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
