//c14210182 Mouritus Huangtara M

import Engine.Object;
import Engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class mainProject {
    Camera camera = new Camera();
    private Window window = new Window(800, 800, "Hello World");
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    private ArrayList<Object> objects = new ArrayList<>();

    private ArrayList<Object> Environment = new ArrayList<>();

    private boolean dragDrop = false;
    private float posX = 0.0f;
    private float posY = 0.025f;
    private float posZ = 3f;
    private boolean state = true;
    private float rotate = 0f;
    private int FPSorTPSorCinemaMode = 0;// 0 fps 1 tps 2 cinema


    private ArrayList<Double> valueArray = new ArrayList<>();
    private ArrayList<Boolean> stateArray = new ArrayList<>();



    public static void main(String[] args) {
        new mainProject().run();
    }

    public Vector2f convertRange(Vector2f pos, float height, float width) {
        float OldMax = 255, OldMin = 0, NewMax = 1, NewMin = 0, OldValue = 208, NewRange, NewValue, OldRange;
        OldRange = (OldMax - OldMin);
        NewRange = (NewMax - NewMin);
        NewValue = (((OldValue - OldMin) * NewRange) / OldRange) + NewMin;
        System.out.println(NewValue);
        return new Vector2f((((pos.x - 0) * (1 - (-1))) / (width - 0) + (-1)), (((pos.y - 0) * (1 - (-1))) / (height - 0) + (-1)) * -1);
    }

    public void init() {
        window.init();
        GL.createCapabilities();

//        camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(0.0f));

        //Wall
        Environment.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 1f), "resources/Blender/Project/BackWall.obj", "resources/Blender/Project/Textures/level0.png"
        ));
        //Floor
        Environment.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 0f), "resources/Blender/Project/FLoor.obj", "resources/Blender/Project/Textures/Carpet.jpg"
        ));


        //Extra Room
        Environment.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.89f, 0.9f, 0.66f, 0f), "resources/Blender/Project/trap.obj"
        ));

        //Extra Room
        Environment.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.83f, 0.87f, 0.65f, 0f), "resources/Blender/Project/ceil.obj"
        ));

        Environment.get(0).getChildObject().get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f, 0.5f, 0.5f, 0f), "resources/Blender/Project/light.obj"
        ));

        Environment.get(0).getChildObject().get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0f, 0.25f, 0f, 0f), "resources/Blender/Project/vent.obj"
        ));

        Environment.get(0).translateObject(0f,-1f,0f);


        objects.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f, 1f, 0.3f, 0f), "resources/Blender/Project/player.obj"
        ));
        objects.get(0).translateObject(0f,-3f,-7f);
        objects.get(0).rotateObject((float)Math.toRadians(180),0f,1f,0f);
        objects.get(0).scaleObject(0.3f,0.3f,0.3f);

        objects.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0f, 0f, 0f, 0f), "resources/Blender/Project/scp.obj"
        ));
        objects.get(1).translateObject(23f,-3.3f,14f);
        objects.get(1).rotateObject((float)Math.toRadians(90),0f,-1f,0f);
        objects.get(1).scaleObject(0.185f,0.185f,0.185f);


