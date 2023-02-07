package org.firstinspires.ftc.teamcode.tests;

import android.graphics.Color;
import android.os.DropBoxManager;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BaseOpMode;

@TeleOp
public class ColorSensorTest<colorSensor> extends LinearOpMode {
    public void runOpMode() {
        NormalizedColorSensor colorSensor;
        final float[] hsvValues = new float[3];

        waitForStart();
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");


        while (opModeIsActive()){
            if (colorSensor instanceof SwitchableLight) {
                SwitchableLight light = (SwitchableLight)colorSensor;
                light.enableLight(!light.isLightOn());
            }
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);
            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.update();
        }
    }
}
