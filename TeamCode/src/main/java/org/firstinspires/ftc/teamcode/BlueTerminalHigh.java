package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@Autonomous (name = "BlueTerminalHigh")

public class BlueTerminalHigh extends BaseOpMode {

    public static final int SLEEP = 100;

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
        // getting to the high junction
        preLoad();
        moveForwards(126, 0.5);
        sleep(SLEEP);
        strafeRight(-23, 0.5);
        sleep(SLEEP);
        // strafing and scoring preload
        moveSlides(HIGH_POS);
        moveForwards(14, 0.5);
        sleep(SLEEP);
        scissor(scissorClosed);
        sleep(SLEEP);
        moveForwards(-8, 0.5);
        sleep(SLEEP);
        moveSlides(0);
        //cycling
        for (int i = 0; i < 5; i++) {
            moveForwards(54, 0.5);
            sleep(SLEEP);
            moveForwards(-52, 0.5);
            sleep(SLEEP);
            turnLeft(90, 0.5);
            sleep(SLEEP);
            // parking
            if (tag == 1) {
                strafeRight(-32, 0.5);
            } else if (tag == 2) {
                strafeRight(30, 0.5);
            } else if (tag == 3) {
                strafeRight(83, 0.5);
            } else {
                telemetry.addData("No Tag Detected", '0');
                telemetry.update();
            }
        }
    }
}