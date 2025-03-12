package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
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

public class OdometryCmd extends SequentialCommandGroup {

    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final String side;
    private Command path;

    public OdometryCmd(DriveSubsystem driveSubsystem, String selectedPath) {
        DRIVE_SUBSYSTEM = driveSubsystem;
        this.side = selectedPath;

        // Define trajectory configurations
        TrajectoryConfig forwardConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared
        ).setKinematics(DriveConstants.kDriveKinematics);

        TrajectoryConfig reverseConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared
        ).setKinematics(DriveConstants.kDriveKinematics)
        .setReversed(true);


        Trajectory robotStartRightToTag11 = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(0.27, 0), new Translation2d(1.06, 0.0), new Translation2d(1.553, 0.3)),
          new Pose2d(1.83, 0.62, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory robotStartLeftToTag9 = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.31, 0), new Translation2d(1.06, 0.0), new Translation2d(1.553, -0.3)),
            new Pose2d(1.83, -0.62, new Rotation2d(0)),
            reverseConfig
          );


        Trajectory fromReefTag11ToFeeder = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(-0.35, -1.89),  new Translation2d(0.34, -2.64)),
          new Pose2d(1.03, -3.40, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory fromReefTag2ToFeeder = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(-0.22, 0.10),  new Translation2d(-0.35, 0.19), new Translation2d(-0.62, 0.45)),
          new Pose2d(-0.96, 1.78, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory fromFeederToTag6 = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.8, 0.95)),
            new Pose2d(2.2, 1.5, new Rotation2d(0)),
            reverseConfig
          );



        // Create PID controllers for trajectory tracking
        PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
        PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
        ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints
        );
        thetaController.enableContinuousInput(-Math.PI, Math.PI);


        SwerveControllerCommand robotStartRightFirstPieceCmd = new SwerveControllerCommand(
            robotStartRightToTag11,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(robotStartRightToTag11);
            }
        };

        SwerveControllerCommand  fromFeederToTag6Cmd = new SwerveControllerCommand(
            fromFeederToTag6,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(fromFeederToTag6);
            }
        };

        SwerveControllerCommand fromReefTag11ToFeederCmd  = new SwerveControllerCommand(
            fromReefTag11ToFeeder,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(fromReefTag11ToFeeder );
            }
        };

        SwerveControllerCommand fromReefTag2ToFeederCmd  = new SwerveControllerCommand(
            fromReefTag2ToFeeder,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(fromReefTag2ToFeeder );
            }
        };

        SwerveControllerCommand robotStartLeftToTag9Cmd  = new SwerveControllerCommand(
            robotStartLeftToTag9,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            thetaController,
            DRIVE_SUBSYSTEM::setModuleStates,
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(robotStartLeftToTag9);
            }
        };

        // Select the correct path based on the input
        if (selectedPath.equals("robotStartRightToTag11")) {
            path = robotStartRightFirstPieceCmd;
        } else if (selectedPath.equals("fromReefTag11ToFeeder")) {
            path = fromReefTag11ToFeederCmd;
        } else if(selectedPath.equals("fromReefTag2ToFeeder")){
            path =  fromReefTag2ToFeederCmd;
        }else if(selectedPath.equals("robotStartLeftToTag9")){
            path =  robotStartLeftToTag9Cmd;
        }else if(selectedPath.equals("fromFeederToTag6")){
            path =  fromFeederToTag6Cmd;
        }

        // Command to stop the drivetrain at the end
        Command stopMovement = new InstantCommand(DRIVE_SUBSYSTEM::stopModules, DRIVE_SUBSYSTEM);

        // Add the follow trajectory command to the command group
        addCommands(
            new InstantCommand(() -> DRIVE_SUBSYSTEM.resetOdometry(getInitialPoseForPath(selectedPath))), // Reset odometry
            path,
            stopMovement // Ensure robot fully stops after the path
        );
    }

    /**
     * Checks if the robot has reached the final pose of the trajectory.
     * Prevents drift from keeping the command running indefinitely.
     */
    private boolean hasReachedFinalPose(Trajectory trajectory) {
        Pose2d currentPose = DRIVE_SUBSYSTEM.getPose();
        Pose2d finalPose = trajectory.getStates().get(trajectory.getStates().size() - 1).poseMeters;

        boolean positionReached = currentPose.getTranslation().getDistance(finalPose.getTranslation()) < 0.05; // 5 cm threshold
        boolean angleReached = Math.abs(currentPose.getRotation().getDegrees() - finalPose.getRotation().getDegrees()) < 2.0;

        return positionReached && angleReached;
    }

    /**
     * Gets the initial pose for a given trajectory path.
     */
    private Pose2d getInitialPoseForPath(String selectedPath) {
        
        if (selectedPath.equals("robotStartRightToTag11")) {
            return new Pose2d(0, 0, new Rotation2d(0));
        } else if  (selectedPath.equals("fromReefTag11ToFeeder")) {
            return new Pose2d(0, 0, new Rotation2d(0));
        } else if  (selectedPath.equals("fromReefTag2ToFeeder")) {
            return new Pose2d(0, 0, new Rotation2d(0));
        } else if  (selectedPath.equals("robotStartRightToTag9")) {
            return new Pose2d(0, 0, new Rotation2d(0));
        }else if  (selectedPath.equals("fromFeederToTag6")) {
            return new Pose2d(0, 0, new Rotation2d(0));
        }



        return new Pose2d(); // Default case
    }
}
