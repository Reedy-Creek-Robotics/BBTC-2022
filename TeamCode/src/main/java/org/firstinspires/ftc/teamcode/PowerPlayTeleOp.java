package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@TeleOp(name = "PowerPlayTeleOp")
public class PowerPlayTeleOp extends LinearOpMode {
    static final double INCREMENT = 0.05;
    double powerFactor = 0.85;
    BNO055IMU imu;
    ElapsedTime timeSinceLastPress;
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;
    DcMotor rightLinearSlide;
    DcMotor leftLinearSlide;
    int BUTTON_DELAY = 250;
    final double scissorClosed = 0.5;
    final double scissorOpen = 0;
    double scissorPosition = scissorClosed;
    final int manualSlideOn = 1;
    final int manualSlideOff = 0;
    int slideToggle = manualSlideOff;
    Servo scissor;

    static final int CYCLE_MS = 500; // period of each cycle

    @Override
    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        rightLinearSlide = hardwareMap.get(DcMotor.class, "rightLinearSlide");
        leftLinearSlide = hardwareMap.get(DcMotor.class, "leftLinearSlide");
        scissor = hardwareMap.get(Servo.class, "scissor");
        //right_hand = hardwareMap.get(Servo.class, "rightClaw");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinearSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        initIMU();

        waitForStart();
        timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        scissor.setPosition(scissorClosed);

        while (opModeIsActive()) {
            processScissor();
            processDriving();
            processLinearSlide();
            processLinearSlidePositions();
            processReadyToGrab();
            processGrab();
            telemetry.update();
        }
    }

    private void processDriving() {
        double y = Math.pow(-gamepad1.left_stick_y, 3);
        double x = Math.pow(gamepad1.left_stick_x, 3);
        double rx = Math.pow(gamepad1.right_stick_x, 3);

        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        //double botHeading = orientation.getYaw(AngleUnit.DEGREES);
      /*  Orientation myRobotOrientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.RADIANS
        );*/
        float botHeading = -imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
      // double botHeading = myRobotOrientation.firstAngle;
        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
        telemetry.addData("Bot Heading", botHeading);

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
       /* this.imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));*/

        this.imu = hardwareMap.tryGet(BNO055IMU.class, "expansionImu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
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

        /*YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double botHeading = orientation.getYaw(AngleUnit.RADIANS);
        telemetry.clear();*/
        telemetry.addData("Calibration Status: ", imu.getCalibrationStatus());
        telemetry.update();
    }

    private String roundNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(number);
    }
    //manual control of the linear slides
    private void processLinearSlide() {
        if (gamepad1.left_trigger >0 || gamepad1.right_trigger >0){
            leftLinearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightLinearSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            double y = gamepad1.left_trigger; // down
            double x = gamepad1.right_trigger; // up
            if (y > 0){
                leftLinearSlide.setPower(y);
                rightLinearSlide.setPower(y);
            }
            if (x > 0){
                leftLinearSlide.setPower(-x);
                rightLinearSlide.setPower(-x);
            }
            if (leftLinearSlide.getCurrentPosition() >= 0 || rightLinearSlide.getCurrentPosition() >= 0){
                leftLinearSlide.setPower(0);
                rightLinearSlide.setPower(0);
            }
            if (x == 0 || y == 0){
                leftLinearSlide.setPower(0);
                rightLinearSlide.setPower(0);
            }
        }

        /*
        double y = gamepad2.left_stick_y;
        leftLinearSlide.setPower(y/2);
        rightLinearSlide.setPower(y/2);
*/

        telemetry.addData("Left Linear Slide Position", leftLinearSlide.getCurrentPosition());
        telemetry.addData("Right Linear Slide Position", rightLinearSlide.getCurrentPosition());
    }

    private void processLinearSlidePositions() {
        // reminder: different slide motors have different tick values
        if (gamepad1.y && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(-2800);
            rightLinearSlide.setTargetPosition(-2800);
            moveSlides();
            //high

        }
        if (gamepad1.a && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(-1192);
            rightLinearSlide.setTargetPosition(-1192);
            moveSlides();
            //low
        }

        if (gamepad1.b && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(-2000);
            rightLinearSlide.setTargetPosition(-2000);
            moveSlides();
            //medium
        }
    }

    protected void moveSlides() {
        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLinearSlide.setPower(1);
        rightLinearSlide.setPower(1);
    }

    private void processScissor() {

        if (gamepad1.right_bumper && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY / 2)) {
            //left_servo.setPosition(scissorOpen);
            if (scissorPosition == scissorOpen) {
                scissorPosition = scissorClosed;
            } else {
                scissorPosition = scissorOpen;
            }
            scissor.setPosition(scissorPosition);

            sleep(CYCLE_MS);
            idle();
            telemetry.addData(">", "X is pressed");
        }

        // Display the current value
        telemetry.addData("Scissor constants", "(open=%5.2f, closed=%5.2f)", scissorOpen, scissorClosed);
        telemetry.addData("Desired Scissor Position", scissorPosition);
        telemetry.addData("Actual Scissor Position", "%5.2f", scissor.getPosition());
    }

    private void processReadyToGrab() {
        if (gamepad1.x && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            //move the slides to the preload position (500)
            //close the scissor
            scissor.setPosition(scissorClosed);
            leftLinearSlide.setTargetPosition(-238);
            rightLinearSlide.setTargetPosition(-238);
            moveSlides();
        }
    }

    // grabbing the cone from ready position
    private void processGrab() {
        if (gamepad1.left_bumper && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(0);
            rightLinearSlide.setTargetPosition(0);
            moveSlides();
            while(opModeIsActive() && leftLinearSlide.isBusy() && rightLinearSlide.isBusy()) {
                idle();
            }
            scissor.setPosition(scissorOpen);
            sleep(1000);
            leftLinearSlide.setTargetPosition(-500);
            rightLinearSlide.setTargetPosition(-500);
            moveSlides();
        }
    }
}

