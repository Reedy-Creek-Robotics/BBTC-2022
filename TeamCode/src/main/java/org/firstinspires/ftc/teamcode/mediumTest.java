package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous (name = "MediumGoalTest")
@Disabled
public class mediumTest extends BaseOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //have the camera read the signal cone
        int tag = detectAprilTags();
        //forwards 78cm, strafe 63cm

        waitForStart();
        telemetry.addData("Tag Detected", tag);
        telemetry.update();
        preLoad();
        moveForwards(68, 0.5);
        sleep(500);
        strafeRight(-30, 0.5);
        sleep(500);
        moveSlides(2910);
        moveForwards(15, 0.5);
        sleep(500);
        scissor(scissorClosed);
        sleep(500);
        moveForwards(-15, 0.5);
        if (tag == 1){

        }
        else if (tag == 2){

        }
        else if (tag == 3){

        }
        else {
            telemetry.addData("No Tag Detected", '0');
            telemetry.update();
        }
    }
}

