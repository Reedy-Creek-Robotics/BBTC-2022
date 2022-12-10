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
public class PowerPlayTeleOp extends LinearOpMode {
    static final double INCREMENT = 0.05;
    double powerFactor = 0.6;
    BNO055IMU imu;
    ElapsedTime timeSinceLastPress;
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;
    DcMotor rightLinearSlide;
    DcMotor leftLinearSlide;
    int BUTTON_DELAY = 250;
    final double scissorClosed = 0.8;
    final double scissorOpen = 0;
    double scissorPosition = scissorClosed;
    final int manualSlideOn = 1;
    final int manualSlideOff = 0;
    int slideToggle = manualSlideOff;
    Servo left_servo;

    static final int CYCLE_MS =  500; // period of each cycle

    @Override
    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        rightLinearSlide = hardwareMap.get(DcMotor.class, "rightLinearSlide");
        leftLinearSlide = hardwareMap.get(DcMotor.class, "leftLinearSlide");
        left_servo = hardwareMap.get(Servo.class, "scissor");
        //right_hand = hardwareMap.get(Servo.class, "rightClaw");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftLinearSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        initIMU();

        waitForStart();
        timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        left_servo.setPosition(scissorClosed);

        while (opModeIsActive()) {
            processScissor();
            processDriving();
            processLinearSlide();
            processLinearSlidePositions();
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
        if (gamepad1.left_bumper &&(timeSinceLastPress.milliseconds() >= BUTTON_DELAY)){
            if (slideToggle == manualSlideOff){
                slideToggle = manualSlideOn;
                telemetry.addData("Manual Slide", "ON");
            }
            else{
                slideToggle = manualSlideOff;
                telemetry.addData("Manual Slide", "OFF");
            }

            timeSinceLastPress.reset();
        }
        double y = gamepad1.left_trigger;
        leftLinearSlide.setPower(y/2);
        rightLinearSlide.setPower(y/2);

        double x = gamepad1.right_trigger;
        leftLinearSlide.setPower(-x/2);
        rightLinearSlide.setPower(-x/2);
        /*
        double y = gamepad2.left_stick_y;
        leftLinearSlide.setPower(y/2);
        rightLinearSlide.setPower(y/2);
*/

        telemetry.addData("Left Linear Slide Position", leftLinearSlide.getCurrentPosition());
        telemetry.addData("Right Linear Slide Position", rightLinearSlide.getCurrentPosition());
    }

    private void processLinearSlidePositions() {

        if (gamepad1.y && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(1840);
            rightLinearSlide.setTargetPosition(1803);
            moveSlides();

        }
        if (gamepad1.a && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(3048);
            rightLinearSlide.setTargetPosition(3030);
            moveSlides();
        }
        if (gamepad1.b && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            leftLinearSlide.setTargetPosition(2013);
            rightLinearSlide.setTargetPosition(2008);
            moveSlides();
        }
    }

    protected void moveSlides(){
        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLinearSlide.setPower(0.15);
        rightLinearSlide.setPower(0.15);
    }

    private void processScissor(){

        if (gamepad1.right_bumper && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
            //left_servo.setPosition(scissorOpen);
            if (scissorPosition == scissorOpen) {
                scissorPosition = scissorClosed;
            }
            else {
                scissorPosition = scissorOpen;
            }
            left_servo.setPosition(scissorPosition);

            sleep(CYCLE_MS);
            idle();
            telemetry.addData(">", "X is pressed");
        }

        // Display the current value
        telemetry.addData("Scissor constants",  "(open=%5.2f, closed=%5.2f)", scissorOpen, scissorClosed);
        telemetry.addData("Desired Scissor Position", scissorPosition);
        telemetry.addData("Actual Scissor Position", "%5.2f", left_servo.getPosition());
    }
}

