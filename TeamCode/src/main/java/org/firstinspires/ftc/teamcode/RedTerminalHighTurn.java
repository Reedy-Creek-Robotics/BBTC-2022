package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@Autonomous (name = "RedTerminalHighTurn")

public class RedTerminalHighTurn extends BaseOpMode{

    public static final int SLEEP = 500;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //have the camera read the signal cone
        int tag = detectAprilTags();
        //forwards 78cm, strafe 63cm

        waitForStart();
        turnLeft(90,0.5);
        /*rightLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Tag Detected", tag);
        telemetry.update();
        // getting to the high junction
        preLoad();
        moveForwards(126, 0.5);
        sleep(SLEEP);
        strafeRight(34, 0.5);
        sleep(SLEEP);
        // strafing and scoring
        moveSlides(HIGH_POS);
        moveForwards(14, 0.5);
        sleep(SLEEP);
        scissor(scissorClosed);
        sleep(SLEEP);
        moveForwards(-8, 0.5);
        sleep(SLEEP);
        moveSlides(0);
        // parking
        if (tag == 1){
            strafeRight(-86, 0.5);
        }
        else if (tag == 2){
            strafeRight(-30, 0.5);
            moveForwards(-10, 0.5);
        }
        else if (tag == 3){
            strafeRight(32, 0.5);
            moveForwards(-10, 0.5);
        }
        else {
            telemetry.addData("No Tag Detected", '0');
            telemetry.update();
        } */
    }
}