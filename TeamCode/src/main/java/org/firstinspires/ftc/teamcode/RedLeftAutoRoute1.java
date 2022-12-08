package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RedLeftRoute1")
public class RedLeftAutoRoute1 extends BaseOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();
        preLoad();

        waitForStart();
        //moving to high junction
        //strafeRight(-160, 0.5);
        //lifting slides to score
        moveSlides(-1000);
        //moveForwards(10, 0.5);
        //10 cm -- might change (tweeking will be needed)
        scissor(scissorClosed);
        //score
        //sleep(250);
        //strafeRight(27.5, 0.5);
        //sleep(250);
        //moveForwards(-55, 0.5);
    }
}