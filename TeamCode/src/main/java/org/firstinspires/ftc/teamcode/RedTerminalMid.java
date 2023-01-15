package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous (name = "RedTerminalMid")

public class RedTerminalMid extends BaseOpMode{

    public static final int SLEEP = 500;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //have the camera read the signal cone
        int tag = detectAprilTags();
        //forwards 78cm, strafe 63cm

        waitForStart();
        rightLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("Tag Detected", tag);
        telemetry.update();
        preLoad();
        moveForwards(63, 0.5);
        sleep(SLEEP);
        strafeRight(40, 0.5);
        sleep(SLEEP);
        moveSlides(MID_POS);
        moveForwards(14, 0.5);
        sleep(SLEEP);
        scissor(scissorClosed);
        sleep(SLEEP);
        moveForwards(-5, 0.5);
        sleep(SLEEP);
        moveSlides(0);
        if (tag == 1){
            strafeRight(-109, 0.5);
        }
        else if (tag == 2){
            strafeRight(-40, 0.5);
        }
        else if (tag == 3){
            strafeRight(32, 0.5);
        }
        else {
            telemetry.addData("No Tag Detected", '0');
            telemetry.update();
        }
    }
}