//        System.out.println(Environment.get(0).getVertices());
    }

    public boolean checkCollide(float x, float y, float z){
        System.out.println(x +", "+y+", "+z );
        boolean floor = y <= Environment.get(0).getChildObject().get(0).updateCenterPointObject().get(1);
        boolean borderWall =
                z >= Environment.get(0).updateCenterPointObject().get(2) + 9.8 ||
                z <= Environment.get(0).updateCenterPointObject().get(2) - 9.8 ||
                x >= Environment.get(0).updateCenterPointObject().get(0) + 10 ||
                x <= Environment.get(0).updateCenterPointObject().get(0) - 9.5;
        boolean wall1 =
                (z <= Environment.get(0).updateCenterPointObject().get(2) - 0.05 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) - 4.5 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) + 1.3 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) + 1.5);
        boolean wall2 =
                (z <= Environment.get(0).updateCenterPointObject().get(2) - 2.5 &&
                 z >= Environment.get(0).updateCenterPointObject().get(2) - 4.5 &&
                 x >= Environment.get(0).updateCenterPointObject().get(0) - 1.42 &&
                 x <= Environment.get(0).updateCenterPointObject().get(0) + 6.9);
        boolean wall3 =
                (z <= Environment.get(0).updateCenterPointObject().get(2) + 3.7 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) - 4.5 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) + 6.5 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) + 6.8)
                ;
        boolean TWall =
                (z <= Environment.get(0).updateCenterPointObject().get(2) -2.5 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) - 7.4 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -3 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -2.7) ||
                (z <= Environment.get(0).updateCenterPointObject().get(2) -6.9 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) - 7.4 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -4.9 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -0.25);
        boolean SideWall =
                (z <= Environment.get(0).updateCenterPointObject().get(2) + 8.9 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) + 0.75 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -7.3 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -6.95) ||
                (z <= Environment.get(0).updateCenterPointObject().get(2) -0.1 &&
                z >= Environment.get(0).updateCenterPointObject().get(2) - 8.1 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -7.3 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -6.95);
        boolean trap =
                (z >= Environment.get(0).updateCenterPointObject().get(2) + 3.6 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -1.6 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -1.3) ||

                (z >= Environment.get(0).updateCenterPointObject().get(2) + 3.6 &&
                z <= Environment.get(0).updateCenterPointObject().get(2) + 3.8 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -4.5 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -1.3)||

                (z >= Environment.get(0).updateCenterPointObject().get(2) + 3.6 &&
                z <= Environment.get(0).updateCenterPointObject().get(2) + 7.2 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -4.5 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -4.3)||

                (z >= Environment.get(0).updateCenterPointObject().get(2) + 3.6 &&
                x >= Environment.get(0).updateCenterPointObject().get(0) -5.7 &&
                x <= Environment.get(0).updateCenterPointObject().get(0) -5.1)
                ;
        if(borderWall || wall1 || wall2 || wall3 || TWall || SideWall || trap){
            return true;
        }
        return false;
    }


    public void move(){
        float move = 0.015f;
        float x = objects.get(0).updateCenterPointObject().get(0);
        float y = objects.get(0).updateCenterPointObject().get(1);
        float z = objects.get(0).updateCenterPointObject().get(2);

        float movex = 0;
        float movez = 0;

        //Maju
        if (window.isKeyPressed(GLFW_KEY_W)) {
            if (!checkCollide(x, y, z - 0.1f)) {
                movez += -move;
            }
        }
        //Kiri
        if (window.isKeyPressed(GLFW_KEY_A)) {
            if (!checkCollide(x-0.1f, y, z)) {
                movex += -move;
            }
        }
        //Mundur
        if (window.isKeyPressed(GLFW_KEY_S)) {
            if (!checkCollide(x, y, z + 0.1f)) {
                movez += move;
            }
        }
        //Kanan
        if (window.isKeyPressed(GLFW_KEY_D)) {
            if (!checkCollide(x+0.1f, y, z)) {
                movex += move;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_I)) {
            camera.moveForward(move);

        }

        if (window.isKeyPressed(GLFW_KEY_J)) {
            camera.moveLeft(move);
        }

        if (window.isKeyPressed(GLFW_KEY_K)) {
            camera.moveBackwards(move);
        }

        if (window.isKeyPressed(GLFW_KEY_L)) {
            camera.moveRight(move);
        }
            //SCP moves
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            objects.get(1).translateObjectAnimate(0f, 0f, 0.5f);
        }

        objects.get(0).translateObjectAnimate(movex, 0f, movez);


        if (window.isKeyPressed(GLFW_KEY_F)) {
            FPSorTPSorCinemaMode = 0;
            state = true;
        }

        if (window.isKeyPressed(GLFW_KEY_T)) {
            FPSorTPSorCinemaMode = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_C)) {
            FPSorTPSorCinemaMode = 2;
            state = true;
        }
    }
    public void input(){
        move();

        if (window.getMousInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - window.getMousInput().getScroll().y * 0.1f);
            window.getMousInput().setScroll(new Vector2f(0f,0f));
        }
    }
    //Jarak collision kalau objectnya lingkaran
//    public float getDist(float x1, float y1, float z1, float x2, float y2, float z2) {
//        return (float) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y2 - y1), 2) + Math.pow(Math.abs(z2 - z1), 2));
//    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(1f, 0.3f, 0.0f, 0.0f);
            GL.createCapabilities();
            glEnable(GL_DEPTH_TEST);

//            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
//            glDepthMask(true);
//            glDepthFunc(GL_LEQUAL);
//            glDepthRange(0.0f, 1.0f);
            glDisableVertexAttribArray(0);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Vector3f tempCenterPoint = objects.get(0).updateCenterPointObject();
            if (FPSorTPSorCinemaMode == 0) {

                    camera.setPosition(tempCenterPoint.x, tempCenterPoint.y + 1.3f, tempCenterPoint.z);

                    Vector2f displayVector = window.getMousInput().getDisplVec();
                    camera.addRotation((float) Math.toRadians(displayVector.x * 0.1), (float) Math.toRadians(displayVector.y * 0.1));

                    objects.get(0).translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
                    objects.get(0).rotateObjectAnimate((float) Math.toRadians(-displayVector.y * 0.1), 0f, 1f, 0f);
                    objects.get(0).translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);



            }

            else if (FPSorTPSorCinemaMode == 1) {

                Vector2f displayVector = window.getMousInput().getDisplVec();
                camera.addRotation(0, (float) Math.toRadians(displayVector.y * 0.1));

                objects.get(0).translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
                objects.get(0).rotateObjectAnimate((float) Math.toRadians(-displayVector.y * 0.1), 0f, 1f, 0f);
                objects.get(0).translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

                camera.setPosition(tempCenterPoint.x - (1.2f * (float) Math.sin(camera.getRotation().y)), tempCenterPoint.y + 1.3f, tempCenterPoint.z + 1.2f * (float) Math.cos(camera.getRotation().y));


            }

            else if (FPSorTPSorCinemaMode == 2) {

                Vector2f displayVector = window.getMousInput().getDisplVec();
                camera.addRotation((float) Math.toRadians(displayVector.x * 0.1), (float) Math.toRadians(displayVector.y * 0.1));

            }

            for (Object object : Environment) {
                object.draw(camera, projection);
            }
            for (Object object : objects) {
                object.draw(camera, projection);
            }




//            for (Object object : objectsRectangle) {
//                object.draw(camera, projection);
//            }
//
//            for (Object object : objectPointControl) {
//                object.drawLine(camera, projection);
//            }
//
//            for (Object object : objectPointCurve) {
//                object.drawLine(camera, projection);
//            }

            input();


            // Restore state
            glDisableVertexAttribArray(0);

            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
