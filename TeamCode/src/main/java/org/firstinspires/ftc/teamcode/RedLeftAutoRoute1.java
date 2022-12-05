package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "RedLeftRoute1")

public class RedLeftAutoRoute1 extends LinearOpMode{

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    public int distance;
    private int frontLeftPos;
    private int frontRightPos;
    private int backLeftPos;
    private int backRightPos;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeftPos = 0;
        backRightPos = 0;
        frontLeftPos = 0;
        frontRightPos = 0;

        waitForStart();
        //double distance = 17.855;
        moveForwards(24, 0.25);
    }

    // distance is in cm
    private void moveForwards(double distance, double speed) {
        double target = distance * 17.855;
        backLeftPos += target;
        frontLeftPos += target;
        backRightPos += target;
        frontRightPos += target;

        backLeft.setTargetPosition(backLeftPos);
        backRight.setTargetPosition(backRightPos);
        frontLeft.setTargetPosition(frontLeftPos);
        frontRight.setTargetPosition(frontRightPos);
    }
}
