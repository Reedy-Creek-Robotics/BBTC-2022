package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RedLeftRoute1")
public class RedLeftAutoRoute1 extends BaseOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();
        preLoad();
        moveForwards(133, 0.5);
        strafeRight(33, 0.5);
        moveSlides(4260);
        moveForwards(10,0.5);
        scissor(scissorOpen);
        sleep(250);
        scissor(scissorClosed);
        strafeRight(-92, 0.5);
        moveSlides(-4260);
    }

    public void backup() {
        preLoad();
        strafeRight(-160, 0.5);
        moveSlides(4260);
        moveForwards(10, 0.5);
        //10 cm -- might change (tweaking will be needed)
        //scissor(scissorClosed);
        //sleep(5000); //for test
        //scissor(scissorOpen); //for test
        sleep(250);
        scissor(scissorClosed);
        sleep(250);
        moveForwards(-10, 0.5);
        sleep(250);
        strafeRight(27.5, 0.5);
        sleep(250);
        moveForwards(-55, 0.5);
        //sleep(20000); //remove after test!!!!!!!!!!!!!!
    }
}