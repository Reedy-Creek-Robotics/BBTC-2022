package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@TeleOp(name = "PowerPlayTeleOp")
public class Squashed_OpMode extends LinearOpMode {
    static final double INCREMENT = 0.05;
    double powerFactor = 0.6;
    BNO055IMU imu;
    ElapsedTime timeSinceLastPress;
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;
    int BUTTON_DELAY = 250;
    DcMotor Linear_Slide_Motor;
    Servo left_hand;
    Servo right_hand;
    static final int CYCLE_MS =  500; // period of each cycle

    @Override
    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        Linear_Slide_Motor =hardwareMap.get(DcMotor.class, "linearSlide");
        left_hand = hardwareMap.get(Servo.class, "leftClaw");
        right_hand = hardwareMap.get(Servo.class, "rightClaw");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        initIMU();

        waitForStart();
        timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        while (opModeIsActive()) {
            processClaw();
            processDriving();
            processLinearSlide();
            telemetry.update();
        }
    }

    private void processDriving() {
        double y = Math.pow(-gamepad1.left_stick_y, 3);
        double x = Math.pow(gamepad1.left_stick_x, 3);
        double rx = Math.pow(gamepad1.right_stick_x, 3);
        double botHeading = -imu.getAngularOrientation().firstAngle;
        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        if (gamepad1.dpad_up && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            timeSinceLastPress.reset();
            if (powerFactor < 1) {
                powerFactor += INCREMENT;
            }
            telemetry.addData("Dpad Up Pressed", "");
        }

        if (gamepad1.dpad_down && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            timeSinceLastPress.reset();
            if (powerFactor > 0) {
                powerFactor -= INCREMENT;
            }
            telemetry.addData("Dpad Down Pressed", "");
        }

        //frontLeft.setPower(y + x + rx);
        //backLeft.setPower(y - x + rx);
        //frontRight.setPower(y - x - rx);
        //backRight.setPower(y + x - rx);
        //double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (rotY - rotX + rx);
        double backLeftPower = (rotY + rotX + rx);
        double frontRightPower = (rotY - rotX - rx);
        double backRightPower = (rotY + rotX - rx);

        backLeft.setPower(frontLeftPower * powerFactor);
        frontLeft.setPower(backLeftPower * powerFactor);
        frontRight.setPower(frontRightPower * powerFactor);
        backRight.setPower(backRightPower * powerFactor);

        telemetry.addData("Motor Power", roundNumber(powerFactor));
    }

    private void initIMU() {
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        telemetry.addLine("Imu is Initializing");
        telemetry.update();
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
            telemetry.clear();
            telemetry.addLine("Calibrating");
            telemetry.update();
        }
        telemetry.clear();
        telemetry.addData("Calibration Status", imu.getCalibrationStatus().toString());
        telemetry.update();
    }

    private String roundNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(number);
    }

    private void processLinearSlide(){
        double y = gamepad2.left_stick_y;
        Linear_Slide_Motor.setPower(y / 2);
        telemetry.addData("Linear Slide Power", y);
    }

    double  left_close = 0.25; // Start at halfway position
    double  right_close = 0.7;
    double  right_open = 1;
    double  left_open = 0;

    private void processClaw(){
        // Display the current value
        telemetry.addData("Left Servo Position", "%5.2f", left_close);
        telemetry.addData("Right Servo Position", "%5.2f", right_close);
        telemetry.addData("Left Servo Position", "%5.2f", left_open);
        telemetry.addData("Right Servo Position", "%5.2f", right_open);
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.update();

        if (gamepad1.x && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            left_hand.setPosition(left_close);
            right_hand.setPosition(right_close);
            sleep(CYCLE_MS);
            idle();
            telemetry.addData(">", "X is pressed");
            telemetry.update();
        }

        if (gamepad1.y && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            left_hand.setPosition(left_open);
            right_hand.setPosition(right_open);
            sleep(CYCLE_MS);
            idle();
            telemetry.addData(">", "Y is pressed");
        }

    }
}

