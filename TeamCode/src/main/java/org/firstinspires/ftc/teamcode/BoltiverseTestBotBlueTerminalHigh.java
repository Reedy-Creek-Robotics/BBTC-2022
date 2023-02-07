package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "BoltiverseTestBlueTerminalHigh")

public class BoltiverseTestBotBlueTerminalHigh extends BoltiverseRobotTestBaseOpMode{

    public static final int SLEEP = 100;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //detect april tags
        waitForStart();
        //preload
        moveForwards(82, 0.5);
        sleep(SLEEP);
        moveForwards(-13, 0.5);
        sleep(SLEEP);
        strafeRight(-26, 0.5);
        sleep(SLEEP);
        //score
        strafeRight(24, 0.5);
        sleep(SLEEP);
        moveForwards(71, 0.5);
        sleep(SLEEP);
        moveForwards(-13, 0.5);
        sleep(SLEEP);
        turnRight(90, 0.5);
        sleep(SLEEP);
        //cycling
        for (int i = 0; i < 5; i++) {
            moveForwards(54, 0.5);
            sleep(SLEEP);
            moveForwards(-52, 0.5);
            sleep(SLEEP);
            turnLeft(90, 0.5);
            sleep(SLEEP);
        }
    }
}