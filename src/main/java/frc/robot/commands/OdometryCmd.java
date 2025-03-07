// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveSubsystem;

import java.util.List;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.None;

public class OdometryCmd extends SequentialCommandGroup {

    private final DriveSubsystem DRIVE_SUBSYSTEM;

    private final String side; 
    
    private Command path; 

    public OdometryCmd(DriveSubsystem driveSubsystem, String selectedPath) {
        // Store the subsystem
        DRIVE_SUBSYSTEM = driveSubsystem;
        this.side = selectedPath; 

        // Define trajectory configuration
        TrajectoryConfig forwardConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared
        ).setKinematics(DriveConstants.kDriveKinematics);

        TrajectoryConfig reverseConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared
        ).setKinematics(DriveConstants.kDriveKinematics)
        .setReversed(true);

        // Define the trajectory you want to follow (in this case a simple straight path with a curve)
        Trajectory RobotRightFromlineToReef = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)), // Start pose
            List.of(new Translation2d(-0.05, 0.5)),  // Waypoints
            new Pose2d(0.43, 1.41, new Rotation2d(0)), // End pose
            forwardConfig
        );
        Trajectory  RobotLeftFromlineToReef = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)), // Start pose
          List.of(new Translation2d(0.09, -0.01), new Translation2d(0.33, 0.04),new Translation2d(1, 0),new Translation2d(1.15, 0.24)),// Waypoints
          new Pose2d(1.41, 0.43, new Rotation2d(0)), // End pose
          reverseConfig
      );

      Trajectory  Othertrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)), // Start pose
        List.of(new Translation2d(0.05, 0.07)), // Waypoints
        new Pose2d(0.25, 0.14, new Rotation2d(0)), // End pose
        reverseConfig
    );


        // Create the PID controllers for trajectory tracking
        PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
        PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
        ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints
        );
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        // Define the SwerveControllerCommand for following the trajectory
        SwerveControllerCommand RobotRightFromlineToReefCmd = new SwerveControllerCommand(
            RobotRightFromlineToReef,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        );

        SwerveControllerCommand RobotLeftFromlineToReefCmd = new SwerveControllerCommand(
            RobotLeftFromlineToReef ,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        );
        


        if(selectedPath == "right"){
            path = RobotRightFromlineToReefCmd; 
        }else if(selectedPath == "left"){
            path = RobotLeftFromlineToReefCmd; 
        }
 
        // Add the follow trajectory command to the command group
        addCommands(
            path
        );
            
       
    }
}
