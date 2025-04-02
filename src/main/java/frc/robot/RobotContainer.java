// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.Constants.AlgaeIntakeConstants;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.CoralSystemContants;
import frc.robot.Constants.DriverControllerConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.OperatorControllerConstants;
import frc.robot.Constants.StatusVariables;
import frc.robot.Constants.pathConstants;
import frc.robot.Constants.photonVisionConstants;
import frc.robot.commands.AlgaeIntakeCmd;
import frc.robot.commands.MoveClimbManually;
import frc.robot.commands.MoveClimbToSetpoint;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.commands.moveCoralSystemToPosition;
import frc.robot.commands.armCommands.MoveArmToSetpoint;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotCentre;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotLeft;
import frc.robot.commands.auto.onePieceAutos.onePieceRobotRight;
import frc.robot.commands.auto.twoPieceAutos.twoPieceRobotLeft;
import frc.robot.commands.auto.twoPieceAutos.twoPieceRobotRight;
import frc.robot.commands.autoBlocks.autoAlignmentToFeeder;
import frc.robot.commands.autoBlocks.autoAlignmentToReef;
import frc.robot.commands.autoBlocks.autoScoreCoral;
import frc.robot.commands.coralIntakeCommands.CoralIntakeCmd;
import frc.robot.commands.coralIntakeCommands.CoralIntakeForTimeCmd;
import frc.robot.commands.coralIntakeCommands.CoralIntakeSensorCmd;
import frc.robot.commands.driveCommands.DriveDistanceAtRobotAngleCmd;
import frc.robot.commands.driveCommands.DriveDistanceCmd;
import frc.robot.commands.driveCommands.OdometryCmd;
import frc.robot.commands.driveCommands.TurnToAngleCommand;
import frc.robot.commands.photonCommands.AlignXandYWithPhoton;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.VisionSubsystem;



