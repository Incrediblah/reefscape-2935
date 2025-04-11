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
        ).setKinematics(DriveConstants.kDriveKinematics);  // NORMAL forward movement

        TrajectoryConfig reverseConfig = new TrajectoryConfig(
            AutoConstants.kMaxSpeedMetersPerSecond,
            AutoConstants.kMaxAccelerationMetersPerSecondSquared
        ).setKinematics(DriveConstants.kDriveKinematics)
        .setReversed(true);  // ENABLED reverse mode



        // PIECE TWO RIGHT 
        Trajectory retrieveSecondPieceRobotRight = TrajectoryGenerator.generateTrajectory(
          new Pose2d(0, 0, new Rotation2d(Math.toRadians(60))),
          List.of(new Translation2d(0.18, -1.9)),
          new Pose2d(0.35, -4.15, new Rotation2d(Math.toRadians(115))),
          reverseConfig
        );

        
        Trajectory depositSecondPieceRobotRight = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(1, 0.25)),
            new Pose2d(2.25, 0.9, new Rotation2d(0)),
            forwardConfig
          );
        



        // PIECE TWO LEFT 
          
        Trajectory retrieveSecondPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(Math.toRadians(-60))),
            List.of(new Translation2d(0.18, 2.2)),
            new Pose2d(0.35, 4.35, new Rotation2d(Math.toRadians(-85))),
            reverseConfig
          );
          
        Trajectory depositSecondPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(1, -0.25)),///  CHANGE THE Y VALUE TO 0.3
            new Pose2d(2.25, -0.9, new Rotation2d(0)), // CHANGE 0.9 TO 0.6 
            forwardConfig
        );

        

        // PIECE THREE RIGHT 
        Trajectory retrieveThirdPieceRobotRight= TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(-1, -1)),
            new Pose2d(-3, -1, new Rotation2d(0)),
            reverseConfig
        );


        Trajectory depositThirdPieceRobotRight = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.8, 0.95)),
            new Pose2d(2.2, 1.5, new Rotation2d(0)),
            forwardConfig
        );




        // PIECE THREE LEFT 

        Trajectory retrieveThirdPieceRobotLeft= TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(-1, 1)),
            new Pose2d(-3, 1, new Rotation2d(0)),
            reverseConfig
        );


        Trajectory depositThirdPieceRobotLeft = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(0.8, 0.95)),
            new Pose2d(2.2, 1.5, new Rotation2d(0)),
            forwardConfig
        );




        // Create PID controllers for trajectory tracking
        PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
        PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
        ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints
        );
        thetaController.enableContinuousInput(-Math.PI, Math.PI);



        // COMMANDS 
        

        // SECOND PIECE RIGHT 
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
                return super.isFinished() || hasReachedFinalPose(depositSecondPieceRobotRight);
            }
        };


         // SECOND PIECE LEFT
         SwerveControllerCommand retrieveSecondPieceRobotLeftCmd = new SwerveControllerCommand(
            retrieveSecondPieceRobotLeft,
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
                return super.isFinished() || hasReachedFinalPose(retrieveSecondPieceRobotLeft);
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
                return super.isFinished() || hasReachedFinalPose(depositSecondPieceRobotLeft);
            }
        };

    




        // THIRD PIECE RIGHT
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
                return super.isFinished() || hasReachedFinalPose(depositThirdPieceRobotRight);
            }
        };


        
        // THIRD PIECE LEFT
        SwerveControllerCommand retrieveThirdPieceRobotLeftCmd = new SwerveControllerCommand(
            retrieveThirdPieceRobotLeft,
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
                return super.isFinished() || hasReachedFinalPose(retrieveThirdPieceRobotLeft);
            }
        };

        
        SwerveControllerCommand depositThirdPieceRobotLeftCmd = new SwerveControllerCommand(
            depositThirdPieceRobotLeft,
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
                return super.isFinished() || hasReachedFinalPose(depositThirdPieceRobotLeft);
            }
        };


  
        if(selectedPath.equals(pathConstants.twoPieceRetrieveRobotRight)){
            path =  retrieveSecondPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.twoPieceDepositRobotRight)){
            path =  depositSecondPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.threePieceRetrieveRobotRight)){
            path =  retrieveThirdPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.threePieceDepositRobotRight)){
            path =  depositThirdPieceRobotRightCmd;
        }else if(selectedPath.equals(pathConstants.twoPieceRetrieveRobotLeft)){
            path =  retrieveSecondPieceRobotLeftCmd;
        }else if(selectedPath.equals(pathConstants.twoPieceDepositRobotLeft)){
            path =  depositSecondPieceRobotLeftCmd;
        } else if(selectedPath.equals(pathConstants.threePieceRetrieveRobotLeft)){
            path =  retrieveThirdPieceRobotLeftCmd;
        } else if(selectedPath.equals(pathConstants.threePieceDepositRobotLeft)){
            path =  depositThirdPieceRobotLeftCmd;
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
        boolean angleReached = Math.abs(currentPose.getRotation().getDegrees() - finalPose.getRotation().getDegrees()) < 5;

        return positionReached && angleReached;
    }

    /**
     * Gets the initial pose for a given trajectory path.
     */
    private Pose2d getInitialPoseForPath(String selectedPath) {
        return new Pose2d(0, 0, new Rotation2d(0));
    }
}