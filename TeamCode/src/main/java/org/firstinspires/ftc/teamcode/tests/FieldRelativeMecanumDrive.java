package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Disabled
@TeleOp(name = "FieldRelativeMecanumDrive")
public class FieldRelativeMecanumDrive extends LinearOpMode {
    static final double INCREMENT = 0.05;
    double powerFactor = 0.85;
    IMU imu;
    ElapsedTime timeSinceLastPress;
    DcMotor backLeft;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor frontRight;

    int BUTTON_DELAY = 250;

    @Override
    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        initIMU();

        waitForStart();
        timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        while (opModeIsActive()) {
            processDriving();
            telemetry.update();
        }
    }

    private void processDriving() {
        double y = Math.pow(-gamepad1.left_stick_y, 3);
        double x = Math.pow(gamepad1.left_stick_x, 3);
        double rx = Math.pow(gamepad1.right_stick_x, 3);

        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        //double botHeading = -orientation.getYaw(AngleUnit.RADIANS);
        Orientation myRobotOrientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.RADIANS
        );

        float botHeading = -myRobotOrientation.firstAngle;
        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
        telemetry.addLine("Bot Heading = " + botHeading);

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
        this.imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        /*
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
        */

        // test that the IMU is initialized?
        //YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        //double botHeading = orientation.getYaw(AngleUnit.RADIANS);
        Orientation myRobotOrientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.RADIANS
        );
        double botHeading = myRobotOrientation.firstAngle;
        telemetry.clear();
        telemetry.addLine("Calibration Status: " + Double.valueOf(botHeading).toString());
        telemetry.update();
    }

    private String roundNumber(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        return df.format(number);
    }
}

