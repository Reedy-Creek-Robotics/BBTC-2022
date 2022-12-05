package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "RedLeftRoute1")
public class RedLeftAutoRoute1 extends BaseOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();
        strafeRight(-160, 0.5);
        sleep(250);
        //score
        strafeRight(27.5, 0.5);
        sleep(250);
        moveForwards(-55, 0.5);
    }
}