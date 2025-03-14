package frc.robot.commands.driveCommands;

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
import frc.robot.Constants.pathConstants;
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


        Trajectory depositFirstPieceRobotRight = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(0.835, 0.43)),
          new Pose2d(1.67, 0.86, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory depositFirstPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.31, 0), new Translation2d(1.06, 0.0), new Translation2d(1.553, -0.3)),
            new Pose2d(1.83, -0.62, new Rotation2d(0)),
            reverseConfig
          );


        Trajectory retrieveSecondPieceRobotRight = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(0.46, -2.17)),
          new Pose2d(0.92, -4.34, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory retrieveSecondPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(-0.98, 1.63)),
          new Pose2d(-1.96, 3.26, new Rotation2d(0)),
          reverseConfig
        );

        Trajectory depositSecondPieceRobotRight = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.55, 1.55)),
            new Pose2d(1.1, 2.1, new Rotation2d(0)),
            reverseConfig
          );
        
          
        Trajectory depositSecondPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.8, 0.95)),
            new Pose2d(2.2, 1.5, new Rotation2d(0)),
            reverseConfig
        );


        Trajectory retrieveThirdPieceRobotRight = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(-0.35, -1.89),  new Translation2d(0.34, -2.64)),
            new Pose2d(1.03, -3.40, new Rotation2d(0)),
            reverseConfig
          );

          Trajectory depositThirdPieceRobotRight = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.8, 0.95)),
            new Pose2d(2.2, 1.5, new Rotation2d(0)),
            reverseConfig
          );



          Trajectory turnPath = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(),
            new Pose2d(-0.5, -0.5, new Rotation2d(60)),
            reverseConfig
          );




        // Create PID controllers for trajectory tracking
        PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
        PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
        ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints
        );
        thetaController.enableContinuousInput(-Math.PI, Math.PI);


        SwerveControllerCommand depositFirstPieceRobotRightCmd = new SwerveControllerCommand(
            depositFirstPieceRobotRight,
            DRIVE_SUBSYSTEM::getPose,
            DriveConstants.kDriveKinematics,
            xController,
            yController,
            new ProfiledPIDController(AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints),
            (states) -> {
                DRIVE_SUBSYSTEM.setModuleStates(states); 
            }, 
            DRIVE_SUBSYSTEM
        ) {
            @Override
            public boolean isFinished() {
                return super.isFinished() || hasReachedFinalPose(depositFirstPieceRobotRight);
            }
        };

        SwerveControllerCommand retrieveSecondPieceRobotRightCmd = new SwerveControllerCommand(
            retrieveSecondPieceRobotRight,
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
                return super.isFinished() || hasReachedFinalPose(retrieveSecondPieceRobotRight);
            }
        };

        SwerveControllerCommand depositSecondPieceRobotRightCmd = new SwerveControllerCommand(
            depositSecondPieceRobotRight,
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
                return super.isFinished() || hasReachedFinalPose(depositFirstPieceRobotRight);
            }
        };

    

        SwerveControllerCommand depositSecondPieceRobotLeftCmd = new SwerveControllerCommand(
            depositSecondPieceRobotLeft,
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
                return super.isFinished() || hasReachedFinalPose(depositSecondPieceRobotRight );
            }
        };

        SwerveControllerCommand depositFirstPieceRobotLeftCmd  = new SwerveControllerCommand(
            depositFirstPieceRobotLeft,
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
                return super.isFinished() || hasReachedFinalPose(depositFirstPieceRobotLeft);
            }
        };


        SwerveControllerCommand retrieveThirdPieceRobotRightCmd = new SwerveControllerCommand(
            retrieveThirdPieceRobotRight,
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
                return super.isFinished() || hasReachedFinalPose(retrieveThirdPieceRobotRight);
            }
        };

        
        SwerveControllerCommand depositThirdPieceRobotRightCmd = new SwerveControllerCommand(
            depositThirdPieceRobotRight,
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
                return super.isFinished() || hasReachedFinalPose(depositThirdPieceRobotRight );
            }
        };

        
        SwerveControllerCommand turnPathCmd = new SwerveControllerCommand(
            turnPath,
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
                return super.isFinished() || hasReachedFinalPose(depositFirstPieceRobotRight);
            }
        };


        // Select the correct path based on the input
        if (selectedPath.equals(pathConstants.onePieceDepositRobotLeft)) {
            path = depositFirstPieceRobotLeftCmd;
        } else if (selectedPath.equals(pathConstants.onePieceDepositRobotRight)) {
            path = depositFirstPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.twoPieceRetrieveRobotRight)){
            path =  retrieveSecondPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.twoPieceDepositRobotRight)){
            path =  depositSecondPieceRobotRightCmd;
        } else if(selectedPath.equals(pathConstants.threePieceRetrieveRobotRight)){
            path =  retrieveThirdPieceRobotRightCmd;
        } else if(selectedPath.equals(pathConstants.threePieceDepositRobotRight)){
            path =  depositThirdPieceRobotRightCmd;
        }else if(selectedPath.equals("turnPath")){
            path = turnPathCmd; 
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
        return new Pose2d(0, 0, new Rotation2d(0));
    }
}
