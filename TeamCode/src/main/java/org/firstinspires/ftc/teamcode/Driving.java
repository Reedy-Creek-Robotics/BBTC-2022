package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@TeleOp
public class Driving extends LinearOpMode {
    static final double INCREMENT   = 0.05;
    double powerFactor = 0.6;
    BNO055IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        initIMU();

        waitForStart();
        int BUTTON_DELAY = 250;
        ElapsedTime timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        while(opModeIsActive()){
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            //double botHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;
            double botHeading = imu.getAngularOrientation().firstAngle;
            double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
            double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

            if (gamepad1.dpad_up&& (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
                timeSinceLastPress.reset();
                if( powerFactor < 1 ) {
                    powerFactor += INCREMENT;
                }
                telemetry.addData("Dpad Up Pressed", "");
            }

            if (gamepad1.dpad_down&& (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
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
            double frontLeftPower = (rotY + rotX + rx);
            double backLeftPower = (rotY - rotX + rx);
            double frontRightPower = (rotY - rotX - rx);
            double backRightPower = (rotY + rotX - rx);

            backLeft.setPower(frontLeftPower * powerFactor);
            frontLeft.setPower(backLeftPower * powerFactor);
            frontRight.setPower(frontRightPower * powerFactor);
            backRight.setPower(backRightPower * powerFactor);

            telemetry.addData("Motor Power", roundNumber(powerFactor));
            telemetry.update();
        }
    }

    private void initIMU(){
        this.imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        telemetry.addLine("Imu is Initializing");
        telemetry.update();
        while(!isStopRequested() && !imu.isGyroCalibrated()){
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
}
