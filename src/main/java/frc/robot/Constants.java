  // Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  public static class DriverControllerConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.05;
  }

  public static class OperatorControllerConstants {
    public static final int kOperatorControllerPort = 1;
  }

  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.8;
    public static final double kSlowSpeedMetersPerSecond = 2;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(24.5);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(24.5);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 1;
    public static final int kRearLeftDrivingCanId = 3;
    public static final int kFrontRightDrivingCanId = 7;
    public static final int kRearRightDrivingCanId = 5;

    public static final int kFrontLeftTurningCanId = 2;
    public static final int kRearLeftTurningCanId = 4;
    public static final int kFrontRightTurningCanId = 8;
    public static final int kRearRightTurningCanId = 6;

    public static final boolean kGyroReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T,
    // 13T, or 14T. This changes the drive speed of the module (a pinion gear with
    // more teeth will result in a robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 13;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.0762;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15
    // teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;
  }

  public static final class ElevatorConstants {

    public static final int kElevator1CanId = 50;
    public static final int kElevator2CanId = 51;

    public static final double kHome = 0;
    public static final double kFeederStation = 19; //14
    public static final double kTravel = 0;

    public static final double kLevel1 = 0;
    public static final double kLevel2 = 0;
    ; //16
    public static final double kLevel3 = 0; // 5
    public static final double kLevel4 = 67; //88


  }

  public static final class ArmConstants {

    public static final int kArmCanId = 55;

    public static final double kHome = 0.9; //-10
    public static final double kFeederStation = 0.2; // -4
    public static final double kClimbPosition = 0;

    public static final double kLevel1 = 0;
    public static final double kLevel2 = 2.6; //-7
    public static final double kLevel3 = 3.35;
    public static final double kLevel4 = 3.2; //84


  }

  public static final class ClimbConstants {

    public static final int kClimbCanId = 57;

    public static final double kclimbOut = 105
    ; 
    public static final double kclimbIn= 230;
    public static final double kHome=0;
    


  }

  public static final class CoralIntakeConstants {

    public static final int kCoralIntakeCanId = 60;

    public static final double kCoralIntakeSpeed = 1;
    public static final double kCoralOutakeSpeed = -1;
    public static final double kCoralNoSpeed = 0.085;


    public static final int intakeSwitchPort = 0; 
  }


  public static class VisionConstants {

    // coral alignment points 
    public static final double leftCamTagX = 9.5;
    public static final double leftCamTagY = -0.7;

    public static final double rightCamTagX = -7.8;
    public static final double rightCamTagY = -1.18;

    // coral tolerances 
    public static final double xTol = 0.5; 
    public static final double yTol = 0.5;
    
    
    //pid stuff
    public static final double driveAlignKp = 0.075; 
    public static final double driveAlignKi = 0;
    public static final double driveAlignKd =0;
    
    public static final double strafeAlignKp = 0.035;
    public static final double strafeAlignKi = 0;
    public static final double strafeAlignKd = 0.0015;

    public static final double rotAlignKp = 0.01; 
    public static final double rotAlignKi = 0;
    public static final double rotAlignKd = 0.001;

    public static final int aprilTagPipeline = 0; 
  }

  
  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 5;
    public static final double kMaxAccelerationMetersPerSecondSquared = 4;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 0.25;
    public static final double kPYController = 0.25;
    public static final double kPThetaController = 0.95;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);


    public static final String autoMode = "auto"; 
    public static final String teleMode = "tele-op"; 
  }

  public static class photonVisionConstants{
    // CAMERA NAME 
    public static String tagCameraName= "feedercamera";


    public static final double FeederCamTagYaw = -15;
    public static final double FeederCamTagPitch = 43;


    public static final double xTol = 0.5; 
    public static final double yTol = 0.5; 
  }

  public static class pathConstants{
    public static String onePieceDepositRobotRight = "depositFirstPieceRobotRight"; 
    public static String onePieceDepositRobotLeft = "depositFirstPieceRobotLeft";

    public static String twoPieceDepositRobotRight = "depositSecondPieceRobotRight"; 
    public static String twoPieceDepositRobotLeft = "depositSecondPieceRobotLeft";

    public static String twoPieceRetrieveRobotRight = "retrieveSecondPieceRobotRight"; 
    public static String twoPieceRetrieveRobotLeft = "retrieveSecondPieceRobotLeft";

    public static String threePieceDepositRobotRight = "depositThreePieceRobotRight"; 
    public static String threePieceDepositRobotLeft = "depositThreePieceRobotLeft";

    public static String threePieceRetrieveRobotRight = "retrieveThreePieceRobotRight"; 
    public static String threePieceRetrieveRobotLeft = "retrieveThreePieceRobotLeft";
  }

  public static class LedConstants {

    public static final int ledPort = 0; 
  
    public static final int ledLength = 60; 
      
    // INDIVIDUAL COLOUR CODES 
    public static final int[] greenColourCode = {0, 255, 0}; 
    public static final int[] blueColourCode = {0, 0, 255}; 
    public static final int[] redColourCode = {255, 0, 0}; 
    public static final int[] orangeColourCode = {255, 25, 0}; 
    public static final int[] whiteColourCode = {255, 125, 50}; 
    public static final int[] vermillionColourCode = {255, 255, 255}; 
    public static final int[] purpleColourCode = {200, 0, 200}; 
    public static final int[] yellowColourCode = {200, 150, 0}; 
  }

  public class StatusVariables{
    public static boolean isLinedUpToReef;
    public static boolean hasCoral;  
  }




}
