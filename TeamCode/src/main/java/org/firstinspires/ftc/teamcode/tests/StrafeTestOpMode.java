package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseOpMode;

@Autonomous(name = "StrafeTest")

public class StrafeTestOpMode extends BaseOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        waitForStart();

            strafeRight(-100, 0.5);
            sleep(1000);
            strafeRight(100, 0.5);
    }
}
