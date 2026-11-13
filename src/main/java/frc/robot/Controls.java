// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import org.littletonrobotics.junction.AutoLogOutput;

/** Add your docs here. */
public class Controls {
  private Controls() {}

  private static String bindMode;
  private static final String modeAString = "Mode_A";
  private static final String modeBString = "Mode_B";

  public static void staticBinds(CommandXboxController controller, Drive drive) {
    bindChooser(controller, drive);

    // Set Default Mode
    bindMode = modeAString;
    setModeA();

    // Default command, normal field-relative drive
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX()));

    // Lock to 0° when X button is held
    // controller
    //     .x()
    //     .whileTrue(
    //         DriveCommands.joystickDriveAtAngle(
    //             drive,
    //             () -> -controller.getLeftY(),
    //             () -> -controller.getLeftX(),
    //             () -> Rotation2d.kZero));

    // Reset gyro to 0° when B button is pressed
    controller
        .b()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
                    drive)
                .ignoringDisable(true));

    // SNAP TO POSE MADE WITH PID
    // In METERS, METERS, DEGREES
    // controller
    //     .a()
    //     .whileTrue(
    //         DriveCommands.DriveToPose2D(
    //                 drive, new Pose2d(x, y, new Rotation2d(Units.degreesToRadians(theta)))
    //                 ));

    // PATHFIND TO POSE MADE WITH PATHPLANNER
    // In METERS, METERS, RADS
    // controller
    //       .a()
    //       .whileTrue(
    //         pathfindToPose(new Pose2d(x, y,
    //         new Rotation2d(Unit.degreesToRadians(theta)))));
  }

  public static void bindChooser(CommandXboxController driverController, Drive drive) {
    driverController.a().whileTrue(setModeA()); // Default Mode
    driverController.b().whileTrue(setModeB());
  }

  // Default Mode
  public static Command setModeA() {
    return Commands.run(
        () -> {
          bindMode = modeAString;
        });
  }

  // Second Mode
  public static Command setModeB() {
    return Commands.run(
        () -> {
          bindMode = modeBString;
        });
  }

  public String getBindString() {
    return bindMode;
  }
}