import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  
  // Call subsystems
  private final DriveSubsystem s_driveSubsystem = new DriveSubsystem();
  private final CoralIntakeSubsystem s_CoralIntakeSubsystem = new CoralIntakeSubsystem();
  private final ArmSubsystem s_ArmSubsystem = new ArmSubsystem();
  private final ElevatorSubsystem s_ElevatorSubsystem = new ElevatorSubsystem();
  private final VisionSubsystem s_VisionSubsystem = new VisionSubsystem();
  private final ClimbSubsystem climb = new ClimbSubsystem();
  private final PhotonSubsystem photon = new PhotonSubsystem();
  private final AlgaeIntakeSubsystem algae = new AlgaeIntakeSubsystem();

  //Autos
  private final Command onePieceCentre=new onePieceRobotCentre(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "right");
  private final Command onePieceRight = new onePieceRobotRight(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "right");
  private final Command onePieceLeft = new onePieceRobotLeft(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "right");
  private final Command twoPieceLeft = new twoPieceRobotLeft(s_driveSubsystem, s_VisionSubsystem, photon, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem);
  private final Command twoPieceRight = new twoPieceRobotRight(s_driveSubsystem, s_VisionSubsystem, photon, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem);
  SendableChooser<Command> m_autoChooser = new SendableChooser<>(); 



  // Setup Driver Controller
  private final CommandXboxController m_driverController =
      new CommandXboxController(DriverControllerConstants.kDriverControllerPort);

  // Setup Operator Controller
  private final CommandXboxController m_operatorController = 
      new CommandXboxController(OperatorControllerConstants.kOperatorControllerPort);

  private final CommandXboxController m_devController = 
      new CommandXboxController(5);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

   // m_autoChooser.setDefaultOption("BLUE-ONE-CENTRE", onePieceCentreBlue);
    m_autoChooser.setDefaultOption("ONE-PIECE-CENTRE", onePieceCentre);
    m_autoChooser.addOption("ONE-PIECE-CENTRE", onePieceCentre);
    m_autoChooser.addOption("ONE-PIECE-RIGHT", onePieceRight);
    m_autoChooser.addOption("ONE-PIECE-LEFT", onePieceLeft);
    m_autoChooser.addOption("TWO-PIECE-RIGHT", twoPieceRight);
    m_autoChooser.addOption("TWO-PIECE-LEFT", twoPieceLeft);
  
    Shuffleboard.getTab("Autonomous").add(m_autoChooser); 




    // Configure the trigger bindings
    configureBindings();

    defaultCommands(); 

    s_driveSubsystem.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> s_driveSubsystem.drive(
                -MathUtil.applyDeadband(-m_driverController.getLeftY(), DriverControllerConstants.kDriveDeadband),
                -MathUtil.applyDeadband(-m_driverController.getLeftX(), DriverControllerConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getRightX(), DriverControllerConstants.kDriveDeadband),
                true),
            s_driveSubsystem));

     
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */

  private void defaultCommands(){
    SmartDashboard.putNumber("Current Coral", Globals.currentPos);
  }

  private void configureBindings() {
    
    //________________________________________DRIVER BUTTONS____________________________________________________________//

    // INTAKE
    m_driverController.leftBumper().onTrue(
      // new CoralIntakeSensorCmd(s_CoralIntakeSubsystem)
     new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralIntakeSpeed)
    );

    m_driverController.leftBumper().onFalse(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralNoSpeed)
    );

    // OUTAKE
    m_driverController.rightBumper().onTrue(
    new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralSlowOutakeSpeed)
    );

    m_driverController.rightBumper().onFalse(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralNoSpeed)
    );

    // m_driverController.rightTrigger().onTrue(
    //   new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralSlowOutakeSpeed)
    //   );

    //   m_driverController.rightTrigger().onFalse(
    //     new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralNoSpeed)
    //     );

        // m_driverController.x().onTrue( new CoralIntakeForTimeCmd(s_CoralIntakeSubsystem,CoralIntakeConstants.kCoralAutoOutake , 1000));
        // m_driverController.x().onFalse( new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralNoSpeed));
  


     // RESET BUTTONS 
    m_driverController.povUp().onTrue(
      new SequentialCommandGroup(
        new InstantCommand(() -> s_driveSubsystem.zeroHeading()),
        new InstantCommand(() -> s_driveSubsystem.adjustGyroToAngle(180))
      )
      );

    
    m_driverController.leftTrigger().onTrue(
      //new autoAlignmentToReef(s_driveSubsystem, s_VisionSubsystem, "left", false, AutoConstants.autoMode)
      // new autoScoreCoral(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "left", AutoConstants.autoMode) 
      // new autoAlignmentToFeeder(s_driveSubsystem, photon, false, "left")
    new AlgaeIntakeCmd(algae, AlgaeIntakeConstants.kAlgaeIntakeSpeed)
    ); 

        
    m_driverController.leftTrigger().onFalse(
     
      // new autoAlignmentToFeeder(s_driveSubsystem, photon, true, "left")
      // new autoAlignmentToReef(s_driveSubsystem, s_VisionSubsystem, "left", true, AutoConstants.autoMode)
      new AlgaeIntakeCmd(algae, AlgaeIntakeConstants.kAlgaenoSpeed)
    ); 

        
    m_driverController.rightTrigger().onTrue(
      // new autoAlignmentToFeeder(s_driveSubsystem, photon, false, "right")
      //new autoAlignmentToReef(s_driveSubsystem, s_VisionSubsystem, "right", false, AutoConstants.autoMode)
      // new autoScoreCoral(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "right", AutoConstants.autoMode)
      new AlgaeIntakeCmd(algae, AlgaeIntakeConstants.kAlgaeOutakeSpeed)
    ); 

        
    m_driverController.rightTrigger().onFalse(
      // new autoAlignmentToFeeder(s_driveSubsystem, photon, true, "right")
      //new autoAlignmentToReef(s_driveSubsystem, s_VisionSubsystem, "right", true, AutoConstants.autoMode)
      new AlgaeIntakeCmd(algae, AlgaeIntakeConstants.kAlgaenoSpeed)
    ); 


    m_driverController.x().onTrue(
      new autoScoreCoral(s_driveSubsystem, s_VisionSubsystem, s_ElevatorSubsystem, s_ArmSubsystem, s_CoralIntakeSubsystem, "right", AutoConstants.autoMode)
     //  new OdometryCmd(s_driveSubsystem, pathConstants.twoPieceDepositRobotLeft)
      // .andThen(          new DriveDistanceCmd(s_driveSubsystem, 0.3, -1.5, false, 5000))
    );

      

   


    //_______________________________________OPERATOR BUTTONS___________________________________________________________//

    // GO TO LEVEL 4
    //GO TO HOME
    m_operatorController.rightBumper().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.HOME)
    ); 

    m_operatorController.b().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.FEEDER)
    ); 

    // m_operatorController.x().onTrue(
    //   new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.L1)
    // ); 

    m_operatorController.a().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.L2)
    ); 


    m_operatorController.x().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.L3)
    ); 

    m_operatorController.y().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.L4)
    ); 

    // m_operatorController.povRight().onTrue(
    //   new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, "CLIMB")
    // ); 


    m_operatorController.rightTrigger().onTrue(
      new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.NET)
    ); 


    //GO TO CLIMB IN
    m_operatorController.leftBumper().onTrue(
      new SequentialCommandGroup(
      //   new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kHome),
      //   new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kLevelClimb),
      //  new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kLevel3),
      //  new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kClimbHigh),
       new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.CLIMB),
       new MoveClimbToSetpoint(climb, ClimbConstants.kclimbOut)

      //  new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kLevelClimb)
      )
    );

    //GO TO CLIMB OUT
    m_operatorController.leftTrigger().onTrue(
      new SequentialCommandGroup(
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kClimbHigh),
        new MoveClimbToSetpoint(climb, ClimbConstants.kclimbUp)

      
      )
    ); 

    //GO TO CLIMB OUT again

