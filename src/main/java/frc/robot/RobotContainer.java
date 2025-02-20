// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.CoralIntakeConstants;
import frc.robot.Constants.DriverControllerConstants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.OperatorControllerConstants;
import frc.robot.commands.CoralIntakeCmd;
import frc.robot.commands.MoveArmToSetpoint;
import frc.robot.commands.MoveElevatorToSetpoint;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem s_driveSubsystem = new DriveSubsystem();
  private final CoralIntakeSubsystem s_CoralIntakeSubsystem = new CoralIntakeSubsystem();
  private final ArmSubsystem s_ArmSubsystem = new ArmSubsystem();
  private final ElevatorSubsystem s_ElevatorSubsystem = new ElevatorSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(DriverControllerConstants.kDriverControllerPort);

  private final CommandXboxController m_operatorController = 
      new CommandXboxController(OperatorControllerConstants.kOperatorControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    s_driveSubsystem.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> s_driveSubsystem.drive(
                -MathUtil.applyDeadband(m_driverController.getLeftY(), DriverControllerConstants.kDriveDeadband),
                -MathUtil.applyDeadband(m_driverController.getLeftX(), DriverControllerConstants.kDriveDeadband),
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
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
   
    // CORAL INTAKE BUTTONS

    m_driverController.leftBumper().whileTrue(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, CoralIntakeConstants.kCoralIntakeSpeed)
    );

    m_driverController.leftBumper().whileFalse(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, 0)
    );

    m_driverController.rightBumper().whileTrue(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, -(CoralIntakeConstants.kCoralIntakeSpeed))
    );

    m_driverController.rightBumper().whileFalse(
      new CoralIntakeCmd(s_CoralIntakeSubsystem, 0)
    );


    m_operatorController.x().onTrue(

      new ParallelCommandGroup(
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kLevel4),
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kLevel4)
      )

    );

    m_operatorController.a().onTrue(

      new SequentialCommandGroup(
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kFeederStation),
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kFeederStation)
      )

    );

    m_operatorController.y().onTrue(

      new SequentialCommandGroup(
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kFeederStation),
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kLevel3)
      )

    );

    m_operatorController.b().onTrue(

      new SequentialCommandGroup(
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kLevel2),
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kLevel2)
      )

    );

    m_operatorController.rightBumper().onTrue(

      new SequentialCommandGroup(
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.kFeederStation),
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.kLevel1)
      )

    );

    m_operatorController.leftBumper().onTrue(

      new SequentialCommandGroup(
        new MoveElevatorToSetpoint(s_ElevatorSubsystem, ElevatorConstants.ktest),
        new MoveArmToSetpoint(s_ArmSubsystem, ArmConstants.ktest)
      )

    );

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }

}