m_operatorController.povUp().onTrue(
  new MoveClimbManually(climb, ClimbConstants.climbDownSpeed)
);

m_operatorController.povUp().onFalse(
  new MoveClimbManually(climb, ClimbConstants.noSpeed)
);
   //HOME
    m_operatorController.povDown().onTrue(
      new MoveClimbManually(climb, ClimbConstants.climbUpSpeed)

    );

    m_operatorController.povDown().onFalse(
      new MoveClimbManually(climb, ClimbConstants.noSpeed)

      );

      m_operatorController.rightStick().onTrue(
       
          new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.A1)
         
        
        // new SequentialCommandGroup(
        //   new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kAlgaeLow),
        //   new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kAlgaeLow)
        // )
      );

      m_operatorController.leftStick().onTrue(
        // new SequentialCommandGroup(
        //   new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kAlgaeHigh),
        //   new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kAlgaeHigh)
        // )
        new moveCoralSystemToPosition(s_ArmSubsystem, s_ElevatorSubsystem, CoralSystemContants.A2)
      );

  
    

    m_devController.a().onTrue(
      new SequentialCommandGroup(
        new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.65, 1.35, -25, false, 5000), 
        new autoAlignmentToReef(s_driveSubsystem, s_VisionSubsystem, "left", false, AutoConstants.autoMode), 
        Commands.waitSeconds(3), 
        new DriveDistanceCmd(s_driveSubsystem, 0.2, -0.2, false, 2000), 
        new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.65, 2.5, 155, false, 5000), 

        new ParallelDeadlineGroup(
          new CoralIntakeSensorCmd(s_CoralIntakeSubsystem), 
          new DriveDistanceCmd(s_driveSubsystem, 0.35, -3, false, 5000)
        )

      )
    ); 
    m_devController.x().onTrue(

      new SequentialCommandGroup(
        new TurnToAngleCommand(s_driveSubsystem, -126), 
        new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.8, 4, 155, false, 10000),
        new ParallelDeadlineGroup(
          new CoralIntakeSensorCmd(s_CoralIntakeSubsystem), 
          new DriveDistanceCmd(s_driveSubsystem, 0.35, -3, false, 5000)
        )
      )


     // new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.25, 3, 25, false, 5000)
    ); 

    m_devController.b().onTrue(
      new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.85, 2, -20, false, 5000)
      //new DriveDistanceAtRobotAngleCmd(s_driveSubsystem, 0.25, 0.5, -25, false, 5000)
      // new TurnToAngleCommand(s_driveSubsystem, 60)
    ); 








  }



  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomoubo
   */
  public Command getAutonomousCommand() {
    return m_autoChooser.getSelected(); }

}
